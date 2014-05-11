package com.hyperlab.luckyhomefinder.sites.gohome.service;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.hyperlab.luckyhomefinder.service.ParsersManager;

public class TestGoHomeManager {

	private ParsersManager parserManager;

	@Before
	public void setup() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"testcontext.xml");
		parserManager = (ParsersManager) context.getBean("parsersManager");
	}

	@Test
	public void testGohomeParsersManager() {
		String[] links = {
				"http://property.gohome.com.hk/Fairview/Fairview-Park/ad-1194360/en/",
				"http://property.gohome.com.hk/Tsuen-Wan/Allway-Gardens/ad-1194352/en/",
				"http://property.gohome.com.hk/Mid-Levels-West/Vantage-Park/ad-1194338/en/",
				"http://property.gohome.com.hk/Mid-Levels-West/Robinson-Place/ad-1130934/en/" };

		parserManager.processLinks(Arrays.asList(links));
	}

}
