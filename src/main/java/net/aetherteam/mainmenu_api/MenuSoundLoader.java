package net.aetherteam.mainmenu_api;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class MenuSoundLoader
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent var1)
    {
        JukeboxPlayer var2 = new JukeboxPlayer();
        File var10000 = new File;
        StringBuilder var10002 = new StringBuilder();
        Minecraft.getMinecraft();
        var10000.<init>(var10002.append(Minecraft.getMinecraftDir()).append("/resources/streaming/").toString());
        File var3 = var10000;
        List var4 = null;

        if (var3.exists())
        {
            var4 = var2.listMusic(var3, true);
        }

        if (var4 != null)
        {
            for (int var5 = 0; var5 < var4.size(); ++var5)
            {
                this.registerStreaming(var1.manager, "streaming/" + (String)var4.get(var5), "/resources/streaming/" + (String)var4.get(var5));
            }
        }
    }

    private void registerSound(SoundManager var1, String var2, String var3)
    {
        try
        {
            URL var4 = MenuSoundLoader.class.getResource(var3);

            if (var4 == null)
            {
                throw new FileNotFoundException();
            }

            var1.soundPoolSounds.addSound(var2, var4);
        }
        catch (Exception var5)
        {
            System.out.println(String.format("Warning: unable to load sound file %s", new Object[] {var3}));
        }
    }

    private void registerStreaming(SoundManager var1, String var2, String var3)
    {
        File var4 = new File(ModLoader.getMinecraftInstance().mcDataDir, "resources/" + var2);

        if (var4.canRead() && var4.isFile())
        {
            ModLoader.getMinecraftInstance().installResource(var2, var4);
            System.out.println("MainMenuAPI - Registering Music: " + var2.replace("streaming/", ""));
        }
        else
        {
            System.err.println("Could not load file: " + var4);
        }
    }
}
