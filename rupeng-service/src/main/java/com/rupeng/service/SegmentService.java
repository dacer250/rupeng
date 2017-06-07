package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.util.CommonUtils;

@Service
public class SegmentService extends BaseService<Segment> {
	@Autowired
	private ChapterService chapterService;

	public Segment selectNext(Long currentSegmentId) {
		//1.当前课程在本章节有下一节课
		
		Segment currentSegment = selectOne(currentSegmentId);
		//当前课程所在章节的所有课程,按序号排序
		Segment param = new Segment();
		param.setChapterId(currentSegment.getChapterId());
		List<Segment> segments = selectList(param, "seqNum asc");
		
		int i = 0;
		for (; i < segments.size(); i++) {
			if (segments.get(i).getId()==currentSegmentId) {
				break;
			}
		}
		if (i<segments.size()-1) {
			return segments.get(i+1);
		}
		
		//2.当前课程是本章节最后一节课程,到下一章节获取第一节课
		Chapter nextChapter = null;
		Long currentChapterId = currentSegment.getChapterId();
		while((nextChapter = chapterService.selectNext(currentChapterId))!=null){
			Segment param2 = new Segment();
			param2.setChapterId(nextChapter.getId());
			PageInfo<Segment> pageInfo = page(1, 1, param2, "seqNum asc");
			
			if (pageInfo!=null&&!CommonUtils.isEmpty(pageInfo.getList())) {
				return pageInfo.getList().get(0);
			}
			
			currentChapterId = nextChapter.getId();
		}
		return null;
	}

}
