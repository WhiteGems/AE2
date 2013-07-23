package net.aetherteam.aether;

import java.io.FileNotFoundException;
import java.net.URL;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class AetherSoundLoader
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent var1)
    {
        this.registerStreaming(var1.manager, "streaming/Aether Tune.ogg", "/resources/streaming/Aether Tune.ogg");
        this.registerStreaming(var1.manager, "streaming/Ascending Dawn.ogg", "/resources/streaming/Ascending Dawn.ogg");
        this.registerStreaming(var1.manager, "streaming/Slider Battle.ogg", "/resources/streaming/Slider Battle.ogg");
        this.registerStreaming(var1.manager, "streaming/Slider Finish.ogg", "/resources/streaming/Slider Finish.ogg");
        this.registerSound(var1.manager, "streaming/Aether Tune.ogg", "/resources/streaming/Aether Tune.ogg");
        this.registerSound(var1.manager, "aeboss/slider/awake.ogg", "/resources/newsound/aeboss/slider/awake.ogg");
        this.registerSound(var1.manager, "aeboss/slider/collide.ogg", "/resources/newsound/aeboss/slider/collide.ogg");
        this.registerSound(var1.manager, "aeboss/slider/die.ogg", "/resources/newsound/aeboss/slider/die.ogg");
        this.registerSound(var1.manager, "aeboss/slider/move.ogg", "/resources/newsound/aeboss/slider/move.ogg");
        this.registerSound(var1.manager, "aeboss/slider/unlock.ogg", "/resources/newsound/aeboss/slider/unlock.ogg");
        this.registerSound(var1.manager, "aemob/aerbunny/die.ogg", "/resources/newsound/aemob/aerbunny/die.ogg");
        this.registerSound(var1.manager, "aemob/aerbunny/hurt1.ogg", "/resources/newsound/aemob/aerbunny/hurt1.ogg");
        this.registerSound(var1.manager, "aemob/aerbunny/hurt2.ogg", "/resources/newsound/aemob/aerbunny/hurt2.ogg");
        this.registerSound(var1.manager, "aemob/aerbunny/land.ogg", "/resources/newsound/aemob/aerbunny/land.ogg");
        this.registerSound(var1.manager, "aemob/aerbunny/lift.ogg", "/resources/newsound/aemob/aerbunny/lift.ogg");
        this.registerSound(var1.manager, "aemob/aerwhale/say.wav", "/resources/newsound/aemob/aerwhale/say.wav");
        this.registerSound(var1.manager, "aemob/aerwhale/die.wav", "/resources/newsound/aemob/aerwhale/die.wav");
        this.registerSound(var1.manager, "aemob/moa/say.wav", "/resources/newsound/aemob/moa/say.wav");
        this.registerSound(var1.manager, "aemob/zephyr/say1.wav", "/resources/newsound/aemob/zephyr/say1.wav");
        this.registerSound(var1.manager, "aemob/zephyr/say2.wav", "/resources/newsound/aemob/zephyr/say2.wav");
        this.registerSound(var1.manager, "aemob/zephyr/shoot.ogg", "/resources/newsound/aemob/zephyr/shoot.ogg");
        this.registerSound(var1.manager, "aemisc/achieveGen.ogg", "/resources/newsound/aemisc/achieveGen.ogg");
        this.registerSound(var1.manager, "aemisc/achieveBronze.ogg", "/resources/newsound/aemisc/achieveBronze.ogg");
        this.registerSound(var1.manager, "aemisc/achieveSilver.ogg", "/resources/newsound/aemisc/achieveSilver.ogg");
        this.registerSound(var1.manager, "aemisc/achieveBronzeNew.ogg", "/resources/newsound/aemisc/achieveBronzeNew.ogg");
        this.registerSound(var1.manager, "aemisc/activateTrap.ogg", "/resources/newsound/aemisc/activateTrap.ogg");
        this.registerSound(var1.manager, "aemisc/shootDart.ogg", "/resources/newsound/aemisc/shootDart.ogg");
        this.registerSound(var1.manager, "aemob/sentryGolem/seenEnemy.ogg", "/resources/newsound/aemob/sentryGolem/seenEnemy.ogg");
        this.registerSound(var1.manager, "aemob/sentryGolem/creepySeen.wav", "/resources/newsound/aemob/sentryGolem/creepySeen.wav");
        this.registerSound(var1.manager, "aemob/sentryGolem/say1.wav", "/resources/newsound/aemob/sentryGolem/say1.wav");
        this.registerSound(var1.manager, "aemob/sentryGolem/say2.wav", "/resources/newsound/aemob/sentryGolem/say2.wav");
        this.registerSound(var1.manager, "aemob/sentryGolem/death.wav", "/resources/newsound/aemob/sentryGolem/death.wav");
        this.registerSound(var1.manager, "aemob/sentryGolem/hit1.wav", "/resources/newsound/aemob/sentryGolem/hit1.wav");
        this.registerSound(var1.manager, "aemob/sentryGolem/hit2.wav", "/resources/newsound/aemob/sentryGolem/hit2.wav");
        this.registerSound(var1.manager, "aemob/hosteye/movement.ogg", "/resources/newsound/aemob/hosteye/movement.ogg");
        this.registerSound(var1.manager, "aemob/sentryGuardian/death.ogg", "/resources/newsound/aemob/sentryGuardian/death.ogg");
        this.registerSound(var1.manager, "aemob/sentryGuardian/hit.ogg", "/resources/newsound/aemob/sentryGuardian/hit.ogg");
        this.registerSound(var1.manager, "aemob/sentryGuardian/spawn.ogg", "/resources/newsound/aemob/sentryGuardian/spawn.ogg");
        this.registerSound(var1.manager, "aemob/sentryGuardian/living.ogg", "/resources/newsound/aemob/sentryGuardian/living.ogg");
        this.registerSound(var1.manager, "aemisc/coin.ogg", "/resources/newsound/aemisc/coin.ogg");
        this.registerSound(var1.manager, "aemob/cog/wall.wav", "/resources/newsound/aemob/cog/wall.wav");
        this.registerSound(var1.manager, "aemob/cog/wall1.wav", "/resources/newsound/aemob/cog/wall1.wav");
        this.registerSound(var1.manager, "aemob/cog/wall2.wav", "/resources/newsound/aemob/cog/wall2.wav");
        this.registerSound(var1.manager, "aemob/cog/wallFinal.ogg", "/resources/newsound/aemob/cog/wallFinal.ogg");
        this.registerSound(var1.manager, "aemob/labyrinthsEye/cogloss.ogg", "/resources/newsound/aemob/labyrinthsEye/cogloss.ogg");
        this.registerSound(var1.manager, "aemob/labyrinthsEye/eyedeath.ogg", "/resources/newsound/aemob/labyrinthsEye/eyedeath.ogg");
        this.registerSound(var1.manager, "aemob/labyrinthsEye/move_1.ogg", "/resources/newsound/aemob/labyrinthsEye/move_1.ogg");
        this.registerSound(var1.manager, "aemob/labyrinthsEye/move_2.ogg", "/resources/newsound/aemob/labyrinthsEye/move_2.ogg");
        this.registerSound(var1.manager, "aeportal/aeportal.wav", "/resources/newsound/aeportal/aeportal.wav");
        this.registerSound(var1.manager, "aeportal/aetravel.wav", "/resources/newsound/aeportal/aetravel.wav");
        this.registerSound(var1.manager, "aeportal/aetrigger.wav", "/resources/newsound/aeportal/aetrigger.wav");
    }

    private void registerSound(SoundManager var1, String var2, String var3)
    {
        try
        {
            URL var4 = AetherSoundLoader.class.getResource(var3);

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

    private void registerStreaming(SoundManager var1, String var2, String var3) {}
}
