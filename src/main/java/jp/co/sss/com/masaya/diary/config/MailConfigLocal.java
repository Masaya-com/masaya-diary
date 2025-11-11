package jp.co.sss.com.masaya.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("local") // ← ローカル起動時だけ有効
public class MailConfigLocal {
    @Bean
    public JavaMailSender mailSender() {
        // 送信しないダミー。必要なら localhost:1025 などに向けてMailHog等へ
        return new JavaMailSenderImpl();
    }
}
