package fr.sharingcraftsman.user.domain.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESCrypter implements Crypter {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private static final String SECRET_KEY_1 = "FwqoMV9Vyg3d7qVu";
  private static final String SECRET_KEY_2 = "5z0YAmIGbThpAe8X";

  private IvParameterSpec ivParameterSpec;
  private SecretKeySpec secretKeySpec;
  private Cipher cipher;

  public AESCrypter() {
    try {
      ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
      secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
      cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    } catch (UnsupportedEncodingException e) {
      log.error("AESCrypter constructor - UnsupportedEncodingException : " + e.getMessage());
    } catch (NoSuchPaddingException e) {
      log.error("AESCrypter constructor - NoSuchPaddingException : " + e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      log.error("AESCrypter constructor - NoSuchAlgorithmException : " + e.getMessage());
    }
  }

  @Override
  public String encrypt(String toEncrypt) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
      return Base64.encodeBase64String(encrypted);
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      log.error("AESCrypter encrypt - InvalidKeyException : " + e.getMessage());
    } catch (IllegalBlockSizeException e) {
      log.error("AESCrypter encrypt - IllegalBlockSizeException : " + e.getMessage());
    } catch (InvalidAlgorithmParameterException e) {
      log.error("AESCrypter encrypt - InvalidAlgorithmParameterException : " + e.getMessage());
    }
    return toEncrypt;
  }

  @Override
  public String decrypt(String toDecrypt) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(toDecrypt));
      return new String(decryptedBytes);
    } catch (InvalidKeyException e) {
      log.error("AESCrypter decrypt - InvalidKeyException : " + e.getMessage());
    } catch (InvalidAlgorithmParameterException e) {
      log.error("AESCrypter decrypt - InvalidAlgorithmParameterException : " + e.getMessage());
    } catch (BadPaddingException e) {
      log.error("AESCrypter decrypt - BadPaddingException : " + e.getMessage());
    } catch (IllegalBlockSizeException e) {
      log.error("AESCrypter decrypt - IllegalBlockSizeException : " + e.getMessage());
    }
    return toDecrypt;
  }
}
