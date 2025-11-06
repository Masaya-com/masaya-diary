package jp.co.sss.com.masaya.diary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.com.masaya.diary.entity.Push_subscription;

public interface Push_subscriptionRepository extends JpaRepository<Push_subscription,Integer> {
		Optional<Push_subscription> findByUserId(Integer userId);
		List<Push_subscription> findAllByUserId(Integer userId);
		void deleteByEndpoint(String endpoint);

}
