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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.dungeons.TrackedMobSpawnerCoord
 * JD-Core Version:    0.6.2
 */