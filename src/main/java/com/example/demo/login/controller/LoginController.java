package com.example.demo.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	//ログイン画面のGET用コントローラー
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getLogin(ModelAndView mav) {
		
		mav.setViewName("/login/login");
		return mav;
	}
	
	//ログイン画面のPOST用コントローラー
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView postLogin(ModelAndView mav) {
		
		//ホーム画面に遷移
		return new ModelAndView("redirect:/home");
	}
}
