package net.aetherteam.threadedlighting.world;

import java.util.ArrayList;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ThreadedChunk extends Chunk
{
    public ThreadedChunk(World par1World, int par2, int par3)
    {
        super(par1World, par2, par3);
    }

    /**
     * Initiates the recalculation of both the block-light and sky-light for a given block inside a chunk.
     */
    public void relightBlock(int x, int y, int z)
    {
        ThreadedWorld threadedWorld = (ThreadedWorld)this.worldObj;
        threadedWorld.queueLock.lock();
        threadedWorld.getClass();
        threadedWorld.queue.add(threadedWorld.createRelightBlock(this, x, y, z));
        threadedWorld.queueLock.unlock();
    }

    public void superRelightBlock(int x, int y, int z)
    {
        this.relightBlock(x, y, z);
    }
}
