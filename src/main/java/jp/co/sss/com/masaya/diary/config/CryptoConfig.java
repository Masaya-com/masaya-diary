package jp.co.sss.com.masaya.diary.config;

import java.security.Security;
import java.util.Properties;

import jakarta.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class CryptoConfig {
  @PostConstruct
  public void registerBC() {
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }
  @Configuration
  @EnableScheduling
  public static class SchedulingConfig {}
  
//既存の import の下あたりに追加


//既存の CryptoConfig クラスの「中」に以下を追加
 // === Mail 手動Bean ===
 @Bean
 public JavaMailSender mailSender(
     @Value("${spring.mail.host:}") String host,
     @Value("${spring.mail.port:0}") int port,
     @Value("${spring.mail.username:}") String username,
     @Value("${spring.mail.password:}") String password,
     @Value("${spring.mail.properties.mail.smtp.auth:true}") boolean smtpAuth,
     @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") boolean starttls
 ) {
   JavaMailSenderImpl sender = new JavaMailSenderImpl();
   sender.setHost(host);
   sender.setPort(port);
   sender.setUsername(username);
   sender.setPassword(password);

   Properties props = sender.getJavaMailProperties();
   props.put("mail.smtp.auth", String.valueOf(smtpAuth));
   props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
   props.put("mail.smtp.connectiontimeout", "10000");
   props.put("mail.smtp.timeout", "10000");
   props.put("mail.smtp.writetimeout", "10000");
   return sender;
 }



}

