package com.fun.util;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-08-31 10:54
 */
public class ImageDownloader {

    public static void download(String imgUrl, String fileName, String savePath) throws IOException {
        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        // 通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        // 得到图片的二进制数据，以二进制封装得到的数据
        byte[] data = readInputStream(inStream);
        inStream.close();
        File imageFile = new File(savePath + fileName + ".jpg");
        FileOutputStream outStream = new FileOutputStream(imageFile);
        outStream.write(data);
        outStream.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        while( (len = inStream.read(buffer)) != -1 ) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        return outStream.toByteArray();
    }
}