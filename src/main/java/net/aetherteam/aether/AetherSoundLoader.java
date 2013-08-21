package net.aetherteam.aether;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class AetherSoundLoader
{
    @ForgeSubscribe
    public void onSound(SoundLoadEvent event)
    {
        event.manager.addMusic("aether:Aether Menu Two.ogg");
        event.manager.addMusic("aether:Aether Menu.ogg");
        event.manager.addSound("aether:Aether Tune.ogg");
        event.manager.addMusic("aether:Slider Battle.ogg");
        event.manager.addMusic("aether:Slider Finish.ogg");
        event.manager.addSound("aether:aeboss/slider/awake.ogg");
        event.manager.addSound("aether:aeboss/slider/collide.ogg");
        event.manager.addSound("aether:aeboss/slider/die.ogg");
        event.manager.addSound("aether:aeboss/slider/move.ogg");
        event.manager.addSound("aether:aeboss/slider/unlock.ogg");
        event.manager.addSound("aether:aemob/aerbunny/die.ogg");
        event.manager.addSound("aether:aemob/aerbunny/hurt1.ogg");
        event.manager.addSound("aether:aemob/aerbunny/hurt2.ogg");
        event.manager.addSound("aether:aemob/aerbunny/land.ogg");
        event.manager.addSound("aether:aemob/aerbunny/lift.ogg");
        event.manager.addSound("aether:aemob/aerwhale/say.ogg");
        event.manager.addSound("aether:aemob/aerwhale/die.ogg");
        event.manager.addSound("aether:aemob/moa/say.ogg");
        event.manager.addSound("aether:aemob/zephyr/say1.ogg");
        event.manager.addSound("aether:aemob/zephyr/say2.ogg");
        event.manager.addSound("aether:aemob/zephyr/shoot.ogg");
        event.manager.addSound("aether:aemisc/achieveGen.ogg");
        event.manager.addSound("aether:aemisc/achieveBronze.ogg");
        event.manager.addSound("aether:aemisc/achieveSilver.ogg");
        event.manager.addSound("aether:aemisc/achieveBronzeNew.ogg");
        event.manager.addSound("aether:aemisc/activateTrap.ogg");
        event.manager.addSound("aether:aemisc/shootDart.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/seenEnemy.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/creepySeen.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/say1.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/say2.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/death.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/hit1.ogg");
        event.manager.addSound("aether:aemob/sentryGolem/hit2.ogg");
        event.manager.addSound("aether:aemob/hosteye/movement.ogg");
        event.manager.addSound("aether:aemob/sentryGuardian/death.ogg");
        event.manager.addSound("aether:aemob/sentryGuardian/hit.ogg");
        event.manager.addSound("aether:aemob/sentryGuardian/spawn.ogg");
        event.manager.addSound("aether:aemob/sentryGuardian/living.ogg");
        event.manager.addSound("aether:aemisc/coin.ogg");
        event.manager.addSound("aether:aemob/cog/wall.ogg");
        event.manager.addSound("aether:aemob/cog/wall1.ogg");
        event.manager.addSound("aether:aemob/cog/wall2.ogg");
        event.manager.addSound("aether:aemob/cog/wallFinal.ogg");
        event.manager.addSound("aether:aemob/labyrinthsEye/cogloss.ogg");
        event.manager.addSound("aether:aemob/labyrinthsEye/eyedeath.ogg");
        event.manager.addSound("aether:aemob/labyrinthsEye/move_1.ogg");
        event.manager.addSound("aether:aemob/labyrinthsEye/move_2.ogg");
        event.manager.addSound("aether:aeportal/aeportal.ogg");
        event.manager.addSound("aether:aeportal/aetravel.ogg");
        event.manager.addSound("aether:aeportal/aetrigger.ogg");
    }
}
