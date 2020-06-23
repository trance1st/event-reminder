package com.bogdan.eventreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    JavaMailSender javaMailSender;

    @Scheduled(fixedRate = 2000)
    public void sendEmail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("trance1st@yahoo.com");
        simpleMailMessage.setFrom("info@event-reminder.ro");
        simpleMailMessage.setSubject("Aveti un eveniment nou");
        simpleMailMessage.setText("Am gasit un eveniment pt dvs");
        javaMailSender.send(simpleMailMessage);
    }
}
