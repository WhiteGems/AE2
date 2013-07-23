package net.aetherteam.aether.items;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemZaniteShovel extends ItemSpade
{
    public static final Block[] blocksEffectiveAgainst = { Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium, AetherBlocks.AetherDirt, AetherBlocks.AetherGrass };

    public ItemZaniteShovel(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public float getStrVsBlocks(ItemStack par1ItemStack, Block par2Block)
    {
        for (int i = 0; i < blocksEffectiveAgainst.length; i++)
        {
            if (blocksEffectiveAgainst[i] == par2Block)
            {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return getStrVsBlocks(itemstack, block) * (itemstack.getItemDamage() / itemstack.getItem().getMaxDamage() + 0.5F);
    }
}

