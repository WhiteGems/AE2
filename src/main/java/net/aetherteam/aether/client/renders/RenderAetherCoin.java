package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAetherCoin;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAetherCoin extends Render
{
    private ModelAetherCoin CoinModel = new ModelAetherCoin();
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/coin/AetherCoins.png");

    public void Render(EntityAetherCoin coin, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        float yOffset = coin.getCoinValue() > 1 ? 1.5F : 0.5F;
        GL11.glTranslatef((float)par2, (float)par4 + yOffset, (float)par6);
        float coinSize = coin.getCoinValue() > 1 ? 0.8F : 0.25F;
        GL11.glScalef(coinSize, coinSize, coinSize);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        this.CoinModel.spinCoin(0.0F, coin.getSpinSpeed(), 0.0F);
        this.func_110777_b(coin);
        this.CoinModel.render(coin, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.Render((EntityAetherCoin)par1Entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
