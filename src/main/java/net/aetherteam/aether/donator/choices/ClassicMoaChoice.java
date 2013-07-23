package net.aetherteam.aether.donator.choices;

import net.aetherteam.aether.donator.DonatorTexture;
import net.aetherteam.aether.donator.EnumChoiceType;

public class ClassicMoaChoice extends MoaChoice
{
    public ClassicMoaChoice()
    {
        super("Classic Moa", EnumChoiceType.MOA, new DonatorTexture("/old/moa_Classic.png", "Classic.png", 128, 64));
    }
}
