package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.npc.EntityBasicNPC;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import net.minecraft.entity.EntityLiving;

public class RenderNPC extends RenderGiantZombie
{
    public RenderNPC(ModelGhast modelBiped, float par2)
    {
        super(modelBiped, par2);
    }

    public void doRenderLiving(EntityLiving living, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(living, par2, par4, par6, par8, par9);

        if ((living instanceof EntityBasicNPC))
        {
            EntityBasicNPC npc = (EntityBasicNPC)living;
            String mood = "Neutral";

            if (npc.isAngry())
            {
                mood = "Angry";
            }
            else if (npc.isScared())
            {
                mood = "Scared";
            }
            else if (npc.isBored())
            {
                mood = "Bored";
            }
            else if (npc.isCurious())
            {
                mood = "Curious";
            }
            else if (npc.isHappy())
            {
                mood = "Happy";
            }
            else if (npc.isHungry())
            {
                mood = "Hungry";
            }
            else if (npc.isInhibited())
            {
                mood = "Inhibited";
            }
            else if (npc.isPuzzled())
            {
                mood = "Puzzled";
            }
            else if (npc.isViolent())
            {
                mood = "Violent";
            }

            String p = String.valueOf((int)(npc.getPleasure() * 100.0F) + "%");
            String a = String.valueOf((int)(npc.getArousal() * 100.0F) + "%");
            String d = String.valueOf((int)(npc.getDominance() * 100.0F) + "%");
            String PAD = "P: " + p + " A: " + a + " D: " + d;
            func_96449_a(living, par2, par4, par6, mood, par9, 1.0D);
            func_96449_a(living, par2, par4 + 0.300000011920929D, par6, PAD, par9, 1.0D);
        }
    }
}

