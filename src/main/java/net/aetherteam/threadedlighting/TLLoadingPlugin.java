package net.aetherteam.threadedlighting;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions( {"net.aetherteam.threadedlighting", "net.aetherteam.threadedlighting.asm"})
public class TLLoadingPlugin implements IFMLLoadingPlugin
{
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    public String[] getASMTransformerClass()
    {
        return new String[] {"net.aetherteam.threadedlighting.asm.TLTransformer"};
    }

    public String getModContainerClass()
    {
        return "net.aetherteam.threadedlighting.ThreadedLighting";
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map<String, Object> data) {}
}
