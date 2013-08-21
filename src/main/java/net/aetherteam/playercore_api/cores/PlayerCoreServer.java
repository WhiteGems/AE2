package net.aetherteam.playercore_api.cores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PlayerCoreServer extends EntityPlayerMP implements IPlayerCoreCommon
{
    protected PlayerCoreServer nextPlayerCore;
    protected PlayerCoreServer player;
    private boolean shouldCallSuper;

    public PlayerCoreServer(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager)
    {
        this(par1MinecraftServer, par2World, par3Str, par4ItemInWorldManager, 0, (PlayerCoreServer)null);
    }

    public PlayerCoreServer(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager, int playerCoreIndex, PlayerCoreServer entityPlayerMP)
    {
        super(par1MinecraftServer, par2World, par3Str, new ItemInWorldManager(par2World));
        this.player = entityPlayerMP == null ? this : entityPlayerMP;

        if (playerCoreIndex < PlayerCoreAPI.playerCoreServerList.size())
        {
            Class nextPlayerCoreClass = (Class)PlayerCoreAPI.playerCoreServerList.get(playerCoreIndex);

            try
            {
                Constructor constructor = nextPlayerCoreClass.getConstructor(new Class[] {MinecraftServer.class, World.class, String.class, ItemInWorldManager.class, Integer.TYPE, PlayerCoreServer.class});
                this.nextPlayerCore = (PlayerCoreServer)constructor.newInstance(new Object[] {par1MinecraftServer, par2World, par3Str, par4ItemInWorldManager, Integer.valueOf(playerCoreIndex + 1), this.player});
            }
            catch (SecurityException var10)
            {
                var10.printStackTrace();
            }
            catch (NoSuchMethodException var11)
            {
                var11.printStackTrace();
            }
            catch (IllegalArgumentException var12)
            {
                var12.printStackTrace();
            }
            catch (InstantiationException var13)
            {
                var13.printStackTrace();
            }
            catch (IllegalAccessException var14)
            {
                var14.printStackTrace();
            }
            catch (InvocationTargetException var15)
            {
                var15.printStackTrace();
            }
        }
        else
        {
            this.nextPlayerCore = this.player;
        }
    }

    public PlayerCoreServer getPlayerCoreObject(Class clazz)
    {
        return this.getClass() == clazz ? this : (this.nextPlayerCore == this.player ? null : this.nextPlayerCore.getPlayerCoreObject(clazz));
    }

    private boolean shouldCallSuper()
    {
        if (!this.shouldCallSuper)
        {
            this.nextPlayerCore.shouldCallSuper = this.nextPlayerCore == this.player;
            return false;
        }
        else
        {
            this.shouldCallSuper = false;
            return true;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.onLivingUpdate();
        }
        else
        {
            super.onLivingUpdate();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.onUpdate();
        }
        else
        {
            super.onUpdate();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.writeEntityToNBT(tag);
        }
        else
        {
            super.writeEntityToNBT(tag);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.readEntityFromNBT(tag);
        }
        else
        {
            super.readEntityFromNBT(tag);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource source)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.onDeath(source);
        }
        else
        {
            super.onDeath(source);
        }
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    public void jump()
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.jump();
        }
        else
        {
            super.jump();
        }
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float i)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.heal(i);
        }
        else
        {
            super.heal(i);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.attackEntityFrom(var1, var2) : super.attackEntityFrom(var1, var2);
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public void attackTargetEntityWithCurrentItem(Entity ent)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.attackTargetEntityWithCurrentItem(ent);
        }
        else
        {
            super.attackTargetEntityWithCurrentItem(ent);
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, float var2, double var3, double var5)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.knockBack(var1, var2, var3, var5);
        }
        else
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    /**
     * Returns how strong the player is against the specified block at this moment
     */
    public float getCurrentPlayerStrVsBlock(Block block, boolean flag)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.getCurrentPlayerStrVsBlock(block, flag) : super.getCurrentPlayerStrVsBlock(block, flag);
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.isInWater() : super.isInWater();
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.handleWaterMovement() : super.handleWaterMovement();
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.handleLavaMovement() : super.handleLavaMovement();
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    public void setFlag(int par1, boolean par2)
    {
        super.setFlag(par1, par2);
    }

    public void setFoodStats(FoodStats foodStats)
    {
        this.foodStats = foodStats;
    }

    public float getMoveForward()
    {
        return this.moveForward;
    }

    public float getMoveStrafing()
    {
        return this.moveStrafing;
    }

    public void setMoveForward(float moveFoward)
    {
        this.moveForward = moveFoward;
    }

    public void setMoveStrafing(float moveStrafing)
    {
        this.moveStrafing = moveStrafing;
    }

    public boolean isJumping()
    {
        return this.player.isJumping;
    }

    public float updateRotation(float par1, float par2, float par3)
    {
        float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);

        if (f3 > par3)
        {
            f3 = par3;
        }

        if (f3 < -par3)
        {
            f3 = -par3;
        }

        return par1 + f3;
    }

    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double d0 = par1Entity.posX - this.posX;
        double d1 = par1Entity.posZ - this.posZ;
        double d2;

        if (par1Entity instanceof EntityLivingBase)
        {
            EntityLivingBase d3 = (EntityLivingBase)par1Entity;
            d2 = d3.posY + (double)d3.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
        }
        else
        {
            d2 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
        }

        double d31 = (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f2 = (float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float)(-(Math.atan2(d2, d31) * 180.0D / Math.PI));
        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, par3);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f2, par2);
    }
}
