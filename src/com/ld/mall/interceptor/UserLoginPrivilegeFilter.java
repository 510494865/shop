package com.ld.mall.interceptor;

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

public class UserLoginPrivilegeFilter implements HandlerInterceptor{
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClient jedisClient;
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//先判断是否登录
		try {
			Cookie[] cookies = request.getCookies();
			User user=null;
			for (Cookie cookie : cookies) {
				if("token".equals(cookie.getName())){
					//已经登录----购物车存入redis中,以便切换客户端,购物车内的商品不会消失
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					user = JsonUtils.jsonToPojo(josn, User.class);
					
				}
			}
			if(user == null){
				//没有登录
				response.sendRedirect(request.getContextPath()+"/login");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
