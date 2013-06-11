package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelLabyrinthEye;
import net.aetherteam.aether.entities.bosses.EntityLabyrinthEye;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderLabyrinthEye extends RenderLiving
{
    public RenderLabyrinthEye(ModelLabyrinthEye var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLiving var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        if (!var1.getHasActivePotion())
        {
            GL11.glTranslatef(0.0F, 0.0F, 0.0F);
            this.loadDownloadableImageTexture(var1.skinUrl, var1.getTexture());
            GL11.glPushMatrix();
            GL11.glRotatef((float) (var1.ticksExisted * 10), 0.0F, 0.0F, 1.0F);

            if (var1.motionX == 0.0D && var1.motionZ != 0.0D)
            {
                ;
            }

            this.mainModel.render(var1, var2, var3, var4, var5, var6, var7);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            ((ModelLabyrinthEye) this.mainModel).renderEye(var1, var2, var3, var4, var5, var6, var7);
        }
    }

    protected int setMarkingBrightness(EntityLabyrinthEye var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        } else
        {
            if (var1.getAwake())
            {
                this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/cogboss/cogglow.png");
            } else
            {
                this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/cogboss/cogglowblue.png");
            }

            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glRotatef((float) (var1.ticksExisted * 10), 0.0F, 1.0F, 0.0F);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (var1.getHasActivePotion())
            {
                GL11.glDepthMask(false);
            } else
            {
                GL11.glDepthMask(true);
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setMarkingBrightness((EntityLabyrinthEye) var1, var2, var3);
    }
}
