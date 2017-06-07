package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.annotation.RupengCacheable;
import com.rupeng.pojo.Subject;

@Service
@RupengCacheable(expire=300)
public class SubjectService extends BaseService<Subject> {

}