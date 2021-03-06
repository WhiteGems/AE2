package net.aetherteam.aether.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import java.util.Iterator;
import net.aetherteam.aether.client.gui.GuiAetherContainerCreative;
import net.aetherteam.aether.client.gui.GuiInventoryAether;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.mounts.MountSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ClientTickHandler implements ITickHandler
{
    public String getLabel()
    {
        return null;
    }

    public void tickEnd(EnumSet<TickType> type, Object ... tickData)
    {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (Minecraft.getMinecraft().getIntegratedServer() != null)
            {
                ;
            }

            GuiScreen guiscreen = mc.currentScreen;

            if (guiscreen instanceof GuiInventory)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventoryAether(mc.thePlayer));
            }

            if (guiscreen instanceof GuiContainerCreative)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiAetherContainerCreative(mc.thePlayer));
            }

            MountSystem.processDirections();
            Dungeon dungeon;

            for (Iterator i$ = DungeonHandler.instance().getInstances().iterator(); i$.hasNext(); dungeon = (Dungeon)i$.next())
            {
                ;
            }
        }
    }

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public void tickStart(EnumSet<TickType> type, Object ... tickData)
    {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            ;
        }
    }
}
