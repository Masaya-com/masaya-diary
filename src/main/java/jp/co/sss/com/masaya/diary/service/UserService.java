package jp.co.sss.com.masaya.diary.service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.sss.com.masaya.diary.entity.User;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import jp.co.sss.com.masaya.diary.validation.UserValidation;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email); 
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません。"); 
        }
        return new UserPrincipal(user); 
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); 
    }
    
	
	@Transactional
	public void save(UserValidation userValidation) {
        User user = new User();
        user.setUsername(userValidation.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userValidation.getPasswordHash()));
        user.setEmail(userValidation.getEmail());
        userRepository.save(user);
    }

	
	

}