package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockZaniteOre extends BlockAether implements IAetherBlock
{
    protected BlockZaniteOre(int blockID)
    {
        super(blockID, Material.rock);
        this.setHardness(3.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        return true;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        player.addExhaustion(0.025F);
        ItemStack itemstack = null;
        int fortune = EnchantmentHelper.getFortuneModifier(player) != 0 ? EnchantmentHelper.getFortuneModifier(player) : 1;

        if (EnchantmentHelper.getSilkTouchModifier(player))
        {
            itemstack = this.createStackedBlock(meta);
        }
        else
        {
            itemstack = new ItemStack(AetherItems.ZaniteGemstone.itemID, MathHelper.clamp_int((new Random()).nextInt(fortune * 2), 1, fortune * 2 + 1), 0);
        }

        if (itemstack != null)
        {
            this.dropBlockAsItem_do(world, x, y, z, itemstack);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int k)
    {
        return AetherItems.ZaniteGemstone.itemID;
    }
}
