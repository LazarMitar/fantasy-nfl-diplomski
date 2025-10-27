package rs.fantasy.fantasyfootball.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        String verificationUrl =  "http://localhost:4200/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Aktivacija naloga - Fantasy NFL");
        message.setText("Pozdrav!\n\n" +
                "Hvala što ste se registrovali na Fantasy NFL platformu. " +
                "Kliknite na link ispod da aktivirate nalog:\n\n" +
                verificationUrl + "\n\n" +
                "Link ističe za 24 sata.\n\n" +
                "Ako niste vi kreirali nalog, ignorišite ovaj email.\n\n" +
                "Srdačan pozdrav,\nFantasy NFL Tim");

        mailSender.send(message);
    }
}