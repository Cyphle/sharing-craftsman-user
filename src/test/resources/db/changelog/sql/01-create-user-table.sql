CREATE TABLE users (
  id               INT(11)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username         VARCHAR(255) NOT NULL UNIQUE,
  firstname        VARCHAR(255) NOT NULL,
  lastname         VARCHAR(255) NOT NULL,
  password         LONGTEXT     NOT NULL,
  is_active        TINYINT(1)   NOT NULL,
  creation_date    DATETIME                          DEFAULT NULL,
  last_update_date DATETIME                          DEFAULT NULL
);