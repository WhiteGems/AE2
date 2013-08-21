package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTempest extends EntityAetherMob implements IMob
{
    private int heightOffsetUpdateTime;
    private float heightOffset = 1.5F;
    private int attackTimer;
    public float sinage;
    public int timeUntilShoot = 30;

    public EntityTempest(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(15.0D);
        this.setEntityHealth(15.0F);
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

        if (this.func_110143_aJ() > 0.0F)
        {
            double a = (double)(this.rand.nextFloat() - 0.5F);
            double b = (double)this.rand.nextFloat();
            double c = (double)(this.rand.nextFloat() - 0.5F);
            double d = this.posX + a * b;
            double e = this.boundingBox.minY + b - 0.30000001192092896D;
            double f = this.posZ + c * b;
            this.worldObj.spawnParticle("reddust", d, e, f, 1.0D, 1.0D, 1.0D);
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
            this.damageEntity(DamageSource.drown, 1.0F);
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
                this.heightOffset = 1.5F + (float)this.rand.nextGaussian() * 3.0F;
            }

            if (this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double)this.getEntityToAttack().getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
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
        }
        else
        {
            this.sinage += 0.2F;
        }

        if (this.sinage > ((float)Math.PI * 2F))
        {
            this.sinage -= ((float)Math.PI * 2F);
        }

        super.onLivingUpdate();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase target = (EntityLivingBase)entity;

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (target != null)
                {
                    if (target.isDead || (double)target.getDistanceToEntity(this) > 12.0D || target instanceof EntityNewZephyr || target instanceof EntityTempest)
                    {
                        target = null;
                        this.entityToAttack = null;
                        return;
                    }

                    if (this.attackTime >= this.timeUntilShoot)
                    {
                        this.shootTarget(target);
                    }

                    if (this.attackTime >= this.timeUntilShoot && this.canEntityBeSeen(target))
                    {
                        this.attackTime = -10;
                    }

                    if (this.attackTime < this.timeUntilShoot)
                    {
                        ++this.attackTime;
                    }
                }

                this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F;
            }
        }
    }

    public void shootTarget(EntityLivingBase target)
    {
        if (this.worldObj.difficultySetting != 0)
        {
            double d5 = target.posX - this.posX;
            double d6 = target.boundingBox.minY + (double)(target.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
            double d7 = target.posZ - this.posZ;
            double d1 = target.posX - this.posX;
            double d2 = target.posZ - this.posZ;
            double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
            double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - target.posY) * 0.25D;
            double var10000 = d1 * d3;
            var10000 = d2 * d3;
            EntityTempestBall snowball = new EntityTempestBall(this.worldObj, this, d5, d6, d7);
            snowball.posY = this.posY + 1.0D;
            this.worldObj.playSoundAtEntity(this, "aether:aemob.zephyr.shoot", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(snowball);
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {}

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump() {}

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.zephyr.say";
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
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(i, j, k) >= 0.0F && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0 && !this.worldObj.isDaytime();
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}
