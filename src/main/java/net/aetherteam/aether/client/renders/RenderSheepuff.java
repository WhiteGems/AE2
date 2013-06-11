package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySheepuff;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import org.lwjgl.opengl.GL11;

public class RenderSheepuff extends RenderLiving
{
    private ModelBase wool;
    private ModelBase puffed;

    public RenderSheepuff(ModelBase var1, ModelBase var2, ModelBase var3, float var4)
    {
        super(var2, var4);
        this.setRenderPassModel(var1);
        this.wool = var1;
        this.puffed = var3;
    }

    protected int setWoolColorAndRender(EntitySheepuff var1, int var2, float var3)
    {
        if (var2 == 0 && !var1.getSheared())
        {
            if (var1.getPuffed())
            {
                this.setRenderPassModel(this.puffed);
            } else
            {
                this.setRenderPassModel(this.wool);
            }

            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sheepuff/fur.png");
            float var4 = var1.getBrightness(var3);
            int var5 = var1.getFleeceColor();
            GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
            return 1;
        } else
        {
            return -1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setWoolColorAndRender((EntitySheepuff) var1, var2, var3);
    }
}
