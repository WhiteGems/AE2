package net.aetherteam.aether.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.enums.AetherEnumElement;
import net.aetherteam.aether.interfaces.AEItem;
import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class AetherItems
{
    @AEItem(
            name = "胜利勋章"
    )
    public static Item VictoryMedal;
    @AEItem(
            names = {"青铜钥匙", "白银钥匙", "黄金钥匙", "守卫钥匙", "主宰钥匙", "齿轮钥匙"}
    )
    public static Item Key;
    @AEItem(
            name = "恐鸟蛋"
    )
    public static Item MoaEgg;
    @AEItem(
            name = "以太唱片"
    )
    public static Item AetherMusicDisk;
    @AEItem(
            name = "金色琥珀"
    )
    public static Item GoldenAmber;
    @AEItem(
            name = "毒箭草花瓣"
    )
    public static Item AechorPetal;
    @AEItem(
            name = "天根木木棍"
    )
    public static Item SkyrootStick;
    @AEItem(
            names = {"黄金飞镖", "剧毒飞镖", "魔力飞镖"}
    )
    public static Item Dart;
    @AEItem(
            names = {"黄金吹筒", "剧毒吹筒", "魔力吹筒", "凤凰吹筒"}
    )
    public static Item DartShooter;
    @AEItem(
            name = "以太碳"
    )
    public static Item AmbrosiumShard;
    @AEItem(
            name = "紫晶宝石"
    )
    public static Item ZaniteGemstone;
    @AEItem(
            names = {"天根木木桶", "天根木牛奶桶", "天根木毒液桶", "天根木药剂桶"}
    )
    public static Item SkyrootBucket;
    @AEItem(
            name = "天根木镐"
    )
    public static Item SkyrootPickaxe;
    @AEItem(
            name = "神圣石镐"
    )
    public static Item HolystonePickaxe;
    @AEItem(
            name = "紫晶镐"
    )
    public static Item ZanitePickaxe;
    @AEItem(
            name = "重力镐"
    )
    public static Item GravititePickaxe;
    @AEItem(
            name = "天根木铲"
    )
    public static Item SkyrootShovel;
    @AEItem(
            name = "神圣石铲"
    )
    public static Item HolystoneShovel;
    @AEItem(
            name = "紫晶铲"
    )
    public static Item ZaniteShovel;
    @AEItem(
            name = "重力铲"
    )
    public static Item GravititeShovel;
    @AEItem(
            name = "天根木斧"
    )
    public static Item SkyrootAxe;
    @AEItem(
            name = "神圣石斧"
    )
    public static Item HolystoneAxe;
    @AEItem(
            name = "紫晶斧"
    )
    public static Item ZaniteAxe;
    @AEItem(
            name = "重力斧"
    )
    public static Item GravititeAxe;
    @AEItem(
            name = "天根木剑"
    )
    public static Item SkyrootSword;
    @AEItem(
            name = "神圣石剑"
    )
    public static Item HolystoneSword;
    @AEItem(
            name = "紫晶剑"
    )
    public static Item ZaniteSword;
    @AEItem(
            name = "重力剑"
    )
    public static Item GravititeSword;
    @AEItem(
            name = "铁泡泡"
    )
    public static Item IronBubble;
    @AEItem(
            name = "杀猪刀"
    )
    public static Item PigSlayer;
    @AEItem(
            name = "吸血鬼之刃"
    )
    public static Item VampireBlade;
    @AEItem(
            name = "烈焰之刃"
    )
    public static Item FlamingSword;
    @AEItem(
            name = "圣剑"
    )
    public static Item HolySword;
    @AEItem(
            name = "雷霆之剑"
    )
    public static Item LightningSword;
    @AEItem(
            name = "雷动飞刀"
    )
    public static Item LightningKnife;
    @AEItem(
            names = {"蓝色凝胶", "金色凝胶"}
    )
    public static Item GummieSwet;
    @AEItem(
            name = "Notch之锤"
    )
    public static Item HammerOfNotch;
    @AEItem(
            name = "降落伞"
    )
    public static Item CloudParachute;
    @AEItem(
            name = "金色降落伞"
    )
    public static Item GoldenCloudParachute;
    @AEItem(
            name = "紫色降落伞"
    )
    public static Item PurpleCloudParachute;
    @AEItem(
            name = "绿色降落伞"
    )
    public static Item GreenCloudParachute;
    @AEItem(
            name = "蓝色浮空伞"
    )
    public static Item BlueCloudParachute;
    @AEItem(
            name = "生命碎片"
    )
    public static Item ShardOfLife;
    @AEItem(
            name = "女武神披风"
    )
    public static Item ValkyrieCape;
    @AEItem(
            name = "女武神之枪"
    )
    public static Item ValkyrieLance;
    @AEItem(
            name = "铁指环"
    )
    public static Item IronRing;
    @AEItem(
            name = "金指环"
    )
    public static Item GoldenRing;
    @AEItem(
            name = "紫晶指环"
    )
    public static Item ZaniteRing;
    @AEItem(
            name = "铁吊坠"
    )
    public static Item IronPendant;
    @AEItem(
            name = "金吊坠"
    )
    public static Item GoldenPendant;
    @AEItem(
            name = "紫晶吊坠"
    )
    public static Item ZanitePendant;
    @AEItem(
            name = "史维特披风"
    )
    public static Item SwetCape;
    @AEItem(
            name = "皮革手套"
    )
    public static Item LeatherGloves;
    @AEItem(
            name = "铁手套"
    )
    public static Item IronGloves;
    @AEItem(
            name = "金手套"
    )
    public static Item GoldenGloves;
    @AEItem(
            name = "钻石手套"
    )
    public static Item DiamondGloves;
    @AEItem(
            name = "紫晶手套"
    )
    public static Item ZaniteGloves;
    @AEItem(
            name = "紫晶头盔"
    )
    public static Item ZaniteHelmet;
    @AEItem(
            name = "紫晶胸甲"
    )
    public static Item ZaniteChestplate;
    @AEItem(
            name = "紫晶护腿"
    )
    public static Item ZaniteLeggings;
    @AEItem(
            name = "紫晶靴子"
    )
    public static Item ZaniteBoots;
    @AEItem(
            name = "重力手套"
    )
    public static Item GravititeGloves;
    @AEItem(
            name = "重力头盔"
    )
    public static Item GravititeHelmet;
    @AEItem(
            name = "重力胸甲"
    )
    public static Item GravititeChestplate;
    @AEItem(
            name = "重力护腿"
    )
    public static Item GravititeLeggings;
    @AEItem(
            name = "重力靴子"
    )
    public static Item GravititeBoots;
    @AEItem(
            name = "凤凰手套"
    )
    public static Item PhoenixGloves;
    @AEItem(
            name = "凤凰头盔"
    )
    public static Item PhoenixHelmet;
    @AEItem(
            name = "凤凰胸甲"
    )
    public static Item PhoenixChestplate;
    @AEItem(
            name = "凤凰护腿"
    )
    public static Item PhoenixLeggings;
    @AEItem(
            name = "凤凰靴子"
    )
    public static Item PhoenixBoots;
    @AEItem(
            name = "黑曜石手套"
    )
    public static Item ObsidianGloves;
    @AEItem(
            name = "黑曜石胸甲"
    )
    public static Item ObsidianChestplate;
    @AEItem(
            name = "黑曜石头盔"
    )
    public static Item ObsidianHelmet;
    @AEItem(
            name = "黑曜石护腿"
    )
    public static Item ObsidianLeggings;
    @AEItem(
            name = "黑曜石靴子"
    )
    public static Item ObsidianBoots;
    @AEItem(
            name = "海神手套"
    )
    public static Item NeptuneGloves;
    @AEItem(
            name = "海神头盔"
    )
    public static Item NeptuneHelmet;
    @AEItem(
            name = "海神胸甲"
    )
    public static Item NeptuneChestplate;
    @AEItem(
            name = "海神护腿"
    )
    public static Item NeptuneLeggings;
    @AEItem(
            name = "海神靴子"
    )
    public static Item NeptuneBoots;
    @AEItem(
            name = "重生之石"
    )
    public static Item RegenerationStone;
    @AEItem(
            name = "隐形斗篷"
    )
    public static Item InvisibilityCloak;
    @AEItem(
            name = "敏捷披风"
    )
    public static Item AgilityCape;
    @AEItem(
            name = "白色披风"
    )
    public static Item WhiteCape;
    @AEItem(
            name = "红色披风"
    )
    public static Item RedCape;
    @AEItem(
            name = "黄色披风"
    )
    public static Item YellowCape;
    @AEItem(
            name = "蓝色披风"
    )
    public static Item BlueCape;
    @AEItem(
            name = "女武神镐"
    )
    public static Item ValkyriePickaxe;
    @AEItem(
            name = "女武神斧"
    )
    public static Item ValkyrieAxe;
    @AEItem(
            name = "女武神铲"
    )
    public static Item ValkyrieShovel;
    @AEItem(
            name = "治愈之石"
    )
    public static Item HealingStone;
    @AEItem(
            name = "冰冻指环"
    )
    public static Item IceRing;
    @AEItem(
            name = "冰冻吊坠"
    )
    public static Item IcePendant;
    @AEItem(
            name = "蓝莓"
    )
    public static Item BlueBerry;
    @AEItem(
            name = "姜饼人"
    )
    public static Item GingerBreadMan;
    @AEItem(
            name = "拐杖糖"
    )
    public static Item CandyCane;
    @AEItem(
            name = "拐杖糖剑"
    )
    public static Item CandyCaneSword;
    @AEItem(
            name = "白苹果"
    )
    public static Item WhiteApple;
    @AEItem(
            name = "蓝色史维特球"
    )
    public static Item SwettyBall;
    @AEItem(
            name = "哨卫靴子"
    )
    public static Item SentryBoots;
    @AEItem(
            name = "女武神唱片"
    )
    public static Item ValkyrieMusicDisk;
    @AEItem(
            name = "女武神头盔"
    )
    public static Item ValkyrieHelmet;
    @AEItem(
            name = "女武神胸甲"
    )
    public static Item ValkyrieChestplate;
    @AEItem(
            name = "女武神护腿"
    )
    public static Item ValkyrieLeggings;
    @AEItem(
            name = "女武神靴子"
    )
    public static Item ValkyrieBoots;
    @AEItem(
            name = "女武神手套"
    )
    public static Item ValkyrieGloves;
    @AEItem(
            name = "灵巧披风"
    )
    public static Item DexterityCape;
    @AEItem(
            name = "蓝色史维特吊坠"
    )
    public static Item SwettyPendant;
    @AEItem(
            name = "Deadmau5之耳 "
    )
    public static Item Deadmau5Ears;
    @AEItem(
            name = "魔莓"
    )
    public static Item EnchantedBerry;
    @AEItem(
            name = "彩虹草莓"
    )
    public static Item Strawberry;
    @AEItem(
            name = "命运之珠"
    )
    public static Item ContinuumOrb;
    @AEItem(
            name = "橙子"
    )
    public static Item Orange;
    @AEItem(
            name = "迷宫唱片"
    )
    public static Item LabyrinthMusicDisk;
    @AEItem(
            name = "恐鸟唱片"
    )
    public static Item MoaMusicDisk;
    @net.aetherteam.aether.interfaces.AEItem(
        name = "以太鲸音乐唱片"
    )
    public static Item AerwhaleMusicDisk;
    @net.aetherteam.aether.interfaces.AEItem(
        name = "水晶经验瓶"
    )
    public static Item CrystalBottle;
    @net.aetherteam.aether.interfaces.AEItem(
        name = "小猪存钱罐"
    )
    public static Item PiggieBank;
    @net.aetherteam.aether.interfaces.AEItem(
        name = "蓝莓"
    )
    public static Item Wyndberry;
    public static HashMap spritesPath = new HashMap();

    public static void init()
    {
        spritesPath.put("ROOT", "/net/aetherteam/aether/client/sprites/");
        spritesPath.put("ARMOR", "/net/aetherteam/aether/client/sprites/armor/");
        spritesPath.put("CAPES", "/net/aetherteam/aether/client/sprites/capes/");
        VictoryMedal = (new ItemAether(makeID("VictoryMedal", 17000))).setIconName("Victory Medal").setMaxStackSize(10).setCreativeTab(Aether.misc);
        Key = (new ItemAetherKey(makeID("Key", 17001))).setCreativeTab(Aether.misc);
        MoaEgg = (new ItemMoaEgg(makeID("MoaEgg", 17002))).setIconName("Moa Egg").setCreativeTab(Aether.misc);
        AetherMusicDisk = (new ItemAetherMusicDisk(makeID("AetherMusicDisk", 17003), "Aether Tune", "Noisestorm", "Aether Tune")).setIconName("Aether Music Disk").setCreativeTab(Aether.misc);
        GoldenAmber = (new ItemAether(makeID("GoldenAmber", 17004))).setIconName("Golden Amber").setCreativeTab(Aether.materials);
        AechorPetal = (new ItemAether(makeID("AechorPetal", 17005))).setIconName("Aechor Petal").setCreativeTab(Aether.materials);
        SkyrootStick = (new ItemAether(makeID("SkyrootStick", 17006))).setIconName("Skyroot Stick").setCreativeTab(Aether.materials);
        Dart = (new ItemDart(makeID("Dart", 17007))).setCreativeTab(Aether.weapons);
        DartShooter = (new ItemDartShooter(makeID("DartShooter", 17008))).setIconName("Dart Shooter").setCreativeTab(Aether.weapons);
        AmbrosiumShard = (new ItemAether(makeID("AmbrosiumShard", 17009))).setIconName("Ambrosium Shard").setCreativeTab(Aether.materials);
        ZaniteGemstone = (new ItemAether(makeID("ZaniteGemstone", 17010))).setIconName("Zanite Gemstone").setCreativeTab(Aether.materials);
        SkyrootBucket = (new ItemSkyrootBucket(makeID("SkyrootBucket", 17011))).setCreativeTab(Aether.misc);
        SkyrootPickaxe = (new ItemSkyrootPickaxe(makeID("SkyrootPickaxe", 17012), EnumToolMaterial.WOOD)).setIconName("Skyroot Pickaxe").setCreativeTab(Aether.tools);
        HolystonePickaxe = (new ItemHolystonePickaxe(makeID("HolystonePickaxe", 17013), EnumToolMaterial.STONE)).setIconName("Holystone Pickaxe").setCreativeTab(Aether.tools);
        ZanitePickaxe = (new ItemZanitePickaxe(makeID("ZanitePickaxe", 17014), EnumToolMaterial.IRON)).setIconName("Zanite Pickaxe").setCreativeTab(Aether.tools);
        GravititePickaxe = (new ItemGravititePickaxe(makeID("GravititePickaxe", 17015), EnumToolMaterial.EMERALD)).setIconName("Gravitite Pickaxe").setCreativeTab(Aether.tools);
        SkyrootShovel = (new ItemSkyrootShovel(makeID("SkyrootShovel", 17016), EnumToolMaterial.WOOD)).setIconName("Skyroot Shovel").setCreativeTab(Aether.tools);
        HolystoneShovel = (new ItemHolystoneShovel(makeID("HolystoneShovel", 17017), EnumToolMaterial.STONE)).setIconName("Holystone Shovel").setCreativeTab(Aether.tools);
        ZaniteShovel = (new ItemZaniteShovel(makeID("ZaniteShovel", 17018), EnumToolMaterial.IRON)).setIconName("Zanite Shovel").setCreativeTab(Aether.tools);
        GravititeShovel = (new ItemGravititeShovel(makeID("GravititeShovel", 17019), EnumToolMaterial.EMERALD)).setIconName("Gravitite Shovel").setCreativeTab(Aether.tools);
        SkyrootAxe = (new ItemSkyrootAxe(makeID("SkyrootAxe", 17020), EnumToolMaterial.WOOD)).setIconName("Skyroot Axe").setCreativeTab(Aether.tools);
        HolystoneAxe = (new ItemHolystoneAxe(makeID("HolystoneAxe", 17021), EnumToolMaterial.STONE)).setIconName("Holystone Axe").setCreativeTab(Aether.tools);
        ZaniteAxe = (new ItemZaniteAxe(makeID("ZaniteAxe", 17022), EnumToolMaterial.IRON)).setIconName("Zanite Axe").setCreativeTab(Aether.tools);
        GravititeAxe = (new ItemGravititeAxe(makeID("GravititeAxe", 17023), EnumToolMaterial.EMERALD)).setIconName("Gravitite Axe").setCreativeTab(Aether.tools);
        SkyrootSword = (new ItemSkyrootSword(makeID("SkyrootSword", 17024), EnumToolMaterial.WOOD)).setIconName("Skyroot Sword").setCreativeTab(Aether.weapons);
        HolystoneSword = (new ItemHolystoneSword(makeID("HolystoneSword", 17025), EnumToolMaterial.STONE)).setIconName("Holystone Sword").setCreativeTab(Aether.weapons);
        ZaniteSword = (new ItemZaniteSword(makeID("ZaniteSword", 17026), EnumToolMaterial.IRON)).setIconName("Zanite Sword").setCreativeTab(Aether.weapons);
        GravititeSword = (new ItemGravititeSword(makeID("GravititeSword", 17027), EnumToolMaterial.EMERALD)).setIconName("Gravitite Sword").setCreativeTab(Aether.weapons);
        IronBubble = (new ItemIronBubble(makeID("IronBubble", 17028), 0, 0, 7)).setIconName("Iron Bubble").setCreativeTab(Aether.accessories);
        PigSlayer = (new ItemPigSlayer(makeID("PigSlayer", 17029))).setIconName("Pig Slayer").setCreativeTab(Aether.weapons);
        VampireBlade = (new ItemVampireBlade(makeID("VampireBlade", 17030))).setIconName("Vampire Blade").setCreativeTab(Aether.weapons);
        FlamingSword = (new ItemElementalSword(makeID("FlamingSword", 17031), AetherEnumElement.Fire)).setIconName("Flaming Sword").setCreativeTab(Aether.weapons);
        HolySword = (new ItemElementalSword(makeID("HolySword", 17032), AetherEnumElement.Holy)).setIconName("Holy Sword").setCreativeTab(Aether.weapons);
        LightningSword = (new ItemElementalSword(makeID("FlamingSword", 17033), AetherEnumElement.Lightning)).setIconName("Lightning Sword").setCreativeTab(Aether.weapons);
        LightningKnife = (new ItemLightningKnife(makeID("LightningKnife", 17034))).setIconName("Lightning Knife").setCreativeTab(Aether.weapons);
        GummieSwet = (new ItemGummieSwet(makeID("GummieSwet", 17035))).setIconName("Gummie Swet").setCreativeTab(Aether.food);
        HammerOfNotch = (new ItemHammerOfNotch(makeID("HammerOfNotch", 17036))).setIconName("Hammer of Notch").setCreativeTab(Aether.weapons);
        CloudParachute = (new ItemCloudParachute(makeID("CloudParachute", 17038), 0)).setIconName("Cloud Parachute").setCreativeTab(Aether.misc);
        GoldenCloudParachute = (new ItemCloudParachute(makeID("GoldenCloudParachute", 17039), 20)).setIconName("Golden Cloud Parachute").setCreativeTab(Aether.misc);
        ShardOfLife = (new ItemShardOfLife(makeID("ShardOfLife", 17041))).setIconName("Shard of Life").setCreativeTab(Aether.misc);
        ValkyrieCape = (new ItemValkyrieCape(makeID("ValkyrieCape", 17042), 0, (String)spritesPath.get("CAPES") + "cape_valkyrie.png", 5)).setIconName("Valkyrie Cape").setCreativeTab(Aether.capes);
        ValkyrieLance = (new ItemValkyrieLance(makeID("ValkyrieLance", 17043), EnumToolMaterial.EMERALD)).setIconName("Valkyrie Lance").setCreativeTab(Aether.weapons);
        IronRing = (new ItemAccessory(makeID("IronRing", 17044), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8)).setIconName("Iron Ring").setCreativeTab(Aether.accessories);
        GoldenRing = (new ItemAccessory(makeID("GoldenRing", 17045), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8)).setIconName("Golden Ring").setCreativeTab(Aether.accessories);
        ZaniteRing = (new ItemAccessory(makeID("ZaniteRing", 17046), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8)).setIconName("Zanite Ring").setCreativeTab(Aether.accessories);
        IronPendant = (new ItemAccessory(makeID("IronPendant", 17047), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4)).setIconName("Iron Pendant").setCreativeTab(Aether.accessories);
        GoldenPendant = (new ItemAccessory(makeID("GoldenPendant", 17048), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4)).setIconName("Golden Pendant").setCreativeTab(Aether.accessories);
        ZanitePendant = (new ItemAccessory(makeID("ZanitePendant", 17049), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4)).setIconName("Zanite Pendant").setCreativeTab(Aether.accessories);
        SwetCape = (new ItemAccessory(makeID("SwetCape", 17051), 0, (String)spritesPath.get("CAPES") + "cape_swet.png", 5)).setIconName("Swet Cape").setCreativeTab(Aether.capes);
        LeatherGloves = (new ItemAccessory(makeID("LeatherGloves", 17052), 0, (String)spritesPath.get("ARMOR") + "Leather.png", 10)).setIconName("Leather Gloves").setCreativeTab(Aether.armour);
        IronGloves = (new ItemAccessory(makeID("IronGloves", 17053), 2, (String)spritesPath.get("ARMOR") + "Accessories.png", 10)).setIconName("Iron Gloves").setCreativeTab(Aether.armour);
        GoldenGloves = (new ItemAccessory(makeID("GoldenGloves", 17054), 1, (String)spritesPath.get("ARMOR") + "Gold.png", 10)).setIconName("Golden Gloves").setCreativeTab(Aether.armour);
        DiamondGloves = (new ItemAccessory(makeID("DiamondGloves", 17055), 3, (String)spritesPath.get("ARMOR") + "Diamond.png", 10)).setIconName("Diamond Gloves").setCreativeTab(Aether.armour);
        ZaniteGloves = (new ItemAccessory(makeID("ZaniteGloves", 17056), 2, (String)spritesPath.get("ARMOR") + "Zanite.png", 10)).setIconName("Zanite Gloves").setCreativeTab(Aether.armour);
        ZaniteHelmet = (new ItemColouredArmor(makeID("ZaniteHelmet", 17057), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 0, 16777215, "Zanite")).setIconName("Zanite Helmet").setCreativeTab(Aether.armour);
        ZaniteChestplate = (new ItemColouredArmor(makeID("ZaniteChestplate", 17058), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 1, 16777215, "Zanite")).setIconName("Zanite Chestplate").setCreativeTab(Aether.armour);
        ZaniteLeggings = (new ItemColouredArmor(makeID("ZaniteLeggings", 17059), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 2, 16777215, "Zanite")).setIconName("Zanite Leggings").setCreativeTab(Aether.armour);
        ZaniteBoots = (new ItemColouredArmor(makeID("ZaniteBoots", 17060), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 3, 16777215, "Zanite")).setIconName("Zanite Boots").setCreativeTab(Aether.armour);
        GravititeGloves = (new ItemAccessory(makeID("GravititeGloves", 17061), 3, (String)spritesPath.get("ARMOR") + "Gravitite.png", 10, 16777215, false)).setIconName("Gravitite Gloves").setCreativeTab(Aether.armour);
        GravititeHelmet = (new ItemColouredArmor(makeID("GravititeHelmet", 17062), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 0, 16777215, "Gravitite")).setIconName("Gravitite Helmet").setCreativeTab(Aether.armour);
        GravititeChestplate = (new ItemColouredArmor(makeID("GravititeChestplate", 17063), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 1, 16777215, "Gravitite")).setIconName("Gravitite Chestplate").setCreativeTab(Aether.armour);
        GravititeLeggings = (new ItemColouredArmor(makeID("GravititeLeggings", 17064), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 2, 16777215, "Gravitite")).setIconName("Gravitite Leggings").setCreativeTab(Aether.armour);
        GravititeBoots = (new ItemColouredArmor(makeID("GravititeBoots", 17065), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 3, 16777215, "Gravitite")).setIconName("Gravitite Boots").setCreativeTab(Aether.armour);
        PhoenixGloves = (new ItemAccessory(makeID("PhoenixGloves", 17066), 3, (String)spritesPath.get("ARMOR") + "Phoenix.png", 10, 16777215, false)).setIconName("Phoenix Gloves").setCreativeTab(Aether.armour);
        PhoenixHelmet = (new ItemAetherArmor(makeID("PhoenixHelmet", 17067), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 0, "Phoenix")).setIconName("Phoenix Helmet").setCreativeTab(Aether.armour);
        PhoenixChestplate = (new ItemAetherArmor(makeID("PhoenixChestplate", 17068), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 1, "Phoenix")).setIconName("Phoenix Chestplate").setCreativeTab(Aether.armour);
        PhoenixLeggings = (new ItemAetherArmor(makeID("PhoenixLeggings", 17069), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 2, "Phoenix")).setIconName("Phoenix Leggings").setCreativeTab(Aether.armour);
        PhoenixBoots = (new ItemAetherArmor(makeID("PhoenixBoots", 17070), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 3, "Phoenix")).setIconName("Phoenix Boots").setCreativeTab(Aether.armour);
        ObsidianGloves = (new ItemAccessory(makeID("ObsidianGloves", 17071), 4, (String)spritesPath.get("ARMOR") + "Obsidian.png", 10, 16777215, false)).setIconName("Obsidian Gloves").setCreativeTab(Aether.armour);
        ObsidianHelmet = (new ItemColouredArmor(makeID("ObsidianHelmet", 17072), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 0, 16777215, "Obsidian")).setIconName("Obsidian Helmet").setCreativeTab(Aether.armour);
        ObsidianChestplate = (new ItemColouredArmor(makeID("ObsidianChestplate", 17073), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 1, 16777215, "Obsidian")).setIconName("Obsidian Chestplate").setCreativeTab(Aether.armour);
        ObsidianLeggings = (new ItemColouredArmor(makeID("ObsidianLeggings", 17074), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 2, 16777215, "Obsidian")).setIconName("Obsidian Leggings").setCreativeTab(Aether.armour);
        ObsidianBoots = (new ItemColouredArmor(makeID("ObsidianBoots", 17075), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 3, 16777215, "Obsidian")).setIconName("Obsidian Boots").setCreativeTab(Aether.armour);
        NeptuneGloves = (new ItemAccessory(makeID("NeptuneGloves", 17076), 3, (String)spritesPath.get("ARMOR") + "Neptune.png", 10, 16777215)).setIconName("Neptune Gloves").setCreativeTab(Aether.armour);
        NeptuneHelmet = (new ItemNeptuneArmor(makeID("NeptuneHelmet", 17077), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 0, 16777215, "Neptune")).setIconName("Neptune Helmet").setCreativeTab(Aether.armour);
        NeptuneChestplate = (new ItemNeptuneArmor(makeID("NeptuneChestplate", 17078), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 1, 16777215, "Neptune")).setIconName("Neptune Chestplate").setCreativeTab(Aether.armour);
        NeptuneLeggings = (new ItemNeptuneArmor(makeID("NeptuneLeggings", 17079), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 2, 16777215, "Neptune")).setIconName("Neptune Leggings").setCreativeTab(Aether.armour);
        NeptuneBoots = (new ItemNeptuneArmor(makeID("NeptuneBoots", 17080), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 3, 16777215, "Neptune")).setIconName("Neptune Boots").setCreativeTab(Aether.armour);
        RegenerationStone = (new ItemRegenerationStone(makeID("RegenerationStone", 17081), 0, 0, 7)).setIconName("Regeneration Stone").setCreativeTab(Aether.accessories);
        InvisibilityCloak = (new ItemInvisibilityCloak(makeID("InvisibilityCloak", 17082), 0, 0, 5)).setIconName("Invisibility Cloak").setCreativeTab(Aether.capes);
        AgilityCape = (new ItemAgilityCape(makeID("AgilityCape", 17083), 0, (String)spritesPath.get("CAPES") + "cape_agility.png", 5)).setIconName("Agility Cape").setCreativeTab(Aether.capes);
        WhiteCape = (new ItemAccessory(makeID("WhiteCape", 17084), 0, (String)spritesPath.get("CAPES") + "cape_white.png", 5)).setIconName("White Cape").setCreativeTab(Aether.capes);
        RedCape = (new ItemAccessory(makeID("RedCape", 17085), 0, (String)spritesPath.get("CAPES") + "cape_red.png", 5)).setIconName("Red Cape").setCreativeTab(Aether.capes);
        YellowCape = (new ItemAccessory(makeID("YellowCape", 17086), 0, (String)spritesPath.get("CAPES") + "cape_yellow.png", 5)).setIconName("Yellow Cape").setCreativeTab(Aether.capes);
        BlueCape = (new ItemAccessory(makeID("BlueCape", 17087), 0, (String)spritesPath.get("CAPES") + "cape_blue.png", 5)).setIconName("Blue Cape").setCreativeTab(Aether.capes);
        ValkyriePickaxe = (new ItemValkyriePickaxe(makeID("ValkyriePickaxe", 17088), EnumToolMaterial.EMERALD)).setIconName("Valkyrie Pickaxe").setCreativeTab(Aether.tools);
        ValkyrieAxe = (new ItemValkyrieAxe(makeID("ValkyrieAxe", 17089), EnumToolMaterial.EMERALD)).setIconName("Valkyrie Axe").setCreativeTab(Aether.tools);
        ValkyrieShovel = (new ItemValkyrieShovel(makeID("ValkyrieShovel", 17090), EnumToolMaterial.EMERALD)).setIconName("Valkyrie Shovel").setCreativeTab(Aether.tools);
        HealingStone = (new ItemHealingStone(makeID("HealingStone", 17091), 0, 1.2F, false)).setIconName("Healing Stone").setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 30, 0, 1.0F).setCreativeTab(Aether.food);
        IceRing = (new ItemIceAccessory(makeID("IceRing", 17092), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8, 9823975)).setIconName("Ice Ring").setCreativeTab(Aether.accessories).setMaxDamage(6000);
        IcePendant = (new ItemIceAccessory(makeID("IcePendant", 17093), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4, 9823975)).setIconName("Ice Pendant").setCreativeTab(Aether.accessories).setMaxDamage(6000);
        BlueBerry = (new ItemAetherFood(makeID("BlueBerry", 17094), 2, false)).setIconName("Blue Berry").setCreativeTab(Aether.food);
        GingerBreadMan = (new ItemAetherFood(makeID("GingerbreadMan", 17095), 2, false)).setIconName("Gingerbread Man").setCreativeTab(Aether.food);
        CandyCane = (new ItemAetherFood(makeID("CandyCane", 17096), 2, false)).setIconName("Candy Cane").setCreativeTab(Aether.food);
        CandyCaneSword = (new ItemCandyCaneSword(makeID("CandyCaneSword", 17097), EnumToolMaterial.WOOD)).setIconName("Candy Cane Sword").setCreativeTab(Aether.weapons);
        WhiteApple = (new ItemPoisonCure(makeID("WhiteApple", 17098), 0, false)).setIconName("White Apple").setAlwaysEdible().setCreativeTab(Aether.food);
        SwettyBall = (new ItemAether(makeID("SwettyBall", 17099))).setIconName("Swetty Ball").setCreativeTab(Aether.materials);
        SentryBoots = (new ItemAetherArmor(makeID("SentryBoots", 17100), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Sentry"), 3, "Sentry")).setIconName("Sentry Boots").setCreativeTab(Aether.armour);
        ValkyrieMusicDisk = (new ItemAetherMusicDisk(makeID("ValkyrieMusicDisk", 17101), "Ascending Dawn", "Emile van Krieken", "Ascending Dawn")).setIconName("Valkyrie Music Disk").setCreativeTab(Aether.misc);
        ValkyrieHelmet = (new ItemAetherArmor(makeID("ValkyrieHelmet", 17102), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 0, "Valkyrie")).setIconName("Valkyrie Helmet").setCreativeTab(Aether.armour);
        ValkyrieChestplate = (new ItemAetherArmor(makeID("ValkyrieChestplate", 17103), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 1, "Valkyrie")).setIconName("Valkyrie Chestplate").setCreativeTab(Aether.armour);
        ValkyrieLeggings = (new ItemAetherArmor(makeID("ValkyrieLeggings", 17104), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 2, "Valkyrie")).setIconName("Valkyrie Leggings").setCreativeTab(Aether.armour);
        ValkyrieBoots = (new ItemAetherArmor(makeID("ValkyrieBoots", 17105), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 3, "Valkyrie")).setIconName("Valkyrie Boots").setCreativeTab(Aether.armour);
        ValkyrieGloves = (new ItemAccessory(makeID("ValkyrieGloves", 17106), 3, (String)spritesPath.get("ARMOR") + "Valkyrie.png", 10, 16777215, false)).setIconName("Valkyrie Gloves").setCreativeTab(Aether.armour);
        DexterityCape = (new ItemDexterityCape(makeID("DexterityCape", 17107), 0, (String)spritesPath.get("CAPES") + "cape_dexterity.png", 5)).setIconName("Dexterity Cape").setCreativeTab(Aether.capes);
        SwettyPendant = (new ItemAccessory(makeID("SwettyPendant", 17108), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4)).setIconName("Swetty Pendant").setCreativeTab(Aether.accessories);
        Deadmau5Ears = (new ItemAccessory(makeID("Deadmau5Ears", 17109), 0, 0, 7)).setIconName("Deadmau5 Ears").setCreativeTab(Aether.accessories);
        EnchantedBerry = (new ItemAetherFood(makeID("EnchantedBerry", 17110), 8, false)).setIconName("Enchanted Berry").setCreativeTab(Aether.food);
        Strawberry = (new ItemAetherFood(makeID("Strawberry", 17112), 8, false)).setIconName("Rainbow Strawberry").setCreativeTab(Aether.food);
        ContinuumOrb = (new ItemContinuum(makeID("ContinuumOrb", 17113))).setIconName("Continuum Orb").setCreativeTab(Aether.materials);
        Orange = (new ItemAetherFood(makeID("Orange", 17114), 4, false)).setIconName("Orange").setCreativeTab(Aether.food);
        LabyrinthMusicDisk = (new ItemAetherMusicDisk(makeID("LabyrinthMusicDisk", 17115), "Demise", "Moorziey", "Demise")).setIconName("Labyrinth Music Disk").setCreativeTab(Aether.misc);
        MoaMusicDisk = (new ItemAetherMusicDisk(makeID("MoaMusicDisk", 17116), "Approaches", "Emile van Krieken", "Approaches")).setIconName("Moa Music Disk").setCreativeTab(Aether.misc);
        AerwhaleMusicDisk = (new ItemAetherMusicDisk(makeID("AerwhaleMusicDisk", 17117), "Aerwhale", "AetherAudio", "Aerwhale")).setIconName("Aerwhale Music Disk").setCreativeTab(Aether.misc);
        CrystalBottle = (new ItemCrystalBottle(makeID("CrystalBottle", 17118), 0, 0, 7)).setIconName("Crystal EXP Bottle").setCreativeTab(Aether.misc);
        PiggieBank = (new ItemPiggieBank(makeID("PiggieBank", 17119), 0, 0, 7)).setIconName("Piggie Bank").setCreativeTab(Aether.misc);
        PurpleCloudParachute = (new ItemCloudParachute(makeID("PurpleCloudParachute", 17120), 0)).setIconName("Purple Cloud Parachute").setCreativeTab(Aether.misc);
        GreenCloudParachute = (new ItemCloudParachute(makeID("GreenCloudParachute", 17121), 0)).setIconName("Green Cloud Parachute").setCreativeTab(Aether.misc);
        BlueCloudParachute = (new ItemCloudParachute(makeID("BlueCloudParachute", 17122), 0)).setIconName("Blue Cloud Parachute").setCreativeTab(Aether.misc);
        Wyndberry = (new ItemAetherFood(makeID("Wyndberry", 17123), 4, false)).setIconName("Wyndberry").setCreativeTab(Aether.food);
        registerItemNames();
        ItemContinuum.addBan(Item.map.itemID);
        ItemContinuum.addBan(Item.enchantedBook.itemID);
        ItemContinuum.addBan(Item.firework.itemID);
        ItemContinuum.addBan(Item.fireworkCharge.itemID);
        ItemContinuum.addBan(Item.monsterPlacer.itemID);
        ItemContinuum.addBan(AetherBlocks.AetherPortal.blockID);
        ItemContinuum.addBan(Block.waterMoving.blockID);
        ItemContinuum.addBan(Block.waterStill.blockID);
        ItemContinuum.addBan(Block.lavaMoving.blockID);
        ItemContinuum.addBan(Block.lavaStill.blockID);
        ItemContinuum.addBan(Block.bedrock.blockID);
        ItemContinuum.addBan(Block.bed.blockID);
        ItemContinuum.addBan(Block.pistonExtension.blockID);
        ItemContinuum.addBan(Block.pistonMoving.blockID);
        ItemContinuum.addBan(Block.furnaceBurning.blockID);
        ItemContinuum.addBan(Block.signPost.blockID);
        ItemContinuum.addBan(Block.endPortal.blockID);
        ItemContinuum.addBan(Block.endPortalFrame.blockID);
        ItemContinuum.addBan(Block.portal.blockID);
        ItemContinuum.addBan(Block.mobSpawner.blockID);
        ItemContinuum.addBan(Block.redstoneComparatorActive.blockID);
        ItemContinuum.addBan(Block.redstoneLampActive.blockID);
        ItemContinuum.addBan(Block.torchRedstoneActive.blockID);
        ItemContinuum.addBan(Block.redstoneRepeaterActive.blockID);
        ItemContinuum.addBan(Block.fire.blockID);
        ItemContinuum.addBan(AetherBlocks.ColdFire.blockID);
    }

    public static int makeID(String var0, int var1)
    {
        return Aether.getConfig().getItem(var0, var1).getInt(var1);
    }

    private static void registerItemNames()
    {
        Field[] var0 = AetherItems.class.getDeclaredFields();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            Field var3 = var0[var2];
            AEItem var4 = var3.getAnnotation(AEItem.class);

            if (var4 != null && Item.class.isAssignableFrom(var3.getType()))
            {
                Item var5;

                try
                {
                    var5 = (Item)var3.get((Object)null);
                }
                catch (IllegalAccessException var8)
                {
                    var8.printStackTrace();
                    continue;
                }

                String[] var6 = var4.names();

                if (var6.length != 0)
                {
                    for (int var7 = 0; var7 < var6.length; ++var7)
                    {
                        LanguageRegistry.addName(new ItemStack(var5, 1, var7), var6[var7]);
                    }
                }
                else
                {
                    LanguageRegistry.addName(var5, var4.name());
                }
            }
        }

        String[] var9 = AetherMoaColour.getNames();

        for (var1 = 0; var1 < AetherMoaColour.colours.size(); ++var1)
        {
            LanguageRegistry.addName(new ItemStack(MoaEgg, 1, var1), var9[var1] + "恐鸟蛋");
        }

        LanguageRegistry.addName(new ItemStack(SkyrootBucket, 1, Block.waterMoving.blockID), "天根木水桶");
    }
}
