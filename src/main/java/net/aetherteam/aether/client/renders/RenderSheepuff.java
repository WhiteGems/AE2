package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySheepuff;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSheepuff extends RenderMinecartMobSpawner
{
    private ModelMinecart wool;
    private ModelMinecart puffed;

    public RenderSheepuff(ModelMinecart modelbase, ModelMinecart modelbase1, ModelMinecart modelbase2, float f)
    {
        super(modelbase1, f);
        a(modelbase);
        this.wool = modelbase;
        this.puffed = modelbase2;
    }

    protected int setWoolColorAndRender(EntitySheepuff entitysheep, int i, float f)
    {
        if ((i == 0) && (!entitysheep.getSheared()))
        {
            if (entitysheep.getPuffed())
            {
                a(this.puffed);
            }
            else
            {
                a(this.wool);
            }

            loadTexture("/net/aetherteam/aether/client/sprites/mobs/sheepuff/fur.png");
            float f1 = entitysheep.getBrightness(f);
            int j = entitysheep.getFleeceColor();
            GL11.glColor3f(f1 * net.minecraft.entity.passive.EntitySheep.fleeceColorTable[j][0], f1 * net.minecraft.entity.passive.EntitySheep.fleeceColorTable[j][1], f1 * net.minecraft.entity.passive.EntitySheep.fleeceColorTable[j][2]);
            return 1;
        }

        return -1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setWoolColorAndRender((EntitySheepuff)entityliving, i, f);
    }
}

