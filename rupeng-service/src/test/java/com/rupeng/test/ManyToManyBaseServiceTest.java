package com.rupeng.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rupeng.pojo.Card;
import com.rupeng.service.CardSubjectService;

public class ManyToManyBaseServiceTest {
	private ApplicationContext applicationContext;
	private CardSubjectService cardSubjectService;
	
	@Before
	public void init(){
		applicationContext = new ClassPathXmlApplicationContext("/beans.xml");
		cardSubjectService = applicationContext.getBean(CardSubjectService.class);
	}
	
	@Test
	public void selectFirstListBySecondId(){
		List<Card> list = cardSubjectService.selectFirstListBySecondId(2);
		Assert.assertNotNull(list);
	}
	
}
