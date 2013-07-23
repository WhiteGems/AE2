package net.aetherteam.aether.oldcode;

import java.util.List;
import java.util.Random;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireMonster extends EntityFlying
    implements IAetherBoss
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
    public static final float jimz = (180F / (float)Math.PI);

    public EntityFireMonster(World world)
    {
        super(world);
        this.texture = "/aether/mobs/firemonster.png";
        setSize(2.25F, 2.5F);
        this.noClip = true;
        this.orgX = MathHelper.floor_double(this.posX);
        this.orgY = (MathHelper.floor_double(this.boundingBox.minY) + 1);
        this.orgZ = MathHelper.floor_double(this.posZ);
        this.wideness = 10;
        this.health = getMaxHealth();
        this.speedness = (0.5D - this.health / 70.0D * 0.2D);
        this.direction = 0;
        this.entCount = this.rand.nextInt(6);
        this.bossName = AetherNameGen.gen();
    }

    public int getMaxHealth()
    {
        return 50;
    }

    public EntityFireMonster(World world, int x, int y, int z, int rad, int dir)
    {
        super(world);
        this.texture = "/aether/mobs/firemonster.png";
        setSize(2.25F, 2.5F);
        setPosition(x + 0.5D, y, z + 0.5D);
        this.wideness = (rad - 2);
        this.orgX = x;
        this.orgY = y;
        this.orgZ = z;
        this.noClip = true;
        this.rotary = (this.rand.nextFloat() * 360.0D);
        this.health = 50;
        this.speedness = (0.5D - this.health / 70.0D * 0.2D);
        this.direction = dir;
        this.entCount = this.rand.nextInt(6);
        this.bossName = AetherNameGen.gen();
    }

    public boolean canDespawn()
    {
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.health > 0)
        {
            double a = this.rand.nextFloat() - 0.5F;
            double b = this.rand.nextFloat();
            double c = this.rand.nextFloat() - 0.5F;
            double d = this.posX + a * b;
            double e = this.boundingBox.minY + b - 0.5D;
            double f = this.posZ + c * b;
            this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
            this.entCount += 1;

            if (this.entCount >= 3)
            {
                burnEntities();
                evapWater();
                this.entCount = 0;
            }

            if (this.hurtness > 0)
            {
                this.hurtness -= 1;

                if (this.hurtness == 0)
                {
                    this.texture = "/aether/mobs/firemonster.png";
                }
            }
        }

        if (this.chatCount > 0)
        {
            this.chatCount -= 1;
        }
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 32.0D);

        if ((entityplayer != null) && (canEntityBeSeen(entityplayer)))
        {
            return entityplayer;
        }

        return null;
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if ((this.gotTarget) && (this.target == null))
        {
            this.target = findPlayerToAttack();
            this.gotTarget = false;
        }

        if (this.target == null)
        {
            setPosition(this.orgX + 0.5D, this.orgY, this.orgZ + 0.5D);
            setDoor(0);
            return;
        }

        this.renderYawOffset = this.rotationYaw;
        setPosition(this.posX, this.orgY, this.posZ);
        this.motionY = 0.0D;
        boolean pool = false;

        if ((this.motionX > 0.0D) && ((int)Math.floor(this.posX) > this.orgX + this.wideness))
        {
            this.rotary = (360.0D - this.rotary);
            pool = true;
        }
        else if ((this.motionX < 0.0D) && ((int)Math.floor(this.posX) < this.orgX - this.wideness))
        {
            this.rotary = (360.0D - this.rotary);
            pool = true;
        }

        if ((this.motionZ > 0.0D) && ((int)Math.floor(this.posZ) > this.orgZ + this.wideness))
        {
            this.rotary = (180.0D - this.rotary);
            pool = true;
        }
        else if ((this.motionZ < 0.0D) && ((int)Math.floor(this.posZ) < this.orgZ - this.wideness))
        {
            this.rotary = (180.0D - this.rotary);
            pool = true;
        }

        if (this.rotary > 360.0D)
        {
            this.rotary -= 360.0D;
        }
        else if (this.rotary < 0.0D)
        {
            this.rotary += 360.0D;
        }

        if (this.target != null)
        {
            faceEntity(this.target, 20.0F, 20.0F);
        }

        double crazy = this.rotary / (180D / Math.PI);
        this.motionX = (Math.sin(crazy) * this.speedness);
        this.motionZ = (Math.cos(crazy) * this.speedness);
        this.motionTimer += 1;

        if ((this.motionTimer >= 20) || (pool))
        {
            this.motionTimer = 0;

            if (this.rand.nextInt(3) == 0)
            {
                this.rotary += (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0D;
            }
        }

        this.flameCount += 1;

        if ((this.flameCount == 40) && (this.rand.nextInt(2) == 0))
        {
            poopFire();
        }
        else if ((this.flameCount >= 55 + this.health / 2) && (this.target != null) && ((this.target instanceof EntityLiving)))
        {
            makeFireBall(1);
            this.flameCount = 0;
        }

        if ((this.target != null) && (this.target.isDead))
        {
            setPosition(this.orgX + 0.5D, this.orgY, this.orgZ + 0.5D);
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.target = null;
            chatLine("§cSuch is the fate of a being who opposes the might of the sun.");
            setDoor(0);
            this.gotTarget = false;
        }
    }

    public void burnEntities()
    {
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 4.0D, 0.0D));

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (((entity1 instanceof EntityLiving)) && (!entity1.isImmuneToFire()))
            {
                entity1.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
                entity1.setFire(15);
            }
        }
    }

    public void evapWater()
    {
        int centerX = MathHelper.floor_double(this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2.0D);
        int centerZ = MathHelper.floor_double(this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2.0D);
        int radius = 10;

        for (int x = centerX - radius; x <= centerX + radius; x++)
            for (int z = centerZ - radius; z <= centerZ + radius; z++)
                for (int i = 0; i < 8; i++)
                {
                    int b = this.orgY - 2 + i;

                    if (this.worldObj.getBlockMaterial(x, b, z) == Material.water)
                    {
                        this.worldObj.setBlock(x, b, z, 0);
                        this.worldObj.playSoundEffect(x + 0.5F, b + 0.5F, z + 0.5F, "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);

                        for (int l = 0; l < 8; l++)
                        {
                            this.worldObj.spawnParticle("largesmoke", x + Math.random(), b + 0.75D, z + Math.random(), 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
    }

    public void makeFireBall(int shots)
    {
        this.worldObj.playSoundAtEntity(this, "mob.ghast.fireball", 5.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        boolean flag = false;
        this.ballCount += 1;

        if (this.ballCount >= 3 + this.rand.nextInt(3))
        {
            flag = true;
            this.ballCount = 0;
        }

        for (int i = 0; i < shots; i++)
        {
            EntityFiroBall e1 = new EntityFiroBall(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, flag);
            this.worldObj.spawnEntityInWorld(e1);
        }
    }

    public void poopFire()
    {
        int x = MathHelper.floor_double(this.posX);
        int z = MathHelper.floor_double(this.posZ);
        int b = this.orgY - 2;

        if (AetherBlocks.isGood(this.worldObj.getBlockId(x, b, z), this.worldObj.getBlockMetadata(x, b, z)))
        {
            this.worldObj.setBlock(x, b, z, Block.fire.blockID);
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("OriginX", (short)this.orgX);
        nbttagcompound.setShort("OriginY", (short)this.orgY);
        nbttagcompound.setShort("OriginZ", (short)this.orgZ);
        nbttagcompound.setShort("Wideness", (short)this.wideness);
        nbttagcompound.setShort("FlameCount", (short)this.flameCount);
        nbttagcompound.setShort("BallCount", (short)this.ballCount);
        nbttagcompound.setShort("ChatLog", (short)this.chatLog);
        nbttagcompound.setShort("Direction", (short)this.direction);
        nbttagcompound.setFloat("Rotary", (float)this.rotary);
        this.gotTarget = (this.target != null);
        nbttagcompound.setBoolean("GotTarget", this.gotTarget);
        nbttagcompound.setString("BossName", this.bossName);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.orgX = nbttagcompound.getShort("OriginX");
        this.orgY = nbttagcompound.getShort("OriginY");
        this.orgZ = nbttagcompound.getShort("OriginZ");
        this.wideness = nbttagcompound.getShort("Wideness");
        this.flameCount = nbttagcompound.getShort("FlameCount");
        this.ballCount = nbttagcompound.getShort("BallCount");
        this.chatLog = nbttagcompound.getShort("ChatLog");
        this.direction = nbttagcompound.getShort("Direction");
        this.rotary = nbttagcompound.getFloat("Rotary");
        this.gotTarget = nbttagcompound.getBoolean("GotTarget");
        this.speedness = (0.5D - this.health / 70.0D * 0.2D);
        this.bossName = nbttagcompound.getString("BossName");
    }

    public void chatLine(String s)
    {
    }

    public boolean chatWithMe()
    {
        if (this.chatCount <= 0)
            if (this.chatLog == 0)
            {
                chatLine("§cYou are certainly a brave soul to have entered this chamber.");
                this.chatLog = 1;
                this.chatCount = 100;
            }
            else if (this.chatLog == 1)
            {
                chatLine("§cBegone human, you serve no purpose here.");
                this.chatLog = 2;
                this.chatCount = 100;
            }
            else if (this.chatLog == 2)
            {
                chatLine("§cYour presence annoys me. Do you not fear my burning aura?");
                this.chatLog = 3;
                this.chatCount = 100;
            }
            else if (this.chatLog == 3)
            {
                chatLine("§cI have nothing to offer you, fool. Leave me at peace.");
                this.chatLog = 4;
                this.chatCount = 100;
            }
            else if (this.chatLog == 4)
            {
                chatLine("§cPerhaps you are ignorant. Do you wish to know who I am?");
                this.chatLog = 5;
                this.chatCount = 100;
            }
            else if (this.chatLog == 5)
            {
                chatLine("§cI am a sun spirit, embodiment of Aether's eternal daylight.");
                chatLine("§cAs long as I am alive, the sun will never set on this world.");
                this.chatLog = 6;
                this.chatCount = 100;
            }
            else if (this.chatLog == 6)
            {
                chatLine("§cMy body burns with the anger of a thousand beasts.");
                chatLine("§cNo man, hero, or villain can harm me. You are no exception.");
                this.chatLog = 7;
                this.chatCount = 100;
            }
            else if (this.chatLog == 7)
            {
                chatLine("§cYou wish to challenge the might of the sun? You are mad.");
                chatLine("§cDo not further insult me or you will feel my wrath.");
                this.chatLog = 8;
                this.chatCount = 100;
            }
            else if (this.chatLog == 8)
            {
                chatLine("§cThis is your final warning. Leave now, or prepare to burn.");
                this.chatLog = 9;
                this.chatCount = 100;
            }
            else
            {
                if (this.chatLog == 9)
                {
                    chatLine("§6As you wish, your death will be slow and agonizing.");
                    this.chatLog = 10;
                    return true;
                }

                if ((this.chatLog == 10) && (this.target == null))
                {
                    chatLine("§cDid your previous death not satisfy your curiosity, human?");
                    this.chatLog = 9;
                    this.chatCount = 100;
                }
            }

        return false;
    }

    public boolean interact(EntityPlayer ep)
    {
        if (chatWithMe())
        {
            this.rotary = ((180D / Math.PI) * Math.atan2(this.posX - ep.posX, this.posZ - ep.posZ));
            this.target = ep;
            setDoor(AetherBlocks.LockedDungeonStone.blockID);
            return true;
        }

        return false;
    }

    public void addVelocity(double d, double d1, double d2)
    {
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        if ((ds.getEntity() == null) || (!(ds.getEntity() instanceof EntityFiroBall)))
        {
            return false;
        }

        this.speedness = (0.5D - this.health / 70.0D * 0.2D);
        boolean flag = super.attackEntityFrom(ds, i);

        if (flag)
        {
            this.hurtness = 15;
            this.texture = "/aether/mobs/firemonsterHurt.png";
            EntityFireMinion minion = new EntityFireMinion(this.worldObj);
            minion.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(minion);
            this.worldObj.spawnEntityInWorld(minion);
            this.worldObj.spawnEntityInWorld(minion);

            if (this.health <= 0)
            {
                chatLine("§bSuch bitter cold... is this the feeling... of pain?");
                setDoor(0);
                unlockTreasure();
            }
        }

        return flag;
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        entityDropItem(new ItemStack(AetherItems.Key, 1, 2), 0.0F);
    }

    private void setDoor(int ID)
    {
        if (this.direction / 2 == 0)
        {
            for (int y = this.orgY - 1; y < this.orgY + 2; y++)
            {
                for (int z = this.orgZ - 1; z < this.orgZ + 2; z++)
                {
                    this.worldObj.setBlock(this.orgX + (this.direction == 0 ? -11 : 11), y, z, ID, 2, 4);
                }
            }
        }
        else
        {
            for (int y = this.orgY - 1; y < this.orgY + 2; y++)
            {
                for (int x = this.orgX - 1; x < this.orgX + 2; x++)
                {
                    this.worldObj.setBlock(x, y, this.orgZ + (this.direction == 3 ? 11 : -11), ID, 2, 4);
                }
            }
        }
    }

    private void unlockTreasure()
    {
        if (this.direction / 2 == 0)
        {
            for (int y = this.orgY - 1; y < this.orgY + 2; y++)
            {
                for (int z = this.orgZ - 1; z < this.orgZ + 2; z++)
                {
                    this.worldObj.setBlock(this.orgX + (this.direction == 0 ? 11 : -11), y, z, 0);
                }
            }
        }
        else
        {
            for (int y = this.orgY - 1; y < this.orgY + 2; y++)
            {
                for (int x = this.orgX - 1; x < this.orgX + 2; x++)
                {
                    this.worldObj.setBlock(x, y, this.orgZ + (this.direction == 3 ? -11 : 11), 0);
                }
            }
        }

        for (int x = this.orgX - 20; x < this.orgX + 20; x++)
        {
            for (int y = this.orgY - 3; y < this.orgY + 6; y++)
            {
                for (int z = this.orgZ - 20; z < this.orgZ + 20; z++)
                {
                    int id = this.worldObj.getBlockId(x, y, z);

                    if (id == AetherBlocks.LockedDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z), 4);
                    }

                    if (id == AetherBlocks.LockedLightDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z), 4);
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
        return this.bossName + ", the Sun Spirit";
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

