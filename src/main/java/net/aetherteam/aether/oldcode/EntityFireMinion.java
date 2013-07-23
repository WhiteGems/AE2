package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityFireMinion extends EntityMob
{
    public EntityFireMinion(World world)
    {
        super(world);
        this.texture = "/aether/mobs/firemonster.png";
        this.health = getMaxHealth();
        this.isImmuneToFire = true;
    }

    public int getMaxHealth()
    {
        return 40;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.health > 0)
        {
            for (int j = 0; j < 4; j++)
            {
                double a = this.rand.nextFloat() - 0.5F;
                double b = this.rand.nextFloat();
                double c = this.rand.nextFloat() - 0.5F;
                double d = this.posX + a * b;
                double e = this.boundingBox.minY + b - 0.5D;
                double f = this.posZ + c * b;
                this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
            }
        }
    }
}

