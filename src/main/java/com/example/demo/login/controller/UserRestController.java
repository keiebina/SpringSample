package com.example.demo.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.RestService;
import com.example.demo.login.domain.service.UserService;

@RestController //RestControllerにすることで各メソッドの戻り値がhtmlを探さなくなる。呼び出し元に戻りを返す処理になる
public class UserRestController {
	
	@Autowired
	RestService service;
	
	//ユーザー全件取得
	@RequestMapping(value = "/rest/get", method = RequestMethod.GET)
	public List<User> getUserMany(){
		//ユーザー全件取得
		return service.selectMany();
	}
	
	//ユーザー1件取得
	@RequestMapping(value = "/rest/get/{id:.+}", method = RequestMethod.GET)
	public User getUserOne(@PathVariable("id") String userId) {
		//ユーザー1件取得
		return service.selectOne(userId);
	}
	
	@RequestMapping(value = "/rest/insert", method = RequestMethod.POST)
	public String postUserOne(@RequestBody User user) {
		//ユーザーを1件登録
		boolean result = service.inesrt(user); //登録成功していればtrue
		String str = "";
		if (result) {
			str = "{\"result\":\"ok\"}";
		}else {
			str = "{\"result\":\"error\"}";
		}
		//結果用の文字列をリターン
		return str;
	}
	
	@RequestMapping(value = "/rest/update", method = RequestMethod.PUT)
	public String putUserOne(@RequestBody User user) {
		//ユーザーを1件更新
		boolean result = service.updateOne(user); //更新成功していればtrue
		String str = "";
		if (result) {
			str = "{\"result\":\"ok\"}";
		}else {
			str = "{\"result\":\"error\"}";
		}
		//結果用の文字列をリターン
		return str;
	}
	
	@RequestMapping(value = "/rest/delete/{id:.+}", method = RequestMethod.DELETE)
	public String deleteUserOne(@PathVariable("id") String userId) {
		//ユーザーを1件削除
		boolean result = service.deleteOne(userId);
		String str = "";
		if (result) {
			str = "{\"result\":\"ok\"}";
		}else {
			str = "{\"result\":\"error\"}";
		}
		return str;
	}
}
