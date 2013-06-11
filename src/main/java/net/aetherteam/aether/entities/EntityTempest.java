package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTempest extends EntityAetherMob implements IMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private int heightOffsetUpdateTime;
    private float heightOffset = 1.5F;
    private int attackTimer;
    public float sinage;
    public int timeUntilShoot = 30;

    public EntityTempest(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/tempest/tempest.png";
        this.setSize(1.25F, 1.5F);
        this.isImmuneToFire = true;
        this.attackTime = this.timeUntilShoot;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.jumpMovementFactor = 0.0F;

        if (this.health > 0)
        {
            double var1 = (double) (this.rand.nextFloat() - 0.5F);
            double var3 = (double) this.rand.nextFloat();
            double var5 = (double) (this.rand.nextFloat() - 0.5F);
            double var7 = this.posX + var1 * var3;
            double var9 = this.boundingBox.minY + var3 - 0.30000001192092896D;
            double var11 = this.posZ + var5 * var3;
            this.worldObj.spawnParticle("reddust", var7, var9, var11, 1.0D, 1.0D, 1.0D);
        }

        if (this.entityToAttack != null)
        {
            this.attackEntity(this.entityToAttack, this.getDistanceToEntity(this.entityToAttack));
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            this.damageEntity(DamageSource.drown, 1);
        }

        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }

        if (!this.worldObj.isRemote)
        {
            --this.heightOffsetUpdateTime;

            if (this.heightOffsetUpdateTime <= 0)
            {
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = 1.5F + (float) this.rand.nextGaussian() * 3.0F;
            }

            if (this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double) this.getEntityToAttack().getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset)
            {
                this.motionY += (0.700000011920929D - this.motionY) * 0.700000011920929D;
            }
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.8D;
        }

        if (this.hurtTime > 0)
        {
            this.sinage += 0.9F;
        } else
        {
            this.sinage += 0.2F;
        }

        if (this.sinage > ((float) Math.PI * 2F))
        {
            this.sinage -= ((float) Math.PI * 2F);
        }

        super.onLivingUpdate();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        EntityLiving var3 = (EntityLiving) var1;

        if (var2 < 10.0F)
        {
            double var4 = var1.posX - this.posX;
            double var6 = var1.posZ - this.posZ;

            if (var3 != null)
            {
                if (var3.isDead || (double) var3.getDistanceToEntity(this) > 12.0D || var3 instanceof EntityNewZephyr || var3 instanceof EntityTempest)
                {
                    var3 = null;
                    this.entityToAttack = null;
                    return;
                }

                if (this.attackTime >= this.timeUntilShoot)
                {
                    this.shootTarget(var3);
                }

                if (this.attackTime >= this.timeUntilShoot && this.canEntityBeSeen(var3))
                {
                    this.attackTime = -10;
                }

                if (this.attackTime < this.timeUntilShoot)
                {
                    ++this.attackTime;
                }
            }

            this.rotationYaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
        }
    }

    public void shootTarget(EntityLiving var1)
    {
        if (this.worldObj.difficultySetting != 0)
        {
            double var2 = var1.posX - this.posX;
            double var4 = var1.boundingBox.minY + (double) (var1.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
            double var6 = var1.posZ - this.posZ;
            double var8 = var1.posX - this.posX;
            double var10 = var1.posZ - this.posZ;
            double var12 = 1.5D / Math.sqrt(var8 * var8 + var10 * var10 + 0.1D);
            double var14 = 0.1D + Math.sqrt(var8 * var8 + var10 * var10 + 0.1D) * 0.5D + (this.posY - var1.posY) * 0.25D;
            double var10000 = var8 * var12;
            var10000 = var10 * var12;
            EntityTempestBall var16 = new EntityTempestBall(this.worldObj, this, var2, var4, var6);
            var16.posY = this.posY + 1.0D;
            this.worldObj.playSoundAtEntity(this, "aemob.zephyr.shoot", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(var16);
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float var1)
    {}

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {}

    public int getMaxHealth()
    {
        return 15;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.zephyr.say";
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(var1, var2, var3) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0 && !this.worldObj.isDaytime();
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}
