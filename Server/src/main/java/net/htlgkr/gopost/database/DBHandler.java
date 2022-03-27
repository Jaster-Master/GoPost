package net.htlgkr.gopost.database;

import com.mysql.cj.jdbc.Blob;
import com.mysql.cj.result.Field;
import net.htlgkr.gopost.util.Encrypt;

import java.beans.XMLEncoder;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHandler {
    private static final String DATABASE_URL ="jdbc:mysql://10.109.0.1:16660/GoPostTest";

    private Connection dbConnection;

    public DBHandler() {
        startDBConnection();
    }

    private boolean startDBConnection() {
        if(System.getenv("DB_PASS")==null)return false;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection= DriverManager.getConnection(
                    DATABASE_URL,"Schotzgoblin",System.getenv("DB_PASS"));
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void executeStatementsOnDB(String statement,Object ... objects) {
        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(statement);
            for (int i = 0; i < objects.length; i++) {
                prepareStatement.setObject(i+1,objects[i]);
            }
            prepareStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> readFromDB(String statement, String ... objects) {
        List<Object> results = new ArrayList<>();
        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(statement);
            for (int i = 0; i < objects.length; i++) {
                //prepareStatement.setObject(i+1,objects[i]);
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next())
            {
                for (String object : objects) {
                    String[] split = object.split(";");
                    int column = Integer.parseInt(split[0]);
                    switch (split[1]){
                        case "BigInt":
                            results.add(resultSet.getInt(column));
                            break;
                        case "String":
                            results.add(resultSet.getString(column));
                            break;
                        case "Boolean":
                            results.add(resultSet.getBoolean(column));
                            break;
                        case "Timestamp":
                            results.add(resultSet.getTimestamp(column));
                            break;
                        case "Blob":
                            results.add(resultSet.getBlob(column));
                            break;
                        case "Double":
                            results.add(resultSet.getDouble(column));
                            break;
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

}
