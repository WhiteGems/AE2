package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerwhale;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderAerwhale extends RenderManager
{
    private ModelMinecart model;

    public RenderAerwhale()
    {
        this.model = new ModelAerwhale();
    }

    public void renderEntityWithPosYaw(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        a("/net/aetherteam/aether/client/sprites/mobs/aerwhale/aerwhale.png");
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(90.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}

