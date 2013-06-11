package net.aetherteam.aether.entities;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityDartEnchanted extends EntityDartGolden
{
    public EntityLiving victim;
    public static int texfxindex = 94;

    public EntityDartEnchanted(World var1)
    {
        super(var1);
    }

    public EntityDartEnchanted(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
    }

    public EntityDartEnchanted(World var1, EntityLiving var2)
    {
        super(var1, var2);
    }

    public void entityInit()
    {
        super.entityInit();
        this.item = new ItemStack(AetherItems.Dart, 1, 2);
        this.dmg = 6;
    }
}
