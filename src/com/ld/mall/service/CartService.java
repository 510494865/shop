package com.ld.mall.service;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
	void addCart(String pid,String buyNum,HttpServletRequest request);
	void delProFromCart(String pid,HttpServletRequest request);
	void clearCart(HttpServletRequest request);
	void cartList(HttpServletRequest request);
}
