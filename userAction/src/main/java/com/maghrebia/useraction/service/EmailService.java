package com.maghrebia.useraction.service;


import com.maghrebia.useraction.entity.EmailRequest;
import com.maghrebia.useraction.exception.EmailSendFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private  final JavaMailSender javaMailSender;
    @Async
    public void sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(emailRequest.getRecipient());
            mimeMessageHelper.setSubject(emailRequest.getSubject());
            mimeMessageHelper.setText(emailRequest.getMessage());
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}");

            throw new EmailSendFailedException("Failed to send email to ");
        }
    }
}
