package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAetherCoin;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderAetherCoin extends Render
{
    private ModelAetherCoin CoinModel = new ModelAetherCoin();

    public void Render(EntityAetherCoin var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        float var10 = var1.getCoinValue() > 1 ? 1.5F : 0.5F;
        GL11.glTranslatef((float)var2, (float)var4 + var10, (float)var6);
        float var11 = var1.getCoinValue() > 1 ? 0.8F : 0.25F;
        GL11.glScalef(var11, var11, var11);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        this.CoinModel.spinCoin(0.0F, var1.getSpinSpeed(), 0.0F);
        this.loadTexture("/net/aetherteam/aether/client/sprites/coin/AetherCoins.png");
        this.CoinModel.render(var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.Render((EntityAetherCoin)var1, var2, var4, var6, var8, var9);
    }
}
