from datetime import date
import time
import selenium as selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
import mysql.connector

# Start year
CompetitionYear = 2023

# Setting Chrome Driver path
path = r'C:\\Users\\arman\\OneDrive\\Desktop\\chromedriver.exe'


while (CompetitionYear <= date.today().year):
    # Opening Chrome automated
    driver = selenium.webdriver.Chrome(path)

    # Defining the URL
    url = 'https://www.mlssoccer.com/standings/' + str(CompetitionYear)

    # Opening url and maximizing the window
    driver.get(url)
    driver.maximize_window()

    while driver.execute_script("return document.readyState") != "complete":
        # Wait 10 seconds to continue execution
        time.sleep(10)

    # Obtaining the webelement with all information
    Data = driver.find_elements(By.CLASS_NAME, "mls-o-table__body")

    EasternData = Data[0]
    WesternData = Data[1]

    MyData = ["DELETE FROM STANDINGS WHERE COMPETITION_YEAR = " + str(CompetitionYear)]
    with open('Standings.txt', 'a') as f:
        f.truncate(0)
        f.write("DELETE FROM STANDINGS WHERE COMPETITION_YEAR = " + str(CompetitionYear) + ";\n")

    for x in range(len(EasternData.find_elements(By.CLASS_NAME, "mls-o-table__name"))):
        TeamName = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__name")[x].text
        TotalPoints = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_points")[x].text
        PointsPerGame = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.points_per_game")[x].text
        TotalMatches = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_matches")[x].text
        TotalWins = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_wins")[x].text
        TotalLosses = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_losses")[x].text
        TotalDraws = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_draws")[x].text
        TotalGoals = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goals")[x].text
        GoalsConceded = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goals_conceded")[x].text
        GoalsDifference = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goal_differential")[x].text
        HomeRecord = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.home_record")[x].text
        AwayRecord = EasternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.away_record")[x].text

        Insert = "INSERT INTO STANDINGS (COMPETITION_YEAR, COMPETITION_TYPE, TEAM_NAME, TOTAL_POINTS, POINTS_PER_GAME, TOTAL_MATCHES, TOTAL_WINS, TOTAL_LOSSES, TOTAL_DRAWS, TOTAL_GOALS, GOALS_CONCEDED, GOALS_DIFFERENCE, HOME_RECORD, AWAY_RECORD) VALUES ("
        Insert = Insert + str(CompetitionYear) + ",'Eastern','" + TeamName + "'," + TotalPoints + "," + PointsPerGame + "," + TotalMatches + "," + TotalWins + "," + TotalLosses + "," + TotalDraws + "," + TotalGoals + "," + GoalsConceded + "," + GoalsDifference + ",'" + HomeRecord + "','" + AwayRecord + "');"

        with open('Standings.txt', 'a') as f:
            f.write(Insert + '\n')
            f.close()

        MyData.append(Insert)

    for x in range(len(WesternData.find_elements(By.CLASS_NAME, "mls-o-table__name"))):
        TeamName = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__name")[x].text
        TotalPoints = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_points")[x].text
        PointsPerGame = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.points_per_game")[x].text
        TotalMatches = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_matches")[x].text
        TotalWins = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_wins")[x].text
        TotalLosses = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_losses")[x].text
        TotalDraws = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_draws")[x].text
        TotalGoals = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goals")[x].text
        GoalsConceded = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goals_conceded")[x].text
        GoalsDifference = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.total_goal_differential")[x].text
        HomeRecord = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.home_record")[x].text
        AwayRecord = WesternData.find_elements(By.CLASS_NAME, "mls-o-table__cell.away_record")[x].text

        Insert = "INSERT INTO STANDINGS (COMPETITION_YEAR, COMPETITION_TYPE, TEAM_NAME, TOTAL_POINTS, POINTS_PER_GAME, TOTAL_MATCHES, TOTAL_WINS, TOTAL_LOSSES, TOTAL_DRAWS, TOTAL_GOALS, GOALS_CONCEDED, GOALS_DIFFERENCE, HOME_RECORD, AWAY_RECORD) VALUES ("
        Insert = Insert + str(CompetitionYear) + ",'Western','" + TeamName + "'," + TotalPoints + "," + PointsPerGame + "," + TotalMatches + "," + TotalWins + "," + TotalLosses + "," + TotalDraws + "," + TotalGoals + "," + GoalsConceded + "," + GoalsDifference + ",'" + HomeRecord + "','" + AwayRecord + "');"

        with open('Standings.txt', 'a') as f:
            f.write(Insert + '\n')
            f.close()

        MyData.append(Insert)

    CompetitionYear += 1
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
        print("Error while connecting to MySQL")
        conn.rollback()
