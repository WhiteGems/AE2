package net.aetherteam.mainmenu_api;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.MathHelper;
import paulscode.sound.SoundSystem;

public class JukeboxPlayer
{
    public SoundManager soundManager = MainMenuAPI.proxy.getClient().sndManager;
    private int musicInterval;
    public List jukeboxMusic;
    public boolean defaultMusic = true;
    public String musicFileName = null;

    public boolean songIsPlaying = false;

    public void process()
    {
        Minecraft.getMinecraft();
        File streaming = new File(Minecraft.getMinecraftDir() + "/resources/streaming/");

        if (streaming.exists())
        {
            this.jukeboxMusic = listMusic(streaming, false);
        }
    }

    public void start()
    {
        MenuBaseConfig.loadConfig();

        if (MenuBaseConfig.hasStartedMusic != true)
        {
            if (getMusicFileName() == null) ;
        }

        MenuBaseConfig.hasStartedMusic = true;
        MenuBaseConfig.setProperty("hasStartedMusic", "true");

        process();
    }

    public void run()
    {
        if ((this.songIsPlaying) && (!MenuBaseConfig.muteMusic))
        {
            this.musicInterval += 1;
        } else this.musicInterval = 0;

        if (this.musicInterval > 100)
        {
            this.songIsPlaying = false;
            this.musicInterval = 0;
        }

        if (this.jukeboxMusic != null)
        {
            if (MenuBaseConfig.musicIndex > this.jukeboxMusic.size() - 1)
            {
                MenuBaseConfig.musicIndex = 0;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }

            if (MenuBaseConfig.musicIndex < 0)
            {
                MenuBaseConfig.musicIndex = this.jukeboxMusic.size() - 1;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }
        }

        if (MenuBaseConfig.muteMusic)
        {
            muteMusic();
        }

        playMenuMusic();
    }

    public int getIndexFromName(String musicName)
    {
        process();

        if (this.jukeboxMusic != null)
        {
            for (int count = 0; count < this.jukeboxMusic.size(); count++)
            {
                if (((String) this.jukeboxMusic.get(count)).equalsIgnoreCase(musicName))
                {
                    return count;
                }
            }
        }

        return MenuBaseConfig.musicIndex;
    }

    public List listMusic(File folder, boolean extension)
    {
        if (folder.exists())
        {
            List files = new ArrayList();

            for (File fileEntry : folder.listFiles())
            {
                if (fileEntry.isDirectory())
                {
                    listMusic(fileEntry, extension);
                } else
                {
                    String nameExtension = fileEntry.getName();
                    String nameNoExtension = fileEntry.getName().replaceFirst("[.][^.]+$", "");

                    if (!files.contains(extension ? nameExtension : nameNoExtension))
                    {
                        files.add(extension ? nameExtension : nameNoExtension);
                    }
                }
            }
            return files;
        }
        return null;
    }

    private void setToNextSong()
    {
        if (this.jukeboxMusic != null)
        {
            if (!MenuBaseConfig.musicSet)
            {
                MenuBaseConfig.musicSet = true;
                MenuBaseConfig.setProperty("musicSet", "true");

                if (MenuBaseConfig.musicIndex > this.jukeboxMusic.size() - 2)
                {
                    MenuBaseConfig.musicIndex = 0;
                    MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                } else
                {
                    MenuBaseConfig.musicIndex += 1;
                    MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                }
            }
        }
    }

    public String getCurrentSongName()
    {
        String name = getCurrentSong();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getCurrentSong()
    {
        if (this.jukeboxMusic != null)
        {
            return (String) this.jukeboxMusic.get(this.defaultMusic ? getIndexFromName(getMusicFileName()) : MathHelper.clamp_int(MenuBaseConfig.musicIndex, 0, this.jukeboxMusic.size() - 1));
        }
        return "";
    }

    public boolean isMusicPlaying()
    {
        return (SoundManager.sndSystem != null) && (SoundManager.sndSystem.playing("streaming"));
    }

    public void muteMusic()
    {
        if ((this.soundManager != null) && (SoundManager.sndSystem != null))
        {
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void toggleMute()
    {
        MenuBaseConfig.muteMusic = !MenuBaseConfig.muteMusic;
        MenuBaseConfig.setProperty("muteMusic", String.valueOf(MenuBaseConfig.muteMusic));

        if (MenuBaseConfig.muteMusic)
        {
            muteMusic();
        } else playMenuMusic();
    }

    public void toggleLoop()
    {
        MenuBaseConfig.loopMusic = !MenuBaseConfig.loopMusic;
        MenuBaseConfig.setProperty("loopMusic", String.valueOf(MenuBaseConfig.loopMusic));
    }

    private void playMusicFile(String musicFile)
    {
        if ((!MenuBaseConfig.muteMusic) && (SoundManager.sndSystem != null) && (!SoundManager.sndSystem.playing("streaming")))
        {
            System.out.println("Playing Music File: " + musicFile);

            float x = (float) MenuBaseConfig.playerPosX;
            float y = (float) MenuBaseConfig.playerPosY;
            float z = (float) MenuBaseConfig.playerPosZ;

            this.soundManager.playStreaming(musicFile, x != 0.0F ? x : 0.0F, y != 0.0F ? y : 0.0F, z != 0.0F ? z : 0.0F);
        }
    }

    public String getMusicFileName()
    {
        return this.musicFileName;
    }

    public JukeboxPlayer setMusicFileName(String name)
    {
        this.musicFileName = name;

        return this;
    }

    public void playMenuMusic()
    {
        if ((SoundManager.sndSystem != null) &&
                ((!MenuBaseConfig.hasPlayedMusic) || ((!SoundManager.sndSystem.playing("streaming")) && (MenuBaseConfig.hasPlayedMusic))) && (MenuBaseConfig.hasStartedMusic))
        {
            if ((!MenuBaseConfig.muteMusic) && (!SoundManager.sndSystem.playing("streaming")) && (!this.songIsPlaying))
            {
                this.songIsPlaying = true;

                if (!MenuBaseConfig.loopMusic)
                {
                    setToNextSong();
                }

                playMusicFile(getCurrentSong());
            }

            MenuBaseConfig.hasPlayedMusic = true;
            MenuBaseConfig.setProperty("hasPlayedMusic", "true");
        }

        MenuBaseConfig.musicSet = false;
        MenuBaseConfig.setProperty("musicSet", "false");
    }
}