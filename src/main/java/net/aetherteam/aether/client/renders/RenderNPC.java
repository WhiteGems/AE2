package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.npc.EntityBasicNPC;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderNPC extends RenderBiped
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("/npc/angel.png");

    public RenderNPC(ModelBiped modelBiped, float par2)
    {
        super(modelBiped, par2);
    }

    public void doRenderLiving(EntityLiving living, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(living, par2, par4, par6, par8, par9);

        if (living instanceof EntityBasicNPC)
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
            this.func_96449_a(living, par2, par4, par6, mood, par9, 1.0D);
            this.func_96449_a(living, par2, par4 + 0.30000001192092896D, par6, PAD, par9, 1.0D);
        }
    }

    protected ResourceLocation func_110775_a(Entity par1Entity)
    {
        return TEXTURE;
    }
}
