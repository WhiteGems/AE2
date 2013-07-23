package net.aetherteam.aether.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.client.gui.social.GuiScreenNotificationOverlay;
import net.aetherteam.aether.containers.ContainerPlayerAether;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.aether.overlays.AetherOverlays;
import net.aetherteam.aether.party.Party;
import net.aetherteam.playercore_api.cores.PlayerCoreClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

@SideOnly(Side.CLIENT)
public class PlayerBaseAetherClient extends PlayerCoreClient
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
    public int updateCounter;
    private float sinage;
    private boolean prevCreative;
    private int coinAmount = 0;
    public static boolean keepInventory = false;
    public Random ab = new Random();
    public float zLevel = -90.0F;
    private Minecraft c = Minecraft.getMinecraft();
    private int parachuteType;
    public List extendedReachItems;
    private boolean isParachuting;

    public PlayerBaseAetherClient(Minecraft var1, World var2, Session var3, NetClientHandler var4, int var5, PlayerCoreClient var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);
        this.player.bL = (Container)(!this.player.ce.isCreativeMode ? new ContainerPlayerAether(this.player.bK, this.inv, this.player.q.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.bK, true, this.player));
        this.player.bM = this.player.bL;
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
    }

    public void removeCoins(int var1)
    {
        this.coinAmount -= var1;
    }

    public void updateGeneralCooldown()
    {
        if (this.generalcooldown == 0 && Aether.proxy.getClientCooldown().get(this.player.bS) != null && Aether.proxy.getClientMaxCooldown().get(this.player.bS) != null)
        {
            this.generalcooldown = ((Integer)Aether.proxy.getClientCooldown().get(this.player.bS)).intValue();
            this.generalcooldownmax = ((Integer)Aether.proxy.getClientMaxCooldown().get(this.player.bS)).intValue();
        }
    }

    public void updateCoinAmount()
    {
        if (Aether.proxy.getClientCoins().get(this.player.bS) != null)
        {
            this.coinAmount = ((Integer)Aether.proxy.getClientCoins().get(this.player.bS)).intValue();
            AetherOverlays.queueCoinbarSlide();
        }
    }

    public void updateParachute()
    {
        if (Aether.proxy.getClientParachuting().get(this.player.bS) != null && Aether.proxy.getClientParachuteType().get(this.player.bS) != null)
        {
            this.isParachuting = ((Boolean)Aether.proxy.getClientParachuting().get(this.player.bS)).booleanValue();
            this.parachuteType = ((Integer)Aether.proxy.getClientParachuteType().get(this.player.bS)).intValue();
        }
    }

    public boolean g_()
    {
        return this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock() ? true : super.g_();
    }

    public void a(EntityLightningBolt var1)
    {
        if (!(var1 instanceof EntityAetherLightning) || ((EntityAetherLightning)var1).playerUsing != this.player)
        {
            super.a(var1);
        }
    }

    public void a(Entity var1, int var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.a(var1, var2, var3, var5);
        }
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.G;
    }

    public void bl()
    {
        if (this.playerHandler.jump())
        {
            super.bl();
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

    public void l_()
    {
        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.T = 0.0F;
        }

        super.l_();
        this.processAbilities();

        if (Aether.proxy.getClientCoins().get(this.player.bS) != null && this.coinAmount != ((Integer)Aether.proxy.getClientCoins().get(this.player.bS)).intValue())
        {
            this.updateCoinAmount();
        }

        if (Aether.proxy.getClientParachuting().get(this.player.bS) != null && this.isParachuting != ((Boolean)Aether.proxy.getClientParachuting().get(this.player.bS)).booleanValue() || Aether.proxy.getClientParachuteType().get(this.player.bS) != null && this.parachuteType != ((Integer)Aether.proxy.getClientParachuteType().get(this.player.bS)).intValue())
        {
            this.updateParachute();
        }

        if (this.prevCreative != this.player.ce.isCreativeMode)
        {
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

        PotionEffect var1 = this.player.b(Potion.regeneration);

        if (var1 != null && var1.getDuration() > 0 && Potion.potionTypes[var1.getPotionID()].isReady(var1.getDuration(), var1.getAmplifier()) && this.player.aX() >= 20 && this.player.aX() < this.maxHealth)
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

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        if (this.player.q.difficultySetting == 0 && this.player.aX() >= 20 && this.player.aX() < this.maxHealth && this.player.ac % 20 == 0)
        {
            this.player.j(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity var2 = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(var2.posX - this.player.u, 2.0D) + Math.pow(var2.posY - this.player.v, 2.0D) + Math.pow(var2.posZ - this.player.w, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.bS) != null)
        {
            this.maxHealth = ((Integer)Aether.proxy.getClientExtraHearts().get(this.player.bS)).intValue();
        }

        ++this.updateCounter;
        this.playerHandler.afterOnUpdate();
    }

    public boolean isAboveBlock(int var1)
    {
        MathHelper.floor_double(this.player.u);
        int var2 = MathHelper.floor_double(this.player.E.minY);
        MathHelper.floor_double(this.player.w);
        return this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), var2 - 1, MathHelper.floor_double(this.player.E.minZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), var2 - 1, MathHelper.floor_double(this.player.E.minZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.maxX), var2 - 1, MathHelper.floor_double(this.player.E.maxZ)) == var1 || this.player.q.getBlockId(MathHelper.floor_double(this.player.E.minX), var2 - 1, MathHelper.floor_double(this.player.E.maxZ)) == var1;
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

            if (this.player.q.isRemote)
            {
                FMLClientHandler.instance().getClient().theWorld.spawnParticle("flame", this.player.u + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.v - 0.5D + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.w + this.playerHandler.rand.nextGaussian() / 3.0D, 0.0D, 0.0D, 0.0D);
            }
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
                ((ItemAccessory)this.inv.slots[var1].getItem()).activateClientPassive(this.player, this);
            }
        }

        if (!this.wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.Y = this.prevStepHeight;
        }
    }

    public float bE()
    {
        float var1 = this.playerHandler.getSpeedModifier();
        return var1 == -1.0F ? super.bE() : var1;
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
        super.b(var1);
    }

    public void a(NBTTagCompound var1)
    {
        if (this.player.q.isRemote)
        {
            File var2 = new File(((SaveHandler)this.player.q.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

            if (var2.exists())
            {
                new NBTTagCompound();

                try
                {
                    NBTTagCompound var3 = CompressedStreamTools.readCompressed(new FileInputStream(var2));
                    this.maxHealth = var3.getInteger("MaxHealth");

                    if (this.maxHealth < 20)
                    {
                        this.maxHealth = 20;
                    }

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

                if (this.maxHealth < 20)
                {
                    this.maxHealth = 20;
                }

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

        return this.player.a(var1, var3);
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

    public void updateNotificationOverlay(Party var1, byte var2)
    {
        this.c.displayGuiScreen(new GuiScreenNotificationOverlay(var1, var2));
    }
}
