package com.ld.mall.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CheckImgController {
	// 集合中保存所有成语
	private List<String> words = new ArrayList<String>();
	
	
	@RequestMapping("/checkImg")
	public void checkImg(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// 初始化阶段，读取new_words.txt
		// web工程中读取 文件，必须使用绝对磁盘路径
		String path = request.getServletContext().getRealPath("/WEB-INF/new_words.txt");
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				words.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 禁止缓存
				// response.setHeader("Cache-Control", "no-cache");
				// response.setHeader("Pragma", "no-cache");
				// response.setDateHeader("Expires", -1);

				int width = 120;
				int height = 30;

				// 步骤一 绘制一张内存中图片
				BufferedImage bufferedImage = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);

				// 步骤二 图片绘制背景颜色 ---通过绘图对象
				Graphics graphics = bufferedImage.getGraphics();// 得到画图对象 --- 画笔
				// 绘制任何图形之前 都必须指定一个颜色
				graphics.setColor(getRandColor(200, 250));
				graphics.fillRect(0, 0, width, height);

				// 步骤三 绘制边框
				graphics.setColor(Color.WHITE);
				graphics.drawRect(0, 0, width - 1, height - 1);

				// 步骤四 四个随机数字
				Graphics2D graphics2d = (Graphics2D) graphics;
				// 设置输出字体
				graphics2d.setFont(new Font("宋体", Font.BOLD, 18));

				Random random = new Random();// 生成随机数
				int index = random.nextInt(words.size());
				String word = words.get(index);// 获得成语

				// 定义x坐标
				int x = 10;
				for (int i = 0; i < word.length(); i++) {
					// 随机颜色
					graphics2d.setColor(new Color(20 + random.nextInt(110), 20 + random
							.nextInt(110), 20 + random.nextInt(110)));
					// 旋转 -30 --- 30度
					int jiaodu = random.nextInt(60) - 30;
					// 换算弧度
					double theta = jiaodu * Math.PI / 180;

					// 获得字母数字
					char c = word.charAt(i);

					// 将c 输出到图片
					graphics2d.rotate(theta, x, 20);
					graphics2d.drawString(String.valueOf(c), x, 20);
					graphics2d.rotate(-theta, x, 20);
					x += 30;
				}

				// 将验证码内容保存session
				request.getSession().setAttribute("checkcode_session", word);

				// 步骤五 绘制干扰线
				graphics.setColor(getRandColor(160, 200));
				int x1;
				int x2;
				int y1;
				int y2;
				for (int i = 0; i < 30; i++) {
					x1 = random.nextInt(width);
					x2 = random.nextInt(12);
					y1 = random.nextInt(height);
					y2 = random.nextInt(12);
					graphics.drawLine(x1, y1, x1 + x2, x2 + y2);
				}

				// 将上面图片输出到浏览器 ImageIO
				graphics.dispose();// 释放资源
				
				//将图片写到response.getOutputStream()中
				ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
	}
	
	@RequestMapping("/checkImg/checkCode")
	@ResponseBody
	public String CheckCode(String checkCode,HttpServletRequest request){
		String word = (String) request.getSession().getAttribute("checkcode_session");
		if(word.equals(checkCode)){
			return "{\"ifSame\":"+true+"}";
		}
		return "{\"ifSame\":"+false+"}";
	}
	
	
	
	
	/**
	 * 取其某一范围的color
	 * 
	 * @param fc
	 *            int 范围参数1
	 * @param bc
	 *            int 范围参数2
	 * @return Color
	 */
	private Color getRandColor(int fc, int bc) {
		// 取其随机颜色
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
