package net.aetherteam.mainmenu_api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.client.event.sound.SoundEvent;

public class JukeboxPlayer
{
    public SoundManager soundManager;
    private int musicInterval;
    public String[] playlist;
    public boolean songIsPlaying;

    public JukeboxPlayer()
    {
        this.soundManager = MainMenuAPI.proxy.getClient().sndManager;
        this.songIsPlaying = false;
    }

    public void start(String[] playlist)
    {
        MenuBaseConfig.loadConfig();
        MenuBaseConfig.hasStartedMusic = true;
        MenuBaseConfig.setProperty("hasStartedMusic", "true");
        this.playlist = playlist;
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

        if (this.playlist != null)
        {
            if (MenuBaseConfig.musicIndex > this.playlist.length - 1)
            {
                MenuBaseConfig.musicIndex = 0;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }

            if (MenuBaseConfig.musicIndex < 0)
            {
                MenuBaseConfig.musicIndex = this.playlist.length - 1;
                MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
            }
        }

        if (MenuBaseConfig.muteMusic)
        {
            this.muteMusic();
        }

        this.playMenuMusic();
    }

    public int getIndexFromName(String musicName)
    {
        if (this.playlist != null)
        {
            for (int count = 0; count < this.playlist.length; ++count)
            {
                if (this.playlist[count].equalsIgnoreCase(musicName))
                {
                    return count;
                }
            }
        }

        return MenuBaseConfig.musicIndex;
    }

    private void setToNextSong()
    {
        if (this.playlist != null && !MenuBaseConfig.musicSet)
        {
            MenuBaseConfig.musicSet = true;
            MenuBaseConfig.setProperty("musicSet", "true");

            if (MenuBaseConfig.musicIndex > this.playlist.length - 2)
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
        String name = this.getCurrentSong().substring(this.getCurrentSong().indexOf(":") + 1);
        return name.isEmpty() ? "" : name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getCurrentSong()
    {
        return this.playlist.length > 0 ? this.playlist[MathHelper.clamp_int(MenuBaseConfig.musicIndex, 0, this.playlist.length - 1)] : "";
    }

    public boolean isMusicPlaying()
    {
        return this.soundManager.sndSystem != null && this.soundManager.sndSystem.playing("BgMusic");
    }

    public void muteMusic()
    {
        if (this.soundManager != null && this.soundManager.sndSystem != null)
        {
            this.soundManager.sndSystem.stop("BgMusic");
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

    private void playMusicFile(String musicFile)
    {
        if (!MenuBaseConfig.muteMusic && this.soundManager.sndSystem != null)
        {
            System.out.println("Playing Music File: " + musicFile);
            SoundPoolEntry soundpoolentry = this.soundManager.soundPoolMusic.getRandomSoundFromSoundPool(musicFile);
            soundpoolentry = SoundEvent.getResult(new PlayBackgroundMusicEvent(this.soundManager, soundpoolentry));

            if (soundpoolentry != null)
            {
                this.soundManager.sndSystem.backgroundMusic("BgMusic", soundpoolentry.func_110457_b(), soundpoolentry.func_110458_a(), false);
                this.soundManager.sndSystem.setVolume("BgMusic", Minecraft.getMinecraft().gameSettings.musicVolume);
                this.soundManager.sndSystem.play("BgMusic");
            }
        }
    }

    public void playMenuMusic()
    {
        if (MainMenuAPI.proxy.getClient().sndManager.sndSystem != null && (!MenuBaseConfig.hasPlayedMusic || !this.isMusicPlaying() && MenuBaseConfig.hasPlayedMusic) && MenuBaseConfig.hasStartedMusic)
        {
            if (!MenuBaseConfig.muteMusic && !this.isMusicPlaying() && !this.songIsPlaying)
            {
                this.songIsPlaying = true;

                if (!MenuBaseConfig.loopMusic)
                {
                    this.setToNextSong();
                }

                this.playMusicFile(this.getCurrentSong());
            }

            MenuBaseConfig.hasPlayedMusic = true;
            MenuBaseConfig.setProperty("hasPlayedMusic", "true");
        }

        MenuBaseConfig.musicSet = false;
        MenuBaseConfig.setProperty("musicSet", "false");
    }
}
