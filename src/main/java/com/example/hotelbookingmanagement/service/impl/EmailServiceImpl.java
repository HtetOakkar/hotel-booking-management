package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.service.EmailService;
import com.example.hotelbookingmanagement.util.EmailTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async("threadPoolTaskExecutor")
    @Override
    public void sendEmail(String to, String subject, String body)  {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("noreply-oakkar@gmail.com");
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }

        mailSender.send(mimeMessage);
    }
}
