package com.ld.mall.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ld.mall.jedis.JedisClient;
import com.ld.mall.mapper.UserMapper;
import com.ld.mall.pojo.User;
import com.ld.mall.pojo.UserExample;
import com.ld.mall.pojo.UserExample.Criteria;
import com.ld.mall.service.UserService;
import com.ld.mall.utils.JsonUtils;
import com.ld.mall.utils.MD5Utils;
import com.ld.mall.vo.UserVo;
@Service
@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=true)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	/**
	 *核对用户名是否存在 
	 */
	public boolean checkUsername(String username) {
		UserExample example=new UserExample();
		example.createCriteria().andUsernameEqualTo(username);
		List<User> user = userMapper.selectByExample(example);
		if(user!=null&&user.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 注册用户
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
	public boolean register(User user) {
		try {
			user.setUid(UUID.randomUUID().toString());
			user.setPassword(MD5Utils.md5(user.getPassword()));
			user.setState(0);
			userMapper.insert(user);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 激活用户
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
	public void activeUser(String activeCode) {
		User user=new User();
		user.setState(1);
		UserExample example=new UserExample();
		example.createCriteria().andCodeEqualTo(activeCode);
		userMapper.updateByExampleSelective(user, example);
	}

	/**
	 * 登录
	 */
	public UserVo login(String username, String password) {
		UserVo userVo=new UserVo();
		UserExample example=new UserExample();
		Criteria c = example.createCriteria().andUsernameEqualTo(username);
		List<User> user = userMapper.selectByExample(example);
		if(user!=null&&user.size()>0){
			userVo.setExit(true);
			if(user.get(0).getState()!=1){
				userVo.setActive(false);
			}else{
				userVo.setActive(true);
			}
		}else{
			userVo.setExit(false);
		}
		c.andPasswordEqualTo(MD5Utils.md5(password));
		List<User> user2=userMapper.selectByExample(example);
		if(user2!=null&&user2.size()>0){
			userVo.setPasswordInfo(true);
			userVo.setUser(user2.get(0));
		}else{
			userVo.setPasswordInfo(false);
		}
		return userVo;
	}

	/**
	 * 从redis中取user
	 */
	public User getUserByToken(String token) {
		String json = jedisClient.get("SESSION:"+token);
		if(StringUtils.isNotBlank(json)){
			//取到用户信息更新token的过期时间
			jedisClient.expire("SESSION:" + token, 1800);
			return JsonUtils.jsonToPojo(json, User.class);
		}
		return null;
	}
	
	

}
