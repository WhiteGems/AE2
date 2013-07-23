package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelTempestBall;
import net.aetherteam.aether.entities.EntityTempestBall;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTempestBall extends RenderCreeper
{
    private ModelTempestBall ball;

    public RenderTempestBall()
    {
        this.ball = new ModelTempestBall();
    }

    public void render(EntityTempestBall tempestBall, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.65F, 0.65F, 0.65F);
        GL11.glRotatef(tempestBall.rotationYaw + 180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        loadDownloadableImageTexture((String)null, "/net/aetherteam/aether/client/sprites/projectiles/electroball/electroball.png");
        this.ball.render(tempestBall, 0.0625F);
        GL11.glPopMatrix();
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        render((EntityTempestBall)par1Entity, par2, par4, par6, par8, par9);
    }
}

