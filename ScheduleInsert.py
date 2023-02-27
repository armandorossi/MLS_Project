from datetime import date, datetime, timedelta
import time
import selenium as selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
import mysql.connector
from mysql.connector import Error

try:
    conn = mysql.connector.connect(host='sql487.main-hosting.eu',
                                         database='u842004852_mlsproject_db',
                                         user='u842004852_mlsproject_use',
                                         password='vknP=j8O&a')
    if conn.is_connected():
        print("Connected to MySQL Server version ")
except Error as e:
    print("Error while connecting to MySQL", e)

# Getting the last date saved
cursor = conn.cursor()
# cursor.execute("SELECT REPLACE(CONVERT(VARCHAR, MAX(SCHEDULE_DATE), 103), '/', '') FROM SCHEDULE")
cursor.execute("SELECT DATE_FORMAT(MAX(SCHEDULE_DATE), '%d%m%Y') FROM SCHEDULE")
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
        GameDate = datetime.strptime(GameDate, "%m/%d/%Y").date()
        # Info could be game time (H:MM) or Cancelled info
        try:
            GameInfo = MLSData[x].find_elements(By.CLASS_NAME, "mls-o-match-strip__time")[1].text
            if GameInfo.find("pm"):
                GameInfo = GameInfo.replace("pm", "") + " PM"
            else:
                GameInfo = GameInfo.replace("am", "") + " AM"
            GameInfo = datetime.strptime(GameInfo, "%I:%M %p").strftime("%H:%M")
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
                Line = "INSERT INTO SCHEDULE (SCHEDULE_DATE, F_TEAM_NAME, S_TEAM_NAME, SCHEDULE_TIME, SCORE) VALUES ('" + GameDate.strftime("%Y-%m-%d") + "','" + Team1 + "','" + Team2 + "'," + GameInfo + ",'" + Score + "')"
            else:
                Line = "INSERT INTO SCHEDULE (SCHEDULE_DATE, F_TEAM_NAME, S_TEAM_NAME, SCHEDULE_TIME, SCORE) VALUES ('" + GameDate.strftime("%Y-%m-%d") + "','" + Team1 + "','" + Team2 + "','" + GameInfo + "','" + Score + "')"
            # Trying to save data to database. If fails, log will be saved to see what happened
            cursor = conn.cursor()
            try:
                cursor.execute(Line)
                conn.commit()
                with open('log.txt', 'a') as f:
                    f.write('\n' + Line + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " SUCCESS")
            except:
                Line = "UPDATE SCHEDULE SET SCORE = '" + Score + "' WHERE SCHEDULE_DATE = '" + GameDate.strftime("%Y-%m-%d") + "' AND F_TEAM_NAME = '" + Team1 + "' AND S_TEAM_NAME = '" + Team2 + "'"
                cursor.execute(Line)
                conn.commit()
                with open('log.txt', 'a') as f:
                    f.write('\n' + Line + " - " + str(datetime.now().strftime("%d/%m/%Y %H:%M:%S")) + " FAILED")
                    f.close()
                print("Error during insert registered to log")
            cursor.close()
            conn.close()

    driver.quit()

    # Adding 7 days (website rule) for next fetch
    dataIn = dataIn + timedelta(days=7)
