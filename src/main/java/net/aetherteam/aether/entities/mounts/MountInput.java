package net.aetherteam.aether.entities.mounts;

public enum MountInput
{
    FORWARD, BACKWARD, LEFT, RIGHT, JUMP;

    public static MountInput getInputFromString(String name)
    {
        for (MountInput direction : values())
        {
            if (direction.name().equals(name))
            {
                return direction;
            }
        }

        return null;
    }
}

