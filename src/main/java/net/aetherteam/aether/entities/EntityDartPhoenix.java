package net.aetherteam.aether.entities;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDartPhoenix extends EntityDartGolden
{
    public EntityLiving victim;
    public static int texfxindex = 94;

    public EntityDartPhoenix(World var1)
    {
        super(var1);
    }

    public EntityDartPhoenix(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
    }

    public EntityDartPhoenix(World var1, EntityLiving var2, ItemStack var3)
    {
        super(var1, var2);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setFire(10);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.setFire(10);
        this.item = new ItemStack(AetherItems.Dart, 1, 0);

        for (int var1 = 0; var1 < 2; ++var1)
        {
            double var2 = this.rand.nextGaussian() * 0.02D;
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, var2, var4, var6);
        }
    }

    public boolean onHitBlock()
    {
        this.curvature = 0.03F;
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.posY);
        int var3 = MathHelper.floor_double(this.posZ);
        this.worldObj.setBlock(var1, var2, var3, 51);
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.setDead();
        return this.victim == null;
    }
}
