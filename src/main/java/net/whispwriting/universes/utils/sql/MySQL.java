package net.whispwriting.universes.utils.sql;

import net.whispwriting.universes.files.DatabaseFile;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQL extends SQL {

    private DatabaseFile databaseConfig;
    private String host, database, username, password;
    private int port;

    public MySQL(DatabaseFile databaseConfig){
        this.databaseConfig = databaseConfig;
        init();
    }

    @Override
    public void create(TableType type) {
        connect();
        switch (type){
            case PLAYERDATA:
                if (!tableExists(type))
                    query("create table playerdata (name VARCHAR(100), uuid VARCHAR(300), " +
                            "stats JSON, previousLocations JSON, economy JSON, constraint player_pk PRIMARY KEY (uuid))", "create");
                break;
            case UNIVERSE:
                if (!tableExists(type))
                    query("create table universe (name VARCHAR(100), environment VARCHAR(50), " +
                            "difficulty VARCHAR(50), pvp BOOLEAN, allowMonsters BOOLEAN, allowAnimals BOOLEAN, " +
                            "allowFlight BOOLEAN, gameMode VARCHAR(50), maxPlayers INT, spawn JSON, respawnWorld " +
                            "VARCHAR(100), " + "blockedCommands LONGTEXT, " +
                            "constraint world_pk PRIMARY KEY (name))", "create");
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
    void connect() {
        try{
            if (connection == null || connection.isClosed() || !connection.isValid(200)) {
                Class.forName("com.mysql.jdbc.Driver");
                super.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port
                        + "/" + database, username, password);
                Bukkit.getLogger().log(Level.INFO, "[Universes] Connection successful");
            }
        }catch(ClassNotFoundException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not establish connection to remote database");
            e.printStackTrace();
        }catch(SQLException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not establish connection to remote database");
            e.printStackTrace();
        }
    }

    @Override
    void init() {
        this.host = databaseConfig.get().getString("host");
        this.port = databaseConfig.get().getInt("port");
        this.database = databaseConfig.get().getString("database");
        this.username = databaseConfig.get().getString("username");
        this.password = databaseConfig.get().getString("password");

        try{
            synchronized (this){
                if (connection != null && !connection.isClosed()){
                    return;
                }
                Bukkit.getLogger().log(Level.INFO, "[Universes] Connecting to remote database");
                connect();
            }
        }catch(SQLException e){
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
}
