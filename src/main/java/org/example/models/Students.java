package org.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;

public class Students extends People {
    private int grade;
    private int[] marks;
    private Connection connection;

    public Students(Connection connection, int age, String name, int grade, int[] marks) {
        super(age, name);
        this.grade = grade;
        this.marks = marks;
        this.connection = connection;
    }

    public void performOperations() {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();

                String createTable = "CREATE TABLE IF NOT EXISTS students (" +
                        "id INTEGER PRIMARY KEY, " +
                        "name TEXT, " +
                        "age INTEGER, " +
                        "grade INTEGER, " +
                        "mark1 INTEGER, " +
                        "mark2 INTEGER, " +
                        "mark3 INTEGER, " +
                        "mark4 INTEGER, " +
                        "mark5 INTEGER, " +
                        "average FLOAT, " +
                        "highestMark INTEGER, " +
                        "lowestMark INTEGER, " +
                        "standardDeviation FLOAT, " +
                        "variance FLOAT)";

                // Create table if not exists
                statement.executeUpdate(createTable);

                Scanner scanner = new Scanner(System.in);

                // Insert data
                System.out.println("Enter student name to insert:");
                String studentName = scanner.nextLine();
                System.out.println("Enter student age:");
                int studentAge = scanner.nextInt();
                System.out.println("Enter student grade:");
                int studentGrade = scanner.nextInt();
                System.out.println("Enter marks for 5 subjects:");
                int[] studentMarks = new int[5];
                for (int i = 0; i < 5; i++) {
                    System.out.print("Mark " + (i + 1) + ": ");
                    studentMarks[i] = scanner.nextInt();
                }

                // Perform data analysis
                float average = calculateAverage(studentMarks);
                int highestMark = findHighestMark(studentMarks);
                int lowestMark = findLowestMark(studentMarks);
                float standardDeviation = calculateStandardDeviation(studentMarks);
                float variance = calculateVariance(studentMarks);

                // Prepare the insert statement
                String insertData = "INSERT INTO students (name, age, grade, mark1, mark2, mark3, mark4, mark5, average, highestMark, lowestMark, standardDeviation, variance) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertData);
                preparedStatement.setString(1, studentName);
                preparedStatement.setInt(2, studentAge);
                preparedStatement.setInt(3, studentGrade);
                for (int i = 0; i < 5; i++) {
                    preparedStatement.setInt(4 + i, studentMarks[i]);
                }
                preparedStatement.setFloat(9, average);
                preparedStatement.setInt(10, highestMark);
                preparedStatement.setInt(11, lowestMark);
                preparedStatement.setFloat(12, standardDeviation);
                preparedStatement.setFloat(13, variance);

                // Execute the statement
                preparedStatement.executeUpdate();

                System.out.println("Data Inserted");

                // Display data
                ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
                System.out.println("ID\tName\tAge\tGrade\tMarks");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    int grade = resultSet.getInt("grade");
                    int[] marks = new int[]{resultSet.getInt("mark1"), resultSet.getInt("mark2"),
                            resultSet.getInt("mark3"), resultSet.getInt("mark4"), resultSet.getInt("mark5")};
                    System.out.printf("%d\t%s\t%d\t%d\t%s\n", id, name, age, grade, Arrays.toString(marks));
                    System.out.print("The Average marks of the Student is : ");
                    System.out.println(calculateAverage(marks));
                    System.out.print("The Highest mark of the Student is : ");
                    System.out.println(findHighestMark(marks));
                    System.out.print("The Lowest mark of the Student is : ");
                    System.out.println(findLowestMark(marks));
                    System.out.print("The S.D of the marks of the Student is : ");
                    System.out.println(calculateStandardDeviation(marks));
                    System.out.print("The variance of the marks of the Student is : ");
                    System.out.println(calculateVariance(marks));

                }

                // Close resources
                resultSet.close();
                statement.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }

    private float calculateAverage(int[] marks) {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return (float) sum / marks.length;
    }

    private int findHighestMark(int[] marks) {
        int highest = marks[0];
        for (int mark : marks) {
            if (mark > highest) {
                highest = mark;
            }
        }
        return highest;
    }

    private int findLowestMark(int[] marks) {
        int lowest = marks[0];
        for (int mark : marks) {
            if (mark < lowest) {
                lowest = mark;
            }
        }
        return lowest;
    }

    private float calculateStandardDeviation(int[] marks) {
        float average = calculateAverage(marks);
        float sum = 0;
        for (int mark : marks) {
            sum += Math.pow(mark - average, 2);
        }
        float variance = sum / marks.length;
        return (float) Math.sqrt(variance);
    }

    private float calculateVariance(int[] marks) {
        float average = calculateAverage(marks);
        float sum = 0;
        for (int mark : marks) {
            sum += Math.pow(mark - average, 2);
        }
        return sum / marks.length;
    }
}
