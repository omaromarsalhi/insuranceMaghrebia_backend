package com.maghrebia.useraction.service;

import com.maghrebia.useraction.Repository.TrackingRepository;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.EngagementScoring;
import com.maghrebia.useraction.entity.UserAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public UserAction saveUserAction(String userId, Action action) {
        Optional<UserAction> existingUserAction = trackingRepository.findByuserId(userId);
        UserAction userAction = existingUserAction.orElseGet(() ->
                UserAction.builder()
                        .userId(userId)
                        .actions(new ArrayList<>())
                        .dailyScores(new HashMap<>())
                        .build()
        );
        action.setCreatedAt(LocalDateTime.now());
        userAction.getActions().add(action);

        int actionScore = EngagementScoring.getTimeBasedScore(action.getEventType(),action.getTimeSpent());
        System.out.println(actionScore);
        LocalDate today = action.getCreatedAt().toLocalDate();
        userAction.getDailyScores().merge(today, actionScore, Integer::sum);
        return trackingRepository.save(userAction);
    }
    public Map<LocalDate, Integer> getUserScoresPerDay(String userId) {
        UserAction userAction = trackingRepository.findByuserId(userId).get();
        return userAction != null ? userAction.getDailyScores() : null;
    }
}



