package net.whispwriting.universes.utils;

public class WorldLoadEventHelper {

    private static WorldLoadEventHelper instance;

    private WorldLoadEventHelper(){}

    private boolean createCommandExecuted = false;
    private boolean convertCommandExecuted = false;

    public boolean isCreateCommandExecuted(){
        return createCommandExecuted;
    }

    public void setCreateCommandExecuted(boolean value){
        createCommandExecuted = value;
    }

    public boolean isConvertCommandExecuted() {
        return convertCommandExecuted;
    }

    public void setConvertCommandExecuted(boolean value){
        convertCommandExecuted = value;
    }

    public static WorldLoadEventHelper getInstance(){
        if (instance == null){
            instance = new WorldLoadEventHelper();
        }

        return instance;
    }
}
