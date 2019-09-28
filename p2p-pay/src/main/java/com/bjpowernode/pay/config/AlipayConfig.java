package com.bjpowernode.pay.config;

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
    public static String app_id = "2016101000655904";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCdhGKfXxRwQarZn6M0vhJ2rfxKJtTyuSGAJ43i4h/wjuTRTkLnZP8NC62PGJLnAoCV6iPA7H7J4OeUYRTOWqYrxyZtcW/9h43wt6HMdOIfIxuweE7wfw3ZgUdGq5j25pDDS7HEgi+3zFi9QKOtBWYHE1JjregsIKMQ33O6vweEtHh0iLBLo1XOw/e8BN4qZRHZBQgZbIBPFaSQRbyjDDuK+BPJpD7iEC1CGNYXuzBJLcUwd+8tR6Le17UW2fX/RYS6tHeHYCoSpQCLyk9BJ0xk/3iBmydZ0ez7gntA5aI11VLawO5RlWMjzacyqG7S+A2etE56suQ693cnqhlpoQ+PAgMBAAECggEAWD8OsZKm9K0Vdo6E9Mt3tJ2Mtk5DByjIp++BAhcFJdQoRUq/GLvwV/Ku7Xgq9NjmCRb/qNnVvmwN0YcCDQmkBuqvkKj4wOTslGuMbesds6wLmzYLo5jKvYw7zk/chpZti7wgf6Mf8Ky/GmfYStq3akdc5RNM+0vdj1pgj8LuB13hKDeL3BVRSUk2ouxdLqZzV2RI/YMZIxnuUKwJW/c8euknEJ8Pu5xUmb85PAbzb71uzv+aywLAmGlND2PH0i6RZeCVf2lmE8jY8kKv1rEljbtgldehWrdmxi9rVfFjLaN7pvo+8c4VSiPYaguDax7mthLSv8YUScemztYvPRkuoQKBgQDknHgFdc02C4tNsbG0lqNQuKyTip38w0iFoTNTTpqlvffg7DGH2M57zvG6jR++ap4+z9rL1fkMcq+enbe8o6EAJKkggijILFdbDUQRbvAMRwUvkYRrR/oxzuRYUUPU/B7K0OQY+2VO6o48c/Yh/OQIAy3qaH5IgDhf3qu7cU5biwKBgQCwY3QClDtFSLZO3ieKx6JkdrkBx2wZsG1PRsvyoY4Ab6uYZjRhAWtNnjAYXgKc20qelStOuCEhSpeNh5pEXFnjS+1rKCRZ6jciZAgcAli+jJT5IwOUek/7OdmdXNC7d2oav9R7HOxJC5WUF5wIruei9GYd4eUJ1tTz0eXA+85sjQKBgCEn9FxOXonlGLoW+IAJjPwrHJR1eT5skDsZSxldAB7js2zCtNhQAHqCZBq7VwxF6FeRy9jeWrWMeJ4Xn8tTLAmaR5E4b3PAwpapEOM6ZMRTFJR2lW79zHKm4AVTG/Rq8IhXzY02ANZ2PFTWOK9rPyNOWr1xlEctmX+GCwAvib7LAoGAE88XeDKtdVp1HuZUCoHg+udaX5jFyxzFKeIJhXYfPG1OGA9iUoKWSmE5hYMJSBRYjPazpmOHVPXLj1lrOcIlViFwyFf5vR9QkcwqHPsmfD/Ney0zeblCwjP8/G8wAd2nTNm+QxU8c/eZFMiR/WenG+XWkgSMdE5sG/W2l7WvuQECgYBtVzOuL6cjbpI36A5tJdqEgS5o2KHA1any3XvufDChc4r4hgsQednfz71+i7cBI5ZIwbt2zE4VH/SrNoaxz6XTSIAs3z1f2Lv3j0UI27/404+l7Y70JuCe1foCLNCi9D1Eyea0O+8n6jr9edRi7edWy2W2SCZQOnWCpLyAIA8Vmw==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvYAahG4y9+fH650RpVHayGudwNYlbQ6Pz1Wh7eXaLeS7QWVWwFR7sgShMRXXflLgpWoTxm9zhi0fon1bSsBSKVumO9wHQSauqqOwPI4gqDcwbfKRIx2GYf8t0DV7OjkoCMGIRdXHwEejF2IDhKHfoGb3DVJNU/8uyI+jB7bC6fcSh3Yle9kSaE98tM/UIOgFGPM7ZwXEHezL90Y2d8nMWAE6NyRED1Vc/wCMm4M/dp5dURQNg++chzJoSfAudii2s3Ru4Od3N1SaBXGf/NPAXhZhylTeoszXAoYoaUdaHBtUA2EPSsk/JjP0HQAgDv7nyXxnO0D99f44udfybrXAnQIDAQAB";


    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //back 为同步返回
    public static String return_url = "http://localhost:8080/p2p/loan/alipayback";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关( 沙箱环境)
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


