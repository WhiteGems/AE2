package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;

public class ItemAetherMusicDisk extends ItemRecord
{
    public String artistName;
    public String songName;

    protected ItemAetherMusicDisk(int var1, String var2, String var3, String var4)
    {
        super(var1, var2);
        this.artistName = var3;
        this.songName = var4;
    }

    /**
     * Return the title for this record.
     */
    public String getRecordTitle()
    {
        return this.artistName + " - " + this.songName;
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister var1)
    {
        this.itemIcon = var1.registerIcon(this.getUnlocalizedName().replace("item.", ""));
    }
}
