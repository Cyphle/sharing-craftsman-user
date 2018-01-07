CREATE TABLE roles (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  role varchar(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO roles(role) VALUES ('ROLE_USER'), ('ROLE_ADMIN');