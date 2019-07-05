package com.ld.mall.service;

import com.ld.mall.pojo.User;
import com.ld.mall.vo.UserVo;

public interface UserService {
	boolean checkUsername(String username);
	boolean register(User user);
	void activeUser(String activeCode);
	UserVo login(String username,String password);
	User getUserByToken(String token);
}
