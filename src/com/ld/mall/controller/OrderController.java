package com.ld.mall.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ld.mall.utils.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ld.mall.pojo.Orders;
import com.ld.mall.service.CartService;
import com.ld.mall.service.OrderService;
import com.ld.mall.utils.PaymentUtil;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    @RequestMapping("/order/submitOrder")
    public String submitOrder(HttpServletRequest request) {
        orderService.submitOrder(request);
        //提交完订单后清空购物车
        cartService.clearCart(request);
        return "redirect:/order_info";
    }

    @RequestMapping("/order/confirmOrder")
    //@ResponseBody
    public String confirmOrder(Orders order, String pd_FrpId, HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        //更新order信息
        orderService.updateOrderInfo(order);

        return "pay";

		/*// 在线支付
		// 接入易宝支付
		// 获得 支付必须基本数据
		//String money = order.getTotal().toString();// 支付金额
		String money ="0.01";// 支付金额,测试需要
		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString(
				"p1_MerId");
		String p2_Order = order.getOid();
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString(
				"callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);

		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="
				+ pd_FrpId + "&p0_Cmd=" + p0_Cmd + "&p1_MerId=" + p1_MerId
				+ "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur="
				+ p4_Cur + "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat
				+ "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url + "&p9_SAF="
				+ p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse="
				+ pr_NeedResponse + "&hmac=" + hmac;

		// 重定向到第三方支付平台
		return "redirect:"+url;*/

    }

    @SuppressWarnings("unused")
    @RequestMapping("/order/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        // 获得回调所有数据
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
        String rb_BankId = request.getParameter("rb_BankId");
        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
        String rp_PayDate = request.getParameter("rp_PayDate");
        String rq_CardNo = request.getParameter("rq_CardNo");
        String ru_Trxtime = request.getParameter("ru_Trxtime");
        // 身份校验 --- 判断是不是支付公司通知你
        String hmac = request.getParameter("hmac");
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");

        // 自己对上面数据进行加密 --- 比较支付公司发过来hamc
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);

        if (isValid) {
            // 响应数据有效
            if (r9_BType.equals("1")) {
                // 修改订单状态
                orderService.updateOrderState(r6_Order);
                // 浏览器重定向
                response.setContentType("text/html;charset=utf-8");
                try {
                    response.getWriter().println("<h1>付款成功！等待商城进一步操作！等待收货...</h1>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (r9_BType.equals("2")) {
                // 服务器点对点 --- 支付公司通知你
                System.out.println("付款成功！");
                // 修改订单状态 为已付款
                // 回复支付公司
                try {
                    response.getWriter().print("success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 数据无效
            System.out.println("数据被篡改！");
        }
    }

    @RequestMapping("/order/myOrders")
    public String myOrders(Model model, HttpServletRequest request) {
        List<Orders> orderList = orderService.myOrders(request);
        model.addAttribute("orderList", orderList);
        return "order_list";
    }
}
