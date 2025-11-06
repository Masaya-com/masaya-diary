package jp.co.sss.com.masaya.diary.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.com.masaya.diary.entity.User;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import jp.co.sss.com.masaya.diary.service.DiaryService;

@Controller
public class CommonController {
	@Autowired
	private DiaryService diaryService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(path = {"/", "/diary/top"}, method = RequestMethod.GET)
	public String redirectToTop(Model model , Principal principal) {
		model.addAttribute("today", LocalDate.now());
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Integer userId = user.getId();
		model.addAttribute("todayTodos", diaryService.getTodosForToday(userId));
		model.addAttribute("recentlyTaskList", diaryService.getRecentlyTaskList(userId));
		return "top";
	}
	

}