
DROP DATABASE TV_Show_Tracker;

CREATE DATABASE TV_Show_Tracker;

USE TV_Show_Tracker;

-- CREATE TABLE TV_SHOW ( tv_id INT NOT NULL, name VARCHAR(255), description TEXT,PRIMARY KEY (tv_id));

CREATE TABLE EPISODE (
	episode_id INT NOT NULL,
    episode_name VARCHAR(50),
    episode_desc TEXT,
    PRIMARY KEY (episode_id)
);

-- CREATE TABLE USER ( username VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, PRIMARY KEY (username, password));
CREATE TABLE SEASON (
	show_ID INT,
    episode_ID INT,
    FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id)
);


-- CREATE TABLE EPISODE ( tv_id INT NOT NULL, episode_id INT NOT NULL, name VARCHAR(50), description TEXT, PRIMARY KEY (episode_id));

CREATE TABLE TV_SHOW(
	show_id INT NOT NULL,
    show_name VARCHAR(50),
    show_desc TEXT,
    PRIMARY KEY (show_id),
    FOREIGN KEY (show_id) REFERENCES TV_SHOW(show_id)
);

-- DROP TABLE HAS_SHOW;
-- CREATE TABLE HAS_SHOW( username VARCHAR(50) NOT NULL, tv_id INT NOT NULL, percentage_completed INT DEFAULT 0, FOREIGN KEY (username) REFERENCES USER (username), FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id));

CREATE TABLE PLAN_TO_WATCH (
	username VARCHAR(50),
    show_id INT
);

CREATE TABLE CURRENTLY_WATCHING(
	username VARCHAR(50),
    show_id INT
);

CREATE TABLE FINALLY_WATCHED(
	username VARCHAR(50),
    SHOW_ID INT
);

CREATE TABLE USER (
	username VARCHAR(50) NOT NULL,
    password VARCHAR(50),
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES PLAN_TO_WATCH(username),
    FOREIGN KEY (username) REFERENCES CURRENTLY_WATCHING(username),
    FOREIGN KEY (username) REFERENCES FINALLY_WATCHING(username)
);

-- CREATE TABLE SEASON ( tv_id INT NOT NULL, episode_id INT NOT NULL, FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id), FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id) );


CREATE TABLE TRACKER (
	username VARCHAR(50) NOT NULL,
    episode_id int NOT NULL,
    completed boolean,
    in_progress boolean,
    not_started boolean,
    PRIMARY KEY (username, episode_id),
    FOREIGN KEY (username) REFERENCES USER (username),
    FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id)
);

-- select * from user;
-- select * from  User;
