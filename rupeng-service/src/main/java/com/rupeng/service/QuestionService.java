package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.QuestionMapper;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;

@Service
public class QuestionService extends BaseService<Question> {

	@Autowired
	private QuestionAnswerService questionAnswerService;
	@Autowired
    private QuestionMapper questionMapper;
	
	public void adopt(Question question, QuestionAnswer questionAnswer) {
		update(question);
		questionAnswerService.update(questionAnswer);
	}

	//我回答的问题
	public PageInfo<Question> pageOfMyAnswered(int pageNum, int pageSize, Long userId) {
		PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectMyAnswered(userId);
        return new PageInfo<Question>(list);
	}

	//查询老师所在所有班级的所有学生提出的所有未解决的问题
	public List<Question> selectUnresolvedQuestionByTeacherId(Long teacherId) {
		return questionMapper.selectUnresolvedQuestionByTeacherId(teacherId);
	}

	//当前用户(学生)提问的所有未解决问题以及回复的所有未解决问题
	public List<Question> selectUnresolvedQuestionByStudentId(Long studentId) {
		return questionMapper.selectUnresolvedQuestionByStudentId(studentId);
	}

}
