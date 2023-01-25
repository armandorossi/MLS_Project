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

    private static final String url = "jdbc:mysql://sql487.main-hosting.eu/u842004852_mlsproject_db";
    private static final String user = "u842004852_mlsproject_use";
    private static final String pass = "vknP=j8O&a";

    private Connection SQLConnection() {
        Connection con =  null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            return con;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return con;
        }
    }

    public String loginConnection (String username, String password) {
//        boolean result = true;
        String result = "1;Login accepted";
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT USER_EMAIL, USER_STATUS, USER_ADMIN FROM USERS WHERE USER_EMAIL = ? AND USER_PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                if (!rs.getString(1).equals(username)) {
                    result = "0;Username not found;";
//                    result = false;
                }
                else if (rs.getInt(2) == 0) { //User deactivated
                    result = "0;Your profile is deactivated;";
//                    result = false;
                }
                else {
                    result += ";" + rs.getString(3);
                }
            }
            else {
                result = "0;Username or password incorrect;";
//                result = false;
            }
            con.close();
        }
        catch (Exception e) {
            result = "0;" + e.getMessage() + ";";
//            result = false;
        }
        return result;
    }

    public boolean registerConnection (Context context, String firstName, String lastName, String email, String password) {
        boolean result = true;
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS (FIRST_NAME, LAST_NAME, USER_EMAIL, USER_PASSWORD, USER_ADMIN, USER_STATUS) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setInt(5, 0);
            ps.setInt(6, 1);

            int row = ps.executeUpdate();

            if (row == 0) {
                result = false;
            }
            con.close();
        }
        catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                Toast.makeText(context, "Username already exists", Toast.LENGTH_LONG).show();
            }
            result = false;
        }
        return result;
    }

    public List<String> yearList () {
        List<String> list = new ArrayList<String>();
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT(YEAR(SCHEDULE_DATE)) YEAR FROM SCHEDULE ORDER BY YEAR DESC");

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(rs.getString(1));
            }
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    public List<String> scheduleList (int year, int month) {
        List<String> list = new ArrayList<String>();
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT S.*, T1.TEAM_NAME, T2.TEAM_NAME FROM SCHEDULE S INNER JOIN TEAMS T1 ON S.F_TEAM_NAME = T1.TEAM_SHORT_NAME INNER JOIN TEAMS T2 ON S.S_TEAM_NAME = T2.TEAM_SHORT_NAME WHERE YEAR(SCHEDULE_DATE) = ? AND MONTH(SCHEDULE_DATE) = ? ORDER BY SCHEDULE_DATE, SCHEDULE_TIME");
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
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    public List<String> userList (int userStatus) {
        List<String> list = new ArrayList<>();
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT CONCAT(FIRST_NAME, ' ', LAST_NAME) FULL_NAME, USER_EMAIL, USER_ADMIN, USER_STATUS FROM USERS WHERE USER_STATUS = ? ORDER BY FIRST_NAME");
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
        }
        catch (Exception e) {
            list = null;
            Log.e("Error", e.getMessage());
        }
        return list;
    }

    public int updateUserStatus (String email, int status) {
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE USERS SET USER_STATUS = ? WHERE USER_EMAIL = ?");
            ps.setInt(1, status);
            ps.setString(2, email);
            return ps.executeUpdate();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return 0;
        }
    }
}
