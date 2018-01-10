ALTER TABLE users DROP COLUMN change_password_key;
ALTER TABLE users DROP COLUMN change_password_expiration_date;

CREATE TABLE change_password_token (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  change_password_key longtext COLLATE utf8_unicode_ci NOT NULL UNIQUE,
  change_password_expiration_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;