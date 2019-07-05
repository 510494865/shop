package com.ld.mall.service;

import java.util.List;

import com.ld.mall.pojo.Category;

public interface CategoryService {
	List<Category> getCategoryList();
	Category getCnameBycid(String cid);
}
