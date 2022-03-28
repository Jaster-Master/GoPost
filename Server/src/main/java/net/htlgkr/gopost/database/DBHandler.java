package net.htlgkr.gopost.database;

import net.htlgkr.gopost.data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHandler {
    private static final String DATABASE_URL = "jdbc:mysql://10.109.0.1:16660/GoPostTest";

    private Connection dbConnection;

    public DBHandler() {
        startDBConnection();
    }

    private boolean startDBConnection() {
        if (System.getenv("DB_PASS") == null) return false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(
                    DATABASE_URL, "Schotzgoblin", System.getenv("DB_PASS"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void executeStatementsOnDB(String statement, Object... objects) {
        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(statement);
            for (int i = 0; i < objects.length; i++) {
                prepareStatement.setObject(i + 1, objects[i]);
            }
            prepareStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DBObject> readFromDB(String statement, Object... objects) {
        List<DBObject> results = new ArrayList<>();
        List<DBObject> dbObjects = Arrays.stream(objects).map(DBObject::new).toList();
        List<DBObject> statementValues = valuesOfStatement(dbObjects.toArray(DBObject[]::new));
        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(statement);
            for (int i = 0; i < statementValues.size(); i++) {
                prepareStatement.setObject(i + 1, statementValues.get(i).getObject());
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                for (DBObject object : dbObjects) {
                    String string = object.getString();
                    String[] split = string.split(";");
                    if (split.length == 2) {
                        int column = Integer.parseInt(split[0]);
                        switch (split[1]) {
                            case "BigInt" -> results.add(new DBObject(resultSet.getLong(column)));
                            case "String" -> results.add(new DBObject(resultSet.getString(column)));
                            case "Boolean" -> results.add(new DBObject(resultSet.getBoolean(column)));
                            case "Timestamp" -> results.add(new DBObject(resultSet.getTimestamp(column)));
                            case "Blob" -> results.add(new DBObject(resultSet.getBlob(column)));
                            case "Double" -> results.add(new DBObject(resultSet.getDouble(column)));
                        }
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public User getUserFromId(long userId) {
        String selectedStatement = "SELECT * FROM GoUser WHERE GoUserId = ?";
        List<DBObject> result = readFromDB(selectedStatement, userId, "2;String", "3;String", "4;String", "5;String", "9;Blob");
        return new User(userId, result.get(0).getString(), result.get(1).getString(), result.get(2).getString(), result.get(3).getBlob());
    }

    private List<DBObject> valuesOfStatement(DBObject[] objects) {
        List<DBObject> statementValues = new ArrayList<>();
        for (DBObject object : objects) {
            try {
                String string = object.getString();
                String[] split = string.split(";");
                if (split.length != 2) {
                    statementValues.add(new DBObject(string));
                }
            } catch (ClassCastException e) {
                statementValues.add(object);
            }
        }
        return statementValues;
    }

}
