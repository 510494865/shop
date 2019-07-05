package com.ld.mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ld.mall.mapper.CategoryMapper;
import com.ld.mall.pojo.Category;
import com.ld.mall.pojo.CategoryExample;
import com.ld.mall.service.CategoryService;
@Service
@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=true)
public class CategroyServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	
	/**
	 * 获取所有分类列表
	 */
	public List<Category> getCategoryList() {
		CategoryExample example=new CategoryExample();
		List<Category> list = categoryMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据cid获得cname
	 */
	public Category getCnameBycid(String cid) {
		Category category = categoryMapper.selectByPrimaryKey(cid);
		return category;
	}

}
