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
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/cardSubject")
public class CardSubjectController {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CardSubjectService cardSubjectService;
	
	//学习卡排序
	@RequestMapping(value="/order.do",method=RequestMethod.GET)
	public ModelAndView orderPage(Long subjectId){
		//获取全部学科数据
		List<Subject> subjects = subjectService.selectList();
		
		//首次访问排序页面subjectId的值为null,需要赋值,默认为查询到的第一个学科
		if(subjectId==null && !CommonUtils.isEmpty(subjects)){//考虑数据库没有学科数据的特殊情况
			subjectId = subjects.get(0).getId();
		}
		
		//选中学科与学习卡关联关系(学科所拥有的学习卡及序号,排序)
		List<CardSubject> cardSubjects = null;
		if (subjectId!=null) {
			CardSubject cardSubject = new CardSubject();
			cardSubject.setSubjectId(subjectId);
			cardSubjects = cardSubjectService.selectList(cardSubject, "seqNum asc");
		}
		
		//所关联的学习卡的数据
		List<Card> cards = null;
		if (subjectId!=null) {
			cards = cardSubjectService.selectFirstListBySecondId(subjectId);
		}
		
		ModelAndView modelAndView = new ModelAndView("cardSubject/order");
		modelAndView.addObject("subjectId", subjectId);
		modelAndView.addObject("subjects", subjects);
		modelAndView.addObject("cards", cards);
		modelAndView.addObject("cardSubjects", cardSubjects);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/order.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult orderSubmit(Long[] cardSubjectIds,Integer[] seqNums){
		cardSubjectService.order(cardSubjectIds,seqNums);
		return AjaxResult.successInstance("保存成功");
	}
	
}
