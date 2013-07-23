package net.aetherteam.aether.recipes;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AetherEnchantments
{
    public static void init()
    {
        addEnchantment(new ItemStack(AetherBlocks.GravititeOre, 1), new ItemStack(AetherBlocks.EnchantedGravitite, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.SkyrootPickaxe, 1), new ItemStack(AetherItems.SkyrootPickaxe, 1), 1, true);
        addEnchantment(new ItemStack(AetherItems.SkyrootSword, 1), new ItemStack(AetherItems.SkyrootSword, 1), 1, true);
        addEnchantment(new ItemStack(AetherItems.SkyrootShovel, 1), new ItemStack(AetherItems.SkyrootShovel, 1), 1, true);
        addEnchantment(new ItemStack(AetherItems.SkyrootAxe, 1), new ItemStack(AetherItems.SkyrootAxe, 1), 1, true);
        addEnchantment(new ItemStack(AetherItems.HolystonePickaxe, 1), new ItemStack(AetherItems.HolystonePickaxe, 1), 2, true);
        addEnchantment(new ItemStack(AetherItems.HolystoneSword, 1), new ItemStack(AetherItems.HolystoneSword, 1), 2, true);
        addEnchantment(new ItemStack(AetherItems.HolystoneShovel, 1), new ItemStack(AetherItems.HolystoneShovel, 1), 2, true);
        addEnchantment(new ItemStack(AetherItems.HolystoneAxe, 1), new ItemStack(AetherItems.HolystoneAxe, 1), 2, true);
        addEnchantment(new ItemStack(AetherItems.ZanitePickaxe, 1), new ItemStack(AetherItems.ZanitePickaxe, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteSword, 1), new ItemStack(AetherItems.ZaniteSword, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteShovel, 1), new ItemStack(AetherItems.ZaniteShovel, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteAxe, 1), new ItemStack(AetherItems.ZaniteAxe, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.GravititePickaxe, 1), new ItemStack(AetherItems.GravititePickaxe, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeSword, 1), new ItemStack(AetherItems.GravititeSword, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeShovel, 1), new ItemStack(AetherItems.GravititeShovel, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeAxe, 1), new ItemStack(AetherItems.GravititeAxe, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.Dart, 1, 0), new ItemStack(AetherItems.Dart, 1, 2), 6);
        addEnchantment(new ItemStack(AetherItems.SkyrootBucket, 1, 2), new ItemStack(AetherItems.SkyrootBucket, 1, 3), 2, true);
        int var0 = 2256;

        for (Item var1 = Item.itemsList[var0]; var1 != null && var1.getUnlocalizedName().equals("item.record"); var1 = Item.itemsList[var0])
        {
            addEnchantment(new ItemStack(var1, 1), new ItemStack(AetherItems.AetherMusicDisk, 1), 4, true);
            ++var0;
        }

        addEnchantment(new ItemStack(Item.helmetLeather, 1), new ItemStack(Item.helmetLeather, 1), 1, true);
        addEnchantment(new ItemStack(Item.plateLeather, 1), new ItemStack(Item.plateLeather, 1), 1, true);
        addEnchantment(new ItemStack(Item.legsLeather, 1), new ItemStack(Item.legsLeather, 1), 1, true);
        addEnchantment(new ItemStack(Item.bootsLeather, 1), new ItemStack(Item.bootsLeather, 1), 1, true);
        addEnchantment(new ItemStack(Item.pickaxeWood, 1), new ItemStack(Item.pickaxeWood, 1), 1, true);
        addEnchantment(new ItemStack(Item.shovelWood, 1), new ItemStack(Item.shovelWood, 1), 1, true);
        addEnchantment(new ItemStack(Item.swordWood, 1), new ItemStack(Item.swordWood, 1), 1, true);
        addEnchantment(new ItemStack(Item.axeWood, 1), new ItemStack(Item.axeWood, 1), 1, true);
        addEnchantment(new ItemStack(Item.hoeWood, 1), new ItemStack(Item.hoeWood, 1), 1, true);
        addEnchantment(new ItemStack(Item.pickaxeStone, 1), new ItemStack(Item.pickaxeStone, 1), 1, true);
        addEnchantment(new ItemStack(Item.shovelStone, 1), new ItemStack(Item.shovelStone, 1), 1, true);
        addEnchantment(new ItemStack(Item.swordStone, 1), new ItemStack(Item.swordStone, 1), 1, true);
        addEnchantment(new ItemStack(Item.axeStone, 1), new ItemStack(Item.axeStone, 1), 1, true);
        addEnchantment(new ItemStack(Item.hoeStone, 1), new ItemStack(Item.hoeStone, 1), 1, true);
        addEnchantment(new ItemStack(Item.helmetIron, 1), new ItemStack(Item.helmetIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.plateIron, 1), new ItemStack(Item.plateIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.legsIron, 1), new ItemStack(Item.legsIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.bootsIron, 1), new ItemStack(Item.bootsIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.pickaxeIron, 1), new ItemStack(Item.pickaxeIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.shovelIron, 1), new ItemStack(Item.shovelIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.swordIron, 1), new ItemStack(Item.swordIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.axeIron, 1), new ItemStack(Item.axeIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.hoeIron, 1), new ItemStack(Item.hoeIron, 1), 2, true);
        addEnchantment(new ItemStack(Item.helmetGold, 1), new ItemStack(Item.helmetGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.plateGold, 1), new ItemStack(Item.plateGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.legsGold, 1), new ItemStack(Item.legsGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.bootsGold, 1), new ItemStack(Item.bootsGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.pickaxeGold, 1), new ItemStack(Item.pickaxeGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.shovelGold, 1), new ItemStack(Item.shovelGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.swordGold, 1), new ItemStack(Item.swordGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.axeGold, 1), new ItemStack(Item.axeGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.hoeGold, 1), new ItemStack(Item.hoeGold, 1), 3, true);
        addEnchantment(new ItemStack(Item.helmetDiamond, 1), new ItemStack(Item.helmetDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.plateDiamond, 1), new ItemStack(Item.plateDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.legsDiamond, 1), new ItemStack(Item.legsDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.bootsDiamond, 1), new ItemStack(Item.bootsDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.pickaxeDiamond, 1), new ItemStack(Item.pickaxeDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.shovelDiamond, 1), new ItemStack(Item.shovelDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.swordDiamond, 1), new ItemStack(Item.swordDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.axeDiamond, 1), new ItemStack(Item.axeDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.hoeDiamond, 1), new ItemStack(Item.hoeDiamond, 1), 4, true);
        addEnchantment(new ItemStack(Item.fishingRod, 1), new ItemStack(Item.fishingRod, 1), 1, true);
        addEnchantment(new ItemStack(AetherBlocks.Quicksoil, 1), new ItemStack(AetherBlocks.QuicksoilGlass, 1), 4);
        addEnchantment(new ItemStack(AetherBlocks.Holystone, 1, 1), new ItemStack(AetherItems.HealingStone, 1), 3, true);
        addEnchantment(new ItemStack(AetherBlocks.Holystone, 1, 3), new ItemStack(AetherItems.HealingStone, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.GravititeHelmet, 1), new ItemStack(AetherItems.GravititeHelmet, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeChestplate, 1), new ItemStack(AetherItems.GravititeChestplate, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeLeggings, 1), new ItemStack(AetherItems.GravititeLeggings, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeBoots, 1), new ItemStack(AetherItems.GravititeBoots, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GravititeGloves, 1), new ItemStack(AetherItems.GravititeGloves, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteHelmet, 1), new ItemStack(AetherItems.ZaniteHelmet, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteChestplate, 1), new ItemStack(AetherItems.ZaniteChestplate, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteLeggings, 1), new ItemStack(AetherItems.ZaniteLeggings, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteBoots, 1), new ItemStack(AetherItems.ZaniteBoots, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteGloves, 1), new ItemStack(AetherItems.ZaniteGloves, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZaniteRing, 1), new ItemStack(AetherItems.ZaniteRing, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.ZanitePendant, 1), new ItemStack(AetherItems.ZanitePendant, 1), 3, true);
        addEnchantment(new ItemStack(AetherItems.LeatherGloves, 1), new ItemStack(AetherItems.LeatherGloves, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.IronGloves, 1), new ItemStack(AetherItems.IronGloves, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.GoldenGloves, 1), new ItemStack(AetherItems.GoldenGloves, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.DiamondGloves, 1), new ItemStack(AetherItems.DiamondGloves, 1), 4, true);
        addEnchantment(new ItemStack(AetherItems.DartShooter, 1, 0), new ItemStack(AetherItems.DartShooter, 1, 2), 4, true);
        addEnchantment(new ItemStack(AetherItems.BlueBerry, 1, 0), new ItemStack(AetherItems.EnchantedBerry, 1, 0), 2, true);
        addEnchantment(new ItemStack(AetherItems.Wyndberry, 1, 0), new ItemStack(AetherItems.Strawberry, 1, 0), 4, true);
    }

    public static void addEnchantment(ItemStack var0, ItemStack var1, int var2)
    {
        TileEntityAltar.addEnchantment(var0, var1, var2);
    }

    public static void addEnchantment(ItemStack var0, ItemStack var1, int var2, boolean var3)
    {
        TileEntityAltar.addEnchantment(var0, var1, var2, var3);
    }
}
