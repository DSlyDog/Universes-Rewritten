package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class LocalDatabase extends AbstractFileSQL{

    public LocalDatabase(Universes pl){
        super(pl, "universes.db", "");
    }

}
