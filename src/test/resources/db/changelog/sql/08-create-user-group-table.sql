CREATE TABLE user_authorizations (
  id           INT(11)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
  access_group VARCHAR(255) NOT NULL UNIQUE,
  username     VARCHAR(255) NOT NULL UNIQUE
);