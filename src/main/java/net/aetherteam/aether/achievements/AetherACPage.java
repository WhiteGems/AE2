package net.aetherteam.aether.achievements;

import java.util.Random;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AetherACPage extends AchievementPage
{
    public AetherACPage(Achievement ... pages)
    {
        super("Aether", pages);
    }

    public int bgGetSprite(Random random, int i, int j)
    {
        return 0;
    }
}
