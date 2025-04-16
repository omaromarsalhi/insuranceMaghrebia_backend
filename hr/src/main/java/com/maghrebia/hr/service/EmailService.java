package com.maghrebia.hr.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Async
    public void sendInterviewTimeEmail(String to, String candidateName,
                                      String emailTemplate,
                                      String interviewDateTime,
                                      String location,
                                      String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("candidateName", candidateName);
        properties.put("interviewDateTime", interviewDateTime);
        properties.put("location", location);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("contact.maghrebia1@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(emailTemplate, context);
        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }

}
