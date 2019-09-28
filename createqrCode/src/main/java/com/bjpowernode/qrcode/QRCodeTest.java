package com.bjpowernode.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/23
 * @description 二维码图片生成器
 */
public class QRCodeTest {
    public static void main(String[] args) throws IOException, WriterException {

        //矩阵编码的格式
        Map<EncodeHintType, Object> encodeHintTypeObjectMap = new HashMap<>();
        encodeHintTypeObjectMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        //创建一个矩阵对象(encode 编码)并指定编码格式(Format 格式)
        //将微信响应的参数作为地址就可以发送
        String url = "https://www.baidu.com/";
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 500, 500, encodeHintTypeObjectMap);

        //文件的地址
        String filePath = "D://";
        //文件的名称
        String fileName = "Test.png";
        //生成文件的路径(这里编译级别会产生错误)
        Path path = FileSystems.getDefault().getPath(filePath, fileName);

        //将矩阵对象转换成二维码图片
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);

        System.out.println("二维码生成路径" + path);
    }
}
