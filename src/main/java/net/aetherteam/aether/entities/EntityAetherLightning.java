package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AABBPool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAetherLightning extends EntityLightningBolt
{
    private int lightningState;
    public long a;
    private int boltLivingTime;
    public EntityPlayer playerUsing;

    public EntityAetherLightning(World var1, double var2, double var4, double var6, EntityPlayer player)
    {
        super(var1, var2, var4, var6);
        this.boltVertex = 0L;
        this.playerUsing = player;
        setLocationAndAngles(var2, var4, var6, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = (this.rand.nextInt(3) + 1);
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.lightningState == 2)
        {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        this.lightningState -= 1;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                this.boltLivingTime -= 1;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();

                if ((!this.worldObj.isRemote) && (this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)))
                {
                    int i = MathHelper.floor_double(this.posX);
                    int j = MathHelper.floor_double(this.posY);
                    int k = MathHelper.floor_double(this.posZ);

                    if ((this.worldObj.getBlockId(i, j, k) == 0) && (Block.fire.canPlaceBlockAt(this.worldObj, i, j, k)))
                    {
                        this.worldObj.setBlock(i, j, k, Block.fire.blockID);
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
                double d0 = 3.0D;
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getAABBPool().getAABB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));

                for (int l = 0; l < list.size(); l++)
                {
                    Entity entity = (Entity)list.get(l);
                    entity.onStruckByLightning(this);
                }
            }
        }
    }
}

