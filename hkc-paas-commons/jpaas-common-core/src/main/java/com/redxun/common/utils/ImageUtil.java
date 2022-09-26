package com.redxun.common.utils;

import cn.hutool.core.codec.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片处理工具类。
 *
 */
public class ImageUtil {
    /**
     * 将图片生成缩略图。
     * @param src
     * @param w
     * @param h
     * @param format
     * @return
     */
    public static byte[] thumbnailImage(byte[] src, int w, int h,String format){
        try {
            ByteArrayInputStream stream=new ByteArrayInputStream(src);
            Image img = ImageIO.read(stream);

            int width = img.getWidth(null);
            int height = img.getHeight(null);

            if(width>w || height >h){
                if(width>height){
                    h= Integer.parseInt(new java.text.DecimalFormat("0").format( (w*1.0) * (height*1.0 / width* 1.0)));
                }
                else{
                    w= Integer.parseInt(new java.text.DecimalFormat("0").format( (h*1.0) * (width*1.0 / height* 1.0)));
                }
            }
            else{
                stream.close();
                return null;
            }


            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
            g.dispose();

            ByteArrayOutputStream out=new ByteArrayOutputStream();
            ImageIO.write(bi, format, out);

            stream.close();

            byte[] rtn= out.toByteArray();
            return rtn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 根据url生成流
     * @param url
     * @return
     */
    public static BufferedImage readImage(URL url){
        BufferedImage bimg = null;
        try {
            if(url==null){
                return null;
            }
            InputStream inStream = url.openStream();
            bimg= ImageIO.read(inStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bimg;
    }
    /**
     * 根据url生成base64编码
     * @param url
     * @return
     */
    public static String getBase64ByImageUrl(URL url){
        byte[] imageInByte = new byte[0];
        try {
            BufferedImage bufferedImage = readImage(url);
            if(bufferedImage==null){
                return "";
            }
            //输出流
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", stream);
            stream.flush();
            imageInByte = stream.toByteArray();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.encode(imageInByte);
    }
}
