package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameModeItem extends GUIItem {

    private enum GameModeValue{
        SURVIVAL(0),
        CREATIVE(1),
        SPECTATOR(2),
        ADVENTURE(3);

        private int value = 0;

        GameModeValue(final int newValue){
            value = newValue;
        }

        public int getValue(){
            return value;
        }

    }

    public GameModeItem(GameMode currentSetting){
        Material[] materials = {Material.GRASS_BLOCK, Material.BEDROCK, Material.END_CRYSTAL, Material.TORCH};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to change GameMode."));
        lore1.add(Utils.chat("&2GameMode is currently Survival."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to change GameMode."));
        lore2.add(Utils.chat("&2GameMode is currently Creative."));
        List<String> lore3 = new ArrayList<>();
        lore3.add(Utils.chat("&dClick to change GameMode."));
        lore3.add(Utils.chat("&2GameMode is currently Spectator."));
        List<String> lore4 = new ArrayList<>();
        lore4.add(Utils.chat("&dClick to change GameMode."));
        lore4.add(Utils.chat("&2GameMode is currently Adventure."));

        super.create(Utils.chat("&bGame Mode"), 1, false, GameModeValue.valueOf(currentSetting.name()).getValue(), materials, lore1, lore2, lore3, lore4);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        GameMode mode = universe.gameMode();
        if (mode == GameMode.SURVIVAL){
            universe.setGameMode(GameMode.CREATIVE);
            updateGameMode(GameMode.CREATIVE, universe);
            player.sendMessage(Utils.chat("&2GameMode has been changed to Creative."));
        }else if (mode == GameMode.CREATIVE){
            universe.setGameMode(GameMode.SPECTATOR);
            updateGameMode(GameMode.SPECTATOR, universe);
            player.sendMessage(Utils.chat("&2GameMode has been changed to Spectator."));
        }else if (mode == GameMode.SPECTATOR){
            universe.setGameMode(GameMode.ADVENTURE);
            updateGameMode(GameMode.ADVENTURE, universe);
            player.sendMessage(Utils.chat("&2GameMode has been changed to Adventure."));
        }else{
            universe.setGameMode(GameMode.SURVIVAL);
            updateGameMode(GameMode.SURVIVAL, universe);
            player.sendMessage(Utils.chat("&2GameMode has been changed to Survival."));
        }
        incrementIndex();
    }

    private static void updateGameMode(GameMode mode, Universe universe){
        Collection<Player> players = universe.serverWorld().getPlayers();
        for (Player player : players){
            player.setGameMode(mode);
        }
    }
}
