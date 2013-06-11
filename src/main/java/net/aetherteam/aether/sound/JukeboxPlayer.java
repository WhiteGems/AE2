package net.aetherteam.aether.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.MathHelper;
import paulscode.sound.SoundSystem;

public class JukeboxPlayer
{
    public SoundManager soundManager = Aether.proxy.getClient().sndManager;
    private int musicInterval;
    public List jukeboxMusic;
    public boolean defaultMusic = true;
    public String musicFileName = null;

    public boolean playingJukebox = false;

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
        JukeboxData.loadConfig();

        if (JukeboxData.hasStartedMusic != true)
        {
            if (getMusicFileName() == null) ;
        }

        JukeboxData.hasStartedMusic = true;
        JukeboxData.setProperty("hasStartedMusic", "true");

        process();
    }

    public void run()
    {
        if ((this.playingJukebox) && (!JukeboxData.muteMusic))
        {
            this.musicInterval += 1;
        } else this.musicInterval = 0;

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

        return JukeboxData.musicIndex;
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
            if (!JukeboxData.musicSet)
            {
                JukeboxData.musicSet = true;
                JukeboxData.setProperty("musicSet", "true");

                if (JukeboxData.musicIndex > this.jukeboxMusic.size() - 2)
                {
                    JukeboxData.musicIndex = 0;
                    JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
                } else
                {
                    JukeboxData.musicIndex += 1;
                    JukeboxData.setProperty("musicIndex", String.valueOf(JukeboxData.musicIndex));
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
            return (String) this.jukeboxMusic.get(this.defaultMusic ? getIndexFromName(getMusicFileName()) : MathHelper.clamp_int(JukeboxData.musicIndex, 0, this.jukeboxMusic.size() - 1));
        }
        return "";
    }

    public boolean isMusicPlaying()
    {
        return (SoundManager.sndSystem != null) && (SoundManager.sndSystem.playing("streaming"));
    }

    public void muteMusic()
    {
        if (isSoundOn())
        {
            SoundManager.sndSystem.stop("streaming");
        }
    }

    public void toggleMute()
    {
        JukeboxData.muteMusic = !JukeboxData.muteMusic;
        JukeboxData.setProperty("muteMusic", String.valueOf(JukeboxData.muteMusic));

        if (JukeboxData.muteMusic)
        {
            muteMusic();
        } else playMenuMusic();
    }

    public void toggleLoop()
    {
        JukeboxData.loopMusic = !JukeboxData.loopMusic;
        JukeboxData.setProperty("loopMusic", String.valueOf(JukeboxData.loopMusic));
    }

    private void playMusicFile(String musicFile)
    {
        if ((!JukeboxData.muteMusic) && (isSoundOn()) && (!isMusicPlaying()))
        {
            float x = (float) JukeboxData.playerPosX;
            float y = (float) JukeboxData.playerPosY;
            float z = (float) JukeboxData.playerPosZ;

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

    public boolean isSoundOn()
    {
        return (this.soundManager != null) && (SoundManager.sndSystem != null);
    }

    public void playMenuMusic()
    {
        if ((isSoundOn()) && ((!JukeboxData.hasPlayedMusic) || ((!isMusicPlaying()) && (JukeboxData.hasPlayedMusic))) && (JukeboxData.hasStartedMusic))
        {
            if ((!JukeboxData.muteMusic) && (!isMusicPlaying()) && (!this.playingJukebox))
            {
                this.playingJukebox = true;

                if (!JukeboxData.loopMusic)
                {
                    setToNextSong();
                }

                playMusicFile(getCurrentSong());
            }

            JukeboxData.hasPlayedMusic = true;
            JukeboxData.setProperty("hasPlayedMusic", "true");
        }

        JukeboxData.musicSet = false;
        JukeboxData.setProperty("musicSet", "false");
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.sound.JukeboxPlayer
 * JD-Core Version:    0.6.2
 */