package com.rupeng.mapper;

import java.util.List;
import java.util.Map;

import com.rupeng.pojo.User;

public interface UserMapper extends IMapper<User> {

	List<User> search(Map<String, Object> params);
}
