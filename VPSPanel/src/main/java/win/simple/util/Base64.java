package win.simple.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

public class Base64 {

    public static String commpressPicForSize(String srcPath) {
        if (StringUtils.isEmpty(srcPath)) {
            return "";
        }
        if (!new File(srcPath).exists()) {
            return "";
        }

        BufferedImage bufferedImage = null;
        try {
            // 1、先转换成jpg
            bufferedImage = Thumbnails.of(srcPath).scale(0.3).asBufferedImage();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bufferedImageToBase64(bufferedImage);
    }


    public static String bufferedImageToBase64(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            // 返回Base64编码过的字节数组字符串
            return encoder.encode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String imageToBase64ByLocal(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

}
