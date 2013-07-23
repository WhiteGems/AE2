package net.aetherteam.aether.entities.ai;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class AIEntityControlledByPlayerPhyg extends EntityAIBase
{
    private final EntityLiving field_82640_a;
    private final float field_82638_b;
    private float field_82639_c = 0.0F;
    private boolean field_82636_d = false;
    private int field_82637_e = 0;
    private int field_82635_f = 0;

    public AIEntityControlledByPlayerPhyg(EntityLiving par1EntityLiving, float par2)
    {
        this.field_82640_a = par1EntityLiving;
        this.field_82638_b = par2;
        setMutexBits(1);
    }

    public void startExecuting()
    {
        this.field_82639_c = 0.0F;
    }

    public void resetTask()
    {
        this.field_82636_d = false;
        this.field_82639_c = 0.0F;
    }

    public boolean func_82634_f()
    {
        return this.field_82636_d;
    }

    public void func_82632_g()
    {
        this.field_82636_d = true;
        this.field_82637_e = 0;
        this.field_82635_f = (this.field_82640_a.getRNG().nextInt(841) + 140);
    }

    public boolean func_82633_h()
    {
        return (!func_82634_f()) && (this.field_82639_c > this.field_82638_b * 0.3F);
    }

    public boolean shouldExecute()
    {
        return (this.field_82640_a.isEntityAlive()) && (this.field_82640_a.riddenByEntity != null) && ((this.field_82640_a.riddenByEntity instanceof EntityPlayer)) && ((this.field_82636_d) || (this.field_82640_a.canBeSteered()));
    }

    public void updateTask()
    {
        EntityPlayer var1 = (EntityPlayer)this.field_82640_a.riddenByEntity;
        EntityCreature var2 = (EntityCreature)this.field_82640_a;
        float var3 = MathHelper.wrapAngleTo180_float(var1.rotationYaw - this.field_82640_a.rotationYaw) * 0.5F;

        if (var3 > 5.0F)
        {
            var3 = 5.0F;
        }

        if (var3 < -5.0F)
        {
            var3 = -5.0F;
        }

        setPositionPlusAngle(var3);

        if (this.field_82639_c < this.field_82638_b)
        {
            this.field_82639_c += (this.field_82638_b - this.field_82639_c) * 0.01F;
        }

        if (this.field_82639_c > this.field_82638_b)
        {
            this.field_82639_c = this.field_82638_b;
        }

        int var4 = MathHelper.floor_double(this.field_82640_a.posX);
        int var5 = MathHelper.floor_double(this.field_82640_a.posY);
        int var6 = MathHelper.floor_double(this.field_82640_a.posZ);
        float var7 = this.field_82639_c;

        if (this.field_82636_d)
        {
            if (this.field_82637_e++ > this.field_82635_f)
            {
                this.field_82636_d = false;
            }

            var7 += var7 * 1.15F * MathHelper.sin(this.field_82637_e / this.field_82635_f * (float)Math.PI);
        }

        float var8 = 0.91F;

        if (this.field_82640_a.onGround)
        {
            var8 = 0.5460001F;
            int var9 = this.field_82640_a.worldObj.getBlockId(MathHelper.floor_float(var4), MathHelper.floor_float(var5) - 1, MathHelper.floor_float(var6));

            if (var9 > 0)
            {
                var8 = Block.blocksList[var9].slipperiness * 0.91F;
            }
        }

        float var21 = 0.1627714F / (var8 * var8 * var8);
        float var10 = MathHelper.sin(var2.rotationYaw * (float)Math.PI / 180.0F);
        float var11 = MathHelper.cos(var2.rotationYaw * (float)Math.PI / 180.0F);
        float var12 = var2.getAIMoveSpeed() * var21;
        float var13 = Math.max(var7, 1.0F);
        var13 = var12 / var13;
        float var14 = var7 * var13;
        float var15 = -(var14 * var10);
        float var16 = var14 * var11;

        if (MathHelper.abs(var15) > MathHelper.abs(var16))
        {
            if (var15 < 0.0F)
            {
                var15 -= this.field_82640_a.width / 2.0F;
            }

            if (var15 > 0.0F)
            {
                var15 += this.field_82640_a.width / 2.0F;
            }

            var16 = 0.0F;
        }
        else
        {
            var15 = 0.0F;

            if (var16 < 0.0F)
            {
                var16 -= this.field_82640_a.width / 2.0F;
            }

            if (var16 > 0.0F)
            {
                var16 += this.field_82640_a.width / 2.0F;
            }
        }

        int var17 = MathHelper.floor_double(this.field_82640_a.posX + var15);
        int var18 = MathHelper.floor_double(this.field_82640_a.posZ + var16);
        PathPoint var19 = new PathPoint(MathHelper.floor_float(this.field_82640_a.width + 1.0F), MathHelper.floor_float(this.field_82640_a.height + var1.height + 1.0F), MathHelper.floor_float(this.field_82640_a.width + 1.0F));

        if (((var4 != var17) || (var6 != var18)) && (PathFinder.func_82565_a(this.field_82640_a, var17, var5, var18, var19, false, false, true) == 0) && (PathFinder.func_82565_a(this.field_82640_a, var4, var5 + 1, var6, var19, false, false, true) == 1) && (PathFinder.func_82565_a(this.field_82640_a, var17, var5 + 1, var18, var19, false, false, true) == 1))
        {
            var2.getJumpHelper().setJumping();
        }

        boolean jpress = false;
        int jrem = 0;

        if ((this.field_82640_a.onGround) && (var1.isJumping))
        {
            this.field_82640_a.onGround = false;
            this.field_82640_a.motionY = 1.4D;
            jpress = true;
            jrem--;
        }
        else if ((this.field_82640_a.handleWaterMovement()) && (var1.isJumping))
        {
            this.field_82640_a.motionY = 0.5D;
            jpress = true;
            jrem--;
        }
        else if ((jrem > 0) && (!jpress) && (var1.isJumping))
        {
            this.field_82640_a.motionY = 1.2D;
            jpress = true;
            jrem--;
        }

        this.field_82640_a.moveEntityWithHeading(0.0F, var7);
    }

    public void setPositionPlusAngle(float var1)
    {
        this.field_82640_a.rotationYaw = MathHelper.wrapAngleTo180_float(this.field_82640_a.rotationYaw + var1);
    }
}

