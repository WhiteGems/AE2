package net.aetherteam.aether.entities.mounts;

import java.util.List;
import net.aetherteam.aether.entities.EntityAetherAnimal;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
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

    public EntitySwet(World var1)
    {
        super(var1);
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
            this.texture = this.dir + "/mobs/swet/swet_blue.png";
            this.moveSpeed = 1.5F;
        }
        else
        {
            this.texture = this.dir + "/mobs/swet/swet_golden.png";
            this.moveSpeed = 3.0F;
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
            for (int var1 = 0; var1 < 3; ++var1)
            {
                float var2 = 0.01745278F;
                double var3 = (double)((float)this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
                double var5 = (double)((float)this.posY + this.height);
                double var7 = (double)((float)this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
                this.worldObj.spawnParticle("splash", var3, var5 - 0.25D, var7, 0.0D, 0.0D, 0.0D);
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
    public void fall(float var1)
    {
        if (!this.friendly)
        {
            super.fall(var1);

            if (this.hops >= 3 && this.health > 0)
            {
                this.dissolve();
            }
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        if (this.riddenByEntity == null || var1 != this.riddenByEntity)
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public void dissolve()
    {
        for (int var1 = 0; var1 < 50; ++var1)
        {
            float var2 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            float var3 = this.rand.nextFloat() * 0.5F + 0.25F;
            float var4 = MathHelper.sin(var2) * var3;
            float var5 = MathHelper.cos(var2) * var3;
            this.worldObj.spawnParticle("splash", this.posX + (double)var4, this.boundingBox.minY + 1.25D, this.posZ + (double)var5, (double)var4 * 1.5D + this.motionX, 4.0D, (double)var5 * 1.5D + this.motionZ);
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

    public void capturePrey(Entity var1)
    {
        this.splorch();
        this.prevPosX = this.posX = var1.posX;
        this.prevPosY = this.posY = var1.posY + 0.009999999776482582D;
        this.prevPosZ = this.posZ = var1.posZ;
        this.prevRotationYaw = this.rotationYaw = var1.rotationYaw;
        this.prevRotationPitch = this.rotationPitch = var1.rotationPitch;
        this.motionX = var1.motionX;
        this.motionY = var1.motionY;
        this.motionZ = var1.motionZ;
        this.setSize(var1.width, var1.height);
        this.setPosition(this.posX, this.posY, this.posZ);
        var1.mountEntity(this);
        this.rotationYaw = this.rand.nextFloat() * 360.0F;
    }

    public boolean attackEntityFrom(EntityLiving var1, int var2)
    {
        if (this.hops == 3 && var1 == null && this.health > 1)
        {
            this.health = 1;
        }

        boolean var3 = super.attackEntityFrom(DamageSource.causeMobDamage(var1), var2);

        if (var3 && this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving)
        {
            if (var1 != null && this.riddenByEntity == var1)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.kickoff = true;
                }
            }
            else
            {
                this.riddenByEntity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);

                if (this.health <= 0)
                {
                    this.kickoff = true;
                }
            }
        }

        if (var3 && this.health <= 0)
        {
            this.dissolve();
        }
        else if (var3 && var1 instanceof EntityLiving && var1.getHealth() > 0 && (this.riddenByEntity == null || var1 != this.riddenByEntity))
        {
            this.entityToAttack = var1;
            this.faceEntity(var1, 180.0F, 180.0F);
            this.kickoff = true;
        }

        if (this.friendly && this.entityToAttack instanceof EntityPlayer)
        {
            this.entityToAttack = null;
        }

        return var3;
    }

    public void updateEntityActionState()
    {
        ++this.entityAge;
        this.despawnEntity();

        if (!this.friendly || this.riddenByEntity == null)
        {
            if (this.entityToAttack == null && this.riddenByEntity == null && this.health > 0)
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

            if (this.entityToAttack != null && this.riddenByEntity == null && this.health > 0)
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
                    Entity var1 = this.getPrey();

                    if (var1 != null)
                    {
                        this.entityToAttack = var1;
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
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("Hops", (short)this.hops);
        var1.setShort("Flutter", (short)this.flutter);

        if (this.riddenByEntity != null)
        {
            this.gotrider = true;
        }

        var1.setBoolean("GotRider", this.gotrider);
        var1.setBoolean("Friendly", this.friendly);
        var1.setBoolean("textureSet", this.textureSet);
        var1.setShort("textureNum", (short)this.textureNum);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.hops = var1.getShort("Hops");
        this.flutter = var1.getShort("Flutter");
        this.gotrider = var1.getBoolean("GotRider");
        this.friendly = var1.getBoolean("Friendly");
        this.textureSet = var1.getBoolean("textureSet");
        this.textureNum = var1.getShort("textureNum");

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
    public void applyEntityCollision(Entity var1)
    {
        if (this.hops == 0 && this.riddenByEntity == null && this.entityToAttack != null && var1 != null && var1 == this.entityToAttack && (var1.ridingEntity == null || !(var1.ridingEntity instanceof EntitySwet)))
        {
            if (var1.riddenByEntity != null)
            {
                var1.riddenByEntity.mountEntity(var1);
            }

            this.capturePrey(var1);
        }

        super.applyEntityCollision(var1);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (!this.worldObj.isRemote)
        {
            if (!this.friendly)
            {
                return true;
            }

            if ((!this.friendly || this.riddenByEntity != null) && this.riddenByEntity == var1)
            {
                ;
            }
        }

        return true;
    }

    protected Entity getPrey()
    {
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(6.0D, 6.0D, 6.0D));
        int var2 = 0;
        Entity var3;

        while (true)
        {
            if (var2 >= var1.size())
            {
                return null;
            }

            var3 = (Entity)var1.get(var2);

            if (var3 instanceof EntityLiving && !(var3 instanceof EntitySwet))
            {
                if (this.friendly)
                {
                    if (!(var3 instanceof EntityPlayer))
                    {
                        break;
                    }
                }
                else if (!(var3 instanceof EntityMob))
                {
                    break;
                }
            }

            ++var2;
        }

        return var3;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        ItemStack var3 = new ItemStack(this.textureNum == 1 ? AetherItems.SwettyBall.itemID : AetherItems.SwettyBall.itemID, 6, this.textureNum == 1 ? 1 : 0);
        this.entityDropItem(var3, 0.0F);
    }

    public int getMaxHealth()
    {
        return 25;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return this;
    }
}
