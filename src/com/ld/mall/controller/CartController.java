package com.ld.mall.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ld.mall.service.CartService;

@Controller
public class CartController {
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/addCart")
	public String addCart(String pid,String buyNum,HttpServletRequest request){
		cartService.addCart(pid, buyNum, request);
		return "redirect:/cart/cartList";
	}
	
	@RequestMapping("/cart/delProFromCart")
	public String delProFromCart(String pid,HttpServletRequest request){
		cartService.delProFromCart(pid, request);
		return "redirect:/cart/cartList";
	}
	
	@RequestMapping("/cart/clearCart")
	public String clearCart(HttpServletRequest request){
		cartService.clearCart(request);
		return "redirect:/cart/cartList";
	}
	@RequestMapping("/cart/cartList")
	public String cartList(HttpServletRequest request){
		cartService.cartList(request);
		return "redirect:/cart";
	}
}
