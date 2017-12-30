package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode
public class Email {
  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private Pattern pattern;
  private Matcher matcher;
  private String email;

  public Email(String email) {
    this.email = email;
  }

  public static Email from(String email) {
    return new Email(email);
  }

  public boolean isValid() {
    pattern = Pattern.compile(EMAIL_PATTERN);
    matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
