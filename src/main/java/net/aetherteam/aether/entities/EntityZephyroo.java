package net.aetherteam.aether.entities;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityZephyroo extends EntityAetherAnimal
{
    private int timeTilJump = 10;

    public EntityZephyroo(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(15.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(40.0D);
        this.setEntityHealth(40.0F);
        this.setSize(0.5F, 2.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        List entitiesAround = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.5D, 1.75D, 5.5D));
        boolean foundPlayer = false;
        Iterator i$ = entitiesAround.iterator();

        while (i$.hasNext())
        {
            Entity entity = (Entity)i$.next();

            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)entity;

                if (player.username.equalsIgnoreCase("ClashJTM"))
                {
                    foundPlayer = true;
                    this.makeLoveToClash(true);
                    this.inLove = 1;
                    this.attackEntity(player, 20.0F);
                    this.faceEntity(player, 5.5F, (float)this.getVerticalFaceSpeed());
                }
                else
                {
                    this.makeLoveToClash(false);
                    this.inLove = 0;
                    this.entityToAttack = null;
                }
            }
        }

        if (!foundPlayer)
        {
            this.makeLoveToClash(false);
            this.inLove = 0;
            this.entityToAttack = null;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (par1Entity instanceof EntityPlayer)
        {
            if (par2 < 3.0F)
            {
                double entityplayer = par1Entity.posX - this.posX;
                double d1 = par1Entity.posZ - this.posZ;
                this.rotationYaw = (float)(Math.atan2(d1, entityplayer) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }

            EntityPlayer entityplayer1 = (EntityPlayer)par1Entity;
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

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.fallDistance = 0.0F;

        if (this.onGround && (this.motionX > 1.0E-4D || this.motionZ > 1.0E-4D))
        {
            this.hop();
        }
        else
        {
            if (this.timeTilJump != 0)
            {
                --this.timeTilJump;
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
        return (float)this.dataWatcher.getWatchableObjectInt(16);
    }

    protected void hop()
    {
        super.jump();
    }
}
