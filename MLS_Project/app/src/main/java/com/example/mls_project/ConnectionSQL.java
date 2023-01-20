package com.example.mls_project;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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

    public boolean loginConnection (String username, String password) {
        boolean result = true;
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("SELECT USER_EMAIL FROM USERS WHERE USER_EMAIL = ? AND USER_PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                if (!rs.getString(1).equals(username)) {
                    result = false;
                }
            }
            else {
                result = false;
            }
            con.close();
        }
        catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean registerConnection (String firstName, String lastName, String email, String password) {
        boolean result = true;
        try {
            Connection con = SQLConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO USERS (FIRST_NAME, LAST_NAME, USER_EMAIL, USER_PASSWORD, USER_ADMIN) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setInt(5, 0);

            int row = ps.executeUpdate();

            if (row == 0) {
                result = false;
            }
            con.close();
        }
        catch (Exception e) {
            result = false;
        }
        return result;
    }
}
