package com.ld.mall.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ld.mall.jedis.JedisClient;
import com.ld.mall.mapper.OrderitemMapper;
import com.ld.mall.mapper.OrdersMapper;
import com.ld.mall.mapper.ProductMapper;
import com.ld.mall.pojo.Cart;
import com.ld.mall.pojo.CartItem;
import com.ld.mall.pojo.Orderitem;
import com.ld.mall.pojo.OrderitemExample;
import com.ld.mall.pojo.Orders;
import com.ld.mall.pojo.OrdersExample;
import com.ld.mall.pojo.Product;
import com.ld.mall.pojo.User;
import com.ld.mall.service.OrderService;
import com.ld.mall.utils.JsonUtils;





@Service
@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=true)
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private OrderitemMapper orderitemMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private JedisClient jedisClient;
	
	
	/**
	 * 提交订单(将数据存入order,orderItem表)
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
	public void submitOrder(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Cookie[] cookies = request.getCookies();
		String token ="";
		for (Cookie cookie : cookies) {
			if("token".equals(cookie.getName())){
				token = cookie.getValue();
			}
		}
		String json = jedisClient.get("SESSION:"+token);
		User user = JsonUtils.jsonToPojo(json, User.class);
		// 目的：封装好一个Order传给dao层
		Orders order = new Orders();
		// 封装订单号
		String oid = UUID.randomUUID().toString();
		order.setOid(oid);
		// 封装下单时间
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ordertime = format.format(date);
		order.setOrdertime(ordertime);
		// 封装订单总金额
		// 获得购物车
		Cart cart = (Cart) session.getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);
		// 封装订单支付状态
		order.setState(0);
		// 封装该订单属于哪个用户
		order.setUid(user.getUid());
		// 封装该订单有多少订单项
		// 获得购物车中的集合map
		Map<String, CartItem> cartItems = cart.getCartItem();
		for (Map.Entry<String, CartItem> entry : cartItems.entrySet()) {
			CartItem cartItem = entry.getValue();
			Orderitem orderitem = new Orderitem();
			orderitem.setItemid(UUID.randomUUID().toString());
			orderitem.setCount(cartItem.getBuyNum());
			orderitem.setSubtotal(cartItem.getSubTotal());
			orderitem.setPid(cartItem.getProduct().getPid());
			orderitem.setProduct(cartItem.getProduct());
			orderitem.setOid(order.getOid());

			order.getOrderItems().add(orderitem);
		}
		//---------------------- order对象封装完毕-----------------
		//添加到order表
		ordersMapper.insert(order);
		//添加到orderitem表
		List<Orderitem> orderitems = order.getOrderItems();
		for (Orderitem orderitem : orderitems) {
			orderitemMapper.insert(orderitem);
		}
		
		//将order放入session域中
		session.setAttribute("order", order);
	}

	/**
	 * 更新订单信息
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
	public void updateOrderInfo(Orders order) {
		ordersMapper.updateByPrimaryKeySelective(order);
	}

	/**
	 * 购买成功后修改付款状态
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
	public void updateOrderState(String oid) {
		Orders order=new Orders();
		order.setOid(oid);
		order.setState(1);
		ordersMapper.updateByPrimaryKeySelective(order);
		
	}

	/**
	 * 我的订单
	 */
	public List<Orders> myOrders(HttpServletRequest request) {
		//获得用户信息
		Cookie[] cookies = request.getCookies();
		String token ="";
		for (Cookie cookie : cookies) {
			if("token".equals(cookie.getName())){
				token = cookie.getValue();
			}
		}
		String json = jedisClient.get("SESSION:"+token);
		User user = JsonUtils.jsonToPojo(json, User.class);
		OrdersExample ordersExample=new OrdersExample();
		ordersExample.createCriteria().andUidEqualTo(user.getUid());
		List<Orders> orderList = ordersMapper.selectByExample(ordersExample);
		OrderitemExample orderitemExample=new OrderitemExample();
		List<Orderitem> orderitemList = orderitemMapper.selectByExample(orderitemExample);
		for (Orderitem orderitem : orderitemList) {
			Product product = productMapper.selectByPrimaryKey(orderitem.getPid());
			orderitem.setProduct(product);
			for (Orders order : orderList) {
				if(order.getOid().equals(orderitem.getOid())){
					order.getOrderItems().add(orderitem);
				}
			}
		}
		return orderList;
	}

}
