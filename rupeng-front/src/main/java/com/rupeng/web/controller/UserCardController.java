package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ClassesService;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.SettingService;
import com.rupeng.service.UserCardService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/userCard")
public class UserCardController {
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private ClassesUserService classesUserService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private SettingService settingService;
	
	//申请学习卡
	@RequestMapping("/applyNext.do")
	public @ResponseBody AjaxResult applyNext(HttpServletRequest request){
		//获取当前用户id
		User user = (User) request.getSession().getAttribute("user");
		Long userId = user.getId();
		//获取用户拥有的最后一张学习卡id
		UserCard userCard = new UserCard();
		userCard.setUserId(userId);
		PageInfo<UserCard> pageInfo = userCardService.page(1, 1, userCard, "createTime desc");
		
		Long lastCardId = null;
		if (pageInfo!=null && !CommonUtils.isEmpty(pageInfo.getList())) {
			lastCardId = pageInfo.getList().get(0).getCardId();
		}//没有学习卡的原因有多种1.正在申请第一张2.没有所属班级,班级没有设置学科,学科没有分配学习卡等等,下面一一排除除了1的情况
		
		//所在班级所属的学科
		ClassesUser classesUser = new ClassesUser();
		classesUser.setUserId(userId);
		classesUser = classesUserService.selectOne(classesUser);
		if (classesUser==null) {
			return AjaxResult.errorInstance("还没有加入班级");
		}
		Long subjectId = classesService.selectOne(classesUser.getClassesId()).getSubjectId();
		if (subjectId==null) {
			return AjaxResult.errorInstance("班级没有设置学科");
		}
		//学科拥有的所有学习卡---排序
		CardSubject cardSubject = new CardSubject();
		cardSubject.setSubjectId(subjectId);
		List<CardSubject> cardSubjects = cardSubjectService.selectList(cardSubject, "seqNum asc");
		if (CommonUtils.isEmpty(cardSubjects)) {
			return AjaxResult.errorInstance("班级所属学科还没有分配学习卡");
		}
		//获取申请的下一张学习卡id
		Long nextCardId = null;
		if (lastCardId==null) {
			nextCardId = cardSubjects.get(0).getCardId();//申请的第一张学习卡
		}else{
			int i = 0;
			for (; i < cardSubjects.size(); i++) {
				if (cardSubjects.get(i).getCardId()==lastCardId) {
					break;
				}
			}
			if (i==cardSubjects.size()-1) {
				return AjaxResult.errorInstance("您已经拥有所有的学习卡");
			}
			nextCardId = cardSubjects.get(i+1).getCardId();
		}
		
		//执行申请学习卡的插入操作
		userCard = new UserCard();
		userCard.setUserId(userId);
		userCard.setCardId(nextCardId);
		userCard.setCreateTime(new Date());
		
		int validDays = Integer.parseInt(settingService.selectByName("card_valid_days").getValue());
		Date endTime = new Date(userCard.getCreateTime().getTime()+1000L*60*60*24*validDays);
		userCard.setEndTime(endTime);
		userCardService.insert(userCard);
		
		return AjaxResult.successInstance("申请学习卡成功");
	}
}
