package com.example.mls_project.Database;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.example.mls_project.BuildConfig;
import com.example.mls_project.Entities.Schedule;
import com.example.mls_project.Entities.Team;
import com.example.mls_project.Entities.TeamStandings;
import com.example.mls_project.Entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectionSQL {

    private static final String url = BuildConfig.URL;
    private static final String user = BuildConfig.USER;
    private static final String pass = BuildConfig.KEY;

    //Method to create a connection and return it to other methods
    private Connection SQLConnection() {
        Connection con;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            return con;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return null;
        }
    }

    //Method to get a connection with the database and retrieve a specific user by username and password (encrypted)
    public String loginConnection (String username, String password) {
        String result = "Yes;Login accepted";
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT USER_EMAIL, USER_STATUS, IF(USER_ADMIN = 1, 'Yes', 'No') USER_ADMIN " +
                    "FROM USERS " +
                    "WHERE USER_EMAIL = ? AND USER_PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                if (!rs.getString(1).equals(username)) {
                    result = "No;Username not found;";
                }
                else if (rs.getInt(2) == 0) { //User deactivated
                    result = "No;Your profile is deactivated;";
                }
                else {
                    result += ";" + rs.getString(3);
                }
            }
            else {
                result = "No;Username or password incorrect;";
            }
            con.close();
        }
        catch (Exception e) {
            result = "No;" + e.getMessage() + ";";
        }
        return result;
    }

    //Method to register a new user
    public boolean registerConnection (Context context, String firstName, String lastName, String email, String password) {
        boolean result = true;
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS (FIRST_NAME, LAST_NAME, USER_EMAIL, USER_PASSWORD, USER_ADMIN, USER_STATUS) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setInt(5, 0); //Fixed as an admin cannot be created using this application
            ps.setInt(6, 1); //Fixed because a new user must be active

            int row = ps.executeUpdate();

            if (row == 0) { //Insert fails
                result = false;
            }
            con.close();
        }
        catch (Exception e) {
            if (Objects.requireNonNull(e.getMessage()).contains("Duplicate entry")) { //Checking if the user is already registered
                Toast.makeText(context, "Username already exists", Toast.LENGTH_LONG).show();
            }
            result = false;
        }
        return result;
    }

    //Method to return a list of years from database
    public List<String> yearList () {
        List<String> list = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT(YEAR(SCHEDULE_DATE)) YEAR " +
                    "FROM SCHEDULE " +
                    "ORDER BY YEAR DESC");

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(rs.getString(1));
            }
            con.close();
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    //Method to return a list of games for a specific year and month
    public List<Schedule> scheduleList (int year, int month) {
        List<Schedule> scheduleList = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT S.SCHEDULE_DATE, S.F_TEAM_NAME, S.S_TEAM_NAME, S.SCHEDULE_TIME, S.SCORE, T1.TEAM_NAME, T2.TEAM_NAME " +
                    "FROM SCHEDULE S " +
                    "INNER JOIN TEAMS T1 ON S.F_TEAM_NAME = T1.TEAM_SHORT_NAME " +
                    "INNER JOIN TEAMS T2 ON S.S_TEAM_NAME = T2.TEAM_SHORT_NAME " +
                    "WHERE YEAR(SCHEDULE_DATE) = ? AND MONTH(SCHEDULE_DATE) = ? " +
                    "ORDER BY SCHEDULE_DATE, SCHEDULE_TIME");
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Schedule schedule = new Schedule(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                scheduleList.add(schedule);
            }
        }
        catch (Exception e) {
            scheduleList = null;
            Log.e("Error", e.getMessage());
        }
        return scheduleList;
    }

    //Method to return a user list from database
    public List<User> userList (int userStatus) {
        List<User> userList = new ArrayList<>();

        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT CONCAT(FIRST_NAME, ' ', LAST_NAME) FULL_NAME, USER_EMAIL, IF(USER_ADMIN = 1, 'Yes', 'No') USER_ADMIN, IF(USER_STATUS = 1, 'Active', 'Inactive') USER_STATUS " +
                    "FROM USERS WHERE USER_STATUS = ? " +
                    "ORDER BY FIRST_NAME");
            ps.setInt(1, userStatus);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                userList.add(user);
            }
        }
        catch (Exception e) {
            userList = null;
            Log.e("Error", e.getMessage());
        }
        return userList;
    }

    //Method to update a user status (Active or Inactive)
    public int updateUserStatus (String email, int status) {
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("UPDATE USERS SET USER_STATUS = ? WHERE USER_EMAIL = ?");
            ps.setInt(1, status);
            ps.setString(2, email);
            int result = ps.executeUpdate();
            con.close();
            return result;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return 0;
        }
    }

    public List<Team> teamList () {
        List<Team> teamList = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT TEAM_NAME, TEAM_SHORT_NAME FROM TEAMS ORDER BY TEAM_NAME");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Team team = new Team(rs.getString(1), rs.getString(2));
                teamList.add(team);
            }
        }
        catch (Exception e) {
            teamList = null;
            Log.e("Error", e.getMessage());
        }
        return teamList;
    }

    public List<Schedule> teamSchedule (String teamName, String date) {
        List<Schedule> teamScheduleList = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT S.SCHEDULE_DATE, S.F_TEAM_NAME, S.S_TEAM_NAME, S.SCHEDULE_TIME, S.SCORE, T1.TEAM_NAME, T2.TEAM_NAME " +
                    "FROM SCHEDULE S " +
                    "INNER JOIN TEAMS T1 ON S.F_TEAM_NAME = T1.TEAM_SHORT_NAME " +
                    "INNER JOIN TEAMS T2 ON S.S_TEAM_NAME = T2.TEAM_SHORT_NAME " +
                    "WHERE S.SCHEDULE_DATE >= ? AND (T1.TEAM_NAME = ? OR T2.TEAM_NAME = ?) " +
                    "ORDER BY S.SCHEDULE_DATE, S.SCHEDULE_TIME");
            ps.setString(1, date);
            ps.setString(2, teamName);
            ps.setString(3, teamName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                teamScheduleList.add(schedule);
            }
        }
        catch (Exception e) {
            teamScheduleList = null;
            Log.e("Error", e.getMessage());
        }
        return teamScheduleList;
    }

    public List<TeamStandings> teamStandings (String year) {
        List<TeamStandings> teamStandingsList = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            assert con != null;
            PreparedStatement ps = con.prepareStatement("SELECT T.TEAM_SHORT_NAME, IFNULL(S.COMPETITION_YEAR, 'N/A'), S.TOTAL_POINTS, S.TOTAL_WINS, S.TOTAL_LOSSES, S.TOTAL_DRAWS " +
                    "FROM TEAMS T " +
                    "LEFT JOIN STANDINGS S ON T.TEAM_SHORT_NAME = S.TEAM_NAME AND S.COMPETITION_YEAR = ? " +
                    "ORDER BY T.TEAM_SHORT_NAME, S.COMPETITION_YEAR");
            ps.setString(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TeamStandings teamWins = new TeamStandings(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                teamStandingsList.add(teamWins);
            }
        }
        catch (Exception e) {
            teamStandingsList = null;
            Log.e("Error", e.getMessage());
        }
        return teamStandingsList;
    }
}
