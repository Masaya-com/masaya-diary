// src/main/java/jp/co/sss/com/masaya/diary/service/DailyNotifier.java
package jp.co.sss.com.masaya.diary.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jp.co.sss.com.masaya.diary.entity.Notification_preference;
import jp.co.sss.com.masaya.diary.repository.Notification_preferenceRepository;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import jp.co.sss.com.masaya.diary.entity.User;

@Component
public class DailyNotifier {

  private static final ZoneId TZ = ZoneId.of("Asia/Tokyo");

  private final Notification_preferenceRepository prefRepo;
  private final NotificationSendService sendService;
  private final UserRepository userRepository;

  public DailyNotifier(Notification_preferenceRepository prefRepo,
                       NotificationSendService sendService,
                       UserRepository userRepository) {
    this.prefRepo = prefRepo;
    this.sendService = sendService;
    this.userRepository = userRepository;
  }

  // 毎時 0 分に実行（日本時間基準）
  @Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
  public void tickHourly() {
    var now = java.time.ZonedDateTime.now(TZ);
    int hour = now.getHour();
    LocalDate today = now.toLocalDate();

    List<Notification_preference> prefs = prefRepo.findAll();
    for (var p : prefs) {
      if (p.getNotifyHour() == null) continue;

      // 時刻一致 & 同日未送信
      boolean due = (p.getNotifyHour() == hour);
      boolean notYet = (p.getLastNotifiedDate() == null || !p.getLastNotifiedDate().isEqual(today));

      if (due && notYet) {
        Integer userId = p.getUserId();
        // ユーザーIDからメールアドレスを取得
        String emailTo = userRepository.findById(userId)
            .map(User::getEmail)
            .orElse(null);
        sendService.notifyUser(userId, emailTo);
        sendService.markSentToday(userId); // 二重送信防止
      }
    }
  }
}