--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--

SET AUTOCOMMIT FALSE;

START TRANSACTION;

--
-- Insert user information into the temporary tables. To add users to the HSQL database, edit things here.
-- 

INSERT INTO users_TEMP (username, password, enabled) VALUES
  ('admin01','password',true),
  ('admin02','password',true),
  ('admin03','password',true),
  ('admin04','password',true),
  ('admin05','password',true),
  ('admin06','password',true),
  ('admin07','password',true),
  ('admin08','password',true),
  ('admin09','password',true),
  ('admin10','password',true),
  ('user01','password',true),
  ('user02','password',true),
  ('user03','password',true),
  ('user04','password',true),
  ('user05','password',true),
  ('user06','password',true),
  ('user07','password',true),
  ('user08','password',true),
  ('user09','password',true),
  ('user10','password',true);


INSERT INTO authorities_TEMP (username, authority) VALUES
  ('admin01','ROLE_ADMIN'),
  ('admin01','ROLE_USER'),
  ('admin02','ROLE_ADMIN'),
  ('admin02','ROLE_USER'),
  ('admin03','ROLE_ADMIN'),
  ('admin03','ROLE_USER'),
  ('admin04','ROLE_ADMIN'),
  ('admin04','ROLE_USER'),
  ('admin05','ROLE_ADMIN'),
  ('admin05','ROLE_USER'),
  ('admin06','ROLE_ADMIN'),
  ('admin06','ROLE_USER'),
  ('admin07','ROLE_ADMIN'),
  ('admin07','ROLE_USER'),
  ('admin08','ROLE_ADMIN'),
  ('admin08','ROLE_USER'),
  ('admin09','ROLE_ADMIN'),
  ('admin09','ROLE_USER'),
  ('admin10','ROLE_ADMIN'),
  ('admin10','ROLE_USER'),
  ('user01','ROLE_USER'),
  ('user02','ROLE_USER'),
  ('user03','ROLE_USER'),
  ('user04','ROLE_USER'),
  ('user05','ROLE_USER'),
  ('user06','ROLE_USER'),
  ('user07','ROLE_USER'),
  ('user08','ROLE_USER'),
  ('user09','ROLE_USER'),
  ('user10','ROLE_USER');
    
-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info_TEMP (sub, preferred_username, name, email, email_verified) VALUES
  ('90342.ASDFJWFA','admin01','Demo Admin01','admin01@example.com', true),
  ('90343.ASDFJWFA','admin02','Demo Admin02','admin02@example.com', true),
  ('90344.ASDFJWFA','admin03','Demo Admin03','admin03@example.com', true),
  ('90345.ASDFJWFA','admin04','Demo Admin04','admin04@example.com', true),
  ('90346.ASDFJWFA','admin05','Demo Admin05','admin05@example.com', true),
  ('90347.ASDFJWFA','admin06','Demo Admin06','admin06@example.com', true),
  ('90348.ASDFJWFA','admin07','Demo Admin07','admin07@example.com', true),
  ('90349.ASDFJWFA','admin08','Demo Admin08','admin08@example.com', true),
  ('90350.ASDFJWFA','admin09','Demo Admin09','admin09@example.com', true),
  ('90351.ASDFJWFA','admin10','Demo Admin10','admin10@example.com', true),
  ('01921.FLANRJQW','user01','Demo User01','user01@example.com', true),
  ('01922.FLANRJQW','user02','Demo User02','user02@example.com', true),
  ('01923.FLANRJQW','user03','Demo User03','user03@example.com', true),
  ('01924.FLANRJQW','user04','Demo User04','user04@example.com', true),
  ('01925.FLANRJQW','user05','Demo User05','user05@example.com', true),
  ('01926.FLANRJQW','user06','Demo User06','user06@example.com', true),
  ('01927.FLANRJQW','user07','Demo User07','user07@example.com', true),
  ('01928.FLANRJQW','user08','Demo User08','user08@example.com', true),
  ('01929.FLANRJQW','user09','Demo User09','user09@example.com', true),
  ('01930.FLANRJQW','user10','Demo User10','user10@example.com', true);

 
--
-- Merge the temporary users safely into the database. This is a two-step process to keep users from being created on every startup with a persistent store.
--

MERGE INTO users 
  USING (SELECT username, password, enabled FROM users_TEMP) AS vals(username, password, enabled)
  ON vals.username = users.username
  WHEN NOT MATCHED THEN 
    INSERT (username, password, enabled) VALUES(vals.username, vals.password, vals.enabled);

MERGE INTO authorities 
  USING (SELECT username, authority FROM authorities_TEMP) AS vals(username, authority)
  ON vals.username = authorities.username AND vals.authority = authorities.authority
  WHEN NOT MATCHED THEN 
    INSERT (username,authority) values (vals.username, vals.authority);

MERGE INTO user_info 
  USING (SELECT sub, preferred_username, name, email, email_verified FROM user_info_TEMP) AS vals(sub, preferred_username, name, email, email_verified)
  ON vals.preferred_username = user_info.preferred_username
  WHEN NOT MATCHED THEN 
    INSERT (sub, preferred_username, name, email, email_verified) VALUES (vals.sub, vals.preferred_username, vals.name, vals.email, vals.email_verified);

    
-- 
-- Close the transaction and turn autocommit back on
-- 
    
COMMIT;

SET AUTOCOMMIT TRUE;

