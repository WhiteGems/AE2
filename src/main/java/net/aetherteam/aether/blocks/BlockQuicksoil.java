package net.aetherteam.aether.blocks;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockQuicksoil extends BlockAether implements IAetherBlock
{
    public BlockQuicksoil(int blockID)
    {
        super(blockID, Material.sand);
        this.slipperiness = 1.23F;
        this.setHardness(0.5F);
        this.setStepSound(Block.soundSandFootstep);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        float f = 0.125F;
        return AxisAlignedBB.getBoundingBox((double)i, (double)j, (double)k, (double)(i + 1), (double)((float)(j + 1) - f), (double)(k + 1));
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity entity)
    {
        Vec3 vec3 = entity.getLookVec();

        if (vec3 != null)
        {
            entity.motionX += vec3.xCoord;
            entity.motionZ += vec3.zCoord;
            entity.velocityChanged = true;
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (meta == 0 && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
        {
            entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem(world, x, y, z, meta, 0);
        }

        this.dropBlockAsItem(world, x, y, z, meta, 0);
    }
}
