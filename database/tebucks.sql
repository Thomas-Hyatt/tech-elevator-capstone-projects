BEGIN TRANSACTION;

DROP TABLE IF EXISTS transfers;
DROP TABLE IF EXISTS statuses;
DROP TABLE IF EXISTS types;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	
	user_id serial PRIMARY KEY,
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	is_activated bool
	
);

CREATE TABLE accounts (

	account_id serial PRIMARY KEY,
	user_id int NOT NULL,
	balance numeric NOT NULL CHECK (balance >= 0),
	
	FOREIGN KEY (user_id) REFERENCES users(user_id)
	
);

CREATE TABLE types (

	type_id serial PRIMARY KEY,
	type_name varchar(16) NOT NULL

);

CREATE TABLE statuses (

	status_id serial PRIMARY KEY,
	status_name varchar(16) NOT NULL

);

CREATE TABLE transfers (

	transfer_id serial PRIMARY KEY,
	type_id int NOT NULL,
	status_id int NOT NULL,
	user_from int NOT NULL, 
	user_to int NOT NULL, 
	amount numeric NOT NULL CHECK (amount > 0) CHECK (user_from != user_to),
	
	FOREIGN KEY (type_id)  REFERENCES types(type_id),
	FOREIGN KEY (status_id) REFERENCES statuses(status_id), 
	FOREIGN KEY (user_from)  REFERENCES users(user_id),
	FOREIGN KEY (user_to)  REFERENCES users(user_id)
);

COMMIT TRANSACTION;

SELECT * FROM accounts
JOIN users USING (user_id);

INSERT INTO types (type_name) VALUES ('Send');
INSERT INTO types (type_name) VALUES ('Request');

INSERT INTO statuses (status_name) VALUES ('Pending');
INSERT INTO statuses (status_name) VALUES ('Approved');
INSERT INTO statuses (status_name) VALUES ('Rejected');

INSERT INTO transfers (type_id, status_id, user_from, user_to, amount) VALUES (1, 2, 1, 2, 20);
INSERT INTO transfers (type_id, status_id, user_from, user_to, amount) VALUES (1, 2, 2, 1, 100);
INSERT INTO transfers (type_id, status_id, user_from, user_to, amount) VALUES (2, 1, 1, 2, 40);
INSERT INTO transfers (type_id, status_id, user_from, user_to, amount) VALUES (1, 2, 2, 1, 200);


SELECT * FROM transfers;
