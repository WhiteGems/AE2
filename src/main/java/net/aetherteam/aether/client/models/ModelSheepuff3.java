package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntitySheepuff;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.src.bdi;

public class ModelSheepuff3 extends TexturedQuad
{
    private float field_78152_i;

    public ModelSheepuff3()
    {
        super(12, 0.0F);
        this.a = new bdi(this, 0, 0);
        this.a.a(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F);
        this.a.a(0.0F, 6.0F, -8.0F);
        this.b = new bdi(this, 28, 8);
        this.b.a(-4.0F, -8.0F, -7.0F, 8, 16, 6, 3.75F);
        this.b.a(0.0F, 5.0F, 2.0F);
        float f = 0.5F;
        this.c = new bdi(this, 0, 16);
        this.c.a(-2.0F, 0.0F, -2.0F, 4, 6, 4, f);
        this.c.a(-3.0F, 12.0F, 7.0F);
        this.d = new bdi(this, 0, 16);
        this.d.a(-2.0F, 0.0F, -2.0F, 4, 6, 4, f);
        this.d.a(3.0F, 12.0F, 7.0F);
        this.e = new bdi(this, 0, 16);
        this.e.a(-2.0F, 0.0F, -2.0F, 4, 6, 4, f);
        this.e.a(-3.0F, 12.0F, -5.0F);
        this.f = new bdi(this, 0, 16);
        this.f.a(-2.0F, 0.0F, -2.0F, 4, 6, 4, f);
        this.f.a(3.0F, 12.0F, -5.0F);
    }

    public void a(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        super.a(par1EntityLiving, par2, par3, par4);
        this.a.d = (6.0F + ((EntitySheepuff)par1EntityLiving).func_70894_j(par4) * 9.0F);
        this.field_78152_i = ((EntitySheepuff)par1EntityLiving).func_70890_k(par4);
    }

    public void a(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.a(par1, par2, par3, par4, par5, par6, par7Entity);
        this.a.f = this.field_78152_i;
    }
}

