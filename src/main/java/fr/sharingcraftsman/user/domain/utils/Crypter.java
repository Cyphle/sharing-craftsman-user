package fr.sharingcraftsman.user.domain.utils;

public interface Crypter {
  String encrypt(String toCrypt);

  String decrypt(String toDecrypt);
}
