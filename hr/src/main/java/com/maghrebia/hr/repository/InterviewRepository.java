package com.maghrebia.hr.repository;

import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.entity.Interview;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {
    Interview findInterviewByCandidateId(Candidate candidate);
    @Aggregation(pipeline = {
            "{ $addFields: { sortOrder: { $switch: { branches: [" +
                    "{ case: { $eq: [\"$status\", \"SCHEDULED\"] }, then: 1 }," +
                    "{ case: { $eq: [\"$status\", \"COMPLETED\"] }, then: 2 }," +
                    "{ case: { $eq: [\"$status\", \"CANCELLED\"] }, then: 3 }" +
                    "], default: 4 } } } }",
            "{ $sort: { sortOrder: 1 } }"
    })
    List<Interview> findAllSorted();

}
