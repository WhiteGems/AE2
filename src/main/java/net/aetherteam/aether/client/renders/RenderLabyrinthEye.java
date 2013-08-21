package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelLabyrinthEye;
import net.aetherteam.aether.entities.bosses.EntityLabyrinthEye;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderLabyrinthEye extends RenderLiving
{
    private static final ResourceLocation TEXTURE_AWAKE = new ResourceLocation("aether", "textures/mobs/cogboss/cogawake.png");
    private static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation("aether", "textures/mobs/cogboss/cogsleep.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("aether", "textures/mobs/cogboss/cogglow.png");
    private static final ResourceLocation TEXTURE_GLOW_BLUE = new ResourceLocation("aether", "textures/mobs/cogboss/cogglowblue.png");

    public RenderLabyrinthEye(ModelLabyrinthEye model, float f)
    {
        super(model, f);
        this.setRenderPassModel(model);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        this.renderManager.renderEngine.func_110577_a(TEXTURE_SLEEP);
        EntityLabyrinthEye eye = (EntityLabyrinthEye)entity;
        ((ModelLabyrinthEye)this.mainModel).cogNumber = eye.getStage();
        GL11.glRotatef((float)(entity.ticksExisted * 10), 0.0F, 0.0F, 1.0F);
        this.mainModel.render(entity, par2, par3, par4, par5, par6, par7);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        ((ModelLabyrinthEye)this.mainModel).renderEye(entity, par2, par3, par4, par5, par6, par7);
        float f1 = 1.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslated(entity.posX, entity.posY + 30.0D, entity.posZ);
        GL11.glPopMatrix();
    }

    protected int setMarkingBrightness(EntityLabyrinthEye entity, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            if (entity.getAwake())
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW);
            }
            else
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW_BLUE);
            }

            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glRotatef((float)(entity.ticksExisted * 10), 0.0F, 1.0F, 0.0F);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (!entity.getActivePotionEffects().isEmpty())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setMarkingBrightness((EntityLabyrinthEye)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntityLabyrinthEye)entity).getAwake() ? TEXTURE_AWAKE : TEXTURE_SLEEP;
    }
}
