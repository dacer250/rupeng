package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.pojo.Setting;

@Service
public class SettingService extends BaseService<Setting> {

	public Setting selectByName(String name) {
		Setting setting = new Setting();
		setting.setName(name);
		return selectOne(setting);
	}

}
