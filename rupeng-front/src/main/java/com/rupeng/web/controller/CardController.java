package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.service.CardService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/card")
public class CardController {
	@Autowired
	private CardService cardService;
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private UserSegmentService userSegmentService;
	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ChapterService chapterService;
	
	@RequestMapping("/detail.do")
	public ModelAndView detail(Long id,HttpServletRequest request){
		//检查用户是否拥有这张学习卡以及学习卡是否已经过期
		User user = (User) request.getSession().getAttribute("user");
		UserCard userCard = new UserCard(user.getId(), id);
		userCard = userCardService.selectOne(userCard);
		if (userCard==null) {
			return new ModelAndView("message","message","您不拥有这张学习卡");
		}
		if (userCard.getEndTime().getTime()<System.currentTimeMillis()) {
			return new ModelAndView("message","message","这张学习卡已经过期");
		}
		
		//学习卡剩余有效时间
		long remainValidDays = CommonUtils.calculateApartDays(new Date(), userCard.getEndTime());
		//最后一次segment学习记录
		Long lastSegmentId = userSegmentService.selectLastSegmentId(user.getId());
		
		
		//查询学习卡的信息
		Card card = cardService.selectOne(id);
		
		//查询此学习卡的所有Chapter--List<Segment>  (seqNum asc)
		Map<Chapter,List<Segment>> courseMap = cardService.selectAllCourse(id);
		
		ModelAndView modelAndView = new ModelAndView("card/detail");
		modelAndView.addObject("card", card);
        modelAndView.addObject("courseMap", courseMap);
        modelAndView.addObject("remainValidDays", remainValidDays);
        modelAndView.addObject("lastSegmentId", lastSegmentId);

        return modelAndView;
	}
	
	//上次学到这里
	@RequestMapping("/last.do")
	public ModelAndView last(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		//最后一次segment学习记录
		Long lastSegmentId = userSegmentService.selectLastSegmentId(user.getId());
		if (lastSegmentId==null) {
			return new ModelAndView("message","message","您还没有学习记录");
		}
		
		//通过segmentId查询出cardId
		Segment segment = segmentService.selectOne(lastSegmentId);
		Chapter chapter = chapterService.selectOne(segment.getChapterId());
		Card card = cardService.selectOne(chapter.getCardId());
		
		//转到最后一次学习的card页
		return detail(card.getId(), request);
	}
}
