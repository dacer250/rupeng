package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Subject;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		List<Subject> subjects = subjectService.selectList();
		ModelAndView modelAndView = new ModelAndView("subject/list");
		modelAndView.addObject("subjects", subjects);
		return modelAndView;
	}
	
	@RequestMapping(value = "/add.do",method = RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("subject/add");
	}
	
	@RequestMapping(value = "/add.do",method = RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(String name){
		//数据有效性检查
		if(CommonUtils.isEmpty(name)){
			return AjaxResult.errorInstance("学科名不能为空");
		}
		
		//学科名唯一性检查
		Subject subject = new Subject();
		subject.setName(name);
		
		if (subjectService.isExisted(subject)) {
			return AjaxResult.errorInstance("学科名已存在");
		}
		
		//执行业务逻辑,添加学科
		subjectService.insert(subject);
		
		return AjaxResult.successInstance("学科添加成功");
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(long id){
		Subject subject = subjectService.selectOne(id);
		ModelAndView modelAndView = new ModelAndView("subject/update");
		modelAndView.addObject("subject", subject);
		return modelAndView;
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(long id,String name){
		//数据有效性检查
		if(CommonUtils.isEmpty(name)){
			return AjaxResult.errorInstance("学科名不能为空");
		}
		
		//学科名唯一性检查:name不能修改为其他已经存在的学科名
		Subject subject = new Subject();
		subject.setName(name);
		subject = subjectService.selectOne(subject);//根据学科名查询
			//如果能查到并且其id与当前id不一致,说明是其他已经存在的学科名
		if (subject!=null&&subject.getId()!=id) {
			return AjaxResult.errorInstance("学科名已存在");
		}
		
		//执行业务逻辑,修改学科
		//(可能有的字段是不能修改的,所以先用id查出旧数据,再把可以修改的字段进行修改)
		subject = subjectService.selectOne(id);
		subject.setName(name);
		
		subjectService.update(subject);
		
		return AjaxResult.successInstance("学科修改成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(long id){
		subjectService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
