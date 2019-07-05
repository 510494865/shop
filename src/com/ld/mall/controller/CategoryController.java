package com.ld.mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ld.mall.pojo.Category;
import com.ld.mall.service.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("/category/list")
	@ResponseBody
	public List<Category> getCategoryList(){
		List<Category> list = categoryService.getCategoryList();
		return list;
	}
}
