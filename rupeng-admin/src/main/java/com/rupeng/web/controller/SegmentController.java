package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Segment;
import com.rupeng.service.SegmentService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/segment")
public class SegmentController {
	@Autowired
	private SegmentService segmentService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(Long chapterId){
		Segment segment = new Segment();
		segment.setChapterId(chapterId);
		List<Segment> segments = segmentService.selectList(segment, "seqNum asc");
		
		ModelAndView modelAndView = new ModelAndView("segment/list","segments",segments);
		modelAndView.addObject("chapterId", chapterId);
		return modelAndView;
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(Long chapterId){
		return new ModelAndView("segment/add","chapterId",chapterId);
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Segment segment){
		if(CommonUtils.isEmpty(segment.getName())||segment.getSeqNum()==null){
			return AjaxResult.errorInstance("段落名,序号不能为空");
		}
		Segment param = new Segment();
		param.setName(segment.getName());
		if(segmentService.isExisted(param)){
			return AjaxResult.errorInstance("段落名已经存在");
		}
		
		segmentService.insert(segment);
		return AjaxResult.successInstance("添加成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long id){
		return new ModelAndView("segment/update","segment",segmentService.selectOne(id));
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Segment segment){
		if(CommonUtils.isEmpty(segment.getName())||segment.getSeqNum()==null){
			return AjaxResult.errorInstance("段落名,序号不能为空");
		}
		Segment param = new Segment();
		param.setName(segment.getName());
		param = segmentService.selectOne(param);
		if(param!=null&&param.getId()!=segment.getId()){
			return AjaxResult.errorInstance("段落名已经存在");
		}
		
		segmentService.update(segment);
		return AjaxResult.successInstance("修改成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		segmentService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
