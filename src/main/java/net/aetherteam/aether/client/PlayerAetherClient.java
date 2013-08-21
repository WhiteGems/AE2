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
public class PlayerAetherClient extends PlayerCoreClient
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
    public int updateCounter;
    private float sinage;
    private boolean prevCreative;
    private int coinAmount = 0;
    public static boolean keepInventory = false;
    public Random rand = new Random();
    public float zLevel = -90.0F;
    private Minecraft mc = Minecraft.getMinecraft();
    private int parachuteType;
    public List<Item> extendedReachItems;
    private boolean isParachuting;

    public PlayerAetherClient(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4, int playerCoreIndex, PlayerCoreClient entityPlayerSP)
    {
        super(par1Minecraft, par2World, par3Session, par4, playerCoreIndex, entityPlayerSP);
        this.extendedReachItems = Arrays.asList(new Item[] {AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});
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

    public void addCoins(int amount)
    {
        this.coinAmount += amount;
    }

    public void removeCoins(int amount)
    {
        this.coinAmount -= amount;
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
    public void knockBack(Entity var1, float var2, double var3, double var5)
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

        if (this.generalcooldown > 0)
        {
            --this.generalcooldown;
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity boss = this.playerHandler.getCurrentBoss().getBossEntity();

            if (Math.sqrt(Math.pow(boss.posX - this.player.posX, 2.0D) + Math.pow(boss.posY - this.player.posY, 2.0D) + Math.pow(boss.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss((IAetherBoss)null);
            }
        }

        ++this.updateCounter;
        this.playerHandler.afterOnUpdate();
    }

    public boolean isAboveBlock(int blockID)
    {
        MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.boundingBox.minY);
        MathHelper.floor_double(this.player.posZ);
        return this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID || this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID;
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
                ((ItemAccessory)this.inv.slots[index].getItem()).activateClientPassive(this.player, this);
            }
        }

        if (!this.wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.stepHeight = this.prevStepHeight;
        }
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
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
        super.writeEntityToNBT(tag);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        if (this.player.worldObj.isRemote)
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

    public void updateNotificationOverlay(Party party, byte guiType)
    {
        this.mc.displayGuiScreen(new GuiScreenNotificationOverlay(party, guiType));
    }
}
