package com.example.waggle.global.service.email;

import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.redis.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisService redisService;
    private final long expire_period = 1000L * 60L * 30;    // 30분
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Async
    public void sendMail(String email, String type) {
        String authCode = generateAuthCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(new InternetAddress("teamwagglewaggle@gmail.com", "Waggle"));
            mimeMessageHelper.setSubject("[와글] 회원가입을 위해 메일을 인증해 주세요.");
            mimeMessageHelper.setText(setContext(authCode, type), true);
            javaMailSender.send(mimeMessage);
            redisService.setValueWithExpiration(AUTH_CODE_PREFIX + email, authCode, expire_period);
        } catch (MessagingException e) {
            throw new MemberHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateAuthCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }
        return key.toString();
    }


    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }


}
