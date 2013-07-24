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
            String var11 = "日常";

            if (var10.isAngry())
            {
                var11 = "愤怒";
            }
            else if (var10.isScared())
            {
                var11 = "恐惧";
            }
            else if (var10.isBored())
            {
                var11 = "厌烦";
            }
            else if (var10.isCurious())
            {
                var11 = "好奇";
            }
            else if (var10.isHappy())
            {
                var11 = "开心";
            }
            else if (var10.isHungry())
            {
                var11 = "着急";
            }
            else if (var10.isInhibited())
            {
                var11 = "羞怯";
            }
            else if (var10.isPuzzled())
            {
                var11 = "迷惑";
            }
            else if (var10.isViolent())
            {
                var11 = "暴躁";
            }

            String var12 = String.valueOf((int)(var10.getPleasure() * 100.0F) + "%");
            String var13 = String.valueOf((int)(var10.getArousal() * 100.0F) + "%");
            String var14 = String.valueOf((int)(var10.getDominance() * 100.0F) + "%");
            String var15 = "愉悦度: " + var12 + "兴奋度: " + var13 + "控制度: " + var14;
            this.func_96449_a(var1, var2, var4, var6, var11, var9, 1.0D);
            this.func_96449_a(var1, var2, var4 + 0.30000001192092896D, var6, var15, var9, 1.0D);
        }
    }
}
