package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode
@ToString
public class Email {
  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private Pattern pattern;
  private Matcher matcher;
  private String email;

  public Email(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public boolean isValid() {
    pattern = Pattern.compile(EMAIL_PATTERN);
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public static Email from(String email) {
    return new Email(email);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Email email1 = (Email) o;

    return email != null ? email.equals(email1.email) : email1.email == null;
  }

  @Override
  public int hashCode() {
    return email != null ? email.hashCode() : 0;
  }
}
