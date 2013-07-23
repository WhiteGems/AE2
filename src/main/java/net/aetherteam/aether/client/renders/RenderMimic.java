package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelMimic;
import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderMimic extends RenderLiving
{
    public RenderMimic(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
    }

    protected int a(EntityMimic var1, int var2, float var3)
    {
        this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/mimic/mimic1.png");
        ((ModelMimic)this.mainModel).render1(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, var1);
        this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/mimic/mimic2.png");
        ((ModelMimic)this.mainModel).render2(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, var1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        return 1;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.a((EntityMimic)var1, var2, var3);
    }
}
