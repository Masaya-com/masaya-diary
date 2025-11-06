package jp.co.sss.com.masaya.diary.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.com.masaya.diary.entity.User;
import jp.co.sss.com.masaya.diary.repository.UserRepository;
import jp.co.sss.com.masaya.diary.service.DiaryService;

@Controller
public class DiaryController {
	@Autowired
	private DiaryService diaryService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(path = { "/diary/list"}, method = RequestMethod.GET)
	public String redirectToList(
	        @RequestParam(name = "searchMonth", required = false)
	        String searchMonth,
	        Model model , Principal principal) {
		String username = principal.getName();
        User user = userRepository.findByUsername(username);
        Integer userId = user.getId();
	    model.addAttribute("searchMonth", searchMonth); // 入力保持用
	    model.addAttribute("diaryEntryList", diaryService.searchByMonth(userId, searchMonth));
	    return "list";
	}


}
