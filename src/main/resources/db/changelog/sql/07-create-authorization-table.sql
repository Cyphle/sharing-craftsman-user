CREATE TABLE authorizations (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  access_group varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  role varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;