package com.rupeng.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.User;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	//处理时间解析格式
	@InitBinder
	protected void InitBinder(WebDataBinder binder){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//参数true表示允许日期为空（null、""）
		binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
	}
	//分页  条件查询
	@RequestMapping("/list.do")
	public ModelAndView list(Integer curr,Date beginTime,Date endTime,String param){
		if(curr==null){
			curr=1;
		}
		if(endTime!=null){//注意:查询条件的时间格式是yyyy-MM-dd,但数据库中用户创建时间还包括时分秒,这里的endTime范围应该包括当天的0:00:00-23:59:59...
			endTime.setTime(endTime.getTime()+1000L*60*60*24-1);
		}
		if(!CommonUtils.isEmpty(param)){
			param = "%"+param+"%";
		}else{
			param= null;//当获取的param的值为""时要设置为null,否则sql语句模糊查询会出错
		}
		Map<String, Object> params = new HashMap<>();
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		params.put("param", param);
		
		PageInfo<User> pageInfo = userService.search(curr,10,params);
		return new ModelAndView("user/list", "pageInfo", pageInfo);
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long id){
		User user = userService.selectOne(id);
		
		return new ModelAndView("user/update","user",user);
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(User user){
		 if (CommonUtils.isEmpty(user.getEmail())) {
	            return AjaxResult.successInstance("邮箱不能为空");
	        }
	        if (!CommonUtils.isEmail(user.getEmail())) {
	            return AjaxResult.successInstance("邮箱格式不正确");
	        }

	        User oldUser = userService.selectOne(user.getId());
	        oldUser.setName(user.getName());
	        oldUser.setEmail(user.getEmail());
	        oldUser.setIsMale(user.getIsMale());
	        oldUser.setPhone(user.getPhone());
	        oldUser.setSchool(user.getSchool());

	        userService.update(oldUser);

	        return AjaxResult.successInstance("修改用户成功！");
	}
	
	@RequestMapping(value="/toggleTeacher.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult toggleTeacher(Long id){
		User user = userService.selectOne(id);
		boolean setTeacher = !user.getIsTeacher();
		user.setIsTeacher(setTeacher);
		userService.update(user);
		return AjaxResult.successInstance(setTeacher?"设置为老师成功":"取消设置为老师成功");
	}
}
