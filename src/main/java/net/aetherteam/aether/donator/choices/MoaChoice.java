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

    public MoaChoice(String var1, EnumChoiceType var2, DonatorTexture var3)
    {
        super(var1, var2, var3);
    }

    public void setOverrideAll(boolean var1)
    {
        this.overrideAll = var1;
    }

    public boolean getOverrideAll()
    {
        return this.overrideAll;
    }

    public AetherMoaColour getOverridingColour()
    {
        return this.colour;
    }

    public void setOverridingColour(AetherMoaColour var1)
    {
        this.colour = var1;
    }

    public void spawnParticleEffects(Random var1, EntityPlayer var2) {}
}
