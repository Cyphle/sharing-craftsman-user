ALTER TABLE users DROP COLUMN change_password_key;
ALTER TABLE users DROP COLUMN change_password_expiration_date;

CREATE TABLE change_password_tokens (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
  change_password_token longtext COLLATE utf8_unicode_ci NOT NULL,
  change_password_expiration_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;