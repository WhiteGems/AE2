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
    public void onSound(SoundLoadEvent event)
    {
        JukeboxPlayer jukebox = new JukeboxPlayer();
        Minecraft.getMinecraft();
        File streaming = new File(Minecraft.getMinecraftDir() + "/resources/streaming/");
        List music = null;

        if (streaming.exists())
        {
            music = jukebox.listMusic(streaming, true);
        }

        if (music != null)
        {
            for (int musicIndex = 0; musicIndex < music.size(); musicIndex++)
            {
                registerStreaming(event.manager, "streaming/" + music.get(musicIndex), "/resources/streaming/" + music.get(musicIndex));
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

    private void registerStreaming(SoundManager manager, String var2, String path)
    {
        File soundFile = new File(ModLoader.getMinecraftInstance().mcDataDir, "resources/" + var2);

        if (soundFile.canRead() && soundFile.isFile())
        {
            ModLoader.getMinecraftInstance().installResource(var2, soundFile);
            System.out.println("MainMenuAPI - Registering Music: " + var2.replace("streaming/", ""));
        }
        else
        {
            System.err.println("Could not load file: " + soundFile);
        }
    }
}
