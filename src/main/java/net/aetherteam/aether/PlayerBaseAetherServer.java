package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.containers.ContainerPlayerAether;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.aetherteam.playercore_api.cores.PlayerCoreServer;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class PlayerBaseAetherServer extends PlayerCoreServer
{
    public AetherCommonPlayerHandler playerHandler = new AetherCommonPlayerHandler(this);
    public int maxHealth;
    public int foodTimer;
    public boolean hasDefeatedSunSpirit;
    public int generalcooldown = 0;
    public int generalcooldownmax = 0;
    public String cooldownName = "Hammer of Notch";
    public InventoryAether inv;
    public float prevStepHeight = 0.5F;
    private boolean jumpBoosted;
    private int flightCount = 0;
    private int maxFlightCount = 52;
    private double flightMod = 1.0D;
    private double maxFlightMod = 15.0D;
    private boolean prevCreative;
    public ArrayList mountInput = new ArrayList();
    private float sinage;
    private int coinAmount;
    public static boolean keepInventory = false;
    private double dungeonPosX;
    private double dungeonPosY;
    private double dungeonPosZ;
    private double nondungeonPosX;
    private double nondungeonPosY;
    private double nondungeonPosZ;
    private int timeInPortal;
    private boolean idleInPortal;
    public static int frostBiteTime;
    public static int frostBiteTimer;
    private Random rand = new Random();
    private int hitAmnt;
    public List extendedReachItems;
    private int timeUntilPortal;

    /** Whether the entity is inside a Portal */
    private boolean inPortal;
    private boolean isParachuting;
    private int parachuteType;

    public PlayerBaseAetherServer(MinecraftServer var1, World var2, String var3, ItemInWorldManager var4, int var5, PlayerCoreServer var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);
        this.player.inventoryContainer = (Container)(!this.player.capabilities.isCreativeMode ? new ContainerPlayerAether(this.player.inventory, this.inv, !this.player.worldObj.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.inventory, false, this.player));
        this.player.openContainer = this.player.inventoryContainer;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var3 = (EntityPlayer)var1.getEntity();
            Party var4 = PartyController.instance().getParty(var3);
            Party var5 = PartyController.instance().getParty(PartyController.instance().getMember(this.player.username));

            if (var4 != null && var5 != null && var4.getName().toLowerCase().equalsIgnoreCase(var5.getName()))
            {
                return false;
            }
        }

        return super.attackEntityFrom(var1, var2);
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }

    public int getCoins()
    {
        return this.coinAmount;
    }

    public void addCoins(int var1)
    {
        this.coinAmount += var1;

        if (!this.player.worldObj.isRemote)
        {
            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.username)));
        }
    }

    public void removeCoins(int var1)
    {
        this.coinAmount -= var1;

        if (!this.player.worldObj.isRemote)
        {
            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.username)));
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock() ? true : super.isOnLadder();
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.isCollidedHorizontally;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt var1)
    {
        if (!(var1 instanceof EntityAetherLightning) || ((EntityAetherLightning)var1).playerUsing != this.player)
        {
            super.onStruckByLightning(var1);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        this.playerHandler.beforeOnLivingUpdate();

        if (this.playerHandler.riddenBy != null && this.playerHandler.riddenBy.shouldBeSitting())
        {
            if (this.playerHandler.riddenBy.animateSitting())
            {
                this.player.setFlag(2, true);
                this.player.shouldRiderSit();
            }

            if (this.playerHandler.riddenBy.sprinting())
            {
                this.player.setFlag(3, true);
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public void attackTargetEntityWithCurrentItem(Entity var1)
    {
        super.attackTargetEntityWithCurrentItem(var1);

        if (var1 instanceof EntityLiving && ((EntityLiving)var1).deathTime <= 0 && !var1.isEntityAlive() && this.player.getCurrentEquippedItem() != null && this.player.getCurrentEquippedItem().itemID == AetherItems.SkyrootSword.itemID && var1 instanceof EntityLiving)
        {
            ;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.fallDistance = 0.0F;
        }

        int var2 = MathHelper.floor_double(this.player.posX);
        int var3 = MathHelper.floor_double(this.player.posY);
        int var4 = MathHelper.floor_double(this.player.posZ);

        if (this.isInBlock(AetherBlocks.ColdFire.blockID))
        {
            this.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 4));
        }

        if (!this.player.worldObj.isRemote && this.player.worldObj instanceof WorldServer)
        {
            ((WorldServer)this.player.worldObj).getMinecraftServer();
            int var1 = this.player.getMaxInPortalTime() + 1;

            if (this.inPortal)
            {
                if (this.player.ridingEntity == null && this.timeInPortal == var1 && this.timeInPortal <= var1)
                {
                    this.inPortal = false;
                    this.timeInPortal = var1;
                    this.player.timeUntilPortal = this.player.getPortalCooldown();
                    Aether.teleportPlayerToAether(this.player, false);
                    this.timeInPortal = var1 + 5;
                }
                else
                {
                    ++this.timeInPortal;
                }
            }
            else
            {
                if (this.timeInPortal > 0 && this.timeInPortal <= var1)
                {
                    this.timeInPortal -= 4;
                }

                if (this.timeInPortal < 0)
                {
                    this.timeInPortal = 0;
                }
            }

            if (!this.idleInPortal)
            {
                this.timeInPortal = 0;
            }

            if (this.timeUntilPortal > 0)
            {
                ;
            }

            this.idleInPortal = this.isInBlock(AetherBlocks.AetherPortal.blockID);
        }

        Party var5 = PartyController.instance().getParty((EntityPlayer)this.player);
        Dungeon var6 = DungeonHandler.instance().getDungeon(var5);

        if (var6 != null)
        {
            if ((DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && !DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) || DungeonHandler.instance().getInstanceAt(var2, var3, var4) == null) && var6.getQueuedMembers().contains(PartyController.instance().getMember((EntityPlayer)this.player)) && var6.hasStarted() && this.player.worldObj.provider.dimensionId == 3)
            {
                this.player.setPositionAndUpdate(this.dungeonPosX, this.dungeonPosY, this.dungeonPosZ);
            }
            else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) && var6.hasStarted())
            {
                this.dungeonPosX = this.player.posX;
                this.dungeonPosY = this.player.posY;
                this.dungeonPosZ = this.player.posZ;
            }
        }
        else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && this.player.worldObj.provider.dimensionId == 3)
        {
            this.player.setPositionAndUpdate(this.nondungeonPosX, this.nondungeonPosY, this.nondungeonPosZ);
        }
        else
        {
            this.nondungeonPosX = this.player.posX;
            this.nondungeonPosY = this.player.posY;
            this.nondungeonPosZ = this.player.posZ;
        }

        super.onUpdate();
        this.processAbilities();
        int var7 = MathHelper.floor_double(this.player.posX);
        int var8 = MathHelper.floor_double(this.player.posY);
        int var9 = MathHelper.floor_double(this.player.posZ);

        if (var6 != null && !var6.hasMember(PartyController.instance().getMember((EntityPlayer)this.player)))
        {
            ;
        }

        if (this.prevCreative != this.player.capabilities.isCreativeMode)
        {
            System.out.println("hey");

            if (!this.player.capabilities.isCreativeMode)
            {
                ;
            }

            this.prevCreative = this.player.capabilities.isCreativeMode;
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.username) != null)
        {
            this.maxHealth = ((Integer)Aether.proxy.getClientExtraHearts().get(this.player.username)).intValue();
        }

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        PotionEffect var10 = this.player.getActivePotionEffect(Potion.regeneration);

        if (var10 != null && var10.getDuration() > 0 && Potion.potionTypes[var10.getPotionID()].isReady(var10.getDuration(), var10.getAmplifier()) && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth)
        {
            this.player.heal(1);
        }

        if (this.player.getFoodStats().getFoodLevel() >= 18 && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth)
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                this.foodTimer = 0;
                this.player.heal(1);
            }
        }
        else
        {
            this.foodTimer = 0;
        }

        if (this.player.worldObj.difficultySetting == 0 && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth && this.player.ticksExisted % 20 == 0)
        {
            this.player.heal(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity var11 = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(var11.posX - this.player.posX, 2.0D) + Math.pow(var11.posY - this.player.posY, 2.0D) + Math.pow(var11.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        ItemStack var15 = this.player.getCurrentEquippedItem();

        if (var15 != null && var15.getItem() != null && this.extendedReachItems.contains(var15.getItem()))
        {
            this.player.theItemInWorldManager.setBlockReachDistance(10.0D);
        }
        else
        {
            this.player.theItemInWorldManager.setBlockReachDistance(5.0D);
        }

        if (this.player.dimension == 3 && this.player.posY < -2.0D)
        {
            Aether.teleportPlayerToAether(this.player, true);
        }

        this.playerHandler.afterOnUpdate();
        float var12 = this.player.getMoveForward();
        float var13 = this.player.getMoveStrafing();

        if (this.player.ridingEntity != null && (var13 != 0.0F || var12 != 0.0F))
        {
            this.player.setMoveForward(0.0F);
            this.player.setMoveStrafing(0.0F);
        }

        if (this.isParachuting)
        {
            Vec3 var14 = this.player.getLookVec();

            switch (this.getParachuteType())
            {
                case 0:
                    this.player.motionX *= 0.6D;
                    this.player.motionY = -0.08D;
                    this.player.motionZ *= 0.6D;
                    break;

                case 1:
                    this.player.motionX *= 0.6D;
                    this.player.motionY = -1.08D;
                    this.player.motionZ *= 0.6D;
                    break;

                case 2:
                    this.player.motionX = var14.xCoord * 0.18000000715255737D;
                    this.player.motionY = -0.07999999821186066D;
                    this.player.motionZ = var14.zCoord * 0.18000000715255737D;
                    break;

                case 3:
                    this.player.motionY = -0.07999999821186066D;
                    break;

                case 4:
                    this.player.motionX *= 0.6D;
                    this.player.motionY = 1.08D;
                    this.player.motionZ *= 0.6D;

                    if (this.player.posY >= (double)this.player.worldObj.getActualHeight() || !this.player.worldObj.isAirBlock(var7, var8 + 1, var9))
                    {
                        this.setParachuting(false, this.parachuteType);
                    }
            }

            this.player.capabilities.allowFlying = true;
            this.player.velocityChanged = true;
            this.player.fallDistance = 0.0F;

            if ((this.getParachuteType() == 4 || this.player.worldObj.isAirBlock(var7, var8 - 1, var9)) && (!this.isParachuting || this.hitAmnt < 4))
            {
                if (this.player.swingProgressInt >= 4 && this.isParachuting && var14.yCoord >= 1.0D)
                {
                    System.out.println(this.hitAmnt);
                    ++this.hitAmnt;
                }
            }
            else
            {
                this.setParachuting(false, this.parachuteType);
                this.player.capabilities.allowFlying = this.player.capabilities.isCreativeMode;
                this.hitAmnt = 0;
            }
        }

        if (this.clientInfoChanged())
        {
            this.updatePlayerClientInfo();
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource var1)
    {
        float var2 = this.player.experience;
        int var3 = this.player.experienceTotal;
        int var4 = this.player.experienceLevel;

        for (int var5 = 0; var5 < 8; ++var5)
        {
            if (this.inv.slots[var5] != null && this.inv.slots[var5].itemID == AetherItems.CrystalBottle.itemID)
            {
                if (this.inv.slots[var5].getTagCompound() != null)
                {
                    this.inv.slots[var5].getTagCompound().setFloat("Experience", this.inv.slots[var5].getTagCompound().getFloat("Experience") + (float)(var4 * 2) + (float)Math.round(var2 * 10.0F) * 0.1F);
                }
                else
                {
                    this.inv.slots[var5].setTagCompound(new NBTTagCompound());
                    this.inv.slots[var5].getTagCompound().setFloat("Experience", this.inv.slots[var5].getTagCompound().getFloat("Experience") + (float)(var4 * 2) + (float)Math.round(var2 * 10.0F) * 0.1F);
                }
            }
            else if (this.inv.slots[var5] != null && this.inv.slots[var5].itemID == AetherItems.PiggieBank.itemID)
            {
                if (this.inv.slots[var5].getTagCompound() != null)
                {
                    this.inv.slots[var5].getTagCompound().setInteger("Coins", this.inv.slots[var5].getTagCompound().getInteger("Coins") + this.getCoins());
                }
                else
                {
                    this.inv.slots[var5].setTagCompound(new NBTTagCompound());
                    this.inv.slots[var5].getTagCompound().setInteger("Coins", this.inv.slots[var5].getTagCompound().getInteger("Coins") + this.getCoins());
                }
            }
        }

        if (this.wearingAccessory(AetherItems.PiggieBank.itemID))
        {
            this.setCoinAmount(0);
        }

        if (this.wearingAccessory(AetherItems.CrystalBottle.itemID))
        {
            this.player.experience = 0.0F;
            this.player.experienceLevel = 0;
        }

        var2 = this.player.experience;
        var3 = this.player.experienceTotal;
        var4 = this.player.experienceLevel;
        super.onDeath(var1);
        PartyMember var11 = PartyController.instance().getMember((EntityPlayer)this.player);
        int var6 = MathHelper.floor_double(this.player.posX);
        int var7 = MathHelper.floor_double(this.player.posY);
        int var8 = MathHelper.floor_double(this.player.posZ);
        Dungeon var9 = DungeonHandler.instance().getInstanceAt(var6, var7, var8);
        System.out.println("Hooking into player death!");

        if (var11 != null && var9 != null)
        {
            Party var10 = PartyController.instance().getParty(var11);

            if (var10 != null && var9.isActive() && var9.isQueuedParty(var10))
            {
                this.player.isDead = false;
                this.player.setEntityHealth(this.maxHealth);
                this.player.motionX = this.player.motionY = this.player.motionZ = 0.0D;
                this.player.setFoodStats(new FoodStats());
                this.player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(var2, var3, var4));
                this.player.setPositionAndUpdate((double)((float)((double)var9.getControllerX() + 0.5D)), (double)((float)((double)var9.getControllerY() + 1.0D)), (double)((float)((double)var9.getControllerZ() + 0.5D)));
                this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonRespawn(var9, var10));
                return;
            }
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public boolean isAboveBlock(int var1)
    {
        MathHelper.floor_double(this.player.posX);
        int var2 = MathHelper.floor_double(this.player.boundingBox.minY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1;
    }

    public boolean isInBlock(int var1)
    {
        MathHelper.floor_double(this.player.posX);
        int var2 = MathHelper.floor_double(this.player.posY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var2, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var2 + 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1;
    }

    public boolean setGeneralCooldown(int var1, String var2)
    {
        if (this.generalcooldown == 0)
        {
            this.generalcooldown = var1;
            this.generalcooldownmax = var1;
            this.cooldownName = var2;

            if (!this.player.worldObj.isRemote)
            {
                this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.generalcooldown, this.generalcooldownmax, this.cooldownName, Collections.singleton(this.player.username)));
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean clientInfoChanged()
    {
        PlayerClientInfo var1 = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(this.player.username);
        return var1 == null ? false : this.player.getTotalArmorValue() != var1.getArmourValue() || this.player.getFoodStats().getFoodLevel() != var1.getHunger() || this.player.getHealth() != var1.getHalfHearts() || this.maxHealth != var1.getMaxHealth() || this.getCoins() != var1.getAetherCoins();
    }

    public void processAbilities()
    {
        if (!this.player.onGround)
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

        if (this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock())
        {
            if (!this.player.onGround && this.player.motionY < 0.0D && !this.player.isInWater() && !this.player.isSneaking())
            {
                this.player.motionY *= 0.6D;
            }

            this.player.fallDistance = -1.0F;
        }

        if (this.player.ticksExisted % 400 == 0)
        {
            if (this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID)
            {
                this.inv.slots[0].damageItem(1, this.player);

                if (this.inv.slots[0].stackSize < 1)
                {
                    this.inv.slots[0] = null;
                }
            }

            if (this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID)
            {
                this.inv.slots[4].damageItem(1, this.player);

                if (this.inv.slots[4].stackSize < 1)
                {
                    this.inv.slots[4] = null;
                }
            }

            if (this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID)
            {
                this.inv.slots[5].damageItem(1, this.player);

                if (this.inv.slots[5].stackSize < 1)
                {
                    this.inv.slots[5] = null;
                }
            }
        }

        if (this.wearingPhoenixArmour())
        {
            this.player.extinguish();
            this.player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 10, 4));
        }

        if (this.wearingGravititeArmour())
        {
            if (this.player.isJumping && !this.jumpBoosted && this.player.isSneaking())
            {
                this.player.motionY = 1.0D;
                this.jumpBoosted = true;
            }

            this.player.fallDistance = -1.0F;
        }

        if (this.wearingObsidianArmour())
        {
            this.player.addPotionEffect(new PotionEffect(Potion.resistance.id, 10, 3));
            this.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 1));
        }

        if (this.wearingValkyrieArmour())
        {
            if (this.player.isJumping)
            {
                if (this.flightMod >= this.maxFlightMod)
                {
                    this.flightMod = this.maxFlightMod;
                }

                if (this.flightCount > 2)
                {
                    if (this.flightCount < this.maxFlightCount)
                    {
                        this.flightMod += 0.25D;
                        this.player.motionY = 0.025D * this.flightMod;
                        ++this.flightCount;
                    }
                }
                else
                {
                    ++this.flightCount;
                }
            }
            else
            {
                this.flightMod = 1.0D;
            }

            this.player.fallDistance = -1.0F;
        }

        if (this.player.onGround)
        {
            this.flightCount = 0;
            this.flightMod = 1.0D;
        }

        if (!this.player.isJumping && this.player.onGround)
        {
            this.jumpBoosted = false;
        }

        for (int var1 = 0; var1 < 8; ++var1)
        {
            if (this.inv.slots[var1] != null && this.inv.slots[var1].getItem() instanceof IAetherAccessory)
            {
                ((ItemAccessory)this.inv.slots[var1].getItem()).activateServerPassive(this.player, this);
            }
        }

        if (!this.wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.stepHeight = this.prevStepHeight;
        }
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    public void jump()
    {
        if (this.playerHandler.jump())
        {
            super.jump();
        }
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        float var1 = this.playerHandler.getSpeedModifier();
        return var1 == -1.0F ? var1 : super.getSpeedModifier();
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(int var1)
    {
        if (this.player.getHealth() > 0)
        {
            this.player.setEntityHealth(this.player.getHealth() + var1);

            if (this.player.getHealth() > this.maxHealth)
            {
                this.player.setEntityHealth(this.maxHealth);
            }

            this.updatePlayerClientInfo();
        }
    }

    public void updatePlayerClientInfo()
    {
        if (!this.player.worldObj.isRemote)
        {
            MinecraftServer var1 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var2 = var1.getConfigurationManager();
            PlayerClientInfo var3 = new PlayerClientInfo(this.player.getHealth(), this.maxHealth, this.player.getFoodStats().getFoodLevel(), this.player.getTotalArmorValue(), this.getCoins());
            Aether.proxy.getPlayerClientInfo().put(this.player.username, var3);
            PartyMember var4 = PartyController.instance().getMember(this.player.username);
            PartyController.instance().getParty(var4);

            for (int var5 = 0; var5 < var2.playerEntityList.size(); ++var5)
            {
                EntityPlayerMP var6 = (EntityPlayerMP)var2.playerEntityList.get(var5);
                PartyController.instance().getMember((EntityPlayer)var6);
                var6.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.username, var3));
            }

            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.username, var3));
        }
    }

    public void increaseMaxHP(int var1)
    {
        if (this.maxHealth <= 40 - var1)
        {
            if (!this.player.worldObj.isRemote)
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth + var1, Collections.singleton(this.player.username)));
            }

            this.maxHealth += var1;
            this.player.setEntityHealth(this.player.getHealth() + var1);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("MaxHealth", this.maxHealth);
        var1.setTag("AetherInventory", this.inv.writeToNBT(new NBTTagList()));
        var1.setBoolean("HasDefeatedSunSpirit", this.hasDefeatedSunSpirit);
        var1.setBoolean("inAether", this.player.dimension == 3);
        var1.setInteger("GeneralCooldown", this.generalcooldown);
        var1.setInteger("GeneralCooldownMax", this.generalcooldownmax);
        var1.setString("CooldownName", this.cooldownName);
        var1.setInteger("Coins", this.coinAmount);
        var1.setDouble("NondungeonPosX", this.nondungeonPosX);
        var1.setDouble("NondungeonPosY", this.nondungeonPosY);
        var1.setDouble("NondungeonPosZ", this.nondungeonPosZ);
        var1.setDouble("DungeonPosX", this.dungeonPosX);
        var1.setDouble("DungeonPosY", this.dungeonPosY);
        var1.setDouble("DungeonPosZ", this.dungeonPosZ);
        super.writeEntityToNBT(var1);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        if (!this.player.worldObj.isRemote)
        {
            File var2 = new File(((SaveHandler)this.player.worldObj.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

            if (var2.exists())
            {
                new NBTTagCompound();

                try
                {
                    NBTTagCompound var3 = CompressedStreamTools.readCompressed(new FileInputStream(var2));
                    this.maxHealth = var3.getInteger("MaxHealth");
                    NBTTagList var4 = var3.getTagList("Inventory");

                    if (this.player.dimension == 3)
                    {
                        this.player.dimension = 3;
                    }

                    this.inv.readFromNBT(var4);
                    var2.delete();
                }
                catch (IOException var5)
                {
                    ;
                }
            }
            else
            {
                System.out.println("Failed to read player data. Making new");
                this.maxHealth = var1.getInteger("MaxHealth");
                NBTTagList var6 = var1.getTagList("AetherInventory");
                this.hasDefeatedSunSpirit = var1.getBoolean("HasDefeatedSunSpirit");

                if (var1.getBoolean("inAether"))
                {
                    this.player.dimension = 3;
                }

                this.generalcooldown = var1.getInteger("GeneralCooldown");
                this.generalcooldownmax = var1.getInteger("GeneralCooldownMax");
                this.cooldownName = var1.getString("CooldownName");
                this.coinAmount = var1.getInteger("Coins");
                this.nondungeonPosX = var1.getDouble("NondungeonPosX");
                this.nondungeonPosY = var1.getDouble("NondungeonPosY");
                this.nondungeonPosZ = var1.getDouble("NondungeonPosZ");
                this.dungeonPosX = var1.getDouble("DungeonPosX");
                this.dungeonPosY = var1.getDouble("DungeonPosY");
                this.dungeonPosZ = var1.getDouble("DungeonPosZ");
                this.inv.readFromNBT(var6);
            }
        }

        super.readEntityFromNBT(var1);
    }

    public void onDefeatSunSpirit()
    {
        this.setHasDefeatedSunSpirit(true);
    }

    public void setHasDefeatedSunSpirit(boolean var1)
    {
        this.hasDefeatedSunSpirit = var1;
    }

    public boolean getHasDefeatedSunSpirit()
    {
        return this.hasDefeatedSunSpirit;
    }

    /**
     * Returns how strong the player is against the specified block at this moment
     */
    public float getCurrentPlayerStrVsBlock(Block var1, boolean var2)
    {
        ItemStack var3 = this.player.inventory.getCurrentItem();
        float var4 = var3 == null ? 1.0F : var3.getItem().getStrVsBlock(var3, var1, 0);

        if (this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID)
        {
            var4 *= 1.0F + (float)this.inv.slots[0].getItemDamage() / ((float)this.inv.slots[0].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID)
        {
            var4 *= 1.0F + (float)this.inv.slots[4].getItemDamage() / ((float)this.inv.slots[4].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID)
        {
            var4 *= 1.0F + (float)this.inv.slots[5].getItemDamage() / ((float)this.inv.slots[5].getMaxDamage() * 3.0F);
        }

        if (!this.wearingNeptuneArmour())
        {
            return var4 == -1.0F ? super.getCurrentPlayerStrVsBlock(var1, var2) : var4;
        }
        else
        {
            if (var4 > 1.0F)
            {
                int var5 = EnchantmentHelper.getEfficiencyModifier(this.player);
                ItemStack var6 = this.player.inventory.getCurrentItem();

                if (var5 > 0 && var6 != null)
                {
                    float var7 = (float)(var5 * var5 + 1);
                    boolean var8 = ForgeHooks.canToolHarvestBlock(var1, 0, var6);

                    if (!var8 && var4 <= 1.0F)
                    {
                        var4 += var7 * 0.08F;
                    }
                    else
                    {
                        var4 += var7;
                    }
                }
            }

            if (this.player.isPotionActive(Potion.digSpeed))
            {
                var4 *= 1.0F + (float)(this.player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
            }

            if (this.player.isPotionActive(Potion.digSlowdown))
            {
                var4 *= 1.0F - (float)(this.player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
            }

            var4 = ForgeEventFactory.getBreakSpeed(this.player, var1, 0, var4);
            return var4 < 0.0F ? 0.0F : var4;
        }
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
        return this.wearingNeptuneArmour() ? false : super.isInWater();
    }

    /**
     * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
     */
    public MovingObjectPosition rayTrace(double var1, float var3)
    {
        ItemStack var4 = this.player.getCurrentEquippedItem();

        if (var4 != null && var4.getItem() != null && this.extendedReachItems.contains(var4.getItem()))
        {
            var1 = 10.0D;
        }

        return this.player.rayTrace(var1, var3);
    }

    public int getAccessoryCount(int var1)
    {
        int var2 = 0;

        for (int var3 = 0; var3 < 8; ++var3)
        {
            if (this.inv.slots[var3] != null && this.inv.slots[var3].itemID == var1)
            {
                ++var2;
            }
        }

        return var2;
    }

    public boolean wearingAccessory(int var1)
    {
        for (int var2 = 0; var2 < 8; ++var2)
        {
            if (this.inv.slots[var2] != null && this.inv.slots[var2].itemID == var1)
            {
                return true;
            }
        }

        return false;
    }

    public void setSlotStack(int var1, ItemStack var2)
    {
        this.inv.slots[var1] = var2;
    }

    public ItemStack getSlotStack(int var1)
    {
        ItemStack var2 = null;

        for (int var3 = 0; var3 < 8; ++var3)
        {
            if (this.inv.slots[var3] != null && this.inv.slots[var3].itemID == var1)
            {
                var2 = this.inv.slots[var3];
                return var2;
            }
        }

        return var2;
    }

    public int getSlotIndex(int var1)
    {
        for (int var2 = 0; var2 < 8; ++var2)
        {
            if (this.inv.slots[var2] != null && this.inv.slots[var2].itemID == var1)
            {
                return var2;
            }
        }

        return 0;
    }

    public boolean wearingArmour(int var1)
    {
        for (int var2 = 0; var2 < 4; ++var2)
        {
            if (this.player.inventory.armorInventory[var2] != null && this.player.inventory.armorInventory[var2].itemID == var1)
            {
                return true;
            }
        }

        return false;
    }

    public float getSinage()
    {
        return this.sinage;
    }

    public boolean wearingNeptuneArmour()
    {
        return this.wearingArmour(AetherItems.NeptuneHelmet.itemID) && this.wearingArmour(AetherItems.NeptuneChestplate.itemID) && this.wearingArmour(AetherItems.NeptuneLeggings.itemID) && this.wearingArmour(AetherItems.NeptuneBoots.itemID) && this.wearingAccessory(AetherItems.NeptuneGloves.itemID);
    }

    public boolean wearingValkyrieArmour()
    {
        return this.wearingArmour(AetherItems.ValkyrieHelmet.itemID) && this.wearingArmour(AetherItems.ValkyrieChestplate.itemID) && this.wearingArmour(AetherItems.ValkyrieLeggings.itemID) && this.wearingArmour(AetherItems.ValkyrieBoots.itemID) && this.wearingAccessory(AetherItems.ValkyrieGloves.itemID);
    }

    public boolean wearingObsidianArmour()
    {
        return this.wearingArmour(AetherItems.ObsidianHelmet.itemID) && this.wearingArmour(AetherItems.ObsidianChestplate.itemID) && this.wearingArmour(AetherItems.ObsidianLeggings.itemID) && this.wearingArmour(AetherItems.ObsidianBoots.itemID) && this.wearingAccessory(AetherItems.ObsidianGloves.itemID);
    }

    public boolean wearingPhoenixArmour()
    {
        return this.wearingArmour(AetherItems.PhoenixHelmet.itemID) && this.wearingArmour(AetherItems.PhoenixChestplate.itemID) && this.wearingArmour(AetherItems.PhoenixLeggings.itemID) && this.wearingArmour(AetherItems.PhoenixBoots.itemID) && this.wearingAccessory(AetherItems.PhoenixGloves.itemID);
    }

    public boolean wearingGravititeArmour()
    {
        return this.wearingArmour(AetherItems.GravititeHelmet.itemID) && this.wearingArmour(AetherItems.GravititeChestplate.itemID) && this.wearingArmour(AetherItems.GravititeLeggings.itemID) && this.wearingArmour(AetherItems.GravititeBoots.itemID) && this.wearingAccessory(AetherItems.GravititeGloves.itemID);
    }

    public void setCoinAmount(int var1)
    {
        this.coinAmount = var1;

        if (!this.player.worldObj.isRemote)
        {
            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.username)));
        }
    }

    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal()
    {
        MathHelper.floor_double(this.player.posX);
        MathHelper.floor_double(this.player.posY);
        MathHelper.floor_double(this.player.posZ);

        if (this.timeUntilPortal > 0)
        {
            this.timeUntilPortal = 900;
        }
        else
        {
            this.inPortal = true;
        }
    }

    public boolean isInPortal()
    {
        return this.inPortal;
    }

    public boolean getParachuting()
    {
        return this.isParachuting;
    }

    public int getParachuteType()
    {
        return this.parachuteType;
    }

    public int getType()
    {
        switch (this.getParachuteType())
        {
            case 0:
                return 0;

            case 1:
                return 2;

            case 2:
                return 5;

            case 3:
                return 3;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    public void setParachuting(boolean var1, int var2)
    {
        this.isParachuting = var1;
        this.parachuteType = var2;

        if (!var1)
        {
            EntityItem var3 = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(Item.silk, this.rand.nextInt(3)));
            EntityItem var4 = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(AetherBlocks.Aercloud, this.rand.nextInt(3), this.getType()));
            this.player.worldObj.spawnEntityInWorld(var4);
            this.player.worldObj.spawnEntityInWorld(var3);
        }

        if (!this.player.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, var1, var1, var2, Collections.singleton(this.player.username)));
        }
    }
}
