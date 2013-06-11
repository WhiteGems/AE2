package net.aetherteam.aether.client;

import cpw.mods.fml.client.FMLClientHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.client.gui.social.GuiScreenNotificationOverlay;
import net.aetherteam.aether.containers.ContainerPlayerAether;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.entities.EntityAetherLightning;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.aether.overlays.AetherOverlays;
import net.aetherteam.aether.party.Party;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class PlayerBaseAetherClient extends PlayerBase
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

    public List extendedReachItems = Arrays.asList(new Item[]{AetherItems.ValkyrieShovel, AetherItems.ValkyriePickaxe, AetherItems.ValkyrieAxe});

    public PlayerBaseAetherClient(PlayerAPI var1)
    {
        super(var1);

        this.maxHealth = 20;

        this.inv = new InventoryAether(this.player);

        this.player.inventoryContainer = (!this.player.capabilities.isCreativeMode ? new ContainerPlayerAether(this.player.inventory, this.inv, this.player.worldObj.isRemote, this.player, this.playerHandler) : new ContainerPlayer(this.player.inventory, true, this.player));
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
        if (this.generalcooldown == 0)
        {
            if ((Aether.proxy.getClientCooldown().get(this.player.username) != null) && (Aether.proxy.getClientMaxCooldown().get(this.player.username) != null))
            {
                this.generalcooldown = ((Integer) Aether.proxy.getClientCooldown().get(this.player.username)).intValue();
                this.generalcooldownmax = ((Integer) Aether.proxy.getClientMaxCooldown().get(this.player.username)).intValue();
            }
        }
    }

    public void updateCoinAmount()
    {
        if (Aether.proxy.getClientCoins().get(this.player.username) != null)
        {
            this.coinAmount = ((Integer) Aether.proxy.getClientCoins().get(this.player.username)).intValue();
            AetherOverlays.queueCoinbarSlide();
        }
    }

    public boolean isOnLadder()
    {
        if ((wearingAccessory(AetherItems.SwettyPendant.itemID)) && (isBesideClimbableBlock()))
        {
            return true;
        }

        return super.isOnLadder();
    }

    public void onStruckByLightning(EntityLightningBolt var1)
    {
        if ((var1 instanceof EntityAetherLightning))
        {
            if (((EntityAetherLightning) var1).playerUsing == this.player)
            {
                return;
            }
        }

        super.onStruckByLightning(var1);
    }

    public void knockBack(Entity var1, int var2, double var3, double var5)
    {
        if (!wearingObsidianArmour())
        {
            super.knockBack(var1, var2, var3, var5);
        }
    }

    public boolean isBesideClimbableBlock()
    {
        return this.player.isCollidedHorizontally;
    }

    public void jump()
    {
        if (this.playerHandler.jump()) super.jump();
    }

    public void beforeOnLivingUpdate()
    {
        this.playerHandler.beforeOnLivingUpdate();
        if ((this.playerHandler.riddenBy != null) && (this.playerHandler.riddenBy.shouldBeSitting()))
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

    public void beforeOnUpdate()
    {
        if (isAboveBlock(AetherBlocks.Aercloud.blockID)) this.player.fallDistance = 0.0F;
    }

    public boolean isAboveBlock(int blockID)
    {
        int x = MathHelper.floor_double(this.player.posX);
        int y = MathHelper.floor_double(this.player.boundingBox.minY);
        int z = MathHelper.floor_double(this.player.posZ);
        return (this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID) || (this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.minZ)) == blockID) || (this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.maxX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID) || (this.player.worldObj.getBlockId(MathHelper.floor_double(this.player.boundingBox.minX), y - 1, MathHelper.floor_double(this.player.boundingBox.maxZ)) == blockID);
    }

    public void afterOnUpdate()
    {
        processAbilities();

        if (this.prevCreative != this.player.capabilities.isCreativeMode)
        {
            if (!this.player.capabilities.isCreativeMode) ;
            this.prevCreative = this.player.capabilities.isCreativeMode;
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.username) != null)
        {
            this.maxHealth = ((Integer) Aether.proxy.getClientExtraHearts().get(this.player.username)).intValue();
        }

        PotionEffect effect = this.player.getActivePotionEffect(Potion.regeneration);
        if ((effect != null) && (effect.getDuration() > 0) && (Potion.potionTypes[effect.getPotionID()].isReady(effect.getDuration(), effect.getAmplifier())) && (this.player.getHealth() >= 20) && (this.player.getHealth() < this.maxHealth))
        {
            this.player.heal(1);
        }
        if ((this.player.getFoodStats().getFoodLevel() >= 18) && (this.player.getHealth() >= 20) && (this.player.getHealth() < this.maxHealth))
        {
            this.foodTimer += 1;
            if (this.foodTimer >= 80)
            {
                this.foodTimer = 0;
                this.player.heal(1);
            }
        } else
        {
            this.foodTimer = 0;
        }

        if (this.generalcooldown > 0)
        {
            this.generalcooldown -= 1;
        }

        if ((this.player.worldObj.difficultySetting == 0) && (this.player.getHealth() >= 20) && (this.player.getHealth() < this.maxHealth) && (this.player.ticksExisted % 20 == 0))
        {
            this.player.heal(1);
        }

        if (this.playerHandler.getCurrentBoss() != null)
        {
            Entity boss = this.playerHandler.getCurrentBoss().getBossEntity();
            if (Math.sqrt(Math.pow(boss.posX - this.player.posX, 2.0D) + Math.pow(boss.posY - this.player.posY, 2.0D) + Math.pow(boss.posZ - this.player.posZ, 2.0D)) > 50.0D)
            {
                this.playerHandler.setCurrentBoss(null);
            }
        }

        if (Aether.proxy.getClientExtraHearts().get(this.player.username) != null)
        {
            this.maxHealth = ((Integer) Aether.proxy.getClientExtraHearts().get(this.player.username)).intValue();
        }

        this.updateCounter += 1;

        this.playerHandler.afterOnUpdate();
    }

    public void processAbilities()
    {
        if (!this.player.onGround) this.sinage += 0.75F;
        else
        {
            this.sinage += 0.15F;
        }

        if (this.sinage > 6.283186F)
        {
            this.sinage -= 6.283186F;
        }

        if ((wearingAccessory(AetherItems.SwettyPendant.itemID)) && (isBesideClimbableBlock()))
        {
            if ((!this.player.onGround) && (this.player.motionY < 0.0D) && (!this.player.isInWater()) && (!this.player.isSneaking()))
            {
                this.player.motionY *= 0.6D;
            }

            this.player.fallDistance = -1.0F;
        }

        if (this.player.ticksExisted % 400 == 0)
        {
            if ((this.inv.slots[0] != null) && (this.inv.slots[0].itemID == AetherItems.ZanitePendant.itemID))
            {
                this.inv.slots[0].damageItem(1, this.player);
                if (this.inv.slots[0].stackSize < 1) this.inv.slots[0] = null;
            }
            if ((this.inv.slots[4] != null) && (this.inv.slots[4].itemID == AetherItems.ZaniteRing.itemID))
            {
                this.inv.slots[4].damageItem(1, this.player);
                if (this.inv.slots[4].stackSize < 1) this.inv.slots[4] = null;
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
            this.player.extinguish();
            this.player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 10, 4));

            if (this.player.worldObj.isRemote)
            {
                FMLClientHandler.instance().getClient().theWorld.spawnParticle("flame", this.player.posX + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.posY - 0.5D + this.playerHandler.rand.nextGaussian() / 5.0D, this.player.posZ + this.playerHandler.rand.nextGaussian() / 3.0D, 0.0D, 0.0D, 0.0D);
            }
        }

        if (wearingGravititeArmour())
        {
            if ((this.player.isJumping) && (!this.jumpBoosted) && (this.player.isSneaking()))
            {
                this.player.motionY = 1.0D;
                this.jumpBoosted = true;
            }

            this.player.fallDistance = -1.0F;
        }

        if (wearingObsidianArmour())
        {
            this.player.addPotionEffect(new PotionEffect(Potion.resistance.id, 10, 3));
            this.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 1));
        }

        if (wearingValkyrieArmour())
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
                        this.player.motionY = (0.025D * this.flightMod);
                        this.flightCount += 1;
                    }
                } else this.flightCount += 1;
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

        if ((!this.player.isJumping) && (this.player.onGround))
        {
            this.jumpBoosted = false;
        }

        for (int index = 0; index < 8; index++)
        {
            if ((this.inv.slots[index] != null) && ((this.inv.slots[index].getItem() instanceof IAetherAccessory)))
            {
                ((ItemAccessory) this.inv.slots[index].getItem()).activateClientPassive(this.player, this);
            }
        }

        if (!wearingAccessory(AetherItems.AgilityCape.itemID))
        {
            this.player.stepHeight = this.prevStepHeight;
        }
    }

    public float getSpeedModifier()
    {
        float speed = this.playerHandler.getSpeedModifier();
        return speed == -1.0F ? super.getSpeedModifier() : speed;
    }

    public AetherCommonPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    public void heal(int i)
    {
        if (this.player.getHealth() <= 0)
        {
            return;
        }

        this.player.setEntityHealth(this.player.getHealth() + i);

        if (this.player.getHealth() > this.maxHealth)
        {
            this.player.setEntityHealth(this.maxHealth);
        }
    }

    public void beforeWriteEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("MaxHealth", this.maxHealth);
        tag.setTag("AetherInventory", this.inv.writeToNBT(new NBTTagList()));
        tag.setBoolean("HasDefeatedSunSpirit", this.hasDefeatedSunSpirit);
        tag.setBoolean("inAether", this.player.dimension == 3);
        tag.setInteger("GeneralCooldown", this.generalcooldown);
        tag.setInteger("GeneralCooldownMax", this.generalcooldownmax);
        tag.setString("CooldownName", this.cooldownName);
        tag.setInteger("Coins", this.coinAmount);
    }

    public void beforeReadEntityFromNBT(NBTTagCompound tag)
    {
        if (this.player.worldObj.isRemote)
        {
            File file = new File(((SaveHandler) this.player.worldObj.getSaveHandler()).getWorldDirectoryName(), "aether.dat");

            if (file.exists())
            {
                NBTTagCompound customData = new NBTTagCompound();
                try
                {
                    customData = CompressedStreamTools.readCompressed(new FileInputStream(file));

                    this.maxHealth = customData.getInteger("MaxHealth");
                    if (this.maxHealth < 20) this.maxHealth = 20;
                    NBTTagList nbttaglist = customData.getTagList("Inventory");

                    if (this.player.dimension == 3)
                    {
                        this.player.dimension = 3;
                    }
                    this.inv.readFromNBT(nbttaglist);
                    file.delete();
                } catch (IOException ioexception)
                {
                }

            } else
            {
                System.out.println("Failed to read player data. Making new");

                this.maxHealth = tag.getInteger("MaxHealth");
                if (this.maxHealth < 20) this.maxHealth = 20;
                NBTTagList nbttaglist = tag.getTagList("AetherInventory");
                this.hasDefeatedSunSpirit = tag.getBoolean("HasDefeatedSunSpirit");

                if (tag.getBoolean("inAether")) this.player.dimension = 3;
                this.generalcooldown = tag.getInteger("GeneralCooldown");
                this.generalcooldownmax = tag.getInteger("GeneralCooldownMax");
                this.cooldownName = tag.getString("CooldownName");
                this.coinAmount = tag.getInteger("Coins");

                this.inv.readFromNBT(nbttaglist);
            }
        }
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

    public float getCurrentPlayerStrVsBlock(Block block, boolean flag)
    {
        ItemStack stack = this.player.inventory.getCurrentItem();
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
                ItemStack itemstack = this.player.inventory.getCurrentItem();

                if ((i > 0) && (itemstack != null))
                {
                    float f1 = i * i + 1;

                    boolean canHarvest = ForgeHooks.canToolHarvestBlock(block, 0, itemstack);

                    if ((!canHarvest) && (f <= 1.0F))
                    {
                        f += f1 * 0.08F;
                    } else
                    {
                        f += f1;
                    }
                }
            }

            if (this.player.isPotionActive(Potion.digSpeed))
            {
                f *= (1.0F + (this.player.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F);
            }

            if (this.player.isPotionActive(Potion.digSlowdown))
            {
                f *= (1.0F - (this.player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F);
            }

            f = ForgeEventFactory.getBreakSpeed(this.player, block, 0, f);

            return f < 0.0F ? 0.0F : f;
        }

        return f == -1.0F ? super.getCurrentPlayerStrVsBlock(block, flag) : f;
    }

    public boolean isInWater()
    {
        if (wearingNeptuneArmour())
        {
            return false;
        }
        return super.isInWater();
    }

    public MovingObjectPosition rayTrace(double var1, float var3)
    {
        ItemStack stack = this.player.getCurrentEquippedItem();
        if ((stack != null) && (stack.getItem() != null) && (this.extendedReachItems.contains(stack.getItem())))
            var1 = 10.0D;
        return this.player.superRayTrace(var1, var3);
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
            if ((this.player.inventory.armorInventory[index] != null) && (this.player.inventory.armorInventory[index].itemID == itemID))
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

    public void updateNotificationOverlay(Party party, byte guiType)
    {
        this.mc.displayGuiScreen(new GuiScreenNotificationOverlay(party, guiType));
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.PlayerBaseAetherClient
 * JD-Core Version:    0.6.2
 */