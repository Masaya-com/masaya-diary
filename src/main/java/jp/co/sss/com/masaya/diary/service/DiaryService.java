package jp.co.sss.com.masaya.diary.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jp.co.sss.com.masaya.diary.entity.Diary_entry;
import jp.co.sss.com.masaya.diary.repository.Diary_entryRepository;

@Service
public class DiaryService {
	private final Diary_entryRepository diaryEntryRepository;
	
	public DiaryService(Diary_entryRepository diaryEntryRepository) {
		this.diaryEntryRepository = diaryEntryRepository;
	}
	
	public Diary_entryRepository getDiaryEntryRepository() {
		return diaryEntryRepository;
	}
	
    public List<Diary_entry> getTodayTaskList(Integer userId) {
        return diaryEntryRepository.findByUserIdOrderByEntryDateDesc(Long.valueOf(userId))
            .stream()
            .filter(entry -> entry.getEntryDate().equals(LocalDate.now()))
            .toList();
    }
 

    public List<Diary_entry> getRecentlyTaskList(Integer userId) {
        return diaryEntryRepository.findByUserIdOrderByEntryDateDesc(Long.valueOf(userId))
            .stream()
            .limit(5)
            .toList();
    }
    
    public List<Diary_entry> getDiaryEntryList(Integer userId){
    	    return diaryEntryRepository.findByUserIdOrderByEntryDateDesc(Long.valueOf(userId))
    	    		.stream()
    	    		.toList();
    	 }
    
 // 追加：yyyy-MM の文字列を受け取り、該当月を検索
    public List<Diary_entry> searchByMonth(Integer userId, String searchMonth) {
        // 未指定なら従来通りの一覧
        if (searchMonth == null || searchMonth.isBlank()) {
            return getDiaryEntryList(userId);
        }
        try {
            java.time.YearMonth ym = java.time.YearMonth.parse(searchMonth); // 例: "2025-10"
            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();
            return diaryEntryRepository.findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(
                    Long.valueOf(userId), start, end);
        } catch (Exception e) {
            // フォーマット不正時は全件にフォールバック（運用に合わせて変更可）
            return getDiaryEntryList(userId);
        }
    }

    private static List<String> tokenize(String s){
        if (s == null || s.trim().isEmpty()) return java.util.Collections.emptyList();
        return java.util.Arrays.stream(s.split("[\\r\\n、,\\s]+"))
                .filter(t -> !t.isBlank())
                .toList();
    }

    public List<String> getTodosForToday(Integer userId){
        LocalDate todayJst = LocalDate.now();
        LocalDate yesterday = todayJst.minusDays(1);

        return diaryEntryRepository.findByUserIdAndEntryDate(Long.valueOf(userId), yesterday)
                .stream()
                .map(Diary_entry::getTodoTomorrow)
                .filter(Objects::nonNull)
                .flatMap(s -> tokenize(s).stream())
                .toList();
    }




}