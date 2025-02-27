package com.maghrebia.complaint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@RequiredArgsConstructor
public class UtilisService {

    public String extractBetterTitle(String response) {
        Pattern pattern = Pattern.compile("A better title would be\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? matcher.group(1) : "No alternative title found";
    }
}
