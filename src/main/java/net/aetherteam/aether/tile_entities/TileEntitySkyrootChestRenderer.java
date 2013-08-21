package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.blocks.BlockSkyrootChest;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntitySkyrootChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation TEXTURE_SKYROOT_LCHEST = new ResourceLocation("aether", "textures/tile_entities/skyrootLargeChest.png");
    private static final ResourceLocation TEXTURE_SKYROOT_CHEST = new ResourceLocation("aether", "textures/tile_entities/skyrootChest.png");
    private ModelChest chestModel = new ModelChest();
    private ModelChest largeChestModel = new ModelLargeChest();

    public void renderTileEntityChestAt(TileEntitySkyrootChest par1TileEntityChest, double par2, double par4, double par6, float par8)
    {
        int var9;

        if (!par1TileEntityChest.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            Block var14 = par1TileEntityChest.getBlockType();
            var9 = par1TileEntityChest.getBlockMetadata();

            if (var14 != null && var9 == 0)
            {
                ((BlockSkyrootChest)var14).unifyAdjacentChests(par1TileEntityChest.getWorldObj(), par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
                var9 = par1TileEntityChest.getBlockMetadata();
            }

            par1TileEntityChest.checkForAdjacentChests();
        }

        if (par1TileEntityChest.adjacentChestZNeg == null && par1TileEntityChest.adjacentChestXNeg == null)
        {
            ModelChest var141;

            if (par1TileEntityChest.adjacentChestXPos == null && par1TileEntityChest.adjacentChestZPosition == null)
            {
                var141 = this.chestModel;
                this.func_110628_a(TEXTURE_SKYROOT_CHEST);
            }
            else
            {
                var141 = this.largeChestModel;
                this.func_110628_a(TEXTURE_SKYROOT_LCHEST);
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short var11 = 0;

            if (var9 == 2)
            {
                var11 = 180;
            }

            if (var9 == 3)
            {
                var11 = 0;
            }

            if (var9 == 4)
            {
                var11 = 90;
            }

            if (var9 == 5)
            {
                var11 = -90;
            }

            if (var9 == 2 && par1TileEntityChest.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && par1TileEntityChest.adjacentChestZPosition != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = par1TileEntityChest.prevLidAngle + (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle) * par8;
            float var13;

            if (par1TileEntityChest.adjacentChestZNeg != null)
            {
                var13 = par1TileEntityChest.adjacentChestZNeg.prevLidAngle + (par1TileEntityChest.adjacentChestZNeg.lidAngle - par1TileEntityChest.adjacentChestZNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (par1TileEntityChest.adjacentChestXNeg != null)
            {
                var13 = par1TileEntityChest.adjacentChestXNeg.prevLidAngle + (par1TileEntityChest.adjacentChestXNeg.lidAngle - par1TileEntityChest.adjacentChestXNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            var141.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 2.0F);
            var141.renderAll();
            GL11.glPopMatrix();
        }
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityChestAt((TileEntitySkyrootChest)par1TileEntity, par2, par4, par6, par8);
    }
}
