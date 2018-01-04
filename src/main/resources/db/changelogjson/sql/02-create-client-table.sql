CREATE TABLE clients (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
  secret longtext COLLATE utf8_unicode_ci NOT NULL,
  is_active tinyint(1) COLLATE utf8_unicode_ci NOT NULL,
  creation_date datetime DEFAULT NULL,
  last_update_date datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;