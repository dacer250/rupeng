package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rupeng.annotation.RupengCacheable;
import com.rupeng.pojo.Chapter;

@Service
@RupengCacheable
public class ChapterService extends BaseService<Chapter> {

	public Chapter selectNext(Long currentChapterId) {
		//当前章节
		Chapter currentChapter = selectOne(currentChapterId);
		Chapter param = new Chapter();
		param.setCardId(currentChapter.getCardId());
		//当前学习卡所有章节,按序号排序
		List<Chapter> chapters = selectList(param, "seqNum asc");
		
		int i = 0;
		for (; i < chapters.size(); i++) {
			if (chapters.get(i).getId()==currentChapterId) {
				break;
			}
		}
		if (i<chapters.size()-1) {//如果索引i(当前)章节不是最后章节
			return chapters.get(i+1);//返回下一章节
		}
		return null;
	}
	
}
