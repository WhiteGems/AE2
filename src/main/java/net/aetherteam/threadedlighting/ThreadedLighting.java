package net.aetherteam.threadedlighting;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import java.util.Arrays;

public class ThreadedLighting extends DummyModContainer
{
    public ThreadedLighting()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "threadedlighting";
        meta.name = "Threaded Lighting";
        meta.version = "0.1";
        meta.credits = "Jaryt23";
        meta.authorList = Arrays.asList(new String[] {"AetherTeam; cafaxo"});
        meta.description = "";
        meta.url = "";
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";
    }

    public boolean registerBus(EventBus bus, LoadController controller)
    {
        return true;
    }
}
