package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelMimic;
import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderMimic extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/mimic/mimic1.png");
    private static final ResourceLocation TEXTURE_MIMIC_1 = new ResourceLocation("aether", "textures/mobs/mimic/mimic1.png");
    private static final ResourceLocation TEXTURE_MIMIC_2 = new ResourceLocation("aether", "textures/mobs/mimic/mimic2.png");

    public RenderMimic(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.setRenderPassModel(modelbase);
    }

    protected int a(EntityMimic entityMimic, int i, float f)
    {
        this.renderManager.renderEngine.func_110577_a(TEXTURE_MIMIC_1);
        ((ModelMimic)this.mainModel).render1(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
        this.renderManager.renderEngine.func_110577_a(TEXTURE_MIMIC_2);
        ((ModelMimic)this.mainModel).render2(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        return 1;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.a((EntityMimic)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
