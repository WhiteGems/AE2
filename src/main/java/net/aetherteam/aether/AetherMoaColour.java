package net.aetherteam.aether;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.donator.Donator;
import net.aetherteam.aether.donator.EnumChoiceType;
import net.aetherteam.aether.donator.choices.MoaChoice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class AetherMoaColour
{
    public int ID;
    public int colour;
    public int jumps;
    public int chance;
    public String name;
    public String texturename;
    public static List<String> names = new ArrayList();
    private static int totalChance;
    public static List<AetherMoaColour> colours = new ArrayList();
    private static Random random = new Random();

    public AetherMoaColour(int i, int j, int k, int l, String s, String texture)
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

    public ResourceLocation getTexture(boolean saddled)
    {
        return new ResourceLocation("aether", "textures/moa/" + (saddled ? "saddle_" : "moa_") + this.texturename + ".png");
    }

    public ResourceLocation getTexture(boolean saddled, EntityPlayer player)
    {
        if (Aether.syncDonatorList.isDonator(player.username) && saddled)
        {
            Aether.getInstance();
            Donator donator = Aether.syncDonatorList.getDonator(player.username);
            boolean hasChoice = donator.containsChoiceType(EnumChoiceType.MOA);
            MoaChoice choice = null;

            if (hasChoice)
            {
                choice = (MoaChoice)donator.getChoiceFromType(EnumChoiceType.MOA);
                return choice.textureFile.localURL;
            }
        }

        return new ResourceLocation("aether", "textures/moa/" + (saddled && !player.isPotionActive(Potion.invisibility) ? "saddle_" : "moa_") + this.texturename + ".png");
    }

    public static AetherMoaColour pickRandomMoa()
    {
        int i = random.nextInt(totalChance);

        for (AetherMoaColour colour1 : colours)
        {
            if (i < colour1.chance)
            {
                return colour1;
            }

            i -= colour1.chance;
        }

        return colours.get(0);
    }

    public static AetherMoaColour getColour(int ID)
    {
        for (AetherMoaColour colour1 : colours)
        {
            if (colour1.ID == ID)
            {
                return colour1;
            }
        }

        return colours.get(0);
    }

    public static String[] getNames()
    {
        String[] namesArray = new String[names.size()];
        namesArray = names.toArray(namesArray);
        return namesArray;
    }

    static
    {
        new AetherMoaColour(0, 7829503, 3, 100, "蓝色", "Blue");
        new AetherMoaColour(1, 16777215, 4, 20, "白色", "White");
        new AetherMoaColour(2, 2236962, 8, 5, "黑色", "Black");
    }
}
