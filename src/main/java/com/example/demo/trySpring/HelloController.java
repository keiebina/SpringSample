package com.example.demo.trySpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ModelAndView getHello(ModelAndView mav) {
		mav.setViewName("/hello");
		return mav;
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public ModelAndView postHello(@RequestParam("text1") String str, ModelAndView mav) {
		mav.setViewName("/helloResponse");
		mav.addObject("sample", str);
		return mav;
	}
	
	@RequestMapping(value = "/hello/db", method = RequestMethod.POST)
	public ModelAndView postDbRequest(@RequestParam("text2")String str, ModelAndView mav) {
		
		//Stringからint型に変換
		int id = Integer.parseInt(str);
		
		//1件検索
		Employee employee = helloService.findOne(id);
		
		//検索結果をModelに登録
		mav.addObject("id", employee.getEmployeeId());
		mav.addObject("name", employee.getEmployeeName());
		mav.addObject("age", employee.getAge());
		
		//helloResponseDB.htmlに画面遷移
		mav.setViewName("/helloResponseDB");
		return mav;
	}
}
