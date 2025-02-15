package com.maghrebia.user.config;

import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.")
@EnableMongoAuditing
public class MongoConfig {

}
