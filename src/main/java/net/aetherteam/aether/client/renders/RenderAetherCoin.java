package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAetherCoin;
import net.aetherteam.aether.entities.EntityAetherCoin;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderAetherCoin extends RenderManager
{
    private ModelAetherCoin CoinModel;

    public RenderAetherCoin()
    {
        this.CoinModel = new ModelAetherCoin();
    }

    public void Render(EntityAetherCoin coin, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        float yOffset = coin.getCoinValue() > 1 ? 1.5F : 0.5F;
        GL11.glTranslatef((float)par2, (float)par4 + yOffset, (float)par6);
        float coinSize = coin.getCoinValue() > 1 ? 0.8F : 0.25F;
        GL11.glScalef(coinSize, coinSize, coinSize);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        this.CoinModel.spinCoin(0.0F, coin.getSpinSpeed(), 0.0F);
        a("/net/aetherteam/aether/client/sprites/coin/AetherCoins.png");
        this.CoinModel.render(coin, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render((EntityAetherCoin)par1Entity, par2, par4, par6, par8, par9);
    }
}

