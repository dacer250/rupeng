package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Chapter;
import com.rupeng.service.ChapterService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/chapter")
public class ChapterController {

	@Autowired
	private ChapterService chapterService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(Long cardId){
		Chapter chapter = new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> chapters = chapterService.selectList(chapter, "seqNum asc");
		
		ModelAndView modelAndView = new ModelAndView("chapter/list","chapters",chapters);
		modelAndView.addObject("cardId", cardId);
		return modelAndView;
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(Long cardId){
		return new ModelAndView("chapter/add","cardId",cardId);
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Chapter chapter){
		if(CommonUtils.isEmpty(chapter.getName())||chapter.getSeqNum()==null){
			return AjaxResult.errorInstance("章节名,序号不能为空");
		}
		Chapter param = new Chapter();
		param.setName(chapter.getName());
		if(chapterService.isExisted(param)){
			return AjaxResult.errorInstance("章节名已经存在");
		}
		
		chapterService.insert(chapter);
		return AjaxResult.successInstance("添加成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long id){
		return new ModelAndView("chapter/update","chapter",chapterService.selectOne(id));
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Chapter chapter){
		if(CommonUtils.isEmpty(chapter.getName())||chapter.getSeqNum()==null){
			return AjaxResult.errorInstance("章节名,序号不能为空");
		}
		Chapter param = new Chapter();
		param.setName(chapter.getName());
		param = chapterService.selectOne(param);
		if(param!=null&&param.getId()!=chapter.getId()){
			return AjaxResult.errorInstance("章节名已经存在");
		}
		
		chapterService.update(chapter);
		return AjaxResult.successInstance("修改成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		chapterService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
}
