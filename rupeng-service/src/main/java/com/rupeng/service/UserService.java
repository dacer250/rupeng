package com.rupeng.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.UserMapper;
import com.rupeng.pojo.User;
import com.rupeng.util.CommonUtils;

@Service
public class UserService extends BaseService<User> {

	@Autowired
	private UserMapper mapper;
	
	public PageInfo<User> search(Integer pageNum, int pageSize, Map<String, Object> params) {
		PageHelper.startPage(pageNum, pageSize);
		List<User> list =  mapper.search(params);
		return new PageInfo<User>(list);
	}

	public User login(String loginName, String password) {
		User user = new User();
        if (CommonUtils.isEmail(loginName)) {
            //尝试使用email登录
            user.setEmail(loginName);
        } else {
            //尝试使用phone登录
            user.setPhone(loginName);
        }
        user = selectOne(user);
        if (user != null) {
            if (user.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(user.getPasswordSalt() + password))) {
                return user;
            }
        }
        return null;
	}

}
