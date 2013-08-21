package net.aetherteam.aether.tile_entities;

import net.aetherteam.aether.client.models.ModelAltar1;
import net.aetherteam.aether.client.models.ModelAltar2;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityAltarRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation TEXTURE_ALTAR = new ResourceLocation("aether", "textures/tile_entities/Altar.png");
    private ModelAltar1 altarModel1 = new ModelAltar1();
    private ModelAltar2 altarModel2 = new ModelAltar2();
    private double radius = 1.5D;
    private double theta = 5.0D;
    private double alpha = 0.0D;
    private double alphaFloat = 0.0D;
    private double angle = 10.0D;

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        this.renderTileEntityAltarAt((TileEntityAltar)tileentity, d, d1, d2, f);
    }

    public void renderTileEntityAltarAt(TileEntityAltar enchanter, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ModelAltar1 modelaltar1 = this.altarModel1;
        ModelAltar2 modelaltar2 = this.altarModel2;
        this.func_110628_a(TEXTURE_ALTAR);
        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 1.0F);
        int meta = 2;

        if (enchanter.worldObj != null)
        {
            meta = enchanter.getBlockMetadata();
        }

        if (meta != 2)
        {
            if (meta == 3)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (meta == 4)
            {
                GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (meta == 5)
            {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        modelaltar1.AmbrosiumBottomLeft.rotateAngleY = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumBottomRight.rotateAngleY = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumTopLeft.rotateAngleY = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumTopRight.rotateAngleY = enchanter.getAmbRotation();
        modelaltar1.renderAll(0.0625F);
        GL11.glPopMatrix();
        Item item;

        if (enchanter.getEnchanterStacks(0) != null)
        {
            item = enchanter.getEnchanterStacks(0).getItem();
            item.getIconFromDamage(enchanter.getEnchanterStacks(0).getItemDamage());
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.2F, (float)d2 + 0.5F);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }

        if (enchanter.getEnchanterStacks(1) != null)
        {
            item = enchanter.getEnchanterStacks(1).getItem();
            Icon var15 = item.getIconFromDamage(enchanter.getEnchanterStacks(1).getItemDamage());

            if (item.getSpriteNumber() > 0)
            {
                this.func_110628_a(TextureMap.field_110576_c);
            }
            else
            {
                this.func_110628_a(TextureMap.field_110575_b);
            }

            int amount = enchanter.getEnchanterStacks(1).stackSize;
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.25F, (float)d2 + 0.5F);
            GL11.glScalef(0.2F, 0.2F, 0.2F);
            this.renderOrbitItem(var15, amount, enchanter.getAmbSpinning());
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void renderItem(Icon icon, int par2, double interval)
    {
        Tessellator var3 = Tessellator.instance;
        float var4 = icon.getMinU();
        float var5 = icon.getMaxU();
        float var6 = icon.getMinV();
        float var7 = icon.getMaxV();
        float var8 = 1.0F;
        float var9 = 0.5F;
        float var10 = 0.25F;
        double y = 0.35D * Math.cos(this.theta);
        double deltaY = y * Math.cos(this.alphaFloat) - y * Math.sin(this.alphaFloat);
        this.alphaFloat += interval;
        GL11.glTranslatef(0.0F, (float)deltaY, 0.0F);
        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 1.0F, 0.0F);
        var3.addVertexWithUV((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
        var3.addVertexWithUV((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
        var3.addVertexWithUV((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
        var3.addVertexWithUV((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
        var3.draw();
    }

    private void renderOrbitItem(Icon icon, int amount, double interval)
    {
        for (int i = 0; i < amount; ++i)
        {
            GL11.glPushMatrix();
            Tessellator var3 = Tessellator.instance;
            float var4 = icon.getMinU();
            float var5 = icon.getMaxU();
            float var6 = icon.getMinV();
            float var7 = icon.getMaxV();
            float var8 = 1.0F;
            float var9 = 0.5F;
            float var10 = 0.25F;
            double dist = Math.PI * (double)i / (double)amount * 2.0D;
            double x = this.radius * Math.cos(this.theta + dist);
            double y = 0.0D;
            double z = this.radius * Math.sin(this.theta + dist);
            double deltaX = z * Math.cos(this.alpha) - x * Math.sin(this.alpha);
            double deltaZ = x * Math.cos(this.alpha) + z * Math.sin(this.alpha);
            GL11.glTranslatef((float)deltaX, (float)y, (float)deltaZ);
            this.alpha += interval / 100.0D;
            this.angle += 0.004999999888241291D;
            GL11.glRotatef(180.0F + RenderManager.instance.playerViewY, 0.0F, -1.0F, 0.0F);
            var3.startDrawingQuads();
            var3.setNormal(0.0F, 1.0F, 0.0F);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
            var3.draw();
            GL11.glPopMatrix();
        }
    }
}
