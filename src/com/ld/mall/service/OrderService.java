package com.ld.mall.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ld.mall.pojo.Orders;

public interface OrderService {
	void submitOrder(HttpServletRequest request);
	void updateOrderInfo(Orders order);
	void updateOrderState(String oid);
	List<Orders> myOrders(HttpServletRequest request); 
}
