package com.taotato.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterCheck;

@Controller
public class UserRegisterController {

	//@Autowired
	//private UserRegisterCheck userRegisterCheckImpl;
	
	//检查用户是否存在
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserInfo(@PathVariable String param,@PathVariable Integer type){
		//TaotaoResult result = userRegisterCheckImpl.checkUserInfo(param, type);
		//return result;
		return null;
	}
	
	//注册添加用户
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user){
		//TaotaoResult result = userRegisterCheckImpl.createUser(user);
		//return result;
		return null;
	}
}
