package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockZaniteOre extends BlockAether
    implements IAetherBlock
{
    protected BlockZaniteOre(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(3.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        return true;
    }

    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        player.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        player.addExhaustion(0.025F);
        ItemStack itemstack = null;
        int fortune = EnchantmentHelper.getFortuneModifier(player) != 0 ? EnchantmentHelper.getFortuneModifier(player) : 1;

        if (EnchantmentHelper.getSilkTouchModifier(player))
        {
            itemstack = createStackedBlock(meta);
        }
        else
        {
            itemstack = new ItemStack(AetherItems.ZaniteGemstone.itemID, MathHelper.clamp_int(new Random().nextInt(fortune * 2), 1, fortune * 2 + 1), 0);
        }

        if (itemstack != null)
        {
            dropBlockAsItem_do(world, x, y, z, itemstack);
        }
    }

    public int idDropped(int i, Random random, int k)
    {
        return AetherItems.ZaniteGemstone.itemID;
    }
}

