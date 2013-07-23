package net.aetherteam.aether.achievements;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class AetherAchievements
{
    public static final int acOff = 800;

    public static void init()
    {
    }

    private static void addAchievementDetails(String achievement, String name, String desc)
    {
    }

    private static void addAchievementName(String achievement, String name)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + achievement, "en_US", name);
    }

    private static void addAchievementDesc(String achievement, String desc)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + achievement + ".desc", "en_US", desc);
    }
}

