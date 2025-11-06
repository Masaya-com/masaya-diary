package jp.co.sss.com.masaya.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.com.masaya.diary.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
}