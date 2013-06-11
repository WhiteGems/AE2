package net.aetherteam.aether.achievements;

import java.util.Random;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AetherACPage extends AchievementPage
{
    public AetherACPage(Achievement[] pages)
    {
        super("Aether", pages);
    }

    public int bgGetSprite(Random random, int i, int j)
    {
        return 0;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.achievements.AetherACPage
 * JD-Core Version:    0.6.2
 */