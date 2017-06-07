package com.rupeng.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

//班级成员管理
@Controller
@RequestMapping("/classesUser")
public class ClassesUserController {
	@Autowired
	private ClassesUserService classesUserService;//操作中间表
	@Autowired
	private UserService userService;
	/**
     * 由于前台用户数量太多，所以不能像其他关联关系一样把所有行列出来
     */
	@RequestMapping("/update.do")
	public ModelAndView update(Long classesId){
		// 把classesId关联的User查询出来
		List<User> users = classesUserService.selectSecondListByFirstId(classesId);
		ModelAndView modelAndView = new ModelAndView("classesUser/update");
		modelAndView.addObject("classesId", classesId);
		modelAndView.addObject("users", users);
		return modelAndView;
	}
	
   /**
    * 添加关联关系,用户列表页(查找要添加关系的用户)
    */
   @RequestMapping(value = "/add.do", method = RequestMethod.GET)
   public ModelAndView addPage(Long classesId,Integer curr,String param) {
	   if(curr==null){//首次访问此页默认查看第一页
		   curr=1;
	   }
		   
	   if(!CommonUtils.isEmpty(param)){
		   param = "%"+param+"%";
	   }else{
		   param= null;//当获取的param的值为""时要设置为null,否则sql语句模糊查询会出错
	   }
	   Map<String, Object> params = new HashMap<>();
	   params.put("param", param);
	   
	   PageInfo<User> pageInfo = userService.search(curr, 10, params);
	   
       ModelAndView modelAndView = new ModelAndView("classesUser/add");
       modelAndView.addObject("classesId", classesId);
       modelAndView.addObject("pageInfo", pageInfo);

       return modelAndView;
   }
   /**
    * 添加关联关系,添加到班级
    */
   @RequestMapping(value = "/add.do", method = RequestMethod.POST)
   public @ResponseBody AjaxResult addSubmit(Long classesId,Long userId) {
	   //学生和老师的添加原则不同
	   //一个学生只能在一个班级中，而老师可以在不同的班级中
	   //一个老师可以在多个班级中，但同一个班级只能添加一次

	   ClassesUser classesUser = new ClassesUser();
	   User user = userService.selectOne(userId);
	   classesUser.setUserId(userId);
	   if(!user.getIsTeacher()){//学生
		   if(classesUserService.isExisted(classesUser)){
			   return AjaxResult.errorInstance("此学生已经在其他班级中");
		   }
	   }else{//老师
		   classesUser.setClassesId(classesId);
		   if(classesUserService.isExisted(classesUser)){
			   return AjaxResult.errorInstance("此老师已经在本班级中");
		   }
	   }
	   
	   classesUser.setClassesId(classesId);
       classesUserService.insert(classesUser);
       return AjaxResult.successInstance("添加成功！");
   }
	
	/**
	 * 删除关联关系(可能存在并发访问时的数据不可重复读和幻读问题,可以使用加锁或事务)
	 */
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long classesId,Long userId){
		ClassesUser classesUser = new ClassesUser(classesId, userId);
		classesUser = classesUserService.selectOne(classesUser);
		classesUserService.delete(classesUser.getId());
		return AjaxResult.successInstance("删除成功");
	}
}
