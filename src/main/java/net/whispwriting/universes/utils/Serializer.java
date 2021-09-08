package net.whispwriting.universes.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerInventoryFile;
import net.whispwriting.universes.utils.sql.SQL;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Serializer {

    private Universes plugin;

    public Serializer(Universes plugin){
        this.plugin = plugin;
    }

    public void serialize(ItemStack[] items, String type, UUID uuid, String name){
        JsonObject json = new JsonObject();
        for (int i=0; i<items.length; i++){
            String result = itemStackToString(items[i]);
            json.addProperty(Integer.toString(i), result);
        }

        PlayerInventoryFile inventoryFile = new PlayerInventoryFile(plugin, uuid.toString(), name, type);
        inventoryFile.get().set("inventory", json.toString());
        inventoryFile.save();
    }

    public ItemStack[] deserialize(UUID uuid, String name, String type){
        PlayerInventoryFile inventoryFile = new PlayerInventoryFile(plugin, uuid.toString(), name, type);
        String inventory = inventoryFile.get().getString("inventory");
        if (inventory != null) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(inventory).getAsJsonObject();
            ItemStack[] items = new ItemStack[json.size()];
            for (int i = 0; i < json.size(); i++) {
                String item = json.get(Integer.toString(i)).getAsString();
                items[i] = stringToItemStack(item);
            }
            return items;
        }else{
            return null;
        }
    }

    public String itemStackToString(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(item);
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    private ItemStack stringToItemStack(String data) {
        try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(data)));) {

            // Read the serialized item
            ItemStack item = (ItemStack) dataInput.readObject();

            return item;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
}
