package net.aetherteam.aether.dungeons;

public class TrackedTileEntityCoord extends TrackedCoord
{
    private int trackedBlock;
    private int trackedBlockMeta;

    public TrackedTileEntityCoord(int var1, int var2, int var3, int var4, int var5)
    {
        super(var1, var2, var3);
        this.trackedBlock = var4;
        this.trackedBlockMeta = var5;
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
