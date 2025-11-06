package jp.co.sss.com.masaya.diary.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.sss.com.masaya.diary.entity.User;
import jp.co.sss.com.masaya.diary.service.UserService;
import jp.co.sss.com.masaya.diary.validation.UserValidation;


@Controller
public class RegisterController {
	@Autowired
	private UserService userService;

	@RequestMapping(path = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute UserValidation userValidation) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", new UserValidation());
        mav.setViewName("register");
        return mav;
    }

	@RequestMapping(path = "/register" , method = RequestMethod.POST)
	public ModelAndView register(@Valid @ModelAttribute UserValidation userValidation, BindingResult result,HttpSession session) { 
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.addObject("user", userValidation);
			mav.setViewName("register");
			return mav;
		}
		User existing = userService.findByUsername(userValidation.getUsername()); 
		if(existing != null){ 
			mav.addObject("user", userValidation); 
			mav.setViewName("register");
			return mav; 
		} 
		userService.save(userValidation); 
		mav.setViewName("login"); 
		return mav; 
	}

}