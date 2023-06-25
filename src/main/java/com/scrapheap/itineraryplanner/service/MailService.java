package com.scrapheap.itineraryplanner.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService{

    @Autowired
    private JavaMailSender mailSender;


    @Value("${mail.email}")
    private String email;


    @Async
    public void sendEmail(String to, String subject, String message){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            mimeMessage.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(email);
            helper.setTo(to);
            helper.setText(message, true);
            this.mailSender.send(mimeMessage);
        }catch (MessagingException e){

        }

    }
}
