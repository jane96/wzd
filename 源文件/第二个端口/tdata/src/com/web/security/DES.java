package com.web.security;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
/**
 DES加密介绍
      DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。
 	注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 @author hz
 */
public class DES {
    public DES() {}
    public final static BASE64Encoder base64encoder = new BASE64Encoder(); 
	public final static BASE64Decoder base64decoder = new BASE64Decoder(); 
    public final static String PASSWORD = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
    public final static byte[] SALT = 	"9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456".getBytes();
    //测试
    public static void main(String args[]) throws IOException {
        //待加密内容
     String str = "system";
     //密码，长度要是8的倍数
     byte[] result = DES.encrypt(str.getBytes(),PASSWORD);
     String s = base64encoder.encode(result);
     byte[] s2 = base64decoder.decodeBuffer(s);
     System.out.println("加密后："+s);
     //直接将如上内容解密
     try {
             byte[] decryResult = DES.decrypt(s2, PASSWORD);
             System.out.println("解密后："+new String(decryResult));
     } catch (Exception e1) {
             e1.printStackTrace();
     }
 }
    /**
     * 加密
     * @param datasource byte[]
     * @param password String
     * @return byte[]
     */
    public static  byte[] encrypt(byte[] datasource, String password) {            
        try{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        //现在，获取数据并加密
        //正式执行加密操作
        return cipher.doFinal(datasource);
        }catch(Throwable e){
                e.printStackTrace();
        }
        return null;
}
    /**
     * 解密
     * @param src byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(src);
        }
    
}