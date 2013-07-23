package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTempest extends EntityAetherMob
    implements IMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private int heightOffsetUpdateTime;
    private float heightOffset = 1.5F;
    private int attackTimer;
    public float sinage;
    public int timeUntilShoot = 30;

    public EntityTempest(World world)
    {
        super(world);
        this.texture = (this.dir + "/mobs/tempest/tempest.png");
        setSize(1.25F, 1.5F);
        this.isImmuneToFire = true;
        this.attackTime = this.timeUntilShoot;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.jumpMovementFactor = 0.0F;

        if (this.health > 0)
        {
            double a = this.rand.nextFloat() - 0.5F;
            double b = this.rand.nextFloat();
            double c = this.rand.nextFloat() - 0.5F;
            double d = this.posX + a * b;
            double e = this.boundingBox.minY + b - 0.300000011920929D;
            double f = this.posZ + c * b;
            this.worldObj.spawnParticle("reddust", d, e, f, 1.0D, 1.0D, 1.0D);
        }

        if (this.entityToAttack != null)
        {
            attackEntity(this.entityToAttack, getDistanceToEntity(this.entityToAttack));
        }
    }

    public void onLivingUpdate()
    {
        if ((this.worldObj.v()) && (!this.worldObj.isRemote))
        {
            damageEntity(DamageSource.drown, 1);
        }

        if (this.attackTimer > 0)
        {
            this.attackTimer -= 1;
        }

        if (!this.worldObj.isRemote)
        {
            this.heightOffsetUpdateTime -= 1;

            if (this.heightOffsetUpdateTime <= 0)
            {
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = (1.5F + (float)this.rand.nextGaussian() * 3.0F);
            }

            if ((getEntityToAttack() != null) && (getEntityToAttack().posY + getEntityToAttack().getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset))
            {
                this.motionY += (0.700000011920929D - this.motionY) * 0.700000011920929D;
            }
        }

        if ((!this.onGround) && (this.motionY < 0.0D))
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

    protected void attackEntity(Entity entity, float f)
    {
        if ((entity instanceof EntityLiving))
        {
            EntityLiving target = (EntityLiving)entity;

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (target != null)
                {
                    if ((target.isDead) || (target.getDistanceToEntity(this) > 12.0D) || ((target instanceof EntityNewZephyr)) || ((target instanceof EntityTempest)))
                    {
                        target = null;
                        this.entityToAttack = null;
                        return;
                    }

                    if (this.attackTime >= this.timeUntilShoot)
                    {
                        shootTarget(target);
                    }

                    if ((this.attackTime >= this.timeUntilShoot) && (canEntityBeSeen(target)))
                    {
                        this.attackTime = -10;
                    }

                    if (this.attackTime < this.timeUntilShoot)
                    {
                        this.attackTime += 1;
                    }
                }

                this.rotationYaw = ((float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F);
            }
        }
    }

    public void shootTarget(EntityLiving target)
    {
        if (this.worldObj.difficultySetting == 0)
        {
            return;
        }

        double d5 = target.posX - this.posX;
        double d6 = target.boundingBox.minY + target.height / 2.0F - (this.posY + this.height / 2.0F);
        double d7 = target.posZ - this.posZ;
        double d1 = target.posX - this.posX;
        double d2 = target.posZ - this.posZ;
        double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
        double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - target.posY) * 0.25D;
        d1 *= d3;
        d2 *= d3;
        EntityTempestBall snowball = new EntityTempestBall(this.worldObj, this, d5, d6, d7);
        this.posY += 1.0D;
        this.worldObj.playSoundAtEntity(this, "aemob.zephyr.shoot", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

        if (!this.worldObj.isRemote)
        {
            this.worldObj.spawnEntityInWorld(snowball);
        }
    }

    protected void fall(float par1)
    {
    }

    protected void jump()
    {
    }

    public int getMaxHealth()
    {
        return 15;
    }

    protected String getLivingSound()
    {
        return "aemob.zephyr.say";
    }

    protected String getHurtSound()
    {
        return "aemob.zephyr.say";
    }

    protected String getDeathSound()
    {
        return "aemob.zephyr.say";
    }

    public boolean canDespawn()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.rand.nextInt(25) == 0) && (getBlockPathWeight(i, j, k) >= 0.0F) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox)) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID) && (this.worldObj.difficultySetting > 0) && (!this.worldObj.v());
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}

