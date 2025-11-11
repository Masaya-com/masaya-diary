package jp.co.sss.com.masaya.diary.config;

import java.security.Security;

import jakarta.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;
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
  



}

