package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
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
    private static final float sponge = (180F / (float)Math.PI);

    public EntityFiroBall(World world)
    {
        super(world);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        setSize(0.9F, 0.9F);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.rand.nextFloat() * 6.0F);
        }
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public EntityFiroBall(World world, double x, double y, double z, boolean flag)
    {
        super(world);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        setSize(0.9F, 0.9F);
        setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.rand.nextFloat() * 6.0F);
        }

        this.smotionX = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionY = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionZ = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));

        if (flag)
        {
            this.frosty = true;
            this.texture = "/aether/mobs/iceyball.png";
            this.smotionX /= 3.0D;
            this.smotionY = 0.0D;
            this.smotionZ /= 3.0D;
        }
    }

    public EntityFiroBall(World world, double x, double y, double z, boolean flag, boolean flag2)
    {
        super(world);
        this.texture = "/aether/mobs/firoball.png";
        this.lifeSpan = 300;
        this.life = this.lifeSpan;
        setSize(0.9F, 0.9F);
        setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);
        this.sinage = new float[3];
        this.isImmuneToFire = true;

        for (int i = 0; i < 3; i++)
        {
            this.sinage[i] = (this.rand.nextFloat() * 6.0F);
        }

        this.smotionX = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionY = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));
        this.smotionZ = ((0.2D + this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D));

        if (flag)
        {
            this.frosty = true;
            this.texture = "/aether/mobs/iceyball.png";
            this.smotionX /= 3.0D;
            this.smotionY = 0.0D;
            this.smotionZ /= 3.0D;
        }

        this.fromCloud = flag2;
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.life -= 1;

        if (this.life <= 0)
        {
            fizzle();
            this.isDead = true;
        }
    }

    public void fizzle()
    {
        if (this.frosty)
        {
            this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }
        else
        {
            this.worldObj.playSoundAtEntity(this, "random.fizz", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }

        for (int j = 0; j < 16; j++)
        {
            double a = this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
            double b = this.posY + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
            double c = this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;

            if (!this.frosty)
            {
                this.worldObj.spawnParticle("largesmoke", a, b, c, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void splode()
    {
        if (this.frosty)
        {
            this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }
        else
        {
            this.worldObj.playSoundAtEntity(this, "random.explode", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
        }

        for (int j = 0; j < 40; j++)
        {
            double a = (this.rand.nextFloat() - 0.5F) * 0.5F;
            double b = (this.rand.nextFloat() - 0.5F) * 0.5F;
            double c = (this.rand.nextFloat() - 0.5F) * 0.5F;

            if (this.frosty)
            {
                a *= 0.5D;
                b *= 0.5D;
                c *= 0.5D;
                this.worldObj.spawnParticle("snowshovel", this.posX, this.posY, this.posZ, a, b + 0.125D, c);
            }
            else
            {
                this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, a, b, c);
            }
        }
    }

    public void updateAnims()
    {
        if (!this.frosty)
            for (int i = 0; i < 3; i++)
            {
                this.sinage[i] += 0.3F + i * 0.13F;

                if (this.sinage[i] > ((float)Math.PI * 2F))
                {
                    this.sinage[i] -= ((float)Math.PI * 2F);
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
            if ((this.frosty) && (this.smacked))
            {
                splode();
                fizzle();
                this.isDead = true;
            }
            else
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.boundingBox.minY);
                int k = MathHelper.floor_double(this.posZ);

                if ((this.smotionX > 0.0D) && (this.worldObj.getBlockId(i + 1, j, k) != 0))
                {
                    this.motionX = (this.smotionX = -this.smotionX);
                }
                else if ((this.smotionX < 0.0D) && (this.worldObj.getBlockId(i - 1, j, k) != 0))
                {
                    this.motionX = (this.smotionX = -this.smotionX);
                }

                if ((this.smotionY > 0.0D) && (this.worldObj.getBlockId(i, j + 1, k) != 0))
                {
                    this.motionY = (this.smotionY = -this.smotionY);
                }
                else if ((this.smotionY < 0.0D) && (this.worldObj.getBlockId(i, j - 1, k) != 0))
                {
                    this.motionY = (this.smotionY = -this.smotionY);
                }

                if ((this.smotionZ > 0.0D) && (this.worldObj.getBlockId(i, j, k + 1) != 0))
                {
                    this.motionZ = (this.smotionZ = -this.smotionZ);
                }
                else if ((this.smotionZ < 0.0D) && (this.worldObj.getBlockId(i, j, k - 1) != 0))
                {
                    this.motionZ = (this.smotionZ = -this.smotionZ);
                }
            }
        }

        updateAnims();
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("LifeLeft", (short)this.life);
        nbttagcompound.setTag("SeriousKingVampire", newDoubleNBTList(new double[] { this.smotionX, this.smotionY, this.smotionZ }));
        nbttagcompound.setBoolean("Frosty", this.frosty);
        nbttagcompound.setBoolean("FromCloud", this.fromCloud);
        nbttagcompound.setBoolean("Smacked", this.smacked);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.life = nbttagcompound.getShort("LifeLeft");
        this.frosty = nbttagcompound.getBoolean("Frosty");
        this.fromCloud = nbttagcompound.getBoolean("FromCloud");

        if (this.frosty)
        {
            this.texture = "/aether/mobs/iceyball.png";
        }

        this.smacked = nbttagcompound.getBoolean("Smacked");
        NBTTagList nbttaglist = nbttagcompound.getTagList("SeriousKingVampire");
        this.smotionX = ((float)((NBTTagDouble)nbttaglist.tagAt(0)).data);
        this.smotionY = ((float)((NBTTagDouble)nbttaglist.tagAt(1)).data);
        this.smotionZ = ((float)((NBTTagDouble)nbttaglist.tagAt(2)).data);
    }

    public void applyEntityCollision(Entity entity)
    {
        super.applyEntityCollision(entity);
        boolean flag = false;

        if ((entity != null) && ((entity instanceof EntityLiving)) && (!(entity instanceof EntityFiroBall)))
        {
            if ((this.frosty) && ((!(entity instanceof EntityFireMonster)) || ((this.smacked) && (!this.fromCloud))) && (!(entity instanceof EntityFireMinion)))
            {
                flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
            }
            else if ((!this.frosty) && (!(entity instanceof EntityFireMonster)) && (!(entity instanceof EntityFireMinion)))
            {
                flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);

                if (flag)
                {
                    setFire(100);
                }
            }
        }

        if (flag)
        {
            splode();
            fizzle();
            this.isDead = true;
        }
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        if (ds.getEntity() != null)
        {
            Vec3 vec3d = ds.getEntity().getLookVec();

            if (vec3d != null)
            {
                this.smotionX = vec3d.xCoord;
                this.smotionY = vec3d.yCoord;
                this.smotionZ = vec3d.zCoord;
            }

            this.smacked = true;
            return true;
        }

        return false;
    }
}

