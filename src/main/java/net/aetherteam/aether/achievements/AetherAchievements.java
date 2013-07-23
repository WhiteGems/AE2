package net.aetherteam.aether.achievements;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class AetherAchievements
{
    public static final int acOff = 800;

    public static void init() {}

    private static void addAchievementDetails(String var0, String var1, String var2) {}

    private static void addAchievementName(String var0, String var1)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + var0, "en_US", var1);
    }

    private static void addAchievementDesc(String var0, String var1)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + var0 + ".desc", "en_US", var1);
    }
}
