from datetime import datetime
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
        cursor = conn.cursor()
        cursor.execute("SELECT DATE_FORMAT(MIN(SCHEDULE_DATE), '%Y%m%d') FROM SCHEDULE WHERE YEAR(SCHEDULE_DATE) = 2023 AND SCORE IS NULL")
        firstDate = cursor.fetchone()
        dataIn = firstDate[0]
        cursor.close()
        conn.close()
except Error as e:
    print("Error while connecting to MySQL", e)

# Setting Chrome Driver path
path = r'C:\\Users\\arman\\OneDrive\\Desktop\\chromedriver.exe'

# Opening Chrome automated
driver = selenium.webdriver.Chrome(path)

# Defining the URL
url = 'https://www.espn.com/soccer/schedule/_/date/' + str(dataIn) + '/league/usa.1'

# Opening url and maximizing the window
driver.get(url)
driver.maximize_window()

# Wait 10 seconds to continue execution
time.sleep(10)

while driver.execute_script("return document.readyState") != "complete":
    # Wait 10 seconds to continue execution
    time.sleep(10)

# Obtaining the web element with all information
MLSData = driver.find_elements(By.CLASS_NAME, "ResponsiveTable")

Lines = MLSData[0].find_elements(By.CLASS_NAME, "Table__TR.Table__TR--sm.Table__even")

with open('ScheduleUpdate.txt', 'a') as f:
    f.truncate(0)

MyData = []

for x in range(len(Lines)):
    Team = Lines[x].find_elements(By.CLASS_NAME, "Table__Team")
    Time = Lines[x].find_elements(By.CLASS_NAME, "date__col.Table__TD")
    Score = Lines[x].find_elements(By.CLASS_NAME, "AnchorLink.at")

    Team1 = Team[0].text
    Team2 = Team[1].text
    Score = Score[0].text.replace(' ', '').replace('-', ':')

    if Score == 'v':
        try:
            Insert = "INSERT INTO SCHEDULE (SCHEDULE_DATE, F_TEAM_NAME, S_TEAM_NAME, SCHEDULE_TIME) VALUES ('" + \
                     datetime.strptime(dataIn, "%Y%m%d").date().strftime('%Y-%m-%d') + "','" + Team1 + "','" + \
                     Team2 + "','" + Time[0].text + "');"
            with open('ScheduleUpdate.txt', 'a') as f:
                f.write(Insert + '\n')

            MyData.append(Insert)
        except:
            print('')
    else:
        try:
            Update = "UPDATE SCHEDULE S " + \
                     "INNER JOIN TEAMS T1 ON S.F_TEAM_NAME = T1.TEAM_SHORT_NAME AND T1.TEAM_ESPN = '" + \
                     Team1 + "' INNER JOIN TEAMS T2 ON S.S_TEAM_NAME = T2.TEAM_SHORT_NAME AND T2.TEAM_ESPN = '" + \
                     Team2 + "' SET S.SCORE = '" + Score + "' WHERE S.SCHEDULE_DATE = '" + \
                     datetime.strptime(dataIn, "%Y%m%d").date().strftime('%Y-%m-%d') + "';"
            with open('ScheduleUpdate.txt', 'a') as f:
                f.write(Update + '\n')

            MyData.append(Update)
        except:
            print('')

driver.quit()

try:
    conn = mysql.connector.connect(host='sql487.main-hosting.eu',
                                   database='u842004852_mlsproject_db',
                                   user='u842004852_mlsproject_use',
                                   password='vknP=j8O&a')
    if conn.is_connected():
        print("Connected to MySQL Server version ")
        cursor = conn.cursor()
        for x in MyData:
            cursor.execute(x)
        conn.commit()
        cursor.close()
        conn.close()
except:
    print("Error while connecting or inserting data into MySQL")
    conn.rollback()
