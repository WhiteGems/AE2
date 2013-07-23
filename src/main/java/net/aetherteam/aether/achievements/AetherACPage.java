package net.aetherteam.aether.achievements;

import java.util.Random;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AetherACPage extends AchievementPage
{
    public AetherACPage(Achievement ... var1)
    {
        super("Aether", var1);
    }

    public int bgGetSprite(Random random, int i, int j)
    {
        return 0;
    }
}
