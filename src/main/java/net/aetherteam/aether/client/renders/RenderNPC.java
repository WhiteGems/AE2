package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.npc.EntityBasicNPC;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;

public class RenderNPC extends RenderBiped
{
    public RenderNPC(ModelBiped var1, float var2)
    {
        super(var1, var2);
    }

    public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9)
    {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);

        if (var1 instanceof EntityBasicNPC)
        {
            EntityBasicNPC var10 = (EntityBasicNPC)var1;
            String var11 = "Neutral";

            if (var10.isAngry())
            {
                var11 = "Angry";
            }
            else if (var10.isScared())
            {
                var11 = "Scared";
            }
            else if (var10.isBored())
            {
                var11 = "Bored";
            }
            else if (var10.isCurious())
            {
                var11 = "Curious";
            }
            else if (var10.isHappy())
            {
                var11 = "Happy";
            }
            else if (var10.isHungry())
            {
                var11 = "Hungry";
            }
            else if (var10.isInhibited())
            {
                var11 = "Inhibited";
            }
            else if (var10.isPuzzled())
            {
                var11 = "Puzzled";
            }
            else if (var10.isViolent())
            {
                var11 = "Violent";
            }

            String var12 = String.valueOf((int)(var10.getPleasure() * 100.0F) + "%");
            String var13 = String.valueOf((int)(var10.getArousal() * 100.0F) + "%");
            String var14 = String.valueOf((int)(var10.getDominance() * 100.0F) + "%");
            String var15 = "P: " + var12 + " A: " + var13 + " D: " + var14;
            this.func_96449_a(var1, var2, var4, var6, var11, var9, 1.0D);
            this.func_96449_a(var1, var2, var4 + 0.30000001192092896D, var6, var15, var9, 1.0D);
        }
    }
}
