package net.aetherteam.aether.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;

@SideOnly(Side.CLIENT)
public class ImageBufferDownloadAether extends ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public ImageBufferDownloadAether setResolution(int var1, int var2)
    {
        this.imageWidth = var1;
        this.imageHeight = var2;
        return this;
    }

    public BufferedImage parseUserSkin(BufferedImage var1)
    {
        if (var1 == null)
        {
            return null;
        } else
        {
            BufferedImage var2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics var3 = var2.getGraphics();
            var3.drawImage(var1, 0, 0, (ImageObserver) null);
            var3.dispose();
            this.imageData = ((DataBufferInt) var2.getRaster().getDataBuffer()).getData();
            this.setAreaOpaque(0, 0, this.imageWidth, this.imageWidth / 2);
            this.setAreaTransparent(this.imageWidth, 0, this.imageHeight, this.imageWidth);
            this.setAreaOpaque(0, this.imageWidth / 2, this.imageHeight, this.imageWidth);
            boolean var4 = false;
            int var7;
            int var6;
            int var5;

            for (var5 = 32; var5 < 64; ++var5)
            {
                for (var6 = 0; var6 < 16; ++var6)
                {
                    var7 = this.imageData[var5 + var6 * 64];

                    if ((var7 >> 24 & 255) < 128)
                    {
                        var4 = true;
                    }
                }
            }

            if (!var4)
            {
                for (var5 = 32; var5 < 64; ++var5)
                {
                    for (var6 = 0; var6 < 16; ++var6)
                    {
                        var7 = this.imageData[var5 + var6 * 64];

                        if ((var7 >> 24 & 255) < 128)
                        {
                            var4 = true;
                        }
                    }
                }
            }

            return var2;
        }
    }

    private void setAreaTransparent(int var1, int var2, int var3, int var4)
    {
        if (!this.hasTransparency(var1, var2, var3, var4))
        {
            for (int var5 = var1; var5 < var3; ++var5)
            {
                for (int var6 = var2; var6 < var4; ++var6)
                {
                    this.imageData[var5 + var6 * this.imageWidth] &= 16777215;
                }
            }
        }
    }

    private void setAreaOpaque(int var1, int var2, int var3, int var4)
    {
        for (int var5 = var1; var5 < var3; ++var5)
        {
            for (int var6 = var2; var6 < var4; ++var6)
            {
                this.imageData[var5 + var6 * this.imageWidth] |= -16777216;
            }
        }
    }

    private boolean hasTransparency(int var1, int var2, int var3, int var4)
    {
        for (int var5 = var1; var5 < var3; ++var5)
        {
            for (int var6 = var2; var6 < var4; ++var6)
            {
                int var7 = this.imageData[var5 + var6 * this.imageWidth];

                if ((var7 >> 24 & 255) < 128)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
