package net.aetherteam.playercore_api.cores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

public class PlayerCoreServer extends EntityPlayerMP
{
    protected PlayerCoreServer nextPlayerCore;
    protected PlayerCoreServer player;
    private boolean shouldCallSuper;

    public PlayerCoreServer(MinecraftServer var1, World var2, String var3, ItemInWorldManager var4)
    {
        this(var1, var2, var3, var4, 0, (PlayerCoreServer)null);
    }

    public PlayerCoreServer(MinecraftServer var1, World var2, String var3, ItemInWorldManager var4, int var5, PlayerCoreServer var6)
    {
        super(var1, var2, var3, new ItemInWorldManager(var2));
        this.player = var6 == null ? this : var6;

        if (var5 < PlayerCoreAPI.playerCoreServerList.size())
        {
            Class var7 = (Class)PlayerCoreAPI.playerCoreServerList.get(var5);

            try
            {
                Constructor var8 = var7.getConstructor(new Class[] {MinecraftServer.class, World.class, String.class, ItemInWorldManager.class, Integer.TYPE, PlayerCoreServer.class});
                this.nextPlayerCore = (PlayerCoreServer)var8.newInstance(new Object[] {var1, var2, var3, var4, Integer.valueOf(var5 + 1), this.player});
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

    public PlayerCoreServer getPlayerCoreObject(Class var1)
    {
        return this.getClass() == var1 ? this : (this.nextPlayerCore == this.player ? null : this.nextPlayerCore.getPlayerCoreObject(var1));
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
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.writeEntityToNBT(var1);
        }
        else
        {
            super.writeEntityToNBT(var1);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.readEntityFromNBT(var1);
        }
        else
        {
            super.readEntityFromNBT(var1);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.onDeath(var1);
        }
        else
        {
            super.onDeath(var1);
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
    public void heal(int var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.heal(var1);
        }
        else
        {
            super.heal(var1);
        }
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.getSpeedModifier() : super.getSpeedModifier();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.attackEntityFrom(var1, var2) : super.attackEntityFrom(var1, var2);
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public void attackTargetEntityWithCurrentItem(Entity var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.attackTargetEntityWithCurrentItem(var1);
        }
        else
        {
            super.attackTargetEntityWithCurrentItem(var1);
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
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
    public float getCurrentPlayerStrVsBlock(Block var1, boolean var2)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.getCurrentPlayerStrVsBlock(var1, var2) : super.getCurrentPlayerStrVsBlock(var1, var2);
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
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    public void setFlag(int var1, boolean var2)
    {
        super.setFlag(var1, var2);
    }

    public void setFoodStats(FoodStats var1)
    {
        this.foodStats = var1;
    }

    public float getMoveForward()
    {
        return this.moveForward;
    }

    public float getMoveStrafing()
    {
        return this.moveStrafing;
    }

    public void setMoveStrafing(float var1)
    {
        this.moveStrafing = var1;
    }
}
