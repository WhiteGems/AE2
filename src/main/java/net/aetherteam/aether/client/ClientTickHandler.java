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

    public void tickEnd(EnumSet var1, Object ... var2)
    {
        if (var1.equals(EnumSet.of(TickType.CLIENT)))
        {
            Minecraft var3 = Minecraft.getMinecraft();

            if (Minecraft.getMinecraft().getIntegratedServer() != null)
            {
                ;
            }

            GuiScreen var4 = var3.currentScreen;

            if (var4 instanceof GuiInventory)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventoryAether(var3.thePlayer));
            }

            if (var4 instanceof GuiContainerCreative)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiAetherContainerCreative(var3.thePlayer));
            }

            MountSystem.processDirections();
            Dungeon var6;

            for (Iterator var5 = DungeonHandler.instance().getInstances().iterator(); var5.hasNext(); var6 = (Dungeon)var5.next())
            {
                ;
            }
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public void tickStart(EnumSet var1, Object ... var2)
    {
        if (var1.equals(EnumSet.of(TickType.CLIENT)))
        {
            ;
        }
    }
}
