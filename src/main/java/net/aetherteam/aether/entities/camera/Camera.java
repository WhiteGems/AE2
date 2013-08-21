package net.aetherteam.aether.entities.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Camera extends EntityLiving
{
    Minecraft mc = Minecraft.getMinecraft();
    Entity entityAttachedTo;
    double offsetX;
    double offsetY;
    double offsetZ;

    public Camera(World world, Entity entity, double offsetX, double offsetY, double offsetZ)
    {
        super(world);

        if (entity == null)
        {
            this.setDead();
        }
        else
        {
            this.entityAttachedTo = entity;

            if (this.entityAttachedTo != null)
            {
                this.mountEntity(this.entityAttachedTo);
            }

            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(0.0D);
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
        }
        else
        {
            CameraManager.turnOffCamera(this.worldObj);
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.offsetX = nbttagcompound.getDouble("offsetX");
        this.offsetY = nbttagcompound.getDouble("offsetY");
        this.offsetZ = nbttagcompound.getDouble("offsetZ");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setDouble("offsetX", this.offsetX);
        nbttagcompound.setDouble("offsetY", this.offsetY);
        nbttagcompound.setDouble("offsetZ", this.offsetZ);
    }
}
