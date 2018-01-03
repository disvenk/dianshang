package com.taotao.sso.service;

import com.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserRegisterCheck {
	public TaotaoResult checkUserInfo(String param,Integer type);
	public TaotaoResult createUser(TbUser user);
}
