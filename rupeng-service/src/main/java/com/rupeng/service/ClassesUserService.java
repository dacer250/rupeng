package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.pojo.Classes;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;

@Service
public class ClassesUserService extends ManyToManyBaseService<ClassesUser, Classes, User> {

}
