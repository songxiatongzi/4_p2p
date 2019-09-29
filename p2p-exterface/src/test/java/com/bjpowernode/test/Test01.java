package com.bjpowernode.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述信息 ：测试日日志
 *
 * @author 明月出天山，苍茫云海间
 * @date 2019/9/29
 */
public class Test01 {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Test01.class);
        logger.info("ceshi");

    }
}
