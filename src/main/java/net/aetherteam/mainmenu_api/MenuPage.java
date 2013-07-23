package net.aetherteam.mainmenu_api;

public class MenuPage
{
    private MenuSlot[] menuSlots = new MenuSlot[3];
    private int slotIndex = 0;

    public void addMenuSlot(MenuSlot var1)
    {
        this.menuSlots[this.slotIndex] = var1;

        if (this.slotIndex < this.getPageAmount())
        {
            ++this.slotIndex;
        }
        else
        {
            System.out.println("Menu Page filled up!");
        }
    }

    public MenuSlot getMenuSlot(int var1)
    {
        return this.menuSlots[var1];
    }

    public int getPageAmount()
    {
        return this.menuSlots.length;
    }
}
