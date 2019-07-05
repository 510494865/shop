package com.ld.mall.pojo;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	//该购物车中存储的n个购物项
	private Map<String,CartItem> cartItem=new HashMap<String,CartItem>() ;
	
	//购物车内商品总计
	private double total;

	public Map<String, CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(Map<String, CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
