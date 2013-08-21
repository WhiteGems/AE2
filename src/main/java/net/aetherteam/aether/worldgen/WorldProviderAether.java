package net.aetherteam.aether.worldgen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderAether extends WorldProviderSurface
{
    /** Array for sunrise/sunset colors (RGBA) */
    private float[] colorsSunriseSunset = new float[4];
    public static boolean isDaey = false;

    /**
     * Returns array with sunrise/sunset colors
     */
    public float[] calcSunriseSunsetColors(float f, float f1)
    {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(f * (float)Math.PI * 2.0F) - 0.0F;
        float f4 = -0.0F;

        if (f3 >= f4 - f2 && f3 <= f4 + f2)
        {
            float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
            f6 *= f6;
            this.colorsSunriseSunset[0] = f5 * 0.3F + 0.1F;
            this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f5 * f5 * 0.7F + 0.2F;
            this.colorsSunriseSunset[3] = f6;
            return this.colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    public boolean isSurfaceWorld()
    {
        return true;
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = this.worldObj.getFirstUncoveredBlock(i, j);
        return k == 0 ? false : Block.blocksList[k].blockMaterial.isSolid();
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean canRespawnHere()
    {
        return false;
    }

    /**
     * Returns a new chunk provider which generates chunks for this world
     */
    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderAether(this.worldObj, this.worldObj.getSeed());
    }

    public String getDepartMessage()
    {
        return "Descending from the Aether";
    }

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    public String getDimensionName()
    {
        return "Aether";
    }

    /**
     * Return Vec3D with biome specific fog color
     */
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

        float f3 = (float)(i >> 16 & 255) / 255.0F;
        float f4 = (float)(i >> 8 & 255) / 255.0F;
        float f5 = (float)(i & 255) / 255.0F;
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return this.worldObj.getWorldVec3Pool().getVecFromPool((double)f3, (double)f4, (double)f5);
    }

    public String getSaveFolder()
    {
        return "AETHER";
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its
     * maximum. The default factor of 0.03125 relative to 256, for example, means the void fog will be at its maximum at
     * (256*0.03125), or 8.
     */
    public double getVoidFogYFactor()
    {
        return 100.0D;
    }

    public String getWelcomeMessage()
    {
        return "Ascending into the Aether";
    }

    /**
     * returns true if this dimension is supposed to display void particles and pull in the far plane based on the
     * user's Y offset.
     */
    public boolean getWorldHasVoidParticles()
    {
        return false;
    }

    public boolean isSkyColored()
    {
        return true;
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
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
