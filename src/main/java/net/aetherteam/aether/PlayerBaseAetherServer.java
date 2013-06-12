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
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class PlayerBaseAetherServer extends ServerPlayerBase
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
    private float zLevel = -90.0F;
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
    public List extendedReachItems;
    private int timeUntilPortal;
    private boolean inPortal;

    public PlayerBaseAetherServer(ServerPlayerAPI var1)
    {
        super(var1);
        this.extendedReachItems = Arrays.asList(new Item[]{AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);
        this.player.inventoryContainer = (Container) (!this.player.capabilities.isCreativeMode ? new ContainerPlayerAether(this.player.inventory, this.inv, !this.player.worldObj.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.inventory, false, this.player));
        this.player.openContainer = this.player.inventoryContainer;
    }

    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var3 = (EntityPlayer) var1.getEntity();
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

    public boolean isOnLadder()
    {
        return this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock() ? true : super.isOnLadder();
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.isCollidedHorizontally;
    }

    public void onStruckByLightning(EntityLightningBolt var1)
    {
        if (!(var1 instanceof EntityAetherLightning) || ((EntityAetherLightning) var1).playerUsing != this.player)
        {
            super.onStruckByLightning(var1);
        }
    }

    public void beforeOnLivingUpdate()
    {
        this.playerHandler.beforeOnLivingUpdate();

        if (this.playerHandler.riddenBy != null && this.playerHandler.riddenBy.shouldBeSitting())
        {
            if (this.playerHandler.riddenBy.animateSitting())
            {
                this.player.superSetFlag(2, true);
                this.player.shouldRiderSit();
            }

            if (this.playerHandler.riddenBy.sprinting())
            {
                this.player.superSetFlag(3, true);
            }
        }
    }

    public void afterAttackTargetEntityWithCurrentItem(Entity var1)
    {
        if (var1 instanceof EntityLiving && ((EntityLiving) var1).deathTime <= 0 && !var1.isEntityAlive() && this.player.getCurrentEquippedItem() != null && this.player.getCurrentEquippedItem().itemID == AetherItems.SkyrootSword.itemID && var1 instanceof EntityLiving)
        {
            ;
        }
    }

    public void beforeOnUpdate()
    {
        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.fallDistance = 0.0F;
        }

        int var2 = MathHelper.floor_double(this.player.posX);
        int var3 = MathHelper.floor_double(this.player.posY);
        int var4 = MathHelper.floor_double(this.player.posZ);

        if (!this.player.worldObj.isRemote && this.player.worldObj instanceof WorldServer)
        {
            MinecraftServer var5 = ((WorldServer) this.player.worldObj).getMinecraftServer();
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
                } else
                {
                    ++this.timeInPortal;
                }
            } else
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

        Party var7 = PartyController.instance().getParty((EntityPlayer) this.player);
        Dungeon var6 = DungeonHandler.instance().getDungeon(var7);

        if (var6 != null)
        {
            if ((DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && !DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) || DungeonHandler.instance().getInstanceAt(var2, var3, var4) == null) && var6.getQueuedMembers().contains(PartyController.instance().getMember((EntityPlayer) this.player)) && var6.hasStarted() && this.player.worldObj.provider.dimensionId == 3)
            {
                this.player.setPositionAndUpdate(this.dungeonPosX, this.dungeonPosY, this.dungeonPosZ);
            } else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) && var6.hasStarted())
            {
                this.dungeonPosX = this.player.posX;
                this.dungeonPosY = this.player.posY;
                this.dungeonPosZ = this.player.posZ;
            }
        } else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && this.player.worldObj.provider.dimensionId == 3)
        {
            this.player.setPositionAndUpdate(this.nondungeonPosX, this.nondungeonPosY, this.nondungeonPosZ);
        } else
        {
            this.nondungeonPosX = this.player.posX;
            this.nondungeonPosY = this.player.posY;
            this.nondungeonPosZ = this.player.posZ;
        }
    }

    public void onDeath(DamageSource var1)
    {
        float var2 = this.player.experience;
        int var3 = this.player.experienceTotal;
        int var4 = this.player.experienceLevel;
        super.onDeath(var1);
        PartyMember var5 = PartyController.instance().getMember((EntityPlayer) this.player);
        int var6 = MathHelper.floor_double(this.player.posX);
        int var7 = MathHelper.floor_double(this.player.posY);
        int var8 = MathHelper.floor_double(this.player.posZ);
        Dungeon var9 = DungeonHandler.instance().getInstanceAt(var6, var7, var8);
        System.out.println("Hooking into player death!");

        if (var5 != null && var9 != null)
        {
            System.out.println("Hooking into player death, preventing! ONE");
            Party var10 = PartyController.instance().getParty(var5);

            if (var10 != null && var9.isActive() && var9.isQueuedParty(var10))
            {
                System.out.println("Hooking into player death, preventing! TWO");
                this.player.isDead = false;
                this.player.setEntityHealth(this.maxHealth);
                this.player.setFoodStatsField(new FoodStats());
                this.player.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(var2, var3, var4));
                this.player.setPositionAndUpdate((double) ((float) ((double) var9.getControllerX() + 0.5D)), (double) ((float) ((double) var9.getControllerY() + 1.0D)), (double) ((float) ((double) var9.getControllerZ() + 0.5D)));
                this.player.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendDungeonRespawn(var9, var10));
                return;
            }
        }
    }

    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public boolean isAboveBlock(int var1)
    {
        int var2 = MathHelper.floor_double(this.player.posX);
        int var3 = MathHelper.floor_double(this.player.boundingBox.minY);
        int var4 = MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var3 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var3 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var3 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var3 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1;
    }

    public boolean isInBlock(int var1)
    {
        int var2 = MathHelper.floor_double(this.player.posX);
        int var3 = MathHelper.floor_double(this.player.posY);
        int var4 = MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var3, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var3 + 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1;
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
        } else
        {
            return false;
        }
    }

    public void afterOnUpdate()
    {
        this.processAbilities();
        int var1 = MathHelper.floor_double(this.player.posX);
        int var2 = MathHelper.floor_double(this.player.posY);
        int var3 = MathHelper.floor_double(this.player.posZ);
        Dungeon var4 = DungeonHandler.instance().getInstanceAt(var1, var2, var3);

        if (var4 != null && !var4.hasMember(PartyController.instance().getMember((EntityPlayer) this.player)))
        {
            ;
        }

        if (this.prevCreative != this.player.capabilities.isCreativeMode)
        {
            // System.out.println("hey"); // hey yo, do not spam my console

            if (!this.player.capabilities.isCreativeMode)
            {
                ;
            }

            this.prevCreative = this.player.capabilities.isCreativeMode;
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.username) != null)
        {
            this.maxHealth = ((Integer) Aether.proxy.getClientExtraHearts().get(this.player.username)).intValue();
        }

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        PotionEffect var5 = this.player.getActivePotionEffect(Potion.regeneration);

        if (var5 != null && var5.getDuration() > 0 && Potion.potionTypes[var5.getPotionID()].isReady(var5.getDuration(), var5.getAmplifier()) && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth)
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
        } else
        {
            this.foodTimer = 0;
        }

        if (this.player.worldObj.difficultySetting == 0 && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth && this.player.ticksExisted % 20 == 0)
        {
            this.player.heal(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity var6 = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(var6.posX - this.player.posX, 2.0D) + Math.pow(var6.posY - this.player.posY, 2.0D) + Math.pow(var6.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss) null);
            }
        }

        ItemStack var9 = this.player.getCurrentEquippedItem();

        if (var9 != null && var9.getItem() != null && this.extendedReachItems.contains(var9.getItem()))
        {
            this.player.theItemInWorldManager.setBlockReachDistance(10.0D);
        } else
        {
            this.player.theItemInWorldManager.setBlockReachDistance(5.0D);
        }

        if (this.player.dimension == 3 && this.player.posY < -2.0D)
        {
            Aether.teleportPlayerToAether(this.player, true);
        }

        this.playerHandler.afterOnUpdate();
        float var7 = this.player.getMoveForwardField();
        float var8 = this.player.getMoveStrafingField();

        if (this.player.ridingEntity != null && (var8 != 0.0F || var7 != 0.0F))
        {
            this.player.setMoveForwardField(0.0F);
            this.player.setMoveStrafingField(0.0F);
        }

        if (this.clientInfoChanged())
        {
            this.updatePlayerClientInfo();
        }
    }

    public boolean clientInfoChanged()
    {
        PlayerClientInfo var1 = (PlayerClientInfo) Aether.proxy.getPlayerClientInfo().get(this.player.username);
        return var1 == null ? false : this.player.getTotalArmorValue() != var1.getArmourValue() || this.player.getFoodStats().getFoodLevel() != var1.getHunger() || this.player.getHealth() != var1.getHalfHearts() || this.maxHealth != var1.getMaxHealth() || this.getCoins() != var1.getAetherCoins();
    }

    public void processAbilities()
    {
        if (!this.player.onGround)
        {
            this.sinage += 0.75F;
        } else
        {
            this.sinage += 0.15F;
        }

        if (this.sinage > ((float) Math.PI * 2F))
        {
            this.sinage -= ((float) Math.PI * 2F);
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
                } else
                {
                    ++this.flightCount;
                }
            } else
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
                ((ItemAccessory) this.inv.slots[var1].getItem()).activateServerPassive(this.player, this);
            }
        }

        if (!this.wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.stepHeight = this.prevStepHeight;
        }
    }

    public void jump()
    {
        if (this.playerHandler.jump())
        {
            super.jump();
        }
    }

    public float getSpeedModifier()
    {
        float var1 = this.playerHandler.getSpeedModifier();
        return var1 == -1.0F ? var1 : super.getSpeedModifier();
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

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
            Party var5 = PartyController.instance().getParty(var4);

            for (int var6 = 0; var6 < var2.playerEntityList.size(); ++var6)
            {
                EntityPlayerMP var7 = (EntityPlayerMP) var2.playerEntityList.get(var6);
                PartyMember var8 = PartyController.instance().getMember((EntityPlayer) var7);
                var7.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.username, var3));
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

    public void beforeWriteEntityToNBT(NBTTagCompound var1)
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
    }

    public void beforeReadEntityFromNBT(NBTTagCompound var1)
    {
        if (!this.player.worldObj.isRemote)
        {
            File var2 = new File(((SaveHandler) this.player.worldObj.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

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
                } catch (IOException var5)
                {
                    ;
                }
            } else
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

    public float getCurrentPlayerStrVsBlock(Block var1, boolean var2)
    {
        ItemStack var3 = this.player.inventory.getCurrentItem();
        float var4 = var3 == null ? 1.0F : var3.getItem().getStrVsBlock(var3, var1, 0);

        if (this.inv.slots[0] != null && this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID)
        {
            var4 *= 1.0F + (float) this.inv.slots[0].getItemDamage() / ((float) this.inv.slots[0].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[4] != null && this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID)
        {
            var4 *= 1.0F + (float) this.inv.slots[4].getItemDamage() / ((float) this.inv.slots[4].getMaxDamage() * 3.0F);
        }

        if (this.inv.slots[5] != null && this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID)
        {
            var4 *= 1.0F + (float) this.inv.slots[5].getItemDamage() / ((float) this.inv.slots[5].getMaxDamage() * 3.0F);
        }

        if (!this.wearingNeptuneArmour())
        {
            return var4 == -1.0F ? super.getCurrentPlayerStrVsBlock(var1, var2) : var4;
        } else
        {
            if (var4 > 1.0F)
            {
                int var5 = EnchantmentHelper.getEfficiencyModifier(this.player);
                ItemStack var6 = this.player.inventory.getCurrentItem();

                if (var5 > 0 && var6 != null)
                {
                    float var7 = (float) (var5 * var5 + 1);
                    boolean var8 = ForgeHooks.canToolHarvestBlock(var1, 0, var6);

                    if (!var8 && var4 <= 1.0F)
                    {
                        var4 += var7 * 0.08F;
                    } else
                    {
                        var4 += var7;
                    }
                }
            }

            if (this.player.isPotionActive(Potion.digSpeed))
            {
                var4 *= 1.0F + (float) (this.player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
            }

            if (this.player.isPotionActive(Potion.digSlowdown))
            {
                var4 *= 1.0F - (float) (this.player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
            }

            var4 = ForgeEventFactory.getBreakSpeed(this.player, var1, 0, var4);
            return var4 < 0.0F ? 0.0F : var4;
        }
    }

    public boolean isInWater()
    {
        return this.wearingNeptuneArmour() ? false : super.isInWater();
    }

    public MovingObjectPosition rayTrace(double var1, float var3)
    {
        ItemStack var4 = this.player.getCurrentEquippedItem();

        if (var4 != null && var4.getItem() != null && this.extendedReachItems.contains(var4.getItem()))
        {
            var1 = 10.0D;
        }

        return this.player.superRayTrace(var1, var3);
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

    public void setInPortal()
    {
        int var1 = MathHelper.floor_double(this.player.posX);
        int var2 = MathHelper.floor_double(this.player.posY);
        int var3 = MathHelper.floor_double(this.player.posZ);

        if (this.timeUntilPortal > 0)
        {
            this.timeUntilPortal = 900;
        } else
        {
            double var10000 = this.player.prevPosX - this.player.posX;
            var10000 = this.player.prevPosZ - this.player.posZ;
            this.inPortal = true;
        }
    }

    public boolean isInPortal()
    {
        return this.inPortal;
    }
}
