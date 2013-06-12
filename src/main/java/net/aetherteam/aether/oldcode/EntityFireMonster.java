package net.aetherteam.aether.oldcode;

import java.util.List;

import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireMonster extends EntityFlying implements IAetherBoss
{
    public int wideness;
    public int orgX;
    public int orgY;
    public int orgZ;
    public int motionTimer;
    public int entCount;
    public int flameCount;
    public int ballCount;
    public int chatLog;
    public int chatCount;
    public int hurtness;
    public int direction;
    public double rotary;
    public double speedness;
    public Entity target;
    public boolean gotTarget;
    public String bossName;
    public static final float jimz = (180F / (float) Math.PI);

    public EntityFireMonster(World var1)
    {
        super(var1);
        this.texture = "/aether/mobs/firemonster.png";
        this.setSize(2.25F, 2.5F);
        this.noClip = true;
        this.orgX = MathHelper.floor_double(this.posX);
        this.orgY = MathHelper.floor_double(this.boundingBox.minY) + 1;
        this.orgZ = MathHelper.floor_double(this.posZ);
        this.wideness = 10;
        this.health = this.getMaxHealth();
        this.speedness = 0.5D - (double) this.health / 70.0D * 0.2D;
        this.direction = 0;
        this.entCount = this.rand.nextInt(6);
        this.bossName = AetherNameGen.gen();
    }

    public int getMaxHealth()
    {
        return 50;
    }

    public EntityFireMonster(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        super(var1);
        this.texture = "/aether/mobs/firemonster.png";
        this.setSize(2.25F, 2.5F);
        this.setPosition((double) var2 + 0.5D, (double) var3, (double) var4 + 0.5D);
        this.wideness = var5 - 2;
        this.orgX = var2;
        this.orgY = var3;
        this.orgZ = var4;
        this.noClip = true;
        this.rotary = (double) this.rand.nextFloat() * 360.0D;
        this.health = 50;
        this.speedness = 0.5D - (double) this.health / 70.0D * 0.2D;
        this.direction = var6;
        this.entCount = this.rand.nextInt(6);
        this.bossName = AetherNameGen.gen();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.health > 0)
        {
            double var1 = (double) (this.rand.nextFloat() - 0.5F);
            double var3 = (double) this.rand.nextFloat();
            double var5 = (double) (this.rand.nextFloat() - 0.5F);
            double var7 = this.posX + var1 * var3;
            double var9 = this.boundingBox.minY + var3 - 0.5D;
            double var11 = this.posZ + var5 * var3;
            this.worldObj.spawnParticle("flame", var7, var9, var11, 0.0D, -0.07500000298023224D, 0.0D);
            ++this.entCount;

            if (this.entCount >= 3)
            {
                this.burnEntities();
                this.evapWater();
                this.entCount = 0;
            }

            if (this.hurtness > 0)
            {
                --this.hurtness;

                if (this.hurtness == 0)
                {
                    this.texture = "/aether/mobs/firemonster.png";
                }
            }
        }

        if (this.chatCount > 0)
        {
            --this.chatCount;
        }
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (this.gotTarget && this.target == null)
        {
            this.target = this.findPlayerToAttack();
            this.gotTarget = false;
        }

        if (this.target == null)
        {
            this.setPosition((double) this.orgX + 0.5D, (double) this.orgY, (double) this.orgZ + 0.5D);
            this.setDoor(0);
        } else
        {
            this.renderYawOffset = this.rotationYaw;
            this.setPosition(this.posX, (double) this.orgY, this.posZ);
            this.motionY = 0.0D;
            boolean var1 = false;

            if (this.motionX > 0.0D && (int) Math.floor(this.posX) > this.orgX + this.wideness)
            {
                this.rotary = 360.0D - this.rotary;
                var1 = true;
            } else if (this.motionX < 0.0D && (int) Math.floor(this.posX) < this.orgX - this.wideness)
            {
                this.rotary = 360.0D - this.rotary;
                var1 = true;
            }

            if (this.motionZ > 0.0D && (int) Math.floor(this.posZ) > this.orgZ + this.wideness)
            {
                this.rotary = 180.0D - this.rotary;
                var1 = true;
            } else if (this.motionZ < 0.0D && (int) Math.floor(this.posZ) < this.orgZ - this.wideness)
            {
                this.rotary = 180.0D - this.rotary;
                var1 = true;
            }

            if (this.rotary > 360.0D)
            {
                this.rotary -= 360.0D;
            } else if (this.rotary < 0.0D)
            {
                this.rotary += 360.0D;
            }

            if (this.target != null)
            {
                this.faceEntity(this.target, 20.0F, 20.0F);
            }

            double var2 = this.rotary / (180D / Math.PI);
            this.motionX = Math.sin(var2) * this.speedness;
            this.motionZ = Math.cos(var2) * this.speedness;
            ++this.motionTimer;

            if (this.motionTimer >= 20 || var1)
            {
                this.motionTimer = 0;

                if (this.rand.nextInt(3) == 0)
                {
                    this.rotary += (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0D;
                }
            }

            ++this.flameCount;

            if (this.flameCount == 40 && this.rand.nextInt(2) == 0)
            {
                this.poopFire();
            } else if (this.flameCount >= 55 + this.health / 2 && this.target != null && this.target instanceof EntityLiving)
            {
                this.makeFireBall(1);
                this.flameCount = 0;
            }

            if (this.target != null && this.target.isDead)
            {
                this.setPosition((double) this.orgX + 0.5D, (double) this.orgY, (double) this.orgZ + 0.5D);
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                this.target = null;
                this.chatLine("\u00a7c这就是反对太阳神的命运");
                this.setDoor(0);
                this.gotTarget = false;
            }
        }
    }

    public void burnEntities()
    {
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 4.0D, 0.0D));

        for (int var2 = 0; var2 < var1.size(); ++var2)
        {
            Entity var3 = (Entity) var1.get(var2);

            if (var3 instanceof EntityLiving && !var3.isImmuneToFire())
            {
                var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
                var3.setFire(15);
            }
        }
    }

    public void evapWater()
    {
        int var1 = MathHelper.floor_double(this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D);
        int var2 = MathHelper.floor_double(this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D);
        byte var3 = 10;

        for (int var4 = var1 - var3; var4 <= var1 + var3; ++var4)
        {
            for (int var5 = var2 - var3; var5 <= var2 + var3; ++var5)
            {
                for (int var6 = 0; var6 < 8; ++var6)
                {
                    int var7 = this.orgY - 2 + var6;

                    if (this.worldObj.getBlockMaterial(var4, var7, var5) == Material.water)
                    {
                        this.worldObj.setBlock(var4, var7, var5, 0);
                        this.worldObj.playSoundEffect((double) ((float) var4 + 0.5F), (double) ((float) var7 + 0.5F), (double) ((float) var5 + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);

                        for (int var8 = 0; var8 < 8; ++var8)
                        {
                            this.worldObj.spawnParticle("largesmoke", (double) var4 + Math.random(), (double) var7 + 0.75D, (double) var5 + Math.random(), 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        }
    }

    public void makeFireBall(int var1)
    {
        this.worldObj.playSoundAtEntity(this, "mob.ghast.fireball", 5.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        boolean var2 = false;
        ++this.ballCount;

        if (this.ballCount >= 3 + this.rand.nextInt(3))
        {
            var2 = true;
            this.ballCount = 0;
        }

        for (int var3 = 0; var3 < var1; ++var3)
        {
            EntityFiroBall var4 = new EntityFiroBall(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, var2);
            this.worldObj.spawnEntityInWorld(var4);
        }
    }

    public void poopFire()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.posZ);
        int var3 = this.orgY - 2;

        if (AetherBlocks.isGood(this.worldObj.getBlockId(var1, var3, var2), this.worldObj.getBlockMetadata(var1, var3, var2)))
        {
            this.worldObj.setBlock(var1, var3, var2, Block.fire.blockID);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("OriginX", (short) this.orgX);
        var1.setShort("OriginY", (short) this.orgY);
        var1.setShort("OriginZ", (short) this.orgZ);
        var1.setShort("Wideness", (short) this.wideness);
        var1.setShort("FlameCount", (short) this.flameCount);
        var1.setShort("BallCount", (short) this.ballCount);
        var1.setShort("ChatLog", (short) this.chatLog);
        var1.setShort("Direction", (short) this.direction);
        var1.setFloat("Rotary", (float) this.rotary);
        this.gotTarget = this.target != null;
        var1.setBoolean("GotTarget", this.gotTarget);
        var1.setString("BossName", this.bossName);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.orgX = var1.getShort("OriginX");
        this.orgY = var1.getShort("OriginY");
        this.orgZ = var1.getShort("OriginZ");
        this.wideness = var1.getShort("Wideness");
        this.flameCount = var1.getShort("FlameCount");
        this.ballCount = var1.getShort("BallCount");
        this.chatLog = var1.getShort("ChatLog");
        this.direction = var1.getShort("Direction");
        this.rotary = (double) var1.getFloat("Rotary");
        this.gotTarget = var1.getBoolean("GotTarget");
        this.speedness = 0.5D - (double) this.health / 70.0D * 0.2D;
        this.bossName = var1.getString("BossName");
    }

    public void chatLine(String var1) {}

    public boolean chatWithMe()
    {
        if (this.chatCount <= 0)
        {
            if (this.chatLog == 0)
            {
                this.chatLine("\u00a7c进入到此房间说明你一定拥有一个勇敢的灵魂");
                this.chatLog = 1;
                this.chatCount = 100;
            } else if (this.chatLog == 1)
            {
                this.chatLine("\u00a7c离开! 人类! 这里不需要你");
                this.chatLog = 2;
                this.chatCount = 100;
            } else if (this.chatLog == 2)
            {
                this.chatLine("\u00a7c你的存在让我很生气 你难道不害怕我燃烧的怒火?");
                this.chatLog = 3;
                this.chatCount = 100;
            } else if (this.chatLog == 3)
            {
                this.chatLine("\u00a7c我有什么可以给你的捏, 傻瓜! 让我静一静");
                this.chatLog = 4;
                this.chatCount = 100;
            } else if (this.chatLog == 4)
            {
                this.chatLine("\u00a7c你或许太无知了, 你不知道我是谁吗?");
                this.chatLog = 5;
                this.chatCount = 100;
            } else if (this.chatLog == 5)
            {
                this.chatLine("\u00a7c我是太阳神, 代表着Aether永恒的太阳");
                this.chatLine("\u00a7c只要我还活着, 这个世界的太阳永远不会落下");
                this.chatLog = 6;
                this.chatCount = 100;
            } else if (this.chatLog == 6)
            {
                this.chatLine("\u00a7c我的身体里燃烧着一千头愤怒的野兽");
                this.chatLine("\u00a7c没有任何人, 任何英雄, 或者恶魂能伤害我, 你也不例外");
                this.chatLog = 7;
                this.chatCount = 100;
            } else if (this.chatLog == 7)
            {
                this.chatLine("\u00a7c你想挑战太阳的威力? 你会抓狂的");
                this.chatLine("\u00a7c不要试图攻击我, 你将感受到我的愤怒");
                this.chatLog = 8;
                this.chatCount = 100;
            } else if (this.chatLog == 8)
            {
                this.chatLine("\u00a7c这是最后一次警告! 赶紧离开, 否则准备燃烧吧");
                this.chatLog = 9;
                this.chatCount = 100;
            } else
            {
                if (this.chatLog == 9)
                {
                    this.chatLine("\u00a76如你所愿, 你将缓慢而痛苦的死亡");
                    this.chatLog = 10;
                    return true;
                }

                if (this.chatLog == 10 && this.target == null)
                {
                    this.chatLine("\u00a7c你上次死亡难道没有满足你的好奇心, 人类?");
                    this.chatLog = 9;
                    this.chatCount = 100;
                }
            }
        }

        return false;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (this.chatWithMe())
        {
            this.rotary = (180D / Math.PI) * Math.atan2(this.posX - var1.posX, this.posZ - var1.posZ);
            this.target = var1;
            this.setDoor(AetherBlocks.LockedDungeonStone.blockID);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double var1, double var3, double var5)
    {}

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {}

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() != null && var1.getEntity() instanceof EntityFiroBall)
        {
            this.speedness = 0.5D - (double) this.health / 70.0D * 0.2D;
            boolean var3 = super.attackEntityFrom(var1, var2);

            if (var3)
            {
                this.hurtness = 15;
                this.texture = "/aether/mobs/firemonsterHurt.png";
                EntityFireMinion var4 = new EntityFireMinion(this.worldObj);
                var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(var4);
                this.worldObj.spawnEntityInWorld(var4);
                this.worldObj.spawnEntityInWorld(var4);

                if (this.health <= 0)
                {
                    this.chatLine("\u00a7b这种苦涩的寒冷... 这感觉是... 痛?");
                    this.setDoor(0);
                    this.unlockTreasure();
                }
            }

            return var3;
        } else
        {
            return false;
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        this.entityDropItem(new ItemStack(AetherItems.Key, 1, 2), 0.0F);
    }

    private void setDoor(int var1)
    {
        int var3;
        int var2;

        if (this.direction / 2 == 0)
        {
            for (var2 = this.orgY - 1; var2 < this.orgY + 2; ++var2)
            {
                for (var3 = this.orgZ - 1; var3 < this.orgZ + 2; ++var3)
                {
                    this.worldObj.setBlock(this.orgX + (this.direction == 0 ? -11 : 11), var2, var3, var1, 2, 4);
                }
            }
        } else
        {
            for (var2 = this.orgY - 1; var2 < this.orgY + 2; ++var2)
            {
                for (var3 = this.orgX - 1; var3 < this.orgX + 2; ++var3)
                {
                    this.worldObj.setBlock(var3, var2, this.orgZ + (this.direction == 3 ? 11 : -11), var1, 2, 4);
                }
            }
        }
    }

    private void unlockTreasure()
    {
        int var1;
        int var2;

        if (this.direction / 2 == 0)
        {
            for (var1 = this.orgY - 1; var1 < this.orgY + 2; ++var1)
            {
                for (var2 = this.orgZ - 1; var2 < this.orgZ + 2; ++var2)
                {
                    this.worldObj.setBlock(this.orgX + (this.direction == 0 ? 11 : -11), var1, var2, 0);
                }
            }
        } else
        {
            for (var1 = this.orgY - 1; var1 < this.orgY + 2; ++var1)
            {
                for (var2 = this.orgX - 1; var2 < this.orgX + 2; ++var2)
                {
                    this.worldObj.setBlock(var2, var1, this.orgZ + (this.direction == 3 ? -11 : 11), 0);
                }
            }
        }

        for (var1 = this.orgX - 20; var1 < this.orgX + 20; ++var1)
        {
            for (var2 = this.orgY - 3; var2 < this.orgY + 6; ++var2)
            {
                for (int var3 = this.orgZ - 20; var3 < this.orgZ + 20; ++var3)
                {
                    int var4 = this.worldObj.getBlockId(var1, var2, var3);

                    if (var4 == AetherBlocks.LockedDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(var1, var2, var3, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(var1, var2, var3), 4);
                    }

                    if (var4 == AetherBlocks.LockedLightDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(var1, var2, var3, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(var1, var2, var3), 4);
                    }
                }
            }
        }
    }

    public int getBossHP()
    {
        return this.health;
    }

    public int getBossMaxHP()
    {
        return 50;
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return "太阳神:" + this.bossName;
    }

    public Entity getBossEntity()
    {
        return this;
    }

    public int getBossStage()
    {
        return 0;
    }

    public EnumBossType getBossType()
    {
        return EnumBossType.BOSS;
    }
}
