package net.aetherteam.aether;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;

import net.aetherteam.aether.client.gui.GuiInventoryAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class AetherKeyHandler extends KeyBindingRegistry.KeyHandler
{
    private static KeyBinding aetherKeyBinding = new KeyBinding("Aether Inventory", 24);

    public AetherKeyHandler()
    {
        super(new KeyBinding[]{aetherKeyBinding}, new boolean[]{false});
    }

    public String getLabel()
    {
        return "Aether Inventory";
    }

    public void keyDown(EnumSet types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if ((kb.equals(aetherKeyBinding)) && (isRepeat))
        {
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
            }

            Minecraft.getMinecraft().displayGuiScreen(new GuiInventoryAether(player));
            return;
        }
    }

    public void keyUp(EnumSet types, KeyBinding kb, boolean tickEnd)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (kb.equals(aetherKeyBinding))
        {
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventoryAether)) ;
            }
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherKeyHandler
 * JD-Core Version:    0.6.2
 */