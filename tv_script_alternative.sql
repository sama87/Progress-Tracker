
DROP DATABASE TV_Show_Tracker2;

CREATE DATABASE TV_Show_Tracker2;

USE TV_Show_Tracker2;

-- CREATE TABLE TV_SHOW ( tv_id INT NOT NULL, name VARCHAR(255), description TEXT,PRIMARY KEY (tv_id));

CREATE TABLE EPISODE (
	episode_id INT NOT NULL,
    episode_name VARCHAR(50),
    episode_desc TEXT,
    PRIMARY KEY (episode_id)
);

-- CREATE TABLE USER ( username VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, PRIMARY KEY (username, password));

CREATE TABLE TV_SHOW(
	show_id INT NOT NULL,
    show_name VARCHAR(50),
    show_desc TEXT,
    PRIMARY KEY (show_id)
);

CREATE TABLE SEASON (
	show_ID INT,
    episode_ID INT,
    FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id),
    FOREIGN KEY (show_id) REFERENCES TV_SHOW(show_id)
);


-- CREATE TABLE EPISODE ( tv_id INT NOT NULL, episode_id INT NOT NULL, name VARCHAR(50), description TEXT, PRIMARY KEY (episode_id));



-- DROP TABLE HAS_SHOW;
-- CREATE TABLE HAS_SHOW( username VARCHAR(50) NOT NULL, tv_id INT NOT NULL, percentage_completed INT DEFAULT 0, FOREIGN KEY (username) REFERENCES USER (username), FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id));
CREATE TABLE USER (
	username VARCHAR(50) NOT NULL,
    password VARCHAR(50),
    PRIMARY KEY (username)
);

CREATE TABLE PLAN_TO_WATCH (
	username VARCHAR(50),
    show_id INT,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES USER(username)
);

CREATE TABLE CURRENTLY_WATCHING(
	username VARCHAR(50),
    show_id INT,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES USER(username)
);

CREATE TABLE FINALLY_WATCHED(
	username VARCHAR(50),
    show_id INT,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES USER(username)
);



-- CREATE TABLE SEASON ( tv_id INT NOT NULL, episode_id INT NOT NULL, FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id), FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id) );


-- CREATE TABLE TRACKER ( username VARCHAR(50) NOT NULL, episode_id int NOT NULL, completed boolean, in_progress boolean, not_started boolean, PRIMARY KEY (username, episode_id), FOREIGN KEY (username) REFERENCES USER (username), FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id));

-- DO NOT RUN 
CREATE TABLE CURRENTLY_WATCHING_EPISODE(
	username VARCHAR(50),
	show_id INT,
    episode_id INT,
    FOREIGN KEY (username) REFERENCES USER(username),
    FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id)
);

-- INSERT INTO user VALUES ('sdf', 'sdf');
-- INSERT INTO CURRENTLY_WATCHING VALUES ('sdf', 1);
INSERT INTO PLAN_TO_WATCH VALUES ('sdf', 1);
select * from user;

-- select * from user;
-- select * from  User;
