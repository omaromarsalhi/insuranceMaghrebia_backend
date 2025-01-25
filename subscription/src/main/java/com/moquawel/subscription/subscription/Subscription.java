package com.moquawel.subscription.subscription;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "subscription")
public class Subscription {

    @Id
    private String subscriptionId;

    private Date started_at;

    private Date ends_at;

    private State state;

    private SubscriptionType type;

}
