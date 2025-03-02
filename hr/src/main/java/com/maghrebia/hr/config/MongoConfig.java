package com.maghrebia.hr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.maghrebia.hr.repository")
@EnableMongoAuditing
@Configuration
public class MongoConfig {
}
