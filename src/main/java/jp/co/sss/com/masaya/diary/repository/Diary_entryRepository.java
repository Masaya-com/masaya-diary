package jp.co.sss.com.masaya.diary.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.com.masaya.diary.entity.Diary_entry;

public interface Diary_entryRepository extends JpaRepository<Diary_entry, Long> {
	List<Diary_entry> findByUserIdOrderByEntryDateDesc(Long userId);
	List<Diary_entry> findByUserIdAndEntryDate(Long userId, LocalDate entryDate);
	// 追加分（範囲検索＋降順）
	List<Diary_entry> findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(
	        Long userId, LocalDate start, LocalDate end);

}