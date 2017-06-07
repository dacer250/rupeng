package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Classes;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ClassesService;
import com.rupeng.service.UserCardService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

//给班级学生发放第一张学习卡
@Controller
@RequestMapping("/userCard")
public class UserCardController {
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private CardSubjectService cardSubjectService;
	
	@RequestMapping("/activateFirstCard.do")
	public @ResponseBody AjaxResult activateFirstCard(Long classesId){
		//查询班级所属学科
		Classes classes = classesService.selectOne(classesId);
		Long subjectId = classes.getSubjectId();
		if (subjectId==null) {
			return AjaxResult.errorInstance("此班级没有设置所属学科");
		}
		
		//查询学科的第一张学习卡
		CardSubject cardSubject = new CardSubject();
		cardSubject.setSubjectId(subjectId);
		List<CardSubject> cardSubjects = cardSubjectService.selectList(cardSubject, "seqNum asc");
		if (CommonUtils.isEmpty(cardSubjects)) {
			return AjaxResult.errorInstance("此班级所属学科没有设置学习卡");
		}
		Long cardId = cardSubjects.get(0).getCardId();
		
		//查询班级的成员,并给那些没有此学习卡的学生发放学习卡
		userCardService.activateFirstCard(classesId,cardId);
		
		return AjaxResult.successInstance("发放成功");
	}
}
