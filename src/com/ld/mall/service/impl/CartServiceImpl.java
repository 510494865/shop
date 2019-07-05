package com.ld.mall.service.impl;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ld.mall.jedis.JedisClient;
import com.ld.mall.mapper.ProductMapper;
import com.ld.mall.pojo.Cart;
import com.ld.mall.pojo.CartItem;
import com.ld.mall.pojo.Product;
import com.ld.mall.pojo.User;
import com.ld.mall.service.CartService;
import com.ld.mall.utils.JsonUtils;

@Service
@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=false)
public class CartServiceImpl implements CartService {
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private JedisClient jedisClient;
	
	
	/**
	 * 添加购物车
	 */
	public void addCart(String pid, String buyNum,HttpServletRequest request) {
		//获得product对象
		Product product = productMapper.selectByPrimaryKey(pid);
		//计算小计
		double subTotal=product.getShopPrice()*Integer.parseInt(buyNum);
		//获得购物车----判断是否存在购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart==null){
			cart=new Cart();
		}
		// 判断购物车是否已经有该购物项
		CartItem cartItem = cart.getCartItem().get(pid);
		double NewsubTotal = subTotal;
		if (cartItem != null) {
			// 有该购物项
			// 修改购买数量
			buyNum += cartItem.getBuyNum();
			// 修改小计
			NewsubTotal = subTotal + cartItem.getSubTotal();
		}
		// 封装cartItem对象
		CartItem item = new CartItem();
		item.setBuyNum(Integer.parseInt(buyNum));
		item.setProduct(product);
		item.setSubTotal(NewsubTotal);
		
		// 将cartItem放入车中
		cart.getCartItem().put(pid, item);
		// 获得购物车原有的总价格
		double total = cart.getTotal();
		cart.setTotal(subTotal + total);
		
		//-----------------------------------------------------------------
		
		//先判断是否登录
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("token".equals(cookie.getName())){
				try {
					//已经登录----购物车存入redis中,以便切换客户端,购物车内的商品不会消失
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					User user = JsonUtils.jsonToPojo(josn, User.class);
					jedisClient.hset("CART_LIST",user.getUid(), JsonUtils.objectToJson(cart));				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//未登录----购物车存入session
		// 将车再次放回session
		request.getSession().setAttribute("cart", cart);
		
		
	}


	/**
	 * 删除购物车中的某一项
	 */
	public void delProFromCart(String pid, HttpServletRequest request) {
		//从session中取出cart
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		Map<String, CartItem> cartItem = cart.getCartItem();
		double subTotal = cartItem.get(pid).getSubTotal();
		double total = cart.getTotal();
		//移除购物项
		cartItem.remove(pid);
		//重新计算总价
		cart.setTotal(total-subTotal);
		cart.setCartItem(cartItem);
		try {
			//先判断是否登录
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if("token".equals(cookie.getName())){
					//已登录----删除redis中的旧记录
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					User user = JsonUtils.jsonToPojo(josn, User.class);
					jedisClient.hdel("CART_LIST",user.getUid());
					//重新存入新的数据
					jedisClient.hset("CART_LIST",user.getUid(),JsonUtils.objectToJson(cart));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//将cart重新加入session
		request.getSession().setAttribute("cart", cart);
	
	}


	/**
	 * 清空购物车
	 */
	public void clearCart(HttpServletRequest request) {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart!=null){
			Map<String, CartItem> cartItem = cart.getCartItem();
			cartItem.clear();
			cart.setTotal(0);
			cart.setCartItem(cartItem);
		}
		try {
			//先判断是否登录
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if("token".equals(cookie.getName())){
					//已登录----删除redis中的记录
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					User user = JsonUtils.jsonToPojo(josn, User.class);
					jedisClient.hdel("CART_LIST",user.getUid());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("cart",cart);
	}


	/**
	 * 获得购物车所有商品内容
	 */
	public void cartList(HttpServletRequest request) {
		try {			
			//先判断是否登录
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if("token".equals(cookie.getName())){
					//已登录---从redis中取出cart					
					String token = cookie.getValue();
					String josn = jedisClient.get("SESSION:"+token);
					User user = JsonUtils.jsonToPojo(josn, User.class);
					String json = jedisClient.hget("CART_LIST", user.getUid());
					if(StringUtils.isNotBlank(json)){
						Cart cart = JsonUtils.jsonToPojo(json, Cart.class);
						request.getSession().setAttribute("cart",cart);
					}else{
						request.getSession().setAttribute("cart",null);
					}
				}
			}
			//未登录则不处理
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
