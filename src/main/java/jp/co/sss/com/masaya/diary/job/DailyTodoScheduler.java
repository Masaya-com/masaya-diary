package jp.co.sss.com.masaya.diary.job;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jp.co.sss.com.masaya.diary.repository.Notification_preferenceRepository;
import jp.co.sss.com.masaya.diary.service.NotificationSendService;

@Component
public class DailyTodoScheduler {

  private final Notification_preferenceRepository prefRepo;
  private final NotificationSendService sendService;

  public DailyTodoScheduler(Notification_preferenceRepository prefRepo,
                            NotificationSendService sendService) {
    this.prefRepo = prefRepo;
    this.sendService = sendService;
  }

  @Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
  public void tick() {
    var now = java.time.ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
    int hour = now.getHour();
    int minute = now.getMinute();
    LocalDate today = now.toLocalDate();

    prefRepo.findAll().forEach(p -> {
      Integer notifyHour = p.getNotifyHour();
      if (notifyHour == null) notifyHour = 7; // 既定
      boolean sentToday = today.equals(p.getLastNotifiedDate());

      if (!sentToday && minute == 0 && hour == notifyHour) {
        Integer userId = p.getUserId();            // @Idが別なら getUserId() で取得
        // usersテーブルのメールを参照しても良い。今回は簡単のためpref側で持っていない想定なので固定/外部取得に合わせる
        String emailTo = "masaya.0630.a@gmail.com"; // ← 実際は users.email を引く
        sendService.notifyUser(userId, emailTo);
        sendService.markSentToday(userId);
      }
    });
  }
}
