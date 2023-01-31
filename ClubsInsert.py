import unicodedata
import urllib.request
import pyodbc
import selenium as selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
import time

# Connector to SQL Server
conn = pyodbc.connect('Driver={SQL Server};'
                          'Server=localhost;'
                          'Database=mls;'
                          'Trusted_Connection=yes;')

# Setting Chrome Driver path
path = r'C:\\Users\\arman\\OneDrive\\Desktop\\chromedriver.exe'

# Opening Chrome automated
driver = selenium.webdriver.Chrome(path)

# Defining the URL
url = 'https://www.mlssoccer.com/clubs/'

# Opening url and maximizing the window
driver.get(url)
driver.maximize_window()

# Wait 10 seconds to continue execution
time.sleep(10)

# Obtaining the webelement with all information
Clubs = driver.find_elements(By.CLASS_NAME, "mls-o-clubs-hub-clubs-list.d3-l-grid--outer.d3-l-section-row")

# Obtaining a list of image objects
Images = Clubs[0].find_elements(By.CLASS_NAME, "img-responsive")

# Obtaining the webelements with all Team names
ClubNames = Clubs[0].find_elements(By.CLASS_NAME, "mls-o-clubs-hub-clubs-list__club-name")

cursor = conn.cursor()
for x in range(len(ClubNames)):
    try:
        Picture = Images[x].get_attribute("src")
        n = unicodedata.normalize('NFD', ClubNames[x].text.lower().replace(" ", "_").replace(".", "")).encode('ascii', 'ignore').decode("utf-8")
        urllib.request.urlretrieve(Picture, "ClubPictures\\" + n + ".png")
    except:
        print("Error during image saving")
    try:
        cursor.execute("INSERT INTO TEAMS (TEAM_NAME) VALUES ('" + ClubNames[x].text + "')")
        conn.commit()
    except:
        print("Error during insert")

cursor.close()

driver.quit()