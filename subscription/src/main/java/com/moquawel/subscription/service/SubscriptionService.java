package com.moquawel.subscription.service;

import com.moquawel.subscription.request.ActivationRequest;
import com.moquawel.subscription.response.ActivationResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionService {

    public ActivationResponse activate(ActivationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println(username);
        return null;
    }

    public ActivationResponse deactivate(@Valid ActivationRequest request) {
        return null;
    }

    public ActivationResponse renew(@Valid ActivationRequest request) {
        return null;
    }
}
