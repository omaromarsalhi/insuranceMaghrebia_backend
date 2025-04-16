package com.maghrebia.useraction.service;

import com.maghrebia.useraction.Repository.TrackingRepository;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.EngagementScoring;
import com.maghrebia.useraction.entity.UserAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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
        LocalDate today = action.getCreatedAt().toLocalDate();
        userAction.getDailyScores().merge(today, actionScore, Integer::sum);
        return trackingRepository.save(userAction);
    }

    public Map<LocalDate, Integer> getUserScoresPerDay(String userId, LocalDate startDate, LocalDate endDate) {
        Optional<UserAction> optionalUserAction = trackingRepository.findByuserId(userId);
        if (optionalUserAction.isEmpty()) {
            return null;
        }

        Map<LocalDate, Integer> allScores = optionalUserAction.get().getDailyScores();

        if (startDate == null || endDate == null) {
            return allScores;
        }

        return allScores.entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public List<Action> getActionsAfterDate(String userId, LocalDateTime date) {
        return trackingRepository.findByuserId(userId)
                .map(userAction -> userAction.getActions().stream()
                        .filter(action -> action.getCreatedAt().isAfter(date))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
    public List<Action> getActionsBetweenDates(String userId, LocalDate start, LocalDate end) {
        Optional<UserAction> optionalUserAction = trackingRepository.findByuserId(userId);

        if (optionalUserAction.isEmpty()) {
            return List.of();
        }

        return optionalUserAction.get().getActions().stream()
                .filter(action -> {
                    LocalDate timestamp = action.getCreatedAt().toLocalDate();
                    return timestamp != null && !timestamp.isBefore(start) && !timestamp.isAfter(end);
                })
                .collect(Collectors.toList());
    }
}



