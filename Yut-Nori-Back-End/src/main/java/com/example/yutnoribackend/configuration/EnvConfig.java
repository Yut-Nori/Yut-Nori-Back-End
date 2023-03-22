package com.example.yutnoribackend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
//env.propoerties 파일 소스 등록
@PropertySources(@PropertySource("classpath:properties/env.properties"))
public class EnvConfig {
}
