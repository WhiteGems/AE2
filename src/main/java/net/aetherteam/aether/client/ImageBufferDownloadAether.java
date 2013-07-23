package net.aetherteam.aether.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import net.minecraft.client.renderer.CallableParticlePositionInfo;
import net.minecraft.client.renderer.ThreadDownloadImage;

@SideOnly(Side.CLIENT)
public class ImageBufferDownloadAether extends CallableParticlePositionInfo
    implements ThreadDownloadImage
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public ImageBufferDownloadAether setResolution(int width, int height)
    {
        this.imageWidth = width;
        this.imageHeight = height;
        return this;
    }

    public BufferedImage a(BufferedImage par1BufferedImage)
    {
        if (par1BufferedImage == null)
        {
            return null;
        }

        BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedimage1.getGraphics();
        graphics.drawImage(par1BufferedImage, 0, 0, (ImageObserver)null);
        graphics.dispose();
        this.imageData = ((DataBufferInt)bufferedimage1.getRaster().getDataBuffer()).getData();
        setAreaOpaque(0, 0, this.imageWidth, this.imageWidth / 2);
        setAreaTransparent(this.imageWidth, 0, this.imageHeight, this.imageWidth);
        setAreaOpaque(0, this.imageWidth / 2, this.imageHeight, this.imageWidth);
        boolean flag = false;

        for (int i = 32; i < 64; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                int k = this.imageData[(i + j * 64)];

                if ((k >> 24 & 0xFF) < 128)
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            for (i = 32; i < 64; i++)
            {
                for (int j = 0; j < 16; j++)
                {
                    int k = this.imageData[(i + j * 64)];

                    if ((k >> 24 & 0xFF) < 128)
                    {
                        flag = true;
                    }
                }
            }
        }

        return bufferedimage1;
    }

    private void setAreaTransparent(int par1, int par2, int par3, int par4)
    {
        if (!hasTransparency(par1, par2, par3, par4))
        {
            for (int i1 = par1; i1 < par3; i1++)
            {
                for (int j1 = par2; j1 < par4; j1++)
                {
                    this.imageData[(i1 + j1 * this.imageWidth)] &= 16777215;
                }
            }
        }
    }

    private void setAreaOpaque(int par1, int par2, int par3, int par4)
    {
        for (int i1 = par1; i1 < par3; i1++)
        {
            for (int j1 = par2; j1 < par4; j1++)
            {
                this.imageData[(i1 + j1 * this.imageWidth)] |= -16777216;
            }
        }
    }

    private boolean hasTransparency(int par1, int par2, int par3, int par4)
    {
        for (int i1 = par1; i1 < par3; i1++)
        {
            for (int j1 = par2; j1 < par4; j1++)
            {
                int k1 = this.imageData[(i1 + j1 * this.imageWidth)];

                if ((k1 >> 24 & 0xFF) < 128)
                {
                    return true;
                }
            }
        }

        return false;
    }
}

