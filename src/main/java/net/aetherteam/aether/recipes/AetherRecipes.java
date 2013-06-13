package net.aetherteam.aether.recipes;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;

public class AetherRecipes
{
    public static void init()
    {
        ModLoader.addRecipe(new ItemStack(AetherItems.HolystonePickaxe, 1), new Object[]{"ZZZ", " Y ", " Y ", Character.valueOf('Z'), AetherBlocks.Holystone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.HolystoneAxe, 1), new Object[]{"ZZ", "ZY", " Y", Character.valueOf('Z'), AetherBlocks.Holystone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.HolystoneShovel, 1), new Object[]{"Z", "Y", "Y", Character.valueOf('Z'), AetherBlocks.Holystone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.HolystoneSword, 1), new Object[]{"Z", "Z", "Y", Character.valueOf('Z'), AetherBlocks.Holystone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.ZanitePickaxe, 1), new Object[]{"ZZZ", " Y ", " Y ", Character.valueOf('Z'), AetherItems.ZaniteGemstone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteAxe, 1), new Object[]{"ZZ", "ZY", " Y", Character.valueOf('Z'), AetherItems.ZaniteGemstone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteShovel, 1), new Object[]{"Z", "Y", "Y", Character.valueOf('Z'), AetherItems.ZaniteGemstone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteSword, 1), new Object[]{"Z", "Z", "Y", Character.valueOf('Z'), AetherItems.ZaniteGemstone, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititePickaxe, 1), new Object[]{"ZZZ", " Y ", " Y ", Character.valueOf('Z'), AetherBlocks.EnchantedGravitite, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeAxe, 1), new Object[]{"ZZ", "ZY", " Y", Character.valueOf('Z'), AetherBlocks.EnchantedGravitite, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeShovel, 1), new Object[]{"Z", "Y", "Y", Character.valueOf('Z'), AetherBlocks.EnchantedGravitite, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeSword, 1), new Object[]{"Z", "Z", "Y", Character.valueOf('Z'), AetherBlocks.EnchantedGravitite, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootPickaxe, 1), new Object[]{"ZZZ", " Y ", " Y ", Character.valueOf('Z'), AetherBlocks.SkyrootPlank, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootAxe, 1), new Object[]{"ZZ", "ZY", " Y", Character.valueOf('Z'), AetherBlocks.SkyrootPlank, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootShovel, 1), new Object[]{"Z", "Y", "Y", Character.valueOf('Z'), AetherBlocks.SkyrootPlank, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootSword, 1), new Object[]{"Z", "Z", "Y", Character.valueOf('Z'), AetherBlocks.SkyrootPlank, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootBucket, 1, 0), new Object[]{"Z Z", " Z ", Character.valueOf('Z'), AetherBlocks.SkyrootPlank});

        ModLoader.addRecipe(new ItemStack(AetherItems.SkyrootStick, 4), new Object[]{"#", "#", Character.valueOf('#'), AetherBlocks.SkyrootPlank});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootCraftingTable, 1), new Object[]{"UU", "UU", Character.valueOf('U'), AetherBlocks.SkyrootPlank});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.AmbrosiumTorch, 2), new Object[]{" Z", " Y", Character.valueOf('Z'), AetherItems.AmbrosiumShard, Character.valueOf('Y'), AetherItems.SkyrootStick});

        ModLoader.addRecipe(new ItemStack(AetherItems.Dart, 1, 0), new Object[]{"X", "Z", "Y", Character.valueOf('X'), AetherItems.GoldenAmber, Character.valueOf('Z'), AetherItems.SkyrootStick, Character.valueOf('Y'), Item.feather});

        ModLoader.addRecipe(new ItemStack(AetherItems.Dart, 8, 1), new Object[]{"XXX", "XZX", "XXX", Character.valueOf('X'), new ItemStack(AetherItems.Dart, 1, 0), Character.valueOf('Z'), new ItemStack(AetherItems.SkyrootBucket, 1, 2)});

        ModLoader.addRecipe(new ItemStack(AetherItems.WhiteCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 0)});

        ModLoader.addRecipe(new ItemStack(AetherItems.RedCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 14)});

        ModLoader.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 11)});

        ModLoader.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 3)});

        ModLoader.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 9)});

        ModLoader.addRecipe(new ItemStack(AetherItems.YellowCape, 1), new Object[]{"XX", "XX", "XX", Character.valueOf('X'), new ItemStack(Block.cloth, 1, 4)});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.Incubator, 1), new Object[]{"XXX", "XZX", "XXX", Character.valueOf('X'), AetherBlocks.Holystone, Character.valueOf('Z'), AetherBlocks.AmbrosiumTorch});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.Freezer, 1), new Object[]{"XXX", "XZX", "YYY", Character.valueOf('X'), AetherBlocks.Holystone, Character.valueOf('Z'), AetherBlocks.Icestone, Character.valueOf('Y'), AetherBlocks.SkyrootPlank});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.ZaniteBlock, 1), new Object[]{"XX", "XX", Character.valueOf('X'), AetherItems.ZaniteGemstone});

        ModLoader.addRecipe(new ItemStack(AetherItems.DartShooter, 1), new Object[]{"X", "X", "Y", Character.valueOf('X'), AetherBlocks.SkyrootPlank, Character.valueOf('Y'), AetherItems.GoldenAmber});

        ModLoader.addRecipe(new ItemStack(Item.saddle, 1), new Object[]{"XXX", "XZX", Character.valueOf('X'), Item.leather, Character.valueOf('Z'), Item.silk});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeHelmet, 1), new Object[]{"XXX", "X X", Character.valueOf('X'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeChestplate, 1), new Object[]{"X X", "XXX", "XXX", Character.valueOf('X'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeLeggings, 1), new Object[]{"XXX", "X X", "X X", Character.valueOf('X'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeBoots, 1), new Object[]{"X X", "X X", Character.valueOf('X'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteHelmet, 1), new Object[]{"XXX", "X X", Character.valueOf('X'), AetherItems.ZaniteGemstone});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteChestplate, 1), new Object[]{"X X", "XXX", "XXX", Character.valueOf('X'), AetherItems.ZaniteGemstone});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteLeggings, 1), new Object[]{"XXX", "X X", "X X", Character.valueOf('X'), AetherItems.ZaniteGemstone});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteBoots, 1), new Object[]{"X X", "X X", Character.valueOf('X'), AetherItems.ZaniteGemstone});

        ModLoader.addShapelessRecipe(new ItemStack(AetherItems.DartShooter, 1, 1), new Object[]{new ItemStack(AetherItems.DartShooter, 1, 0), AetherItems.AechorPetal});
        ModLoader.addShapelessRecipe(new ItemStack(AetherItems.ZaniteGemstone, 4), new Object[]{AetherBlocks.ZaniteBlock});

        ModLoader.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 5), new Object[]{AetherBlocks.PurpleFlower});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootChest, 1), new Object[]{"PPP", "P P", "PPP", Character.valueOf('P'), AetherBlocks.SkyrootPlank});

        ModLoader.addRecipe(new ItemStack(Item.doorWood), new Object[]{"PP", "PP", "PP", Character.valueOf('P'), AetherBlocks.SkyrootPlank});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootFence, 2), new Object[]{"SSS", "SSS", Character.valueOf('S'), AetherItems.SkyrootStick});
        ModLoader.addRecipe(new ItemStack(Block.ladder, 4), new Object[]{"S S", "SSS", "S S", Character.valueOf('S'), AetherItems.SkyrootStick});
        ModLoader.addRecipe(new ItemStack(Block.jukebox), new Object[]{"PPP", "PGP", "PPP", Character.valueOf('P'), AetherBlocks.SkyrootPlank, Character.valueOf('G'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootPlank, 4), new Object[]{"L", Character.valueOf('L'), AetherBlocks.AetherLog});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.Altar), new Object[]{"HHH", "HZH", "HHH", Character.valueOf('H'), AetherBlocks.Holystone, Character.valueOf('Z'), AetherItems.ZaniteGemstone});

        ModLoader.addRecipe(new ItemStack(AetherItems.LeatherGloves), new Object[]{"C C", Character.valueOf('C'), Item.leather});
        ModLoader.addRecipe(new ItemStack(AetherItems.IronGloves), new Object[]{"C C", Character.valueOf('C'), Item.ingotIron});
        ModLoader.addRecipe(new ItemStack(AetherItems.GoldenGloves), new Object[]{"C C", Character.valueOf('C'), Item.ingotGold});
        ModLoader.addRecipe(new ItemStack(AetherItems.DiamondGloves), new Object[]{"C C", Character.valueOf('C'), Item.diamond});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteGloves), new Object[]{"C C", Character.valueOf('C'), AetherItems.ZaniteGemstone});
        ModLoader.addRecipe(new ItemStack(AetherItems.GravititeGloves), new Object[]{"C C", Character.valueOf('C'), AetherBlocks.EnchantedGravitite});

        ModLoader.addRecipe(new ItemStack(AetherItems.IronRing), new Object[]{" C ", "C C", " C ", Character.valueOf('C'), Item.ingotIron});
        ModLoader.addRecipe(new ItemStack(AetherItems.GoldenRing), new Object[]{" C ", "C C", " C ", Character.valueOf('C'), Item.ingotGold});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZaniteRing), new Object[]{" C ", "C C", " C ", Character.valueOf('C'), AetherItems.ZaniteGemstone});

        ModLoader.addRecipe(new ItemStack(AetherItems.IronPendant), new Object[]{"SSS", "S S", " C ", Character.valueOf('S'), Item.silk, Character.valueOf('C'), Item.ingotIron});
        ModLoader.addRecipe(new ItemStack(AetherItems.GoldenPendant), new Object[]{"SSS", "S S", " C ", Character.valueOf('S'), Item.silk, Character.valueOf('C'), Item.ingotGold});
        ModLoader.addRecipe(new ItemStack(AetherItems.ZanitePendant), new Object[]{"SSS", "S S", " C ", Character.valueOf('S'), Item.silk, Character.valueOf('C'), AetherItems.ZaniteGemstone});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.HolystoneStairs, 4), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.Holystone, 1, 1)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.MossyHolystoneStairs, 6), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.Holystone, 1, 2)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.IcestoneStairs, 4), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), AetherBlocks.Icestone});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootStairs, 4), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), AetherBlocks.SkyrootPlank});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.CarvedStairs, 6), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 0)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.AngelicStairs, 6), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 1)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.HellfireStairs, 6), new Object[]{"  U", " UU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 2)});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.HolystoneWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.Holystone, 1, 1)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.MossyHolystoneWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.Holystone, 1, 2)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.IcestoneWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), AetherBlocks.Icestone});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.SkyrootLogWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), AetherBlocks.AetherLog});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.CarvedWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 0)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.AngelicWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 1)});
        ModLoader.addRecipe(new ItemStack(AetherBlocks.HellfireWall, 6), new Object[]{"UUU", "UUU", Character.valueOf('U'), new ItemStack(AetherBlocks.DungeonStone, 1, 2)});

        ModLoader.addRecipe(new ItemStack(AetherBlocks.HolystoneBrick, 2), new Object[]{"UU", "UU", Character.valueOf('U'), AetherBlocks.Holystone});
    
        ModLoader.addRecipe(new ItemStack(AetherItems.GoldenCloudParachute, 1), new Object[]{"ZZ", "ZZ", Character.valueOf('Z'), AetherBlocks.Aercloud});

    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.recipes.AetherRecipes
 * JD-Core Version:    0.6.2
 */
