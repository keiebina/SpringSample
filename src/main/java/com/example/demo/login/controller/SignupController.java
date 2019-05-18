package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.UserService;

@Controller
public class SignupController {
	
	@Autowired
	private UserService userService;
	
	//ラジオボタンの実装========================================================================================================================
	private Map<String, String> radioMarriage;
	//ラジオボタンの初期化メソッド
	private Map<String, String> initRadioMarriage(){
		Map<String, String> radio = new LinkedHashMap<>();
		//既婚、未婚をMapに格納
		radio.put("既婚", "true");
		radio.put("未婚", "false");
		return radio;
	}
	
//	//@ExceptionHandlerの使い方==================================================================================================================
//	@ExceptionHandler(DataAccessException.class)
//	public String DataAccessExceptionHandler(DataAccessException e, Model model) {
//		//例外クラスのメッセージを登録
//		model.addAttribute("error", "内部サーバーエラー（DB) : ExceptionHandler");
//		//例外クラスのメッセージを登録
//		model.addAttribute("message", "SignupControllerでDataAccessExeptionが発生しました");
//		//HTTPのエラーコード(500)を登録
//		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
//		return "error";
//	}
//		
//	//@ExceptionHandlerの使い方=================================================================================================================
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(Exception e, Model model) {
//		//例外クラスのメッセージを登録
//		model.addAttribute("error", "内部サーバーエラー : ExceptionHandler");
//		//例外クラスのメッセージを登録
//		model.addAttribute("message", "SignupControllerでExceptionが発生しました");
//		//HTTPのエラーコード(500)を登録
//		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
//		return "error";
//	}
	
	//ユーザー登録画面のGET用コントローラー======================================================================================================
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView getSignUp(@ModelAttribute SignupForm form, ModelAndView mav) {
		System.out.println("ユーザー登録画面のGET用コントローラーが呼ばれました");
		//ラジオボタンの初期化メソッド呼び出し
		radioMarriage = initRadioMarriage();
		//ラジオボタン用のMapを登録
		mav.addObject("radioMarriage", radioMarriage);
		mav.setViewName("login/signup");
		return mav;
	}
	
	//ユーザー登録画面のPOST用コントローラー=====================================================================================================
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView postSignUp(@ModelAttribute @Validated(GroupOrder.class) SignupForm form, BindingResult bindingResult, ModelAndView mav) {
		
		//入力チェックに引っかかった際の処理
		if(bindingResult.hasErrors()) {
			//GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
			return getSignUp(form, mav);
		}
		
		//formの中身を出力
		System.out.println(form);
		
		//insert用変数
		User user = new User();
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());
		user.setRole("ROLE_GENERAL");      //ロール（一般）
		
		//ユーザー登録処理
		boolean result = userService.insert(user);
		
		//ユーザー登録結果の判定
		if(result == true) {
			System.out.println("insert成功");
		}else {
			System.out.println("insert失敗");
		}
		//login.htmlへリダイレクト
		return new ModelAndView("redirect:/login");
	}
	
}
