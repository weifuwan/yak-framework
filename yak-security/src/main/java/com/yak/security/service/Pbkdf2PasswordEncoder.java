package com.yak.security.service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.util.Base64;
public class Pbkdf2PasswordEncoder implements PasswordEncoder {
  private final int iterations;
  private final SecureRandom random = new SecureRandom();
  public Pbkdf2PasswordEncoder(int i) {
    if (i < 10000)
      throw new IllegalArgumentException("iterations must be >= 10000");
    iterations = i;
  }
  public String encode(CharSequence raw) {
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return "pbkdf2$" + iterations + "$" +
        Base64.getEncoder().encodeToString(salt) + "$" +
        hash(raw, salt, iterations);
  }
  public boolean matches(CharSequence raw, String encoded) {
    try {
      String[] p = encoded.split("\\$");
      if (p.length != 4 || !"pbkdf2".equals(p[0]))
        return false;
      byte[] salt = Base64.getDecoder().decode(p[2]);
      return MessageDigest.isEqual(
          Base64.getDecoder().decode(p[3]),
          Base64.getDecoder().decode(hash(raw, salt, Integer.parseInt(p[1]))));
    } catch (RuntimeException e) {
      return false;
    }
  }
  private String hash(CharSequence raw, byte[] salt, int count) {
    PBEKeySpec spec =
        new PBEKeySpec(raw.toString().toCharArray(), salt, count, 256);
    try {
      return Base64.getEncoder().encodeToString(
          SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
              .generateSecret(spec)
              .getEncoded());
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("PBKDF2 unavailable", e);
    } finally {
      spec.clearPassword();
    }
  }
}
