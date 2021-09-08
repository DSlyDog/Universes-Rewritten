package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class DatabaseFile extends AbstractFile {
    public DatabaseFile(Universes pl) {
        super(pl, "database-config.yml", "");
    }

    public void createConfig(){
        config.addDefault("remote-database", false);
        config.addDefault("host", "localhost");
        config.addDefault("port", 3306);
        config.addDefault("database", "root");
        config.addDefault("username", "username");
        config.addDefault("password", "password");
    }
}
