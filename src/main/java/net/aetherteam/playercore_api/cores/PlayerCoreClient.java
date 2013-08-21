package net.aetherteam.playercore_api.cores;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class PlayerCoreClient extends EntityClientPlayerMP implements IPlayerCoreCommon
{
    protected PlayerCoreClient nextPlayerCore;
    protected PlayerCoreClient player;
    private boolean shouldCallSuper;

    public PlayerCoreClient(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4)
    {
        this(par1Minecraft, par2World, par3Session, par4, 0, (PlayerCoreClient)null);
    }

    public PlayerCoreClient(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4, int playerCoreIndex, PlayerCoreClient entityPlayerSP)
    {
        super(par1Minecraft, par2World, par3Session, par4);
        this.player = entityPlayerSP == null ? this : entityPlayerSP;

        if (playerCoreIndex < PlayerCoreAPI.playerCoreClientList.size())
        {
            Class nextPlayerCoreClass = (Class)PlayerCoreAPI.playerCoreClientList.get(playerCoreIndex);

            try
            {
                Constructor constructor = nextPlayerCoreClass.getConstructor(new Class[] {Minecraft.class, World.class, Session.class, NetClientHandler.class, Integer.TYPE, PlayerCoreClient.class});
                this.nextPlayerCore = (PlayerCoreClient)constructor.newInstance(new Object[] {par1Minecraft, par2World, par3Session, par4, Integer.valueOf(playerCoreIndex + 1), this.player});
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

    public PlayerCoreClient getPlayerCoreObject(Class clazz)
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
     * Returns how strong the player is against the specified block at this moment
     */
    public float getCurrentPlayerStrVsBlock(Block block, boolean flag)
    {
        return !this.shouldCallSuper() ? this.nextPlayerCore.getCurrentPlayerStrVsBlock(block, flag) : super.getCurrentPlayerStrVsBlock(block, flag);
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    public void setFlag(int par1, boolean par2)
    {
        super.setFlag(par1, par2);
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
