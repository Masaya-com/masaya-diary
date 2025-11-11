package jp.co.sss.com.masaya.diary.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("prod")
public class MailConfig {

    // spring.mail.* の値（application.properties ＆ 環境変数）を束ねたもの
    @Bean
    public JavaMailSender mailSender(MailProperties props) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(props.getHost());                    // 必須: smtp.gmail.com 等
        sender.setPort(props.getPort() == null ? 587 : props.getPort());
        sender.setUsername(props.getUsername());
        sender.setPassword(props.getPassword());

        Properties javaMailProps = new Properties();
        // application.properties の spring.mail.properties.* を取り込みつつ、足りなければ補完
        if (props.getProperties() != null) {
            javaMailProps.putAll(props.getProperties());
        }
        // よくある既定値（未指定なら補う）
        javaMailProps.putIfAbsent("mail.smtp.auth", "true");
        javaMailProps.putIfAbsent("mail.smtp.starttls.enable", "true");
        javaMailProps.putIfAbsent("mail.smtp.connectiontimeout", "10000");
        javaMailProps.putIfAbsent("mail.smtp.timeout", "10000");
        javaMailProps.putIfAbsent("mail.smtp.writetimeout", "10000");

        sender.setJavaMailProperties(javaMailProps);
        return sender;
    }
}
