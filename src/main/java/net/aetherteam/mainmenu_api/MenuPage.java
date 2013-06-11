package net.aetherteam.mainmenu_api;

import java.io.PrintStream;

public class MenuPage
{
    private MenuSlot[] menuSlots = new MenuSlot[3];
    private int slotIndex = 0;

    public void addMenuSlot(MenuSlot slot)
    {
        this.menuSlots[this.slotIndex] = slot;

        if (this.slotIndex < getPageAmount()) this.slotIndex += 1;
        else System.out.println("Menu Page filled up!");
    }

    public MenuSlot getMenuSlot(int index)
    {
        return this.menuSlots[index];
    }

    public int getPageAmount()
    {
        return this.menuSlots.length;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.MenuPage
 * JD-Core Version:    0.6.2
 */