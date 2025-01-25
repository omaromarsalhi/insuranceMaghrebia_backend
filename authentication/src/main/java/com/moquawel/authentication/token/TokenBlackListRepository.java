package com.moquawel.authentication.token;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TokenBlackListRepository extends MongoRepository<TokenBlackList, String> {

    Optional<TokenBlackList> findByJti(String jti);
}
