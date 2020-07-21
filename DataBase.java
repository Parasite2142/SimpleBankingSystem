package banking;

import java.sql.*;
import java.util.Scanner;

public class DataBase {

    private static DataBase db;
    private final String url;

    private DataBase(String url) {
        this.url = url;
        String table = "CREATE TABLE IF NOT EXISTS card (\n" +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "   number TEXT NOT NULL,\n" +
                "   pin TEXT NOT NULL,\n" +
                "   balance INTEGER DEFAULT 0\n" +
                ");";
        try (Connection connection = DriverManager.getConnection(this.url);
             Statement stmt = connection.createStatement()) {
            stmt.execute(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DataBase getInstance(String url) {
        if (db == null) {
            db = new DataBase(url);
        }
        return db;
    }

    protected void closeAccount(String cn) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection connection = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Account logIntoAccount(String cn, String pin, Scanner sc) {
        if (cn.length() != 16 || pin.length() != 4) {
            System.out.println("Wrong card number or PIN!\n");
            return null;
        }
        String sql = "SELECT number, pin, balance FROM card WHERE (number = ? AND pin = ?)";
        Account account = null;
        try (Connection connection = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cn);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            account = new Account(rs.getString("number"),
                    rs.getInt("balance"), sc);
            if (account.getCard() == null) {
                System.out.println("Wrong card number or PIN!\n");
                account = null;
            } else {
                System.out.println("You have successfully logged in!\n");
            }
        } catch (SQLException throwable) {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
        return account;
    }

    protected void addAccountToBase(String cn, String pin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?, ?)";
        try (Connection connection = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cn);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
            addedPrint(cn, pin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void makeTransfer(String from, String to, int amount) {
        addIncome(from, -amount);
        addIncome(to, amount);
        System.out.println("Success!");
    }

    protected void addIncome(String account, int amount) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setString(2, account);
            pstmt.executeUpdate();
            System.out.println("Income was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean checkAccount(String cn) {
        String sql = "SELECT CASE WHEN EXISTS(SELECT 1 FROM card WHERE number = ?)" +
                "THEN 1 ELSE 0 END";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, cn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.getInt(1) != 1) {
                System.out.println("Such a card does not exist.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addedPrint(String cn, String pin) {
        System.out.println("Your card has been created");
        System.out.println("You card number:");
        System.out.println(cn);
        System.out.println("Your card PIN");
        System.out.println(pin + "\n");
        System.out.println("added");
    }
}
