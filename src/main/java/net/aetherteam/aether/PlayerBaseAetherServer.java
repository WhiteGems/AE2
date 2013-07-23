package net.aetherteam.aether;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.containers.ContainerPlayerAether;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.data.PlayerClientInfo;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
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
    public List extendedReachItems = Arrays.asList(new Item[] { AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe });
    private int ao;
    private boolean ap;
    private boolean isParachuting;
    private int parachuteType;

    public PlayerBaseAetherServer(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager, int playerCoreIndex, PlayerCoreServer entityPlayerMP)
    {
        super(par1MinecraftServer, par2World, par3Str, par4ItemInWorldManager, playerCoreIndex, entityPlayerMP);
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);

        if (!this.player.q.isRemote)
        {
            tmpTernaryOp = 1;
        }

        this.player.bL = (!this.player.ce.isCreativeMode ? new ContainerPlayerAether(this.player.bK, this.inv, false, this.player, this.playerHandler) : new ContainerPlayer(this.player.bK, false, this.player));
        this.player.bM = this.player.bL;
    }

    public boolean a(DamageSource var1, int var2)
    {
        if ((var1.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer attackingPlayer = (EntityPlayer)var1.getEntity();
            Party attackingParty = PartyController.instance().getParty(attackingPlayer);
            Party receivingParty = PartyController.instance().getParty(PartyController.instance().getMember(this.player.bS));

            if ((attackingParty != null) && (receivingParty != null) && (attackingParty.getName().toLowerCase().equalsIgnoreCase(receivingParty.getName())))
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

    public void addCoins(int amount)
    {
        this.coinAmount += amount;

        if (!this.player.q.isRemote)
        {
            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.bS)));
        }
    }

    public void removeCoins(int amount)
    {
        this.coinAmount -= amount;

        if (!this.player.q.isRemote)
        {
            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCoinChange(false, true, this.coinAmount, Collections.singleton(this.player.bS)));
        }
    }

    public boolean g_()
    {
        if ((wearingAccessory(AetherItems.SwettyPendant.itemID)) && (isBesideClimbableBlock()))
        {
            return true;
        }

        return super.g_();
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.G;
    }

    public void a(EntityLightningBolt var1)
    {
        if ((var1 instanceof EntityAetherLightning))
        {
            if (((EntityAetherLightning)var1).playerUsing == this.player)
            {
                return;
            }
        }

        super.a(var1);
    }

    public void c()
    {
        this.playerHandler.beforeOnLivingUpdate();

        if ((this.playerHandler.riddenBy != null) && (this.playerHandler.riddenBy.shouldBeSitting()))
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

    public void q(Entity ent)
    {
        super.q(ent);

        if ((ent instanceof EntityLiving))
        {
            if ((((EntityLiving)ent).deathTime > 0) || (ent.isEntityAlive()) || (this.player.cd() == null) || (this.player.cd().itemID != AetherItems.SkyrootSword.itemID) || (!(ent instanceof EntityLiving)));
        }
    }

    public void l_()
    {
        if (isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.T = 0.0F;
        }

        int posX = MathHelper.floor_double(this.player.u);
        int posY = MathHelper.floor_double(this.player.v);
        int posZ = MathHelper.floor_double(this.player.w);

        if (isInBlock(AetherBlocks.ColdFire.blockID))
        {
            this.player.d(new PotionEffect(Potion.moveSlowdown.id, 10, 4));
        }

        if ((!this.player.q.isRemote) && ((this.player.q instanceof WorldServer)))
        {
            ((WorldServer)this.player.q).p();
            int i = this.player.y() + 1;

            if (this.ap)
            {
                if ((this.player.o == null) && (this.aq == i) && (this.aq <= i))
                {
                    this.ap = false;
                    this.aq = i;
                    this.player.ao = this.player.aa();
                    Aether.teleportPlayerToAether(this.player, false);
                    this.aq = (i + 5);
                }
                else
                {
                    this.aq += 1;
                }
            }
            else
            {
                if ((this.aq > 0) && (this.aq <= i))
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

            if (this.ao > 0);

            this.idleInPortal = isInBlock(AetherBlocks.AetherPortal.blockID);
        }

        Party party = PartyController.instance().getParty(this.player);
        Dungeon dungeon = DungeonHandler.instance().getDungeon(party);

        if (dungeon != null)
        {
            if (((DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null) && (!DungeonHandler.instance().getInstanceAt(posX, posY, posZ).equals(dungeon))) || ((DungeonHandler.instance().getInstanceAt(posX, posY, posZ) == null) && (dungeon.getQueuedMembers().contains(PartyController.instance().getMember(this.player))) && (dungeon.hasStarted()) && (this.player.q.provider.dimensionId == 3)))
            {
                this.player.a(this.dungeonPosX, this.dungeonPosY, this.dungeonPosZ);
            }
            else if ((DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null) && (DungeonHandler.instance().getInstanceAt(posX, posY, posZ).equals(dungeon)) && (dungeon.hasStarted()))
            {
                this.dungeonPosX = this.player.u;
                this.dungeonPosY = this.player.v;
                this.dungeonPosZ = this.player.w;
            }
        }
        else if ((DungeonHandler.instance().getInstanceAt(posX, posY, posZ) != null) && (this.player.q.provider.dimensionId == 3))
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
        processAbilities();
        int x = MathHelper.floor_double(this.player.u);
        int y = MathHelper.floor_double(this.player.v);
        int z = MathHelper.floor_double(this.player.w);

        if (((dungeon == null) || (dungeon.hasMember(PartyController.instance().getMember(this.player)))) ||
                (this.prevCreative != this.player.ce.isCreativeMode))
        {
            System.out.println("hey");

            if (!this.player.ce.isCreativeMode);

            this.prevCreative = this.player.ce.isCreativeMode;
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.bS) != null)
        {
            this.maxHealth = ((Integer)Aether.proxy.getClientExtraHearts().get(this.player.bS)).intValue();
        }

        if (this.generalcooldown > 0)
        {
            this.generalcooldown -= 1;
        }

        PotionEffect effect = this.player.b(Potion.regeneration);

        if ((effect != null) && (effect.getDuration() > 0) && (Potion.potionTypes[effect.getPotionID()].isReady(effect.getDuration(), effect.getAmplifier())) && (this.player.aX() >= 20) && (this.player.aX() < this.maxHealth))
        {
            this.player.j(1);
        }

        if ((this.player.cn().getFoodLevel() >= 18) && (this.player.aX() >= 20) && (this.player.aX() < this.maxHealth))
        {
            this.foodTimer += 1;

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

        if ((this.player.q.difficultySetting == 0) && (this.player.aX() >= 20) && (this.player.aX() < this.maxHealth) && (this.player.ac % 20 == 0))
        {
            this.player.j(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity boss = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(boss.posX - this.player.u, 2.0D) + Math.pow(boss.posY - this.player.v, 2.0D) + Math.pow(boss.posZ - this.player.w, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss(null);
            }
        }

        ItemStack stack = this.player.cd();

        if ((stack != null) && (stack.getItem() != null) && (this.extendedReachItems.contains(stack.getItem())))
        {
            this.player.c.setBlockReachDistance(10.0D);
        }
        else
        {
            this.player.c.setBlockReachDistance(5.0D);
        }

        if ((this.player.ar == 3) && (this.player.v < -2.0D))
        {
            Aether.teleportPlayerToAether(this.player, true);
        }

        this.playerHandler.afterOnUpdate();
        float forward = this.player.getMoveForward();
        float strafe = this.player.getMoveStrafing();

        if (this.player.o != null)
        {
            if ((strafe != 0.0F) || (forward != 0.0F))
            {
                this.player.f(0.0F);
                this.player.setMoveStrafing(0.0F);
            }
        }

        if (this.isParachuting)
        {
            Vec3 vec3 = this.player.Y();

            switch (getParachuteType())
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
                    this.player.x = (vec3.xCoord * 0.1800000071525574D);
                    this.player.y = -0.07999999821186066D;
                    this.player.z = (vec3.zCoord * 0.1800000071525574D);
                    break;

                case 3:
                    this.player.y = -0.07999999821186066D;
                    break;

                case 4:
                    this.player.x *= 0.6D;
                    this.player.y = 1.08D;
                    this.player.z *= 0.6D;

                    if ((this.player.v >= this.player.q.R()) || (!this.player.q.isAirBlock(x, y + 1, z)))
                    {
                        setParachuting(false, this.parachuteType);
                    }

                    break;
            }

            this.player.ce.allowFlying = true;
            this.player.J = true;
            this.player.T = 0.0F;

            if (((getParachuteType() != 4) && (!this.player.q.isAirBlock(x, y - 1, z))) || ((this.isParachuting) && (this.hitAmnt >= 4)))
            {
                setParachuting(false, this.parachuteType);
                this.player.ce.allowFlying = this.player.ce.isCreativeMode;
                this.hitAmnt = 0;
            }
            else if ((this.player.bs >= 4) && (this.isParachuting) && (vec3.yCoord >= 1.0D))
            {
                System.out.println(this.hitAmnt);
                this.hitAmnt += 1;
            }
        }

        if (clientInfoChanged())
        {
            updatePlayerClientInfo();
        }
    }

    public void a(DamageSource source)
    {
        float exp = this.player.ch;
        int expTotal = this.player.cg;
        int expLevel = this.player.cf;

        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == AetherItems.CrystalBottle.itemID))
            {
                if (this.inv.slots[index].getTagCompound() != null)
                {
                    this.inv.slots[index].getTagCompound().setFloat("Experience", this.inv.slots[index].getTagCompound().getFloat("Experience") + expLevel * 2 + Math.round(exp * 10.0F) * 0.1F);
                }
                else
                {
                    this.inv.slots[index].setTagCompound(new NBTTagCompound());
                    this.inv.slots[index].getTagCompound().setFloat("Experience", this.inv.slots[index].getTagCompound().getFloat("Experience") + expLevel * 2 + Math.round(exp * 10.0F) * 0.1F);
                }
            }
            else if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == AetherItems.PiggieBank.itemID))
            {
                if (this.inv.slots[index].getTagCompound() != null)
                {
                    this.inv.slots[index].getTagCompound().setInteger("Coins", this.inv.slots[index].getTagCompound().getInteger("Coins") + getCoins());
                }
                else
                {
                    this.inv.slots[index].setTagCompound(new NBTTagCompound());
                    this.inv.slots[index].getTagCompound().setInteger("Coins", this.inv.slots[index].getTagCompound().getInteger("Coins") + getCoins());
                }
            }
        }

        if (wearingAccessory(AetherItems.PiggieBank.itemID))
        {
            setCoinAmount(0);
        }

        if (wearingAccessory(AetherItems.CrystalBottle.itemID))
        {
            this.player.ch = 0.0F;
            this.player.cf = 0;
        }

        exp = this.player.ch;
        expTotal = this.player.cg;
        expLevel = this.player.cf;
        super.a(source);
        PartyMember member = PartyController.instance().getMember(this.player);
        int x = MathHelper.floor_double(this.player.u);
        int y = MathHelper.floor_double(this.player.v);
        int z = MathHelper.floor_double(this.player.w);
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(x, y, z);
        System.out.println("Hooking into player death!");

        if ((member != null) && (dungeon != null))
        {
            Party party = PartyController.instance().getParty(member);

            if ((party != null) && (dungeon.isActive()) && (dungeon.isQueuedParty(party)))
            {
                this.player.M = false;
                this.player.b(this.maxHealth);
                this.player.x = (this.player.y = this.player.z = 0.0D);
                this.player.setFoodStats(new FoodStats());
                this.player.a.sendPacketToPlayer(new Packet43Experience(exp, expTotal, expLevel));
                this.player.a((float)(dungeon.getControllerX() + 0.5D), (float)(dungeon.getControllerY() + 1.0D), (float)(dungeon.getControllerZ() + 0.5D));
                this.player.a.sendPacketToPlayer(AetherPacketHandler.sendDungeonRespawn(dungeon, party));
                return;
            }
        }
    }

    public void a(Entity var1, int var2, double var3, double var5)
    {
        if (!wearingObsidianArmour())
        {
            super.a(var1, var2, var3, var5);
        }
    }

    public boolean isAboveBlock(int blockID)
    {
        MathHelper.floor_double(this.player.u);
        int y = MathHelper.floor_double(this.player.E.minY);
        MathHelper.floor_double(this.player.w);
        return (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), y - 1, MathHelper.floor_double(this.player.E.minZ)) == blockID) || (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), y - 1, MathHelper.floor_double(this.player.E.minZ)) == blockID) || (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), y - 1, MathHelper.floor_double(this.player.E.maxZ)) == blockID) || (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), y - 1, MathHelper.floor_double(this.player.E.maxZ)) == blockID);
    }

    public boolean isInBlock(int blockID)
    {
        MathHelper.floor_double(this.player.u);
        int y = MathHelper.floor_double(this.player.v);
        MathHelper.floor_double(this.player.w);
        return (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), y, MathHelper.floor_double(this.player.E.minZ)) == blockID) || (this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), y + 1, MathHelper.floor_double(this.player.E.minZ)) == blockID);
    }

    public boolean setGeneralCooldown(int cooldown, String stackName)
    {
        if (this.generalcooldown == 0)
        {
            this.generalcooldown = cooldown;
            this.generalcooldownmax = cooldown;
            this.cooldownName = stackName;

            if (!this.player.q.isRemote)
            {
                this.player.a.sendPacketToPlayer(AetherPacketHandler.sendCooldown(false, true, this.generalcooldown, this.generalcooldownmax, this.cooldownName, Collections.singleton(this.player.bS)));
            }

            return true;
        }

        return false;
    }

    public boolean clientInfoChanged()
    {
        PlayerClientInfo playerClientInfo = (PlayerClientInfo)Aether.proxy.getPlayerClientInfo().get(this.player.bS);

        if (playerClientInfo != null)
        {
            return (this.player.aZ() != playerClientInfo.getArmourValue()) || (this.player.cn().getFoodLevel() != playerClientInfo.getHunger()) || (this.player.aX() != playerClientInfo.getHalfHearts()) || (this.maxHealth != playerClientInfo.getMaxHealth()) || (getCoins() != playerClientInfo.getAetherCoins());
        }

        return false;
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

        if ((wearingAccessory(AetherItems.SwettyPendant.itemID)) && (isBesideClimbableBlock()))
        {
            if ((!this.player.F) && (this.player.y < 0.0D) && (!this.player.G()) && (!this.player.ag()))
            {
                this.player.y *= 0.6D;
            }

            this.player.T = -1.0F;
        }

        if (this.player.ac % 400 == 0)
        {
            if ((this.inv.slots[0] != null) && (this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID))
            {
                this.inv.slots[0].damageItem(1, this.player);

                if (this.inv.slots[0].stackSize < 1)
                {
                    this.inv.slots[0] = null;
                }
            }

            if ((this.inv.slots[4] != null) && (this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID))
            {
                this.inv.slots[4].damageItem(1, this.player);

                if (this.inv.slots[4].stackSize < 1)
                {
                    this.inv.slots[4] = null;
                }
            }

            if ((this.inv.slots[5] != null) && (this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID))
            {
                this.inv.slots[5].damageItem(1, this.player);

                if (this.inv.slots[5].stackSize < 1)
                {
                    this.inv.slots[5] = null;
                }
            }
        }

        if (wearingPhoenixArmour())
        {
            this.player.A();
            this.player.d(new PotionEffect(Potion.fireResistance.id, 10, 4));
        }

        if (wearingGravititeArmour())
        {
            if ((this.player.bG) && (!this.jumpBoosted) && (this.player.ag()))
            {
                this.player.y = 1.0D;
                this.jumpBoosted = true;
            }

            this.player.T = -1.0F;
        }

        if (wearingObsidianArmour())
        {
            this.player.d(new PotionEffect(Potion.resistance.id, 10, 3));
            this.player.d(new PotionEffect(Potion.moveSlowdown.id, 10, 1));
        }

        if (wearingValkyrieArmour())
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
                        this.player.y = (0.025D * this.flightMod);
                        this.flightCount += 1;
                    }
                }
                else
                {
                    this.flightCount += 1;
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

        if ((!this.player.bG) && (this.player.F))
        {
            this.jumpBoosted = false;
        }

        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && ((this.inv.slots[index].getItem() instanceof IAetherAccessory)))
            {
                ((ItemAccessory)this.inv.slots[index].getItem()).activateServerPassive(this.player, this);
            }
        }

        if (!wearingAccessory(AetherItems.AgilityCape.itemID))
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
        float speed = this.playerHandler.getSpeedModifier();
        return speed == -1.0F ? speed : super.bE();
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    public void j(int i)
    {
        if (this.player.aX() <= 0)
        {
            return;
        }

        this.player.b(this.player.aX() + i);

        if (this.player.aX() > this.maxHealth)
        {
            this.player.b(this.maxHealth);
        }

        updatePlayerClientInfo();
    }

    public void updatePlayerClientInfo()
    {
        if (!this.player.q.isRemote)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            ServerConfigurationManager configManager = server.getConfigurationManager();
            PlayerClientInfo playerClientInfo = new PlayerClientInfo(this.player.aX(), this.maxHealth, this.player.cn().getFoodLevel(), this.player.aZ(), getCoins());
            Aether.proxy.getPlayerClientInfo().put(this.player.bS, playerClientInfo);
            PartyMember member = PartyController.instance().getMember(this.player.bS);
            PartyController.instance().getParty(member);

            for (int playerAmount = 0; playerAmount < configManager.playerEntityList.size(); playerAmount++)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP)configManager.playerEntityList.get(playerAmount);
                PartyController.instance().getMember(entityPlayer);
                entityPlayer.playerNetServerHandler.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.bS, playerClientInfo));
            }

            this.player.a.sendPacketToPlayer(AetherPacketHandler.sendPlayerClientInfo(false, true, this.player.bS, playerClientInfo));
        }
    }

    public void increaseMaxHP(int i)
    {
        if (this.maxHealth <= 40 - i)
        {
            if (!this.player.q.isRemote)
            {
                PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendHeartChange(false, true, this.maxHealth + i, Collections.singleton(this.player.bS)));
            }

            this.maxHealth += i;
            this.player.b(this.player.aX() + i);
        }
    }

    public void b(NBTTagCompound tag)
    {
        tag.setInteger("MaxHealth", this.maxHealth);
        tag.setTag("AetherInventory", this.inv.writeToNBT(new NBTTagList()));
        tag.setBoolean("HasDefeatedSunSpirit", this.hasDefeatedSunSpirit);
        tag.setBoolean("inAether", this.player.ar == 3);
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
        super.b(tag);
    }

    public void a(NBTTagCompound tag)
    {
        if (!this.player.q.isRemote)
        {
            File file = new File(((SaveHandler)this.player.q.L()).getWorldDirectoryName(), "aether.dat");

            if (file.exists())
            {
                NBTTagCompound customData = new NBTTagCompound();

                try
                {
                    customData = CompressedStreamTools.readCompressed(new FileInputStream(file));
                    this.maxHealth = customData.getInteger("MaxHealth");
                    NBTTagList nbttaglist = customData.getTagList("Inventory");

                    if (this.player.ar == 3)
                    {
                        this.player.ar = 3;
                    }

                    this.inv.readFromNBT(nbttaglist);
                    file.delete();
                }
                catch (IOException ioexception)
                {
                }
            }
            else
            {
                System.out.println("Failed to read player data. Making new");
                this.maxHealth = tag.getInteger("MaxHealth");
                NBTTagList nbttaglist = tag.getTagList("AetherInventory");
                this.hasDefeatedSunSpirit = tag.getBoolean("HasDefeatedSunSpirit");

                if (tag.getBoolean("inAether"))
                {
                    this.player.ar = 3;
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
                this.inv.readFromNBT(nbttaglist);
            }
        }

        super.a(tag);
    }

    public void onDefeatSunSpirit()
    {
        setHasDefeatedSunSpirit(true);
    }

    public void setHasDefeatedSunSpirit(boolean defeatedSunSpirit)
    {
        this.hasDefeatedSunSpirit = defeatedSunSpirit;
    }

    public boolean getHasDefeatedSunSpirit()
    {
        return this.hasDefeatedSunSpirit;
    }

    public float a(Block block, boolean flag)
    {
        ItemStack stack = this.player.bK.getCurrentItem();
        float f = stack == null ? 1.0F : stack.getItem().getStrVsBlock(stack, block, 0);

        if ((this.inv.slots[0] != null) && (this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID))
        {
            f *= (1.0F + this.inv.slots[0].getItemDamage() / (this.inv.slots[0].getMaxDamage() * 3.0F));
        }

        if ((this.inv.slots[4] != null) && (this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID))
        {
            f *= (1.0F + this.inv.slots[4].getItemDamage() / (this.inv.slots[4].getMaxDamage() * 3.0F));
        }

        if ((this.inv.slots[5] != null) && (this.inv.slots[5].itemID == AetherItems.ZaniteRing.itemID))
        {
            f *= (1.0F + this.inv.slots[5].getItemDamage() / (this.inv.slots[5].getMaxDamage() * 3.0F));
        }

        if (wearingNeptuneArmour())
        {
            if (f > 1.0F)
            {
                int i = EnchantmentHelper.getEfficiencyModifier(this.player);
                ItemStack itemstack = this.player.bK.getCurrentItem();

                if ((i > 0) && (itemstack != null))
                {
                    float f1 = i * i + 1;
                    boolean canHarvest = ForgeHooks.canToolHarvestBlock(block, 0, itemstack);

                    if ((!canHarvest) && (f <= 1.0F))
                    {
                        f += f1 * 0.08F;
                    }
                    else
                    {
                        f += f1;
                    }
                }
            }

            if (this.player.a(Potion.digSpeed))
            {
                f *= (1.0F + (this.player.b(Potion.digSpeed).getAmplifier() + 1) * 0.2F);
            }

            if (this.player.a(Potion.digSlowdown))
            {
                f *= (1.0F - (this.player.b(Potion.digSlowdown).getAmplifier() + 1) * 0.2F);
            }

            f = ForgeEventFactory.getBreakSpeed(this.player, block, 0, f);
            return f < 0.0F ? 0.0F : f;
        }

        return f == -1.0F ? super.a(block, flag) : f;
    }

    public boolean G()
    {
        if (wearingNeptuneArmour())
        {
            return false;
        }

        return super.G();
    }

    public MovingObjectPosition a(double var1, float var3)
    {
        ItemStack stack = this.player.cd();

        if ((stack != null) && (stack.getItem() != null) && (this.extendedReachItems.contains(stack.getItem())))
        {
            var1 = 10.0D;
        }

        return this.player.rayTrace(var1, var3);
    }

    public int getAccessoryCount(int itemID)
    {
        int count = 0;

        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == itemID))
            {
                count++;
            }
        }

        return count;
    }

    public boolean wearingAccessory(int itemID)
    {
        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == itemID))
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

        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == itemID))
            {
                slot = this.inv.slots[index];
                return slot;
            }
        }

        return slot;
    }

    public int getSlotIndex(int itemID)
    {
        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && (this.inv.slots[index].itemID == itemID))
            {
                return index;
            }
        }

        return 0;
    }

    public boolean wearingArmour(int itemID)
    {
        for (int index = 0; index < 4; index++)
        {
            if ((this.player.bK.armorInventory[index] != null) && (this.player.bK.armorInventory[index].itemID == itemID))
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
        return (wearingArmour(AetherItems.NeptuneHelmet.itemID)) && (wearingArmour(AetherItems.NeptuneChestplate.itemID)) && (wearingArmour(AetherItems.NeptuneLeggings.itemID)) && (wearingArmour(AetherItems.NeptuneBoots.itemID)) && (wearingAccessory(AetherItems.NeptuneGloves.itemID));
    }

    public boolean wearingValkyrieArmour()
    {
        return (wearingArmour(AetherItems.ValkyrieHelmet.itemID)) && (wearingArmour(AetherItems.ValkyrieChestplate.itemID)) && (wearingArmour(AetherItems.ValkyrieLeggings.itemID)) && (wearingArmour(AetherItems.ValkyrieBoots.itemID)) && (wearingAccessory(AetherItems.ValkyrieGloves.itemID));
    }

    public boolean wearingObsidianArmour()
    {
        return (wearingArmour(AetherItems.ObsidianHelmet.itemID)) && (wearingArmour(AetherItems.ObsidianChestplate.itemID)) && (wearingArmour(AetherItems.ObsidianLeggings.itemID)) && (wearingArmour(AetherItems.ObsidianBoots.itemID)) && (wearingAccessory(AetherItems.ObsidianGloves.itemID));
    }

    public boolean wearingPhoenixArmour()
    {
        return (wearingArmour(AetherItems.PhoenixHelmet.itemID)) && (wearingArmour(AetherItems.PhoenixChestplate.itemID)) && (wearingArmour(AetherItems.PhoenixLeggings.itemID)) && (wearingArmour(AetherItems.PhoenixBoots.itemID)) && (wearingAccessory(AetherItems.PhoenixGloves.itemID));
    }

    public boolean wearingGravititeArmour()
    {
        return (wearingArmour(AetherItems.GravititeHelmet.itemID)) && (wearingArmour(AetherItems.GravititeChestplate.itemID)) && (wearingArmour(AetherItems.GravititeLeggings.itemID)) && (wearingArmour(AetherItems.GravititeBoots.itemID)) && (wearingAccessory(AetherItems.GravititeGloves.itemID));
    }

    public void setCoinAmount(int i)
    {
        this.coinAmount = i;

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
        switch (getParachuteType())
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
        }

        return 0;
    }

    public void setParachuting(boolean isParachuting, int parachuteType)
    {
        this.isParachuting = isParachuting;
        this.parachuteType = parachuteType;

        if (!isParachuting)
        {
            EntityItem item = new EntityItem(this.player.q, this.player.u, this.player.v, this.player.w, new ItemStack(Item.silk, this.ab.nextInt(3)));
            EntityItem block = new EntityItem(this.player.q, this.player.u, this.player.v, this.player.w, new ItemStack(AetherBlocks.Aercloud, this.ab.nextInt(3), getType()));
            this.player.q.spawnEntityInWorld(block);
            this.player.q.spawnEntityInWorld(item);
        }

        if (!this.player.q.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendParachuteCheck(false, isParachuting, isParachuting, parachuteType, Collections.singleton(this.player.bS)));
        }
    }
}

