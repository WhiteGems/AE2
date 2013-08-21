package net.aetherteam.aether.entities.mounts;

public enum MountInput
{
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    JUMP;

    public static MountInput getInputFromString(String name)
    {
        MountInput[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            MountInput direction = arr$[i$];

            if (direction.name().equals(name))
            {
                return direction;
            }
        }

        return null;
    }
}
