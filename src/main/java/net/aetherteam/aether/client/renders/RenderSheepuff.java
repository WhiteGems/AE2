package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySheepuff;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSheepuff extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sheepuff/sheepuff.png");
    private static final ResourceLocation TEXTURE_FUR = new ResourceLocation("aether", "textures/mobs/sheepuff/fur.png");
    private ModelBase wool;
    private ModelBase puffed;

    public RenderSheepuff(ModelBase modelbase, ModelBase modelbase1, ModelBase modelbase2, float f)
    {
        super(modelbase1, f);
        this.setRenderPassModel(modelbase);
        this.wool = modelbase;
        this.puffed = modelbase2;
    }

    protected int setWoolColorAndRender(EntitySheepuff entitysheep, int i, float f)
    {
        if (i == 0 && !entitysheep.getSheared())
        {
            if (entitysheep.getPuffed())
            {
                this.setRenderPassModel(this.puffed);
            }
            else
            {
                this.setRenderPassModel(this.wool);
            }

            this.renderManager.renderEngine.func_110577_a(TEXTURE_FUR);
            float f1 = entitysheep.getBrightness(f);
            int j = entitysheep.getFleeceColor();
            GL11.glColor3f(f1 * EntitySheep.fleeceColorTable[j][0], f1 * EntitySheep.fleeceColorTable[j][1], f1 * EntitySheep.fleeceColorTable[j][2]);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setWoolColorAndRender((EntitySheepuff)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
