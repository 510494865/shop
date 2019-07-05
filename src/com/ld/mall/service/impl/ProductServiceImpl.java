package com.ld.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ld.mall.mapper.ProductMapper;
import com.ld.mall.pojo.Product;
import com.ld.mall.pojo.ProductExample;
import com.ld.mall.service.ProductService;
import com.ld.mall.vo.PageBean;

@Service
@Transactional(isolation=Isolation.REPEATABLE_READ,propagation=Propagation.REQUIRED,readOnly=true)
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductMapper productMapper;
	
	/**
	 *准备热门商品
	 */
	public List<Product> getHotProduct() {
		ProductExample example=new ProductExample();
		example.createCriteria().andIsHotEqualTo(1);
		List<Product> list = productMapper.selectByExample(example);
		List<Product> hotProductList=new ArrayList<Product>();
		for(int i=0;i<9;i++){
			hotProductList.add(list.get(i));
		}
		return hotProductList;
	}

	/**
	 * 准备最新商品
	 */
	public List<Product> getNewProduct() {
		List<Product> list = productMapper.getNewProduct();
		return list;
	}

	/**
	 * 模糊查找商品信息
	 */
	public List<String> searchWord(String word) {
		ProductExample example=new ProductExample();
		example.createCriteria().andPnameLike("%"+word+"%");
		List<Product> productList = productMapper.selectByExample(example);
		List<String> list=new ArrayList<String>();
		if(productList.size()>=8){
			for(int i=0;i<8;i++){
				list.add(productList.get(i).getPname());
			}			
		}else{
			for(int i=0;i<productList.size();i++){
				list.add(productList.get(i).getPname());
			}
		}
		return list;
	}

	/**
	 * 根据cid获得商品列表
	 */
	public PageBean<Product> getProductListByCid(String currentPage,String cid) {
		//封装一个pageBean 返回controller层
		PageBean<Product> pageBean = new PageBean<Product>();

		int currentCount = 12;
		// 封装当前页
		pageBean.setCurrentPage(Integer.parseInt(currentPage));
		// 封装每页显示的个数
		pageBean.setCurrentCount(currentCount);
		//封装总条数
		ProductExample example=new ProductExample();
		example.createCriteria().andCidEqualTo(cid);
		int totalCount = productMapper.selectByExample(example).size();
		pageBean.setTotalCount(totalCount);
		//封装总页数
		int totalPage=(int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		//当前页显示的数据
		int index=(Integer.parseInt(currentPage)-1)*currentCount;
		List<Product> productList = productMapper.getProductListByCid(cid,index,currentCount);
		pageBean.setList(productList);
		return pageBean;
	}

	/**
	 * 获取商品详细信息
	 */
	public Product getProductInfo(String pid) {
		Product product = productMapper.selectByPrimaryKey(pid);
		return product;
	}

	/**
	 * 根据商品名获取商品列表
	 */
	public PageBean<Product> getProductListByPname(String search) {
		PageBean<Product> pageBean=new PageBean<Product>();
		ProductExample example=new ProductExample();
		example.createCriteria().andPnameLike("%"+search+"%");
		List<Product> productList = productMapper.selectByExample(example);
		pageBean.setList(productList);
		pageBean.setTotalCount(productList.size());
		pageBean.setCurrentCount(50);
		return pageBean;
	}

}
