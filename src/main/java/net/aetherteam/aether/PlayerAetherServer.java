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
import net.aetherteam.aether.entities.mounts.MountInput;
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
import net.minecraft.entity.SharedMonsterAttributes;
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

public class PlayerAetherServer extends PlayerCoreServer
{
    public AetherCommonPlayerHandler playerHandler = new AetherCommonPlayerHandler(this);
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
    public ArrayList<MountInput> mountInput = new ArrayList();
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
    private int zephyrCoolDown = 120;
    private Random rand = new Random();
    private int hitAmnt;
    public List<Item> extendedReachItems;
    private int timeUntilPortal;

    /** Whether the entity is inside a Portal */
    private boolean inPortal;
    private boolean isParachuting;
    private int parachuteType;

    public PlayerAetherServer(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager, int playerCoreIndex, PlayerCoreServer entityPlayerMP)
    {
        super(par1MinecraftServer, par2World, par3Str, par4ItemInWorldManager, playerCoreIndex, entityPlayerMP);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.inv = new InventoryAether(this.player);
        this.player.inventoryContainer = (Container)(!this.player.capabilities.isCreativeMode ? new ContainerPlayerAether(this.player.inventory, this.inv, !this.player.worldObj.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.inventory, false, this.player));
        this.player.openContainer = this.player.inventoryContainer;
    }

    public int getZephyrCoolDown()
    {
        return this.zephyrCoolDown;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer attackingPlayer = (EntityPlayer)var1.getEntity();
            Party attackingParty = PartyController.instance().getParty(attackingPlayer);
            Party receivingParty = PartyController.instance().getParty(PartyController.instance().getMember(this.player.username));

            if (attackingParty != null && receivingParty != null && attackingParty.getName().toLowerCase().equalsIgnoreCase(receivingParty.getName()))
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

    public void addCoins(int amount)
    {
        this.coinAmount += amount;

        if (!this.player.worldObj.isRemote)
        {
            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.username)));
        }
    }

    public void removeCoins(int amount)
    {
        this.coinAmount -= amount;

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
    public void attackTargetEntityWithCurrentItem(Entity ent)
    {
        super.attackTargetEntityWithCurrentItem(ent);

        if (ent instanceof EntityLiving && ((EntityLiving)ent).deathTime <= 0 && !ent.isEntityAlive() && this.player.getCurrentEquippedItem() != null && this.player.getCurrentEquippedItem().itemID == AetherItems.SkyrootSword.itemID && ent instanceof EntityLiving)
        {
            ;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.getZephyrCoolDown() > 0)
        {
            --this.zephyrCoolDown;
        }

        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.fallDistance = 0.0F;
        }

        int posX = MathHelper.floor_double(this.player.posX);
        int posY = MathHelper.floor_double(this.player.posY);
        int posZ = MathHelper.floor_double(this.player.posZ);

        if (this.isInBlock(AetherBlocks.ColdFire.blockID))
        {
            this.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 4));
        }

        if (!this.player.worldObj.isRemote && this.player.worldObj instanceof WorldServer)
        {
            ((WorldServer)this.player.worldObj).getMinecraftServer();
            int i = this.player.getMaxInPortalTime() + 1;

            if (this.inPortal)
            {
                if (this.player.ridingEntity == null && this.timeInPortal == i && this.timeInPortal <= i)
                {
                    this.inPortal = false;
                    this.timeInPortal = i;
                    this.player.timeUntilPortal = this.player.getPortalCooldown();
                    Aether.teleportPlayerToAether(this.player, false);
                    this.timeInPortal = i + 5;
                }
                else
                {
                    ++this.timeInPortal;
                }
            }
            else
            {
                if (this.timeInPortal > 0 && this.timeInPortal <= i)
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

        Party party = PartyController.instance().getParty((EntityPlayer)this.player);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

        if (dungeon != null)
        {
            if ((DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null && !DungeonHandler.instance().getInstanceAt(posX, posY, posZ).equals(dungeon) || DungeonHandler.instance().getInstanceAt(posX, posY, posZ) == null) && dungeon.getQueuedMembers().contains(PartyController.instance().getMember((EntityPlayer)this.player)) && dungeon.hasStarted() && this.player.worldObj.provider.dimensionId == 3)
            {
                this.player.setPositionAndUpdate(this.dungeonPosX, this.dungeonPosY, this.dungeonPosZ);
            }
            else if (DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null && DungeonHandler.instance().getInstanceAt(posX, posY, posZ).equals(dungeon) && dungeon.hasStarted())
            {
                this.dungeonPosX = this.player.posX;
                this.dungeonPosY = this.player.posY;
                this.dungeonPosZ = this.player.posZ;
            }
        }
        else if (DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null && this.player.worldObj.provider.dimensionId == 3)
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
        int x = MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.posY);
        int z = MathHelper.floor_double(this.player.posZ);

        if (dungeon != null && !dungeon.hasMember(PartyController.instance().getMember((EntityPlayer)this.player)))
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
            this.player.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.player.func_110138_aP());
        }

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        if (this.player.worldObj.difficultySetting == 0 && this.player.func_110143_aJ() >= 20.0F && this.player.func_110143_aJ() < this.player.func_110138_aP() && this.player.ticksExisted % 20 == 0)
        {
            this.player.heal(1.0F);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity stack = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(stack.posX - this.player.posX, 2.0D) + Math.pow(stack.posY - this.player.posY, 2.0D) + Math.pow(stack.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        ItemStack var14 = this.player.getCurrentEquippedItem();

        if (var14 != null && var14.getItem() != null && this.extendedReachItems.contains(var14.getItem()))
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
        float forward = this.player.getMoveForward();
        float strafe = this.player.getMoveStrafing();

        if (this.player.ridingEntity != null && (strafe != 0.0F || forward != 0.0F))
        {
            this.player.setMoveForward(0.0F);
            this.player.setMoveStrafing(0.0F);
        }

        if (this.isParachuting)
        {
            Vec3 vec3 = this.player.getLookVec();

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
                    this.player.motionX = vec3.xCoord * 0.18000000715255737D;
                    this.player.motionY = -0.07999999821186066D;
                    this.player.motionZ = vec3.zCoord * 0.18000000715255737D;
                    break;

                case 3:
                    this.player.motionY = -0.07999999821186066D;
                    break;

                case 4:
                    this.player.motionX *= 0.6D;
                    this.player.motionY = 1.08D;
                    this.player.motionZ *= 0.6D;

                    if (this.player.posY >= (double)this.player.worldObj.getActualHeight() || !this.player.worldObj.isAirBlock(x, y + 1, z))
                    {
                        this.setParachuting(false, this.parachuteType);
                    }
            }

            this.player.capabilities.allowFlying = true;
            this.player.velocityChanged = true;
            this.player.fallDistance = 0.0F;

            if ((this.getParachuteType() == 4 || this.player.worldObj.isAirBlock(x, y - 1, z)) && (!this.isParachuting || this.hitAmnt < 4))
            {
                if (this.player.swingProgress >= 4.0F && this.isParachuting && vec3.yCoord >= 1.0D)
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
    public void onDeath(DamageSource source)
    {
        float exp = this.player.experience;
        int expTotal = this.player.experienceTotal;
        int expLevel = this.player.experienceLevel;

        for (int member = 0; member < 8; ++member)
        {
            if (this.inv.slots[member] != null && this.inv.slots[member].itemID == AetherItems.CrystalBottle.itemID)
            {
                if (this.inv.slots[member].getTagCompound() != null)
                {
                    this.inv.slots[member].getTagCompound().setFloat("Experience", this.inv.slots[member].getTagCompound().getFloat("Experience") + (float)(expLevel * 2) + (float)Math.round(exp * 10.0F) * 0.1F);
                }
                else
                {
                    this.inv.slots[member].setTagCompound(new NBTTagCompound());
                    this.inv.slots[member].getTagCompound().setFloat("Experience", this.inv.slots[member].getTagCompound().getFloat("Experience") + (float)(expLevel * 2) + (float)Math.round(exp * 10.0F) * 0.1F);
                }
            }
            else if (this.inv.slots[member] != null && this.inv.slots[member].itemID == AetherItems.PiggieBank.itemID)
            {
                if (this.inv.slots[member].getTagCompound() != null)
                {
                    this.inv.slots[member].getTagCompound().setInteger("Coins", this.inv.slots[member].getTagCompound().getInteger("Coins") + this.getCoins());
                }
                else
                {
                    this.inv.slots[member].setTagCompound(new NBTTagCompound());
                    this.inv.slots[member].getTagCompound().setInteger("Coins", this.inv.slots[member].getTagCompound().getInteger("Coins") + this.getCoins());
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

        exp = this.player.experience;
        expTotal = this.player.experienceTotal;
        expLevel = this.player.experienceLevel;
        super.onDeath(source);
        PartyMember var11 = PartyController.instance().getMember((EntityPlayer)this.player);
        int x = MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.posY);
        int z = MathHelper.floor_double(this.player.posZ);
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(x, y, z);
        System.out.println("Hooking into player death!");

        if (var11 != null && dungeon != null)
        {
            Party party = PartyController.instance().getParty(var11);

            if (party != null && dungeon.isActive() && dungeon.isQueuedParty(party))
            {
                this.player.isDead = false;
                this.player.setEntityHealth(this.player.func_110138_aP());
                this.player.motionX = this.player.motionY = this.player.motionZ = 0.0D;
                this.player.setFoodStats(new FoodStats());
                this.player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(exp, expTotal, expLevel));
                this.player.setPositionAndUpdate((double)((float)((double)dungeon.getControllerX() + 0.5D)), (double)((float)((double)dungeon.getControllerY() + 1.0D)), (double)((float)((double)dungeon.getControllerZ() + 0.5D)));
                this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonRespawn(dungeon, party));
                return;
            }
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity var1, float var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public boolean isAboveBlock(int blockID)
    {
        MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.boundingBox.minY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID;
    }

    public boolean isInBlock(int blockID)
    {
        MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.posY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y + 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID;
    }

    public boolean setGeneralCooldown(int cooldown, String stackName)
    {
        if (this.generalcooldown == 0)
        {
            this.generalcooldown = cooldown;
            this.generalcooldownmax = cooldown;
            this.cooldownName = stackName;

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
        PlayerClientInfo playerClientInfo = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(this.player.username);
        return playerClientInfo == null ? false : this.player.getTotalArmorValue() != playerClientInfo.getArmourValue() || this.player.getFoodStats().getFoodLevel() != playerClientInfo.getHunger() || this.player.func_110143_aJ() != (float)playerClientInfo.getHalfHearts() || this.player.func_110138_aP() != (float)playerClientInfo.getMaxHealth() || this.getCoins() != playerClientInfo.getAetherCoins();
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
            if (this.player.isJumping() && !this.jumpBoosted && this.player.isSneaking())
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
            if (this.player.isJumping())
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

        if (!this.player.isJumping() && this.player.onGround)
        {
            this.jumpBoosted = false;
        }

        for (int index = 0; index < 8; ++index)
        {
            if (this.inv.slots[index] != null && this.inv.slots[index].getItem() instanceof IAetherAccessory)
            {
                ((ItemAccessory)this.inv.slots[index].getItem()).activateServerPassive(this.player, this);
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

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float i)
    {
        if (this.player.func_110143_aJ() > 0.0F)
        {
            this.player.setEntityHealth(this.player.func_110143_aJ() + i);

            if (this.player.func_110143_aJ() > this.player.func_110138_aP())
            {
                this.player.setEntityHealth(this.player.func_110138_aP());
            }

            if (!this.player.worldObj.isRemote)
            {
                System.out.println("Health: " + this.player.getDataWatcher().func_111145_d(6));
                System.out.println("Max Health: " + this.player.func_110138_aP());
            }

            this.updatePlayerClientInfo();
        }
    }

    public void updatePlayerClientInfo()
    {
        if (!this.player.worldObj.isRemote)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();
            PlayerClientInfo playerClientInfo = new PlayerClientInfo((int)this.player.getDataWatcher().func_111145_d(6), (int)this.player.func_110138_aP(), this.player.getFoodStats().getFoodLevel(), this.player.getTotalArmorValue(), this.getCoins());
            Aether.proxy.getPlayerClientInfo().put(this.player.username, playerClientInfo);
            PartyMember member = PartyController.instance().getMember(this.player.username);
            PartyController.instance().getParty(member);

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); ++playerAmount)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP)configManager.playerEntityList.get(playerAmount);
                PartyController.instance().getMember((EntityPlayer)entityPlayer);
                entityPlayer.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.username, playerClientInfo));
            }

            this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.username, playerClientInfo));
        }
    }

    public void increaseMaxHP(int i)
    {
        if (!this.player.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, (int)this.player.func_110138_aP() + i, Collections.singleton(this.player.username)));
        }

        this.player.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)(this.player.func_110138_aP() + (float)i));
        this.player.setEntityHealth(this.player.func_110143_aJ() + (float)i);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setTag("AetherInventory", this.inv.writeToNBT(new NBTTagList()));
        tag.setBoolean("HasDefeatedSunSpirit", this.hasDefeatedSunSpirit);
        tag.setBoolean("inAether", this.player.dimension == 3);
        tag.setInteger("GeneralCooldown", this.generalcooldown);
        tag.setInteger("GeneralCooldownMax", this.generalcooldownmax);
        tag.setString("CooldownName", this.cooldownName);
        tag.setInteger("Coins", this.coinAmount);
        tag.setDouble("NondungeonPosX", this.nondungeonPosX);
        tag.setDouble("NondungeonPosY", this.nondungeonPosY);
        tag.setDouble("NondungeonPosZ", this.nondungeonPosZ);
        tag.setDouble("DungeonPosX", this.dungeonPosX);
        tag.setDouble("DungeonPosY", this.dungeonPosY);
        tag.setDouble("DungeonPosZ", this.dungeonPosZ);
        tag.setInteger("ZephyrCoolDown", this.zephyrCoolDown);
        super.writeEntityToNBT(tag);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        if (!this.player.worldObj.isRemote)
        {
            File file = new File(((SaveHandler)this.player.worldObj.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

            if (file.exists())
            {
                new NBTTagCompound();

                try
                {
                    NBTTagCompound nbttaglist = CompressedStreamTools.readCompressed(new FileInputStream(file));
                    NBTTagList ioexception = nbttaglist.getTagList("Inventory");

                    if (this.player.dimension == 3)
                    {
                        this.player.dimension = 3;
                    }

                    this.inv.readFromNBT(ioexception);
                    file.delete();
                }
                catch (IOException var5)
                {
                    ;
                }
            }
            else
            {
                System.out.println("Failed to read player data. Making new");
                NBTTagList nbttaglist1 = tag.getTagList("AetherInventory");
                this.hasDefeatedSunSpirit = tag.getBoolean("HasDefeatedSunSpirit");

                if (tag.getBoolean("inAether"))
                {
                    this.player.dimension = 3;
                }

                this.generalcooldown = tag.getInteger("GeneralCooldown");
                this.generalcooldownmax = tag.getInteger("GeneralCooldownMax");
                this.cooldownName = tag.getString("CooldownName");
                this.coinAmount = tag.getInteger("Coins");
                this.nondungeonPosX = tag.getDouble("NondungeonPosX");
                this.nondungeonPosY = tag.getDouble("NondungeonPosY");
                this.nondungeonPosZ = tag.getDouble("NondungeonPosZ");
                this.dungeonPosX = tag.getDouble("DungeonPosX");
                this.dungeonPosY = tag.getDouble("DungeonPosY");
                this.dungeonPosZ = tag.getDouble("DungeonPosZ");
                this.zephyrCoolDown = tag.getInteger("ZephyrCooldown");
                this.inv.readFromNBT(nbttaglist1);
            }
        }

        super.readEntityFromNBT(tag);
    }

    public void onDefeatSunSpirit()
    {
        this.setHasDefeatedSunSpirit(true);
    }

    public void setHasDefeatedSunSpirit(boolean defeatedSunSpirit)
    {
        this.hasDefeatedSunSpirit = defeatedSunSpirit;
    }

    public boolean getHasDefeatedSunSpirit()
    {
        return this.hasDefeatedSunSpirit;
    }

    /**
     * Returns how strong the player is against the specified block at this moment
     */
    public float getCurrentPlayerStrVsBlock(Block block, boolean flag)
    {
        ItemStack stack = this.player.inventory.getCurrentItem();
        float f = stack == null ? 1.0F : stack.getItem().getStrVsBlock(stack, block, 0);

        if (this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID)
        {
            f *= 1.0F + (float)this.inv.slots[0].getItemDamage() / ((float)this.inv.slots[0].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID)
        {
            f *= 1.0F + (float)this.inv.slots[4].getItemDamage() / ((float)this.inv.slots[4].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID)
        {
            f *= 1.0F + (float)this.inv.slots[5].getItemDamage() / ((float)this.inv.slots[5].getMaxDamage() * 3.0F);
        }

        if (!this.wearingNeptuneArmour())
        {
            return f == -1.0F ? super.getCurrentPlayerStrVsBlock(block, flag) : f;
        }
        else
        {
            if (f > 1.0F)
            {
                int i = EnchantmentHelper.getEfficiencyModifier(this.player);
                ItemStack itemstack = this.player.inventory.getCurrentItem();

                if (i > 0 && itemstack != null)
                {
                    float f1 = (float)(i * i + 1);
                    boolean canHarvest = ForgeHooks.canToolHarvestBlock(block, 0, itemstack);

                    if (!canHarvest && f <= 1.0F)
                    {
                        f += f1 * 0.08F;
                    }
                    else
                    {
                        f += f1;
                    }
                }
            }

            if (this.player.isPotionActive(Potion.digSpeed))
            {
                f *= 1.0F + (float)(this.player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
            }

            if (this.player.isPotionActive(Potion.digSlowdown))
            {
                f *= 1.0F - (float)(this.player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
            }

            f = ForgeEventFactory.getBreakSpeed(this.player, block, 0, f);
            return f < 0.0F ? 0.0F : f;
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return this.wearingNeptuneArmour() ? false : super.handleWaterMovement();
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return this.wearingPhoenixArmour() ? false : super.handleLavaMovement();
    }

    /**
     * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
     */
    public MovingObjectPosition rayTrace(double var1, float var3)
    {
        ItemStack stack = this.player.getCurrentEquippedItem();

        if (stack != null && stack.getItem() != null && this.extendedReachItems.contains(stack.getItem()))
        {
            var1 = 10.0D;
        }

        return this.player.rayTrace(var1, var3);
    }

    public int getAccessoryCount(int itemID)
    {
        int count = 0;

        for (int index = 0; index < 8; ++index)
        {
            if (this.inv.slots[index] != null && this.inv.slots[index].itemID == itemID)
            {
                ++count;
            }
        }

        return count;
    }

    public boolean wearingAccessory(int itemID)
    {
        for (int index = 0; index < 8; ++index)
        {
            if (this.inv.slots[index] != null && this.inv.slots[index].itemID == itemID)
            {
                return true;
            }
        }

        return false;
    }

    public void setSlotStack(int slotIndex, ItemStack stack)
    {
        this.inv.slots[slotIndex] = stack;
    }

    public ItemStack getSlotStack(int itemID)
    {
        ItemStack slot = null;

        for (int index = 0; index < 8; ++index)
        {
            if (this.inv.slots[index] != null && this.inv.slots[index].itemID == itemID)
            {
                slot = this.inv.slots[index];
                return slot;
            }
        }

        return slot;
    }

    public int getSlotIndex(int itemID)
    {
        for (int index = 0; index < 8; ++index)
        {
            if (this.inv.slots[index] != null && this.inv.slots[index].itemID == itemID)
            {
                return index;
            }
        }

        return 0;
    }

    public boolean wearingArmour(int itemID)
    {
        for (int index = 0; index < 4; ++index)
        {
            if (this.player.inventory.armorInventory[index] != null && this.player.inventory.armorInventory[index].itemID == itemID)
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

    public void setCoinAmount(int i)
    {
        this.coinAmount = i;

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

    public void setParachuting(boolean isParachuting, int parachuteType)
    {
        this.isParachuting = isParachuting;
        this.parachuteType = parachuteType;

        if (!isParachuting)
        {
            EntityItem item = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(Item.silk, this.rand.nextInt(3)));
            EntityItem block = new EntityItem(this.player.worldObj, this.player.posX, this.player.posY, this.player.posZ, new ItemStack(AetherBlocks.Aercloud, this.rand.nextInt(3), this.getType()));
            this.player.worldObj.spawnEntityInWorld(block);
            this.player.worldObj.spawnEntityInWorld(item);
        }

        if (!this.player.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, isParachuting, isParachuting, parachuteType, Collections.singleton(this.player.username)));
        }
    }

    public void setZephyrCooldown()
    {
        this.zephyrCoolDown = 120;
    }
}
