package net.aetherteam.aether.dungeons;

public class TrackedMobSpawnerCoord extends TrackedCoord
{
    private String mobID;

    public TrackedMobSpawnerCoord(int var1, int var2, int var3, String var4)
    {
        super(var1, var2, var3);
        this.mobID = var4;
    }

    public String getMobID()
    {
        return this.mobID;
    }
}
