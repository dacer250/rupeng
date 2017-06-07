package com.rupeng.web.aop;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.rupeng.annotation.RupengCacheable;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;
import com.rupeng.util.ReflectUtils;

/**
 *  适合缓存的数据的特点（并不是绝对的）：
 *  1 很少更新的数据，如Subject、Card、Classes等
 *  2 经常被使用的数据
 *  3 数据量不大的数据
 *  
 *  redis缓存操作：
 *  1 把查询到的数据存放到缓存中
 *  2 从缓存中查询数据
 *  3 把缓存数据清空
 *  
 *  redis缓存结构：
 *  key的格式为 cache_全类名_方法签名_方法参数值列表，这样设计的目的是删除时可以使用 cache_全类名_* 删掉此类相关的所有缓存
 *  value是json格式的字符串数据
 *  同时指定key的过期时间(及时清除不经常使用的数据;减少数据库管理员直接更改数据库数据(前台系统并不知道数据库数据被更改)带来的危害时间)
 *
 *  使用三个注解 灵活控制哪些类的哪些方法需要哪些缓存操作：
 *  1 只有使用@RupengCacheable标注的类才支持缓存（作用是可灵活控制哪些service类需要缓存功能）
 *  2 只有使用@RupengUseCache标注的方法才支持查询缓存和存入缓存（作用是可灵活控制哪些方法可以从缓存中取得数据，以及把数据存入缓存）
 *  3 只有使用@RupengClearCache标注的方法才支持清空缓存（作用是可灵活控制哪些方法执行后会清空缓存）
 *  这三个注解配合使用即可满足我们对缓存的绝大部分要求
 *
 *   使用原则：
 *   1 只要是会更新数据的操作都要清理缓存
 *   2 由于表之间的关联关系，相关联的表进行更新操作时，就可能造成缓存中的数据和数据库中不一致，这时可以通过给缓存数据设置过期时间来解决。至于过期时间的长短由数据不一致可接受时间决定，比如60秒、10秒等，可以在@RupengCacheable中设置
 *   3 由于需要支持过期时间，就需要每条缓存使用一个独立的key，为了更新操作时方便删除一个表的全部缓存，就需要保证同一个类的缓存的key的前缀一致，比如cache_全类名_方法签名_方法参数值
 *   4 查询方法的返回值如果不能转换为json格式的字符串则不能使用缓存，如public Map<Chapter, List<Segment>> selectAllCourse(Long cardId)
 *
 *
 *   注意：同一个类中的一个方法调用另一个方法时，另一个方法不会被增强（对于查询方法，没被增强也没关系
 */

//service方法增强(增加缓存,清除缓存)
@Aspect
public class CacheAspect {
	private static final Logger logger = LogManager.getLogger(CacheAspect.class);
	
	@Pointcut("@target(com.rupeng.annotation.RupengCacheable)")
	private void cacheable(){
		
	}
	@Pointcut("@annotation(com.rupeng.annotation.RupengUseCache)")
	private void useCache(){
		
	}
	@Pointcut("@annotation(com.rupeng.annotation.RupengClearCache)")
	private void clearCache(){
		
	}
	
	@SuppressWarnings("all")
	@Around("cacheable() && useCache()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		//1 尝试从缓存里面获取数据，如果获取到，直接返回获取到的数据
		//key的格式为 cache_全类名_方法签名_方法参数值列表
		Class targetClass = joinPoint.getTarget().getClass();
		Method targetMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
		Object[] args = joinPoint.getArgs();
		String argsStr = JsonUtils.toJson(args);
		if (!CommonUtils.isEmpty(argsStr)) {
			argsStr = argsStr.replaceAll(":", "_");//redis中":"有特殊含义,相当于目录分隔符,需要替换为其他字符
		}
		
		StringBuilder key = new StringBuilder();
		key.append("cache_");
		key.append(targetClass.getName()).append("_");
		key.append(targetMethod.getName()).append("_");
		key.append(argsStr);
		
		String keyStr = key.toString();
		
		String valueStr = JedisUtils.get(keyStr);
		
		if (!CommonUtils.isEmpty(valueStr)) {
			Class returnType = ReflectUtils.getActualReturnType(targetClass, targetMethod);
			Class[] parametricTypes = ReflectUtils.getActualParametricTypeOfReturnType(targetClass, targetMethod);
			
			logger.debug("从缓存中获取到数据:{}",valueStr);
			return JsonUtils.toBean(valueStr, returnType, parametricTypes);
		}
		
		//2 如果没有获取到，就执行原方法从数据库里面查询数据
		Object returnObject  = joinPoint.proceed();
		
		//3 把从数据库查询到的数据存到缓存中一份
		RupengCacheable rupengCacheable = (RupengCacheable) targetClass.getAnnotation(RupengCacheable.class);
		valueStr = JsonUtils.toJson(returnObject);
		JedisUtils.setex(keyStr, rupengCacheable.expire(), valueStr);
		
		logger.debug("把从数据库中查询到的数据存入缓存:{}",valueStr);
		return returnObject ;
	}
	
	@SuppressWarnings("all")
	@After("cacheable()&&clearCache()")
	public void after(JoinPoint joinPoint){
		//清空目标类的所有缓存,keyPattern为 cache_全类名_*  
		Class targetClass = joinPoint.getTarget().getClass();
		
		StringBuilder key = new StringBuilder();
		key.append("cache_");
		key.append(targetClass.getName()).append("_*");
		
		String keyPattern = key.toString();
		JedisUtils.del(keyPattern);//删除符合正则表达式的key
		
		logger.debug("从缓存中清除数据,keyPattern:{}",keyPattern);
	}
}
