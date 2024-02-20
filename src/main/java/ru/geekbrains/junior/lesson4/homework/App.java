package ru.geekbrains.junior.lesson4.homework;

import ru.geekbrains.junior.lesson4.models.Course;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class App {
    private final static Random random = new Random();
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3307/";
        String user = "root";
        String password = "Password";

        // Подключение к базе данных
        Connection connection = DriverManager.getConnection(url, user, password);

        // Создание базы данных
        createDatabase(connection);
        System.out.println("Database created successfully");

        // Использование базы данных
        useDatabase(connection);
        System.out.println("Use database successfully");

        // Создание таблицы
        createTable(connection);
        System.out.println("Create table successfully");

        // Вставка данных
        int count = random.nextInt(5, 11);
        for (int i = 0; i < count; i++)
            insertData(connection, Course.create());
        System.out.println("Insert data successfully");

        // Чтение данных
        Collection<Course> courses = readData(connection);
        for (var course: courses)
            System.out.println(course);
        System.out.println("Read data successfully");

        // Обновление данных
        for (var course: courses) {
            course.updateTitle();
            course.updateDuration();
            updateData(connection, course);
        }
        System.out.println("Update data successfully");

        // Удаление данных
        //for (var course: courses)
        //    deleteData(connection, course.getId());
        //System.out.println("Delete data successfully");

        // Закрытие соединения
        connection.close();
        System.out.println("Database connection close successfully");
    }

    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseSQL =  "CREATE DATABASE IF NOT EXISTS SchoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
            statement.execute();
        }
    }

    private static void useDatabase(Connection connection) throws SQLException {
        String useDatabaseSQL =  "USE SchoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(useDatabaseSQL)) {
            statement.execute();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Courses  (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration INT);";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    /**
     * Добавление данных в таблицу students
     * @param connection Соединение с БД
     * @param course курс
     * @throws SQLException Исключение при выполнении запроса
     */
    private static void insertData(Connection connection, Course course) throws SQLException {
        String insertDataSQL = "INSERT INTO Courses (title, duration) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.executeUpdate();
        }
    }

    /**
     * Чтение данных из таблицы students
     * @param connection Соединение с БД
     * @return Коллекция курсов
     * @throws SQLException Исключение при выполнении запроса
     */
    private static Collection<Course> readData(Connection connection) throws SQLException {
        ArrayList<Course> courseList = new ArrayList<>();
        String readDataSQL = "SELECT * FROM Courses;";
        try (PreparedStatement statement = connection.prepareStatement(readDataSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int duration = resultSet.getInt("duration");
                courseList.add(new Course(id, title, duration));
            }
            return courseList;
        }
    }

    /**
     * Обновление данных в таблице students по идентификатору
     * @param connection Соединение с БД
     * @param course курс
     * @throws SQLException Исключение при выполнении запроса
     */
    private static void updateData(Connection connection, Course course) throws SQLException {
        String updateDataSQL = "UPDATE Courses SET title=?, duration=? WHERE id=?;";
        try (PreparedStatement statement = connection.prepareStatement(updateDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Удаление записи из таблицы students по идентификатору
     * @param connection Соединение с БД
     * @param id Идентификатор записи
     * @throws SQLException Исключение при выполнении запроса
     */
    private static void deleteData(Connection connection, int id) throws SQLException {
        String deleteDataSQL = "DELETE FROM Courses WHERE id=?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteDataSQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
