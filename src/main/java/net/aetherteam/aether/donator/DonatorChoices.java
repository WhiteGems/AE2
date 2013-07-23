package net.aetherteam.aether.donator;

import java.util.ArrayList;
import net.aetherteam.aether.donator.choices.AercloudCapeChoice;
import net.aetherteam.aether.donator.choices.ClassicMoaChoice;
import net.aetherteam.aether.donator.choices.PhoenixMoaChoice;
import net.aetherteam.aether.donator.choices.SliderCapeChoice;
import net.aetherteam.aether.donator.choices.ValkyrieMoaChoice;

public class DonatorChoices
{
    public ArrayList capeChoices = new ArrayList();
    public ArrayList moaChoices = new ArrayList();

    public DonatorChoices()
    {
        this.capeChoices.add(new SliderCapeChoice());
        this.capeChoices.add(new AercloudCapeChoice());
        this.moaChoices.add(new ValkyrieMoaChoice());
        this.moaChoices.add(new PhoenixMoaChoice());
        this.moaChoices.add(new ClassicMoaChoice());
    }
}
