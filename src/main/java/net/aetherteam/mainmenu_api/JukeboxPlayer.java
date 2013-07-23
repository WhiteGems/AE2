package net.aetherteam.mainmenu_api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.MathHelper;

public class JukeboxPlayer
{
    public SoundManager soundManager;
    private int musicInterval;
    public List jukeboxMusic;
    public boolean defaultMusic;
    public String musicFileName;
    public boolean songIsPlaying;

    public JukeboxPlayer()
    {
        this.soundManager = MainMenuAPI.proxy.getClient().sndManager;
        this.defaultMusic = true;
        this.musicFileName = null;
        this.songIsPlaying = false;
    }

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

        if (!MenuBaseConfig.hasStartedMusic && this.getMusicFileName() != null)
        {
            ;
        }

        MenuBaseConfig.hasStartedMusic = true;
        MenuBaseConfig.setProperty("hasStartedMusic", "true");
        this.process();
    }

    public void run()
    {
        if (this.songIsPlaying && !MenuBaseConfig.muteMusic)
        {
            ++this.musicInterval;
        }
        else
        {
            this.musicInterval = 0;
        }

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
            this.muteMusic();
        }

        this.playMenuMusic();
    }

    public int getIndexFromName(String var1)
    {
        this.process();

        if (this.jukeboxMusic != null)
        {
            for (int var2 = 0; var2 < this.jukeboxMusic.size(); ++var2)
            {
                if (((String)this.jukeboxMusic.get(var2)).equalsIgnoreCase(var1))
                {
                    return var2;
                }
            }
        }

        return MenuBaseConfig.musicIndex;
    }

    public List listMusic(File var1, boolean var2)
    {
        if (var1.exists())
        {
            ArrayList var3 = new ArrayList();
            File[] var4 = var1.listFiles();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                File var7 = var4[var6];

                if (var7.isDirectory())
                {
                    this.listMusic(var7, var2);
                }
                else
                {
                    String var8 = var7.getName();
                    String var9 = var7.getName().replaceFirst("[.][^.]+$", "");

                    if (!var3.contains(var2 ? var8 : var9))
                    {
                        var3.add(var2 ? var8 : var9);
                    }
                }
            }

            return var3;
        }
        else
        {
            return null;
        }
    }

    private void setToNextSong()
    {
        if (this.jukeboxMusic != null && !MenuBaseConfig.musicSet)
        {
            MenuBaseConfig.musicSet = true;
            MenuBaseConfig.setProperty("musicSet", "true");

            if (MenuBaseConfig.musicIndex > this.jukeboxMusic.size() - 2)
            {
                MenuBaseConfig.musicIndex = 0;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }
            else
            {
                ++MenuBaseConfig.musicIndex;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }
        }
    }

    public String getCurrentSongName()
    {
        String var1 = this.getCurrentSong();
        return var1.isEmpty() ? "" : var1.substring(0, 1).toUpperCase() + var1.substring(1);
    }

    private String getCurrentSong()
    {
        return this.jukeboxMusic != null ? (String)this.jukeboxMusic.get(this.defaultMusic ? this.getIndexFromName(this.getMusicFileName()) : MathHelper.clamp_int(MenuBaseConfig.musicIndex, 0, this.jukeboxMusic.size() - 1)) : "";
    }

    public boolean isMusicPlaying()
    {
        SoundManager var10000 = this.soundManager;
        boolean var1;

        if (SoundManager.sndSystem != null)
        {
            var10000 = this.soundManager;

            if (SoundManager.sndSystem.playing("streaming"))
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public void muteMusic()
    {
        if (this.soundManager != null)
        {
            SoundManager var10000 = this.soundManager;

            if (SoundManager.sndSystem != null)
            {
                var10000 = this.soundManager;
                SoundManager.sndSystem.stop("streaming");
            }
        }
    }

    public void toggleMute()
    {
        MenuBaseConfig.muteMusic = !MenuBaseConfig.muteMusic;
        MenuBaseConfig.setProperty("muteMusic", String.valueOf(MenuBaseConfig.muteMusic));

        if (MenuBaseConfig.muteMusic)
        {
            this.muteMusic();
        }
        else
        {
            this.playMenuMusic();
        }
    }

    public void toggleLoop()
    {
        MenuBaseConfig.loopMusic = !MenuBaseConfig.loopMusic;
        MenuBaseConfig.setProperty("loopMusic", String.valueOf(MenuBaseConfig.loopMusic));
    }

    private void playMusicFile(String var1)
    {
        if (!MenuBaseConfig.muteMusic)
        {
            SoundManager var10000 = this.soundManager;

            if (SoundManager.sndSystem != null)
            {
                var10000 = this.soundManager;

                if (!SoundManager.sndSystem.playing("streaming"))
                {
                    System.out.println("Playing Music File: " + var1);
                    float var2 = (float)MenuBaseConfig.playerPosX;
                    float var3 = (float)MenuBaseConfig.playerPosY;
                    float var4 = (float)MenuBaseConfig.playerPosZ;
                    this.soundManager.playStreaming(var1, var2 != 0.0F ? var2 : 0.0F, var3 != 0.0F ? var3 : 0.0F, var4 != 0.0F ? var4 : 0.0F);
                }
            }
        }
    }

    public String getMusicFileName()
    {
        return this.musicFileName;
    }

    public JukeboxPlayer setMusicFileName(String var1)
    {
        this.musicFileName = var1;
        return this;
    }

    public void playMenuMusic()
    {
        SoundManager var10000 = MainMenuAPI.proxy.getClient().sndManager;

        if (SoundManager.sndSystem != null)
        {
            label29:
            {
                if (MenuBaseConfig.hasPlayedMusic)
                {
                    var10000 = this.soundManager;

                    if (SoundManager.sndSystem.playing("streaming") || !MenuBaseConfig.hasPlayedMusic)
                    {
                        break label29;
                    }
                }

                if (MenuBaseConfig.hasStartedMusic)
                {
                    if (!MenuBaseConfig.muteMusic)
                    {
                        var10000 = this.soundManager;

                        if (!SoundManager.sndSystem.playing("streaming") && !this.songIsPlaying)
                        {
                            this.songIsPlaying = true;

                            if (!MenuBaseConfig.loopMusic)
                            {
                                this.setToNextSong();
                            }

                            this.playMusicFile(this.getCurrentSong());
                        }
                    }

                    MenuBaseConfig.hasPlayedMusic = true;
                    MenuBaseConfig.setProperty("hasPlayedMusic", "true");
                }
            }
        }

        MenuBaseConfig.musicSet = false;
        MenuBaseConfig.setProperty("musicSet", "false");
    }
}
