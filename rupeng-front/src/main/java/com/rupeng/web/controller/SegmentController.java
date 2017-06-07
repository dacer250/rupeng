package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.QuestionService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/segment")
public class SegmentController {

	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CardService cardService;
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private UserSegmentService userSegmentService;
	@Autowired
	private QuestionService questionService;
	
	
	@RequestMapping("/detail.do")
	public ModelAndView detail(Long id,HttpServletRequest request){
		//Segment
		Segment segment = segmentService.selectOne(id);
		//Chapter
		Chapter chapter = chapterService.selectOne(segment.getChapterId());
		//Card
		Card card = cardService.selectOne(chapter.getCardId());
		
		//检查用户是否有此学习卡以及学习卡是否过期
		User user = (User) request.getSession().getAttribute("user");
		UserCard userCard = new UserCard(user.getId(), card.getId());
		userCard = userCardService.selectOne(userCard);
		if (userCard==null || userCard.getEndTime().getTime()<System.currentTimeMillis()) {
			return new ModelAndView("message","message","您没有此张学习卡或学习卡已经过期");
		}
		
		//相关问题
		Question question = new Question();
		question.setSegmentId(id);
		List<Question> questions = questionService.selectList(question);
		
		ModelAndView modelAndView = new ModelAndView("segment/detail");
		modelAndView.addObject("segment", segment);
		modelAndView.addObject("chapter", chapter);
		modelAndView.addObject("card", card);
		modelAndView.addObject("questions", questions);
		
		//添加学习记录
		UserSegment userSegment = new UserSegment(user.getId(), id);
		userSegment.setCreateTime(new Date());
		userSegmentService.insert(userSegment);

		return modelAndView;
	}
	
	//下一节课
	@RequestMapping("/next.do")
	public ModelAndView next(Long currentSegmentId,HttpServletRequest request){
		//查询下一节课程信息
		Segment nextSegment = segmentService.selectNext(currentSegmentId);
		
		if (nextSegment==null) {
			return new ModelAndView("message","message","已经是本张学习卡最后一节课");
		}
		
		//转到下节课程详情页
		return detail(nextSegment.getId(), request);
	}
	
	@RequestMapping("/list.do")
	public @ResponseBody AjaxResult list(Long chapterId){
		if (chapterId == null) {
			throw new RuntimeException("篇章id值不能为null");
		}
		Segment segment = new Segment();
		segment.setChapterId(chapterId);
		
		List<Segment> segmentList = segmentService.selectList(segment, "seqNum asc");
		return AjaxResult.successInstance(segmentList);
	}
}
