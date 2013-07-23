package net.aetherteam.aether;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.choices.MoaChoice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class AetherMoaColour
{
    public int ID;
    public int colour;
    public int jumps;
    public int chance;
    public String name;
    public String texturename;
    public static List names = new ArrayList();
    private static int totalChance;
    public static List colours = new ArrayList();
    private static Random random = new Random();

    public AetherMoaColour(int i, int j, int k, int l, String s,String texture)
    {
        this.ID = i;
        this.colour = j;
        this.jumps = k;
        this.chance = l;
        totalChance += l;
        this.name = s;
        this.texturename = texture;
        colours.add(this);
        names.add(this.name);
    }

    public String getTexture(boolean saddled)
    {
        return "/net/aetherteam/aether/client/sprites/moa/" + (saddled ? "saddle_" : "moa_") + this.texturename + ".png";
    }

    public String getTexture(boolean saddled, EntityPlayer player)
    {
        if (Aether.syncDonatorList.isDonator(player.username) && saddled)
        {
            Aether.getInstance();
            Donator var3 = Aether.syncDonatorList.getDonator(player.username);
            boolean var4 = var3.containsChoiceType(EnumChoiceType.MOA);
            MoaChoice var5 = null;

            if (var4)
            {
                var5 = (MoaChoice)var3.getChoiceFromType(EnumChoiceType.MOA);
                return var5.textureFile.localURL;
            }
        }

        return "/net/aetherteam/aether/client/sprites/moa/" + ((saddled) && (!player.isPotionActive(Potion.invisibility)) ? "saddle_" : "moa_") + this.texturename + ".png";
    }

    public static AetherMoaColour pickRandomMoa()
    {
        int var0 = random.nextInt(totalChance);

        for (int var1 = 0; var1 < colours.size(); ++var1)
        {
            if (var0 < ((AetherMoaColour)colours.get(var1)).chance)
            {
                return (AetherMoaColour)colours.get(var1);
            }

            var0 -= ((AetherMoaColour)colours.get(var1)).chance;
        }

        return (AetherMoaColour)colours.get(0);
    }

    public static AetherMoaColour getColour(int var0)
    {
        for (int var1 = 0; var1 < colours.size(); ++var1)
        {
            if (((AetherMoaColour)colours.get(var1)).ID == var0)
            {
                return (AetherMoaColour)colours.get(var1);
            }
        }

        return (AetherMoaColour)colours.get(0);
    }

    public static String[] getNames()
    {
        String[] var0 = new String[names.size()];
        var0 = (String[])names.toArray(var0);
        return var0;
    }

    static
    {
        new AetherMoaColour(0, 7829503, 3, 100, "蓝色", "Blue");
        new AetherMoaColour(1, 16777215, 4, 20, "白色", "White");
        new AetherMoaColour(2, 2236962, 8, 5, "黑色", "Black");
    }
}
