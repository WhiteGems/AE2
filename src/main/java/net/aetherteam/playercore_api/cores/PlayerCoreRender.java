package net.aetherteam.playercore_api.cores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerCoreRender extends RenderPlayer
{
    protected PlayerCoreRender nextPlayerCore;
    protected PlayerCoreRender renderPlayer;
    private boolean shouldCallSuper;

    public PlayerCoreRender()
    {
        this(0, (PlayerCoreRender)null);
    }

    public PlayerCoreRender(int playerCoreIndex, PlayerCoreRender renderPlayer)
    {
        this.renderPlayer = renderPlayer == null ? this : renderPlayer;

        if (playerCoreIndex < PlayerCoreAPI.playerCoreRenderList.size())
        {
            Class nextPlayerCoreClass = (Class)PlayerCoreAPI.playerCoreRenderList.get(playerCoreIndex);

            try
            {
                Constructor constructor = nextPlayerCoreClass.getConstructor(new Class[] {Integer.TYPE, PlayerCoreRender.class});
                this.nextPlayerCore = (PlayerCoreRender)constructor.newInstance(new Object[] {Integer.valueOf(playerCoreIndex + 1), this.renderPlayer});
            }
            catch (SecurityException var6)
            {
                var6.printStackTrace();
            }
            catch (NoSuchMethodException var7)
            {
                var7.printStackTrace();
            }
            catch (IllegalArgumentException var8)
            {
                var8.printStackTrace();
            }
            catch (InstantiationException var9)
            {
                var9.printStackTrace();
            }
            catch (IllegalAccessException var10)
            {
                var10.printStackTrace();
            }
            catch (InvocationTargetException var11)
            {
                var11.printStackTrace();
            }
        }
        else
        {
            this.nextPlayerCore = this.renderPlayer;
        }
    }

    public PlayerCoreRender getPlayerCoreObject(Class clazz)
    {
        return this.getClass() == clazz ? this : (this.nextPlayerCore == this.renderPlayer ? null : this.nextPlayerCore.getPlayerCoreObject(clazz));
    }

    private boolean shouldCallSuper()
    {
        if (!this.shouldCallSuper)
        {
            this.nextPlayerCore.shouldCallSuper = this.nextPlayerCore == this.renderPlayer;
            return false;
        }
        else
        {
            this.shouldCallSuper = false;
            return true;
        }
    }

    /**
     * Method for adding special render rules
     */
    public void renderSpecials(AbstractClientPlayer entityplayer, float f)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.renderSpecials(entityplayer, f);
        }
        else
        {
            super.renderSpecials(entityplayer, f);
        }
    }

    public void func_130009_a(AbstractClientPlayer player, double d, double d1, double d2, float f, float f1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.func_130009_a(player, d, d1, d2, f, f1);
        }
        else
        {
            super.func_130009_a(player, d, d1, d2, f, f1);
        }
    }

    public void renderFirstPersonArm(EntityPlayer player)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.renderFirstPersonArm(player);
        }
        else
        {
            super.renderFirstPersonArm(player);
        }
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    public float renderSwingProgress(EntityLivingBase player, float f1)
    {
        return super.renderSwingProgress(player, f1);
    }

    public ModelBiped getModelBipedMain()
    {
        return (ModelBiped)this.mainModel;
    }
}
