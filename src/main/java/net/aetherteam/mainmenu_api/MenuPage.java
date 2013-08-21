package net.aetherteam.mainmenu_api;

public class MenuPage
{
    private MenuSlot[] menuSlots = new MenuSlot[3];
    private int slotIndex = 0;

    public void addMenuSlot(MenuSlot slot)
    {
        this.menuSlots[this.slotIndex] = slot;

        if (this.slotIndex < this.getPageAmount())
        {
            ++this.slotIndex;
        }
        else
        {
            System.out.println("Menu Page filled up!");
        }
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
