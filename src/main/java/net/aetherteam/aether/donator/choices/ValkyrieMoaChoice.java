package net.aetherteam.aether.donator.choices;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.minecraft.entity.player.EntityPlayer;

public class ValkyrieMoaChoice extends MoaChoice
{
    public ValkyrieMoaChoice()
    {
        super("Valkyrie Moa", EnumChoiceType.MOA, new DonatorTexture("saddle_Valkyrie.png", "Valkyrie.png", 256, 128));
    }

    public void spawnParticleEffects(Random random, EntityPlayer player)
    {
        Aether.proxy.spawnDonatorMoaParticles(player, random);
    }
}
