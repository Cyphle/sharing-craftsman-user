CREATE TABLE tokens (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
  client varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  access_token longtext COLLATE utf8_unicode_ci NOT NULL,
  refresh_token longtext COLLATE utf8_unicode_ci NOT NULL,
  expiration_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;