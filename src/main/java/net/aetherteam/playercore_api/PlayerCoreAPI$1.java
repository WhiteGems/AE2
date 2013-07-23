package net.aetherteam.playercore_api;

import net.aetherteam.playercore_api.PlayerCoreAPI$PlayerCoreType;

class PlayerCoreAPI$1
{
    static final int[] $SwitchMap$net$aetherteam$playercore_api$PlayerCoreAPI$PlayerCoreType = new int[PlayerCoreAPI$PlayerCoreType.values().length];

    static
    {
        try
        {
            $SwitchMap$net$aetherteam$playercore_api$PlayerCoreAPI$PlayerCoreType[PlayerCoreAPI$PlayerCoreType.CLIENT.ordinal()] = 1;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$net$aetherteam$playercore_api$PlayerCoreAPI$PlayerCoreType[PlayerCoreAPI$PlayerCoreType.RENDER.ordinal()] = 2;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$aetherteam$playercore_api$PlayerCoreAPI$PlayerCoreType[PlayerCoreAPI$PlayerCoreType.SERVER.ordinal()] = 3;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
