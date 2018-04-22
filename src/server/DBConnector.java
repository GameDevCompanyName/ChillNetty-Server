package server;

import java.sql.*;
import java.util.Random;

/*
Класс для работы с БД.
 */

public class DBConnector {

    private static String className = "DBConnector";
    private static Connection connection = initConnection();

    //Установка соединения с бд
    private static Connection initConnection() {
        Logger.log("Инициализирую Connection", className);
        String url = "jdbc:sqlite:ChillChat.db";
        Connection connection = null;

        try {
            Logger.log("DriverManager...", className);
            connection = DriverManager.getConnection(url);
            Logger.log("DriverManager CONNECTED", className);
        } catch (SQLException e) {
            Logger.logError("DriverManager NOT CONNECTED", className);
            Logger.logError(e.toString(), className);
        }

        checkIfTableExists();

        return connection;

    }

    //Проверка логина и пароля
    public static boolean checkLoginAttempt(String login, String password) {
        Logger.log("Проверяю правильносить пароля для юзера: " + login, className);
        String sql = "SELECT login, password FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String dbPassword = rs.getString("password");
                if (password.equals(dbPassword)) {
                    Logger.log("Пароль правильный для юзера: " + login, className);
                    return true;
                } else {
                    Logger.log("Пароль неверный для юзера: " + login, className);
                    return false;
                }
            }
        } catch (SQLException e) {
            Logger.logError("Что-то пошло не так при проверке пароля для: " + login, className);
            Logger.logError(e.toString(), className);
        }
        return false;
    }

    //Проверка наличия таблицы пользователей
    private static void checkIfTableExists() {

        Logger.log("Проверяю наличие таблицы", className);

        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	login VARCHAR(20) NOT NULL UNIQUE,\n"
                + "	password VARCHAR(20) NOT NULL,\n"
                + "	color VARCHAR(6) NOT NULL,\n"
                + " role VARCHAR(20) NOT NULL,\n"
                + " regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + ");";

        try (Statement stmt = connection.createStatement()) {
            Logger.log("Statement execute...", className);
            stmt.execute(sql);
            Logger.log("Statement executed", className);
            Logger.log("Таблица существует и работает", className);
        } catch (SQLException e) {
            Logger.logError("Проблема доступа к таблице", className);
            Logger.logError(e.toString(), className);
        }
    }

    //Добавление нового пользователя
    public static void insertNewUser(String login, String password) {

        String sql = "INSERT INTO Users ("
                + "login,"
                + "password,"
                + "color,"
                + "role) VALUES(?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int colorCode = 1 + (new Random().nextInt(7));
            String color = Integer.toString(colorCode);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, color);
            pstmt.setString(4, "user");
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить пароль юзера
    public void updateUserPassword(String login, String password) {

        String sql = "UPDATE Users SET password = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, password);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить роль юзера
    public void updateUserRole(String login, String role) {

        String sql = "UPDATE Users SET role = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, role);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            Broadcaster.changeUserRole(login, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить цвет юзера
    public void updateUserColor(String login, String color) {

        String sql = "UPDATE Users SET color = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, color);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            Broadcaster.changeUserColor(login, color);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Проверить наличие юзера
    public static boolean searchForUser(String login) {
        String sql = "SELECT * FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                return false;
            }
            else
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Получить цвет пользователя
    public static String getUserColor(String login){
        String sql = "SELECT color FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()){
                return "false";
            }
            return rs.getString("color");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "false";
    }

    //Получить роль пользователя
    public static String getUserRole(String login){
        String sql = "SELECT role FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()){
                return "false";
            }
            return rs.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "false";
    }
}


