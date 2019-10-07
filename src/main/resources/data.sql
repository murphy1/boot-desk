INSERT INTO issue (name) VALUES ('Broken Link');
INSERT INTO issue (name) VALUES ('Cannot upload data');
INSERT INTO issue (name) VALUES ('Problem with server');
INSERT INTO issue (name) VALUES ('Cannot access building');
INSERT INTO service_request (name) VALUES ('Update SQL data');
INSERT INTO service_request (name) VALUES ('Change server');
INSERT INTO service_request (name) VALUES ('Set up new PCs');
INSERT INTO service_request (name) VALUES ('Create new users');
INSERT INTO admin (first_name, last_Name, username, password, active, roles)
VALUES ('Stephen', 'Murphy', 'smurphy94', 'password', 'true', 'ADMIN');
INSERT INTO agent (first_name, last_Name, username, password, active, roles)
VALUES ('Andrea', 'Lee', 'anlee', 'password', 'true', 'AGENT');