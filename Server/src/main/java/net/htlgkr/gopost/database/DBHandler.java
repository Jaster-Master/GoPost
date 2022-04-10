package net.htlgkr.gopost.database;

import net.htlgkr.gopost.data.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        try {
            dbConnection = DriverManager.getConnection(DATABASE_URL, "Schotzgoblin", readPassword());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String readPassword() {
        try {
            return new BufferedReader(new FileReader("DB_P.lock")).readLine();
        } catch (IOException e) {
            return null;
        }
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
        List<DBObject> returnValues = subtractionOfObjectsAndStatements(dbObjects, statementValues);
        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(statement);
            for (int i = 0; i < statementValues.size(); i++) {
                prepareStatement.setObject(i + 1, statementValues.get(i).getObject());
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                for (DBObject object : returnValues) {
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
        String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserProfilePicture FROM GoUser WHERE GoUserId = ?";
        List<DBObject> result = readFromDB(selectUserStatement, userId, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;Blob");
        return new User(result.get(0).getLong(), result.get(1).getString(), result.get(2).getString(), result.get(3).getString(), result.get(4).getString(), result.get(5).getBlob());
    }

    public User getUserFromName(String userName) {
        String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserProfilePicture FROM GoUser WHERE GoUserName = ?";
        List<DBObject> result = readFromDB(selectUserStatement, userName, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;Blob");
        return new User(result.get(0).getLong(), result.get(1).getString(), result.get(2).getString(), result.get(3).getString(), result.get(4).getString(), result.get(5).getBlob());
    }

    private List<DBObject> subtractionOfObjectsAndStatements(List<DBObject> dbObjects, List<DBObject> statementValues) {
        List<DBObject> results = new ArrayList<>();
        for (int i = statementValues.size(); i < dbObjects.size(); i++) {
            results.add(dbObjects.get(i));
        }
        return results;
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
