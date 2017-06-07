package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/card")
public class CardController {
	@Autowired
	private CardService cardService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CardSubjectService cardSubjectService;
	
	@RequestMapping("/list.do")
	public ModelAndView list(){
		return new ModelAndView("card/list","cards",cardService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("card/add","subjects",subjectService.selectList());
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult addSubmit(Card card,Long[] subjectIds){
		if(CommonUtils.isEmpty(card.getName())){
			return AjaxResult.errorInstance("学习卡名不能为空");
		}
		if(subjectIds==null||subjectIds.length==0){
			return AjaxResult.errorInstance("请设置学习卡所属学科");
		}
		Card param = new Card();
		param.setName(card.getName());
		List<Card> list = cardService.selectList(param);
		if(list!=null&&list.size()>0){
			return AjaxResult.errorInstance("学习卡名已经存在");
		}
		
		cardService.insert(card,subjectIds);
		
		return AjaxResult.successInstance("添加学习卡成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public ModelAndView updatePage(Long id){
		ModelAndView modelAndView = new ModelAndView("card/update","card",cardService.selectOne(id));
		List<Subject> subjects = subjectService.selectList();
		modelAndView.addObject("subjects", subjects);
		CardSubject cardSubject = new CardSubject();
		cardSubject.setCardId(id);
		List<CardSubject> cardSubjects = cardSubjectService.selectList(cardSubject);
		modelAndView.addObject("cardSubjects", cardSubjects);
		return modelAndView;
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult updateSubmit(Card card,Long[] subjectIds){
		if(CommonUtils.isEmpty(card.getName())){
			return AjaxResult.errorInstance("学习卡名不能为空");
		}
		if(subjectIds==null||subjectIds.length==0){
			return AjaxResult.errorInstance("请设置学习卡所属学科");
		}
		Card param = new Card();
		param.setName(card.getName());
		param = cardService.selectOne(param);
		//学习卡名不能为其他已经存在的name
		if(param!=null&&param.getId()!=card.getId()){
			return AjaxResult.errorInstance("学习卡名已经存在");
		}
		
		cardService.update(card,subjectIds);
		
		return AjaxResult.successInstance("修改学习卡成功");
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody AjaxResult delete(Long id){
		cardService.delete(id);
		//*同时应删除学习卡与学科关联关系,避免其他管理模块获取到已经删除的学习卡与学科的关联关系
		return AjaxResult.successInstance("学习卡删除成功");
	}
}
