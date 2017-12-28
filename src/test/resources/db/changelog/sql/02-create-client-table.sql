CREATE TABLE clients (
  id               INT(11)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name             VARCHAR(255) NOT NULL UNIQUE,
  secret           LONGTEXT     NOT NULL,
  is_active        TINYINT(1)   NOT NULL,
  creation_date    DATETIME                          DEFAULT NULL,
  last_update_date DATETIME                          DEFAULT NULL
);