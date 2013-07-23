package net.aetherteam.aether.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityTreasureChestRenderer extends TileEntityBeaconRenderer
{
    private ModelBook chestModel;
    private ModelBook largeChestModel;

    public TileEntityTreasureChestRenderer()
    {
        this.chestModel = new ModelBook();
        this.largeChestModel = new ModelBiped();
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        a((TileEntityChest)tileentity, d, d1, d2, f);
    }

    public void a(TileEntityChest tileentitychest, double d, double d1, double d2, float f)
    {
        int i;
        int i;

        if (tileentitychest.worldObj == null)
        {
            i = 0;
        }
        else
        {
            Block block = tileentitychest.getBlockType();
            i = tileentitychest.getBlockMetadata();

            if ((block != null) && (i == 0))
            {
                ((BlockChest)block).unifyAdjacentChests(tileentitychest.worldObj, tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord);
                i = tileentitychest.getBlockMetadata();
            }

            tileentitychest.checkForAdjacentChests();
        }

        if ((tileentitychest.adjacentChestZNeg != null) || (tileentitychest.adjacentChestXNeg != null))
        {
            return;
        }

        ModelBook modelchest;

        if ((tileentitychest.adjacentChestXPos != null) || (tileentitychest.adjacentChestZPosition != null))
        {
            ModelBook modelchest = this.largeChestModel;
            bindTextureByName("/net/aetherteam/aether/client/sprites/tile_entities/treasureChest.png");
        }
        else
        {
            modelchest = this.chestModel;
            bindTextureByName("/net/aetherteam/aether/client/sprites/tile_entities/treasureChest.png");
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int j = 0;

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

        if ((i == 2) && (tileentitychest.adjacentChestXPos != null))
        {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
        }

        if ((i == 5) && (tileentitychest.adjacentChestZPosition != null))
        {
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        }

        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = tileentitychest.prevLidAngle + (tileentitychest.lidAngle - tileentitychest.prevLidAngle) * f;

        if (tileentitychest.adjacentChestZNeg != null)
        {
            float f2 = tileentitychest.adjacentChestZNeg.prevLidAngle + (tileentitychest.adjacentChestZNeg.lidAngle - tileentitychest.adjacentChestZNeg.prevLidAngle) * f;

            if (f2 > f1)
            {
                f1 = f2;
            }
        }

        if (tileentitychest.adjacentChestXNeg != null)
        {
            float f3 = tileentitychest.adjacentChestXNeg.prevLidAngle + (tileentitychest.adjacentChestXNeg.lidAngle - tileentitychest.adjacentChestXNeg.prevLidAngle) * f;

            if (f3 > f1)
            {
                f1 = f3;
            }
        }

        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        modelchest.coverRight.f = (-(f1 * (float)Math.PI / 2.0F));
        modelchest.a();
        GL11.glPopMatrix();
    }
}

