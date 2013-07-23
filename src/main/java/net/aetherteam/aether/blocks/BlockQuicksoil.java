package net.aetherteam.aether.blocks;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockQuicksoil extends BlockAether
    implements IAetherBlock
{
    public BlockQuicksoil(int blockID)
    {
        super(blockID, Material.sand);
        this.slipperiness = 1.23F;
        setHardness(0.5F);
        setStepSound(Block.soundSandFootstep);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        float f = 0.125F;
        return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1 - f, k + 1);
    }

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

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if ((meta == 0) && (entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootShovel.itemID))
        {
            entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
            dropBlockAsItem(world, x, y, z, meta, 0);
        }

        dropBlockAsItem(world, x, y, z, meta, 0);
    }
}

