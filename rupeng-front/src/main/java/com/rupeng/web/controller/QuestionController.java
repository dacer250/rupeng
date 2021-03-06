package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ChapterService;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.QuestionAnswerService;
import com.rupeng.service.QuestionService;
import com.rupeng.service.SegmentService;
import com.rupeng.service.UserSegmentService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;

@Controller
@RequestMapping("/question")
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ClassesUserService classesUserService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private UserSegmentService userSegmentService;
	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CardService cardService;
	@Autowired
	private QuestionAnswerService questionAnswerService;
	
	//我要提问
	@RequestMapping(value="/ask.do",method=RequestMethod.GET)
	public ModelAndView askPage(HttpServletRequest request){
		//获取当前用户
		User user = (User) request.getSession().getAttribute("user");
		
		//学习记录
		//最后一次学习的segmentId
		Long lastSegmentId = null;
		//最后一次学习的chapterId
		Long lastChapterId = null;
		//最后一次学习的cardId
		Long lastCardId = null;
		
		//当前用户所在班级所属学科所有的学习卡(查询用户所拥有的学习卡比较精确但比较麻烦)
		List<Card> cardList = null;
		//选中学习卡的所有章节(1.最后一次学习的学习卡的所有章节2.还没开始学习的默认第一张学习卡的所有章节3.null)
		List<Chapter> chapterList = null;
		//选中章节的所有课程(同上)
		List<Segment> segmentList = null;
		
		//用户所在班级所属的学科
		Classes classes = classesUserService.selectFirstOneBySecondId(user.getId());
//		if (classes==null) {
//			return new ModelAndView("message","message","您还没有加入班级,不能提问");
//		}
		Long subjectId = classes.getSubjectId();
//		if (subjectId==null) {
//			return new ModelAndView("message","message","您所在班级还没有设置学科,不能提问");
//		}
		//班级拥有的学习卡
		cardList = cardSubjectService.selectFirstListBySecondId(subjectId,"seqNum asc");
		
		//查询学习记录
		UserSegment userSegment = new UserSegment();
		userSegment.setUserId(user.getId());
		lastSegmentId = userSegmentService.selectLastSegmentId(user.getId());
		
		if (lastSegmentId!=null) {
			lastChapterId = segmentService.selectOne(lastSegmentId).getChapterId();
			lastCardId = chapterService.selectOne(lastChapterId).getCardId();
			
			Segment segment = new Segment();
			segment.setChapterId(lastChapterId);
			segmentList = segmentService.selectList(segment, "seqNum asc");
			
			Chapter chapter = new Chapter();
			chapter.setCardId(lastCardId);
			chapterList = chapterService.selectList(chapter, "seqNum asc");
		}else{
			//没有学习记录,默认显示第一张学习卡
            if (!CommonUtils.isEmpty(cardList)) {
                Card firstCard = cardList.get(0);

                Chapter chapter = new Chapter();
                chapter.setCardId(firstCard.getId());
                chapterList = chapterService.selectList(chapter, "seqNum asc");

                if (!CommonUtils.isEmpty(chapterList)) {
                    Chapter firstChapter = chapterList.get(0);

                    Segment param = new Segment();
                    param.setChapterId(firstChapter.getId());
                    segmentList = segmentService.selectList(param, "seqNum asc");
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("question/ask");
        modelAndView.addObject("cardList", cardList);
        modelAndView.addObject("chapterList", chapterList);
        modelAndView.addObject("segmentList", segmentList);
        modelAndView.addObject("lastCardId", lastCardId);
        modelAndView.addObject("lastChapterId", lastChapterId);
        modelAndView.addObject("lastSegmentId", lastSegmentId);

        return modelAndView;
	}
	
	//提交问题
	@RequestMapping(value="/ask.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult askSubmit(Long segmentId,String errorInfo,String errorCode,String description,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		
		if (CommonUtils.isEmpty(errorInfo)&&CommonUtils.isEmpty(errorCode)&&CommonUtils.isEmpty(description)) {
			return AjaxResult.errorInstance("报错信息、相关代码、问题描述不能全为空");
		}
		
		Question question = new Question();
		
		if (segmentId!=null) {
			Segment segment = segmentService.selectOne(segmentId);
			Chapter chapter = chapterService.selectOne(segment.getChapterId());
			Card card = cardService.selectOne(chapter.getCardId());
			StringBuilder builder = new StringBuilder();
			builder.append(card.getName()).append(" >> ");
			builder.append(chapter.getSeqNum()).append(" ").append(chapter.getName()).append(" >> ");
			builder.append(segment.getSeqNum()).append(" ").append(segment.getName());
			
			question.setCourseInfo(builder.toString());
		}
		
		question.setCreateTime(new Date());
		question.setDescription(description);
		question.setErrorCode(errorCode);
		question.setErrorInfo(errorInfo);
		question.setSegmentId(segmentId);
		question.setUserId(user.getId());
		question.setUsername(user.getName());
		
		questionService.insert(question);
		
		//创建并保存通知消息    ---发送给提问学生所在班级的老师
		
		//1 创建消息对象    
		Map<String, Object> notification = new HashMap<>();
		
			//查询当前用户新插入的提问
		Question param = new Question();
		param.setUserId(user.getId());
		question = questionService.page(1, 1, param, "createTime desc").getList().get(0);
		
		notification.put("questionId", question.getId());
		notification.put("content", "学生提问了新的问题");
		
		//2 保存到redis服务器(使用set数据结构,短时间内相同问题的多次新回复只提示一次)   
		//key设计为notification_{teacherId}   value使用set结构   json 
		
			//查询当前提问用户所在班级的老师
		Classes classes = classesUserService.selectFirstOneBySecondId(user.getId());
		List<User> userList = classesUserService.selectSecondListByFirstId(classes.getId());
		for (User user2 : userList) {
			if (user2.getIsTeacher()!=null&&user2.getIsTeacher()) {
				//保存消息
				JedisUtils.sadd("notification_"+user2.getId(), JsonUtils.toJson(notification));
			}
		}
		
		return AjaxResult.successInstance("提问成功");
	}
	
	//问题列表
	@RequestMapping(value="/list.do")
	public ModelAndView list(Integer pageNum,String condition,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		
		if (pageNum==null) {
			pageNum=1;
		}
		
		if (CommonUtils.isEmpty(condition)) {//默认显示我提问的问题
			condition="myAsked";
		}
		
		PageInfo<Question> pageInfo = null;
		Question question = new Question();
		int pageSize = 10;
		if (condition.equals("myAsked")) {//我提问的
			question.setUserId(user.getId());
			pageInfo = questionService.page(pageNum, pageSize, question, "isResolved asc");
		}else if (condition.equals("allUnresolved")) {//所有未解决的
			question.setIsResolved(false);
			pageInfo = questionService.page(pageNum, pageSize, question, "createTime desc");
		}else if (condition.equals("allResolved")) {//所有解决的
			question.setIsResolved(true);
			pageInfo = questionService.page(pageNum, pageSize, question, "createTime desc");
		}else if (condition.equals("myAnswered")) {//我回答的问题
			pageInfo = questionService.pageOfMyAnswered(pageNum,pageSize,user.getId());
			/*//去重
			List<Question> list = pageInfo.getList();
			if (!CommonUtils.isEmpty(list)) {
				Set<Question> set = new LinkedHashSet<>(list);
				list = new ArrayList<Question>(set);
				pageInfo.setList(list);
				//int pages = (int) Math.ceil(list.size()/(pageSize*1.0));
				//pageInfo.setPages(pages);
			}*/
		}
		
		ModelAndView modelAndView = new ModelAndView("question/list");
		modelAndView.addObject("pageInfo", pageInfo);
		modelAndView.addObject("condition", condition);
		
		return modelAndView;
	}
	
	//问题详情
	@RequestMapping(value="/detail.do")
	public ModelAndView detail(Long id,HttpServletRequest request){
		//问题信息
		Question question = questionService.selectOne(id);
		//班级信息
		Classes classes = classesUserService.selectFirstOneBySecondId(question.getUserId());
		
		//此问题所有的答案
		QuestionAnswer questionAnswer = new QuestionAnswer();
		questionAnswer.setQuestionId(id);
		List<QuestionAnswer> allAnswerList = questionAnswerService.selectList(questionAnswer, "createTime desc");
		
		//处理和此问题相关的所有答案,使答案之间有层级关系
		//1 先找到所有顶层答案  parentId==null
		List<QuestionAnswer> rootAnswerList = new ArrayList<>();
		
		if (!CommonUtils.isEmpty(allAnswerList)) {
			//获取顶层答案
			for (QuestionAnswer answer : allAnswerList) {
				if (answer.getParentId()==null) {
					rootAnswerList.add(answer);
				}
			}
			
			//2 给每个答案找到所有直接子答案
			for (QuestionAnswer answer : allAnswerList) {
				//找到当前遍历到的答案的所有直接子答案
				List<QuestionAnswer> childrenAnswerList = new ArrayList<>();
				
				for (QuestionAnswer answer2 : allAnswerList) {
					if (answer2.getParentId()==answer.getId()) {
						childrenAnswerList.add(answer2);
					}
				}
				
				//把直接子答案设置到父答案中
				answer.setChildrenAnswerList(childrenAnswerList);
			}
		}
		
		ModelAndView modelAndView = new ModelAndView("question/detail");
		modelAndView.addObject("question", question);
		modelAndView.addObject("classes", classes);
		modelAndView.addObject("rootAnswerList", rootAnswerList);
		
		return modelAndView;
	}
	
	//我来回答
	@RequestMapping(value="/answer.do")
	public @ResponseBody AjaxResult answer(Long questionId,Long parentId,String content,HttpServletRequest request){
		if (CommonUtils.isEmpty(content)) {
			return AjaxResult.errorInstance("回答不能为空");
		}
		
		User user = (User) request.getSession().getAttribute("user");
		
		QuestionAnswer questionAnswer = new QuestionAnswer();
		questionAnswer.setContent(content);
		questionAnswer.setCreateTime(new Date());
		questionAnswer.setParentId(parentId);
		questionAnswer.setQuestionId(questionId);
		questionAnswer.setUserId(user.getId());
		questionAnswer.setUsername(user.getName());
		
		questionAnswerService.insert(questionAnswer);
		
		//创建并保存通知消息  ---发送给问题参与者
		Map<String, Object> notification = new HashMap<>();
		notification.put("questionId", questionId);
		
		Set<Long> userIdSet = new HashSet<>();
		//问题提问者
		Question question = questionService.selectOne(questionId);
		userIdSet.add(question.getUserId());
		//问题参与者
		QuestionAnswer param = new QuestionAnswer();
		param.setQuestionId(questionId);
		List<QuestionAnswer> answerList = questionAnswerService.selectList(param);
		for (QuestionAnswer answer : answerList) {
			userIdSet.add(answer.getUserId());
		}
		//去除此回复者
		userIdSet.remove(user.getId());
		
		//保存到redids
		for (Long userId : userIdSet) {
			//更人性化的通知
            if (userId == question.getUserId()) {
                notification.put("content", "您提问的问题有新回答");
            } else {
                notification.put("content", "您参与的问题有新回答");
            }
			JedisUtils.sadd("notification_"+userId, JsonUtils.toJson(notification));
		}
		
		return AjaxResult.successInstance("提交回答成功");
	}
	
	//采纳
	@RequestMapping(value="/adopt.do")
	public @ResponseBody AjaxResult adopt(Long questionAnswerId,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		
		QuestionAnswer questionAnswer = questionAnswerService.selectOne(questionAnswerId);
		Question question = questionService.selectOne(questionAnswer.getQuestionId());
		//只有问题的提问者或者老师可以采纳
		if (!question.getUserId().equals(user.getId())&&(!user.getIsTeacher()==true)) {
			return AjaxResult.successInstance("非提问者或老师不能采纳");
		}
		
		questionAnswer.setIsAdopted(true);
		question.setIsResolved(true);
		question.setResolvedTime(new Date());
		
		questionService.adopt(question,questionAnswer);
		
		//创建并保存通知消息  ---发送给问题参与者
		Map<String, Object> notification = new HashMap<>();
		notification.put("questionId", question.getId());
		
		Set<Long> userIdSet = new HashSet<>();//去重
		//问题提问者
		userIdSet.add(question.getUserId());
		//问题参与者
		QuestionAnswer param = new QuestionAnswer();
		param.setQuestionId(question.getId());
		List<QuestionAnswer> answerList = questionAnswerService.selectList(param);
		for (QuestionAnswer answer : answerList) {
			userIdSet.add(answer.getUserId());
		}
		//去除此采纳者
		userIdSet.remove(user.getId());
		
		//保存到redis
		for (Long userId : userIdSet) {
			//更人性化的通知
            if (userId == question.getUserId()) {
                notification.put("content", "您提问的问题有回答被采纳了");
            } else {
                notification.put("content", "您参与的问题有回答被采纳了");
            }
			JedisUtils.sadd("notification_"+userId, JsonUtils.toJson(notification));
		}
		
		return AjaxResult.successInstance("采纳成功");
	}
		
}
