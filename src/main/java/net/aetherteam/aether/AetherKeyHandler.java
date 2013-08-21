package net.aetherteam.aether;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.aetherteam.aether.client.gui.GuiInventoryAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;

public class AetherKeyHandler extends KeyBindingRegistry.KeyHandler
{
    private static KeyBinding aetherKeyBinding = new KeyBinding("Aether Inventory", 24);

    public AetherKeyHandler()
    {
        super(new KeyBinding[] {aetherKeyBinding}, new boolean[] {false});
    }

    public String getLabel()
    {
        return "Aether Inventory";
    }

    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (kb.equals(aetherKeyBinding) && isRepeat)
        {
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
            }
            else
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventoryAether(player));
            }
        }
    }

    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
    {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (kb.equals(aetherKeyBinding) && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiInventoryAether)
        {
            ;
        }
    }

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }
}
