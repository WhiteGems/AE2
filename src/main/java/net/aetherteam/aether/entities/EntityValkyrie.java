package net.aetherteam.aether.entities;

import net.aetherteam.aether.AetherNameGen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.oldcode.EntityHomeShot;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityValkyrie extends EntityDungeonMob implements IAetherBoss
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

    public EntityValkyrie(World var1)
    {
        super(var1);
        this.setSize(0.8F, 1.6F);
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

    public EntityValkyrie(World var1, double var2, double var4, double var6, boolean var8)
    {
        super(var1);

        if (var8)
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

        this.setSize(0.8F, 1.6F);
        this.name = AetherNameGen.valkGen();
        this.teleTimer = this.rand.nextInt(250);
        this.moveSpeed = 0.5F;
        this.timeLeft = 1200;
        this.attackStrength = 7;
        this.safeX = this.posX = var2;
        this.safeY = this.posY = var4;
        this.safeZ = this.posZ = var6;
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

    private void becomeAngryAt(Entity var1)
    {
        this.entityToAttack = var1;
        this.angerLevel = 200 + this.rand.nextInt(200);

        if (this.boss)
        {
            for (int var2 = this.dungeonZ + 2; var2 < this.dungeonZ + 23; var2 += 7)
            {
                if (this.worldObj.getBlockId(this.dungeonX - 1, this.dungeonY, var2) == 0)
                {
                    this.dungeonEntranceZ = var2;
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, var2, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, var2 + 1, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, var2 + 1, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, var2, AetherBlocks.LockedDungeonStone.blockID, 1, 4);
                    return;
                }
            }
        }
    }

    public void setDungeon(int var1, int var2, int var3)
    {
        this.hasDungeon = true;
        this.dungeonX = var1;
        this.dungeonY = var2;
        this.dungeonZ = var3;
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

        for (int var1 = this.dungeonX - 26; var1 < this.dungeonX + 29; ++var1)
        {
            for (int var2 = this.dungeonY - 1; var2 < this.dungeonY + 22; ++var2)
            {
                for (int var3 = this.dungeonZ - 5; var3 < this.dungeonZ + 25; ++var3)
                {
                    int var4 = this.worldObj.getBlockId(var1, var2, var3);

                    if (var4 == AetherBlocks.LockedDungeonStone.blockID)
                    {
                        this.worldObj.setBlock(var1, var2, var3, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(var1, var2, var3), 4);
                    }

                    if (var4 == AetherBlocks.Trap.blockID)
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

    private void chatItUp(String var1)
    {
        if (this.chatTime <= 0 && this.otherDimension())
        {
            this.chatTime = 60;
        }
    }

    public void teleport(double var1, double var3, double var5, int var7)
    {
        int var8 = this.rand.nextInt(var7 + 1);
        int var9 = this.rand.nextInt(var7 / 2);
        int var10 = var7 - var8;
        var8 *= this.rand.nextInt(2) * 2 - 1;
        var9 *= this.rand.nextInt(2) * 2 - 1;
        var10 *= this.rand.nextInt(2) * 2 - 1;
        var1 += (double)var8;
        var3 += (double)var9;
        var5 += (double)var10;
        int var11 = (int)Math.floor(var1 - 0.5D);
        int var12 = (int)Math.floor(var3 - 0.5D);
        int var13 = (int)Math.floor(var5 - 0.5D);
        boolean var14 = false;

        for (int var15 = 0; var15 < 32 && !var14; ++var15)
        {
            int var16 = var11 + (this.rand.nextInt(var7 / 2) - this.rand.nextInt(var7 / 2));
            int var17 = var12 + (this.rand.nextInt(var7 / 2) - this.rand.nextInt(var7 / 2));
            int var18 = var13 + (this.rand.nextInt(var7 / 2) - this.rand.nextInt(var7 / 2));

            if (var17 <= 124 && var17 >= 5 && this.isAirySpace(var16, var17, var18) && this.isAirySpace(var16, var17 + 1, var18) && !this.isAirySpace(var16, var17 - 1, var18) && (!this.hasDungeon || var16 > this.dungeonX && var16 < this.dungeonX + 20 && var17 > this.dungeonY && var17 < this.dungeonY + 12 && var18 > this.dungeonZ && var18 < this.dungeonZ + 20))
            {
                var11 = var16;
                var12 = var17;
                var13 = var18;
                var14 = true;
            }
        }

        if (!var14)
        {
            this.teleFail();
        }
        else
        {
            this.spawnExplosionParticle();
            this.setPosition((double)var11 + 0.5D, (double)var12 + 0.5D, (double)var13 + 0.5D);
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.moveForward = 0.0F;
            this.moveStrafing = 0.0F;
            this.isJumping = false;
            this.rotationPitch = 0.0F;
            this.rotationYaw = 0.0F;
            this.setPathToEntity((PathEntity)null);
            this.renderYawOffset = this.rand.nextFloat() * 360.0F;
            this.spawnExplosionParticle();
            this.teleTimer = this.rand.nextInt(40);
        }
    }

    public void makeHomeShot(int var1, EntityLiving var2)
    {
        for (int var3 = 0; var3 < var1; ++var3)
        {
            EntityHomeShot var4 = new EntityHomeShot(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, var2);
            this.worldObj.spawnEntityInWorld(var4);
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

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (!this.duel && !this.talking && this.boss)
        {
            ;
        }

        if (!this.hasMet)
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
            this.faceEntity(this.mc.thePlayer, 15.0F, 180.0F);
        }
        else
        {
            super.updateEntityActionState();
            ++this.teleTimer;

            if (this.teleTimer >= 450)
            {
                if (this.entityToAttack != null)
                {
                    if (this.boss && this.onGround && this.rand.nextInt(2) == 0 && this.entityToAttack != null && this.entityToAttack instanceof EntityLiving)
                    {
                        this.makeHomeShot(1, (EntityLiving)this.entityToAttack);
                        this.teleTimer = -100;
                    }
                    else
                    {
                        this.teleport(this.entityToAttack.posX, this.entityToAttack.posY, this.entityToAttack.posZ, 7);
                    }
                }
                else if (this.onGround && !this.boss)
                {
                    this.teleport(this.posX, this.posY, this.posZ, 12 + this.rand.nextInt(12));
                }
                else
                {
                    this.teleport(this.safeX, this.safeY, this.safeZ, 6);
                }
            }
            else if (this.teleTimer < 446 && (this.posY <= 0.0D || this.posY <= this.safeY - 16.0D))
            {
                this.teleTimer = 446;
            }
            else if (this.teleTimer % 5 == 0 && this.entityToAttack != null && !this.canEntityBeSeen(this.entityToAttack))
            {
                this.teleTimer += 100;
            }
        }

        if (this.onGround && this.teleTimer % 10 == 0 && !this.boss)
        {
            this.safeX = this.posX;
            this.safeY = this.posY;
            this.safeZ = this.posZ;
        }

        if (this.entityToAttack != null && this.entityToAttack.isDead)
        {
            this.entityToAttack = null;

            if (this.boss)
            {
                this.unlockDoor();
            }

            this.angerLevel = 0;
        }

        if (this.chatTime > 0)
        {
            --this.chatTime;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.lastMotionY = this.motionY;
        super.onUpdate();

        if (!this.onGround && this.entityToAttack != null && this.lastMotionY >= 0.0D && this.motionY < 0.0D && this.getDistanceToEntity(this.entityToAttack) <= 16.0F && this.canEntityBeSeen(this.entityToAttack))
        {
            double var1 = this.entityToAttack.posX - this.posX;
            double var3 = this.entityToAttack.posZ - this.posZ;
            double var5 = Math.atan2(var1, var3);
            this.motionX = Math.sin(var5) * 0.25D;
            this.motionZ = Math.cos(var5) * 0.25D;
        }

        if (!this.onGround && !this.isOnLadder() && Math.abs(this.motionY - this.lastMotionY) > 0.07D && Math.abs(this.motionY - this.lastMotionY) < 0.09D)
        {
            this.motionY += 0.054999999701976776D;

            if (this.motionY < -0.2750000059604645D)
            {
                this.motionY = -0.2750000059604645D;
            }
        }

        this.moveSpeed = this.entityToAttack == null ? 0.5F : 1.0F;

        if (this.worldObj.difficultySetting <= 0 && (this.entityToAttack != null || this.angerLevel > 0))
        {
            this.angerLevel = 0;
            this.entityToAttack = null;
        }

        if (this.isSwinging)
        {
            this.prevSwingProgress += 0.15F;
            this.swingProgress += 0.15F;

            if (this.prevSwingProgress > 1.0F || this.swingProgress > 1.0F)
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

        if (!this.otherDimension())
        {
            --this.timeLeft;

            if (this.timeLeft <= 0)
            {
                this.isDead = true;
                this.spawnExplosionParticle();
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setShort("Anger", (short)this.angerLevel);
        var1.setShort("TeleTimer", (short)this.teleTimer);
        var1.setShort("TimeLeft", (short)this.timeLeft);
        var1.setBoolean("Boss", this.boss);
        var1.setBoolean("Duel", this.duel);
        var1.setInteger("DungeonX", this.dungeonX);
        var1.setInteger("DungeonY", this.dungeonY);
        var1.setInteger("DungeonZ", this.dungeonZ);
        var1.setInteger("DungeonEntranceZ", this.dungeonEntranceZ);
        var1.setTag("SafePos", this.newDoubleNBTList(new double[] {this.safeX, this.safeY, this.safeZ}));
        var1.setString("BossName", this.name);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.angerLevel = var1.getShort("Anger");
        this.teleTimer = var1.getShort("TeleTimer");
        this.timeLeft = var1.getShort("TimeLeft");
        this.duel = var1.getBoolean("Duel");
        this.boss = var1.getBoolean("Boss");
        this.dungeonX = var1.getInteger("DungeonX");
        this.dungeonY = var1.getInteger("DungeonY");
        this.dungeonZ = var1.getInteger("DungeonZ");
        this.dungeonEntranceZ = var1.getInteger("DungeonEntranceZ");

        if (this.boss)
        {
            this.texture = "/aether/mobs/valkyrie2.png";
        }

        NBTTagList var2 = var1.getTagList("SafePos");
        this.safeX = ((NBTTagDouble)var2.tagAt(0)).data;
        this.safeY = ((NBTTagDouble)var2.tagAt(1)).data;
        this.safeZ = ((NBTTagDouble)var2.tagAt(2)).data;

        if (var1.getBoolean("IsCurrentBoss"))
        {
            this.name = var1.getString("BossName");
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        return this.otherDimension() && (this.worldObj.difficultySetting <= 0 || this.boss && !this.duel || this.angerLevel <= 0) ? null : super.findPlayerToAttack();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() instanceof EntityPlayer && this.worldObj.difficultySetting > 0)
        {
            int var3;

            if (this.boss && (!this.duel || this.worldObj.difficultySetting <= 0))
            {
                this.spawnExplosionParticle();
                var3 = this.rand.nextInt(2);

                if (var3 == 2)
                {
                    this.chatItUp("Sorry, I don\'t fight with weaklings.");
                }
                else
                {
                    this.chatItUp("Try defeating some weaker valkyries first.");
                }

                return false;
            }
            else
            {
                if (this.boss)
                {
                    if (this.entityToAttack == null)
                    {
                        this.chatTime = 0;
                        this.chatItUp("This will be your final battle!");
                    }
                    else
                    {
                        this.teleTimer += 60;
                    }
                }
                else if (this.entityToAttack == null)
                {
                    this.chatTime = 0;
                    var3 = this.rand.nextInt(3);

                    if (var3 == 2)
                    {
                        this.chatItUp("I\'m not going easy on you!");
                    }
                    else if (var3 == 1)
                    {
                        this.chatItUp("You\'re gonna regret that!");
                    }
                    else
                    {
                        this.chatItUp("Now you\'re in for it!");
                    }
                }
                else
                {
                    this.teleTimer -= 10;
                }

                this.becomeAngryAt(var1.getEntity());
                boolean var5 = super.attackEntityFrom(var1, var2);

                if (var5 && this.health <= 0)
                {
                    int var4 = this.rand.nextInt(3);
                    this.isDead = true;

                    if (this.boss)
                    {
                        this.isDead = false;
                        this.unlockDoor();
                        this.unlockTreasure();
                        this.chatItUp("You are truly... a mighty warrior...");
                    }
                    else if (var4 == 2)
                    {
                        this.chatItUp("Alright, alright! You win!");
                    }
                    else if (var4 == 1)
                    {
                        this.chatItUp("Okay, I give up! Geez!");
                    }
                    else
                    {
                        this.chatItUp("Oww! Fine, here\'s your medal...");
                    }

                    this.spawnExplosionParticle();
                }

                return var5;
            }
        }
        else
        {
            this.teleport(this.posX, this.posY, this.posZ, 8);
            this.extinguish();
            return false;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity var1, float var2)
    {
        if (this.attackTime <= 0 && var2 < 2.75F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.swingArm();
            var1.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);

            if (var1 != null && this.entityToAttack != null && var1 == this.entityToAttack && var1 instanceof EntityLiving)
            {
                EntityLiving var3 = (EntityLiving)var1;

                if (var3.getHealth() <= 0)
                {
                    this.entityToAttack = null;
                    this.angerLevel = 0;
                    int var4 = this.rand.nextInt(3);
                    this.chatTime = 0;

                    if (this.boss)
                    {
                        this.chatItUp("As expected of a human.");
                        this.unlockDoor();
                    }
                    else if (var4 == 2)
                    {
                        this.chatItUp("You want a medallion? Try being less pathetic.");
                    }
                    else if (var4 == 1 && var3 instanceof EntityPlayer)
                    {
                        EntityPlayer var5 = (EntityPlayer)var3;
                        String var6 = var5.username;
                        this.chatItUp("Maybe some day, " + var6 + "... maybe some day.");
                    }
                    else
                    {
                        this.chatItUp("Humans aren\'t nearly as cute when they\'re dead.");
                    }
                }
            }
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        if (this.boss)
        {
            this.entityDropItem(new ItemStack(AetherItems.Key, 1, 1), 0.0F);
            this.dropItem(Item.swordGold.itemID, 1);
        }
        else
        {
            this.dropItem(AetherItems.VictoryMedal.itemID, 1);
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float var1) {}

    public boolean otherDimension()
    {
        return true;
    }

    public boolean isAirySpace(int var1, int var2, int var3)
    {
        int var4 = this.worldObj.getBlockId(var1, var2, var3);
        return var4 == 0 || Block.blocksList[var4].getCollisionBoundingBoxFromPool(this.worldObj, var1, var2, var3) == null;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return !this.boss;
    }

    public Entity getBossEntity()
    {
        return this;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.worldObj.getFullBlockLightValue(var1, var2, var3) > 8 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
    }

    public int getMedals()
    {
        int var1 = 0;
        ItemStack[] var2 = this.mc.thePlayer.inventory.mainInventory;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];

            if (var5 != null && var5.itemID == AetherItems.VictoryMedal.itemID)
            {
                var1 += var5.stackSize;
            }
        }

        return var1;
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
