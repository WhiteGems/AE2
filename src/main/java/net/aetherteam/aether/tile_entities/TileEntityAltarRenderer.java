package net.aetherteam.aether.tile_entities;

import net.aetherteam.aether.client.models.ModelAltar1;
import net.aetherteam.aether.client.models.ModelAltar2;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityAltarRenderer extends TileEntitySpecialRenderer
{
    private ModelAltar1 altarModel1 = new ModelAltar1();
    private ModelAltar2 altarModel2 = new ModelAltar2();
    private double radius = 1.5D;
    private double theta = 5.0D;
    private double alpha = 0.0D;
    private double alphaFloat = 0.0D;
    private double angle = 10.0D;

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        this.renderTileEntityAltarAt((TileEntityAltar)var1, var2, var4, var6, var8);
    }

    public void renderTileEntityAltarAt(TileEntityAltar var1, double var2, double var4, double var6, float var8)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ModelAltar1 var9 = this.altarModel1;
        ModelAltar2 var10 = this.altarModel2;
        this.bindTextureByName("/net/aetherteam/aether/client/sprites/tile_entities/Altar.png");
        GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 1.0F);
        int var11 = 2;

        if (var1.worldObj != null)
        {
            var11 = var1.getBlockMetadata();
        }

        if (var11 != 2)
        {
            if (var11 == 3)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (var11 == 4)
            {
                GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (var11 == 5)
            {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        var9.AmbrosiumBottomLeft.rotateAngleY = var1.getAmbRotation();
        var9.AmbrosiumBottomRight.rotateAngleY = var1.getAmbRotation();
        var9.AmbrosiumTopLeft.rotateAngleY = var1.getAmbRotation();
        var9.AmbrosiumTopRight.rotateAngleY = var1.getAmbRotation();
        var9.renderAll(0.0625F);
        GL11.glPopMatrix();
        Item var12;

        if (var1.getEnchanterStacks(0) != null)
        {
            var12 = var1.getEnchanterStacks(0).getItem();
            var12.getIconFromDamage(var1.getEnchanterStacks(0).getItemDamage());
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.2F, (float)var6 + 0.5F);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }

        if (var1.getEnchanterStacks(1) != null)
        {
            var12 = var1.getEnchanterStacks(1).getItem();
            Icon var13 = var12.getIconFromDamage(var1.getEnchanterStacks(1).getItemDamage());

            if (var12.getSpriteNumber() > 0)
            {
                this.bindTextureByName("/gui/items.png");
            }
            else
            {
                this.bindTextureByName("/terrain.png");
            }

            int var14 = var1.getEnchanterStacks(1).stackSize;
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.25F, (float)var6 + 0.5F);
            GL11.glScalef(0.2F, 0.2F, 0.2F);
            this.renderOrbitItem(var13, var14, var1.getAmbSpinning());
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void renderItem(Icon var1, int var2, double var3)
    {
        Tessellator var5 = Tessellator.instance;
        float var6 = var1.getMinU();
        float var7 = var1.getMaxU();
        float var8 = var1.getMinV();
        float var9 = var1.getMaxV();
        float var10 = 1.0F;
        float var11 = 0.5F;
        float var12 = 0.25F;
        double var13 = 0.35D * Math.cos(this.theta);
        double var15 = var13 * Math.cos(this.alphaFloat) - var13 * Math.sin(this.alphaFloat);
        this.alphaFloat += var3;
        GL11.glTranslatef(0.0F, (float)var15, 0.0F);
        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        var5.startDrawingQuads();
        var5.setNormal(0.0F, 1.0F, 0.0F);
        var5.addVertexWithUV((double)(0.0F - var11), (double)(0.0F - var12), 0.0D, (double)var6, (double)var9);
        var5.addVertexWithUV((double)(var10 - var11), (double)(0.0F - var12), 0.0D, (double)var7, (double)var9);
        var5.addVertexWithUV((double)(var10 - var11), (double)(1.0F - var12), 0.0D, (double)var7, (double)var8);
        var5.addVertexWithUV((double)(0.0F - var11), (double)(1.0F - var12), 0.0D, (double)var6, (double)var8);
        var5.draw();
    }

    private void renderOrbitItem(Icon var1, int var2, double var3)
    {
        for (int var5 = 0; var5 < var2; ++var5)
        {
            GL11.glPushMatrix();
            Tessellator var6 = Tessellator.instance;
            float var7 = var1.getMinU();
            float var8 = var1.getMaxU();
            float var9 = var1.getMinV();
            float var10 = var1.getMaxV();
            float var11 = 1.0F;
            float var12 = 0.5F;
            float var13 = 0.25F;
            double var14 = Math.PI * (double)var5 / (double)var2 * 2.0D;
            double var16 = this.radius * Math.cos(this.theta + var14);
            double var18 = 0.0D;
            double var20 = this.radius * Math.sin(this.theta + var14);
            double var22 = var20 * Math.cos(this.alpha) - var16 * Math.sin(this.alpha);
            double var24 = var16 * Math.cos(this.alpha) + var20 * Math.sin(this.alpha);
            GL11.glTranslatef((float)var22, (float)var18, (float)var24);
            this.alpha += var3 / 100.0D;
            this.angle += 0.004999999888241291D;
            GL11.glRotatef(180.0F + RenderManager.instance.playerViewY, 0.0F, -1.0F, 0.0F);
            var6.startDrawingQuads();
            var6.setNormal(0.0F, 1.0F, 0.0F);
            var6.addVertexWithUV((double)(0.0F - var12), (double)(0.0F - var13), 0.0D, (double)var7, (double)var10);
            var6.addVertexWithUV((double)(var11 - var12), (double)(0.0F - var13), 0.0D, (double)var8, (double)var10);
            var6.addVertexWithUV((double)(var11 - var12), (double)(1.0F - var13), 0.0D, (double)var8, (double)var9);
            var6.addVertexWithUV((double)(0.0F - var12), (double)(1.0F - var13), 0.0D, (double)var7, (double)var9);
            var6.draw();
            GL11.glPopMatrix();
        }
    }
}
