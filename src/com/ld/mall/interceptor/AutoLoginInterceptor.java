package com.ld.mall.interceptor;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ld.mall.jedis.JedisClient;
import com.ld.mall.pojo.User;
import com.ld.mall.service.UserService;
import com.ld.mall.utils.JsonUtils;
import com.ld.mall.vo.UserVo;

public class AutoLoginInterceptor implements HandlerInterceptor{
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClient jedisClient;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		User user=null;
		//判断是否登录
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if("token".equals(cookie.getName())){
					//已登录----删除redis中的旧记录
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					user = JsonUtils.jsonToPojo(josn, User.class);

				}
			}
		}
		if(user == null){
			String cook_username = null;
			String cook_password = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("cookie_username")) {
						cook_username = cookie.getValue();
					}
					if (cookie.getName().equals("cookie_password")) {
						cook_password = cookie.getValue();
					}
				}
			}
			if (cook_username != null && cook_password != null) {
				UserVo userVo = userService.login(cook_username, cook_password);
				if(userVo.getUser()!=null){
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
			}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}
}
