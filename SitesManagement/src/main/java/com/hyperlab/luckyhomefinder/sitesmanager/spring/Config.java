package com.hyperlab.luckyhomefinder.sitesmanager.spring;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hyperlab.luckyhomefinder.repository.spring.ReposConf;

@Configuration
@Import(ReposConf.class)
public class Config {

}
