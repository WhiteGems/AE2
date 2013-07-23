package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.blocks.BlockSkyrootChest;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntitySkyrootChestRenderer extends TileEntityRenderer
{
    private ModelBook chestModel = new ModelBook();

    private ModelBook largeChestModel = new ModelBiped();

    public void renderTileEntityChestAt(TileEntitySkyrootChest par1TileEntityChest, double par2, double par4, double par6, float par8)
    {
        int var9;
        int var9;

        if (!par1TileEntityChest.func_70309_m())
        {
            var9 = 0;
        }
        else
        {
            Block var10 = par1TileEntityChest.getBlockType();
            var9 = par1TileEntityChest.getBlockMetadata();

            if ((var10 != null) && (var9 == 0))
            {
                ((BlockSkyrootChest)var10).unifyAdjacentChests(par1TileEntityChest.getWorldObj(), par1TileEntityChest.xCoord, par1TileEntityChest.yCoord, par1TileEntityChest.zCoord);
                var9 = par1TileEntityChest.getBlockMetadata();
            }

            par1TileEntityChest.checkForAdjacentChests();
        }

        if ((par1TileEntityChest.adjacentChestZNeg == null) && (par1TileEntityChest.adjacentChestXNeg == null))
        {
            ModelBook var14;

            if ((par1TileEntityChest.adjacentChestXPos == null) && (par1TileEntityChest.adjacentChestZPosition == null))
            {
                ModelBook var14 = this.chestModel;
                a("/net/aetherteam/aether/client/sprites/tile_entities/skyrootChest.png");
            }
            else
            {
                var14 = this.largeChestModel;
                a("/net/aetherteam/aether/client/sprites/tile_entities/skyrootLargeChest.png");
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

            if ((var9 == 2) && (par1TileEntityChest.adjacentChestXPos != null))
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if ((var9 == 5) && (par1TileEntityChest.adjacentChestZPosition != null))
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = par1TileEntityChest.prevLidAngle + (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle) * par8;

            if (par1TileEntityChest.adjacentChestZNeg != null)
            {
                float var13 = par1TileEntityChest.adjacentChestZNeg.prevLidAngle + (par1TileEntityChest.adjacentChestZNeg.lidAngle - par1TileEntityChest.adjacentChestZNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (par1TileEntityChest.adjacentChestXNeg != null)
            {
                float var13 = par1TileEntityChest.adjacentChestXNeg.prevLidAngle + (par1TileEntityChest.adjacentChestXNeg.lidAngle - par1TileEntityChest.adjacentChestXNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            var14.coverRight.f = (-(var12 * (float)Math.PI / 2.0F));
            var14.a();
            GL11.glPopMatrix();
        }
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        renderTileEntityChestAt((TileEntitySkyrootChest)par1TileEntity, par2, par4, par6, par8);
    }
}

