package com.rupeng.mapper;

import java.util.List;

import com.rupeng.pojo.Question;

public interface QuestionMapper extends IMapper<Question> {

	List<Question> selectMyAnswered(Long userId);

	List<Question> selectUnresolvedQuestionByTeacherId(Long teacherId);

	List<Question> selectUnresolvedQuestionByStudentId(Long studentId);

}
