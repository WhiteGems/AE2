package net.aetherteam.aether.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAetherCoin extends Entity
{
    public float spinSpeed = 0.0F;
    public int xpOrbAge = 0;
    public int field_70532_c;
    private int xpOrbHealth = 5;
    private EntityPlayer closestPlayer;

    /** Threshold color for tracking players */
    private int xpTargetColor;

    public EntityAetherCoin(World par1World, double par2, double par4, double par6, int value)
    {
        super(par1World);
        this.setSize(0.5F, 0.5F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.setCoinValue(value);

        if (this.getCoinValue() <= 0)
        {
            this.setDead();
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityAetherCoin(World par1World)
    {
        super(par1World);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Short.valueOf((short)0));
    }

    public int getCoinValue()
    {
        return this.dataWatcher.getWatchableObjectShort(16);
    }

    public void setCoinValue(int value)
    {
        this.dataWatcher.updateObject(16, Short.valueOf((short)value));
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        float var2 = 0.5F;

        if (var2 < 0.0F)
        {
            var2 = 0.0F;
        }

        if (var2 > 1.0F)
        {
            var2 = 1.0F;
        }

        int var3 = super.getBrightnessForRender(par1);
        int var4 = var3 & 255;
        int var5 = var3 >> 16 & 255;
        var4 += (int)(var2 * 15.0F * 16.0F);

        if (var4 > 240)
        {
            var4 = 240;
        }

        return var4 | var5 << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.spinSpeed += 0.35F;

        if (this.field_70532_c > 0)
        {
            --this.field_70532_c;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746D;

        if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava)
        {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        double var1 = 8.0D;

        if (this.closestPlayer != null)
        {
            double var13 = (this.closestPlayer.posX - this.posX) / var1;
            double var5 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / var1;
            double var7 = (this.closestPlayer.posZ - this.posZ) / var1;
            double var9 = Math.sqrt(var13 * var13 + var5 * var5 + var7 * var7);
            double var11 = 1.0D - var9;

            if (var11 > 0.0D)
            {
                var11 *= var11;
                this.motionX += var13 / var9 * var11 * 0.1D;
                this.motionY += var5 / var9 * var11 * 0.1D;
                this.motionZ += var7 / var9 * var11 * 0.1D;
            }
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float var131 = 0.98F;

        if (this.onGround)
        {
            var131 = 0.58800006F;
            int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

            if (var4 > 0)
            {
                var131 = Block.blocksList[var4].slipperiness * 0.98F;
            }
        }

        this.motionX *= (double)var131;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double)var131;

        if (this.onGround)
        {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
        {
            this.setDead();
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int par1)
    {
        this.attackEntityFrom(DamageSource.inFire, par1);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        this.setBeenAttacked();
        this.xpOrbHealth -= par2;

        if (this.xpOrbHealth <= 0)
        {
            this.setDead();
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)((byte)this.xpOrbHealth));
        par1NBTTagCompound.setShort("Age", (short)this.xpOrbAge);
        par1NBTTagCompound.setShort("Value", (short)this.getCoinValue());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.xpOrbHealth = par1NBTTagCompound.getShort("Health") & 255;
        this.xpOrbAge = par1NBTTagCompound.getShort("Age");
        this.setCoinValue(par1NBTTagCompound.getShort("Value"));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && this.field_70532_c == 0 && par1EntityPlayer.xpCooldown == 0)
        {
            par1EntityPlayer.xpCooldown = 2;
            this.worldObj.playSoundAtEntity(this, "aether:aemisc.coin", 0.3F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
            par1EntityPlayer.onItemPickup(this, 1);
            Aether.getServerPlayer(par1EntityPlayer).addCoins(this.getCoinValue());
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    public float getSpinSpeed()
    {
        return this.spinSpeed;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }
}
