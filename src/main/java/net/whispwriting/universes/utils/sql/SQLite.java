package net.whispwriting.universes.utils.sql;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.LocalDatabase;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLite extends SQL {

    private LocalDatabase localDatabase;

    public SQLite(Universes plugin){
        this.localDatabase = new LocalDatabase(plugin);
        init();
    }

    @Override
    public void create(TableType type) {
        closeConnection();
        connect();
        switch (type){
            case PLAYERDATA:
                if (!tableExists(type))
                    query("create table playerdata (name VARCHAR(100), uuid VARCHAR(300), " +
                            "stats JSON, previousLocations JSON, economy JSON)", "create");
                break;
            case UNIVERSE:
                if (!tableExists(type))
                    query("create table universe (name VARCHAR(100), environment VARCHAR(50), " +
                            "difficulty VARCHAR(50), pvp BOOLEAN, allowMonsters BOOLEAN, allowAnimals BOOLEAN, " +
                            "allowFlight BOOLEAN, gameMode VARCHAR(50), maxPlayers INT, spawn JSON, respawnWorld " +
                            "VARCHAR(100), blockedCommands LONGTEXT)", "create");
                break;
            case INVENTORY:
                if (!tableExists(type))
                    query("create table inventory (uuid VARCHAR(300), universe VARCHAR(100), type VARCHAR(50), " +
                                    "inventory JSON)",
                            "create");
                break;
        }
    }

    @Override
    public SQLResult query(String query, String type){
        closeConnection();
        return super.query(query, type);
    }

    @Override
    void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                super.connection = DriverManager.getConnection("jdbc:sqlite:" + localDatabase.get());
            }
        }catch(ClassNotFoundException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not establish connection to local database");
            e.printStackTrace();
        }catch(SQLException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not establish connection to local database");
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Bukkit.getLogger().log(Level.INFO, "[Universes] Using local database");
                connect();
            }
        }catch(SQLException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not establish connection to local database");
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeConnection(){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
