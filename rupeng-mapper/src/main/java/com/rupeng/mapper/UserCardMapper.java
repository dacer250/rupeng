package com.rupeng.mapper;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;

public interface UserCardMapper extends IManyToManyMapper<UserCard, User, Card> {

}
