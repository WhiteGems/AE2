package net.aetherteam.aether.entities;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.altar.EntityFakeItem;
import net.aetherteam.aether.entities.bosses.EntityCog;
import net.aetherteam.aether.entities.bosses.EntityHostEye;
import net.aetherteam.aether.entities.bosses.EntityLabyrinthEye;
import net.aetherteam.aether.entities.bosses.EntityMiniSlider;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.aetherteam.aether.entities.bosses.EntitySliderHostMimic;
import net.aetherteam.aether.entities.dungeon.EntityRewardItem;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.aetherteam.aether.entities.mounts.EntitySwet;
import net.aetherteam.aether.oldcode.EntityMiniCloud;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import net.minecraft.world.biome.BiomeGenBase;

public class AetherEntities
{
    public static Aether mod;
    private static int entityId = 1;

    public static void addSpawn(String var0, int var1, int var2, EnumCreatureType var3)
    {
        EntityRegistry.addSpawn(var0, makeWeight(var0, var1), makeGroupSize(var0, var2), makeGroupSize(var0, var2), var3, new BiomeGenBase[]{Aether.biome});
    }

    public static void addMappings()
    {
        EntityRegistry.registerGlobalEntityID(EntityAerwhale.class, "Aerwhale", ModLoader.getUniqueEntityId(), 13434879, 39423);
        EntityRegistry.registerGlobalEntityID(EntityPhyg.class, "Phyg", ModLoader.getUniqueEntityId(), 13434879, 16751001);
        EntityRegistry.registerGlobalEntityID(EntityAerbunny.class, "Aerbunny", ModLoader.getUniqueEntityId(), 13434879, 26316);
        EntityRegistry.registerGlobalEntityID(EntitySheepuff.class, "Sheepuff", ModLoader.getUniqueEntityId(), 13434879, 13421823);
        EntityRegistry.registerGlobalEntityID(EntityCockatrice.class, "Cockatrice", ModLoader.getUniqueEntityId(), 13434879, 5156188);
        EntityRegistry.registerGlobalEntityID(EntityMoa.class, "Moa", ModLoader.getUniqueEntityId(), 13434879, 3394764);
        EntityRegistry.registerGlobalEntityID(EntityAechorPlant.class, "AechorPlant", ModLoader.getUniqueEntityId(), 13434879, 13144035);
        EntityRegistry.registerGlobalEntityID(EntitySentry.class, "Sentry", ModLoader.getUniqueEntityId(), 13434879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntityBattleSentry.class, "SentryMelee", ModLoader.getUniqueEntityId(), 13234879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntityMimic.class, "Mimic", ModLoader.getUniqueEntityId(), 13434879, 14592106);
        EntityRegistry.registerGlobalEntityID(EntityMiniCloud.class, "CloudSentry", ModLoader.getUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(EntitySwet.class, "Swet", ModLoader.getUniqueEntityId(), 13434879, 4107504);
        EntityRegistry.registerGlobalEntityID(EntityTrackingGolem.class, "SentryGolem", ModLoader.getUniqueEntityId(), 13434879, 4107504);
        EntityRegistry.registerGlobalEntityID(EntitySentryGolem.class, "SentryGolemRanged", ModLoader.getUniqueEntityId(), 13434879, 4107504);
        EntityRegistry.registerGlobalEntityID(EntitySentryGuardian.class, "SentryGuardian", ModLoader.getUniqueEntityId(), 134351879, 4127984);
        EntityRegistry.registerGlobalEntityID(EntitySliderHostMimic.class, "Host", ModLoader.getUniqueEntityId(), 134351879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntityHostEye.class, "HostEye", ModLoader.getUniqueEntityId(), 134351879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntityMiniSlider.class, "MiniSlider", ModLoader.getUniqueEntityId(), 134351879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntitySlider.class, "Slider", ModLoader.getUniqueEntityId(), 134351879, 10395294);
        EntityRegistry.registerGlobalEntityID(EntityCog.class, "Cog", ModLoader.getUniqueEntityId(), 1244351879, 6200990);
        EntityRegistry.registerGlobalEntityID(EntityLabyrinthEye.class, "LabyrinthEye", ModLoader.getUniqueEntityId(), 124435179, 2006638);
        EntityRegistry.registerGlobalEntityID(EntityCarrionSprout.class, "CarrionSprout", ModLoader.getUniqueEntityId(), 124435179, 2006638);
        EntityRegistry.registerGlobalEntityID(EntityTempest.class, "Tempest", ModLoader.getUniqueEntityId(), 124435179, 2006638);
        EntityRegistry.registerGlobalEntityID(EntityNewZephyr.class, "Zephyr", ModLoader.getUniqueEntityId(), 124435179, 2006638);
        EntityRegistry.registerGlobalEntityID(EntityZephyroo.class, "Zephyroo", ModLoader.getUniqueEntityId(), 124435179, 2006638);
        EntityRegistry.registerModEntity(EntitySlider.class, "Slider", 1, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityFloatingBlock.class, "FloatingBlock", 2, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityPoisonNeedle.class, "PoisonNeedle", 3, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityDartGolden.class, "DartGolden", 4, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityDartEnchanted.class, "DartEnchanted", 5, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityDartPoison.class, "DartPoison", 6, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityLightningKnife.class, "LightningKnife", 7, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityNotchWave.class, "NotchWave", 8, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityZephyrSnowball.class, "ZephyrSnowball", 9, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityTNTPresent.class, "TNTPresent", 10, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityAerbunny.class, "Aerbunny", 11, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityAetherLightning.class, "AetherLightning", 12, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntitySwet.class, "Swet", 13, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityCloudParachute.class, "CloudParachute", 15, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityDartPhoenix.class, "DartPhoenix", 16, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityHostEye.class, "HostEye", 17, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityProjectileSentry.class, "Sentry Projectile", 18, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityMiniSlider.class, "MiniSlider", 19, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityAetherCoin.class, "AetherCoin", 20, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityCog.class, "Cog", 21, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityLabyrinthEye.class, "LabyrinthEye", 22, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityTempestBall.class, "TempestBall", 23, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityZephyroo.class, "Zephyroo", 24, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityFakeItem.class, "FakeItem", 25, Aether.getInstance(), 80, 1, true);
        EntityRegistry.registerModEntity(EntityRewardItem.class, "RewardItem", 26, Aether.getInstance(), 80, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.Aerwhale.name", "en_US", "Aerwhale");
        LanguageRegistry.instance().addStringLocalization("entity.ZephyrSnowball.name", "en_US", "Zephyr Snowball");
        LanguageRegistry.instance().addStringLocalization("entity.Phyg.name", "en_US", "Phyg");
        LanguageRegistry.instance().addStringLocalization("entity.Aerbunny.name", "en_US", "Aerbunny");
        LanguageRegistry.instance().addStringLocalization("entity.Sheepuff.name", "en_US", "Sheepuff");
        LanguageRegistry.instance().addStringLocalization("entity.CloudParachute.name", "en_US", "Cloud Parachute");
        LanguageRegistry.instance().addStringLocalization("entity.FloatingBlock.name", "en_US", "Floating Block");
        LanguageRegistry.instance().addStringLocalization("entity.NotchWave.name", "en_US", "Wave of Notch");
        LanguageRegistry.instance().addStringLocalization("entity.Cockatrice.name", "en_US", "Cockatrice");
        LanguageRegistry.instance().addStringLocalization("entity.Moa.name", "en_US", "Moa");
        LanguageRegistry.instance().addStringLocalization("entity.FlyingCow.name", "en_US", "Flying Cow");
        LanguageRegistry.instance().addStringLocalization("entity.Aether.Slider.name", "en_US", "Slider");
        LanguageRegistry.instance().addStringLocalization("entity.AechorPlant.name", "en_US", "Aechor Plant");
        LanguageRegistry.instance().addStringLocalization("entity.PoisonNeedle.name", "en_US", "Poison Needle");
        LanguageRegistry.instance().addStringLocalization("entity.DartGolden.name", "en_US", "Golden Dart");
        LanguageRegistry.instance().addStringLocalization("entity.DartEnchanted.name", "en_US", "Enchanted Dart");
        LanguageRegistry.instance().addStringLocalization("entity.DartPoison.name", "en_US", "Poison Dart");
        LanguageRegistry.instance().addStringLocalization("entity.Sentry.name", "en_US", "Detonation Sentry");
        LanguageRegistry.instance().addStringLocalization("entity.SentryMelee.name", "en_US", "Battle Sentry");
        LanguageRegistry.instance().addStringLocalization("entity.LightningKnife.name", "en_US", "Lightning Knife");
        LanguageRegistry.instance().addStringLocalization("entity.Mimic.name", "en_US", "Mimic");
        LanguageRegistry.instance().addStringLocalization("entity.CloudSentry.name", "en_US", "Cloud Sentry");
        LanguageRegistry.instance().addStringLocalization("entity.TNTPresent.name", "en_US", "Present TNT");
        LanguageRegistry.instance().addStringLocalization("entity.AetherLightning.name", "en_US", "Aether Lightning");
        LanguageRegistry.instance().addStringLocalization("entity.Swet.name", "en_US", "Swet");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGolem.name", "en_US", "Tracking Golem");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGolemRanged.name", "en_US", "Sentry Golem");
        LanguageRegistry.instance().addStringLocalization("entity.PhoenixDart.name", "en_US", "Phoenix Dart");
        LanguageRegistry.instance().addStringLocalization("entity.Host.name", "en_US", "Slider Host Mimic");
        LanguageRegistry.instance().addStringLocalization("entity.HostEye.name", "en_US", "Host Eye");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGuardian.name", "en_US", "Sentry Guardian");
        LanguageRegistry.instance().addStringLocalization("entity.Aether.SentryProjectile.name", "en_US", "Sentry Projectile");
        LanguageRegistry.instance().addStringLocalization("entity.MiniSlider.name", "en_US", "Mini Slider");
        LanguageRegistry.instance().addStringLocalization("entity.Slider.name", "en_US", "Slider");
        LanguageRegistry.instance().addStringLocalization("entity.Cog.name", "en_US", "Loose Cog");
        LanguageRegistry.instance().addStringLocalization("entity.LabyrinthEye.name", "en_US", "Labyrinth\'s Eye");
        LanguageRegistry.instance().addStringLocalization("entity.CarrionSprout.name", "en_US", "Carrion Sprout");
        LanguageRegistry.instance().addStringLocalization("entity.Zephyr.name", "en_US", "Zephyr");
        LanguageRegistry.instance().addStringLocalization("entity.Zephyroo.name", "en_US", "Zephyroo");
        LanguageRegistry.instance().addStringLocalization("entity.Tempest.name", "en_US", "Tempest");
        LanguageRegistry.instance().addStringLocalization("entity.Sprite.name", "en_US", "Sprite");
        LanguageRegistry.instance().addStringLocalization("entity.Aerwhale.name", "zh_CN", "以太鲸");
        LanguageRegistry.instance().addStringLocalization("entity.ZephyrSnowball.name", "zh_CN", "西风怪雪球");
        LanguageRegistry.instance().addStringLocalization("entity.Phyg.name", "zh_CN", "以太猪");
        LanguageRegistry.instance().addStringLocalization("entity.Aerbunny.name", "zh_CN", "以太兔");
        LanguageRegistry.instance().addStringLocalization("entity.Sheepuff.name", "zh_CN", "以太羊");
        LanguageRegistry.instance().addStringLocalization("entity.CloudParachute.name", "zh_CN", "浮云");
        LanguageRegistry.instance().addStringLocalization("entity.FloatingBlock.name", "zh_CN", "悬浮方块");
        LanguageRegistry.instance().addStringLocalization("entity.NotchWave.name", "zh_CN", "Notch光波");
        LanguageRegistry.instance().addStringLocalization("entity.Cockatrice.name", "zh_CN", "鸡蛇怪");
        LanguageRegistry.instance().addStringLocalization("entity.Moa.name", "zh_CN", "恐鸟");
        LanguageRegistry.instance().addStringLocalization("entity.FlyingCow.name", "zh_CN", "以太牛");
        LanguageRegistry.instance().addStringLocalization("entity.Aether.Slider.name", "zh_CN", "滑行者");
        LanguageRegistry.instance().addStringLocalization("entity.AechorPlant.name", "zh_CN", "毒箭草");
        LanguageRegistry.instance().addStringLocalization("entity.PoisonNeedle.name", "zh_CN", "毒针");
        LanguageRegistry.instance().addStringLocalization("entity.DartGolden.name", "zh_CN", "黄金飞镖");
        LanguageRegistry.instance().addStringLocalization("entity.DartEnchanted.name", "zh_CN", "魔力飞镖");
        LanguageRegistry.instance().addStringLocalization("entity.DartPoison.name", "zh_CN", "剧毒飞镖");
        LanguageRegistry.instance().addStringLocalization("entity.Sentry.name", "zh_CN", "自爆哨兵");
        LanguageRegistry.instance().addStringLocalization("entity.SentryMelee.name", "zh_CN", "近战哨兵");
        LanguageRegistry.instance().addStringLocalization("entity.LightningKnife.name", "zh_CN", "雷动飞刀");
        LanguageRegistry.instance().addStringLocalization("entity.Mimic.name", "zh_CN", "宝箱怪");
        LanguageRegistry.instance().addStringLocalization("entity.CloudSentry.name", "zh_CN", "云之哨兵");
        LanguageRegistry.instance().addStringLocalization("entity.TNTPresent.name", "zh_CN", "TNT礼物");
        LanguageRegistry.instance().addStringLocalization("entity.AetherLightning.name", "zh_CN", "以太雷霆");
        LanguageRegistry.instance().addStringLocalization("entity.Swet.name", "zh_CN", "以太史莱姆");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGolem.name", "zh_CN", "追踪傀儡");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGolemRanged.name", "zh_CN", "哨卫傀儡");
        LanguageRegistry.instance().addStringLocalization("entity.PhoenixDart.name", "zh_CN", "凤凰飞镖");
        LanguageRegistry.instance().addStringLocalization("entity.Host.name", "zh_CN", "拟态滑行主宰");
        LanguageRegistry.instance().addStringLocalization("entity.HostEye.name", "zh_CN", "主宰之眼");
        LanguageRegistry.instance().addStringLocalization("entity.SentryGuardian.name", "zh_CN", "哨兵傀儡");
        LanguageRegistry.instance().addStringLocalization("entity.Aether.SentryProjectile.name", "zh_CN", "哨兵射弹");
        LanguageRegistry.instance().addStringLocalization("entity.MiniSlider.name", "zh_CN", "迷你滑行者");
        LanguageRegistry.instance().addStringLocalization("entity.Slider.name", "zh_CN", "滑行者");
        LanguageRegistry.instance().addStringLocalization("entity.Cog.name", "zh_CN", "散落的齿轮");
        LanguageRegistry.instance().addStringLocalization("entity.LabyrinthEye.name", "zh_CN", "迷宫之眼");
        LanguageRegistry.instance().addStringLocalization("entity.CarrionSprout.name", "zh_CN", "食腐花");
        LanguageRegistry.instance().addStringLocalization("entity.Zephyr.name", "zh_CN", "西风怪");
        LanguageRegistry.instance().addStringLocalization("entity.Zephyroo.name", "zh_CN", "以太袋鼠");
        LanguageRegistry.instance().addStringLocalization("entity.Tempest.name", "zh_CN", "风暴怪");
        LanguageRegistry.instance().addStringLocalization("entity.Sprite.name", "zh_CN", "烈焰太阳神");
        addSpawn("Aerwhale", 8, 5, EnumCreatureType.creature);
        addSpawn("Tempest", 10, 1, EnumCreatureType.monster);
        addSpawn("Zephyr", 10, 1, EnumCreatureType.monster);
        addSpawn("Phyg", 12, 4, EnumCreatureType.creature);
        addSpawn("Sheepuff", 10, 4, EnumCreatureType.creature);
        addSpawn("Aerbunny", 12, 3, EnumCreatureType.creature);
        addSpawn("Moa", 10, 3, EnumCreatureType.creature);
        addSpawn("Zephyroo", 10, 6, EnumCreatureType.creature);
        addSpawn("Cockatrice", 10, 1, EnumCreatureType.monster);
        addSpawn("AechorPlant", 10, 2, EnumCreatureType.creature);
        addSpawn("Swet", 14, 8, EnumCreatureType.creature);
        addSpawn("CarrionSprout", 10, 3, EnumCreatureType.creature);
    }

    public static int makeGroupSize(String var0, int var1)
    {
        Aether.getInstance();
        return Aether.getConfig().get("GroupSize" + var0, "general", var1).getInt(var1);
    }

    public static int makeWeight(String var0, int var1)
    {
        Aether.getInstance();
        return Aether.getConfig().get("Weight" + var0, "general", var1).getInt(var1);
    }

    public static void registerEntity(Class var0, String var1, int var2, Render var3, int var4, int var5, boolean var6)
    {
        EntityRegistry.registerModEntity(var0, var1, var2, Aether.getInstance(), var4, var5, var6);
        RenderingRegistry.registerEntityRenderingHandler(var0, var3);
    }

    public static void addMapping(Class var0, String var1, int var2, int var3)
    {
        ++entityId;
        EntityRegistry.registerModEntity(var0, var1, entityId, mod, var2, var3, false);
    }

    public static void addMapping(Class var0, String var1, int var2, int var3, boolean var4, boolean var5)
    {
        if (var5)
        {
            EntityRegistry.registerModEntity(var0, var1, EntityRegistry.findGlobalUniqueEntityId(), mod, var2, var3, var4);
        } else
        {
            ++entityId;
            EntityRegistry.registerModEntity(var0, var1, entityId, mod, var2, var3, var4);
        }
    }

    public static void init(Aether var0)
    {
        mod = var0;
        addMappings();
    }
}
