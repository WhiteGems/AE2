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
    public List<String> jukeboxMusic;
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
        File streaming = new File(Minecraft.getMinecraft().mcDataDir + "/resources/streaming/");

        if (streaming.exists())
        {
            this.jukeboxMusic = this.listMusic(streaming, false);
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

    public int getIndexFromName(String musicName)
    {
        this.process();

        if (this.jukeboxMusic != null)
        {
            for (int count = 0; count < this.jukeboxMusic.size(); ++count)
            {
                if (((String)this.jukeboxMusic.get(count)).equalsIgnoreCase(musicName))
                {
                    return count;
                }
            }
        }

        return JukeboxData.musicIndex;
    }

    public List<String> listMusic(File folder, boolean extension)
    {
        if (folder.exists())
        {
            ArrayList files = new ArrayList();
            File[] arr$ = folder.listFiles();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                File fileEntry = arr$[i$];

                if (fileEntry.isDirectory())
                {
                    this.listMusic(fileEntry, extension);
                }
                else
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
        String name = this.getCurrentSong();
        return name.isEmpty() ? "" : name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getCurrentSong()
    {
        return this.jukeboxMusic != null ? (String)this.jukeboxMusic.get(this.defaultMusic ? this.getIndexFromName(this.getMusicFileName()) : MathHelper.clamp_int(JukeboxData.musicIndex, 0, this.jukeboxMusic.size() - 1)) : "";
    }

    public boolean isMusicPlaying()
    {
        return this.soundManager.sndSystem != null && this.soundManager.sndSystem.playing("streaming");
    }

    public void muteMusic()
    {
        if (this.isSoundOn())
        {
            this.soundManager.sndSystem.stop("streaming");
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

    private void playMusicFile(String musicFile)
    {
        if (!JukeboxData.muteMusic && this.isSoundOn() && !this.isMusicPlaying())
        {
            float x = (float)JukeboxData.playerPosX;
            float y = (float)JukeboxData.playerPosY;
            float z = (float)JukeboxData.playerPosZ;
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
        return this.soundManager != null && this.soundManager.sndSystem != null;
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
