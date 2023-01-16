DROP DATABASE TV_Show_Tracker;

CREATE DATABASE TV_Show_Tracker;

USE TV_Show_Tracker;

CREATE TABLE TV_SHOW (
	tv_id INT NOT NULL,
    name VARCHAR(255),
    description TEXT,
    PRIMARY KEY (tv_id)
);

CREATE TABLE USER (
	username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (username, password)
);

CREATE TABLE EPISODE (
	tv_id INT NOT NULL,
	episode_id INT NOT NULL,
    name VARCHAR(50),
    description TEXT, 
    PRIMARY KEY (episode_id)
);

CREATE TABLE HAS_SHOW(
	username VARCHAR(50) NOT NULL,
    tv_id INT NOT NULL,
    percentage_completed INT,
    PRIMARY KEY (username, tv_id),
    FOREIGN KEY (username) REFERENCES USER (username),
    FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id)
);


CREATE TABLE SEASON (
	tv_id INT NOT NULL,
    episode_id INT NOT NULL,
    FOREIGN KEY (tv_id) REFERENCES TV_SHOW(tv_id),
    FOREIGN KEY (episode_id) REFERENCES EPISODE(episode_id)
);


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

select * from user;
select * from tv_show;
select * from episode;

delete from user 
where username='joel';
