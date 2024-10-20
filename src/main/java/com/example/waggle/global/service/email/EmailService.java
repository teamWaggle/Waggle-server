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
        String authNum = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setFrom(new InternetAddress("wjdgks1233@gmail.com", "Waggle"));
            mimeMessageHelper.setSubject("[와글] 회원가입을 위해 메일을 인증해 주세요."); // 메일 제목
            mimeMessageHelper.setText(setContext(authNum, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            redisService.setValueWithExpiration(AUTH_CODE_PREFIX + email, authNum, expire_period);
        } catch (MessagingException e) {
            throw new MemberHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }


    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }


}
