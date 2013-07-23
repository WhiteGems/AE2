package net.aetherteam.playercore_api;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions( {"net.aetherteam.playercore_api, net.aetherteam.playercore_api.asm"})
public class PlayerCoreLoadingPlugin implements IFMLLoadingPlugin
{
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    public String[] getASMTransformerClass()
    {
        return new String[] {"net.aetherteam.playercore_api.asm.PlayerCoreTransformer"};
    }

    public String getModContainerClass()
    {
        return "net.aetherteam.playercore_api.PlayerCoreAPI";
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map var1) {}
}
