package com.maghrebia.useraction.entity;

import java.util.Map;

public class EngagementScoring {

    private static final Map<String, Integer> SCORE_MAP = Map.of(
            "POSTULER_OFFRE", 10,
            "AJOUTER_FAVORI", 8,
            "LIRE_DESCRIPTION", 5,
            "Chat", 6,
            "SCROLLER", 2,
            "Click_Show_offer", 1,
            "Form_offer_Submission", 10,
            "Payment_transaction", 10
    );

    public static int getTimeBasedScore(String actionType, long timeSpent) {
        int baseScore = SCORE_MAP.getOrDefault(actionType, 0);
        System.out.println(baseScore);
        if (timeSpent < 5000) {
            return baseScore;
        } else if (timeSpent < 30000) {
            return baseScore + 2;
        } else {
            return baseScore + 5;
        }
    }


}
