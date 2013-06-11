package net.aetherteam.aether.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class AetherChunk extends Chunk
{
    public AetherChunk(World var1, byte[] var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
    }

    /**
     * Checks whether skylight needs updated; if it does, calls updateSkylight_do
     */
    public void updateSkylight()
    {
        this.resetRelightChecks();
    }

    /**
     * Called once-per-chunk-per-tick, and advances the round-robin relight check index per-storage-block by up to 8
     * blocks at a time. In a worst-case scenario, can potentially take up to 1.6 seconds, calculated via
     * (4096/(8*16))/20, to re-check all blocks in a chunk, which could explain both lagging light updates in certain
     * cases as well as Nether relight
     */
    public void enqueueRelightChecks()
    {
        this.resetRelightChecks();
    }
}
