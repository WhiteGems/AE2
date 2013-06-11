package net.aetherteam.aether.oldcode;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityFiroBall extends EntityFlying
{
    public float[] sinage;
    public double smotionX;
    public double smotionY;
    public double smotionZ;
    public int life;
    public int lifeSpan;
    public boolean frosty;
    public boolean smacked;
    public boolean fromCloud;
    private static final double topSpeed = 0.125D;
    private static final float sponge = (180F / (float) Math.PI);

    public EntityFiroBall(World var1)
    {
        super(var1);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        this.setSize(0.9F, 0.9F);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int var2 = 0; var2 < 3; ++var2)
        {
            this.sinage[var2] = this.rand.nextFloat() * 6.0F;
        }
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public EntityFiroBall(World var1, double var2, double var4, double var6, boolean var8)
    {
        super(var1);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        this.setSize(0.9F, 0.9F);
        this.setPositionAndRotation(var2, var4, var6, this.rotationYaw, this.rotationPitch);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int var9 = 0; var9 < 3; ++var9)
        {
            this.sinage[var9] = this.rand.nextFloat() * 6.0F;
        }

        this.smotionX = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionY = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionZ = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);

        if (var8)
        {
            this.frosty = true;
            this.texture = "/aether/mobs/iceyball.png";
            this.smotionX /= 3.0D;
            this.smotionY = 0.0D;
            this.smotionZ /= 3.0D;
        }
    }

    public EntityFiroBall(World var1, double var2, double var4, double var6, boolean var8, boolean var9)
    {
        super(var1);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        this.setSize(0.9F, 0.9F);
        this.setPositionAndRotation(var2, var4, var6, this.rotationYaw, this.rotationPitch);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int var10 = 0; var10 < 3; ++var10)
        {
            this.sinage[var10] = this.rand.nextFloat() * 6.0F;
        }

        this.smotionX = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionY = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
        this.smotionZ = (0.2D + (double) this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);

        if (var8)
        {
            this.frosty = true;
            this.texture = "/aether/mobs/iceyball.png";
            this.smotionX /= 3.0D;
            this.smotionY = 0.0D;
            this.smotionZ /= 3.0D;
        }

        this.fromCloud = var9;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        --this.life;

        if (this.life <= 0)
        {
            this.fizzle();
            this.isDead = true;
        }
    }

    public void fizzle()
    {
        if (this.frosty)
        {
            this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        } else
        {
            this.worldObj.playSoundAtEntity(this, "random.fizz", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }

        for (int var1 = 0; var1 < 16; ++var1)
        {
            double var2 = this.posX + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
            double var4 = this.posY + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
            double var6 = this.posZ + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;

            if (!this.frosty)
            {
                this.worldObj.spawnParticle("largesmoke", var2, var4, var6, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void splode()
    {
        if (this.frosty)
        {
            this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        } else
        {
            this.worldObj.playSoundAtEntity(this, "random.explode", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }

        for (int var1 = 0; var1 < 40; ++var1)
        {
            double var2 = (double) ((this.rand.nextFloat() - 0.5F) * 0.5F);
            double var4 = (double) ((this.rand.nextFloat() - 0.5F) * 0.5F);
            double var6 = (double) ((this.rand.nextFloat() - 0.5F) * 0.5F);

            if (this.frosty)
            {
                var2 *= 0.5D;
                var4 *= 0.5D;
                var6 *= 0.5D;
                this.worldObj.spawnParticle("snowshovel", this.posX, this.posY, this.posZ, var2, var4 + 0.125D, var6);
            } else
            {
                this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, var2, var4, var6);
            }
        }
    }

    public void updateAnims()
    {
        if (!this.frosty)
        {
            for (int var1 = 0; var1 < 3; ++var1)
            {
                this.sinage[var1] += 0.3F + (float) var1 * 0.13F;

                if (this.sinage[var1] > ((float) Math.PI * 2F))
                {
                    this.sinage[var1] -= ((float) Math.PI * 2F);
                }
            }
        }
    }

    public void updateEntityActionState()
    {
        this.motionX = this.smotionX;
        this.motionY = this.smotionY;
        this.motionZ = this.smotionZ;

        if (this.isCollided)
        {
            if (this.frosty && this.smacked)
            {
                this.splode();
                this.fizzle();
                this.isDead = true;
            } else
            {
                int var1 = MathHelper.floor_double(this.posX);
                int var2 = MathHelper.floor_double(this.boundingBox.minY);
                int var3 = MathHelper.floor_double(this.posZ);

                if (this.smotionX > 0.0D && this.worldObj.getBlockId(var1 + 1, var2, var3) != 0)
                {
                    this.motionX = this.smotionX = -this.smotionX;
                } else if (this.smotionX < 0.0D && this.worldObj.getBlockId(var1 - 1, var2, var3) != 0)
                {
                    this.motionX = this.smotionX = -this.smotionX;
                }

                if (this.smotionY > 0.0D && this.worldObj.getBlockId(var1, var2 + 1, var3) != 0)
                {
                    this.motionY = this.smotionY = -this.smotionY;
                } else if (this.smotionY < 0.0D && this.worldObj.getBlockId(var1, var2 - 1, var3) != 0)
                {
                    this.motionY = this.smotionY = -this.smotionY;
                }

                if (this.smotionZ > 0.0D && this.worldObj.getBlockId(var1, var2, var3 + 1) != 0)
                {
                    this.motionZ = this.smotionZ = -this.smotionZ;
                } else if (this.smotionZ < 0.0D && this.worldObj.getBlockId(var1, var2, var3 - 1) != 0)
                {
                    this.motionZ = this.smotionZ = -this.smotionZ;
                }
            }
        }

        this.updateAnims();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("LifeLeft", (short) this.life);
        var1.setTag("SeriousKingVampire", this.newDoubleNBTList(new double[]{this.smotionX, this.smotionY, this.smotionZ}));
        var1.setBoolean("Frosty", this.frosty);
        var1.setBoolean("FromCloud", this.fromCloud);
        var1.setBoolean("Smacked", this.smacked);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.life = var1.getShort("LifeLeft");
        this.frosty = var1.getBoolean("Frosty");
        this.fromCloud = var1.getBoolean("FromCloud");

        if (this.frosty)
        {
            this.texture = "/aether/mobs/iceyball.png";
        }

        this.smacked = var1.getBoolean("Smacked");
        NBTTagList var2 = var1.getTagList("SeriousKingVampire");
        this.smotionX = (double) ((float) ((NBTTagDouble) var2.tagAt(0)).data);
        this.smotionY = (double) ((float) ((NBTTagDouble) var2.tagAt(1)).data);
        this.smotionZ = (double) ((float) ((NBTTagDouble) var2.tagAt(2)).data);
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity var1)
    {
        super.applyEntityCollision(var1);
        boolean var2 = false;

        if (var1 != null && var1 instanceof EntityLiving && !(var1 instanceof EntityFiroBall))
        {
            if (this.frosty && (!(var1 instanceof EntityFireMonster) || this.smacked && !this.fromCloud) && !(var1 instanceof EntityFireMinion))
            {
                var2 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
            } else if (!this.frosty && !(var1 instanceof EntityFireMonster) && !(var1 instanceof EntityFireMinion))
            {
                var2 = var1.attackEntityFrom(DamageSource.causeMobDamage(this), 5);

                if (var2)
                {
                    this.setFire(100);
                }
            }
        }

        if (var2)
        {
            this.splode();
            this.fizzle();
            this.isDead = true;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() != null)
        {
            Vec3 var3 = var1.getEntity().getLookVec();

            if (var3 != null)
            {
                this.smotionX = var3.xCoord;
                this.smotionY = var3.yCoord;
                this.smotionZ = var3.zCoord;
            }

            this.smacked = true;
            return true;
        } else
        {
            return false;
        }
    }
}
