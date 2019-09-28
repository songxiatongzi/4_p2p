package com.bjpowernode.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/22
 * description 日期类
 */
public class DateUtils {

    /**
     * 获取当前时间
     * @return 当前时间生成的字符串
     */
    public static String getTimestamp() {
        //把当前日期转化成为指定样式的字符串
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String ss = simpleDateFormat.format(new Date());
//        System.out.println(ss);
        //以上为转换过程
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()).toString();
    }
}
