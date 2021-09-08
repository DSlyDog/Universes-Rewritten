package net.whispwriting.universes.utils.sql;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Map;
import java.util.logging.Level;

public abstract class SQL {

    Connection connection;

    abstract void connect();
    abstract void init();
    public abstract void create(TableType type);
    public abstract void close();

    public SQLResult query(String query, String queryType){
        connect();
        try {
            if (connection != null && !connection.isClosed()) {
                PreparedStatement statement;
                switch (queryType) {
                    case "create":
                        statement = connection.prepareStatement(query);
                        boolean result = statement.execute();
                        return new SQLResult(result);
                    case "query":
                        statement = connection.prepareStatement(query);
                        ResultSet results = statement.executeQuery();
                        return new SQLResult(results);
                    case "insert":
                        statement = connection.prepareStatement(query);
                        result = statement.execute();
                        return new SQLResult(result);
                    case "update":
                        statement = connection.prepareStatement(query);
                        int intResult = statement.executeUpdate();
                        return new SQLResult(intResult);
                    default:
                        Bukkit.getLogger().log(Level.SEVERE, "Incompatible query type given. An operation failed." +
                                " Please report this to a dev, as this was likely caused by a mistake in the code.");
                        return null;
                }
            }else{
                Bukkit.getLogger().log(Level.SEVERE, "Failed to establish a connection to the database for a " +
                        "query. Is the database server down?");
                return null;
            }
        }catch(SQLException e){
            System.err.println("Something went wrong with the SQL");
            e.printStackTrace();
            return null;
        }
    }

    boolean tableExists(TableType type){
        try {
            switch(type) {
                case UNIVERSE:
                    DatabaseMetaData dbm = connection.getMetaData();
                    ResultSet tables = dbm.getTables(null, null, "universe", null);
                    if (tables.next())
                        return true;
                    else
                        return false;
                case INVENTORY:
                    dbm = connection.getMetaData();
                    tables = dbm.getTables(null, null, "inventory", null);
                    if (tables.next())
                        return true;
                    else
                        return false;
                case PLAYERDATA:
                    dbm = connection.getMetaData();
                    tables = dbm.getTables(null, null, "playerdata", null);
                    if (tables.next())
                        return true;
                    else
                        return false;
                default:
                    return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
