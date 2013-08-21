package net.aetherteam.aether.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityTreasureChestRenderer extends TileEntityChestRenderer
{
    private static final ResourceLocation TEXTURE_TREASURE_CHEST = new ResourceLocation("aether", "textures/tile_entities/treasureChest.png");

    /** The normal small chest model. */
    private ModelChest chestModel = new ModelChest();

    /** The large double chest model. */
    private ModelChest largeChestModel = new ModelLargeChest();

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        this.renderTileEntityChestAt((TileEntityChest)tileentity, d, d1, d2, f);
    }

    /**
     * Renders the TileEntity for the chest at a position.
     */
    public void renderTileEntityChestAt(TileEntityChest tileentitychest, double d, double d1, double d2, float f)
    {
        int i;

        if (tileentitychest.worldObj == null)
        {
            i = 0;
        }
        else
        {
            Block modelchest = tileentitychest.getBlockType();
            i = tileentitychest.getBlockMetadata();

            if (modelchest != null && i == 0)
            {
                ((BlockChest)modelchest).unifyAdjacentChests(tileentitychest.worldObj, tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord);
                i = tileentitychest.getBlockMetadata();
            }

            tileentitychest.checkForAdjacentChests();
        }

        if (tileentitychest.adjacentChestZNeg == null && tileentitychest.adjacentChestXNeg == null)
        {
            ModelChest modelchest1;

            if (tileentitychest.adjacentChestXPos == null && tileentitychest.adjacentChestZPosition == null)
            {
                modelchest1 = this.chestModel;
                this.func_110628_a(TEXTURE_TREASURE_CHEST);
            }
            else
            {
                modelchest1 = this.largeChestModel;
                this.func_110628_a(TEXTURE_TREASURE_CHEST);
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short j = 0;

            if (i == 2)
            {
                j = 180;
            }

            if (i == 3)
            {
                j = 0;
            }

            if (i == 4)
            {
                j = 90;
            }

            if (i == 5)
            {
                j = -90;
            }

            if (i == 2 && tileentitychest.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && tileentitychest.adjacentChestZPosition != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)j, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = tileentitychest.prevLidAngle + (tileentitychest.lidAngle - tileentitychest.prevLidAngle) * f;
            float f3;

            if (tileentitychest.adjacentChestZNeg != null)
            {
                f3 = tileentitychest.adjacentChestZNeg.prevLidAngle + (tileentitychest.adjacentChestZNeg.lidAngle - tileentitychest.adjacentChestZNeg.prevLidAngle) * f;

                if (f3 > f1)
                {
                    f1 = f3;
                }
            }

            if (tileentitychest.adjacentChestXNeg != null)
            {
                f3 = tileentitychest.adjacentChestXNeg.prevLidAngle + (tileentitychest.adjacentChestXNeg.lidAngle - tileentitychest.adjacentChestXNeg.prevLidAngle) * f;

                if (f3 > f1)
                {
                    f1 = f3;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            modelchest1.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
            modelchest1.renderAll();
            GL11.glPopMatrix();
        }
    }
}
