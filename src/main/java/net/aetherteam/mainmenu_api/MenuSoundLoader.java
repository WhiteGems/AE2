package net.aetherteam.mainmenu_api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ScheduledSound;
import net.minecraft.client.audio.SoundPoolEntry;
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
                registerStreaming(event.manager, "streaming/" + (String)music.get(musicIndex), "/resources/streaming/" + (String)music.get(musicIndex));
            }
        }
    }

    private void registerSound(SoundPoolEntry manager, String name, String path)
    {
        try
        {
            URL filePath = MenuSoundLoader.class.getResource(path);

            if (filePath != null)
            {
                manager.soundUrl.addSound(name, filePath);
            }
            else
            {
                throw new FileNotFoundException();
            }
        }
        catch (Exception ex)
        {
            System.out.println(String.format("Warning: unable to load sound file %s", new Object[] { path }));
        }
    }

    private void registerStreaming(SoundPoolEntry manager, String name, String path)
    {
        File soundFile = new File(ModLoader.getMinecraftInstance().mcDataDir, "resources/" + name);

        if ((soundFile.canRead()) && (soundFile.isFile()))
        {
            ModLoader.getMinecraftInstance().installResource(name, soundFile);
            System.out.println("MainMenuAPI - Registering Music: " + name.replace("streaming/", ""));
        }
        else
        {
            System.err.println("Could not load file: " + soundFile);
        }
    }
}

