package net.whispwriting.universes.gui;

import net.whispwriting.universes.Universes;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GUI {

    enum SIZE{
        ONE(9),
        TWO(18),
        THREE(27);

        private int value = 0;

        SIZE(final int newValue){
            value = newValue;
        }

        public int getValue(){
            return value;
        }
    }

    void build();

    void updateItem(int index);

    Inventory get();

    GUIItem getItem(int index);
}
