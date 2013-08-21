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
            String mood = "日常";

            if (npc.isAngry())
            {
                mood = "愤怒";
            }
            else if (npc.isScared())
            {
                mood = "恐惧";
            }
            else if (npc.isBored())
            {
                mood = "厌烦";
            }
            else if (npc.isCurious())
            {
                mood = "好奇";
            }
            else if (npc.isHappy())
            {
                mood = "开心";
            }
            else if (npc.isHungry())
            {
                mood = "着急";
            }
            else if (npc.isInhibited())
            {
                mood = "羞怯";
            }
            else if (npc.isPuzzled())
            {
                mood = "迷惑";
            }
            else if (npc.isViolent())
            {
                mood = "暴躁";
            }

            String p = String.valueOf((int)(npc.getPleasure() * 100.0F) + "%");
            String a = String.valueOf((int)(npc.getArousal() * 100.0F) + "%");
            String d = String.valueOf((int)(npc.getDominance() * 100.0F) + "%");
            String PAD = "愉悦度: " + p + " 兴奋度: " + a + " 控制度: " + d;
            this.func_96449_a(living, par2, par4, par6, mood, par9, 1.0D);
            this.func_96449_a(living, par2, par4 + 0.30000001192092896D, par6, PAD, par9, 1.0D);
        }
    }

    protected ResourceLocation func_110775_a(Entity par1Entity)
    {
        return TEXTURE;
    }
}
