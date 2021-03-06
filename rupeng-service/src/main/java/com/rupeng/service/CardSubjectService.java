package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rupeng.annotation.RupengCacheable;
import com.rupeng.annotation.RupengClearCache;
import com.rupeng.annotation.RupengUseCache;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;

@Service
@RupengCacheable
public class CardSubjectService extends ManyToManyBaseService<CardSubject, Card, Subject> {

	@RupengClearCache
	public void order(Long[] cardSubjectIds, Integer[] seqNums) {
		if(cardSubjectIds!=null&&cardSubjectIds.length>0){
			CardSubject cardSubject;
			for (int i = 0; i < cardSubjectIds.length; i++) {
				cardSubject = selectOne(cardSubjectIds[i]);
				cardSubject.setSeqNum(seqNums[i]);
				update(cardSubject);
			}
		}
	}

	@RupengUseCache
	public List<Card> selectFirstListBySecondId(Long subjectId, String orderBy) {
		PageHelper.orderBy(orderBy);
		return selectFirstListBySecondId(subjectId);
	}
}
