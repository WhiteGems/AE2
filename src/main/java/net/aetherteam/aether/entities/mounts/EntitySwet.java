package net.aetherteam.aether.entities.mounts;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
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
        this.health = 25;

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
            this.texture = (this.dir + "/mobs/swet/swet_blue.png");
            this.moveSpeed = 1.5F;
        }
        else
        {
            this.texture = (this.dir + "/mobs/swet/swet_golden.png");
            this.moveSpeed = 3.0F;
        }

        setSize(0.8F, 0.8F);
        setPosition(this.posX, this.posY, this.posZ);
        this.hops = 0;
        this.gotrider = false;
        this.flutter = 0;
        this.ticker = 0;
        this.slimeJumpDelay = (this.rand.nextInt(20) + 10);
    }

    public void updateRidden()
    {
        super.updateRidden();

        if ((this.riddenByEntity != null) && (this.kickoff))
        {
            this.riddenByEntity.mountEntity(this);
            this.kickoff = false;
        }
    }

    public void updateRiderPosition()
    {
        this.riddenByEntity.setPosition(this.posX, this.boundingBox.minY - 0.300000011920929D + this.riddenByEntity.yOffset, this.posZ);
    }

    public void onUpdate()
    {
        if (this.entityToAttack != null)
        {
            for (int i = 0; i < 3; i++)
            {
                float f = 0.0174528F;
                double d = (float)this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
                double d1 = (float)this.posY + this.height;
                double d2 = (float)this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
                this.worldObj.spawnParticle("splash", d, d1 - 0.25D, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        super.onUpdate();

        if (this.gotrider)
        {
            if (this.riddenByEntity != null)
            {
                return;
            }

            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
            int j = 0;

            if (j < list.size())
            {
                Entity entity = (Entity)list.get(j);
                capturePrey(entity);
            }

            this.gotrider = false;
        }

        if (handleWaterMovement())
        {
            dissolve();
        }
    }

    protected boolean canDespawn()
    {
        return true;
    }

    public void fall(float f)
    {
        if (this.friendly)
        {
            return;
        }

        super.fall(f);

        if ((this.hops >= 3) && (this.health > 0))
        {
            dissolve();
        }
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
        if ((this.riddenByEntity != null) && (entity == this.riddenByEntity))
        {
            return;
        }

        super.knockBack(entity, i, d, d1);
    }

    public void dissolve()
    {
        for (int i = 0; i < 50; i++)
        {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float f1 = this.rand.nextFloat() * 0.5F + 0.25F;
            float f2 = MathHelper.sin(f) * f1;
            float f3 = MathHelper.cos(f) * f1;
            this.worldObj.spawnParticle("splash", this.posX + f2, this.boundingBox.minY + 1.25D, this.posZ + f3, f2 * 1.5D + this.motionX, 4.0D, f3 * 1.5D + this.motionZ);
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.posY += this.riddenByEntity.yOffset - 0.3F;
            this.riddenByEntity.mountEntity(this);
        }

        setDead();
    }

    public void setDead()
    {
        super.setDead();
    }

    public void capturePrey(Entity entity)
    {
        splorch();
        this.prevPosX = (this.posX = entity.posX);
        this.prevPosY = (this.posY += 0.009999999776482582D);
        this.prevPosZ = (this.posZ = entity.posZ);
        this.prevRotationYaw = (this.rotationYaw = entity.rotationYaw);
        this.prevRotationPitch = (this.rotationPitch = entity.rotationPitch);
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionZ = entity.motionZ;
        setSize(entity.width, entity.height);
        setPosition(this.posX, this.posY, this.posZ);
        entity.mountEntity(this);
        this.rotationYaw = (this.rand.nextFloat() * 360.0F);
    }

    public boolean attackEntityFrom(EntityLiving entity, int i)
    {
        if ((this.hops == 3) && (entity == null) && (this.health > 1))
        {
            this.health = 1;
        }

        boolean flag = super.attackEntityFrom(DamageSource.causeMobDamage(entity), i);

        if ((flag) && (this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityLiving)))
        {
            if ((entity != null) && (this.riddenByEntity == entity))
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.kickoff = true;
                }
            }
            else
            {
                this.riddenByEntity.attackEntityFrom(DamageSource.causeMobDamage(this), i);

                if (this.health <= 0)
                {
                    this.kickoff = true;
                }
            }
        }

        if ((flag) && (this.health <= 0))
        {
            dissolve();
        }
        else if ((flag) && ((entity instanceof EntityLiving)))
        {
            EntityLiving entityliving = entity;

            if ((entityliving.getHealth() > 0) && ((this.riddenByEntity == null) || (entityliving != this.riddenByEntity)))
            {
                this.entityToAttack = entity;
                faceEntity(entity, 180.0F, 180.0F);
                this.kickoff = true;
            }
        }

        if ((this.friendly) && ((this.entityToAttack instanceof EntityPlayer)))
        {
            this.entityToAttack = null;
        }

        return flag;
    }

    public void updateEntityActionState()
    {
        this.entityAge += 1;
        despawnEntity();

        if ((this.friendly) && (this.riddenByEntity != null))
        {
            return;
        }

        if ((this.entityToAttack == null) && (this.riddenByEntity == null) && (this.health > 0))
        {
            if ((this.onGround) && (this.slimeJumpDelay-- <= 0))
            {
                this.slimeJumpDelay = getJumpDelay();
                this.isJumping = true;
                this.worldObj.playSoundAtEntity(this, getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
                this.moveStrafing = (1.0F - this.rand.nextFloat() * 2.0F);
                this.moveForward = 16.0F;
            }
            else
            {
                this.isJumping = false;

                if (this.onGround)
                {
                    this.moveStrafing = (this.moveForward = 0.0F);
                }
            }
        }

        if ((!this.onGround) && (this.isJumping))
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

        if ((this.entityToAttack != null) && (this.riddenByEntity == null) && (this.health > 0))
        {
            faceEntity(this.entityToAttack, 10.0F, 10.0F);
        }

        if ((this.entityToAttack != null) && (this.entityToAttack.isDead))
        {
            this.entityToAttack = null;
        }

        if ((!this.onGround) && (this.motionY < 0.0500000007450581D) && (this.flutter > 0))
        {
            this.motionY += 0.07000000029802322D;
            this.flutter -= 1;
        }

        if (this.ticker < 4)
        {
            this.ticker += 1;
        }
        else
        {
            if ((this.onGround) && (this.riddenByEntity == null) && (this.hops != 0) && (this.hops != 3))
            {
                this.hops = 0;
            }

            if ((this.entityToAttack == null) && (this.riddenByEntity == null))
            {
                Entity entity = getPrey();

                if (entity != null)
                {
                    this.entityToAttack = entity;
                }
            }
            else if ((this.entityToAttack != null) && (this.riddenByEntity == null))
            {
                if (getDistanceToEntity(this.entityToAttack) <= 9.0F)
                {
                    if ((this.onGround) && (canEntityBeSeen(this.entityToAttack)))
                    {
                        splotch();
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
            else if ((this.riddenByEntity != null) && (this.onGround))
            {
                if (this.hops == 0)
                {
                    splotch();
                    this.onGround = false;
                    this.motionY = 0.3499999940395355D;
                    this.moveForward = 0.8F;
                    this.hops = 1;
                    this.flutter = 5;
                    this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                }
                else if (this.hops == 1)
                {
                    splotch();
                    this.onGround = false;
                    this.motionY = 0.449999988079071D;
                    this.moveForward = 0.9F;
                    this.hops = 2;
                    this.flutter = 5;
                    this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
                }
                else if (this.hops == 2)
                {
                    splotch();
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

        if ((this.onGround) && (this.hops >= 3))
        {
            dissolve();
        }
    }

    protected int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

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
            this.texture = "/aether/mobs/swets.png";
            this.moveSpeed = 1.5F;
        }
        else
        {
            this.texture = "/aether/mobs/goldswets.png";
            this.moveSpeed = 3.0F;
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

    protected String getHurtSound()
    {
        return "mob.slime.big";
    }

    protected String getDeathSound()
    {
        return "mob.slime.big";
    }

    public void applyEntityCollision(Entity entity)
    {
        if ((this.hops == 0) && (this.riddenByEntity == null) && (this.entityToAttack != null) && (entity != null) && (entity == this.entityToAttack) && ((entity.ridingEntity == null) || (!(entity.ridingEntity instanceof EntitySwet))))
        {
            if (entity.riddenByEntity != null)
            {
                entity.riddenByEntity.mountEntity(entity);
            }

            capturePrey(entity);
        }

        super.applyEntityCollision(entity);
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if (!this.worldObj.isRemote)
        {
            if (!this.friendly)
            {
                return true;
            }

            if (((this.friendly) && (this.riddenByEntity == null)) || (this.riddenByEntity != entityplayer));
        }

        return true;
    }

    protected Entity getPrey()
    {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(6.0D, 6.0D, 6.0D));

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (((entity instanceof EntityLiving)) && (!(entity instanceof EntitySwet)) && (this.friendly ? !(entity instanceof EntityPlayer) : !(entity instanceof EntityMob)))
            {
                return entity;
            }
        }

        return null;
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        ItemStack stack = new ItemStack(this.textureNum == 1 ? AetherItems.SwettyBall.itemID : AetherItems.SwettyBall.itemID, 6, this.textureNum == 1 ? 1 : 0);
        entityDropItem(stack, 0.0F);
    }

    public int getMaxHealth()
    {
        return 25;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return this;
    }
}

