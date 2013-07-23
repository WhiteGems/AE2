package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.interfaces.IAetherMob;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCockatrice extends EntityAetherMob
    implements IAetherMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public EntityLiving target;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public int timeUntilNextEgg;
    public int jumps;
    public int jrem;
    public boolean jpress;
    public boolean gotrider;

    public EntityCockatrice(World world)
    {
        super(world);
        this.destPos = 0.0F;
        this.field_755_h = 1.0F;
        this.stepHeight = 1.0F;
        this.jrem = 0;
        this.jumps = 3;
        this.texture = (this.dir + "/mobs/cockatrice/cockatrice.png");
        setSize(1.0F, 2.0F);
        this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
    }

    public int getMaxHealth()
    {
        return 10;
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(par1PotionEffect);
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.rand.nextInt(25) == 0) && (getBlockPathWeight(i, j, k) >= 0.0F) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox)) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID) && (this.worldObj.difficultySetting > 0) && (!this.worldObj.v());
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.ignoreFrustumCheck = (this.riddenByEntity == Aether.proxy.getClientPlayer());

        if (!this.worldObj.isRemote)
        {
            if (this.gotrider)
            {
                if (this.riddenByEntity != null)
                {
                    return;
                }

                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
                int i = 0;

                if (i < list.size())
                {
                    Entity entity = (Entity)list.get(i);
                    entity.mountEntity(this);
                }

                this.gotrider = false;
            }
        }

        if ((!this.worldObj.isRemote) && (this.worldObj.difficultySetting == 0))
        {
            setDead();
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if ((entity instanceof EntityLiving))
        {
            this.target = ((EntityLiving)entity);

            if (f < 10.0F)
            {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;

                if (this.target != null)
                {
                    if ((this.target.isDead) || (this.target.getDistanceToEntity(this) > 12.0D))
                    {
                        this.target = null;
                        this.attackTime = 0;
                    }

                    if ((this.attackTime >= 20) && (canEntityBeSeen(this.target)))
                    {
                        shootTarget();
                        this.attackTime = -10;
                    }

                    if (this.attackTime < 20)
                    {
                        this.attackTime += 2;
                    }
                }

                this.rotationYaw = ((float)(Math.atan2(d1, d) * 180.0D / Math.PI) - 90.0F);
                this.hasAttacked = true;
            }
        }
    }

    public void shootTarget()
    {
        if (this.worldObj.difficultySetting == 0)
        {
            return;
        }

        double d1 = this.target.posX - this.posX;
        double d2 = this.target.posZ - this.posZ;
        double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
        double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
        d1 *= d3;
        d2 *= d3;
        EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
        this.posY += 0.5D;
        this.worldObj.playSoundAtEntity(this, "aemisc.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));

        if (!this.worldObj.isRemote)
        {
            this.worldObj.spawnEntityInWorld(entityarrow);
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.field_756_e = this.field_752_b;
        this.field_757_d = this.destPos;
        this.destPos = ((float)(this.destPos + (this.onGround ? -1 : 4) * 0.05D));

        if (this.destPos < 0.01F)
        {
            this.destPos = 0.01F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (this.onGround)
        {
            this.destPos = 0.0F;
            this.jpress = false;
            this.jrem = this.jumps;
        }

        if ((!this.onGround) && (this.field_755_h < 1.0F))
        {
            this.field_755_h = 1.0F;
        }

        this.field_755_h = ((float)(this.field_755_h * 0.9D));

        if ((!this.onGround) && (this.motionY < 0.0D))
        {
            if (this.riddenByEntity == null)
            {
                this.motionY *= 0.6D;
            }
            else
            {
                this.motionY *= 0.6375D;
            }
        }

        this.field_752_b += this.field_755_h * 2.0F;

        if ((!this.worldObj.isRemote) && (--this.timeUntilNextEgg <= 0))
        {
            this.timeUntilNextEgg = (this.rand.nextInt(6000) + 6000);
        }
    }

    protected void fall(float f)
    {
    }

    public boolean attackEntityFrom(DamageSource src, int i)
    {
        Entity entity = src.getEntity();

        if ((entity != null) && (this.riddenByEntity != null) && (entity == this.riddenByEntity))
        {
            return false;
        }

        boolean flag = super.attackEntityFrom(src, i);

        if ((flag) && (this.riddenByEntity != null) && ((this.health <= 0) || (this.rand.nextInt(3) == 0)))
        {
            this.riddenByEntity.mountEntity(this);
        }

        return flag;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Jumps", (short)this.jumps);
        nbttagcompound.setShort("Remaining", (short)this.jrem);

        if (this.riddenByEntity != null)
        {
            this.gotrider = true;
        }

        nbttagcompound.setBoolean("GotRider", this.gotrider);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.jumps = nbttagcompound.getShort("Jumps");
        this.jrem = nbttagcompound.getShort("Remaining");
        this.gotrider = nbttagcompound.getBoolean("GotRider");
    }

    protected String getLivingSound()
    {
        return "aemob.moa.say";
    }

    protected String getHurtSound()
    {
        return "aemob.moa.say";
    }

    protected String getDeathSound()
    {
        return "aemob.moa.say";
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        dropItem(Item.feather.itemID, 3);
    }
}

