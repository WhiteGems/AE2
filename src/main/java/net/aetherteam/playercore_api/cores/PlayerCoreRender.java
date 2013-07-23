package net.aetherteam.playercore_api.cores;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.aetherteam.playercore_api.PlayerCoreAPI;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLiving;
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

    public PlayerCoreRender(int var1, PlayerCoreRender var2)
    {
        this.renderPlayer = var2 == null ? this : var2;

        if (var1 < PlayerCoreAPI.playerCoreRenderList.size())
        {
            Class var3 = (Class)PlayerCoreAPI.playerCoreRenderList.get(var1);

            try
            {
                Constructor var4 = var3.getConstructor(new Class[] {Integer.TYPE, PlayerCoreRender.class});
                this.nextPlayerCore = (PlayerCoreRender)var4.newInstance(new Object[] {Integer.valueOf(var1 + 1), this.renderPlayer});
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

    public PlayerCoreRender getPlayerCoreObject(Class var1)
    {
        return this.getClass() == var1 ? this : (this.nextPlayerCore == this.renderPlayer ? null : this.nextPlayerCore.getPlayerCoreObject(var1));
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
    public void renderSpecials(EntityPlayer var1, float var2)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.renderSpecials(var1, var2);
        }
        else
        {
            super.renderSpecials(var1, var2);
        }
    }

    public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.renderPlayer(var1, var2, var4, var6, var8, var9);
        }
        else
        {
            super.renderPlayer(var1, var2, var4, var6, var8, var9);
        }
    }

    public void renderFirstPersonArm(EntityPlayer var1)
    {
        if (!this.shouldCallSuper())
        {
            this.nextPlayerCore.renderFirstPersonArm(var1);
        }
        else
        {
            super.renderFirstPersonArm(var1);
        }
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    /**
     * loads the specified texture
     */
    public void loadTexture(String var1)
    {
        super.loadTexture(var1);
    }

    /**
     * loads the specified downloadable texture or alternative built in texture
     */
    public boolean loadDownloadableImageTexture(String var1, String var2)
    {
        return super.loadDownloadableImageTexture(var1, var2);
    }

    public float renderSwingProgress(EntityLiving var1, float var2)
    {
        return super.renderSwingProgress(var1, var2);
    }

    public ModelBiped getModelBipedMain()
    {
        return (ModelBiped)this.mainModel;
    }
}
