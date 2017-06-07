package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;
import com.rupeng.util.CommonUtils;

@Service
public class UserSegmentService extends ManyToManyBaseService<UserSegment, User, Segment> {

	public Long selectLastSegmentId(Long userId) {
		//按时间倒序排序,查询出学生最后一次的学习记录
		UserSegment param = new UserSegment();
		param.setUserId(userId);
		
		PageInfo<UserSegment> pageInfo = page(1, 1, param, "createTime desc");
		if (pageInfo==null || CommonUtils.isEmpty(pageInfo.getList())) {
			return null;//没有开始学习,无记录
		}
		
		return pageInfo.getList().get(0).getSegmentId();
	}

}