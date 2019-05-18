package com.example.demo.login.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	//結婚ステータスのラジオボタン作成========================================================================
	//ラジオボタン用変数
	private Map<String, String> radioMarriage;
	//ラジオボタンの初期化メソッド
	private Map<String, String> initRadioMarriage(){
		Map<String, String> radio = new LinkedHashMap<>();
		//既婚、未婚をMapに格納
		radio.put("既婚", "true");
		radio.put("既婚", "false");
		return radio;
	}
	
	//ホーム画面のGET用メソッド===============================================================================
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getHome(ModelAndView mav) {
		//コンテンツ部分にユーザー一覧を表示するための文字列を登録
		mav.addObject("contents", "login/home :: home_contents");
		mav.setViewName("login/homeLayout");
		return mav;
	}
	
	//ユーザー一覧画面のGET用メソッド=========================================================================
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public ModelAndView getUserList(ModelAndView mav) {
		//コンテンツ部分にユーザー一覧を表示するための文字列を登録
		mav.addObject("contents", "login/userList :: userList_contents");
		//ユーザー一覧の生成・登録
		List<User> userList = userService.selectMany();
		mav.addObject("userList", userList);
		//データ件数を取得
		int count = userService.count();
		mav.addObject("userListCount", count);
		
		mav.setViewName("login/homeLayout");
		return mav;
	}
	
	//ユーザー詳細画面のGET用メソッド(動的URL)================================================================
	@RequestMapping(value = "/userDetail/{id:.+}")
	public ModelAndView getUserDetail(@ModelAttribute SignupForm form, ModelAndView mav, @PathVariable("id") String userId) {
		//ユーザーID確認（デバッグ）
		System.out.println("userId=" + userId);
		//コンテンツ部分にユーザー表示するための文字列を登録
		mav.addObject("contents", "login/userDetail :: userDetail_contents");
		//結婚ステータス用ラジオボタンの初期化
		radioMarriage = initRadioMarriage();
		//ラジオボタン用のMapをModelに登録
		mav.addObject("radioMarriage", radioMarriage);
		//ユーザーIDのチェック
		if (userId != null && userId.length() > 0) {
			//ユーザー情報を取得
			User user = userService.selectOne(userId);
			//Userクラスをフォームクラスに変換
			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setBirthday(user.getBirthday());
			form.setAge(user.getAge());
			form.setMarriage(user.isMarriage());
			//画面表示用に登録
			mav.addObject("signupForm", form);
		}
		mav.setViewName("/login/homeLayout");
		return mav;
	}
	
	//ユーザー更新用処理（ボタン名によりメソッド判定 param = "update" ）=====================================
	@RequestMapping(value = "/userDetail", params = "update")
	public ModelAndView postUserDetailUpdate(@ModelAttribute SignupForm form, ModelAndView mav) {
		System.out.println("更新ボタンの処理");
		//Userインスタンスの生成
		User user = new User();
		//フォームクラスをUserクラスに変換
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());
		//更新実行
		boolean result = userService.updateOne(user);
		//更新実行
			if (result) {
				mav.addObject("result", "更新成功");
			}else {
				mav.addObject("result", "更新失敗");
			}
		//更新実行
		//ユーザー一覧画面の表示
		return getUserList(mav);
	}
	
	//ユーザー削除用処理=====================================================================================
	@RequestMapping(value = "/userDetail", params = "delete")
	public ModelAndView postUserDetailDelete(@ModelAttribute SignupForm form, ModelAndView mav) {
		System.out.println("削除ボタンの処理");
		//削除実行
		boolean result = userService.deleteOne(form.getUserId());
		if (result) {
			mav.addObject("result", "削除成功");
		}else {
			mav.addObject("result", "削除失敗");
		}
		//ユーザー一覧画面を表示
		return getUserList(mav);
	}
	
	//ログアウト用メソッド====================================================================================
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView postLogout(ModelAndView mav) {
		//ログイン画面にリダイレクト
		return new ModelAndView("redirect:/login");
	}
	
	//ユーザー一覧のCSV出力用メソッド=========================================================================
	@RequestMapping(value = "/userList/csv", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getUserListCsv(Model model) {
		//ユーザーを全件取得して、CSVをサーバーに保存する
		userService.userCsvOut();
		byte[] bytes = null;
		
		try {
			//サーバーに保存されているsample.csvファイルをbyteで取得する
			bytes = userService.getFile("sample.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//HTTPヘッダーの設定
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "sample.csv");
		
		//sample.csvを戻す
		return new ResponseEntity<>(bytes, header, HttpStatus.OK);
	}
	
}
