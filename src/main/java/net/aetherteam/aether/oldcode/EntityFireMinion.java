package net.aetherteam.aether.oldcode;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityFireMinion extends EntityMob
{
    public EntityFireMinion(World var1)
    {
        super(var1);
        this.texture = "/aether/mobs/firemonster.png";
        this.health = this.getMaxHealth();
        this.isImmuneToFire = true;
    }

    public int getMaxHealth()
    {
        return 40;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.health > 0)
        {
            for (int var1 = 0; var1 < 4; ++var1)
            {
                double var2 = (double) (this.rand.nextFloat() - 0.5F);
                double var4 = (double) this.rand.nextFloat();
                double var6 = (double) (this.rand.nextFloat() - 0.5F);
                double var8 = this.posX + var2 * var4;
                double var10 = this.boundingBox.minY + var4 - 0.5D;
                double var12 = this.posZ + var6 * var4;
                this.worldObj.spawnParticle("flame", var8, var10, var12, 0.0D, -0.07500000298023224D, 0.0D);
            }
        }
    }
}
