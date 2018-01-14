CREATE TABLE users (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
  password longtext COLLATE utf8_unicode_ci NOT NULL,
  firstname varchar(255) COLLATE utf8_unicode_ci,
  lastname varchar(255) COLLATE utf8_unicode_ci,
  is_active tinyint(1) COLLATE utf8_unicode_ci NOT NULL,
  creation_date datetime DEFAULT NULL,
  last_update_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;