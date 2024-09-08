package org.example.mailsender.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailSenderDemo {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final Set<String> set = new HashSet<>();

    @GetMapping("/sendMessage/")
    public ResponseEntity<?> sendSimpleMail(@RequestParam("to") String to) {
        Random random = new Random();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("husanboybackanddeveloper@gmail.com");
        message.setTo(to);
        message.setSubject("HH.UZ");
        int i = random.nextInt(1000, 9999);
        message.setText(Integer.toString(i));
        User user = User.builder()
                .email(to)
                .code(Integer.toString(i))
                .build();
        userRepository.save(user);
        mailSender.send(message);
        return ResponseEntity.ok("MESSAGE SENT SUCCESSFULLY");
    }


    @GetMapping("/success/")
    public ResponseEntity<?> success(@RequestParam("id") Integer id,
                                     @RequestParam("code") String code) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null && user.getCode().equals(code)) {
            return ResponseEntity.ok("YOU HAVE LOGIN SUCCESS");
        }
        return ResponseEntity.ok("CODE IS INCORRECT");
    }
}
