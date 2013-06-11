package net.aetherteam.aether.blocks;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class BlockQuicksoil extends BlockAether implements IAetherBlock
{
    public BlockQuicksoil(int var1)
    {
        super(var1, Material.sand);
        this.slipperiness = 1.23F;
        this.setHardness(0.5F);
        this.setStepSound(Block.soundSandFootstep);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World var1, int var2, int var3, int var4, Entity var5)
    {
        var5.motionX *= 1.1D;
        var5.motionZ *= 1.1D;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);

        if (var6 == 0 && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
        {
            var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem(var1, var3, var4, var5, var6, 0);
        }

        this.dropBlockAsItem(var1, var3, var4, var5, var6, 0);
    }
}
