package org.example.mailsender.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
@Slf4j
public class MailSenderDemo {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

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

    @GetMapping("/sendAttachment/")
    public ResponseEntity<?> sendSimpleMailWithAttachment(@RequestParam("to") String to,
                                                          @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Random random = new Random();
        helper.setTo(to);
        helper.setFrom("husanboybackanddeveloper@gmail.com");
        helper.setSubject("HH.UZ");
        int i = random.nextInt(1000, 9999);
        helper.setText(Integer.toString(i));
        User user = User.builder()
                .email(to)
                .code(Integer.toString(i))
                .build();
        userRepository.save(user);
        if (file != null) {
            File tempFile = File.createTempFile("upload-", Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(tempFile);

            FileSystemResource resource = new FileSystemResource(tempFile);
            helper.addAttachment(Objects.requireNonNull(resource.getFilename()), resource);
            mailSender.send(mimeMessage);
            tempFile.deleteOnExit();
        } else
            mailSender.send(mimeMessage);
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
