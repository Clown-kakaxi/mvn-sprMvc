package com.yqx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/h11")
public class HelloController{

	@RequestMapping("/hello.do")
	public String execute(){
		System.out.println("通过注解方式处理hello.do请求");
		return "j_hello";
	}
	
}
