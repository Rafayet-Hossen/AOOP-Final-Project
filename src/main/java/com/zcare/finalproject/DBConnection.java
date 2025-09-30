package com.zcare.finalproject;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/zcare", "root", "rafayet150903");
    }
}
