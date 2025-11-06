package jp.co.sss.com.masaya.diary.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jp.co.sss.com.masaya.diary.entity.Push_subscription;
import jp.co.sss.com.masaya.diary.repository.Notification_preferenceRepository;
import jp.co.sss.com.masaya.diary.repository.Push_subscriptionRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;

@Service
public class NotificationSendService {

  private final JavaMailSender mailSender;
  private final Push_subscriptionRepository pushRepo;
  private final NotificationContentService contentService;
  private final Notification_preferenceRepository prefRepo;

  public NotificationSendService(
      JavaMailSender mailSender,
      Push_subscriptionRepository pushRepo,
      NotificationContentService contentService,
      Notification_preferenceRepository prefRepo) {
    this.mailSender = mailSender;
    this.pushRepo = pushRepo;
    this.contentService = contentService;
    this.prefRepo = prefRepo;
  }

  @Value("${app.mail.from:${spring.mail.username:}}")
  private String mailFrom;

  @Value("${webpush.vapid.publicKey}")
  private String vapidPublicKey;

  @Value("${webpush.vapid.privateKey}")
  private String vapidPrivateKey;

  @Value("${webpush.vapid.subject}")
  private String vapidSubject;

  /** 指定ユーザーへ通知（成功/失敗はログ程度の扱い） */
  public void notifyUser(Integer userId, String emailTo) {
    // 本文を組み立て
    String emailBody = contentService.buildEmailBody(userId);
    byte[] webPushPayload = contentService.buildWebPushPayload(userId);

    // メール
    prefRepo.findByUserId(Long.valueOf(userId)).ifPresent(p -> {
      if (Boolean.TRUE.equals(p.getEnableEmail()) && emailTo != null && !emailTo.isBlank()) {
        try {
          SimpleMailMessage msg = new SimpleMailMessage();
          msg.setTo(emailTo);
          msg.setSubject("今日のTODO（まさやの日常）");
          msg.setText(emailBody);
          if (mailFrom != null && !mailFrom.isBlank()) msg.setFrom(mailFrom);
          mailSender.send(msg);
        } catch (Exception e) {
          // ログだけ（必要なら logger を使う）
          System.err.println("Mail failed: " + e.getMessage());
        }
      }
    });

    // WebPush
    try {
      var keyPair = new java.security.KeyPair(
          Utils.loadPublicKey(vapidPublicKey),
          Utils.loadPrivateKey(vapidPrivateKey));
      var pushService = new PushService(keyPair, vapidSubject);

      List<Push_subscription> subs = pushRepo.findAllByUserId(userId);
      for (var s : subs) {
        try {
          var notif = new Notification(
              s.getEndpoint(), s.getP256dh(), s.getAuth(), webPushPayload);
          pushService.send(notif);
        } catch (Exception e) {
          System.err.println("WebPush failed to one endpoint: " + e.getMessage());
        }
      }
    } catch (Exception e) {
      System.err.println("WebPush init failed: " + e.getMessage());
    }
  }

  /** 同日二重送信防止（送ったら印をつける） */
  public void markSentToday(Integer userId) {
    prefRepo.findByUserId(Long.valueOf(userId)).ifPresent(p -> {
      p.setLastNotifiedDate(LocalDate.now());
      prefRepo.save(p);
    });
  }
}
