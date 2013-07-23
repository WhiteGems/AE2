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

public class BlockAmbrosiumOre extends BlockAether implements IAetherBlock
{
    public BlockAmbrosiumOre(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);
        int var7 = EnchantmentHelper.getFortuneModifier(var2) != 0 ? EnchantmentHelper.getFortuneModifier(var2) : 1;

        if (!var1.isRemote)
        {
            ItemStack var8;

            if (this.canSilkHarvest(var1, var2, var3, var4, var5, var6) && EnchantmentHelper.getSilkTouchModifier(var2))
            {
                var8 = this.createStackedBlock(var6);

                if (var8 != null)
                {
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var8);
                }
            }
            else if (var6 == 0 && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
            {
                var8 = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int((new Random()).nextInt(var7 * 5), 1, var7 * 5 + 1), 0);
                var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var8);
            }
            else
            {
                var8 = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int((new Random()).nextInt(var7), 1, var7 + 1), 0);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var8);
            }

            this.dropXpOnBlockBreak(var1, var3, var4, var5, 2);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return AetherItems.AmbrosiumShard.itemID;
    }
}
