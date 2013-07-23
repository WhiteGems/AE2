package net.aetherteam.playercore_api.cores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class PlayerCoreClient extends EntityClientPlayerMP
{
    protected PlayerCoreClient nextPlayerCore;
    protected PlayerCoreClient player;
    private boolean shouldCallSuper;

    public PlayerCoreClient(Minecraft var1, World var2, Session var3, NetClientHandler var4)
    {
        this(var1, var2, var3, var4, 0, (PlayerCoreClient)null);
    }

    public PlayerCoreClient(Minecraft var1, World var2, Session var3, NetClientHandler var4, int var5, PlayerCoreClient var6)
    {
        super(var1, var2, var3, var4);
        this.player = var6 == null ? this : var6;

        if (var5 < PlayerCoreAPI.playerCoreClientList.size())
        {
            Class var7 = (Class)PlayerCoreAPI.playerCoreClientList.get(var5);

            try
            {
                Constructor var8 = var7.getConstructor(new Class[] {Minecraft.class, World.class, Session.class, NetClientHandler.class, Integer.TYPE, PlayerCoreClient.class});
                this.nextPlayerCore = (PlayerCoreClient)var8.newInstance(new Object[] {var1, var2, var3, var4, Integer.valueOf(var5 + 1), this.player});
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

    public PlayerCoreClient getPlayerCoreObject(Class var1)
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
     * Returns how strong the player is against the specified block at this moment
     */
    public float getCurrentPlayerStrVsBlock(Block var1, boolean var2)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.getCurrentPlayerStrVsBlock(var1, var2) : super.getCurrentPlayerStrVsBlock(var1, var2);
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    public void setFlag(int var1, boolean var2)
    {
        super.setFlag(var1, var2);
    }
}
