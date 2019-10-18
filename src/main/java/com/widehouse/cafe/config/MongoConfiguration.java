package com.widehouse.cafe.config;

import com.mongodb.client.MongoClients;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by kiel on 2017. 3. 14..
 */
@EnableMongoRepositories(basePackages = {"com.widehouse.cafe.comment.entity"})
public class MongoConfiguration {
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(MongoClients.create("mongodb://localhost"), "test");
    }
}
