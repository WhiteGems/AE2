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
    /**
     * Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc.
     */
    private int lightningState;

    /**
     * A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt
     */
    public long boltVertex = 0L;

    /**
     * Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time.
     */
    private int boltLivingTime;
    public EntityPlayer playerUsing;

    public EntityAetherLightning(World var1, double var2, double var4, double var6, EntityPlayer player)
    {
        super(var1, var2, var4, var6);
        this.playerUsing = player;
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
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();

                if (!this.worldObj.isRemote && this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10))
                {
                    int d0 = MathHelper.floor_double(this.posX);
                    int j = MathHelper.floor_double(this.posY);
                    int list = MathHelper.floor_double(this.posZ);

                    if (this.worldObj.getBlockId(d0, j, list) == 0 && Block.fire.canPlaceBlockAt(this.worldObj, d0, j, list))
                    {
                        this.worldObj.setBlock(d0, j, list, Block.fire.blockID);
                    }
                }
            }
        }

        if (this.lightningState >= 0)
        {
            if (this.worldObj.isRemote)
            {
                this.worldObj.lastLightningBolt = 2;
            }
            else
            {
                double var6 = 3.0D;
                List var7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().getAABB(this.posX - var6, this.posY - var6, this.posZ - var6, this.posX + var6, this.posY + 6.0D + var6, this.posZ + var6));

                for (int l = 0; l < var7.size(); ++l)
                {
                    Entity entity = (Entity)var7.get(l);
                    entity.onStruckByLightning(this);
                }
            }
        }
    }
}
