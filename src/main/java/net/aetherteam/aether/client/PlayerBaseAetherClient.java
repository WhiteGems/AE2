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
    public Random rand = new Random();
    public float zLevel = -90.0F;
    private Minecraft mc = Minecraft.getMinecraft();
    private int parachuteType;
    public List extendedReachItems;
    private boolean isParachuting;

    public PlayerBaseAetherClient(Minecraft var1, World var2, Session var3, NetClientHandler var4, int var5, PlayerCoreClient var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
        this.maxHealth = 20;
        this.inv = new InventoryAether(this.player);
        this.player.inventoryContainer = (Container)(!this.player.capabilities.isCreativeMode ? new ContainerPlayerAether(this.player.inventory, this.inv, this.player.worldObj.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.inventory, true, this.player));
        this.player.openContainer = this.player.inventoryContainer;
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
        if (this.generalcooldown == 0 && Aether.proxy.getClientCooldown().get(this.player.username) != null && Aether.proxy.getClientMaxCooldown().get(this.player.username) != null)
        {
            this.generalcooldown = ((Integer)Aether.proxy.getClientCooldown().get(this.player.username)).intValue();
            this.generalcooldownmax = ((Integer)Aether.proxy.getClientMaxCooldown().get(this.player.username)).intValue();
        }
    }

    public void updateCoinAmount()
    {
        if (Aether.proxy.getClientCoins().get(this.player.username) != null)
        {
            this.coinAmount = ((Integer)Aether.proxy.getClientCoins().get(this.player.username)).intValue();
            AetherOverlays.queueCoinbarSlide();
        }
    }

    public void updateParachute()
    {
        if (Aether.proxy.getClientParachuting().get(this.player.username) != null && Aether.proxy.getClientParachuteType().get(this.player.username) != null)
        {
            this.isParachuting = ((Boolean)Aether.proxy.getClientParachuting().get(this.player.username)).booleanValue();
            this.parachuteType = ((Integer)Aether.proxy.getClientParachuteType().get(this.player.username)).intValue();
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return this.wearingAccessory(AetherItems.SwettyPendant.itemID) && this.isBesideClimbableBlock() ? true : super.isOnLadder();
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
     * knocks back this entity
     */
    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        if (!this.wearingObsidianArmour())
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.isCollidedHorizontally;
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
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.isAboveBlock(AetherBlocks.Aercloud.blockID))
        {
            this.player.fallDistance = 0.0F;
        }

        super.onUpdate();
        this.processAbilities();

        if (Aether.proxy.getClientCoins().get(this.player.username) != null && this.coinAmount != ((Integer)Aether.proxy.getClientCoins().get(this.player.username)).intValue())
        {
            this.updateCoinAmount();
        }

        if (Aether.proxy.getClientParachuting().get(this.player.username) != null && this.isParachuting != ((Boolean)Aether.proxy.getClientParachuting().get(this.player.username)).booleanValue() || Aether.proxy.getClientParachuteType().get(this.player.username) != null && this.parachuteType != ((Integer)Aether.proxy.getClientParachuteType().get(this.player.username)).intValue())
        {
            this.updateParachute();
        }

        if (this.prevCreative != this.player.capabilities.isCreativeMode)
        {
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

        PotionEffect var1 = this.player.getActivePotionEffect(Potion.regeneration);

        if (var1 != null && var1.getDuration() > 0 && Potion.potionTypes[var1.getPotionID()].isReady(var1.getDuration(), var1.getAmplifier()) && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth)
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

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        if (this.player.worldObj.difficultySetting == 0 && this.player.getHealth() >= 20 && this.player.getHealth() < this.maxHealth && this.player.ticksExisted % 20 == 0)
        {
            this.player.heal(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity var2 = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(var2.posX - this.player.posX, 2.0D) + Math.pow(var2.posY - this.player.posY, 2.0D) + Math.pow(var2.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.username) != null)
        {
            this.maxHealth = ((Integer)Aether.proxy.getClientExtraHearts().get(this.player.username)).intValue();
        }

        ++this.updateCounter;
        this.playerHandler.afterOnUpdate();
    }

    public boolean isAboveBlock(int var1)
    {
        MathHelper.floor_double(this.player.posX);
        int var2 = MathHelper.floor_double(this.player.boundingBox.minY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1 || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), var2 - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == var1;
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

            if (this.player.worldObj.isRemote)
            {
                FMLClientHandler.instance().getClient().theWorld.spawnParticle("flame", this.player.posX + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.posY - 0.5D + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.posZ + this.playerHandler.rand.nextGaussian() / 3.0D, 0.0D, 0.0D, 0.0D);
            }
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
                ((ItemAccessory)this.inv.slots[var1].getItem()).activateClientPassive(this.player, this);
            }
        }

        if (!this.wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.stepHeight = this.prevStepHeight;
        }
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        float var1 = this.playerHandler.getSpeedModifier();
        return var1 == -1.0F ? super.getSpeedModifier() : var1;
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
        super.writeEntityToNBT(var1);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        if (this.player.worldObj.isRemote)
        {
            File var2 = new File(((SaveHandler)this.player.worldObj.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

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

                if (this.maxHealth < 20)
                {
                    this.maxHealth = 20;
                }

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

    public void updateNotificationOverlay(Party var1, byte var2)
    {
        this.mc.displayGuiScreen(new GuiScreenNotificationOverlay(var1, var2));
    }
}
