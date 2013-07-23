package net.aetherteam.aether;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
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
        return "以太物品栏";
    }

    public void keyDown(EnumSet var1, KeyBinding var2, boolean var3, boolean var4)
    {
        EntityClientPlayerMP var5 = Minecraft.getMinecraft().thePlayer;

        if (var2.equals(aetherKeyBinding) && var4)
        {
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
            }
            else
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventoryAether(var5));
            }
        }
    }

    public void keyUp(EnumSet var1, KeyBinding var2, boolean var3)
    {
        EntityClientPlayerMP var4 = Minecraft.getMinecraft().thePlayer;

        if (var2.equals(aetherKeyBinding) && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiInventoryAether)
        {
            ;
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }
}
