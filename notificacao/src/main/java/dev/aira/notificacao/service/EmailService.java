package dev.aira.notificacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(String para, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}

