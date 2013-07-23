package net.aetherteam.aether.worldgen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderAether extends WorldProviderSurface
{
    private float[] colorsSunriseSunset = new float[4];

    public static boolean isDaey = false;

    public float[] calcSunriseSunsetColors(float f, float f1)
    {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(f * (float)Math.PI * 2.0F) - 0.0F;
        float f4 = -0.0F;

        if ((f3 >= f4 - f2) && (f3 <= f4 + f2))
        {
            float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
            f6 *= f6;
            this.colorsSunriseSunset[0] = (f5 * 0.3F + 0.1F);
            this.colorsSunriseSunset[1] = (f5 * f5 * 0.7F + 0.2F);
            this.colorsSunriseSunset[2] = (f5 * f5 * 0.7F + 0.2F);
            this.colorsSunriseSunset[3] = f6;
            return this.colorsSunriseSunset;
        }

        return null;
    }

    public boolean isSurfaceWorld()
    {
        return true;
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = this.worldObj.getFirstUncoveredBlock(i, j);

        if (k == 0)
        {
            return false;
        }

        return Block.blocksList[k].blockMaterial.isSolid();
    }

    public boolean canRespawnHere()
    {
        return false;
    }

    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderAether(this.worldObj, this.worldObj.getTotalWorldTime());
    }

    public String getDepartMessage()
    {
        return "Descending from the Aether";
    }

    public String getDimensionName()
    {
        return "Aether";
    }

    public Vec3 getFogColor(float f, float f1)
    {
        int i = 8421536;

        if (isDaey)
        {
            i = 104890528;
        }

        float f2 = MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        float f3 = (i >> 16 & 0xFF) / 255.0F;
        float f4 = (i >> 8 & 0xFF) / 255.0F;
        float f5 = (i & 0xFF) / 255.0F;
        f3 *= (f2 * 0.94F + 0.06F);
        f4 *= (f2 * 0.94F + 0.06F);
        f5 *= (f2 * 0.91F + 0.09F);
        return this.worldObj.U().getVecFromPool(f3, f4, f5);
    }

    public String getSaveFolder()
    {
        return "AETHER";
    }

    public double getVoidFogYFactor()
    {
        return 100.0D;
    }

    public String getWelcomeMessage()
    {
        return "Ascending into the Aether";
    }

    public boolean getWorldHasVoidParticles()
    {
        return false;
    }

    public boolean isSkyColored()
    {
        return true;
    }

    protected void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerAether(1.0D);
    }

    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer()
    {
        return super.getSkyRenderer();
    }

    public double getHorizon()
    {
        return 0.0D;
    }
}

