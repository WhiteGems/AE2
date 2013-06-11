package net.aetherteam.aether.donator.choices;

import java.util.Random;

import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.donator.DonatorChoice;
import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.minecraft.entity.player.EntityPlayer;

public class MoaChoice extends DonatorChoice
{
    AetherMoaColour colour = null;
    boolean overrideAll = true;

    public MoaChoice(String name, EnumChoiceType type, DonatorTexture texture)
    {
        super(name, type, texture);
    }

    public void setOverrideAll(boolean all)
    {
        this.overrideAll = all;
    }

    public boolean getOverrideAll()
    {
        return this.overrideAll;
    }

    public AetherMoaColour getOverridingColour()
    {
        return this.colour;
    }

    public void setOverridingColour(AetherMoaColour colour)
    {
        this.colour = colour;
    }

    public void spawnParticleEffects(Random random, EntityPlayer player)
    {
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.donator.choices.MoaChoice
 * JD-Core Version:    0.6.2
 */