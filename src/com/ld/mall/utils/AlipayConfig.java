package com.ld.mall.utils;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092900623062";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDXBQTtD44qHJ7sjf7kEPXSZrkc9wYB9ma1XSN1oagQ+wtvPfS7VkXFRJSuBzuh56hyEeBsunxiccnO2pLcB/VhGORVGNCQYhp2uTY6Fmt4OOCyeJTUhmngMx3xs3kE3R4YyPkcUanF8ucocRUUlz+mywl0Zi3WMnmprdbj7Z3VIrx3IAr+LDzLyRxRZPCWKytUuboKmvcsHLvZI32ozB0p4pBuEIg9m9+CtQDPLrnCdDtxqq9ZOu8R7uE6lCgaxInR0oqsoJAxfetUv71uB63rtzwz++9Sqtwhxh0/7uZxjS4YoifC8dNpFN3VTkQTLFlc5r97s0fEdgwceh5DvuB/AgMBAAECggEBAJInXEDTGPww9imvHzng+Wbp4hv5IQFGfmv7AjZcCsR4ED6uK2MHcmdzhWiBjBwyPgJDenwNiqzT0awkUDz1v+ofJF1ff60Usg5POLflM2nFp55IbknDj8YQmOMI7lLLBkEson+IVtExmcSYkQ6s/FwgmK2hIrj9GEuo2W+WvXj4Y3su+hPODj0miCzwNprcPVVCe4sCmGbU+A1XTGqKmLEdxGcGxPytji3TyxmDM8RRAmIBdAT3GlL90eGvvYc9UtJRNCp9MOE9n1NGnN30BggNvENawc0lDeMUfBDvbj6JtQbU2zWVZ2ELgHhGWDKpQrv2FXcs1ykratreTgag9NECgYEA8I0gCI+ZqHnNcAtxKCPZrrRRO6Zguw7l4FwSfpHlpegHNeO8o0BihEUGYiYW7TJt4A6QK7s48KQmbLEWGTFeULYDzacx18rEk7z1LSZR9Dzsm/0qmge3JLrAeB+CgvEPlKqzVRG25fTLy3RQKu0r+QQl3VtTPyUBZo02BckUCKkCgYEA5NQhjT1rgc2V1UYGVgpAYid8gUd/rRHPhSo8k0sEcfu/7tj+bFP6vaJpPtxRUoQf5yjDfEDUVxDlzH5nQ/RI1AIgED3UNhvUYJfJLv2FJHlXqRYqJHOpqet4hAjV/tebELwAxo17YKC4TAaUhyWRXUPh4O394KAhZMCDv0LskOcCgYEA2C/NEUSVcS9+bWsrGEsX8QYHV5eQqaya9gdpa60NegxMpqb0urc7kupB8WWQBz7iNpKACX1qkALGsbp5RMz0mY7rXiEcD3chMjGJGzmsP9edQhfF+MZkVzMrlQmkjq+9M/wxzYttF7/ohAIVpm6erV6H9SeFamOEnbjdS1fKlHkCgYAkDBbM/SP2asBzvHg7DZpe3AGNDApc5TVfsOuh+GtW2WW+5ogN4JzbSr5xnJti+FVeQNmqtTu+C3dn43G5Dr56R2tto4/OhkvKe57BvEZBHKxmgotY+q97iRCPHCU55BUVachlftw0uJzPvSmgsJZZrtviZxtbS153VKxscMp7eQKBgCuljw/S67rv9jqe1z3JkzCZ+tBTnN9uBcBQR0V3mzsbpqogcDuUqqSWJcTdlMYCgpaCTqkatgAuX84FOAFn3J+aRrJWAF/VPCCBzuLUM1em+56mDcHWhxWFedBoLh8UVt7AnCCcH1Wgqwegyzf4Y2N/POk3cAInjexEwfP0QTyM";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1wUE7Q+OKhye7I3+5BD10ma5HPcGAfZmtV0jdaGoEPsLbz30u1ZFxUSUrgc7oeeochHgbLp8YnHJztqS3Af1YRjkVRjQkGIadrk2OhZreDjgsniU1IZp4DMd8bN5BN0eGMj5HFGpxfLnKHEVFJc/pssJdGYt1jJ5qa3W4+2d1SK8dyAK/iw8y8kcUWTwlisrVLm6Cpr3LBy72SN9qMwdKeKQbhCIPZvfgrUAzy65wnQ7caqvWTrvEe7hOpQoGsSJ0dKKrKCQMX3rVL+9bget67c8M/vvUqrcIcYdP+7mcY0uGKInwvHTaRTd1U5EEyxZXOa/e7NHxHYMHHoeQ77gfwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

