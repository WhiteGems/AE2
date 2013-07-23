package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCog;
import net.aetherteam.aether.entities.bosses.EntityCog;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderCog extends RenderManager
{
    private ModelCog CogModel;

    public RenderCog()
    {
        this.CogModel = new ModelCog();
    }

    public void Render(EntityCog cog, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4 + 0.5F, (float)par6);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(cog.life * 500, 1.0F, 1.0F, 1.0F);

        if (!cog.isLarge())
        {
            GL11.glScalef(0.25F, 0.25F, 0.25F);
        }

        a("/net/aetherteam/aether/client/sprites/mobs/cog.png");
        this.CogModel.render(cog, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render((EntityCog)par1Entity, par2, par4, par6, par8, par9);
    }
}

