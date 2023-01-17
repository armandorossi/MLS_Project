from datetime import date, datetime, timedelta
import time
import selenium as selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
import pyodbc

# Connector to SQL Server
conn = pyodbc.connect('Driver={SQL Server};'
                          'Server=localhost;'
                          'Database=mls;'
                          'Trusted_Connection=yes;')

# Getting the last date saved
cursor = conn.cursor()
cursor.execute("SELECT REPLACE(CONVERT(VARCHAR, MAX(SCHEDULE_DATE), 103), '/', '') FROM SCHEDULE")
LastDate = cursor.fetchone()
cursor.close()

# Setting start date to fetch from MLS schedule website
if (not LastDate[0]):
    dataIn = datetime.strptime("29122014", "%d%m%Y").date() # 2014-12-29
else:
    dataIn = datetime.strptime(LastDate[0], "%d%m%Y").date()

# Adjusting the start date to match website rules (schedule search always starts on monday)
while (datetime.weekday(dataIn) != 0):
    dataIn = dataIn - timedelta(days=1)

# Getting today date
dataOut = datetime.strptime("3112" + str(date.today().year), "%d%m%Y").date()

# Setting Chrome Driver path
path = r'C:\\Users\\arman\\OneDrive\\Desktop\\chromedriver.exe'

# Loop to fetch all necessary data
while (dataIn < dataOut):
    # Opening Chrome automated
    driver = selenium.webdriver.Chrome(path)

    # Defining the URL
    url = 'https://www.mlssoccer.com/schedule/scores#competition=mls-regular-season&club=all&date=' + str(dataIn)


    # Opening url and maximizing the window
    driver.get(url)
    driver.maximize_window()

    # Wait 10 seconds to continue execution
    time.sleep(10)

    # Obtaining the webelement with all information
    MLSData = driver.find_elements(By.CLASS_NAME, "mls-o-match-strip__match")

    # Loop to obtain each information individually
    for x in range(len(MLSData)):
        # Game date as available at MLS Schedule
        GameDate = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__time")[0].text
        # Info could be game time (H:MM) or Cancelled info
        try:
            GameInfo = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__time")[1].text
        except:
            GameInfo = "null"
        # Team 1 and 2 info
        Team1 = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__club-short-name")[0].text
        Team2 = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__club-short-name")[1].text
        # Game score when game has been already played
        try:
            Score = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__score.mls-o-match-strip__score--post ")[0].text
        except:
            Score = "null"
        # If game has been cancelled, no info will be saved. Otherwise, a query is set to be executed
        if GameDate != "Cancelled":
            if GameInfo == "null":
                Line = "INSERT INTO SCHEDULE VALUES ('" + GameDate + "','" + Team1 + "','" + Team2 + "'," + GameInfo + ",'" + Score + "')"
            else:
                Line = "INSERT INTO SCHEDULE VALUES ('" + GameDate + "','" + Team1 + "','" + Team2 + "','" + GameInfo + "','" + Score + "')"
            # Trying to save data to database. If fails, log will be saved to see what happened
            cursor = conn.cursor()
            try:
                cursor.execute(Line)
                conn.commit()
                with open('log.txt', 'a') as f:
                    f.write('\n' + Line + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " SUCCESS")
            except:
                with open('log.txt', 'a') as f:
                    f.write('\n' + Line + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " FAILED")
                    f.close()
                print("Error during insert registered to log")
            cursor.close()

    driver.quit()

    # Adding 7 days (website rule) for next fetch
    dataIn = dataIn + timedelta(days=7)

    # OLD CODE

    # # Finding classes in the URL (Date and Time, Team names and Score)
    # # For games that has been already played, Time will not be available for most games. For future games, score will not be available
    # DateTime = driver.find_elements(By.CLASS_NAME, "mls-o-match-strip__time")
    # HomeTeam = driver.find_elements(By.CLASS_NAME, "mls-o-match-strip__club-short-name")
    # Scores = driver.find_elements(By.CLASS_NAME, "mls-o-match-strip__score.mls-o-match-strip__score--post ")
    #
    # # Variables to fetch data
    # Dates = []
    # Times = []
    # Teams = []
    # GameScores = []
    # Cancelled = 0
    #
    # # Loop to save game Date and Time (when available) in the right place
    # for dt in DateTime:
    #     text = dt.text
    #     if (text.find('/') != -1):
    #         Dates.append("'" + text + "'")
    #     elif (text.find(':') != -1):
    #         Times.append("'" + text + "'")
    #     elif (text == "Cancelled"):
    #         Cancelled = 1
    #
    # # Loop to save teams like 'team1','team2'
    # y = 0
    # for x in range(len(HomeTeam)):
    #     if (x % 2 != 0):
    #         Teams[y] = Teams[y] + ",'" + HomeTeam[x].text + "'"
    #         y = y + 1
    #     else:
    #         Teams.append("'" + HomeTeam[x].text + "'")
    #
    # # Loop to save score, when available
    # for s in Scores:
    #     text = s.text
    #     GameScores.append("'" + text + "'")
    #
    # # Creating the insert query as INSERT INTO SCHEDULE VALUES('date','team1','team2','time','score')
    # Lines = []
    # Line = "INSERT INTO SCHEDULE VALUES ("
    # for x in range(len(Dates)):
    #     try:
    #         Line = Line + Dates[x] + ","
    #     except:
    #         Line = Line + "null,"
    #     try:
    #         Line = Line + Teams[x] + ","
    #     except:
    #         Line = Line + "null,"
    #     try:
    #         Line = Line + Times[x] + ","
    #     except:
    #         Line = Line + "null,"
    #     try:
    #         Line = Line + GameScores[x] + ","
    #     except:
    #         Line = Line + "null,"
    #     Line = Line + str(Cancelled) + ")"
    #     Lines.append(Line)
    #     Line = "INSERT INTO SCHEDULE VALUES ("
    #
    # # Inserting into database and saving log with SUCCESS or FAIL message
    # if len(Lines) > 0:
    #     cursor = conn.cursor()
    #     for y in range(len(Lines)):
    #         try:
    #             cursor.execute(Lines[y])
    #             conn.commit()
    #             with open('log.txt', 'a') as f:
    #                 f.write('\n' + Lines[y] + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " SUCCESS")
    #         except:
    #             with open('log.txt', 'a') as f:
    #                 f.write('\n' + Lines[y] + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " FAILED")
    #                 f.close()
    #             print("Error during insert registered to log")
    #     cursor.close()
    #
    # driver.quit()

# conn.close()