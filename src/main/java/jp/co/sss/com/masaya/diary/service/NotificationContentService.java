package jp.co.sss.com.masaya.diary.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.com.masaya.diary.entity.Diary_entry;
import jp.co.sss.com.masaya.diary.repository.Diary_entryRepository;

@Service
public class NotificationContentService {
	
	@Autowired
	private Diary_entryRepository diaryEntryRepository;
	
	private static List<String> tokenize(String s){
        if (s == null || s.trim().isEmpty()) return java.util.Collections.emptyList();
        return java.util.Arrays.stream(s.split("[\\r\\n、,\\s]+"))
                .filter(t -> !t.isBlank())
                .toList();
    }
	
	public List<String> getTodayTodosFromYesterday(Integer userId) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		List<Diary_entry> y = diaryEntryRepository.findByUserIdAndEntryDate(Long.valueOf(userId),yesterday);
		if(!y.isEmpty()) {
			String todoTomorrow = y.get(0).getTodoTomorrow();
			return tokenize(todoTomorrow);
		}
		
		return java.util.Collections.emptyList();
	}
	
	
	public String buildEmailBody(Integer userId) {
	    var lines = getTodayTodosFromYesterday(userId);
	    var sb = new StringBuilder();
	    sb.append("おはようございます！本日のTODOです。\n\n");
	    if (lines.isEmpty()) {
	      sb.append("（前日の「明日やること」が未登録です）\n");
	    } else {
	      for (String s : lines) sb.append("・").append(s).append("\n");
	    }
	    sb.append("\n— まさやの日常\n");
	    return sb.toString();
	  }

	public byte[] buildWebPushPayload(Integer userId) {
	    var lines = getTodayTodosFromYesterday(userId);
	    String body = lines.isEmpty()
	        ? "前日の「明日やること」が未登録です。"
	        : String.join(" / ", lines);

	    // 表示内容をJSONとして組み立て
	    String json = """
	    {
	      "title": "今日のタスクを確認",
	      "body": "%s",
	      "url": "/masaya-diary/diary/top",
	      "icon": "/icons/icon.png",
	      "badge": "/icons/badge.png"
	    }
	    """.formatted(body.replace("\"", "\\\"")); // ダブルクォートだけ念のためエスケープ

	    return json.getBytes(StandardCharsets.UTF_8);
	}

}
