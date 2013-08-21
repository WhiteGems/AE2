package net.aetherteam.aether.entities;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDartPhoenix extends EntityDartGolden
{
    public static int texfxindex = 94;

    public EntityDartPhoenix(World world)
    {
        super(world);
    }

    public EntityDartPhoenix(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityDartPhoenix(World world, EntityLivingBase ent, ItemStack stack)
    {
        super(world, ent);
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

        for (int fireAmount = 0; fireAmount < 2; ++fireAmount)
        {
            double d = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, d, d1, d2);
        }
    }

    public boolean onHitBlock()
    {
        this.curvature = 0.03F;
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        this.worldObj.setBlock(x, y, z, 51);
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.setDead();
        return this.victim == null;
    }
}
