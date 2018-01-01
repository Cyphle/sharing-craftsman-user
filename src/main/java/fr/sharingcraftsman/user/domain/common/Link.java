package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Link {
  private String link;

  public Link(String link) {
    this.link = link;
  }

  public String getLink() {
    return link;
  }

  public static Link to(String link) {
    return new Link(link);
  }
}
