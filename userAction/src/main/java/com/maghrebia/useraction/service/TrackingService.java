package com.maghrebia.useraction.service;

import com.maghrebia.useraction.Repository.TrackingRepository;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.UserAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                        .build()
        );
        action.setCreatedAt(LocalDateTime.now());
        userAction.getActions().add(action);
        return trackingRepository.save(userAction);
    }

}


