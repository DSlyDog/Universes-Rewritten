package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DifficultyItem extends GUIItem {

    private enum DifficultyValue{
        PEACEFUL(0),
        EASY(1),
        NORMAL(2),
        HARD(3);

        private int value = 0;

        DifficultyValue(final int newValue){
            value = newValue;
        }

        public int getValue(){
            return value;
        }

    }

    public DifficultyItem(Difficulty currentSetting){
        Material[] materials = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to change Difficulty."));
        lore1.add(Utils.chat("&2Difficulty is currently Peaceful."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to change Difficulty."));
        lore2.add(Utils.chat("&2Difficulty is currently Easy."));
        List<String> lore3 = new ArrayList<>();
        lore3.add(Utils.chat("&dClick to change Difficulty."));
        lore3.add(Utils.chat("&2Difficulty is currently Normal."));
        List<String> lore4 = new ArrayList<>();
        lore4.add(Utils.chat("&dClick to change GameMode."));
        lore4.add(Utils.chat("&2Difficulty is currently Hard."));

        super.create(Utils.chat("&bDifficulty"), 1, false, DifficultyItem.DifficultyValue.valueOf(currentSetting.name()).getValue(), materials, lore1, lore2, lore3, lore4);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        Difficulty difficulty = universe.getDifficulty();
        if (difficulty == Difficulty.PEACEFUL){
            universe.setDifficulty(Difficulty.EASY);
            player.sendMessage(Utils.chat("&2Difficulty has been changed to Easy."));
        }else if (difficulty == Difficulty.EASY){
            universe.setDifficulty(Difficulty.NORMAL);
            player.sendMessage(Utils.chat("&2Difficulty has been changed to Normal."));
        }else if (difficulty == Difficulty.NORMAL){
            universe.setDifficulty(Difficulty.HARD);
            player.sendMessage(Utils.chat("&2Difficulty has been changed to Hard."));
        }else{
            universe.setDifficulty(Difficulty.PEACEFUL);
            player.sendMessage(Utils.chat("&2Difficulty has been changed to Peaceful."));
        }
        incrementIndex();
    }
}
