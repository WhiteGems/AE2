package net.aetherteam.aether.recipes;

import cpw.mods.fml.common.registry.GameRegistry;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AetherRecipes
{
    public static void init()
    {
        OreDictionary.registerOre("planks", Block.planks);
        OreDictionary.registerOre("planks", AetherBlocks.SkyrootPlank);
        OreDictionary.registerOre("wood", AetherBlocks.AetherLog);
        OreDictionary.registerOre("wood", Block.wood);
        OreDictionary.registerOre("stick", Item.stick);
        OreDictionary.registerOre("stick", AetherItems.SkyrootStick);
        GameRegistry.addRecipe(new ItemStack(Block.pistonStickyBase), new Object[] { "X", "O", 'X', AetherItems.SwettyBall, 'O', Block.pistonBase });
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.HolystonePickaxe, 1), new Object[] { "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.Holystone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.HolystoneAxe, 1), new Object[] { "ZZ", "ZY", " Y", 'Z', AetherBlocks.Holystone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.HolystoneShovel, 1), new Object[] { "Z", "Y", "Y", 'Z', AetherBlocks.Holystone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.HolystoneSword, 1), new Object[] { "Z", "Z", "Y", 'Z', AetherBlocks.Holystone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.ZanitePickaxe, 1), new Object[] { "ZZZ", " Y ", " Y ", 'Z', AetherItems.ZaniteGemstone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.ZaniteAxe, 1), new Object[] { "ZZ", "ZY", " Y", 'Z', AetherItems.ZaniteGemstone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.ZaniteShovel, 1), new Object[] { "Z", "Y", "Y", 'Z', AetherItems.ZaniteGemstone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.ZaniteSword, 1), new Object[] { "Z", "Z", "Y", 'Z', AetherItems.ZaniteGemstone, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.GravititePickaxe, 1), new Object[] { "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.EnchantedGravitite, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.GravititeAxe, 1), new Object[] { "ZZ", "ZY", " Y", 'Z', AetherBlocks.EnchantedGravitite, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.GravititeShovel, 1), new Object[] { "Z", "Y", "Y", 'Z', AetherBlocks.EnchantedGravitite, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.GravititeSword, 1), new Object[] { "Z", "Z", "Y", 'Z', AetherBlocks.EnchantedGravitite, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.SkyrootPickaxe, 1), new Object[] { "ZZZ", " Y ", " Y ", 'Z', AetherBlocks.SkyrootPlank, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.SkyrootAxe, 1), new Object[] { "ZZ", "ZY", " Y", 'Z', AetherBlocks.SkyrootPlank, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.SkyrootShovel, 1), new Object[] { "Z", "Y", "Y", 'Z', AetherBlocks.SkyrootPlank, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.SkyrootSword, 1), new Object[] { "Z", "Z", "Y", 'Z', AetherBlocks.SkyrootPlank, 'Y', "stick" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AetherItems.SkyrootBucket, 1, 0), new Object[] { "Z Z", " Z ", 'Z', AetherBlocks.SkyrootPlank }));
        GameRegistry.addRecipe(new ItemStack(AetherItems.SkyrootStick, 4), new Object[] { "#", "#", '#', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootCraftingTable, 1), new Object[] { "UU", "UU", 'U', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.AmbrosiumTorch, 2), new Object[] { " Z", " Y", 'Z', AetherItems.AmbrosiumShard, 'Y', AetherItems.SkyrootStick });
        GameRegistry.addRecipe(new ItemStack(AetherItems.Dart, 1, 0), new Object[] { "X", "Z", "Y", 'X', AetherItems.GoldenAmber, 'Z', AetherItems.SkyrootStick, 'Y', Item.feather });
        GameRegistry.addRecipe(new ItemStack(AetherItems.Dart, 8, 1), new Object[] { "XXX", "XZX", "XXX", 'X', new ItemStack(AetherItems.Dart, 1, 0), 'Z', new ItemStack(AetherItems.SkyrootBucket, 1, 2) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.WhiteCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 0) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.RedCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 14) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 11) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 3) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.BlueCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 9) });
        GameRegistry.addRecipe(new ItemStack(AetherItems.YellowCape, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(Block.cloth, 1, 4) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.Incubator, 1), new Object[] { "XXX", "XZX", "XXX", 'X', AetherBlocks.Holystone, 'Z', AetherBlocks.AmbrosiumTorch });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.Freezer, 1), new Object[] { "XXX", "XZX", "YYY", 'X', AetherBlocks.Holystone, 'Z', AetherBlocks.Icestone, 'Y', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.ZaniteBlock, 1), new Object[] { "XX", "XX", 'X', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.DartShooter, 1), new Object[] { "X", "X", "Y", 'X', AetherBlocks.SkyrootPlank, 'Y', AetherItems.GoldenAmber });
        GameRegistry.addRecipe(new ItemStack(Item.saddle, 1), new Object[] { "XXX", "XZX", 'X', Item.leather, 'Z', Item.silk });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GravititeHelmet, 1), new Object[] { "XXX", "X X", 'X', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GravititeChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GravititeLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GravititeBoots, 1), new Object[] { "X X", "X X", 'X', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteHelmet, 1), new Object[] { "XXX", "X X", 'X', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteBoots, 1), new Object[] { "X X", "X X", 'X', AetherItems.ZaniteGemstone });
        GameRegistry.addShapelessRecipe(new ItemStack(AetherItems.DartShooter, 1, 1), new Object[] { new ItemStack(AetherItems.DartShooter, 1, 0), AetherItems.AechorPetal });
        GameRegistry.addShapelessRecipe(new ItemStack(AetherItems.ZaniteGemstone, 4), new Object[] { AetherBlocks.ZaniteBlock });
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 5), new Object[] { AetherBlocks.PurpleFlower });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootChest, 1), new Object[] { "PPP", "P P", "PPP", 'P', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(Item.doorWood), new Object[] { "PP", "PP", "PP", 'P', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootFence, 2), new Object[] { "SSS", "SSS", 'S', AetherItems.SkyrootStick });
        GameRegistry.addRecipe(new ItemStack(Block.ladder, 4), new Object[] { "S S", "SSS", "S S", 'S', AetherItems.SkyrootStick });
        GameRegistry.addRecipe(new ItemStack(Block.jukebox), new Object[] { "PPP", "PGP", "PPP", 'P', AetherBlocks.SkyrootPlank, 'G', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootPlank, 4), new Object[] { "L", 'L', AetherBlocks.AetherLog });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.Altar), new Object[] { "HHH", "HZH", "HHH", 'H', AetherBlocks.Holystone, 'Z', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.LeatherGloves), new Object[] { "C C", 'C', Item.leather });
        GameRegistry.addRecipe(new ItemStack(AetherItems.IronGloves), new Object[] { "C C", 'C', Item.ingotIron });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GoldenGloves), new Object[] { "C C", 'C', Item.ingotGold });
        GameRegistry.addRecipe(new ItemStack(AetherItems.DiamondGloves), new Object[] { "C C", 'C', Item.diamond });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteGloves), new Object[] { "C C", 'C', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GravititeGloves), new Object[] { "C C", 'C', AetherBlocks.EnchantedGravitite });
        GameRegistry.addRecipe(new ItemStack(AetherItems.IronRing), new Object[] { " C ", "C C", " C ", 'C', Item.ingotIron });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GoldenRing), new Object[] { " C ", "C C", " C ", 'C', Item.ingotGold });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZaniteRing), new Object[] { " C ", "C C", " C ", 'C', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.IronPendant), new Object[] { "SSS", "S S", " C ", 'S', Item.silk, 'C', Item.ingotIron });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GoldenPendant), new Object[] { "SSS", "S S", " C ", 'S', Item.silk, 'C', Item.ingotGold });
        GameRegistry.addRecipe(new ItemStack(AetherItems.ZanitePendant), new Object[] { "SSS", "S S", " C ", 'S', Item.silk, 'C', AetherItems.ZaniteGemstone });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.HolystoneStairs, 4), new Object[] { "  U", " UU", "UUU", 'U', new ItemStack(AetherBlocks.Holystone, 1, 1) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.MossyHolystoneStairs, 6), new Object[] { "  U", " UU", "UUU", 'U', new ItemStack(AetherBlocks.Holystone, 1, 2) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.IcestoneStairs, 4), new Object[] { "  U", " UU", "UUU", 'U', AetherBlocks.Icestone });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootStairs, 4), new Object[] { "  U", " UU", "UUU", 'U', AetherBlocks.SkyrootPlank });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.CarvedStairs, 6), new Object[] { "  U", " UU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 0) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.AngelicStairs, 6), new Object[] { "  U", " UU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 1) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.HellfireStairs, 6), new Object[] { "  U", " UU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 2) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.HolystoneWall, 6), new Object[] { "UUU", "UUU", 'U', new ItemStack(AetherBlocks.Holystone, 1, 1) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.MossyHolystoneWall, 6), new Object[] { "UUU", "UUU", 'U', new ItemStack(AetherBlocks.Holystone, 1, 2) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.IcestoneWall, 6), new Object[] { "UUU", "UUU", 'U', AetherBlocks.Icestone });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.SkyrootLogWall, 6), new Object[] { "UUU", "UUU", 'U', AetherBlocks.AetherLog });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.CarvedWall, 6), new Object[] { "UUU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 0) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.AngelicWall, 6), new Object[] { "UUU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 1) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.HellfireWall, 6), new Object[] { "UUU", "UUU", 'U', new ItemStack(AetherBlocks.DungeonStone, 1, 2) });
        GameRegistry.addRecipe(new ItemStack(AetherBlocks.HolystoneBrick, 2), new Object[] { "UU", "UU", 'U', AetherBlocks.Holystone });
        GameRegistry.addRecipe(new ItemStack(AetherItems.CloudParachute, 1), new Object[] { "XXX", "XSX", "S S", 'X', new ItemStack(AetherBlocks.Aercloud, 1, 0), 'S', Item.silk });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GoldenCloudParachute, 1), new Object[] { "XXX", "XSX", "S S", 'X', new ItemStack(AetherBlocks.Aercloud, 1, 2), 'S', Item.silk });
        GameRegistry.addRecipe(new ItemStack(AetherItems.BlueCloudParachute, 1), new Object[] { "XXX", "XSX", "S S", 'X', new ItemStack(AetherBlocks.Aercloud, 1, 1), 'S', Item.silk });
        GameRegistry.addRecipe(new ItemStack(AetherItems.PurpleCloudParachute, 1), new Object[] { "XXX", "XSX", "S S", 'X', new ItemStack(AetherBlocks.Aercloud, 1, 5), 'S', Item.silk });
        GameRegistry.addRecipe(new ItemStack(AetherItems.GreenCloudParachute, 1), new Object[] { "XXX", "XSX", "S S", 'X', new ItemStack(AetherBlocks.Aercloud, 1, 3), 'S', Item.silk });

        for (int i = 0; i <= Item.itemsList[Item.shears.itemID].getMaxDamage(); i++)
            for (int j = 0; j <= 15; j++)
                GameRegistry.addShapelessRecipe(new ItemStack(Item.silk, 4), new Object[] { new ItemStack(Block.cloth, 1, j), new ItemStack(Item.shears, 1, i) });
    }
}

