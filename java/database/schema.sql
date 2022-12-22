BEGIN TRANSACTION;

DROP TABLE IF EXISTS direct_messages;
DROP TABLE IF EXISTS reply_votes;
DROP TABLE IF EXISTS replies;
DROP TABLE IF EXISTS post_votes;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS forums_users;
DROP TABLE IF EXISTS forums;
DROP TABLE IF EXISTS banned_users;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE banned_users (
	user_id int NOT NULL REFERENCES users(user_id),
	admin_id int NOT NULL REFERENCES users(user_id),
	banned_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	banned_until timestamp,
	comment varchar(100), 
	PRIMARY KEY (user_id, admin_id) 
);

CREATE TABLE forums (
	forum_id SERIAL PRIMARY KEY,
	name varchar(50) NOT NULL UNIQUE,
	description varchar(500),
	time_created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE forums_users (
	forum_id int NOT NULL REFERENCES forums(forum_id),
	user_id int NOT NULL REFERENCES users(user_id),
	is_moderator boolean NOT NULL,
	PRIMARY KEY (forum_id, user_id)
);

CREATE TABLE posts (
	post_id SERIAL PRIMARY KEY,
	user_id int NOT NULL,
	forum_id int NOT NULL,
	title varchar(100) NOT NULL,
	text varchar(1000),
	media_link varchar(2048),
	date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT fk_forum_id FOREIGN KEY (forum_id) REFERENCES forums(forum_id)
);

CREATE TABLE post_votes (
	user_id int NOT NULL REFERENCES users(user_id),
	post_id int NOT NULL REFERENCES posts(post_id),
	is_upvote boolean NOT NULL,
	date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (user_id, post_id)
);

CREATE TABLE replies (
	reply_id SERIAL PRIMARY KEY,
	reply_to_id int,
	user_from_id int NOT NULL,
	post_id int NOT NULL REFERENCES posts(post_id),
	text varchar(1000),
	media_link varchar(2048),
	date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_deleted boolean NOT NULL DEFAULT false,
	CONSTRAINT fk_reply_to_id FOREIGN KEY (reply_to_id) REFERENCES replies(reply_id),
	CONSTRAINT fk_user_from_id FOREIGN KEY (user_from_id) REFERENCES users(user_id),
	CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE reply_votes (
	user_id int NOT NULL REFERENCES users(user_id),
	reply_id int NOT NULL REFERENCES replies(reply_id),
	is_upvote boolean NOT NULL,
	date_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (user_id, reply_id)
);

CREATE TABLE direct_messages (
	message_id serial PRIMARY KEY,
	user_from_id int,
	user_to_id int ,
	text varchar(200),
	media_link varchar(2048),
	time_sent timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_user_from_id FOREIGN KEY(user_from_id) REFERENCES users(user_id),
	CONSTRAINT fk_user_to_id FOREIGN KEY (user_to_id) REFERENCES users(user_id)
);


INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');
INSERT INTO users (username,password_hash,role) VALUES ('thomas','$2a$10$o5.Ewr63EeKteAETT66C3OfH0hAf70edtJYeF5HEyr.xK7irTOdKW','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('tommy','$2a$10$lqxLRsaMs2jlYS4EpK45DOTRzDO5wvXsuJ4jUj1x75ghHk3LY6rAG','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('kevin','$2a$10$beTSk0ZDcT/2bcYxnzDwdOZak/VCT.45JRwMIG/0s2fHh0CsjZ/zq','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('Annoying_Dad_Joke_Bot','$2y$10$ysXpJBFRQLpmaBsUlp1n3ezOezPIiZfyYlDAtc0UrmaEmkfWF2eoC','ROLE_USER');

INSERT INTO forums (name, description) VALUES ('ducks','We post pictures of ducks here');
INSERT INTO forums (name, description) VALUES ('trucks','We post pictures of trucks here');
INSERT INTO forums (name, description) VALUES ('flowers','We post pictures of flowers here');

INSERT INTO posts (user_id, forum_id, title, text, media_link)
		   VALUES (3, 1, 'Check out this duck', 'lol',
				   'https://a57.foxnews.com/static.foxnews.com/foxnews.com/content/uploads/2022/09/720/405/ducks-1.png?ve=1&tl=1');
INSERT INTO posts (user_id, forum_id, title, text, media_link)
		   VALUES (5, 2, 'Example2', 'HAHAHAHA',
				   'https://www.thedrive.com/uploads/2022/05/27/Ram-Heavy-Hero.jpg?auto=webp&auto=webp&optimize=high&quality=70&width=1920');
INSERT INTO posts (user_id, forum_id, title, text, media_link)
		   VALUES (4, 3, 'purple flower', 'woooo',
				   'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx4r3njXKlHUhqmsDkuxfAnUhVTSecf_1oRJWTpcAY1SV7jLHC18alzpm-zUhQU85J2jo&usqp=CAU');

INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (null, 3, 1, 'That is a big duck',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (null, 4, 1, 'I was there. The duck was truly scary',
				   'https://dejpknyizje2n.cloudfront.net/svgcustom/clipart/preview/cute-terrified-emoji-sticker-29791-300x300.png');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (null, 5, 1, 'Where was this?',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (1, 5, 1, 'It wasnt that big I have seen worse lol',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (2, 3, 1, 'THis guy is tripping over a duck omg',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (2, 4, 1, 'guys I wasnt completely serious when I made that comment',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (2, 5, 1, 'Same, but I wouldnt call it scary haha',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (3, 4, 1, 'I believe it was Scandanavia, but I could be wrong',
				   '');
INSERT INTO replies (reply_to_id, user_from_id, post_id, text, media_link)
		   VALUES (5, 4, 1, 'STOP MAKING FUN OF ME!!!',
				   '');
				   

COMMIT TRANSACTION;