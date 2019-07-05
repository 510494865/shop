package com.ld.mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ld.mall.pojo.Product;
import com.ld.mall.service.ProductService;

@Controller
public class IndexController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/")
	public String showIndex(Model model){

		//准备热门商品
		List<Product> hotProductList = productService.getHotProduct();
		model.addAttribute("hotProductList", hotProductList);

		//准备最新商品
		List<Product> newProductList = productService.getNewProduct();
		model.addAttribute("newProductList", newProductList);
		return "index";
	}
	@RequestMapping("/{path}")
	public String Redirect(@PathVariable String path){
		return path;
	}
}
