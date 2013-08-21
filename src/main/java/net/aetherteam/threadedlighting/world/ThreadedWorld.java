package net.aetherteam.threadedlighting.world;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.logging.ILogAgent;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldSettings;

public class ThreadedWorld extends WorldClient implements Runnable
{
    private Thread thread = new Thread(this);
    public ArrayList queue = new ArrayList();
    public ReentrantLock queueLock = new ReentrantLock();

    public ThreadedWorld(NetClientHandler par1NetClientHandler, WorldSettings par2WorldSettings, int par3, int par4, Profiler par5Profiler, ILogAgent par6iLogAgent)
    {
        super(par1NetClientHandler, par2WorldSettings, par3, par4, par5Profiler, par6iLogAgent);
    }

    /**
     * Adds a IWorldAccess to the list of worldAccesses
     */
    public void addWorldAccess(IWorldAccess par1IWorldAccess)
    {
        if (this.worldAccesses.isEmpty())
        {
            this.thread.start();
        }

        super.addWorldAccess(par1IWorldAccess);
    }

    /**
     * Removes a worldAccess from the worldAccesses object
     */
    public void removeWorldAccess(IWorldAccess par1IWorldAccess)
    {
        super.removeWorldAccess(par1IWorldAccess);

        if (this.worldAccesses.isEmpty())
        {
            this.thread.stop();
        }
    }

    public void updateAllLightTypes(int par1, int par2, int par3)
    {
        if (!this.provider.hasNoSky)
        {
            this.queueLock.lock();
            this.queue.add(new UpdateSkylight(par1, par2, par3));
            this.queueLock.unlock();
        }

        this.updateLightByType(EnumSkyBlock.Block, par1, par2, par3);
    }

    public void run()
    {
        while (true)
        {
            long start = System.currentTimeMillis();
            this.queueLock.lock();

            if (this.queue.size() > 0)
            {
                Object[] time = new Object[this.queue.size()];
                this.queue.toArray(time);
                this.queue.clear();
                this.queueLock.unlock();
                Object[] arr$ = time;
                int e = time.length;

                for (int i$ = 0; i$ < e; ++i$)
                {
                    Object object = arr$[i$];

                    if (object instanceof UpdateSkylight)
                    {
                        UpdateSkylight data = (UpdateSkylight)object;
                        this.updateLightByType(EnumSkyBlock.Sky, data.x, data.y, data.z);
                    }
                    else if (object instanceof RelightBlock)
                    {
                        RelightBlock var11 = (RelightBlock)object;
                        var11.chunk.superRelightBlock(var11.x, var11.y, var11.z);
                    }
                }
            }
            else
            {
                this.queueLock.unlock();
            }

            long var10 = System.currentTimeMillis() - start;

            if (var10 > 100L)
            {
                System.out.println("prevented lag lasting " + var10 + " milliseconds.");
            }

            if (var10 < 10L)
            {
                try
                {
                    Thread.currentThread();
                    Thread.sleep(10L - var10);
                }
                catch (InterruptedException var9)
                {
                    var9.printStackTrace();
                }
            }
        }
    }

    RelightBlock createRelightBlock(ThreadedChunk chunk, int x, int y, int z)
    {
        return new RelightBlock(chunk,x,y,z);
    }

    public class RelightBlock
    {
        ThreadedChunk chunk;
        int x;
        int y;
        int z;

        RelightBlock(ThreadedChunk chunk, int x, int y, int z)
        {
            this.chunk = chunk;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    class UpdateSkylight
    {
        int x;
        int y;
        int z;

        UpdateSkylight(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
