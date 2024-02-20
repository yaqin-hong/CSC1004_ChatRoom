package org.example;

import java.sql.*;

public class sqlite {
    static Connection sql = null;
    static Statement stmt = null;
    public sqlite() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String sqlcmd = "CREATE TABLE IF NOT EXISTS CHATROOM " +
                "(TIME           TEXT    NOT NULL, " +
                " USERNAME       TEXT    NOT NULL, " +
                " CHAT           TEXT) ";
        try {
            sql = DriverManager.getConnection("jdbc:sqlite:history.db");
            stmt = sql.createStatement();
            stmt.executeUpdate(sqlcmd);
            sql.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addRecord(String time, String name, String chat) {
        String sqlcmd = "INSERT INTO CHATROOM (TIME,USERNAME,CHAT) " +
                "VALUES ('" + time + "','" + name + "','" + chat + "');";
        try {
            stmt.executeUpdate(sqlcmd);
            sql.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String searchName(String name) {
        String sqlcmd = "SELECT * FROM CHATROOM WHERE USERNAME='" + name + "';";
        String ret = "";
        try {
            ResultSet rs = stmt.executeQuery(sqlcmd);
            while (rs.next()) {
                ret = ret + "\r\n" + rs.getString("TIME") + "/ " +
                        rs.getString("USERNAME") + ": " + rs.getString("CHAT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public String searchKey(String key) {
        String sqlcmd = "SELECT * FROM CHATROOM WHERE CHAT like '%" + key + "%';";
        String ret = "";
        try {
            ResultSet rs = stmt.executeQuery(sqlcmd);
            while (rs.next()) {
                ret = ret + "\r\n" + rs.getString("TIME") + "/ " +
                        rs.getString("USERNAME") + ": " + rs.getString("CHAT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
