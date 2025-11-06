package jp.co.sss.com.masaya.diary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.com.masaya.diary.entity.Notification_preference;

public interface Notification_preferenceRepository extends JpaRepository<Notification_preference, Integer> {

	Optional<Notification_preference> findByUserId(Long userId);
	
	

}
