package com.maghrebia.user.service;

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
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendVerificationEmail(String to, String username,
                                      String emailTemplate,
                                      String confirmationUrl,
                                      String activationCode,
                                      String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());


        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("contact.maghrebia1@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(emailTemplate, context);
        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendResetPasswordEmail(String to, String username,
                                       String emailTemplate,
                                       String resetUrl,
                                       String subject) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("resetUrl", resetUrl);

        Context context = new Context();
        context.setVariables(properties);
        mimeMessageHelper.setFrom("contact.maghrebia1@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(emailTemplate, context);
        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendLoginAccountEmail(String to, String username,
                                      String emailTemplate,
                                      String password,
                                      String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("email", to);
        properties.put("password", password);

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
