CREATE TABLE tokens (
  id              INT(11)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username        VARCHAR(255) NOT NULL UNIQUE,
  client          VARCHAR(255) NOT NULL UNIQUE,
  access_token    LONGTEXT     NOT NULL,
  refresh_token   LONGTEXT     NOT NULL,
  expiration_date DATETIME                          DEFAULT NULL
);