package net.aetherteam.aether.dungeons;

public class TrackedMobSpawnerCoord extends TrackedCoord
{
    private String mobID;

    public TrackedMobSpawnerCoord(int x, int y, int z, String mobID)
    {
        super(x, y, z);
        this.mobID = mobID;
    }

    public String getMobID()
    {
        return this.mobID;
    }
}

