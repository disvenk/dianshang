package com.taotao.sso.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;

@Service
public class UserRegisterCheckImpl implements UserRegisterCheck {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	//检查用户名，手机，邮箱是否存在
	@Override
	public TaotaoResult checkUserInfo(String param, Integer type) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		//判断注册条件
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}
		
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list == null || list.size() == 0){
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	//注册添加user
	@Override
	public TaotaoResult createUser(TbUser user) {
		//进行非空校验
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "密码不能为空");
		}
		if(StringUtils.isBlank(user.getPhone())){
			return TaotaoResult.build(400, "手机号不能为空");
		}
		/*if(StringUtils.isBlank(user.getEmail())){
			return TaotaoResult.build(400, "邮箱不能为空");
		}*/
		
		//检验用户名是否占用
		TaotaoResult result1 = checkUserInfo(user.getUsername(), 1);
		if(!(Boolean)result1.getData()){
			return TaotaoResult.build(400, "用户名已被占用");
		}
		//校验手机是否占用
		TaotaoResult result2 = checkUserInfo(user.getPhone(), 2);
		if(!(Boolean)result2.getData()){
			return TaotaoResult.build(400, "该手机号已经注册");
		}
		//校验邮箱是否占用
		TaotaoResult result3 = checkUserInfo(user.getEmail(), 3);
		if(!(Boolean)result3.getData()){
			return TaotaoResult.build(400, "该邮箱已经被使用");
		}
		//补全属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		
		//把用户存储到数据库
		tbUserMapper.insert(user);
		
		return TaotaoResult.ok();
	
	}

}
