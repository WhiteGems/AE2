package net.aetherteam.aether.entities;

import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityZephyroo extends EntityAetherAnimal
{
    private int timeTilJump;

    public EntityZephyroo(World world)
    {
        super(world);
        this.timeTilJump = 10;
        this.texture = (this.dir + "/mobs/Zephyroo.png");
        this.moveSpeed = 5.0F;
        setSize(0.5F, 2.0F);
    }

    public float getSpeedModifier()
    {
        return 3.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        List entitiesAround = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
        boolean foundPlayer = false;

        for (Entity entity : entitiesAround)
        {
            if ((entity instanceof EntityPlayer))
            {
                EntityPlayer player = (EntityPlayer)entity;

                if (player.username.equalsIgnoreCase("ClashJTM"))
                {
                    foundPlayer = true;
                    makeLoveToClash(true);
                    this.inLove = 1;
                    attackEntity(player, 20.0F);
                    faceEntity(player, 5.5F, getVerticalFaceSpeed());
                }
                else
                {
                    makeLoveToClash(false);
                    this.inLove = 0;
                    this.entityToAttack = null;
                }
            }
        }

        if (!foundPlayer)
        {
            makeLoveToClash(false);
            this.inLove = 0;
            this.entityToAttack = null;
        }
    }

    protected void attackEntity(Entity par1Entity, float par2)
    {
        if ((par1Entity instanceof EntityPlayer))
        {
            if (par2 < 3.0F)
            {
                double d0 = par1Entity.posX - this.posX;
                double d1 = par1Entity.posZ - this.posZ;
                this.rotationYaw = ((float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F);
                this.hasAttacked = true;
            }

            EntityPlayer entityplayer = (EntityPlayer)par1Entity;
            this.entityToAttack = par1Entity;
        }
        else
        {
            super.attackEntity(par1Entity, par2);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Integer(0));
        this.dataWatcher.addObject(17, new Byte((byte)0));
    }

    public boolean isLovingClash()
    {
        return this.dataWatcher.getWatchableObjectByte(17) >= 1;
    }

    public void makeLoveToClash(boolean love)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)(love ? 1 : 0)));
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return new EntityZephyroo(this.worldObj);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.fallDistance = 0.0F;

        if ((this.onGround) && ((this.motionX > 0.0001D) || (this.motionZ > 0.0001D)))
        {
            hop();
        }
        else
        {
            if (this.timeTilJump != 0)
            {
                this.timeTilJump -= 1;
            }

            if (this.timeTilJump == 0)
            {
                this.timeTilJump = 10;
            }
        }

        this.dataWatcher.updateObject(16, Integer.valueOf(this.timeTilJump));
    }

    public float getTimeTilJump()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    protected void hop()
    {
        super.jump();
    }
}

