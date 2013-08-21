package net.aetherteam.aether.dungeons;

public class TrackedTileEntityCoord extends TrackedCoord
{
    private int trackedBlock;
    private int trackedBlockMeta;

    public TrackedTileEntityCoord(int x, int y, int z, int trackedBlock, int trackedBlockMeta)
    {
        super(x, y, z);
        this.trackedBlock = trackedBlock;
        this.trackedBlockMeta = trackedBlockMeta;
    }

    public int getBlock()
    {
        return this.trackedBlock;
    }

    public int getBlockMeta()
    {
        return this.trackedBlockMeta;
    }
}
