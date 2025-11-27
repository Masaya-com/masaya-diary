package jp.co.sss.com.masaya.diary.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class MailConfig {

    @Bean
    public JavaMailSender mailSender(MailProperties props) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(props.getHost());
        sender.setPort(props.getPort() == null ? 587 : props.getPort());
        sender.setUsername(props.getUsername());
        sender.setPassword(props.getPassword());

        Properties p = new Properties();
        if (props.getProperties() != null) p.putAll(props.getProperties());
        p.putIfAbsent("mail.smtp.auth", "true");
        p.putIfAbsent("mail.smtp.starttls.enable", "true");
        p.putIfAbsent("mail.smtp.connectiontimeout", "10000");
        p.putIfAbsent("mail.smtp.timeout", "10000");
        p.putIfAbsent("mail.smtp.writetimeout", "10000");
        sender.setJavaMailProperties(p);
        return sender;
    }
}
