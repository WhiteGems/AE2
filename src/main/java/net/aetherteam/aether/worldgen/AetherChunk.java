package net.aetherteam.aether.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class AetherChunk extends Chunk
{
    public AetherChunk(World par1World, byte[] byte1, int par2, int par3)
    {
        super(par1World, byte1, par2, par3);
    }

    public void updateSkylight()
    {
        resetRelightChecks();
    }

    public void enqueueRelightChecks()
    {
        resetRelightChecks();
    }
}

