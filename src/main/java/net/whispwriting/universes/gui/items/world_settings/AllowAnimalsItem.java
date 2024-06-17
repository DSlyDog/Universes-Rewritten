package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.ArrayList;
import java.util.List;

public class AllowAnimalsItem extends GUIItem {

    public AllowAnimalsItem(boolean currentValue){
        Material[] materials = {Material.WOLF_SPAWN_EGG};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&callowAnimals is currently disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2allowAnimals is currently enabled."));

        super.create(Utils.chat("&bAllow Animals"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        if (universe.isAllowAnimals()){
            universe.allowAnimals(false);
            List<Entity> entities = universe.serverWorld().getEntities();
            for (Entity e : entities){
                if (e instanceof Animals)
                    if (e instanceof Tameable) {
                        if (!((Tameable) e).isTamed())
                            e.remove();
                    }else {
                        e.remove();
                    }
            }
            player.sendMessage(Utils.chat("&cAnimals are no longer allowed in &4" + universe.name() + "."));
        }else{
            universe.allowAnimals(true);
            player.sendMessage(Utils.chat("&2Animals are now allowed in &a" + universe.name() + "."));
        }
        incrementIndex();
    }
}
