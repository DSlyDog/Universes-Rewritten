package net.whispwriting.universes.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.sql.SQL;
import net.whispwriting.universes.utils.sql.SQLResult;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializerOld {
    private SQL sql;
    private Universes plugin;

    public SerializerOld(Universes plugin, SQL sql) {
        this.plugin = plugin;
        this.sql = sql;
    }

    public void serialize(ItemStack[] items, String type, UUID uuid, String name) {
        JsonObject json = new JsonObject();

        for(int i = 0; i < items.length; ++i) {
            String result = this.itemStackToString(items[i]);
            json.addProperty(Integer.toString(i), result);
        }

        SQLResult result = this.sql.query("select * from inventory where uuid='" + uuid.toString() + "' and universe='" + name + "' and type='" + type + "'", "query");
        if (!result.isBooleanResult()) {
            ResultSet resultSet = result.sqlResults();

            try {
                if (!resultSet.next()) {
                    this.sql.query("insert into inventory values('" + uuid.toString() + "', '" + name + "', '" + type + "', '" + json + "')", "insert");
                } else {
                    this.sql.query("update inventory set inventory='" + json + "' where uuid='" + uuid.toString() + "' and universe='" + name + "' and type='" + type + "'", "update");
                }
            } catch (SQLException var9) {
                Bukkit.getLogger().log(Level.WARNING, "[Universes] Could not read query result in serializer.");
                var9.printStackTrace();
            }
        }

    }

    public ItemStack[] deserialize(UUID uuid, OfflinePlayer player, String name, String type) {
        SQLResult result = this.sql.query("select * from inventory where uuid='" + uuid.toString() + "' and universe='" + name + "' and type='" + type + "'", "query");
        if (!result.isBooleanResult()) {
            ResultSet resultSet = result.sqlResults();

            try {
                if (!resultSet.next()) {
                    return null;
                } else {
                    JsonObject json = (new JsonParser()).parse(resultSet.getString("inventory")).getAsJsonObject();
                    byte var10 = -1;
                    switch(type.hashCode()) {
                        case 1364760889:
                            if (type.equals("enderchest")) {
                                var10 = 0;
                            }
                        default:
                            ItemStack[] items;
                            switch(var10) {
                                case 0:
                                    items = new ItemStack[27];
                                    break;
                                default:
                                    items = new ItemStack[41];
                            }

                            for(int i = 0; i < json.size(); ++i) {
                                String item = json.get(Integer.toString(i)).getAsString();
                                items[i] = this.stringToItemStack(item);
                            }

                            return items;
                    }
                }
            } catch (SQLException var11) {
                Bukkit.getLogger().log(Level.SEVERE, "[Universes] Sql query failed in deserialize. Please report this to a dev.");
                var11.printStackTrace();
                return null;
            }
        } else {
            Bukkit.getLogger().log(Level.WARNING, "[Universes] SQL query returned a boolean result instead of a result set. This should not be the case and may prevent inventories from loading. Please report this to a dev.");
            return null;
        }
    }

    public JsonObject parseStats(UUID uuid) {
        SQLResult sqlResult = this.sql.query("select stats from playerdata where uuid='" + uuid + "'", "query");
        if (!sqlResult.isBooleanResult()) {
            ResultSet resultSet = sqlResult.sqlResults();

            try {
                if (resultSet.next()) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(resultSet.getString("stats")).getAsJsonObject();
                } else {
                    return null;
                }
            } catch (SQLException var5) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to retrieve player stats from the database.");
                var5.printStackTrace();
                return null;
            } catch (IllegalStateException var6) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void storeStats(JsonObject stats, UUID uuid) {
        this.sql.query("update playerdata set stats='" + stats + "' where uuid='" + uuid + "'", "update");
    }

    public String itemStackToString(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            String var4;
            try {
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                try {
                    dataOutput.writeObject(item);
                    var4 = Base64Coder.encodeLines(outputStream.toByteArray());
                } catch (Throwable var8) {
                    try {
                        dataOutput.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }

                    throw var8;
                }

                dataOutput.close();
            } catch (Throwable var9) {
                try {
                    outputStream.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }

                throw var9;
            }

            outputStream.close();
            return var4;
        } catch (IOException var10) {
            return null;
        }
    }

    private ItemStack stringToItemStack(String data) {
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(data)));

            ItemStack var4;
            try {
                ItemStack item = (ItemStack)dataInput.readObject();
                var4 = item;
            } catch (Throwable var6) {
                try {
                    dataInput.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            dataInput.close();
            return var4;
        } catch (ClassNotFoundException | IOException var7) {
            return null;
        }
    }
}
