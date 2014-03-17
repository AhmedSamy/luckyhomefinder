package com.hyperlap.luckyhomefinder.sites.hse28.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hyperlab.luckyhomefinder.repository.spring.RepositoryConfiguration;
import com.hyperlab.luckyhomefinder.sites.hse28.services.H28seParserFactory;
import com.hyperlab.luckyhomefinder.sites.hse28.services.Manager;

/**
 * Spring configuration class for the h28se modules.
 * */

@Configuration
@Import({ RepositoryConfiguration.class })
@ComponentScan(basePackages={"com.hyperlab.luckyhomefinder.sites.hse28.*"})
public class H28seConfiguration {
	@Bean(name = "h28seManager")
	public Manager manager() {
		H28seParserFactory parserFactory = new H28seParserFactory();
		return new Manager(parserFactory);
	}

}
