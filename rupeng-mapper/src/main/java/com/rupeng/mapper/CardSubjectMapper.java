package com.rupeng.mapper;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;

public interface CardSubjectMapper extends IManyToManyMapper<CardSubject, Card, Subject> {

}
