package net.aetherteam.aether.entities.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Camera extends EntityLiving
{
    Minecraft mc = Minecraft.getMinecraft();
    Entity entityAttachedTo;
    double offsetX;
    double offsetY;
    double offsetZ;

    public Camera(World world)
    {
        super(world);
    }

    public Camera(World world, Entity entity, double offsetX, double offsetY, double offsetZ)
    {
        this(world);

        if (entity == null)
        {
            setDead();
            return;
        }

        this.entityAttachedTo = entity;

        if (this.entityAttachedTo != null)
        {
            mountEntity(this.entityAttachedTo);
        }

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void onUpdate()
    {
        if (this.entityAttachedTo != null)
        {
            mountEntity(this.entityAttachedTo);
            setRotation(this.entityAttachedTo.rotationYaw, this.entityAttachedTo.rotationPitch);
        }
        else
        {
            CameraManager.turnOffCamera(this.worldObj);
            setDead();
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.offsetX = nbttagcompound.getDouble("offsetX");
        this.offsetY = nbttagcompound.getDouble("offsetY");
        this.offsetZ = nbttagcompound.getDouble("offsetZ");
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setDouble("offsetX", this.offsetX);
        nbttagcompound.setDouble("offsetY", this.offsetY);
        nbttagcompound.setDouble("offsetZ", this.offsetZ);
    }

    public int getMaxHealth()
    {
        return 0;
    }
}

