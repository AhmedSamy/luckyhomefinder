package com.hyperlab.luckyhomefinder.service.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.hyperlab.luckyhomefinder.repository.spring.ReposConf;

@Configuration
@EnableMongoRepositories
@Import({ReposConf.class})
public class Appconfig {

}
