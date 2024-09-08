package org.example.mailsender;

import org.example.mailsender.mail.MailSenderDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class MailSenderApplication {

    @Autowired
    private MailSenderDemo mailSender;

    public static void main(String[] args) {
        SpringApplication.run(MailSenderApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public ResponseEntity<?> sendMail() {
        return mailSender.sendSimpleMail(
                "hjorayev700@gmail.com"
        );
    }
}
