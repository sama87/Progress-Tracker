## DROP DATABASE TV_Show_Tracker;

CREATE DATABASE TV_Show_Tracker;

USE TV_Show_Tracker;

CREATE TABLE HAS_SHOW(
	username VARCHAR(50) NOT NULL,
    id INT NOT NULL,
    percentage_completed INT,
    PRIMARY KEY (username, id)
);

SHOW TABLES;

CREATE TABLE TV_SHOW (
	id INT NOT NULL,
    name VARCHAR(255),
    description VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES HAS_SHOW(id)
);