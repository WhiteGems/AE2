package net.aetherteam.aether.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class AetherItems
{
    @AEItem(name = "Victory Medal")
    public static Item VictoryMedal;

    @AEItem(names = {"Bronze Key", "Silver Key", "Golden Key", "Guardian Key", "Host Key", "Cog Key"})
    public static Item Key;

    @AEItem(name = "Moa Egg")
    public static Item MoaEgg;

    @AEItem(name = "Aether Music Disk")
    public static Item AetherMusicDisk;

    @AEItem(name = "Golden Amber")
    public static Item GoldenAmber;

    @AEItem(name = "Aechor Petal")
    public static Item AechorPetal;

    @AEItem(name = "Skyroot Stick")
    public static Item SkyrootStick;

    @AEItem(names = {"Golden Dart", "Poison Dart", "Enchanted Dart"})
    public static Item Dart;

    @AEItem(names = {"Golden Dart Shooter", "Poison Dart Shooter", "Enchanted Dart Shooter", "Phoenix Dart Shooter"})
    public static Item DartShooter;

    @AEItem(name = "Ambrosium Shard")
    public static Item AmbrosiumShard;

    @AEItem(name = "Zanite Gemstone")
    public static Item ZaniteGemstone;

    @AEItem(names = {"Skyroot Bucket", "Skyroot Milk Bucket", "Skyroot Poison Bucket", "Skyroot Cure Bucket"})
    public static Item SkyrootBucket;

    @AEItem(name = "Skyroot Pickaxe")
    public static Item SkyrootPickaxe;

    @AEItem(name = "Holystone Pickaxe")
    public static Item HolystonePickaxe;

    @AEItem(name = "Zanite Pickaxe")
    public static Item ZanitePickaxe;

    @AEItem(name = "Gravitite Pickaxe")
    public static Item GravititePickaxe;

    @AEItem(name = "Skyroot Shovel")
    public static Item SkyrootShovel;

    @AEItem(name = "Holystone Shovel")
    public static Item HolystoneShovel;

    @AEItem(name = "Zanite Shovel")
    public static Item ZaniteShovel;

    @AEItem(name = "Gravitite Shovel")
    public static Item GravititeShovel;

    @AEItem(name = "Skyroot Axe")
    public static Item SkyrootAxe;

    @AEItem(name = "Holystone Axe")
    public static Item HolystoneAxe;

    @AEItem(name = "Zanite Axe")
    public static Item ZaniteAxe;

    @AEItem(name = "Gravitite Axe")
    public static Item GravititeAxe;

    @AEItem(name = "Skyroot Sword")
    public static Item SkyrootSword;

    @AEItem(name = "Holystone Sword")
    public static Item HolystoneSword;

    @AEItem(name = "Zanite Sword")
    public static Item ZaniteSword;

    @AEItem(name = "Gravitite Sword")
    public static Item GravititeSword;

    @AEItem(name = "Iron Bubble")
    public static Item IronBubble;

    @AEItem(name = "Pig Slayer")
    public static Item PigSlayer;

    @AEItem(name = "Vampire Blade")
    public static Item VampireBlade;

    @AEItem(name = "Flaming Sword")
    public static Item FlamingSword;

    @AEItem(name = "Holy Sword")
    public static Item HolySword;

    @AEItem(name = "Lightning Sword")
    public static Item LightningSword;

    @AEItem(name = "Lightning Knife")
    public static Item LightningKnife;

    @AEItem(names = {"Blue Gummie Swet", "Golden Gummie Swet"})
    public static Item GummieSwet;

    @AEItem(name = "Hammer of Notch")
    public static Item HammerOfNotch;

    @AEItem(name = "Cold Cloud Parachute")
    public static Item CloudParachute;

    @AEItem(name = "Golden Cloud Parachute")
    public static Item GoldenCloudParachute;

    @AEItem(name = "Purple Cloud Parachute")
    public static Item PurpleCloudParachute;

    @AEItem(name = "Green Cloud Parachute")
    public static Item GreenCloudParachute;

    @AEItem(name = "Blue Cloud Parachute")
    public static Item BlueCloudParachute;

    @AEItem(name = "Shard of Life")
    public static Item ShardOfLife;

    @AEItem(name = "Valkyrie Cape")
    public static Item ValkyrieCape;

    @AEItem(name = "Valkyrie Lance")
    public static Item ValkyrieLance;

    @AEItem(name = "Iron Ring")
    public static Item IronRing;

    @AEItem(name = "Golden Ring")
    public static Item GoldenRing;

    @AEItem(name = "Zanite Ring")
    public static Item ZaniteRing;

    @AEItem(name = "Iron Pendant")
    public static Item IronPendant;

    @AEItem(name = "Golden Pendant")
    public static Item GoldenPendant;

    @AEItem(name = "Zanite Pendant")
    public static Item ZanitePendant;

    @AEItem(name = "Swet Cape")
    public static Item SwetCape;

    @AEItem(name = "Leather Gloves")
    public static Item LeatherGloves;

    @AEItem(name = "Iron Gloves")
    public static Item IronGloves;

    @AEItem(name = "Golden Gloves")
    public static Item GoldenGloves;

    @AEItem(name = "Diamond Gloves")
    public static Item DiamondGloves;

    @AEItem(name = "Zanite Gloves")
    public static Item ZaniteGloves;

    @AEItem(name = "Zanite Helmet")
    public static Item ZaniteHelmet;

    @AEItem(name = "Zanite Chestplate")
    public static Item ZaniteChestplate;

    @AEItem(name = "Zanite Leggings")
    public static Item ZaniteLeggings;

    @AEItem(name = "Zanite Boots")
    public static Item ZaniteBoots;

    @AEItem(name = "Gravitite Gloves")
    public static Item GravititeGloves;

    @AEItem(name = "Gravitite Helmet")
    public static Item GravititeHelmet;

    @AEItem(name = "Gravitite Chestplate")
    public static Item GravititeChestplate;

    @AEItem(name = "Gravitite Leggings")
    public static Item GravititeLeggings;

    @AEItem(name = "Gravitite Boots")
    public static Item GravititeBoots;

    @AEItem(name = "Phoenix Gloves")
    public static Item PhoenixGloves;

    @AEItem(name = "Phoenix Helmet")
    public static Item PhoenixHelmet;

    @AEItem(name = "Phoenix Chestplate")
    public static Item PhoenixChestplate;

    @AEItem(name = "Phoenix Leggings")
    public static Item PhoenixLeggings;

    @AEItem(name = "Phoenix Boots")
    public static Item PhoenixBoots;

    @AEItem(name = "Obsidian Gloves")
    public static Item ObsidianGloves;

    @AEItem(name = "Obsidian Chestplate")
    public static Item ObsidianChestplate;

    @AEItem(name = "Obsidian Helmet")
    public static Item ObsidianHelmet;

    @AEItem(name = "Obsidian Leggings")
    public static Item ObsidianLeggings;

    @AEItem(name = "Obsidian Boots")
    public static Item ObsidianBoots;

    @AEItem(name = "Neptune Gloves")
    public static Item NeptuneGloves;

    @AEItem(name = "Neptune Helmet")
    public static Item NeptuneHelmet;

    @AEItem(name = "Neptune Chestplate")
    public static Item NeptuneChestplate;

    @AEItem(name = "Neptune Leggings")
    public static Item NeptuneLeggings;

    @AEItem(name = "Neptune Boots")
    public static Item NeptuneBoots;

    @AEItem(name = "Regeneration Stone")
    public static Item RegenerationStone;

    @AEItem(name = "Invisibility Cloak")
    public static Item InvisibilityCloak;

    @AEItem(name = "Agility Cape")
    public static Item AgilityCape;

    @AEItem(name = "White Cape")
    public static Item WhiteCape;

    @AEItem(name = "Red Cape")
    public static Item RedCape;

    @AEItem(name = "Yellow Cape")
    public static Item YellowCape;

    @AEItem(name = "Blue Cape")
    public static Item BlueCape;

    @AEItem(name = "Valkyrie Pickaxe")
    public static Item ValkyriePickaxe;

    @AEItem(name = "Valkyrie Axe")
    public static Item ValkyrieAxe;

    @AEItem(name = "Valkyrie Shovel")
    public static Item ValkyrieShovel;

    @AEItem(name = "Healing Stone")
    public static Item HealingStone;

    @AEItem(name = "Ice Ring")
    public static Item IceRing;

    @AEItem(name = "Ice Pendant")
    public static Item IcePendant;

    @AEItem(name = "Blue Berry")
    public static Item BlueBerry;

    @AEItem(name = "Gingerbread Man")
    public static Item GingerBreadMan;

    @AEItem(name = "Candy Cane")
    public static Item CandyCane;

    @AEItem(name = "Candy Cane Sword")
    public static Item CandyCaneSword;

    @AEItem(name = "White Apple")
    public static Item WhiteApple;

    @AEItem(name = "Swetty Ball")
    public static Item SwettyBall;

    @AEItem(name = "Sentry Boots")
    public static Item SentryBoots;

    @AEItem(name = "Valkyrie Music Disk")
    public static Item ValkyrieMusicDisk;

    @AEItem(name = "Valkyrie Helmet")
    public static Item ValkyrieHelmet;

    @AEItem(name = "Valkyrie Chestplate")
    public static Item ValkyrieChestplate;

    @AEItem(name = "Valkyrie Leggings")
    public static Item ValkyrieLeggings;

    @AEItem(name = "Valkyrie Boots")
    public static Item ValkyrieBoots;

    @AEItem(name = "Valkyrie Gloves")
    public static Item ValkyrieGloves;

    @AEItem(name = "Dexterity Cape")
    public static Item DexterityCape;

    @AEItem(name = "Swetty Pendant")
    public static Item SwettyPendant;

    @AEItem(name = "Deadmau5 Ears")
    public static Item Deadmau5Ears;

    @AEItem(name = "Enchanted Berry")
    public static Item EnchantedBerry;

    @AEItem(name = "Rainbow Strawberry")
    public static Item Strawberry;

    @AEItem(name = "Continuum Orb")
    public static Item ContinuumOrb;

    @AEItem(name = "Orange")
    public static Item Orange;

    @AEItem(name = "Labyrinth Music Disk")
    public static Item LabyrinthMusicDisk;

    @AEItem(name = "Moa Music Disk")
    public static Item MoaMusicDisk;

    @AEItem(name = "Aerwhale Music Disk")
    public static Item AerwhaleMusicDisk;

    @AEItem(name = "Crystal EXP Bottle")
    public static Item CrystalBottle;

    @AEItem(name = "Piggie Bank")
    public static Item PiggieBank;

    @AEItem(name = "Wyndberry")
    public static Item Wyndberry;
    public static HashMap spritesPath = new HashMap();

    public static void init()
    {
        spritesPath.put("ROOT", "/net/aetherteam/aether/client/sprites/");
        spritesPath.put("ARMOR", "/net/aetherteam/aether/client/sprites/armor/");
        spritesPath.put("CAPES", "/net/aetherteam/aether/client/sprites/capes/");
        VictoryMedal = new ItemAether(makeID("VictoryMedal", 17000)).setIconName("Victory Medal").setMaxStackSize(10).setCreativeTab(Aether.misc);
        Key = new ItemAetherKey(makeID("Key", 17001)).setCreativeTab(Aether.misc);
        MoaEgg = new ItemMoaEgg(makeID("MoaEgg", 17002)).setIconName("Moa Egg").setCreativeTab(Aether.misc);
        AetherMusicDisk = new ItemAetherMusicDisk(makeID("AetherMusicDisk", 17003), "Aether Tune", "Noisestorm", "Aether Tune").setIconName("Aether Music Disk").setCreativeTab(Aether.misc);
        GoldenAmber = new ItemAether(makeID("GoldenAmber", 17004)).setIconName("Golden Amber").setCreativeTab(Aether.materials);
        AechorPetal = new ItemAether(makeID("AechorPetal", 17005)).setIconName("Aechor Petal").setCreativeTab(Aether.materials);
        SkyrootStick = new ItemAether(makeID("SkyrootStick", 17006)).setIconName("Skyroot Stick").setCreativeTab(Aether.materials);
        Dart = new ItemDart(makeID("Dart", 17007)).setCreativeTab(Aether.weapons);
        DartShooter = new ItemDartShooter(makeID("DartShooter", 17008)).setIconName("Dart Shooter").setCreativeTab(Aether.weapons);
        AmbrosiumShard = new ItemAether(makeID("AmbrosiumShard", 17009)).setIconName("Ambrosium Shard").setCreativeTab(Aether.materials);
        ZaniteGemstone = new ItemAether(makeID("ZaniteGemstone", 17010)).setIconName("Zanite Gemstone").setCreativeTab(Aether.materials);
        SkyrootBucket = new ItemSkyrootBucket(makeID("SkyrootBucket", 17011)).setCreativeTab(Aether.misc);
        SkyrootPickaxe = new ItemSkyrootPickaxe(makeID("SkyrootPickaxe", 17012), EnumToolMaterial.WOOD).setIconName("Skyroot Pickaxe").setCreativeTab(Aether.tools);
        HolystonePickaxe = new ItemHolystonePickaxe(makeID("HolystonePickaxe", 17013), EnumToolMaterial.STONE).setIconName("Holystone Pickaxe").setCreativeTab(Aether.tools);
        ZanitePickaxe = new ItemZanitePickaxe(makeID("ZanitePickaxe", 17014), EnumToolMaterial.IRON).setIconName("Zanite Pickaxe").setCreativeTab(Aether.tools);
        GravititePickaxe = new ItemGravititePickaxe(makeID("GravititePickaxe", 17015), EnumToolMaterial.EMERALD).setIconName("Gravitite Pickaxe").setCreativeTab(Aether.tools);
        SkyrootShovel = new ItemSkyrootShovel(makeID("SkyrootShovel", 17016), EnumToolMaterial.WOOD).setIconName("Skyroot Shovel").setCreativeTab(Aether.tools);
        HolystoneShovel = new ItemHolystoneShovel(makeID("HolystoneShovel", 17017), EnumToolMaterial.STONE).setIconName("Holystone Shovel").setCreativeTab(Aether.tools);
        ZaniteShovel = new ItemZaniteShovel(makeID("ZaniteShovel", 17018), EnumToolMaterial.IRON).setIconName("Zanite Shovel").setCreativeTab(Aether.tools);
        GravititeShovel = new ItemGravititeShovel(makeID("GravititeShovel", 17019), EnumToolMaterial.EMERALD).setIconName("Gravitite Shovel").setCreativeTab(Aether.tools);
        SkyrootAxe = new ItemSkyrootAxe(makeID("SkyrootAxe", 17020), EnumToolMaterial.WOOD).setIconName("Skyroot Axe").setCreativeTab(Aether.tools);
        HolystoneAxe = new ItemHolystoneAxe(makeID("HolystoneAxe", 17021), EnumToolMaterial.STONE).setIconName("Holystone Axe").setCreativeTab(Aether.tools);
        ZaniteAxe = new ItemZaniteAxe(makeID("ZaniteAxe", 17022), EnumToolMaterial.IRON).setIconName("Zanite Axe").setCreativeTab(Aether.tools);
        GravititeAxe = new ItemGravititeAxe(makeID("GravititeAxe", 17023), EnumToolMaterial.EMERALD).setIconName("Gravitite Axe").setCreativeTab(Aether.tools);
        SkyrootSword = new ItemSkyrootSword(makeID("SkyrootSword", 17024), EnumToolMaterial.WOOD).setIconName("Skyroot Sword").setCreativeTab(Aether.weapons);
        HolystoneSword = new ItemHolystoneSword(makeID("HolystoneSword", 17025), EnumToolMaterial.STONE).setIconName("Holystone Sword").setCreativeTab(Aether.weapons);
        ZaniteSword = new ItemZaniteSword(makeID("ZaniteSword", 17026), EnumToolMaterial.IRON).setIconName("Zanite Sword").setCreativeTab(Aether.weapons);
        GravititeSword = new ItemGravititeSword(makeID("GravititeSword", 17027), EnumToolMaterial.EMERALD).setIconName("Gravitite Sword").setCreativeTab(Aether.weapons);
        IronBubble = new ItemIronBubble(makeID("IronBubble", 17028), 0, 0, 7).setIconName("Iron Bubble").setCreativeTab(Aether.accessories);
        PigSlayer = new ItemPigSlayer(makeID("PigSlayer", 17029)).setIconName("Pig Slayer").setCreativeTab(Aether.weapons);
        VampireBlade = new ItemVampireBlade(makeID("VampireBlade", 17030)).setIconName("Vampire Blade").setCreativeTab(Aether.weapons);
        FlamingSword = new ItemElementalSword(makeID("FlamingSword", 17031), AetherEnumElement.Fire).setIconName("Flaming Sword").setCreativeTab(Aether.weapons);
        HolySword = new ItemElementalSword(makeID("HolySword", 17032), AetherEnumElement.Holy).setIconName("Holy Sword").setCreativeTab(Aether.weapons);
        LightningSword = new ItemElementalSword(makeID("FlamingSword", 17033), AetherEnumElement.Lightning).setIconName("Lightning Sword").setCreativeTab(Aether.weapons);
        LightningKnife = new ItemLightningKnife(makeID("LightningKnife", 17034)).setIconName("Lightning Knife").setCreativeTab(Aether.weapons);
        GummieSwet = new ItemGummieSwet(makeID("GummieSwet", 17035)).setIconName("Gummie Swet").setCreativeTab(Aether.food);
        HammerOfNotch = new ItemHammerOfNotch(makeID("HammerOfNotch", 17036)).setIconName("Hammer of Notch").setCreativeTab(Aether.weapons);
        CloudParachute = new ItemCloudParachute(makeID("CloudParachute", 17038), 0).setIconName("Cloud Parachute").setCreativeTab(Aether.misc);
        GoldenCloudParachute = new ItemCloudParachute(makeID("GoldenCloudParachute", 17039), 20).setIconName("Golden Cloud Parachute").setCreativeTab(Aether.misc);
        ShardOfLife = new ItemShardOfLife(makeID("ShardOfLife", 17041)).setIconName("Shard of Life").setCreativeTab(Aether.misc);
        ValkyrieCape = new ItemValkyrieCape(makeID("ValkyrieCape", 17042), 0, (String)spritesPath.get("CAPES") + "cape_valkyrie.png", 5).setIconName("Valkyrie Cape").setCreativeTab(Aether.capes);
        ValkyrieLance = new ItemValkyrieLance(makeID("ValkyrieLance", 17043), EnumToolMaterial.EMERALD).setIconName("Valkyrie Lance").setCreativeTab(Aether.weapons);
        IronRing = new ItemAccessory(makeID("IronRing", 17044), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8).setIconName("Iron Ring").setCreativeTab(Aether.accessories);
        GoldenRing = new ItemAccessory(makeID("GoldenRing", 17045), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8).setIconName("Golden Ring").setCreativeTab(Aether.accessories);
        ZaniteRing = new ItemAccessory(makeID("ZaniteRing", 17046), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8).setIconName("Zanite Ring").setCreativeTab(Aether.accessories);
        IronPendant = new ItemAccessory(makeID("IronPendant", 17047), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4).setIconName("Iron Pendant").setCreativeTab(Aether.accessories);
        GoldenPendant = new ItemAccessory(makeID("GoldenPendant", 17048), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4).setIconName("Golden Pendant").setCreativeTab(Aether.accessories);
        ZanitePendant = new ItemAccessory(makeID("ZanitePendant", 17049), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4).setIconName("Zanite Pendant").setCreativeTab(Aether.accessories);
        SwetCape = new ItemAccessory(makeID("SwetCape", 17051), 0, (String)spritesPath.get("CAPES") + "cape_swet.png", 5).setIconName("Swet Cape").setCreativeTab(Aether.capes);
        LeatherGloves = new ItemAccessory(makeID("LeatherGloves", 17052), 0, (String)spritesPath.get("ARMOR") + "Leather.png", 10).setIconName("Leather Gloves").setCreativeTab(Aether.armour);
        IronGloves = new ItemAccessory(makeID("IronGloves", 17053), 2, (String)spritesPath.get("ARMOR") + "Accessories.png", 10).setIconName("Iron Gloves").setCreativeTab(Aether.armour);
        GoldenGloves = new ItemAccessory(makeID("GoldenGloves", 17054), 1, (String)spritesPath.get("ARMOR") + "Gold.png", 10).setIconName("Golden Gloves").setCreativeTab(Aether.armour);
        DiamondGloves = new ItemAccessory(makeID("DiamondGloves", 17055), 3, (String)spritesPath.get("ARMOR") + "Diamond.png", 10).setIconName("Diamond Gloves").setCreativeTab(Aether.armour);
        ZaniteGloves = new ItemAccessory(makeID("ZaniteGloves", 17056), 2, (String)spritesPath.get("ARMOR") + "Zanite.png", 10).setIconName("Zanite Gloves").setCreativeTab(Aether.armour);
        ZaniteHelmet = new ItemColouredArmor(makeID("ZaniteHelmet", 17057), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 0, 16777215, "Zanite").setIconName("Zanite Helmet").setCreativeTab(Aether.armour);
        ZaniteChestplate = new ItemColouredArmor(makeID("ZaniteChestplate", 17058), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 1, 16777215, "Zanite").setIconName("Zanite Chestplate").setCreativeTab(Aether.armour);
        ZaniteLeggings = new ItemColouredArmor(makeID("ZaniteLeggings", 17059), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 2, 16777215, "Zanite").setIconName("Zanite Leggings").setCreativeTab(Aether.armour);
        ZaniteBoots = new ItemColouredArmor(makeID("ZaniteBoots", 17060), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Zanite"), 3, 16777215, "Zanite").setIconName("Zanite Boots").setCreativeTab(Aether.armour);
        GravititeGloves = new ItemAccessory(makeID("GravititeGloves", 17061), 3, (String)spritesPath.get("ARMOR") + "Gravitite.png", 10, 16777215, false).setIconName("Gravitite Gloves").setCreativeTab(Aether.armour);
        GravititeHelmet = new ItemColouredArmor(makeID("GravititeHelmet", 17062), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 0, 16777215, "Gravitite").setIconName("Gravitite Helmet").setCreativeTab(Aether.armour);
        GravititeChestplate = new ItemColouredArmor(makeID("GravititeChestplate", 17063), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 1, 16777215, "Gravitite").setIconName("Gravitite Chestplate").setCreativeTab(Aether.armour);
        GravititeLeggings = new ItemColouredArmor(makeID("GravititeLeggings", 17064), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 2, 16777215, "Gravitite").setIconName("Gravitite Leggings").setCreativeTab(Aether.armour);
        GravititeBoots = new ItemColouredArmor(makeID("GravititeBoots", 17065), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Gravitite"), 3, 16777215, "Gravitite").setIconName("Gravitite Boots").setCreativeTab(Aether.armour);
        PhoenixGloves = new ItemAccessory(makeID("PhoenixGloves", 17066), 3, (String)spritesPath.get("ARMOR") + "Phoenix.png", 10, 16777215, false).setIconName("Phoenix Gloves").setCreativeTab(Aether.armour);
        PhoenixHelmet = new ItemAetherArmor(makeID("PhoenixHelmet", 17067), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 0, "Phoenix").setIconName("Phoenix Helmet").setCreativeTab(Aether.armour);
        PhoenixChestplate = new ItemAetherArmor(makeID("PhoenixChestplate", 17068), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 1, "Phoenix").setIconName("Phoenix Chestplate").setCreativeTab(Aether.armour);
        PhoenixLeggings = new ItemAetherArmor(makeID("PhoenixLeggings", 17069), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 2, "Phoenix").setIconName("Phoenix Leggings").setCreativeTab(Aether.armour);
        PhoenixBoots = new ItemAetherArmor(makeID("PhoenixBoots", 17070), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Phoenix"), 3, "Phoenix").setIconName("Phoenix Boots").setCreativeTab(Aether.armour);
        ObsidianGloves = new ItemAccessory(makeID("ObsidianGloves", 17071), 4, (String)spritesPath.get("ARMOR") + "Obsidian.png", 10, 16777215, false).setIconName("Obsidian Gloves").setCreativeTab(Aether.armour);
        ObsidianHelmet = new ItemColouredArmor(makeID("ObsidianHelmet", 17072), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 0, 16777215, "Obsidian").setIconName("Obsidian Helmet").setCreativeTab(Aether.armour);
        ObsidianChestplate = new ItemColouredArmor(makeID("ObsidianChestplate", 17073), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 1, 16777215, "Obsidian").setIconName("Obsidian Chestplate").setCreativeTab(Aether.armour);
        ObsidianLeggings = new ItemColouredArmor(makeID("ObsidianLeggings", 17074), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 2, 16777215, "Obsidian").setIconName("Obsidian Leggings").setCreativeTab(Aether.armour);
        ObsidianBoots = new ItemColouredArmor(makeID("ObsidianBoots", 17075), CommonProxy.OBSIDIAN, Aether.proxy.addArmor("Obsidian"), 3, 16777215, "Obsidian").setIconName("Obsidian Boots").setCreativeTab(Aether.armour);
        NeptuneGloves = new ItemAccessory(makeID("NeptuneGloves", 17076), 3, (String)spritesPath.get("ARMOR") + "Neptune.png", 10, 16777215).setIconName("Neptune Gloves").setCreativeTab(Aether.armour);
        NeptuneHelmet = new ItemNeptuneArmor(makeID("NeptuneHelmet", 17077), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 0, 16777215, "Neptune").setIconName("Neptune Helmet").setCreativeTab(Aether.armour);
        NeptuneChestplate = new ItemNeptuneArmor(makeID("NeptuneChestplate", 17078), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 1, 16777215, "Neptune").setIconName("Neptune Chestplate").setCreativeTab(Aether.armour);
        NeptuneLeggings = new ItemNeptuneArmor(makeID("NeptuneLeggings", 17079), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 2, 16777215, "Neptune").setIconName("Neptune Leggings").setCreativeTab(Aether.armour);
        NeptuneBoots = new ItemNeptuneArmor(makeID("NeptuneBoots", 17080), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Neptune"), 3, 16777215, "Neptune").setIconName("Neptune Boots").setCreativeTab(Aether.armour);
        RegenerationStone = new ItemRegenerationStone(makeID("RegenerationStone", 17081), 0, 0, 7).setIconName("Regeneration Stone").setCreativeTab(Aether.accessories);
        InvisibilityCloak = new ItemInvisibilityCloak(makeID("InvisibilityCloak", 17082), 0, 0, 5).setIconName("Invisibility Cloak").setCreativeTab(Aether.capes);
        AgilityCape = new ItemAgilityCape(makeID("AgilityCape", 17083), 0, (String)spritesPath.get("CAPES") + "cape_agility.png", 5).setIconName("Agility Cape").setCreativeTab(Aether.capes);
        WhiteCape = new ItemAccessory(makeID("WhiteCape", 17084), 0, (String)spritesPath.get("CAPES") + "cape_white.png", 5).setIconName("White Cape").setCreativeTab(Aether.capes);
        RedCape = new ItemAccessory(makeID("RedCape", 17085), 0, (String)spritesPath.get("CAPES") + "cape_red.png", 5).setIconName("Red Cape").setCreativeTab(Aether.capes);
        YellowCape = new ItemAccessory(makeID("YellowCape", 17086), 0, (String)spritesPath.get("CAPES") + "cape_yellow.png", 5).setIconName("Yellow Cape").setCreativeTab(Aether.capes);
        BlueCape = new ItemAccessory(makeID("BlueCape", 17087), 0, (String)spritesPath.get("CAPES") + "cape_blue.png", 5).setIconName("Blue Cape").setCreativeTab(Aether.capes);
        ValkyriePickaxe = new ItemValkyriePickaxe(makeID("ValkyriePickaxe", 17088), EnumToolMaterial.EMERALD).setIconName("Valkyrie Pickaxe").setCreativeTab(Aether.tools);
        ValkyrieAxe = new ItemValkyrieAxe(makeID("ValkyrieAxe", 17089), EnumToolMaterial.EMERALD).setIconName("Valkyrie Axe").setCreativeTab(Aether.tools);
        ValkyrieShovel = new ItemValkyrieShovel(makeID("ValkyrieShovel", 17090), EnumToolMaterial.EMERALD).setIconName("Valkyrie Shovel").setCreativeTab(Aether.tools);
        HealingStone = new ItemHealingStone(makeID("HealingStone", 17091), 0, 1.2F, false).setIconName("Healing Stone").setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 30, 0, 1.0F).setCreativeTab(Aether.food);
        IceRing = new ItemIceAccessory(makeID("IceRing", 17092), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 8, 9823975).setIconName("Ice Ring").setCreativeTab(Aether.accessories).setMaxDamage(6000);
        IcePendant = new ItemIceAccessory(makeID("IcePendant", 17093), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4, 9823975).setIconName("Ice Pendant").setCreativeTab(Aether.accessories).setMaxDamage(6000);
        BlueBerry = new ItemAetherFood(makeID("BlueBerry", 17094), 2, false).setIconName("Blue Berry").setCreativeTab(Aether.food);
        GingerBreadMan = new ItemAetherFood(makeID("GingerbreadMan", 17095), 2, false).setIconName("Gingerbread Man").setCreativeTab(Aether.food);
        CandyCane = new ItemAetherFood(makeID("CandyCane", 17096), 2, false).setIconName("Candy Cane").setCreativeTab(Aether.food);
        CandyCaneSword = new ItemCandyCaneSword(makeID("CandyCaneSword", 17097), EnumToolMaterial.WOOD).setIconName("Candy Cane Sword").setCreativeTab(Aether.weapons);
        WhiteApple = new ItemPoisonCure(makeID("WhiteApple", 17098), 0, false).setIconName("White Apple").setAlwaysEdible().setCreativeTab(Aether.food);
        SwettyBall = new ItemAether(makeID("SwettyBall", 17099)).setIconName("Swetty Ball").setCreativeTab(Aether.materials);
        SentryBoots = new ItemAetherArmor(makeID("SentryBoots", 17100), EnumArmorMaterial.IRON, Aether.proxy.addArmor("Sentry"), 3, "Sentry").setIconName("Sentry Boots").setCreativeTab(Aether.armour);
        ValkyrieMusicDisk = new ItemAetherMusicDisk(makeID("ValkyrieMusicDisk", 17101), "Ascending Dawn", "Emile van Krieken", "Ascending Dawn").setIconName("Valkyrie Music Disk").setCreativeTab(Aether.misc);
        ValkyrieHelmet = new ItemAetherArmor(makeID("ValkyrieHelmet", 17102), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 0, "Valkyrie").setIconName("Valkyrie Helmet").setCreativeTab(Aether.armour);
        ValkyrieChestplate = new ItemAetherArmor(makeID("ValkyrieChestplate", 17103), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 1, "Valkyrie").setIconName("Valkyrie Chestplate").setCreativeTab(Aether.armour);
        ValkyrieLeggings = new ItemAetherArmor(makeID("ValkyrieLeggings", 17104), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 2, "Valkyrie").setIconName("Valkyrie Leggings").setCreativeTab(Aether.armour);
        ValkyrieBoots = new ItemAetherArmor(makeID("ValkyrieBoots", 17105), EnumArmorMaterial.DIAMOND, Aether.proxy.addArmor("Valkyrie"), 3, "Valkyrie").setIconName("Valkyrie Boots").setCreativeTab(Aether.armour);
        ValkyrieGloves = new ItemAccessory(makeID("ValkyrieGloves", 17106), 3, (String)spritesPath.get("ARMOR") + "Valkyrie.png", 10, 16777215, false).setIconName("Valkyrie Gloves").setCreativeTab(Aether.armour);
        DexterityCape = new ItemDexterityCape(makeID("DexterityCape", 17107), 0, (String)spritesPath.get("CAPES") + "cape_dexterity.png", 5).setIconName("Dexterity Cape").setCreativeTab(Aether.capes);
        SwettyPendant = new ItemAccessory(makeID("SwettyPendant", 17108), 0, (String)spritesPath.get("ARMOR") + "Accessories.png", 4).setIconName("Swetty Pendant").setCreativeTab(Aether.accessories);
        Deadmau5Ears = new ItemAccessory(makeID("Deadmau5Ears", 17109), 0, 0, 7).setIconName("Deadmau5 Ears").setCreativeTab(Aether.accessories);
        EnchantedBerry = new ItemAetherFood(makeID("EnchantedBerry", 17110), 8, false).setIconName("Enchanted Berry").setCreativeTab(Aether.food);
        Strawberry = new ItemAetherFood(makeID("Strawberry", 17112), 8, false).setIconName("Rainbow Strawberry").setCreativeTab(Aether.food);
        ContinuumOrb = new ItemContinuum(makeID("ContinuumOrb", 17113)).setIconName("Continuum Orb").setCreativeTab(Aether.materials);
        Orange = new ItemAetherFood(makeID("Orange", 17114), 4, false).setIconName("Orange").setCreativeTab(Aether.food);
        LabyrinthMusicDisk = new ItemAetherMusicDisk(makeID("LabyrinthMusicDisk", 17115), "Demise", "Moorziey", "Demise").setIconName("Labyrinth Music Disk").setCreativeTab(Aether.misc);
        MoaMusicDisk = new ItemAetherMusicDisk(makeID("MoaMusicDisk", 17116), "Approaches", "Emile van Krieken", "Approaches").setIconName("Moa Music Disk").setCreativeTab(Aether.misc);
        AerwhaleMusicDisk = new ItemAetherMusicDisk(makeID("AerwhaleMusicDisk", 17117), "Aerwhale", "AetherAudio", "Aerwhale").setIconName("Aerwhale Music Disk").setCreativeTab(Aether.misc);
        CrystalBottle = new ItemCrystalBottle(makeID("CrystalBottle", 17118), 0, 0, 7).setIconName("Crystal EXP Bottle").setCreativeTab(Aether.misc);
        PiggieBank = new ItemPiggieBank(makeID("PiggieBank", 17119), 0, 0, 7).setIconName("Piggie Bank").setCreativeTab(Aether.misc);
        PurpleCloudParachute = new ItemCloudParachute(makeID("PurpleCloudParachute", 17120), 0).setIconName("Purple Cloud Parachute").setCreativeTab(Aether.misc);
        GreenCloudParachute = new ItemCloudParachute(makeID("GreenCloudParachute", 17121), 0).setIconName("Green Cloud Parachute").setCreativeTab(Aether.misc);
        BlueCloudParachute = new ItemCloudParachute(makeID("BlueCloudParachute", 17122), 0).setIconName("Blue Cloud Parachute").setCreativeTab(Aether.misc);
        Wyndberry = new ItemAetherFood(makeID("Wyndberry", 17123), 4, false).setIconName("Wyndberry").setCreativeTab(Aether.food);
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

    public static int makeID(String name, int def)
    {
        return Aether.getConfig().getItem(name, def).getInt(def);
    }

    private static void registerItemNames()
    {
        for (Field f : AetherItems.class.getDeclaredFields())
        {
            AEItem ann = (AEItem)f.getAnnotation(AEItem.class);

            if ((ann != null) && (Item.class.isAssignableFrom(f.getType())))
            {
                Item item;

                try
                {
                    item = (Item)f.get(null);
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                    continue;
                }

                String[] names = ann.names();

                if (names.length != 0)
                {
                    for (int i = 0; i < names.length; i++)
                    {
                        LanguageRegistry.addName(new ItemStack(item, 1, i), names[i]);
                    }
                }
                else
                {
                    LanguageRegistry.addName(item, ann.name());
                }
            }
        }

        String[] moaNames = AetherMoaColour.getNames();

        for (int i = 0; i < AetherMoaColour.colours.size(); i++)
        {
            LanguageRegistry.addName(new ItemStack(MoaEgg, 1, i), moaNames[i] + " Moa Egg");
        }

        LanguageRegistry.addName(new ItemStack(SkyrootBucket, 1, Block.waterMoving.blockID), "Skyroot Water Bucket");
    }
}

