CREATE TABLE user_authorizations (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  access_group varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  username varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;