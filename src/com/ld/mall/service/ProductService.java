package com.ld.mall.service;

import java.util.List;

import com.ld.mall.pojo.Product;
import com.ld.mall.vo.PageBean;

public interface ProductService {
	List<Product> getHotProduct();
	List<Product> getNewProduct();
	List<String> searchWord(String word);
	PageBean<Product> getProductListByCid(String currentPage,String cid);
	Product getProductInfo(String pid);
	PageBean<Product> getProductListByPname(String search);
}
