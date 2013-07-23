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

    protected ItemAetherMusicDisk(int itemID, String s, String artist, String song)
    {
        super(itemID, s);
        this.artistName = artist;
        this.songName = song;
    }

    public String getRecordTitle()
    {
        return this.artistName + " - " + this.songName;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(getUnlocalizedName().replace("item.", ""));
    }
}

