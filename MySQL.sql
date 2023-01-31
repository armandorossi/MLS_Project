DROP TABLE USERS;
CREATE TABLE USERS(
	ID				INT PRIMARY KEY AUTO_INCREMENT
,	FIRST_NAME		VARCHAR(50)
,	LAST_NAME		VARCHAR(100)
,	USER_EMAIL		VARCHAR(200)
,	USER_PASSWORD	VARCHAR(200)
,	USER_ADMIN		BIT
,	USER_STATUS		BIT
,	UNIQUE (USER_EMAIL)
);

DROP TABLE SCHEDULE;
CREATE TABLE SCHEDULE(
	ID				INT PRIMARY KEY AUTO_INCREMENT
,	SCHEDULE_DATE	DATE NOT NULL
,	F_TEAM_NAME		VARCHAR(100) NOT NULL
,	S_TEAM_NAME		VARCHAR(100) NOT NULL
,	SCHEDULE_TIME	TIME(0)
,	SCORE			VARCHAR(10)
,	UNIQUE (SCHEDULE_DATE, F_TEAM_NAME, S_TEAM_NAME)
);

DROP TABLE TEAMS;
CREATE TABLE TEAMS(
	TEAM_ID			INT PRIMARY KEY AUTO_INCREMENT
,	TEAM_NAME		VARCHAR(50) NOT NULL
,	TEAM_SHORT_NAME	VARCHAR(50) NULL
,	UNIQUE (TEAM_NAME)
);