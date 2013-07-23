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
    protected BlockZaniteOre(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public boolean canSilkHarvest(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        return true;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);
        ItemStack var7 = null;
        int var8 = EnchantmentHelper.getFortuneModifier(var2) != 0 ? EnchantmentHelper.getFortuneModifier(var2) : 1;

        if (EnchantmentHelper.getSilkTouchModifier(var2))
        {
            var7 = this.createStackedBlock(var6);
        }
        else
        {
            var7 = new ItemStack(AetherItems.ZaniteGemstone.itemID, MathHelper.clamp_int((new Random()).nextInt(var8 * 2), 1, var8 * 2 + 1), 0);
        }

        if (var7 != null)
        {
            this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return AetherItems.ZaniteGemstone.itemID;
    }
}
