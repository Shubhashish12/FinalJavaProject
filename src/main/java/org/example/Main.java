package org.example;

import org.example.models.ConnectDB;
import org.example.models.Students;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();

        if (connection != null) {
            Students students = new Students(connection, 0, "", 0, new int[5]);
            students.performOperations();
            db.closeConnection();
        } else {
            System.out.println("Connection failed.");
        }
    }
}
