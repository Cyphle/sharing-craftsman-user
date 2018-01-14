ALTER TABLE users ADD COLUMN change_password_key longtext COLLATE utf8_unicode_ci;
ALTER TABLE users ADD COLUMN change_password_expiration_date datetime