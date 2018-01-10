package fr.sharingcraftsman.user.domain.client;

public class UnknownClient extends AbstractClient {
  @Override
  public boolean isKnown() {
    return false;
  }

  public static AbstractClient get() {
    return new UnknownClient();
  }
}
