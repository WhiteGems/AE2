package net.aetherteam.aether;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPool;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class AetherSoundLoader
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        registerStreaming(event.manager, "streaming/Aether Tune.ogg", "/resources/streaming/Aether Tune.ogg");
        registerStreaming(event.manager, "streaming/Ascending Dawn.ogg", "/resources/streaming/Ascending Dawn.ogg");

        registerStreaming(event.manager, "streaming/Slider Battle.ogg", "/resources/streaming/Slider Battle.ogg");
        registerStreaming(event.manager, "streaming/Slider Finish.ogg", "/resources/streaming/Slider Finish.ogg");

        registerSound(event.manager, "streaming/Aether Tune.ogg", "/resources/streaming/Aether Tune.ogg");
        registerSound(event.manager, "aeboss/slider/awake.ogg", "/resources/newsound/aeboss/slider/awake.ogg");
        registerSound(event.manager, "aeboss/slider/collide.ogg", "/resources/newsound/aeboss/slider/collide.ogg");
        registerSound(event.manager, "aeboss/slider/die.ogg", "/resources/newsound/aeboss/slider/die.ogg");
        registerSound(event.manager, "aeboss/slider/move.ogg", "/resources/newsound/aeboss/slider/move.ogg");
        registerSound(event.manager, "aeboss/slider/unlock.ogg", "/resources/newsound/aeboss/slider/unlock.ogg");

        registerSound(event.manager, "aemob/aerbunny/die.ogg", "/resources/newsound/aemob/aerbunny/die.ogg");
        registerSound(event.manager, "aemob/aerbunny/hurt1.ogg", "/resources/newsound/aemob/aerbunny/hurt1.ogg");
        registerSound(event.manager, "aemob/aerbunny/hurt2.ogg", "/resources/newsound/aemob/aerbunny/hurt2.ogg");
        registerSound(event.manager, "aemob/aerbunny/land.ogg", "/resources/newsound/aemob/aerbunny/land.ogg");
        registerSound(event.manager, "aemob/aerbunny/lift.ogg", "/resources/newsound/aemob/aerbunny/lift.ogg");

        registerSound(event.manager, "aemob/aerwhale/say.wav", "/resources/newsound/aemob/aerwhale/say.wav");
        registerSound(event.manager, "aemob/aerwhale/die.wav", "/resources/newsound/aemob/aerwhale/die.wav");

        registerSound(event.manager, "aemob/moa/say.wav", "/resources/newsound/aemob/moa/say.wav");

        registerSound(event.manager, "aemob/zephyr/say1.wav", "/resources/newsound/aemob/zephyr/say1.wav");
        registerSound(event.manager, "aemob/zephyr/say2.wav", "/resources/newsound/aemob/zephyr/say2.wav");
        registerSound(event.manager, "aemob/zephyr/shoot.ogg", "/resources/newsound/aemob/zephyr/shoot.ogg");

        registerSound(event.manager, "aemisc/achieveGen.ogg", "/resources/newsound/aemisc/achieveGen.ogg");
        registerSound(event.manager, "aemisc/achieveBronze.ogg", "/resources/newsound/aemisc/achieveBronze.ogg");
        registerSound(event.manager, "aemisc/achieveSilver.ogg", "/resources/newsound/aemisc/achieveSilver.ogg");
        registerSound(event.manager, "aemisc/achieveBronzeNew.ogg", "/resources/newsound/aemisc/achieveBronzeNew.ogg");

        registerSound(event.manager, "aemisc/activateTrap.ogg", "/resources/newsound/aemisc/activateTrap.ogg");

        registerSound(event.manager, "aemisc/shootDart.ogg", "/resources/newsound/aemisc/shootDart.ogg");

        registerSound(event.manager, "aemob/sentryGolem/seenEnemy.ogg", "/resources/newsound/aemob/sentryGolem/seenEnemy.ogg");
        registerSound(event.manager, "aemob/sentryGolem/creepySeen.wav", "/resources/newsound/aemob/sentryGolem/creepySeen.wav");
        registerSound(event.manager, "aemob/sentryGolem/say1.wav", "/resources/newsound/aemob/sentryGolem/say1.wav");
        registerSound(event.manager, "aemob/sentryGolem/say2.wav", "/resources/newsound/aemob/sentryGolem/say2.wav");
        registerSound(event.manager, "aemob/sentryGolem/death.wav", "/resources/newsound/aemob/sentryGolem/death.wav");
        registerSound(event.manager, "aemob/sentryGolem/hit1.wav", "/resources/newsound/aemob/sentryGolem/hit1.wav");
        registerSound(event.manager, "aemob/sentryGolem/hit2.wav", "/resources/newsound/aemob/sentryGolem/hit2.wav");

        registerSound(event.manager, "aemob/hosteye/movement.ogg", "/resources/newsound/aemob/hosteye/movement.ogg");

        registerSound(event.manager, "aemob/sentryGuardian/death.ogg", "/resources/newsound/aemob/sentryGuardian/death.ogg");
        registerSound(event.manager, "aemob/sentryGuardian/hit.ogg", "/resources/newsound/aemob/sentryGuardian/hit.ogg");
        registerSound(event.manager, "aemob/sentryGuardian/spawn.ogg", "/resources/newsound/aemob/sentryGuardian/spawn.ogg");
        registerSound(event.manager, "aemob/sentryGuardian/living.ogg", "/resources/newsound/aemob/sentryGuardian/living.ogg");

        registerSound(event.manager, "aemisc/coin.ogg", "/resources/newsound/aemisc/coin.ogg");

        registerSound(event.manager, "aemob/cog/wall.wav", "/resources/newsound/aemob/cog/wall.wav");
        registerSound(event.manager, "aemob/cog/wall1.wav", "/resources/newsound/aemob/cog/wall1.wav");
        registerSound(event.manager, "aemob/cog/wall2.wav", "/resources/newsound/aemob/cog/wall2.wav");
        registerSound(event.manager, "aemob/cog/wallFinal.ogg", "/resources/newsound/aemob/cog/wallFinal.ogg");

        registerSound(event.manager, "aeportal/aeportal.wav", "/resources/newsound/aeportal/aeportal.wav");
        registerSound(event.manager, "aeportal/aetravel.wav", "/resources/newsound/aeportal/aetravel.wav");
        registerSound(event.manager, "aeportal/aetrigger.wav", "/resources/newsound/aeportal/aetrigger.wav");
    }

    private void registerSound(SoundManager manager, String name, String path)
    {
        try
        {
            URL filePath = AetherSoundLoader.class.getResource(path);
            if (filePath != null) manager.soundPoolSounds.addSound(name, filePath);
            else throw new FileNotFoundException();
        } catch (Exception ex)
        {
            System.out.println(String.format("Warning: unable to load sound file %s", new Object[]{path}));
        }
    }

    private void registerStreaming(SoundManager manager, String name, String path)
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherSoundLoader
 * JD-Core Version:    0.6.2
 */