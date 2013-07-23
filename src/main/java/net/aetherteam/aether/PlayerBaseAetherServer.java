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
    private int aq;
    private boolean idleInPortal;
    public static int frostBiteTime;
    public static int frostBiteTimer;
    private Random ab = new Random();
    private int hitAmnt;
    public List extendedReachItems;
    private int ao;
    private boolean ap;
    private boolean isParachuting;
    private int parachuteType;

    public PlayerBaseAetherServer(MinecraftServer var1, World var2, String var3, ItemInWorldManager var4, int var5, PlayerCoreServer var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);
        this.player.bL = (Container)(!this.player.ce.isCreativeMode ? new ContainerPlayerAether(this.player.bK, this.inv, !this.player.q.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.bK, false, this.player));
        this.player.bM = this.player.bL;
    }

    public boolean a(DamageSource var1, int var2)
    {
        if (var1.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer var3 = (EntityPlayer)var1.getEntity();
            Party var4 = PartyController.instance().getParty(var3);
            Party var5 = PartyController.instance().getParty(PartyController.instance().getMember(this.player.bS));

            if (var4 != null && var5 != null && var4.getName().toLowerCase().equalsIgnoreCase(var5.getName()))
            {
                return false;
            }
        }

        return super.a(var1, var2);
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

        if (!this.player.q.isRemote)
        {
            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.bS)));
        }
    }

    public void removeCoins(int var1)
    {
        this.coinAmount -= var1;

        if (!this.player.q.isRemote)
        {
            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.bS)));
        }
    }

    public boolean g_()
    {
        return this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock() ? true : super.g_();
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.G;
    }

    public void a(EntityLightningBolt var1)
    {
        if (!(var1 instanceof EntityAetherLightning) || ((EntityAetherLightning)var1).playerUsing != this.player)
        {
            super.a(var1);
        }
    }

    public void c()
    {
        this.playerHandler.beforeOnLivingUpdate();

        if (this.playerHandler.riddenBy != null && this.playerHandler.riddenBy.shouldBeSitting())
        {
            if (this.playerHandler.riddenBy.animateSitting())
            {
                this.player.a(2, true);
                this.player.shouldRiderSit();
            }

            if (this.playerHandler.riddenBy.sprinting())
            {
                this.player.a(3, true);
            }
        }

        super.c();
    }

    public void q(Entity var1)
    {
        super.q(var1);

        if (var1 instanceof EntityLiving && ((EntityLiving)var1).deathTime <= 0 && !var1.isEntityAlive() && this.player.cd() != null && this.player.cd().itemID == AetherItems.SkyrootSword.itemID && var1 instanceof EntityLiving)
        {
            ;
        }
    }

    public void l_()
    {
        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.T = 0.0F;
        }

        int var2 = MathHelper.floor_double(this.player.u);
        int var3 = MathHelper.floor_double(this.player.v);
        int var4 = MathHelper.floor_double(this.player.w);

        if (this.isInBlock(AetherBlocks.ColdFire.blockID))
        {
            this.player.d(new PotionEffect(Potion.moveSlowdown.id, 10, 4));
        }

        if (!this.player.q.isRemote && this.player.q instanceof WorldServer)
        {
            ((WorldServer)this.player.q).getMinecraftServer();
            int var1 = this.player.y() + 1;

            if (this.ap)
            {
                if (this.player.o == null && this.aq == var1 && this.aq <= var1)
                {
                    this.ap = false;
                    this.aq = var1;
                    this.player.ao = this.player.aa();
                    Aether.teleportPlayerToAether(this.player, false);
                    this.aq = var1 + 5;
                }
                else
                {
                    ++this.aq;
                }
            }
            else
            {
                if (this.aq > 0 && this.aq <= var1)
                {
                    this.aq -= 4;
                }

                if (this.aq < 0)
                {
                    this.aq = 0;
                }
            }

            if (!this.idleInPortal)
            {
                this.aq = 0;
            }

            if (this.ao > 0)
            {
                ;
            }

            this.idleInPortal = this.isInBlock(AetherBlocks.AetherPortal.blockID);
        }

        Party var5 = PartyController.instance().getParty((EntityPlayer)this.player);
        Dungeon var6 = DungeonHandler.instance().getDungeon(var5);

        if (var6 != null)
        {
            if ((DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && !DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) || DungeonHandler.instance().getInstanceAt(var2, var3, var4) == null) && var6.getQueuedMembers().contains(PartyController.instance().getMember((EntityPlayer)this.player)) && var6.hasStarted() && this.player.q.provider.dimensionId == 3)
            {
                this.player.a(this.dungeonPosX, this.dungeonPosY, this.dungeonPosZ);
            }
            else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && DungeonHandler.instance().getInstanceAt(var2, var3, var4).equals(var6) && var6.hasStarted())
            {
                this.dungeonPosX = this.player.u;
                this.dungeonPosY = this.player.v;
                this.dungeonPosZ = this.player.w;
            }
        }
        else if (DungeonHandler.instance().getInstanceAt(var2, var3, var4) != null && this.player.q.provider.dimensionId == 3)
        {
            this.player.a(this.nondungeonPosX, this.nondungeonPosY, this.nondungeonPosZ);
        }
        else
        {
            this.nondungeonPosX = this.player.u;
            this.nondungeonPosY = this.player.v;
            this.nondungeonPosZ = this.player.w;
        }

        super.l_();
        this.processAbilities();
        int var7 = MathHelper.floor_double(this.player.u);
        int var8 = MathHelper.floor_double(this.player.v);
        int var9 = MathHelper.floor_double(this.player.w);

        if (var6 != null && !var6.hasMember(PartyController.instance().getMember((EntityPlayer)this.player)))
        {
            ;
        }

        if (this.prevCreative != this.player.ce.isCreativeMode)
        {
            System.out.println("hey");

            if (!this.player.ce.isCreativeMode)
            {
                ;
            }

            this.prevCreative = this.player.ce.isCreativeMode;
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.bS) != null)
        {
            this.maxHealth = ((Integer)Aether.proxy.getClientExtraHearts().get(this.player.bS)).intValue();
        }

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        PotionEffect var10 = this.player.b(Potion.regeneration);

        if (var10 != null && var10.getDuration() > 0 && Potion.potionTypes[var10.getPotionID()].isReady(var10.getDuration(), var10.getAmplifier()) && this.player.aX() >= 20 && this.player.aX() < this.maxHealth)
        {
            this.player.j(1);
        }

        if (this.player.cn().getFoodLevel() >= 18 && this.player.aX() >= 20 && this.player.aX() < this.maxHealth)
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                this.foodTimer = 0;
                this.player.j(1);
            }
        }
        else
        {
            this.foodTimer = 0;
        }

        if (this.player.q.difficultySetting == 0 && this.player.aX() >= 20 && this.player.aX() < this.maxHealth && this.player.ac % 20 == 0)
        {
            this.player.j(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity var11 = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(var11.posX - this.player.u, 2.0D) + Math.pow(var11.posY - this.player.v, 2.0D) + Math.pow(var11.posZ - this.player.w, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        ItemStack var15 = this.player.cd();

        if (var15 != null && var15.getItem() != null && this.extendedReachItems.contains(var15.getItem()))
        {
            this.player.c.setBlockReachDistance(10.0D);
        }
        else
        {
            this.player.c.setBlockReachDistance(5.0D);
        }

        if (this.player.ar == 3 && this.player.v < -2.0D)
        {
            Aether.teleportPlayerToAether(this.player, true);
        }

        this.playerHandler.afterOnUpdate();
        float var12 = this.player.getMoveForward();
        float var13 = this.player.getMoveStrafing();

        if (this.player.o != null && (var13 != 0.0F || var12 != 0.0F))
        {
            this.player.f(0.0F);
            this.player.setMoveStrafing(0.0F);
        }

        if (this.isParachuting)
        {
            Vec3 var14 = this.player.Y();

            switch (this.getParachuteType())
            {
                case 0:
                    this.player.x *= 0.6D;
                    this.player.y = -0.08D;
                    this.player.z *= 0.6D;
                    break;

                case 1:
                    this.player.x *= 0.6D;
                    this.player.y = -1.08D;
                    this.player.z *= 0.6D;
                    break;

                case 2:
                    this.player.x = var14.xCoord * 0.18000000715255737D;
                    this.player.y = -0.07999999821186066D;
                    this.player.z = var14.zCoord * 0.18000000715255737D;
                    break;

                case 3:
                    this.player.y = -0.07999999821186066D;
                    break;

                case 4:
                    this.player.x *= 0.6D;
                    this.player.y = 1.08D;
                    this.player.z *= 0.6D;

                    if (this.player.v >= (double)this.player.q.getActualHeight() || !this.player.q.isAirBlock(var7, var8 + 1, var9))
                    {
                        this.setParachuting(false, this.parachuteType);
                    }
            }

            this.player.ce.allowFlying = true;
            this.player.J = true;
            this.player.T = 0.0F;

            if ((this.getParachuteType() == 4 || this.player.q.isAirBlock(var7, var8 - 1, var9)) && (!this.isParachuting || this.hitAmnt < 4))
            {
                if (this.player.bs >= 4 && this.isParachuting && var14.yCoord >= 1.0D)
                {
                    System.out.println(this.hitAmnt);
                    ++this.hitAmnt;
                }
            }
            else
            {
                this.setParachuting(false, this.parachuteType);
                this.player.ce.allowFlying = this.player.ce.isCreativeMode;
                this.hitAmnt = 0;
            }
        }

        if (this.clientInfoChanged())
        {
            this.updatePlayerClientInfo();
        }
    }

    public void a(DamageSource var1)
    {
        float var2 = this.player.ch;
        int var3 = this.player.cg;
        int var4 = this.player.cf;

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
            this.player.ch = 0.0F;
            this.player.cf = 0;
        }

        var2 = this.player.ch;
        var3 = this.player.cg;
        var4 = this.player.cf;
        super.a(var1);
        PartyMember var11 = PartyController.instance().getMember((EntityPlayer)this.player);
        int var6 = MathHelper.floor_double(this.player.u);
        int var7 = MathHelper.floor_double(this.player.v);
        int var8 = MathHelper.floor_double(this.player.w);
        Dungeon var9 = DungeonHandler.instance().getInstanceAt(var6, var7, var8);
        System.out.println("Hooking into player death!");

        if (var11 != null && var9 != null)
        {
            Party var10 = PartyController.instance().getParty(var11);

            if (var10 != null && var9.isActive() && var9.isQueuedParty(var10))
            {
                this.player.M = false;
                this.player.b(this.maxHealth);
                this.player.x = this.player.y = this.player.z = 0.0D;
                this.player.setFoodStats(new FoodStats());
                this.player.a.sendPacketToPlayer(new Packet43Experience(var2, var3, var4));
                this.player.a((double)((float)((double)var9.getControllerX() + 0.5D)), (double)((float)((double)var9.getControllerY() + 1.0D)), (double)((float)((double)var9.getControllerZ() + 0.5D)));
                this.player.a.sendPacketToPlayer(AetherPacketHandler.sendDungeonRespawn(var9, var10));
                return;
            }
        }
    }

    public void a(Entity var1, int var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.a(var1, var2, var3, var5);
        }
    }

    public boolean isAboveBlock(int var1)
    {
        MathHelper.floor_double(this.player.u);
        int var2 = MathHelper.floor_double(this.player.E.minY);
        MathHelper.floor_double(this.player.w);
        return this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), var2 - 1, MathHelper.floor_double(this.player.E.minZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), var2 - 1, MathHelper.floor_double(this.player.E.minZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), var2 - 1, MathHelper.floor_double(this.player.E.maxZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), var2 - 1, MathHelper.floor_double(this.player.E.maxZ)) == var1;
    }

    public boolean isInBlock(int var1)
    {
        MathHelper.floor_double(this.player.u);
        int var2 = MathHelper.floor_double(this.player.v);
        MathHelper.floor_double(this.player.w);
        return this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), var2, MathHelper.floor_double(this.player.E.minZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), var2 + 1, MathHelper.floor_double(this.player.E.minZ)) == var1;
    }

    public boolean setGeneralCooldown(int var1, String var2)
    {
        if (this.generalcooldown == 0)
        {
            this.generalcooldown = var1;
            this.generalcooldownmax = var1;
            this.cooldownName = var2;

            if (!this.player.q.isRemote)
            {
                this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.generalcooldown, this.generalcooldownmax, this.cooldownName, Collections.singleton(this.player.bS)));
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
        PlayerClientInfo var1 = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(this.player.bS);
        return var1 == null ? false : this.player.aZ() != var1.getArmourValue() || this.player.cn().getFoodLevel() != var1.getHunger() || this.player.aX() != var1.getHalfHearts() || this.maxHealth != var1.getMaxHealth() || this.getCoins() != var1.getAetherCoins();
    }

    public void processAbilities()
    {
        if (!this.player.F)
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
            if (!this.player.F && this.player.y < 0.0D && !this.player.G() && !this.player.ag())
            {
                this.player.y *= 0.6D;
            }

            this.player.T = -1.0F;
        }

        if (this.player.ac % 400 == 0)
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
            this.player.A();
            this.player.d(new PotionEffect(Potion.fireResistance.id, 10, 4));
        }

        if (this.wearingGravititeArmour())
        {
            if (this.player.bG && !this.jumpBoosted && this.player.ag())
            {
                this.player.y = 1.0D;
                this.jumpBoosted = true;
            }

            this.player.T = -1.0F;
        }

        if (this.wearingObsidianArmour())
        {
            this.player.d(new PotionEffect(Potion.resistance.id, 10, 3));
            this.player.d(new PotionEffect(Potion.moveSlowdown.id, 10, 1));
        }

        if (this.wearingValkyrieArmour())
        {
            if (this.player.bG)
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
                        this.player.y = 0.025D * this.flightMod;
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

            this.player.T = -1.0F;
        }

        if (this.player.F)
        {
            this.flightCount = 0;
            this.flightMod = 1.0D;
        }

        if (!this.player.bG && this.player.F)
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
            this.player.Y = this.prevStepHeight;
        }
    }

    public void bl()
    {
        if (this.playerHandler.jump())
        {
            super.bl();
        }
    }

    public float bE()
    {
        float var1 = this.playerHandler.getSpeedModifier();
        return var1 == -1.0F ? var1 : super.bE();
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    public void j(int var1)
    {
        if (this.player.aX() > 0)
        {
            this.player.b(this.player.aX() + var1);

            if (this.player.aX() > this.maxHealth)
            {
                this.player.b(this.maxHealth);
            }

            this.updatePlayerClientInfo();
        }
    }

    public void updatePlayerClientInfo()
    {
        if (!this.player.q.isRemote)
        {
            MinecraftServer var1 = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager var2 = var1.getConfigurationManager();
            PlayerClientInfo var3 = new PlayerClientInfo(this.player.aX(), this.maxHealth, this.player.cn().getFoodLevel(), this.player.aZ(), this.getCoins());
            Aether.proxy.getPlayerClientInfo().put(this.player.bS, var3);
            PartyMember var4 = PartyController.instance().getMember(this.player.bS);
            PartyController.instance().getParty(var4);

            for (int var5 = 0; var5 < var2.playerEntityList.size(); ++var5)
            {
                EntityPlayerMP var6 = (EntityPlayerMP)var2.playerEntityList.get(var5);
                PartyController.instance().getMember((EntityPlayer)var6);
                var6.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.bS, var3));
            }

            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.bS, var3));
        }
    }

    public void increaseMaxHP(int var1)
    {
        if (this.maxHealth <= 40 - var1)
        {
            if (!this.player.q.isRemote)
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth + var1, Collections.singleton(this.player.bS)));
            }

            this.maxHealth += var1;
            this.player.b(this.player.aX() + var1);
        }
    }

    public void b(NBTTagCompound var1)
    {
        var1.setInteger("MaxHealth", this.maxHealth);
        var1.setTag("AetherInventory", this.inv.writeToNBT(new NBTTagList()));
        var1.setBoolean("HasDefeatedSunSpirit", this.hasDefeatedSunSpirit);
        var1.setBoolean("inAether", this.player.ar == 3);
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
        super.b(var1);
    }

    public void a(NBTTagCompound var1)
    {
        if (!this.player.q.isRemote)
        {
            File var2 = new File(((SaveHandler)this.player.q.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

            if (var2.exists())
            {
                new NBTTagCompound();

                try
                {
                    NBTTagCompound var3 = CompressedStreamTools.readCompressed(new FileInputStream(var2));
                    this.maxHealth = var3.getInteger("MaxHealth");
                    NBTTagList var4 = var3.getTagList("Inventory");

                    if (this.player.ar == 3)
                    {
                        this.player.ar = 3;
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
                    this.player.ar = 3;
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

        super.a(var1);
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

    public float a(Block var1, boolean var2)
    {
        ItemStack var3 = this.player.bK.getCurrentItem();
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
            return var4 == -1.0F ? super.a(var1, var2) : var4;
        }
        else
        {
            if (var4 > 1.0F)
            {
                int var5 = EnchantmentHelper.getEfficiencyModifier(this.player);
                ItemStack var6 = this.player.bK.getCurrentItem();

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

            if (this.player.a(Potion.digSpeed))
            {
                var4 *= 1.0F + (float)(this.player.b(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
            }

            if (this.player.a(Potion.digSlowdown))
            {
                var4 *= 1.0F - (float)(this.player.b(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
            }

            var4 = ForgeEventFactory.getBreakSpeed(this.player, var1, 0, var4);
            return var4 < 0.0F ? 0.0F : var4;
        }
    }

    public boolean G()
    {
        return this.wearingNeptuneArmour() ? false : super.G();
    }

    public MovingObjectPosition a(double var1, float var3)
    {
        ItemStack var4 = this.player.cd();

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
            if (this.player.bK.armorInventory[var2] != null && this.player.bK.armorInventory[var2].itemID == var1)
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

        if (!this.player.q.isRemote)
        {
            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.bS)));
        }
    }

    public void Z()
    {
        MathHelper.floor_double(this.player.u);
        MathHelper.floor_double(this.player.v);
        MathHelper.floor_double(this.player.w);

        if (this.ao > 0)
        {
            this.ao = 900;
        }
        else
        {
            this.ap = true;
        }
    }

    public boolean isInPortal()
    {
        return this.ap;
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
            EntityItem var3 = new EntityItem(this.player.q, this.player.u, this.player.v, this.player.w, new ItemStack(Item.silk, this.ab.nextInt(3)));
            EntityItem var4 = new EntityItem(this.player.q, this.player.u, this.player.v, this.player.w, new ItemStack(AetherBlocks.Aercloud, this.ab.nextInt(3), this.getType()));
            this.player.q.spawnEntityInWorld(var4);
            this.player.q.spawnEntityInWorld(var3);
        }

        if (!this.player.q.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, var1, var1, var2, Collections.singleton(this.player.bS)));
        }
    }
}
