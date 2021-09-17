package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

import java.io.FileWriter;
import java.io.IOException;

public class EconomyConfig extends AbstractFile{

    public EconomyConfig(Universes plugin) {
        super(plugin, "economy-config.yml", "");
    }

    @Override
    public void writeComments() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("#This file contains all the configuration options for the new economy system. The following\n" +
                    "#options enable or disable the universes economy system, and change currency symbols and names." +
                    "\n" +
                    "use-universes-economy: false" +
                    "\n" +
                    "currency-prefix: \"$\"" +
                    "\n" +
                    "currency-name-singular: \"Dollar\"" +
                    "\n" +
                    "currency-name-plural: \"Dollars\"");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
