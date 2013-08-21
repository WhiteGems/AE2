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
    public BlockAmbrosiumOre(int blockID)
    {
        super(blockID, Material.rock);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);
        int i1 = EnchantmentHelper.getFortuneModifier(entityplayer) != 0 ? EnchantmentHelper.getFortuneModifier(entityplayer) : 1;

        if (!world.isRemote)
        {
            ItemStack stack;

            if (this.canSilkHarvest(world, entityplayer, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(entityplayer))
            {
                stack = this.createStackedBlock(meta);

                if (stack != null)
                {
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }
            else if (meta == 0 && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
            {
                stack = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int((new Random()).nextInt(i1 * 5), 1, i1 * 5 + 1), 0);
                entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
            else
            {
                stack = new ItemStack(AetherItems.AmbrosiumShard.itemID, MathHelper.clamp_int((new Random()).nextInt(i1), 1, i1 + 1), 0);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }

            this.dropXpOnBlockBreak(world, x, y, z, 2);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int k)
    {
        return AetherItems.AmbrosiumShard.itemID;
    }
}
