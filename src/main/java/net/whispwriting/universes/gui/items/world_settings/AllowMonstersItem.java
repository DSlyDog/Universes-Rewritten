package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.List;

public class AllowMonstersItem extends GUIItem {

    public AllowMonstersItem(boolean currentValue){
        Material[] materials = {Material.ZOMBIE_SPAWN_EGG};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&callowMonsters is currently disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2allowMonsters is currently enabled."));

        super.create(Utils.chat("&bAllow Monsters"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        if (universe.isAllowMonsters()){
            universe.allowMonsters(false);
            List<Entity> entities = universe.serverWorld().getEntities();
            for (Entity e : entities){
                if (e instanceof Monster || e instanceof Slime){
                    e.remove();
                }
            }
            player.sendMessage(Utils.chat("&cMonsters are no longer allowed in &4" + universe.serverWorld().getName()));
        }else{
            universe.allowMonsters(true);
            player.sendMessage(Utils.chat("&2Monsters are now allowed in &a" + universe.serverWorld().getName()));
        }
        incrementIndex();
    }
}
