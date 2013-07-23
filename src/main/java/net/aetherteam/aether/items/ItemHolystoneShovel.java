package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

public class ItemHolystoneShovel extends ItemSpade
{
    private static Random random = new Random();

    public static final Block[] blocksEffectiveAgainst = { Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium, AetherBlocks.AetherDirt, AetherBlocks.AetherGrass };

    public ItemHolystoneShovel(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, int i, int j, int k, int l, EntityLiving entityliving)
    {
        if (random.nextInt(20) == 0)
        {
            if (!world.isRemote)
            {
                entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
            }
        }

        return super.onBlockDestroyed(itemstack, world, i, j, k, l, entityliving);
    }

    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
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
}

