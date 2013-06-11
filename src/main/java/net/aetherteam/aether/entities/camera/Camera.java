package net.aetherteam.aether.entities.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Camera extends EntityLiving
{
    Minecraft mc;
    Entity entityAttachedTo;
    double offsetX;
    double offsetY;
    double offsetZ;

    public Camera(World var1)
    {
        super(var1);
        this.mc = Minecraft.getMinecraft();
    }

    public Camera(World var1, Entity var2, double var3, double var5, double var7)
    {
        this(var1);

        if (var2 == null)
        {
            this.setDead();
        } else
        {
            this.entityAttachedTo = var2;

            if (this.entityAttachedTo != null)
            {
                this.mountEntity(this.entityAttachedTo);
            }

            this.offsetX = var3;
            this.offsetY = var5;
            this.offsetZ = var7;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.entityAttachedTo != null)
        {
            this.mountEntity(this.entityAttachedTo);
            this.setRotation(this.entityAttachedTo.rotationYaw, this.entityAttachedTo.rotationPitch);
        } else
        {
            CameraManager.turnOffCamera(this.worldObj);
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.offsetX = var1.getDouble("offsetX");
        this.offsetY = var1.getDouble("offsetY");
        this.offsetZ = var1.getDouble("offsetZ");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setDouble("offsetX", this.offsetX);
        var1.setDouble("offsetY", this.offsetY);
        var1.setDouble("offsetZ", this.offsetZ);
    }

    public int getMaxHealth()
    {
        return 0;
    }
}
