package com.maghrebia.useraction.Repository;

import com.maghrebia.useraction.entity.ReportResponse;
import com.maghrebia.useraction.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<ReportResponse,String>  {

    List<ReportResponse> findByUserId(String userId);
    ReportResponse findTopByUserIdOrderByCreatedAtDesc(String userId);
}
