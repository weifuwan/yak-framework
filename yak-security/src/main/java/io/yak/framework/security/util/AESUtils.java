package io.yak.framework.security.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESUtils {
  private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);
  private static final String ALGORITHM = "AES";
  private static final String AES_KEY = "Szjx2022@666666$";
  private static final String CHARSET_NAME = "utf-8";

  public static String encrypt(String sSrc) {
    try {
      byte[] raw = AES_KEY.getBytes(CHARSET_NAME);
      SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(1, skeySpec);
      byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET_NAME));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      logger.error("encrypt failed, sSrc:{}.", (Object)sSrc, (Object)e);
      return null;
    }
  }

  public static String decrypt(String sSrc) {
    try {
      byte[] raw = AES_KEY.getBytes(CHARSET_NAME);
      SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(2, skeySpec);
      byte[] encrypted1 =
          Base64.getDecoder().decode(sSrc.getBytes(CHARSET_NAME));
      return new String(cipher.doFinal(encrypted1), CHARSET_NAME);
    } catch (Exception e) {
      logger.error("encrypt failed, sSrc:{}.", (Object)sSrc, (Object)e);
      return null;
    }
  }

  public static void main(String[] args) {
    String cSrc = "admin";
    System.out.println(cSrc);
    String enString = AESUtils.encrypt(cSrc);
    System.out.println("\u52a0\u5bc6\u540e\u7684\u5b57\u4e32\u662f\uff1a" +
                       enString);
    String DeString = AESUtils.decrypt(enString);
    System.out.println("\u89e3\u5bc6\u540e\u7684\u5b57\u4e32\u662f\uff1a" +
                       DeString);
  }
}
