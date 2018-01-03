package com.taotao.sso.service;

import com.common.pojo.TaotaoResult;

public interface UserLoginService {
	public TaotaoResult userLogin(String username, String password);
	public TaotaoResult getUserByToken(String token);
	public TaotaoResult userLogout(String token);
}
