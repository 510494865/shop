package com.ld.mall.controller;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ld.mall.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
















import com.ld.mall.jedis.JedisClient;
import com.ld.mall.pojo.User;
import com.ld.mall.service.UserService;
import com.ld.mall.utils.JsonUtils;
import com.ld.mall.vo.UserVo;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClient jedisClient;
	
	@RequestMapping("user/checkUsername")
	@ResponseBody
	public String checkUsername(String username){
		boolean isExist = userService.checkUsername(username);
		return "{\"isExist\":"+isExist+"}";
	}
	
	@RequestMapping("/user/register")
	public String Register(User user){
		String code=UUID.randomUUID().toString();
		user.setCode(code);
		boolean isTrue = userService.register(user);
		if(isTrue){
			//发送激活邮件
			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
					+ "<a href='http://localhost:8080/Mall/user/active?activeCode="+code+"'>"
					+ "http://localhost:8080/Mall/user/active?activeCode="+code+"</a>";
			
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {	
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return "registerSuccess";			
		}else {
			return "registerFail";
		}
	}
	
	@RequestMapping("/user/active")
	public String activeUser(String activeCode){
		userService.activeUser(activeCode);
		return "redirect:/login";
	}
	
	@RequestMapping("/user/login")
	public String login(String username,String password,String autoLogin,Model model,HttpServletRequest request,HttpServletResponse response){
		UserVo userVo = userService.login(username, password);
		
		if(userVo.isExit()==false){
			model.addAttribute("loginInfo", "账号不存在");
			return "login";
		}else if(userVo.isPasswordInfo()==false){
			model.addAttribute("loginInfo", "密码错误");
			return "login";
		}else if(userVo.isActive()==false){
			model.addAttribute("loginInfo", "账号未激活");
			return "login";
		}else{
			//生成token
			String token=UUID.randomUUID().toString();
			//把用户信息存入redis
			jedisClient.set("SESSION:"+token,JsonUtils.objectToJson(userVo.getUser()));
			//设置session过期时间
			jedisClient.expire("SESSION:"+token, 1800);
			//把token存入cookie
			Cookie cookie = new Cookie("token", token);
			cookie.setMaxAge(10*60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}

		if("autoLogin".equals(autoLogin)){
			//要自动登录
			//创建存储用户名的cookie
			Cookie cookie_username = new Cookie("cookie_username",username);
			cookie_username.setMaxAge(10*60);
			cookie_username.setPath("/");
			//创建存储密码的cookie
			Cookie cookie_password = new Cookie("cookie_password",password);
			cookie_password.setMaxAge(10*60);
			cookie_password.setPath("/");

			response.addCookie(cookie_username);
			response.addCookie(cookie_password);

		}

		return "redirect:/";
	}
	
	@RequestMapping("/user/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("token".equals(cookie.getName())){
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			if("cookie_username".equals(cookie.getName())){
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public User getUserByToken(@PathVariable String token){
		User user = userService.getUserByToken(token);
		return user;
		
	}
}
