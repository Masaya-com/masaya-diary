package jp.co.sss.com.masaya.diary.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.com.masaya.diary.entity.Diary_entry;
import jp.co.sss.com.masaya.diary.entity.User;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import jp.co.sss.com.masaya.diary.service.DiaryService;

@Controller
public class EntryController {
	@Autowired
	private DiaryService diaryService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(path = { "/diary/new" } , method = RequestMethod.GET)
	public String redirectToNew(Model model , Principal principal) {
		model.addAttribute("today", java.time.LocalDate.now());
		String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Integer userId = user.getId();
		model.addAttribute("todayTodos", diaryService.getTodosForToday(userId));
		return "entry";
	}
	
	@RequestMapping(path = { "/diary/entry" } , method = RequestMethod.POST)
	public String submitEntry(
		@RequestParam("doneToday") String doneToday,
		@RequestParam("impression") String impression,
		@RequestParam("mood") Integer mood,
		@RequestParam("todoTomorrow") String todoTomorrow,
		Principal principal
	) {
		Diary_entry entry = new Diary_entry();
		String username = principal.getName();
        User user = userRepository.findByUsername(username);
		entry.setUserId(user.getId()); 
		entry.setEntryDate(java.time.LocalDate.now());
		entry.setDoneToday(doneToday);
		entry.setReflection(impression);
		entry.setMood(mood);
		entry.setTodoTomorrow(todoTomorrow);
		diaryService.getDiaryEntryRepository().save(entry);
		return "redirect:/diary/top";
	}

}