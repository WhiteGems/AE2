package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.oldcode.EntityHomeShot;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityValkyrie extends EntityDungeonMob
    implements IAetherBoss
{
    private String optPrefix;
    public boolean hasMet;
    public boolean talking;
    public boolean isSwinging;
    public boolean boss;
    public boolean duel;
    public boolean hasDungeon;
    public int teleTimer;
    public int angerLevel;
    public int timeLeft;
    public int chatTime;
    public int dungeonX;
    public int dungeonY;
    public int dungeonZ;
    public int dungeonEntranceZ;
    public double safeX;
    public double safeY;
    public double safeZ;
    public float sinage;
    public double lastMotionY;
    public String name;
    Minecraft mc = ModLoader.getMinecraftInstance();

    public EntityValkyrie(World world)
    {
        super(world);
        setSize(0.8F, 1.6F);
        this.texture = "/aether/mobs/valkyrie.png";
        this.teleTimer = this.rand.nextInt(250);
        this.health = 50;
        this.moveSpeed = 0.5F;
        this.timeLeft = 1200;
        this.attackStrength = 7;
        this.safeX = this.posX;
        this.safeY = this.posY;
        this.safeZ = this.posZ;
    }

    public EntityValkyrie(World world, double x, double y, double z, boolean flag)
    {
        super(world);

        if (flag)
        {
            this.texture = "/aether/mobs/valkyrie2.png";
            this.health = 500;
            this.boss = true;
        }
        else
        {
            this.texture = "/aether/mobs/valkyrie.png";
            this.health = 50;
        }

        setSize(0.8F, 1.6F);
        this.name = AetherNameGen.valkGen();
        this.teleTimer = this.rand.nextInt(250);
        this.moveSpeed = 0.5F;
        this.timeLeft = 1200;
        this.attackStrength = 7;
        this.safeX = (this.posX = x);
        this.safeY = (this.posY = y);
        this.safeZ = (this.posZ = z);
        this.hasDungeon = false;
    }

    public void swingArm()
    {
        if (!this.isSwinging)
        {
            this.isSwinging = true;
            this.prevSwingProgress = 0.0F;
            this.swingProgress = 0.0F;
        }
    }

    private void becomeAngryAt(Entity entity)
    {
        this.entityToAttack = entity;
        this.angerLevel = (200 + this.rand.nextInt(200));

        if (this.boss)
        {
            for (int k = this.dungeonZ + 2; k < this.dungeonZ + 23; k += 7)
                if (this.worldObj.getBlockId(this.dungeonX - 1, this.dungeonY, k) == 0)
                {
                    this.dungeonEntranceZ = k;
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, k, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, k + 1, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, k + 1, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, k, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    return;
                }
        }
    }

    public void setDungeon(int i, int j, int k)
    {
        this.hasDungeon = true;
        this.dungeonX = i;
        this.dungeonY = j;
        this.dungeonZ = k;
    }

    private void unlockDoor()
    {
        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ, 0);
        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ + 1, 0);
        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ + 1, 0);
        this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ, 0);
    }

    private void unlockTreasure()
    {
        this.worldObj.setBlock(this.dungeonX + 16, this.dungeonY + 1, this.dungeonZ + 9, Block.trapdoor.blockID, 3, 4);
        this.worldObj.setBlock(this.dungeonX + 17, this.dungeonY + 1, this.dungeonZ + 9, Block.trapdoor.blockID, 2, 4);
        this.worldObj.setBlock(this.dungeonX + 16, this.dungeonY + 1, this.dungeonZ + 10, Block.trapdoor.blockID, 3, 4);
        this.worldObj.setBlock(this.dungeonX + 17, this.dungeonY + 1, this.dungeonZ + 10, Block.trapdoor.blockID, 2, 4);

        for (int x = this.dungeonX - 26; x < this.dungeonX + 29; x++)
            for (int y = this.dungeonY - 1; y < this.dungeonY + 22; y++)
                for (int z = this.dungeonZ - 5; z < this.dungeonZ + 25; z++)
                {
                    int id = this.worldObj.getBlockId(x, y, z);

                    if (id == AetherBlocks.LockedDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z), 4);
                    }

                    if (id == AetherBlocks.Trap.blockID)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z), 4);
                    }

                    if (id == AetherBlocks.LockedLightDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(x, y, z, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z), 4);
                    }
                }
    }

    private void chatItUp(String s)
    {
        if ((this.chatTime <= 0) && (otherDimension()))
        {
            this.chatTime = 60;
        }
    }

    public void teleport(double x, double y, double z, int rad)
    {
        int a = this.rand.nextInt(rad + 1);
        int b = this.rand.nextInt(rad / 2);
        int c = rad - a;
        a *= (this.rand.nextInt(2) * 2 - 1);
        b *= (this.rand.nextInt(2) * 2 - 1);
        c *= (this.rand.nextInt(2) * 2 - 1);
        x += a;
        y += b;
        z += c;
        int newX = (int)Math.floor(x - 0.5D);
        int newY = (int)Math.floor(y - 0.5D);
        int newZ = (int)Math.floor(z - 0.5D);
        boolean flag = false;

        for (int q = 0; (q < 32) && (!flag); q++)
        {
            int i = newX + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
            int j = newY + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
            int k = newZ + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));

            if ((j <= 124) && (j >= 5))
            {
                if ((isAirySpace(i, j, k)) && (isAirySpace(i, j + 1, k)) && (!isAirySpace(i, j - 1, k)) && ((!this.hasDungeon) || ((i > this.dungeonX) && (i < this.dungeonX + 20) && (j > this.dungeonY) && (j < this.dungeonY + 12) && (k > this.dungeonZ) && (k < this.dungeonZ + 20))))
                {
                    newX = i;
                    newY = j;
                    newZ = k;
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            teleFail();
        }
        else
        {
            spawnExplosionParticle();
            setPosition(newX + 0.5D, newY + 0.5D, newZ + 0.5D);
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.moveForward = 0.0F;
            this.moveStrafing = 0.0F;
            this.isJumping = false;
            this.rotationPitch = 0.0F;
            this.rotationYaw = 0.0F;
            setPathToEntity(null);
            this.renderYawOffset = (this.rand.nextFloat() * 360.0F);
            spawnExplosionParticle();
            this.teleTimer = this.rand.nextInt(40);
        }
    }

    public void makeHomeShot(int shots, EntityLiving ep)
    {
        for (int i = 0; i < shots; i++)
        {
            EntityHomeShot e1 = new EntityHomeShot(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, ep);
            this.worldObj.spawnEntityInWorld(e1);
        }
    }

    public void teleFail()
    {
        this.teleTimer -= this.rand.nextInt(40) + 40;

        if (this.posY <= 0.0D)
        {
            this.teleTimer = 446;
        }
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if (((this.duel) || (this.talking) || (!this.boss)) ||
                (!this.hasMet))
        {
            this.hasMet = true;
        }

        return true;
    }

    public void updateEntityActionState()
    {
        if (this.talking)
        {
            this.moveStrafing = 0.0F;
            this.motionY *= 0.5D;
            this.moveForward = 0.0F;
            faceEntity(this.mc.thePlayer, 15.0F, 180.0F);
        }
        else
        {
            super.updateEntityActionState();
            this.teleTimer += 1;

            if (this.teleTimer >= 450)
            {
                if (this.entityToAttack != null)
                {
                    if ((this.boss) && (this.onGround) && (this.rand.nextInt(2) == 0) && (this.entityToAttack != null) && ((this.entityToAttack instanceof EntityLiving)))
                    {
                        makeHomeShot(1, (EntityLiving)this.entityToAttack);
                        this.teleTimer = -100;
                    }
                    else
                    {
                        teleport(this.entityToAttack.posX, this.entityToAttack.posY, this.entityToAttack.posZ, 7);
                    }
                }
                else if ((!this.onGround) || (this.boss))
                {
                    teleport(this.safeX, this.safeY, this.safeZ, 6);
                }
                else
                {
                    teleport(this.posX, this.posY, this.posZ, 12 + this.rand.nextInt(12));
                }
            }
            else if ((this.teleTimer < 446) && ((this.posY <= 0.0D) || (this.posY <= this.safeY - 16.0D)))
            {
                this.teleTimer = 446;
            }
            else if ((this.teleTimer % 5 == 0) && (this.entityToAttack != null) && (!canEntityBeSeen(this.entityToAttack)))
            {
                this.teleTimer += 100;
            }
        }

        if ((this.onGround) && (this.teleTimer % 10 == 0) && (!this.boss))
        {
            this.safeX = this.posX;
            this.safeY = this.posY;
            this.safeZ = this.posZ;
        }

        if ((this.entityToAttack != null) && (this.entityToAttack.isDead))
        {
            this.entityToAttack = null;

            if (this.boss)
            {
                unlockDoor();
            }

            this.angerLevel = 0;
        }

        if (this.chatTime > 0)
        {
            this.chatTime -= 1;
        }
    }

    public void onUpdate()
    {
        this.lastMotionY = this.motionY;
        super.onUpdate();

        if ((!this.onGround) && (this.entityToAttack != null) && (this.lastMotionY >= 0.0D) && (this.motionY < 0.0D) && (getDistanceToEntity(this.entityToAttack) <= 16.0F) && (canEntityBeSeen(this.entityToAttack)))
        {
            double a = this.entityToAttack.posX - this.posX;
            double b = this.entityToAttack.posZ - this.posZ;
            double angle = Math.atan2(a, b);
            this.motionX = (Math.sin(angle) * 0.25D);
            this.motionZ = (Math.cos(angle) * 0.25D);
        }

        if ((!this.onGround) && (!isOnLadder()) && (Math.abs(this.motionY - this.lastMotionY) > 0.07000000000000001D) && (Math.abs(this.motionY - this.lastMotionY) < 0.09D))
        {
            this.motionY += 0.05499999970197678D;

            if (this.motionY < -0.2750000059604645D)
            {
                this.motionY = -0.2750000059604645D;
            }
        }

        this.moveSpeed = (this.entityToAttack == null ? 0.5F : 1.0F);

        if ((this.worldObj.difficultySetting <= 0) && ((this.entityToAttack != null) || (this.angerLevel > 0)))
        {
            this.angerLevel = 0;
            this.entityToAttack = null;
        }

        if (this.isSwinging)
        {
            this.prevSwingProgress += 0.15F;
            this.swingProgress += 0.15F;

            if ((this.prevSwingProgress > 1.0F) || (this.swingProgress > 1.0F))
            {
                this.isSwinging = false;
                this.prevSwingProgress = 0.0F;
                this.swingProgress = 0.0F;
            }
        }

        if (!this.onGround)
        {
            this.sinage += 0.75F;
        }
        else
        {
            this.sinage += 0.15F;
        }

        if (this.sinage > ((float)Math.PI * 2F))
        {
            this.sinage -= ((float)Math.PI * 2F);
        }

        if (!otherDimension())
        {
            this.timeLeft -= 1;

            if (this.timeLeft <= 0)
            {
                this.isDead = true;
                spawnExplosionParticle();
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)this.angerLevel);
        nbttagcompound.setShort("TeleTimer", (short)this.teleTimer);
        nbttagcompound.setShort("TimeLeft", (short)this.timeLeft);
        nbttagcompound.setBoolean("Boss", this.boss);
        nbttagcompound.setBoolean("Duel", this.duel);
        nbttagcompound.setInteger("DungeonX", this.dungeonX);
        nbttagcompound.setInteger("DungeonY", this.dungeonY);
        nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
        nbttagcompound.setInteger("DungeonEntranceZ", this.dungeonEntranceZ);
        nbttagcompound.setTag("SafePos", newDoubleNBTList(new double[] { this.safeX, this.safeY, this.safeZ }));
        nbttagcompound.setString("BossName", this.name);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        this.teleTimer = nbttagcompound.getShort("TeleTimer");
        this.timeLeft = nbttagcompound.getShort("TimeLeft");
        this.duel = nbttagcompound.getBoolean("Duel");
        this.boss = nbttagcompound.getBoolean("Boss");
        this.dungeonX = nbttagcompound.getInteger("DungeonX");
        this.dungeonY = nbttagcompound.getInteger("DungeonY");
        this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
        this.dungeonEntranceZ = nbttagcompound.getInteger("DungeonEntranceZ");

        if (this.boss)
        {
            this.texture = "/aether/mobs/valkyrie2.png";
        }

        NBTTagList nbttaglist = nbttagcompound.getTagList("SafePos");
        this.safeX = ((NBTTagDouble)nbttaglist.tagAt(0)).data;
        this.safeY = ((NBTTagDouble)nbttaglist.tagAt(1)).data;
        this.safeZ = ((NBTTagDouble)nbttaglist.tagAt(2)).data;

        if (nbttagcompound.getBoolean("IsCurrentBoss"))
        {
            this.name = nbttagcompound.getString("BossName");
        }
    }

    protected Entity findPlayerToAttack()
    {
        if ((otherDimension()) && ((this.worldObj.difficultySetting <= 0) || ((this.boss) && (!this.duel)) || (this.angerLevel <= 0)))
        {
            return null;
        }

        return super.findPlayerToAttack();
    }

    public boolean attackEntityFrom(DamageSource ds, int i)
    {
        if (((ds.getEntity() instanceof EntityPlayer)) && (this.worldObj.difficultySetting > 0))
        {
            if ((this.boss) && ((!this.duel) || (this.worldObj.difficultySetting <= 0)))
            {
                spawnExplosionParticle();
                int pokey = this.rand.nextInt(2);

                if (pokey == 2)
                {
                    chatItUp("Sorry, I don't fight with weaklings.");
                }
                else
                {
                    chatItUp("Try defeating some weaker valkyries first.");
                }

                return false;
            }

            if (this.boss)
            {
                if (this.entityToAttack == null)
                {
                    this.chatTime = 0;
                    chatItUp("This will be your final battle!");
                }
                else
                {
                    this.teleTimer += 60;
                }
            }
            else if (this.entityToAttack == null)
            {
                this.chatTime = 0;
                int pokey = this.rand.nextInt(3);

                if (pokey == 2)
                {
                    chatItUp("I'm not going easy on you!");
                }
                else if (pokey == 1)
                {
                    chatItUp("You're gonna regret that!");
                }
                else
                {
                    chatItUp("Now you're in for it!");
                }
            }
            else
            {
                this.teleTimer -= 10;
            }

            becomeAngryAt(ds.getEntity());
        }
        else
        {
            teleport(this.posX, this.posY, this.posZ, 8);
            extinguish();
            return false;
        }

        boolean flag = super.attackEntityFrom(ds, i);

        if ((flag) && (this.health <= 0))
        {
            int pokey = this.rand.nextInt(3);
            this.isDead = true;

            if (this.boss)
            {
                this.isDead = false;
                unlockDoor();
                unlockTreasure();
                chatItUp("You are truly... a mighty warrior...");
            }
            else if (pokey == 2)
            {
                chatItUp("Alright, alright! You win!");
            }
            else if (pokey == 1)
            {
                chatItUp("Okay, I give up! Geez!");
            }
            else
            {
                chatItUp("Oww! Fine, here's your medal...");
            }

            spawnExplosionParticle();
        }

        return flag;
    }

    protected void attackEntity(Entity entity, float f)
    {
        if ((this.attackTime <= 0) && (f < 2.75F) && (entity.boundingBox.maxY > this.boundingBox.minY) && (entity.boundingBox.minY < this.boundingBox.maxY))
        {
            this.attackTime = 20;
            swingArm();
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);

            if ((entity != null) && (this.entityToAttack != null) && (entity == this.entityToAttack) && ((entity instanceof EntityLiving)))
            {
                EntityLiving e1 = (EntityLiving)entity;

                if (e1.getHealth() <= 0)
                {
                    this.entityToAttack = null;
                    this.angerLevel = 0;
                    int pokey = this.rand.nextInt(3);
                    this.chatTime = 0;

                    if (this.boss)
                    {
                        chatItUp("As expected of a human.");
                        unlockDoor();
                    }
                    else if (pokey == 2)
                    {
                        chatItUp("You want a medallion? Try being less pathetic.");
                    }
                    else if ((pokey == 1) && ((e1 instanceof EntityPlayer)))
                    {
                        EntityPlayer ep = (EntityPlayer)e1;
                        String s = ep.username;
                        chatItUp("Maybe some day, " + s + "... maybe some day.");
                    }
                    else
                    {
                        chatItUp("Humans aren't nearly as cute when they're dead.");
                    }
                }
            }
        }
    }

    protected void dropFewItems(boolean var1, int var2)
    {
        if (this.boss)
        {
            entityDropItem(new ItemStack(AetherItems.Key, 1, 1), 0.0F);
            dropItem(Item.swordGold.itemID, 1);
        }
        else
        {
            dropItem(AetherItems.VictoryMedal.itemID, 1);
        }
    }

    public void fall(float f)
    {
    }

    public boolean otherDimension()
    {
        return true;
    }

    public boolean isAirySpace(int x, int y, int z)
    {
        int p = this.worldObj.getBlockId(x, y, z);
        return (p == 0) || (Block.blocksList[p].getCollisionBoundingBoxFromPool(this.worldObj, x, y, z) == null);
    }

    public boolean canDespawn()
    {
        return !this.boss;
    }

    public Entity getBossEntity()
    {
        return this;
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.worldObj.getFullBlockLightValue(i, j, k) > 8) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox));
    }

    public int getMedals()
    {
        int medals = 0;

        for (ItemStack item : this.mc.thePlayer.bK.mainInventory)
        {
            if ((item != null) &&
                    (item.itemID == AetherItems.VictoryMedal.itemID))
            {
                medals += item.stackSize;
            }
        }

        return medals;
    }

    public int getBossHP()
    {
        return this.health;
    }

    public int getBossMaxHP()
    {
        return 500;
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return this.name + ", the Valkyrie Queen";
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

