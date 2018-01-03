package com.taotao.sso.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.common.pojo.TaotaoResult;
import com.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;

@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_PRE}")
	private String SESSION_PRE;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	//用户登录校验
	@Override
	public TaotaoResult userLogin(String username,String password) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		//判断是否存在
		if(list == null || list.size() == 0){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		
		TbUser tbUser = list.get(0);
		tbUser.setPassword(null);
		String token = UUID.randomUUID().toString();
		jedisClient.set(SESSION_PRE+":"+token, JsonUtils.objectToJson(tbUser));
		jedisClient.expire(SESSION_PRE+":"+token, SESSION_EXPIRE);
		return TaotaoResult.ok(token);
	}

	//根据token来取user
	@Override
	public TaotaoResult getUserByToken(String token) {
		//从redis中取出userJson
		String userJson = jedisClient.get(SESSION_PRE+":"+token);
		if(userJson==null || userJson.equals("")){
			return TaotaoResult.build(400, "此用户登录已经过期");
		}
		jedisClient.expire(SESSION_PRE+":"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(userJson,TbUser.class);
		
		return TaotaoResult.ok(user);
	}

	//用户注销
	@Override
	public TaotaoResult userLogout(String token) {
		jedisClient.del(SESSION_PRE+":"+token);
		return TaotaoResult.build(200, "退出成功");
	}
}
