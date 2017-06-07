package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.annotation.RupengCacheable;
import com.rupeng.pojo.Classes;

@Service
@RupengCacheable
public class ClassesService extends BaseService<Classes> {

}
