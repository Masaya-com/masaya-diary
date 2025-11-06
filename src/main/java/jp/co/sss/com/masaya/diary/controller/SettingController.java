package jp.co.sss.com.masaya.diary.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sss.com.masaya.diary.entity.Notification_preference;
import jp.co.sss.com.masaya.diary.entity.Push_subscription;
import jp.co.sss.com.masaya.diary.repository.Notification_preferenceRepository;
import jp.co.sss.com.masaya.diary.repository.Push_subscriptionRepository;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;

@Controller
public class SettingController {
	
	private final Notification_preferenceRepository repo;

    @Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	public SettingController(Notification_preferenceRepository repo) {
		this.repo = repo;
	}
	
	@Value("${app.mail.from:${spring.mail.username:}}")
    private String mailFrom;

    @Value("${webpush.vapid.publicKey}")
    private String vapidPublicKey; // Base64URL

    @Value("${webpush.vapid.privateKey}")
    private String vapidPrivateKey; // Base64URL

    @Value("${webpush.vapid.subject}")
    private String vapidSubject; // mailto:you@example.com 等

	
	@RequestMapping(path = "/diary/setting", method = RequestMethod.GET)
	public String GoToSetting(Model model, Principal principal) {
		String username = principal.getName();
		int userId = userRepository.findByUsername(username).getId();
		Notification_preference prefs = repo.findAll().stream()
			.filter(p -> p.getUserId() == userId)
			.findFirst()
			.orElseGet(() -> {
				Notification_preference p = new Notification_preference();
				p.setUserId(userId);
				p.setEnableWebPush(true);
				p.setEnableEmail(true);
				p.setNotifyHour(7);
				return p;
			});
		model.addAttribute("prefs", prefs);
		return "setting";
		
	}
	
    @RequestMapping(path = "/diary/settingPost", method = RequestMethod.POST)
    public String save(@ModelAttribute("prefs") Notification_preference prefs, Principal principal) {
		String username = principal.getName();
		int userId = userRepository.findByUsername(username).getId();
		prefs.setUserId(userId);
        // バリデーション（範囲外は丸め）
        if (prefs.getNotifyHour() == null) prefs.setNotifyHour(7);
        if (prefs.getNotifyHour() < 0) prefs.setNotifyHour(0);
        if (prefs.getNotifyHour() > 23) prefs.setNotifyHour(23);
        repo.save(prefs); // 既存なら更新、無ければ作成
        return "redirect:/diary/top"; // PRG
    }


    // ====== メール動作確認 ======
    @GetMapping("/diary/testMail")
    @ResponseBody
    public String testMail(Principal principal) {
        try {
            String username = principal.getName();
            String emailTo = userRepository.findByUsername(username).getEmail();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo); // ユーザーの登録メールアドレスに送信
            message.setSubject("Test Mail");
            message.setText("This is a test mail from masaya-diary.");
            if (mailFrom != null && !mailFrom.isBlank()) {
                message.setFrom(mailFrom);
            }
            mailSender.send(message);
            return "Mail sent!";
        } catch (Exception e) {
            return "Mail send failed: " + e.getMessage();
        }
    }

 // JSON受信用DTO
    static class SubscribeDTO {
      public String endpoint;
      public String p256dh;
      public String auth;
    }

    @Autowired
    private Push_subscriptionRepository pushRepo;

    // 購読を保存
    @PostMapping("/diary/push/subscribe")
    @ResponseBody
    public String subscribe(@RequestBody SubscribeDTO dto, Principal principal) {
		String username = principal.getName();
		int userId = userRepository.findByUsername(username).getId();
		var ps = new Push_subscription();
		ps.setUserId(userId);
		ps.setEndpoint(dto.endpoint);
		ps.setP256dh(dto.p256dh);
		ps.setAuth(dto.auth);
		pushRepo.save(ps);
		return "Subscribed!";
	}

    // DBに保存された購読を使って送信（ダミーではなく実値）
    @GetMapping("/diary/testWebPush")
    @ResponseBody
    public String testWebPush(Principal principal) {
      try {
        String username = principal.getName();
        int userId = userRepository.findByUsername(username).getId();
        var keyPair = new java.security.KeyPair(
          Utils.loadPublicKey(vapidPublicKey),   // @Value で読んだ値（Base64URL）
          Utils.loadPrivateKey(vapidPrivateKey)
        );
        var pushService = new PushService(keyPair, vapidSubject);

        var sub = pushRepo.findByUserId(userId)
          .orElseThrow(() -> new IllegalStateException("No subscription"));

        var payload = """
          {"title":"Masaya Diary","body":"Test WebPush from masaya-diary!"}
          """;

        var notification = new Notification(
          sub.getEndpoint(),
          sub.getP256dh(),
          sub.getAuth(),
          payload.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
        pushService.send(notification);
        return "WebPush sent!";
      } catch (Exception e) {
        return "WebPush send failed: " + e.getMessage();
      }
    }
    
    static class UnsubscribeDTO { public String endpoint; }

    @PostMapping("/diary/push/unsubscribe")
    @ResponseBody
    public String unsubscribe(@RequestBody UnsubscribeDTO dto) {
      if (dto.endpoint == null || dto.endpoint.isBlank()) return "No endpoint";
      pushRepo.deleteByEndpoint(dto.endpoint);
      return "Unsubscribed";
    }
    


}