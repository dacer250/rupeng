package com.rupeng.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Subject;
import com.rupeng.service.SubjectService;

public class BaseServiceTest {
	private ApplicationContext applicationContext;
	private SubjectService subjectService;
	
	@Before
	public void init(){
		applicationContext = new ClassPathXmlApplicationContext("/beans.xml");
		subjectService = applicationContext.getBean(SubjectService.class);
	}
	
	@Test
	public void insetIsExistedSelectOneDelete(){
		Subject subject = new Subject();
		subject.setName("_testName");
		
		int r1 = subjectService.insert(subject);
		Assert.assertSame(1, r1);
		
		boolean r2 = subjectService.isExisted(subject);
		Assert.assertSame(true, r2);
		
		subject = subjectService.selectOne(subject);
		Assert.assertNotNull(subject);
		
		int r3 = subjectService.delete(subject.getId());
		Assert.assertSame(1, r3);
		
	}
	
	@Test
	public void updateSelectListPage(){
		Subject subject = new Subject();
		subject.setName("_testName");
		subjectService.insert(subject);
		subject = subjectService.selectOne(subject);
		
		subject.setName("_testName2");
		int r1 = subjectService.update(subject);
		Assert.assertSame(1, r1);
		
		int r2 = subjectService.selectList().size();
		Assert.assertTrue(r2>0);
		
		PageInfo<Subject> pageInfo = subjectService.page(1, 10, subject, "name desc");
		Assert.assertNotNull(pageInfo.getList());
	
		subjectService.delete(subject.getId());
	}
}
