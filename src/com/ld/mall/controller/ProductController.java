package com.ld.mall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ld.mall.pojo.Category;
import com.ld.mall.pojo.Product;
import com.ld.mall.service.CategoryService;
import com.ld.mall.service.ProductService;
import com.ld.mall.vo.PageBean;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	
	@RequestMapping("/product/searchWord")
	@ResponseBody
	public List<String> searchWord(String word){
		if(StringUtils.isNotBlank(word)){
			List<String> list = productService.searchWord(word);
			return list;			
		}
		return null;
	}
	
	@RequestMapping("/product/productListByCid")
	public String getProductListByCid(@RequestParam(defaultValue="1")String currentPage,String cid,Model model,HttpServletRequest request){
		// 获得客户端携带名字叫pids的cookie
		List<Product> historyProductList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product product = productService.getProductInfo(pid);
						historyProductList.add(product);
					}

				}
			}
		}
		// 将历史记录的集合放到域中
		model.addAttribute("historyProductList", historyProductList);

		//-----------------------------------------------------------

		
		PageBean<Product> pageBean = productService.getProductListByCid(currentPage, cid);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("cid", cid);
		return "product_list";
	}
	
	@RequestMapping("/product/productInfo")
	public String getProductInfo(String pid,String cid,String currentPage,Model model,HttpServletRequest request,HttpServletResponse response){
		//获得客户端携带的pid
		String pids=pid;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())){
					pids=cookie.getValue();
					//将pids拆成一个数组
					String[] split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list=new LinkedList<String>(asList);
					//判断集合中是否存在当前pid
					if(list.contains(pid)){
						//包含当前查看商品的pid
						list.remove(pid);
					}
					list.addFirst(pid);
					//将集合转成字符串
					StringBuffer sb=new StringBuffer();
					for(int i=0;i<list.size()&&i<7;i++){
						sb.append(list.get(i));
						sb.append("-");
					}
					//去掉最后面的-
					pids=sb.substring(0, sb.length()-1);
				}
			}
		}
		//当在转发之前 创建cookie存储pid
		Cookie cookie=new Cookie("pids", pids);
		response.addCookie(cookie);

	//----------------------------------------------------------------------

		Product product_info = productService.getProductInfo(pid);
		Category category = categoryService.getCnameBycid(cid);
		model.addAttribute("productInfo", product_info);
		model.addAttribute("cid", cid);
		model.addAttribute("cname", category.getCname());
		model.addAttribute("currentPage", currentPage);
		return "product_info";
	}
	
	@RequestMapping("/product/productListByPname")
	public String getProductListByPname(String search,Model model,HttpServletRequest request){
		// 获得客户端携带名字叫pids的cookie
		List<Product> historyProductList = new ArrayList<Product>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split("-");
					for (String pid : split) {
						Product product = productService.getProductInfo(pid);
						historyProductList.add(product);
					}

				}
			}
		}
		// 将历史记录的集合放到域中
		model.addAttribute("historyProductList", historyProductList);

		//-----------------------------------------------------------
		PageBean<Product> pageBean = productService.getProductListByPname(search);
		model.addAttribute("pageBean", pageBean);
		return "product_list";
	}
}
