package com.rupeng.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Classes;
import com.rupeng.service.ClassesService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/classes")
public class ClassesController {
	@Autowired
	private ClassesService classesService;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		ModelAndView modelAndView = new ModelAndView("classes/list","classesList",classesService.selectList());
		modelAndView.addObject("subjectList",subjectService.selectList());
		return modelAndView;
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		ModelAndView modelAndView = new ModelAndView("classes/add");
		modelAndView.addObject("subjectList",subjectService.selectList());
		return modelAndView;
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Classes classes){
		if (CommonUtils.isEmpty(classes.getName())) {
			return AjaxResult.errorInstance("班级名不能为空");
		}
		
		if (classesService.isExisted(classes)) {
			return AjaxResult.errorInstance("班级已经存在");
		}
		
		classesService.insert(classes);
		return AjaxResult.successInstance("班级添加成功");
	}
	
	@RequestMapping(value = "/update.do", method = RequestMethod.GET)
    public ModelAndView updatePage(Long id) {

        ModelAndView modelAndView = new ModelAndView("classes/update");
        modelAndView.addObject("subjectList", subjectService.selectList());
        modelAndView.addObject("classes", classesService.selectOne(id));

        return modelAndView;
    }

    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public @ResponseBody AjaxResult updateSubmit(Classes classes) {

        if (CommonUtils.isEmpty(classes.getName())) {
            return AjaxResult.errorInstance("班级名称不能为空");
        }

        Classes oldClasses = classesService.selectOne(classes.getId());
        oldClasses.setName(classes.getName());
        oldClasses.setSubjectId(classes.getSubjectId());
        classesService.update(oldClasses);

        return AjaxResult.successInstance("修改成功！");
    }
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		classesService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
