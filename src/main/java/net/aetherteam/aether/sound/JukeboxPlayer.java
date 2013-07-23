package net.aetherteam.aether.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.Aether;
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
    public boolean playingJukebox;

    public JukeboxPlayer()
    {
        this.soundManager = Aether.proxy.getClient().sndManager;
        this.defaultMusic = true;
        this.musicFileName = null;
        this.playingJukebox = false;
    }

    public void process()
    {
        File var10000 = new File;
        StringBuilder var10002 = new StringBuilder();
        Minecraft.getMinecraft();
        var10000.<init>(var10002.append(Minecraft.getMinecraftDir()).append("/resources/streaming/").toString());
        File var1 = var10000;

        if (var1.exists())
        {
            this.jukeboxMusic = this.listMusic(var1, false);
        }
    }

    public void start()
    {
        JukeboxData.loadConfig();

        if (!JukeboxData.hasStartedMusic && this.getMusicFileName() != null)
        {
            ;
        }

        JukeboxData.hasStartedMusic = true;
        JukeboxData.setProperty("hasStartedMusic", "true");
        this.process();
    }

    public void run()
    {
        if (this.playingJukebox && !JukeboxData.muteMusic)
        {
            ++this.musicInterval;
        }
        else
        {
            this.musicInterval = 0;
        }

        if (this.musicInterval > 100)
        {
            this.playingJukebox = false;
            this.musicInterval = 0;
        }

        if (this.jukeboxMusic != null)
        {
            if (JukeboxData.musicIndex > this.jukeboxMusic.size() - 1)
            {
                JukeboxData.musicIndex = 0;
                JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
            }

            if (JukeboxData.musicIndex < 0)
            {
                JukeboxData.musicIndex = this.jukeboxMusic.size() - 1;
                JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
            }
        }

        if (JukeboxData.muteMusic)
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

        return JukeboxData.musicIndex;
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
        if (this.jukeboxMusic != null && !JukeboxData.musicSet)
        {
            JukeboxData.musicSet = true;
            JukeboxData.setProperty("musicSet", "true");

            if (JukeboxData.musicIndex > this.jukeboxMusic.size() - 2)
            {
                JukeboxData.musicIndex = 0;
                JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
            }
            else
            {
                ++JukeboxData.musicIndex;
                JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
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
        return this.jukeboxMusic != null ? (String)this.jukeboxMusic.get(this.defaultMusic ? this.getIndexFromName(this.getMusicFileName()) : MathHelper.clamp_int(JukeboxData.musicIndex, 0, this.jukeboxMusic.size() - 1)) : "";
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
        if (this.isSoundOn())
        {
            SoundManager var10000 = this.soundManager;
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void toggleMute()
    {
        JukeboxData.muteMusic = !JukeboxData.muteMusic;
        JukeboxData.setProperty("muteMusic", String.valueOf(JukeboxData.muteMusic));

        if (JukeboxData.muteMusic)
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
        JukeboxData.loopMusic = !JukeboxData.loopMusic;
        JukeboxData.setProperty("loopMusic", String.valueOf(JukeboxData.loopMusic));
    }

    private void playMusicFile(String var1)
    {
        if (!JukeboxData.muteMusic && this.isSoundOn() && !this.isMusicPlaying())
        {
            float var2 = (float)JukeboxData.playerPosX;
            float var3 = (float)JukeboxData.playerPosY;
            float var4 = (float)JukeboxData.playerPosZ;
            this.soundManager.playStreaming(var1, var2 != 0.0F ? var2 : 0.0F, var3 != 0.0F ? var3 : 0.0F, var4 != 0.0F ? var4 : 0.0F);
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

    public boolean isSoundOn()
    {
        boolean var1;

        if (this.soundManager != null)
        {
            SoundManager var10000 = this.soundManager;

            if (SoundManager.sndSystem != null)
            {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }

    public void playMenuMusic()
    {
        if (this.isSoundOn() && (!JukeboxData.hasPlayedMusic || !this.isMusicPlaying() && JukeboxData.hasPlayedMusic) && JukeboxData.hasStartedMusic)
        {
            if (!JukeboxData.muteMusic && !this.isMusicPlaying() && !this.playingJukebox)
            {
                this.playingJukebox = true;

                if (!JukeboxData.loopMusic)
                {
                    this.setToNextSong();
                }

                this.playMusicFile(this.getCurrentSong());
            }

            JukeboxData.hasPlayedMusic = true;
            JukeboxData.setProperty("hasPlayedMusic", "true");
        }

        JukeboxData.musicSet = false;
        JukeboxData.setProperty("musicSet", "false");
    }
}
