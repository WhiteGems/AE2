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

    public int bgGetSprite(Random var1, int var2, int var3)
    {
        return 0;
    }
}
