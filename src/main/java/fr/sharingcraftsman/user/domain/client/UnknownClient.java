package fr.sharingcraftsman.user.domain.client;

public class UnknownClient extends BaseClient {
  @Override
  public boolean isKnown() {
    return false;
  }

  public static BaseClient get() {
    return new UnknownClient();
  }
}
