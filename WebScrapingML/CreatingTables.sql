use MLS_Project;
DROP TABLE IF EXISTS TableRankBase;
CREATE TABLE dbo.TableRankBase (
    Generalkey int IDENTITY(1,1) PRIMARY KEY,
	Rk INT NOT NULL, 
	TEAM_NAME varchar(50) NOT NULL, 
	MP INT, 
	W INT, 
	D INT, 
	L INT, 
	GF INT, 
	GA INT, 
	GD INT, 
	PTS INT, 
	PTS_MP FLOAT,
	XG FLOAT, 
	XGA FLOAT, 
	XGD FLOAT, 
	XGD_90 FLOAT, 
	NOTES varchar(60),
	CONFERENCE varchar(50),
	SEASON INT NOT NULL
);

USE MLS_Project;
DROP TABLE IF EXISTS TableDetailsBase;
CREATE TABLE dbo.TableDetailsBase (
	Generalkey INT IDENTITY(1,1) PRIMARY KEY,
	TEAM_NAME varchar(50) NOT NULL, 
	GAME_Date DATE NOT NULL,
	GAME_Time TIME NOT NULL,
	Comp varchar(50),
	SEASON_Round varchar(100),
	WEEK_DAY varchar(20),
	TEAM_Venue varchar(20),
	GAME_Result varchar(5),
	GF INT,
	GA INT,
	Opponent varchar(50),
	xG FLOAT,
	xGA FLOAT,
	Poss INT,
	Attendance FLOAT,
	Captain varchar(50),
	Formation varchar(50),
	Referee varchar(50),
	Notes varchar(50),
	Season INT
);

DROP TABLE IF EXISTS TableGameDetails;
CREATE TABLE dbo.TableGameDetails (
    Generalkey int IDENTITY(1,1) PRIMARY KEY,
	GAME_Date DATE NOT NULL,
	GAME_Time TIME NOT NULL,
	WEEK_DAY varchar(20),
	TEAM_AWAY varchar(50),
	TEAM_HOME varchar(50),
	RESULT_GAME varchar(1),
	SEASON INT NOT NULL
);

use MLS_Project;
DROP TABLE IF EXISTS TableRankingStandardTeamNames;
CREATE TABLE dbo.TableRankingStandardTeamNames (
    Generalkey int IDENTITY(1,1) PRIMARY KEY,
	Rk INT NOT NULL, 
	TEAM_NAME varchar(50) NOT NULL, 
	MP INT, 
	W INT, 
	D INT, 
	L INT, 
	GF INT, 
	GA INT, 
	GD INT, 
	PTS INT, 
	PTS_MP FLOAT,
	XG FLOAT, 
	XGA FLOAT, 
	XGD FLOAT, 
	XGD_90 FLOAT, 
	NOTES varchar(60),
	CONFERENCE varchar(50),
	SEASON INT NOT NULL
);

use MLS_Project;
DROP TABLE IF EXISTS TableMatchDetailsFull;
CREATE TABLE dbo.TableMatchDetailsFull (
    Generalkey int IDENTITY(1,1) PRIMARY KEY,
	GAME_Date DATE NOT NULL,
	GAME_Time TIME NOT NULL,
	WEEK_DAY varchar(20),
	TEAM_AWAY varchar(50),
	TEAM_HOME varchar(50),
	RESULT_GAME varchar(1),
	SEASON INT NOT NULL,
	SEASON_BEFORE INT NOT NULL,

	HOME_SB_RK FLOAT,
	HOME_SB_PTS_MP FLOAT,
	HOME_SB_W_MP FLOAT,
	HOME_SB_D_MP FLOAT,
	HOME_SB_L_MP FLOAT,
	HOME_SB_GD_MP FLOAT,

	AWAY_SB_RK FLOAT,
	AWAY_SB_PTS_MP FLOAT,
	AWAY_SB_W_MP FLOAT,
	AWAY_SB_D_MP FLOAT,
	AWAY_SB_L_MP FLOAT,
	AWAY_SB_GD_MP FLOAT
);
