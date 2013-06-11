package net.aetherteam.aether.entities.mounts;

public enum MountInput
{
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    JUMP;

    public static MountInput getInputFromString(String var0)
    {
        MountInput[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            MountInput var4 = var1[var3];

            if (var4.name().equals(var0))
            {
                return var4;
            }
        }

        return null;
    }
}
