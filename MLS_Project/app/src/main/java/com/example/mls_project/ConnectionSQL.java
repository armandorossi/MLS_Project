package com.example.mls_project;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ConnectionSQL {

    private static final String url = BuildConfig.URL;//"jdbc:mysql://sql487.main-hosting.eu/u842004852_mlsproject_db";
    private static final String user = BuildConfig.USER;//"u842004852_mlsproject_use";
    private static final String pass = BuildConfig.KEY;//"vknP=j8O&a";

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
        String result = "1;Login accepted";
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT USER_EMAIL, USER_STATUS, USER_ADMIN " +
                    "FROM USERS " +
                    "WHERE USER_EMAIL = ? AND USER_PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                if (!rs.getString(1).equals(username)) {
                    result = "0;Username not found;";
                }
                else if (rs.getInt(2) == 0) { //User deactivated
                    result = "0;Your profile is deactivated;";
                }
                else {
                    result += ";" + rs.getString(3);
                }
            }
            else {
                result = "0;Username or password incorrect;";
            }
            con.close();
        }
        catch (Exception e) {
            result = "0;" + e.getMessage() + ";";
        }
        return result;
    }

    //Method to register a new user
    public boolean registerConnection (Context context, String firstName, String lastName, String email, String password) {
        boolean result = true;
        try {
            Connection con = SQLConnection();
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
            if (e.getMessage().contains("Duplicate entry")) { //Checking if the user is already registered
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
    public List<String> scheduleList (int year, int month) {
        List<String> list = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT S.*, T1.TEAM_NAME, T2.TEAM_NAME " +
                    "FROM SCHEDULE S " +
                    "INNER JOIN TEAMS T1 ON S.F_TEAM_NAME = T1.TEAM_SHORT_NAME " +
                    "INNER JOIN TEAMS T2 ON S.S_TEAM_NAME = T2.TEAM_SHORT_NAME " +
                    "WHERE YEAR(SCHEDULE_DATE) = ? AND MONTH(SCHEDULE_DATE) = ? " +
                    "ORDER BY SCHEDULE_DATE, SCHEDULE_TIME");
            ps.setInt(1, year);
            ps.setInt(2, month);

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()){
                String value = "";
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i == 1) {
                        value = rs.getString(i);
                    }
                    else {
                        value += ";" + rs.getString(i);
                    }
                }
                list.add(value);
            }
            con.close();
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    //Method to return a user list from database
    public List<String> userList (int userStatus) {
        List<String> list = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT CONCAT(FIRST_NAME, ' ', LAST_NAME) FULL_NAME, USER_EMAIL, USER_ADMIN, USER_STATUS " +
                    "FROM USERS WHERE USER_STATUS = ? " +
                    "ORDER BY FIRST_NAME");
            ps.setInt(1, userStatus);

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String value = "";
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (i == 1) {
                        value = rs.getString(i);
                    }
                    else {
                        value += ";" + rs.getString(i);
                    }
                }
                list.add(value);
            }
            con.close();
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    //Method to update a user status (Active or Inactive)
    public int updateUserStatus (String email, int status) {
        try {
            Connection con = SQLConnection();
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
}
