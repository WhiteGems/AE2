package net.aetherteam.aether.client.gui;

import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.containers.ContainerAltar;
import net.aetherteam.aether.containers.ContainerFreezer;
import net.aetherteam.aether.containers.ContainerIncubator;
import net.aetherteam.aether.containers.ContainerSkyrootWorkbench;

public class AetherGuis
{
    public static void init()
    {
        AetherGuiHandler.registerGui(GuiFreezer.class, ContainerFreezer.class);
        AetherGuiHandler.registerGui(GuiIncubator.class, ContainerIncubator.class);
        AetherGuiHandler.registerGui(GuiAltar.class, ContainerAltar.class);
        AetherGuiHandler.registerGui(GuiSkyrootCrafting.class, ContainerSkyrootWorkbench.class);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.AetherGuis
 * JD-Core Version:    0.6.2
 */