package com.rupeng.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Setting;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.CommonUtils;

@Service
public class UserCardService extends ManyToManyBaseService<UserCard, User, Card> {

	@Autowired
	private ClassesUserService classesUserService;
	@Autowired
	private SettingService settingService;
	
	/**
	 * 给指定的班级的所有的学生发放指定的学习卡
	 * @param classesId
	 * @param cardId
	 */
	public void activateFirstCard(Long classesId, Long cardId) {
		
		List<User> users = classesUserService.selectSecondListByFirstId(classesId);
		
		if (CommonUtils.isEmpty(users)) {
			return;
		}
		
		Setting setting = new Setting();
		setting.setName("card_valid_days");
		int validDays = Integer.parseInt(settingService.selectOne(setting).getValue());
		
		for (User user : users) {
			if (!user.getIsTeacher()) {//过滤老师
				UserCard userCard = new UserCard(user.getId(), cardId);
				
				if (!isExisted(userCard)) {//过滤已有此学习卡的学生
					userCard.setCreateTime(new Date());
					userCard.setEndTime(new Date(userCard.getCreateTime().getTime()+validDays*1000L*60*60*24));
					insert(userCard);//执行插入
				}
			}
		}
	}

}