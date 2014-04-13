package com.hyperlap.luckyhomefinder.sites.gohome.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hyperlab.luckyhomefinder.repository.spring.ReposConf;
import com.hyperlap.luckyhomefinder.service.ParsersManager;
import com.hyperlap.luckyhomefinder.sites.gohome.service.GoHomeParserFactory;

/**
 * Spring configuration for GoHome module.
 * */
@Configuration
@Import({ ReposConf.class })
@ComponentScan(basePackages = { "com.hyperlap.luckyhomefinder.sites.gohome.*" })
public class GoHomeConf {

	@Bean(name = "gohomeManager")
	public ParsersManager manager() {
		GoHomeParserFactory parserFactory = new GoHomeParserFactory();
		return new ParsersManager(parserFactory);
	}
}
