package net.aetherteam.aether.entities;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAetherAnimal extends EntityAnimal implements IAetherMob
{
    Random random = new Random();

    public EntityAetherAnimal(World world)
    {
        super(world);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return this.worldObj.getBlockId(par1, par2 - 1, par3) == AetherBlocks.AetherGrass.blockID ? 10.0F : this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.deathTime == 18 && !this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild())
        {
            for (int amount = 0; amount < this.random.nextInt(4); ++amount)
            {
                this.worldObj.spawnEntityInWorld(new EntityAetherCoin(this.worldObj, this.posX, this.posY, this.posZ, 1));
            }
        }

        super.onDeathUpdate();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere() && this.getBlockPathWeight(i, j, k) >= 0.0F && this.getBlockPathWeight(i, j, k) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal var1)
    {
        return null;
    }
}
