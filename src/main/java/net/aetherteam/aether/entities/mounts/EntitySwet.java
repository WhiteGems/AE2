package net.aetherteam.aether.entities.mounts;

import java.util.List;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySwet extends EntityAetherAnimal
{
    private int slimeJumpDelay = 0;
    public int ticker;
    public int flutter;
    public int hops;
    public int textureNum;
    public boolean textureSet;
    public boolean gotrider;
    public boolean kickoff;
    public boolean friendly;

    public EntitySwet(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(25.0D);
        this.setEntityHealth(25.0F);

        if (!this.textureSet)
        {
            if (this.rand.nextInt(2) == 0)
            {
                this.textureNum = 2;
            }
            else
            {
                this.textureNum = 1;
            }

            this.textureSet = true;
        }

        if (this.textureNum == 1)
        {
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.5D);
        }
        else
        {
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(3.0D);
        }

        this.setSize(0.8F, 0.8F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.hops = 0;
        this.gotrider = false;
        this.flutter = 0;
        this.ticker = 0;
        this.slimeJumpDelay = this.rand.nextInt(20) + 10;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();

        if (this.riddenByEntity != null && this.kickoff)
        {
            this.riddenByEntity.mountEntity(this);
            this.kickoff = false;
        }
    }

    public void updateRiderPosition()
    {
        this.riddenByEntity.setPosition(this.posX, this.boundingBox.minY - 0.30000001192092896D + (double)this.riddenByEntity.yOffset, this.posZ);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.entityToAttack != null)
        {
            for (int list = 0; list < 3; ++list)
            {
                float j = 0.01745278F;
                double entity = (double)((float)this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
                double d1 = (double)((float)this.posY + this.height);
                double d2 = (double)((float)this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
                this.worldObj.spawnParticle("splash", entity, d1 - 0.25D, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        super.onUpdate();

        if (this.gotrider)
        {
            if (this.riddenByEntity != null)
            {
                return;
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
            byte var10 = 0;

            if (var10 < var9.size())
            {
                Entity var11 = (Entity)var9.get(var10);
                this.capturePrey(var11);
            }

            this.gotrider = false;
        }

        if (this.handleWaterMovement())
        {
            this.dissolve();
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return true;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float f)
    {
        if (!this.friendly)
        {
            super.fall(f);

            if (this.hops >= 3 && this.func_110143_aJ() > 0.0F)
            {
                this.dissolve();
            }
        }
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
        if (this.riddenByEntity == null || entity != this.riddenByEntity)
        {
            super.knockBack(entity, (float)i, d, d1);
        }
    }

    public void dissolve()
    {
        for (int i = 0; i < 50; ++i)
        {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float f1 = this.rand.nextFloat() * 0.5F + 0.25F;
            float f2 = MathHelper.sin(f) * f1;
            float f3 = MathHelper.cos(f) * f1;
            this.worldObj.spawnParticle("splash", this.posX + (double)f2, this.boundingBox.minY + 1.25D, this.posZ + (double)f3, (double)f2 * 1.5D + this.motionX, 4.0D, (double)f3 * 1.5D + this.motionZ);
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.posY += (double)(this.riddenByEntity.yOffset - 0.3F);
            this.riddenByEntity.mountEntity(this);
        }

        this.setDead();
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }

    public void capturePrey(Entity entity)
    {
        this.splorch();
        this.prevPosX = this.posX = entity.posX;
        this.prevPosY = this.posY = entity.posY + 0.009999999776482582D;
        this.prevPosZ = this.posZ = entity.posZ;
        this.prevRotationYaw = this.rotationYaw = entity.rotationYaw;
        this.prevRotationPitch = this.rotationPitch = entity.rotationPitch;
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionZ = entity.motionZ;
        this.setSize(entity.width, entity.height);
        this.setPosition(this.posX, this.posY, this.posZ);
        entity.mountEntity(this);
        this.rotationYaw = this.rand.nextFloat() * 360.0F;
    }

    public boolean attackEntityFrom(EntityLiving entity, int i)
    {
        if (this.hops == 3 && entity == null && this.func_110143_aJ() > 1.0F)
        {
            this.setEntityHealth(1.0F);
        }

        boolean flag = super.attackEntityFrom(DamageSource.causeMobDamage(entity), (float)i);

        if (flag && this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving)
        {
            if (entity != null && this.riddenByEntity == entity)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.kickoff = true;
                }
            }
            else
            {
                this.riddenByEntity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)i);

                if (this.func_110143_aJ() <= 0.0F)
                {
                    this.kickoff = true;
                }
            }
        }

        if (flag && this.func_110143_aJ() <= 0.0F)
        {
            this.dissolve();
        }
        else if (flag && entity instanceof EntityLiving && entity.func_110143_aJ() > 0.0F && (this.riddenByEntity == null || entity != this.riddenByEntity))
        {
            this.entityToAttack = entity;
            this.faceEntity(entity, 180.0F, 180.0F);
            this.kickoff = true;
        }

        if (this.friendly && this.entityToAttack instanceof EntityPlayer)
        {
            this.entityToAttack = null;
        }

        return flag;
    }

    public void updateEntityActionState()
    {
        ++this.entityAge;
        this.despawnEntity();

        if (!this.friendly || this.riddenByEntity == null)
        {
            if (this.entityToAttack == null && this.riddenByEntity == null && this.func_110143_aJ() > 0.0F)
            {
                if (this.onGround && this.slimeJumpDelay-- <= 0)
                {
                    this.slimeJumpDelay = this.getJumpDelay();
                    this.isJumping = true;
                    this.worldObj.playSoundAtEntity(this, this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
                    this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
                    this.moveForward = 16.0F;
                }
                else
                {
                    this.isJumping = false;

                    if (this.onGround)
                    {
                        this.moveStrafing = this.moveForward = 0.0F;
                    }
                }
            }

            if (!this.onGround && this.isJumping)
            {
                this.isJumping = false;
            }
            else if (this.onGround)
            {
                if (this.moveForward > 0.05F)
                {
                    this.moveForward *= 0.75F;
                }
                else
                {
                    this.moveForward = 0.0F;
                }
            }

            if (this.entityToAttack != null && this.riddenByEntity == null && this.func_110143_aJ() > 0.0F)
            {
                this.faceEntity(this.entityToAttack, 10.0F, 10.0F);
            }

            if (this.entityToAttack != null && this.entityToAttack.isDead)
            {
                this.entityToAttack = null;
            }

            if (!this.onGround && this.motionY < 0.05000000074505806D && this.flutter > 0)
            {
                this.motionY += 0.07000000029802322D;
                --this.flutter;
            }

            if (this.ticker < 4)
            {
                ++this.ticker;
            }
            else
            {
                if (this.onGround && this.riddenByEntity == null && this.hops != 0 && this.hops != 3)
                {
                    this.hops = 0;
                }

                if (this.entityToAttack == null && this.riddenByEntity == null)
                {
                    Entity entity = this.getPrey();

                    if (entity != null)
                    {
                        this.entityToAttack = entity;
                    }
                }
                else if (this.entityToAttack != null && this.riddenByEntity == null)
                {
                    if (this.getDistanceToEntity(this.entityToAttack) <= 9.0F)
                    {
                        if (this.onGround && this.canEntityBeSeen(this.entityToAttack))
                        {
                            this.splotch();
                            this.flutter = 10;
                            this.isJumping = true;
                            this.moveForward = 1.0F;
                            this.rotationYaw += 5.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                        }
                    }
                    else
                    {
                        this.entityToAttack = null;
                        this.isJumping = false;
                        this.moveForward = 0.0F;
                    }
                }
                else if (this.riddenByEntity != null && this.onGround)
                {
                    if (this.hops == 0)
                    {
                        this.splotch();
                        this.onGround = false;
                        this.motionY = 0.3499999940395355D;
                        this.moveForward = 0.8F;
                        this.hops = 1;
                        this.flutter = 5;
                        this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                    }
                    else if (this.hops == 1)
                    {
                        this.splotch();
                        this.onGround = false;
                        this.motionY = 0.44999998807907104D;
                        this.moveForward = 0.9F;
                        this.hops = 2;
                        this.flutter = 5;
                        this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                    }
                    else if (this.hops == 2)
                    {
                        this.splotch();
                        this.onGround = false;
                        this.motionY = 1.25D;
                        this.moveForward = 1.25F;
                        this.hops = 3;
                        this.flutter = 5;
                        this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                    }
                }

                this.ticker = 0;
            }

            if (this.onGround && this.hops >= 3)
            {
                this.dissolve();
            }
        }
    }

    protected int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Hops", (short)this.hops);
        nbttagcompound.setShort("Flutter", (short)this.flutter);

        if (this.riddenByEntity != null)
        {
            this.gotrider = true;
        }

        nbttagcompound.setBoolean("GotRider", this.gotrider);
        nbttagcompound.setBoolean("Friendly", this.friendly);
        nbttagcompound.setBoolean("textureSet", this.textureSet);
        nbttagcompound.setShort("textureNum", (short)this.textureNum);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.hops = nbttagcompound.getShort("Hops");
        this.flutter = nbttagcompound.getShort("Flutter");
        this.gotrider = nbttagcompound.getBoolean("GotRider");
        this.friendly = nbttagcompound.getBoolean("Friendly");
        this.textureSet = nbttagcompound.getBoolean("textureSet");
        this.textureNum = nbttagcompound.getShort("textureNum");

        if (this.textureNum == 1)
        {
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.5D);
        }
        else
        {
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(3.0D);
        }
    }

    public void splorch()
    {
        this.worldObj.playSoundAtEntity(this, "mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    public void splotch()
    {
        this.worldObj.playSoundAtEntity(this, "mob.slime.big", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    protected String getJumpSound()
    {
        return "mob.slime.big";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime.big";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime.big";
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entity)
    {
        if (this.hops == 0 && this.riddenByEntity == null && this.entityToAttack != null && entity != null && entity == this.entityToAttack && (entity.ridingEntity == null || !(entity.ridingEntity instanceof EntitySwet)))
        {
            if (entity.riddenByEntity != null)
            {
                entity.riddenByEntity.mountEntity(entity);
            }

            this.capturePrey(entity);
        }

        super.applyEntityCollision(entity);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        if (!this.worldObj.isRemote)
        {
            if (!this.friendly)
            {
                return true;
            }

            if ((!this.friendly || this.riddenByEntity != null) && this.riddenByEntity == entityplayer)
            {
                ;
            }
        }

        return true;
    }

    protected Entity getPrey()
    {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(6.0D, 6.0D, 6.0D));
        int i = 0;
        Entity entity;

        while (true)
        {
            if (i >= list.size())
            {
                return null;
            }

            entity = (Entity)list.get(i);

            if (entity instanceof EntityLiving && !(entity instanceof EntitySwet))
            {
                if (this.friendly)
                {
                    if (!(entity instanceof EntityPlayer))
                    {
                        break;
                    }
                }
                else if (!(entity instanceof EntityMob))
                {
                    break;
                }
            }

            ++i;
        }

        return entity;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        ItemStack stack = new ItemStack(this.textureNum == 1 ? AetherItems.SwettyBall.itemID : AetherItems.SwettyBall.itemID, 6, this.textureNum == 1 ? 1 : 0);
        this.entityDropItem(stack, 0.0F);
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return this;
    }
}
