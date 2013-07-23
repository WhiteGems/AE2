package net.aetherteam.aether.tile_entities;

import net.aetherteam.aether.client.models.ModelAltar1;
import net.aetherteam.aether.client.models.ModelAltar2;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityAltarRenderer extends TileEntityRenderer
{
    private ModelAltar1 altarModel1;
    private ModelAltar2 altarModel2;
    private double radius = 1.5D;
    private double theta = 5.0D;
    private double alpha = 0.0D;
    private double alphaFloat = 0.0D;
    private double angle = 10.0D;

    public TileEntityAltarRenderer()
    {
        this.altarModel1 = new ModelAltar1();
        this.altarModel2 = new ModelAltar2();
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityAltarAt((TileEntityAltar)tileentity, d, d1, d2, f);
    }

    public void renderTileEntityAltarAt(TileEntityAltar enchanter, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ModelAltar1 modelaltar1 = this.altarModel1;
        ModelAltar2 modelaltar2 = this.altarModel2;
        a("/net/aetherteam/aether/client/sprites/tile_entities/Altar.png");
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

        modelaltar1.AmbrosiumBottomLeft.g = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumBottomRight.g = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumTopLeft.g = enchanter.getAmbRotation();
        modelaltar1.AmbrosiumTopRight.g = enchanter.getAmbRotation();
        modelaltar1.renderAll(0.0625F);
        GL11.glPopMatrix();

        if (enchanter.getEnchanterStacks(0) != null)
        {
            Item item = enchanter.getEnchanterStacks(0).getItem();
            Icon var15 = item.getIconFromDamage(enchanter.getEnchanterStacks(0).getItemDamage());
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
            Item item = enchanter.getEnchanterStacks(1).getItem();
            Icon var15 = item.getIconFromDamage(enchanter.getEnchanterStacks(1).getItemDamage());

            if (item.getSpriteNumber() > 0)
            {
                a("/gui/items.png");
            }
            else
            {
                a("/terrain.png");
            }

            int amount = enchanter.getEnchanterStacks(1).stackSize;
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.25F, (float)d2 + 0.5F);
            GL11.glScalef(0.2F, 0.2F, 0.2F);
            renderOrbitItem(var15, amount, enchanter.getAmbSpinning());
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void renderItem(Icon icon, int par2, double interval)
    {
        Rect2i var3 = Rect2i.rectX;
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
        GL11.glRotatef(180.0F - RenderEnderman.endermanModel.j, 0.0F, 1.0F, 0.0F);
        var3.b();
        var3.b(0.0F, 1.0F, 0.0F);
        var3.a(0.0F - var9, 0.0F - var10, 0.0D, var4, var7);
        var3.a(var8 - var9, 0.0F - var10, 0.0D, var5, var7);
        var3.a(var8 - var9, 1.0F - var10, 0.0D, var5, var6);
        var3.a(0.0F - var9, 1.0F - var10, 0.0D, var4, var6);
        var3.getRectX();
    }

    private void renderOrbitItem(Icon icon, int amount, double interval)
    {
        for (int i = 0; i < amount; i++)
        {
            GL11.glPushMatrix();
            Rect2i var3 = Rect2i.rectX;
            float var4 = icon.getMinU();
            float var5 = icon.getMaxU();
            float var6 = icon.getMinV();
            float var7 = icon.getMaxV();
            float var8 = 1.0F;
            float var9 = 0.5F;
            float var10 = 0.25F;
            double dist = Math.PI * i / amount * 2.0D;
            double x = this.radius * Math.cos(this.theta + dist);
            double y = 0.0D;
            double z = this.radius * Math.sin(this.theta + dist);
            double deltaX = z * Math.cos(this.alpha) - x * Math.sin(this.alpha);
            double deltaZ = x * Math.cos(this.alpha) + z * Math.sin(this.alpha);
            GL11.glTranslatef((float)deltaX, (float)y, (float)deltaZ);
            this.alpha += interval / 100.0D;
            this.angle += 0.00499999988824129D;
            GL11.glRotatef(180.0F + RenderEnderman.endermanModel.j, 0.0F, -1.0F, 0.0F);
            var3.b();
            var3.b(0.0F, 1.0F, 0.0F);
            var3.a(0.0F - var9, 0.0F - var10, 0.0D, var4, var7);
            var3.a(var8 - var9, 0.0F - var10, 0.0D, var5, var7);
            var3.a(var8 - var9, 1.0F - var10, 0.0D, var5, var6);
            var3.a(0.0F - var9, 1.0F - var10, 0.0D, var4, var6);
            var3.getRectX();
            GL11.glPopMatrix();
        }
    }
}

