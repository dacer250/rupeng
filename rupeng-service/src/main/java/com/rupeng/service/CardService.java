package com.rupeng.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.annotation.RupengCacheable;
import com.rupeng.annotation.RupengClearCache;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.util.CommonUtils;

@Service
@RupengCacheable
public class CardService extends BaseService<Card> {

	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private SegmentService segmentService;
	
	@RupengClearCache
	public void insert(Card card, Long[] subjectIds) {
		insert(card);
		
		Card param = new Card();
		param.setName(card.getName());
		Long cardId = selectOne(param).getId();
		
		cardSubjectService.updateFirst(cardId, subjectIds);
	}
	
	@RupengClearCache
	public void update(Card card, Long[] subjectIds) {
		update(card);
		
		cardSubjectService.updateFirst(card.getId(), subjectIds);
	}

	public Map<Chapter, List<Segment>> selectAllCourse(Long cardId) {
		//查询出所有Chapter,按序号排序
		Chapter param = new Chapter();
		param.setCardId(cardId);
		List<Chapter> chapters = chapterService.selectList(param, "seqNum asc");
		if (CommonUtils.isEmpty(chapters)) {
			return null;
		}
		
		//查询出所有Segment,按序号排序
		Map<Chapter,List<Segment>> courseMap = new LinkedHashMap<>();
		
		for (Chapter chapter : chapters) {
			Segment segment = new Segment();
			segment.setChapterId(chapter.getId());
			List<Segment> segments = segmentService.selectList(segment, "seqNum asc");
			courseMap.put(chapter, segments);
		}
		
		return courseMap;
	}

}
