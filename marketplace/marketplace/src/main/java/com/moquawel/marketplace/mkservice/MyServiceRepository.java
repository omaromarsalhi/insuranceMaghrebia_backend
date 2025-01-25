package com.moquawel.marketplace.mkservice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyServiceRepository extends MongoRepository<MyService, String> {

    List<MyService> findAllByCategoryId(String name);
    void deleteMyServiceByCategoryId(String id);

}
