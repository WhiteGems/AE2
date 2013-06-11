package net.aetherteam.aether.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import java.lang.reflect.Field;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.items.ItemBlockAercloud;
import net.aetherteam.aether.items.ItemBlockAetherGrass;
import net.aetherteam.aether.items.ItemBlockAetherLog;
import net.aetherteam.aether.items.ItemBlockBronzeDoor;
import net.aetherteam.aether.items.ItemBlockChristmasLeaves;
import net.aetherteam.aether.items.ItemBlockCrystalLeaves;
import net.aetherteam.aether.items.ItemBlockDungeon;
import net.aetherteam.aether.items.ItemBlockDungeonHolystone;
import net.aetherteam.aether.items.ItemBlockEntrance;
import net.aetherteam.aether.items.ItemBlockHolystone;
import net.aetherteam.aether.items.ItemBlockQuicksoil;
import net.aetherteam.aether.items.ItemBlockTrap;
import net.aetherteam.aether.worldgen.AetherGenLargeTree;
import net.aetherteam.aether.worldgen.AetherGenMassiveTree;
import net.aetherteam.aether.worldgen.AetherGenNormalTree;
import net.aetherteam.aether.worldgen.AetherGenPurpleTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class AetherBlocks
{
    @AEBlock(
            name = "Aether Portal")
    public static BlockAetherPortal AetherPortal;
    public static int AetherPortalID = 165;
    @AEBlock(
            name = "Aether Dirt")
    public static Block AetherDirt;
    public static int AetherDirtID = 166;
    @AEBlock(
            names = {"Aether Grass", "\u00a7bEnchanted Aether Grass"},
            itemBlock = ItemBlockAetherGrass.class)
    public static Block AetherGrass;
    public static int AetherGrassID = 167;
    @AEBlock(
            name = "Quicksoil",
            itemBlock = ItemBlockQuicksoil.class)
    public static Block Quicksoil;
    public static int QuicksoilID = 168;
    @AEBlock(
            names = {"Holystone", "Mossy Holystone"},
            itemBlock = ItemBlockHolystone.class)
    public static Block Holystone;
    public static int HolystoneID = 169;
    @AEBlock(
            name = "Icestone")
    public static Block Icestone;
    public static int IcestoneID = 170;
    @AEBlock(
            names = {"Cold Aercloud", "Blue Aercloud", "Golden Aercloud", "Green Aercloud", "Storm Aercloud", "Purple Aercloud"},
            itemBlock = ItemBlockAercloud.class)
    public static Block Aercloud;
    public static int AercloudID = 171;
    @AEBlock(
            name = "Aerogel")
    public static Block Aerogel;
    public static int AerogelID = 172;
    @AEBlock(
            name = "Altar")
    public static Block Altar;
    public static int AltarID = 173;
    @AEBlock(
            name = "Incubator")
    public static Block Incubator;
    public static int IncubatorID = 174;
    @AEBlock(
            names = {"Skyroot Log", "Golden Oak Log"},
            itemBlock = ItemBlockAetherLog.class)
    public static Block AetherLog;
    public static int AetherLogID = 175;
    @AEBlock(
            name = "Skyroot Plank")
    public static Block SkyrootPlank;
    public static int SkyrootPlankID = 176;
    @AEBlock(
            name = "Green Skyroot Leaves")
    public static Block GreenSkyrootLeaves;
    public static int GreenSkyrootLeavesID = 177;
    @AEBlock(
            name = "Golden Oak Leaves")
    public static Block GoldenOakLeaves;
    public static int GoldenOakLeavesID = 178;
    @AEBlock(
            name = "Green Skyroot Sapling")
    public static Block GreenSkyrootSapling;
    public static int GreenSkyrootSaplingID = 179;
    @AEBlock(
            name = "Golden Oak Sapling")
    public static Block GoldenOakSapling;
    public static int GoldenOakSaplingID = 180;
    @AEBlock(
            name = "Ambrosium Ore")
    public static Block AmbrosiumOre;
    public static int AmbrosiumOreID = 181;
    @AEBlock(
            name = "Ambrosium Torch")
    public static Block AmbrosiumTorch;
    public static int AmbrosiumTorchID = 182;
    @AEBlock(
            name = "Zanite Ore")
    public static Block ZaniteOre;
    public static int ZaniteOreID = 183;
    @AEBlock(
            name = "Gravitite Ore")
    public static Block GravititeOre;
    public static int GravititeOreID = 184;
    @AEBlock(
            name = "Enchanted Gravitite")
    public static Block EnchantedGravitite;
    public static int EnchantedGravititeID = 185;
    @AEBlock(
            names = {"Carved Trap", "Angelic Trap", "Hellfire Trap"},
            itemBlock = ItemBlockTrap.class)
    public static Block Trap;
    public static int TrapID = 186;
    @AEBlock(
            name = "Chest Mimic")
    public static Block SkyrootChestMimic;
    public static int SkyrootChestMimicID = 187;
    @AEBlock(
            name = "Treasure Chest")
    public static Block TreasureChest;
    public static int TreasureChestID = 188;
    @AEBlock(
            names = {"Carved Stone", "Angelic Stone", "Hellfire Stone"},
            itemBlock = ItemBlockDungeon.class)
    public static Block DungeonStone;
    public static int DungeonStoneID = 189;
    @AEBlock(
            names = {"Sentry Stone", "Light Angelic Stone", "Light Hellfire Stone"},
            itemBlock = ItemBlockDungeon.class)
    public static Block LightDungeonStone;
    public static int LightDungeonStoneID = 190;
    @AEBlock(
            names = {"Locked Carved Stone", "Locked Angelic Stone", "Locked Hellfire Stone"},
            itemBlock = ItemBlockDungeon.class)
    public static Block LockedDungeonStone;
    public static int LockedDungeonStoneID = 191;
    @AEBlock(
            names = {"Locked Sentry Stone", "Locked Light Angelic Stone", "Locked Light Hellfire Stone"},
            itemBlock = ItemBlockDungeon.class)
    public static Block LockedLightDungeonStone;
    public static int LockedLightDungeonStoneID = 192;
    @AEBlock(
            names = {"Pillar", "Pillar Top", "Pillar Bottom"},
            itemBlock = ItemBlockDungeon.class)
    public static Block Pillar;
    public static int PillarID = 193;
    @AEBlock(
            name = "Zanite Block")
    public static Block ZaniteBlock;
    public static int ZaniteBlockID = 194;
    @AEBlock(
            name = "Quicksoil Glass")
    public static Block QuicksoilGlass;
    public static int QuicksoilGlassID = 195;
    @AEBlock(
            name = "Freezer")
    public static Block Freezer;
    public static int FreezerID = 196;
    @AEBlock(
            name = "White Flower")
    public static Block WhiteFlower;
    public static int WhiteFlowerID = 197;
    @AEBlock(
            name = "Purple Flower")
    public static Block PurpleFlower;
    public static int PurpleFlowerID = 198;
    @AEBlock(
            names = {"Christmas Leaves", "Decorative Leaves"},
            itemBlock = ItemBlockChristmasLeaves.class)
    public static Block ChristmasLeaves;
    public static int ChristmasLeavesID = 199;
    @AEBlock(
            name = "Present")
    public static Block Present;
    public static int PresentID = 200;
    @AEBlock(
            name = "Berry Bush")
    public static Block BerryBush;
    public static int BerryBushID = 201;
    @AEBlock(
            name = "Berry Bush Stem")
    public static Block BerryBushStem;
    public static int BerryBushStemID = 202;
    @AEBlock(
            names = {"Crystal Leaves", "Crystal Fruit Leaves"},
            itemBlock = ItemBlockCrystalLeaves.class)
    public static Block CrystalLeaves;
    public static int CrystalLeavesID = 203;
    @AEBlock(
            name = "Mossy Holystone Stairs")
    public static Block MossyHolystoneStairs;
    public static int MossyHolystoneStairsID = 204;
    @AEBlock(
            name = "Icestone Stairs")
    public static Block IcestoneStairs;
    public static int IcestoneStairsID = 205;
    @AEBlock(
            name = "Skyroot Stairs")
    public static Block SkyrootStairs;
    public static int SkyrootStairsID = 206;
    @AEBlock(
            name = "Carved Stairs")
    public static Block CarvedStairs;
    public static int CarvedStairsID = 207;
    @AEBlock(
            name = "Angelic Stairs")
    public static Block AngelicStairs;
    public static int AngelicStairsID = 208;
    @AEBlock(
            name = "Hellfire Stairs")
    public static Block HellfireStairs;
    public static int HellfireStairsID = 209;
    @AEBlock(
            name = "Holystone Fence")
    public static Block HolystoneWall;
    public static int HolystoneWallID = 210;
    @AEBlock(
            name = "Mossy Holystone Wall")
    public static Block MossyHolystoneWall;
    public static int MossyHolystoneWallID = 211;
    @AEBlock(
            name = "Icestone Wall")
    public static Block IcestoneWall;
    public static int IcestoneWallID = 212;
    @AEBlock(
            name = "Skyroot Fence")
    public static Block SkyrootFence;
    public static int SkyrootFenceID = 213;
    @AEBlock(
            name = "Carved Wall")
    public static Block CarvedWall;
    public static int CarvedWallID = 214;
    @AEBlock(
            name = "Angelic Wall")
    public static Block AngelicWall;
    public static int AngelicWallID = 215;
    @AEBlock(
            name = "Hellfire Wall")
    public static Block HellfireWall;
    public static int HellfireWallID = 216;
    @AEBlock(
            name = "Holystone Brick")
    public static Block HolystoneBrick;
    public static int HolystoneBrickID = 217;
    @AEBlock(
            name = "Blue Skyroot Leaves")
    public static Block BlueSkyrootLeaves;
    public static int BlueSkyrootLeavesID = 218;
    @AEBlock(
            name = "Skyroot Log Wall")
    public static Block SkyrootLogWall;
    public static int SkyrootLogWallID = 219;
    @AEBlock(
            name = "Tall Aether Grass")
    public static Block TallAetherGrass;
    public static int TallAetherGrassID = 220;
    @AEBlock(
            name = "Dark Blue Skyroot Leaves")
    public static Block DarkBlueSkyrootLeaves;
    public static int DarkBlueSkyrootLeavesID = 221;
    @AEBlock(
            name = "Blue Skyroot Sapling")
    public static Block BlueSkyrootSapling;
    public static int BlueSkyrootSaplingID = 222;
    @AEBlock(
            name = "Skyroot Chest")
    public static Block SkyrootChest;
    public static int SkyrootChestID = 223;
    @AEBlock(
            name = "Bronze Door Controller")
    public static Block BronzeDoorController;
    public static int BronzeDoorControllerID = 224;
    @AEBlock(
            name = "Purple Skyroot Leaves")
    public static Block PurpleSkyrootLeaves;
    public static int PurpleSkyrootLeavesID = 225;
    @AEBlock(
            name = "Purple Skyroot Sapling")
    public static Block PurpleSkyrootSapling;
    public static int PurpleSkyrootSaplingID = 226;
    @AEBlock(
            name = "Orange Fruit Tree Sapling")
    public static Block BlockOrangeTree;
    public static int OrangeFruitTreeID = 227;
    @AEBlock(
            names = {"Bronze Door", "Bronze Door Lock"},
            itemBlock = ItemBlockBronzeDoor.class)
    public static Block BronzeDoor;
    public static int BronzeDoorID = 228;
    @AEBlock(
            name = "Continuum Ore")
    public static Block ContinuumOre;
    public static int ContinuumOreID = 229;
    @AEBlock(
            name = "Dark Blue Skyroot Sapling")
    public static Block DarkBlueSkyrootSapling;
    public static int DarkBlueSkyrootSaplingID = 230;
    @AEBlock(
            name = "Skyroot Crafting Table")
    public static Block SkyrootCraftingTable;
    public static int SkyrootCraftingTableID = 231;
    @AEBlock(
            names = {"Dungeon Entrance", "Dungeon Entrance Lock"},
            itemBlock = ItemBlockEntrance.class)
    public static Block DungeonEntrance;
    public static int DungeonEntranceID = 232;
    @AEBlock(
            name = "Dungeon Entrance Controller")
    public static Block DungeonEntranceController;
    public static int DungeonEntranceControllerID = 233;
    @AEBlock(
            names = {"Dungeon Holystone", "Dungeon Mossy Holystone"},
            itemBlock = ItemBlockDungeonHolystone.class)
    public static Block DungeonHolystone;
    public static int DungeonHolystoneID = 234;
    @AEBlock(
            name = "Locked Carved Stone Stairs")
    public static Block CarvedDungeonStairs;
    public static int CarvedDungeonStairsID = 235;
    @AEBlock(
            name = "Locked Carved Stone Wall")
    public static Block CarvedDungeonWall;
    public static int CarvedDungeonWallID = 236;
    @AEBlock(
            name = "Holystone Stairs")
    public static Block HolystoneStairs;
    public static int HolystoneStairsID = 237;
    @AEBlock(
            name = "Cold Fire")
    public static BlockColdFire ColdFire;
    public static int ColdFireID = 238;
    public static int altarRenderId;
    public static int treasureChestRenderId;
    public static int skyrootChestRenderId;
    public static int entranceRenderId;
    public static String terrainCat = "Aether Terrain";

    public static void init()
    {
        AetherPortal = (BlockAetherPortal) (new BlockAetherPortal(makeTerrainID(terrainCat, "AetherPortal", AetherPortalID))).setUnlocalizedName("Aether:Aether Portal").setLightValue(0.75F).setCreativeTab(Aether.blocks);
        AetherDirt = (new BlockAetherDirt(makeTerrainID(terrainCat, "Aether Dirt", AetherDirtID))).setIconName("Aether Dirt").setCreativeTab(Aether.blocks);
        AetherGrass = (new BlockAetherGrass(makeTerrainID(terrainCat, "Aether Grass", AetherGrassID))).setIconName("Aether Grass").setCreativeTab(Aether.blocks);
        Quicksoil = (new BlockQuicksoil(makeTerrainID(terrainCat, "Quicksoil", QuicksoilID))).setIconName("Quicksoil").setCreativeTab(Aether.blocks);
        Holystone = (new BlockHolystone(makeTerrainID(terrainCat, "Holystone", HolystoneID))).setIconName("Holystone").setCreativeTab(Aether.blocks);
        Icestone = (new BlockIcestone(makeID("Icestone", IcestoneID))).setIconName("Icestone").setCreativeTab(Aether.blocks);
        Aercloud = (new BlockAercloud(makeID("Aercloud", AercloudID))).setIconName("Aercloud").setCreativeTab(Aether.blocks);
        Aerogel = (new BlockAerogel(makeID("Aerogel", AerogelID))).setIconName("Aerogel").setCreativeTab(Aether.blocks);
        Altar = (new BlockAltar(makeID("Altar", AltarID))).setIconName("Altar").setCreativeTab(Aether.blocks);
        Incubator = (new BlockIncubator(makeID("Incubator", IncubatorID))).setIconName("Incubator").setCreativeTab(Aether.blocks);
        AetherLog = (new BlockAetherLog(makeID("Skyroot Log", AetherLogID))).setIconName("Skyroot Log").setCreativeTab(Aether.blocks);
        SkyrootPlank = (new BlockAetherPlank(makeID("Skyroot Plank", SkyrootPlankID), Material.wood)).setIconName("Skyroot Plank").setCreativeTab(Aether.blocks);
        GreenSkyrootLeaves = (new BlockAetherLeaves(makeID("Green Skyroot Leaves", GreenSkyrootLeavesID), GreenSkyrootSaplingID)).setIconName("Green Skyroot Leaves").setCreativeTab(Aether.blocks);
        GoldenOakLeaves = (new BlockAetherLeaves(makeID("Golden Oak Leaves", GoldenOakLeavesID), GoldenOakSaplingID)).setIconName("Golden Oak Leaves").setCreativeTab(Aether.blocks);
        GreenSkyrootSapling = (new BlockAetherSapling(GreenSkyrootSaplingID, new AetherGenNormalTree(GreenSkyrootLeaves.blockID, AetherLog.blockID, 0))).setIconName("Green Skyroot Sapling").setCreativeTab(Aether.blocks);
        GoldenOakSapling = (new BlockAetherSapling(GoldenOakSaplingID, new AetherGenLargeTree(GoldenOakLeaves.blockID, AetherLog.blockID, 2))).setIconName("Golden Oak Sapling").setCreativeTab(Aether.blocks);
        AmbrosiumOre = (new BlockAmbrosiumOre(makeID("Ambrosium Ore", AmbrosiumOreID))).setIconName("Ambrosium Ore").setCreativeTab(Aether.blocks);
        AmbrosiumTorch = (new BlockAmbrosiumTorch(makeID("Ambrosium Torch", AmbrosiumTorchID))).setIconName("Ambrosium Torch").setCreativeTab(Aether.blocks);
        ZaniteOre = (new BlockZaniteOre(makeID("Zanite Ore", ZaniteOreID))).setIconName("Zanite Ore").setCreativeTab(Aether.blocks);
        GravititeOre = (new BlockFloating(makeID("Gravitite Ore", GravititeOreID), false)).setIconName("Gravitite Ore").setCreativeTab(Aether.blocks);
        EnchantedGravitite = (new BlockEnchantedGravitite(makeID("Enchanted Gravitite", EnchantedGravititeID), true)).setIconName("Enchanted Gravitite").setCreativeTab(Aether.blocks);
        Trap = (new BlockTrap(makeID("Trap", TrapID))).setIconName("Trap");
        SkyrootChestMimic = (new BlockChestMimic(makeID("Chest Mimic", SkyrootChestMimicID))).setIconName("Chest Mimic").setCreativeTab(Aether.blocks);
        TreasureChest = (new BlockTreasureChest(makeID("Treasure Chest", TreasureChestID), 29)).setIconName("Treasure Chest").setCreativeTab(Aether.blocks);
        DungeonStone = (new BlockDungeon(makeID("Dungeon Stone", DungeonStoneID), 0.5F, 0.0F)).setIconName("Dungeon Stone").setCreativeTab(Aether.blocks);
        LightDungeonStone = (new BlockDungeon(makeID("Light Dungeon Stone", LightDungeonStoneID), 0.5F, 0.75F)).setIconName("Light Dungeon Stone").setCreativeTab(Aether.blocks);
        LockedDungeonStone = (new BlockDungeon(makeID("Locked Dungeon Stone", LockedDungeonStoneID), -1.0F, 0.0F, 1000000.0F)).setIconName("Locked Dungeon Stone").setCreativeTab(Aether.blocks);
        LockedLightDungeonStone = (new BlockDungeon(makeID("Locked Light Dungeon Stone", LockedLightDungeonStoneID), -1.0F, 0.5F, 1000000.0F)).setIconName("Locked Light Dungeon Stone").setCreativeTab(Aether.blocks);
        Pillar = (new BlockPillar(makeID("Pillar", PillarID))).setIconName("Pillar").setCreativeTab(Aether.blocks);
        ZaniteBlock = (new BlockZanite(makeID("Zanite Block", ZaniteBlockID))).setIconName("Zanite Block").setCreativeTab(Aether.blocks);
        QuicksoilGlass = (new BlockQuicksoilGlass(makeID("Quicksoil Glass", QuicksoilGlassID))).setIconName("Quicksoil Glass").setCreativeTab(Aether.blocks);
        Freezer = (new BlockFreezer(makeID("Freezer", FreezerID))).setIconName("Freezer").setCreativeTab(Aether.blocks);
        WhiteFlower = (new BlockAetherFlower(makeID("White Flower", WhiteFlowerID))).setIconName("White Flower").setCreativeTab(Aether.blocks);
        PurpleFlower = (new BlockAetherFlower(makeID("Purple Flower", PurpleFlowerID))).setIconName("Purple Flower").setCreativeTab(Aether.blocks);
        ChristmasLeaves = (new BlockChristmasLeaves(makeID("Christmas Leaves", ChristmasLeavesID))).setIconName("Christmas Leaves").setCreativeTab(Aether.blocks);
        Present = (new BlockPresent(makeID("Present", PresentID))).setIconName("Present").setCreativeTab(Aether.blocks);
        BerryBush = (new BlockBerryBush(makeID("Berry Bush", BerryBushID))).setIconName("Berry Bush").setCreativeTab(Aether.blocks);
        BerryBushStem = (new BlockBerryBushStem(makeID("Berry Bush Stem", BerryBushStemID))).setIconName("Berry Bush Stem").setCreativeTab(Aether.blocks);
        CrystalLeaves = (new BlockCrystalLeaves(makeID("Crystal Leaves", CrystalLeavesID))).setIconName("Crystal Leaves").setCreativeTab(Aether.blocks);
        HolystoneStairs = (new BlockAetherStairs(makeID("Holystone Stairs", HolystoneStairsID), Holystone, 0)).setIconName("Holystone Stairs").setCreativeTab(Aether.blocks);
        MossyHolystoneStairs = (new BlockAetherStairs(makeID("Mossy Holystone Stairs", MossyHolystoneStairsID), Holystone, 2)).setIconName("Mossy Holystone Stairs").setCreativeTab(Aether.blocks);
        IcestoneStairs = (new BlockAetherStairs(makeID("Icestone Stairs", IcestoneStairsID), Icestone, 0)).setIconName("Icestone Stairs").setCreativeTab(Aether.blocks);
        SkyrootStairs = (new BlockAetherStairs(makeID("Skyroot Stairs", SkyrootStairsID), SkyrootPlank, 0)).setIconName("Skyroot Stairs").setCreativeTab(Aether.blocks);
        CarvedStairs = (new BlockAetherStairs(makeID("Carved Stone Stairs", CarvedStairsID), DungeonStone, 0)).setIconName("Carved Stone Stairs").setCreativeTab(Aether.blocks);
        AngelicStairs = (new BlockAetherStairs(makeID("Angelic Stone Stairs", AngelicStairsID), DungeonStone, 1)).setIconName("Angelic Stone Stairs").setCreativeTab(Aether.blocks);
        HellfireStairs = (new BlockAetherStairs(makeID("Hellfire Stone Stairs", HellfireStairsID), DungeonStone, 2)).setIconName("Hellfire Stone Stairs").setCreativeTab(Aether.blocks);
        HolystoneWall = (new BlockAetherWall(makeID("Holystone Wall", HolystoneWallID), Holystone, 0)).setIconName("Holystone Wall").setCreativeTab(Aether.blocks);
        MossyHolystoneWall = (new BlockAetherWall(makeID("Mossy Holystone Wall", MossyHolystoneWallID), Holystone, 2)).setIconName("Mossy Holystone Wall").setCreativeTab(Aether.blocks);
        IcestoneWall = (new BlockAetherWall(makeID("Icestone Wall", IcestoneWallID), Icestone, 0)).setIconName("Icestone Wall").setCreativeTab(Aether.blocks);
        SkyrootFence = (new BlockAetherFence(makeID("Skyroot Fence", SkyrootFenceID), SkyrootPlank, 0, Material.wood)).setIconName("Skyroot Fence").setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(Aether.blocks);
        CarvedWall = (new BlockAetherWall(makeID("Carved Stone Wall", CarvedWallID), DungeonStone, 0)).setIconName("Carved Stone").setCreativeTab(Aether.blocks);
        AngelicWall = (new BlockAetherWall(makeID("Angelic Stone Wall", AngelicWallID), DungeonStone, 1)).setIconName("Angelic Stone").setCreativeTab(Aether.blocks);
        HellfireWall = (new BlockAetherWall(makeID("Hellfire Stone Wall", HellfireWallID), DungeonStone, 2)).setIconName("Hellfire Stone").setCreativeTab(Aether.blocks);
        HolystoneBrick = (new BlockAether(makeID("Holystone Brick", HolystoneBrickID), Material.rock)).setIconName("Holystone Brick").setHardness(0.5F).setStepSound(Block.soundStoneFootstep).setCreativeTab(Aether.blocks);
        SkyrootLogWall = (new BlockAetherWall(makeID("Skyroot Log Wall", SkyrootLogWallID), AetherLog, 0)).setIconName("Skyroot Log Wall").setCreativeTab(Aether.blocks);
        TallAetherGrass = (new BlockTallAetherGrass(makeID("Tall Aether Grass", TallAetherGrassID))).setIconName("Tall Aether Grass").setCreativeTab(Aether.blocks);
        DarkBlueSkyrootLeaves = (new BlockAetherLeaves(DarkBlueSkyrootLeavesID, DarkBlueSkyrootSaplingID)).setIconName("Dark Blue Skyroot Leaves").setCreativeTab(Aether.blocks);
        BlueSkyrootSapling = (new BlockAetherSapling(BlueSkyrootSaplingID, new AetherGenNormalTree(BlueSkyrootLeavesID, AetherLog.blockID, 0))).setIconName("Blue Skyroot Sapling").setCreativeTab(Aether.blocks);
        BlueSkyrootLeaves = (new BlockAetherLeaves(BlueSkyrootLeavesID, BlueSkyrootSaplingID)).setIconName("Blue Skyroot Leaves").setCreativeTab(Aether.blocks);
        SkyrootChest = (new BlockSkyrootChest(makeID("Skyroot Chest", SkyrootChestID), 0)).setIconName("Skyroot Chest").setCreativeTab(Aether.blocks).setHardness(2.5F).setStepSound(Block.soundWoodFootstep);
        BronzeDoorController = (new BlockBronzeDoorController(makeID("Bronze Door Lock", BronzeDoorControllerID))).setIconName("Bronze Door Lock");
        PurpleSkyrootSapling = (new BlockAetherSapling(PurpleSkyrootSaplingID, new AetherGenPurpleTree(PurpleSkyrootLeavesID, 5, true))).setIconName("Purple Skyroot Sapling").setCreativeTab(Aether.blocks);
        PurpleSkyrootLeaves = (new BlockAetherLeaves(PurpleSkyrootLeavesID, PurpleSkyrootSaplingID)).setIconName("Purple Skyroot Leaves").setCreativeTab(Aether.blocks);
        BlockOrangeTree = (new BlockOrangeTree(makeID("BlockOrangeTree", OrangeFruitTreeID))).setIconName("BlockOrangeTree").setCreativeTab(Aether.blocks);
        BronzeDoor = (new BlockBronzeDoor(makeID("Bronze Door", BronzeDoorID))).setIconName("Bronze Door").setCreativeTab(Aether.blocks);
        ContinuumOre = (new BlockContinuumOre(makeID("Continuum Ore", ContinuumOreID))).setIconName("Continuum Ore").setCreativeTab(Aether.blocks);
        DarkBlueSkyrootSapling = (new BlockAetherSapling(DarkBlueSkyrootSaplingID, new AetherGenMassiveTree(DarkBlueSkyrootLeaves.blockID, 8, true))).setIconName("Dark Blue Skyroot Sapling").setCreativeTab(Aether.blocks);
        SkyrootCraftingTable = (new BlockSkyrootWorkbench(makeID("Skyroot Workbench", SkyrootCraftingTableID))).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("Skyroot Workbench").setCreativeTab(Aether.blocks);
        DungeonEntrance = (new BlockEntranceDoor(makeID("Dungeon Entrance", DungeonEntranceID))).setIconName("Dungeon Entrance");
        DungeonEntranceController = (new BlockEntranceController(makeID("Dungeon Entrance Controller", DungeonEntranceControllerID))).setIconName("Dungeon Entrance Controller");
        DungeonHolystone = (new BlockDungeonHolystone(makeID("Dungeon Holystone", DungeonHolystoneID))).setIconName("Dungeon Holystone");
        CarvedDungeonWall = (new BlockLockedAetherWall(makeID("Locked Carved Stone Wall", CarvedDungeonWallID), DungeonStone, 0)).setIconName("Locked Carved Stone Wall").setBlockUnbreakable();
        CarvedDungeonStairs = (new BlockLockedAetherStairs(makeID("Locked Carved Stone Stairs", CarvedDungeonStairsID), DungeonStone, 0)).setIconName("Locked Carved Stone Stairs").setBlockUnbreakable();
        ColdFire = (BlockColdFire) (new BlockColdFire(makeID("Cold Fire", ColdFireID))).setIconName("Cold Fire").setBlockUnbreakable();
        Block.blocksList[Block.bed.blockID] = null;
        Block.blocksList[Block.bed.blockID] = (new BlockAetherBed(26)).setHardness(0.2F).setUnlocalizedName("bed");
        registerBlocks();
        addHarvestLevel();
    }

    public static void addHarvestLevel()
    {
        MinecraftForge.setBlockHarvestLevel(Holystone, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(Icestone, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(ZaniteOre, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(GravititeOre, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(AmbrosiumOre, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(Quicksoil, "shovel", 0);
        MinecraftForge.setBlockHarvestLevel(AetherDirt, "shovel", 0);
        MinecraftForge.setBlockHarvestLevel(AetherGrass, "shovel", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootChest, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootCraftingTable, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootFence, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootLogWall, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootPlank, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(SkyrootStairs, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(AetherLog, "axe", 0);
        MinecraftForge.setBlockHarvestLevel(AetherLog, 1, "axe", 1);
    }

    public static boolean isGood(int var0, int var1)
    {
        return var0 == 0;
    }

    public static int makeID(String var0, int var1)
    {
        return Aether.getConfig().getBlock(var0, var1).getInt(var1);
    }

    public static int makeTerrainID(String var0, String var1, int var2)
    {
        return Aether.getConfig().getTerrainBlock(var0, var1, var2, "An Aether Terrain Block").getInt(var2);
    }

    public static void registerBlocks()
    {
        Field[] var0 = AetherBlocks.class.getDeclaredFields();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            Field var3 = var0[var2];
            AEBlock var4 = (AEBlock) var3.getAnnotation(AEBlock.class);

            if (var4 != null && Block.class.isAssignableFrom(var3.getType()))
            {
                Block var5;

                try
                {
                    var5 = (Block) var3.get((Object) null);
                } catch (IllegalAccessException var8)
                {
                    var8.printStackTrace();
                    continue;
                }

                GameRegistry.registerBlock(var5, var4.itemBlock());
                String[] var6 = var4.names();

                if (var6.length != 0)
                {
                    for (int var7 = 0; var7 < var6.length; ++var7)
                    {
                        LanguageRegistry.addName(new ItemStack(var5, 1, var7), var6[var7]);
                    }
                } else
                {
                    LanguageRegistry.addName(var5, var4.name());
                }
            }
        }

        LanguageRegistry.addName(new ItemStack(Holystone, 1, 0), "Holystone");
        LanguageRegistry.addName(new ItemStack(Holystone, 1, 1), "Holystone");
        LanguageRegistry.addName(new ItemStack(Holystone, 1, 2), "Mossy Holystone");
        LanguageRegistry.addName(new ItemStack(Holystone, 1, 3), "Mossy Holystone");
        LanguageRegistry.addName(new ItemStack(DungeonHolystone, 1, 0), "Dungeon Holystone");
        LanguageRegistry.addName(new ItemStack(DungeonHolystone, 1, 1), "Dungeon Holystone");
        LanguageRegistry.addName(new ItemStack(DungeonHolystone, 1, 2), "Dungeon Mossy Holystone");
        LanguageRegistry.addName(new ItemStack(DungeonHolystone, 1, 3), "Dungeon Mossy Holystone");
        LanguageRegistry.addName(new ItemStack(AetherLog, 1, 0), "Skyroot Log");
        LanguageRegistry.addName(new ItemStack(AetherLog, 1, 1), "Skyroot Log");
        LanguageRegistry.addName(new ItemStack(AetherLog, 1, 2), "Golden Oak Log");
    }
}
