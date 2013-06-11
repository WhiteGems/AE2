package net.aetherteam.aether.entities;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAetherLightning extends EntityLightningBolt
{
    private int lightningState;

    /**
     * A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt
     */
    public long boltVertex = 0L;
    private int boltLivingTime;
    public EntityPlayer playerUsing;

    public EntityAetherLightning(World var1, double var2, double var4, double var6, EntityPlayer var8)
    {
        super(var1, var2, var4, var6);
        this.playerUsing = var8;
        this.setLocationAndAngles(var2, var4, var6, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.lightningState == 2)
        {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();

                if (!this.worldObj.isRemote && this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10))
                {
                    int var1 = MathHelper.floor_double(this.posX);
                    int var2 = MathHelper.floor_double(this.posY);
                    int var3 = MathHelper.floor_double(this.posZ);

                    if (this.worldObj.getBlockId(var1, var2, var3) == 0 && Block.fire.canPlaceBlockAt(this.worldObj, var1, var2, var3))
                    {
                        this.worldObj.setBlock(var1, var2, var3, Block.fire.blockID);
                    }
                }
            }
        }

        if (this.lightningState >= 0)
        {
            if (this.worldObj.isRemote)
            {
                this.worldObj.lastLightningBolt = 2;
            } else
            {
                double var7 = 3.0D;
                List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().getAABB(this.posX - var7, this.posY - var7, this.posZ - var7, this.posX + var7, this.posY + 6.0D + var7, this.posZ + var7));

                for (int var4 = 0; var4 < var6.size(); ++var4)
                {
                    Entity var5 = (Entity) var6.get(var4);
                    var5.onStruckByLightning(this);
                }
            }
        }
    }
}
