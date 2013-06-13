package net.aetherteam.aether.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.aetherteam.aether.containers.ContainerAetherCreative;
import net.aetherteam.aether.containers.SlotAetherCreativeInventory;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StringTranslate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiAetherContainerCreative extends AetherInventoryEffectRenderer
{
    private static InventoryBasic inventory = new InventoryBasic("tmp", true, 45);
    private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
    private float currentScroll = 0.0F;
    private boolean isScrolling = false;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List backupContainerSlots;
    private Slot field_74235_v = null;
    private boolean field_74234_w = false;
    private CreativeCrafting field_82324_x;
    private static int tabPage = 0;
    private int maxPages = 0;
    private EntityPlayer player;

    public GuiAetherContainerCreative(EntityPlayer var1)
    {
        super(new ContainerAetherCreative(var1));
        this.player = var1;
        var1.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        var1.addStat(AchievementList.openInventory, 1);
        this.ySize = 158;
        this.xSize = 195;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (!this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
    }

    protected void handleMouseClick(Slot var1, int var2, int var3, int var4)
    {
        this.field_74234_w = true;
        boolean var5 = var4 == 1;
        var4 = var2 == -999 && var4 == 0 ? 4 : var4;
        InventoryPlayer var7;
        ItemStack var6;

        if (var1 == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && var4 != 5)
        {
            var7 = this.mc.thePlayer.inventory;

            if (var7.getItemStack() != null)
            {
                if (var3 == 0)
                {
                    this.mc.thePlayer.dropPlayerItem(var7.getItemStack());
                    this.mc.playerController.func_78752_a(var7.getItemStack());
                    var7.setItemStack((ItemStack) null);
                }

                if (var3 == 1)
                {
                    var6 = var7.getItemStack().splitStack(1);
                    this.mc.thePlayer.dropPlayerItem(var6);
                    this.mc.playerController.func_78752_a(var6);

                    if (var7.getItemStack().stackSize == 0)
                    {
                        var7.setItemStack((ItemStack) null);
                    }
                }
            }
        } else
        {
            int var8;

            if (var1 == this.field_74235_v && var5)
            {
                for (var8 = 0; var8 < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++var8)
                {
                    this.mc.playerController.sendSlotPacket((ItemStack) null, var8);
                }
            } else
            {
                ItemStack var9;

                if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
                {
                    if (var1 == this.field_74235_v)
                    {
                        this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
                    } else if (var4 == 4 && var1 != null && var1.getHasStack())
                    {
                        var9 = var1.decrStackSize(var3 == 0 ? 1 : var1.getStack().getMaxStackSize());
                        this.mc.thePlayer.dropPlayerItem(var9);
                        this.mc.playerController.func_78752_a(var9);
                    } else if (var4 == 4 && this.mc.thePlayer.inventory.getItemStack() != null)
                    {
                        this.mc.thePlayer.dropPlayerItem(this.mc.thePlayer.inventory.getItemStack());
                        this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
                        this.mc.thePlayer.inventory.setItemStack((ItemStack) null);
                    } else
                    {
                        this.mc.thePlayer.inventoryContainer.slotClick(var1 == null ? var2 : SlotAetherCreativeInventory.func_75240_a((SlotAetherCreativeInventory) var1).slotNumber, var3, var4, this.mc.thePlayer);
                        this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
                    }

                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendAccessoryChange(Aether.getClientPlayer(this.player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(this.player.username), (byte) 0));
                } else if (var4 != 5 && var1.inventory == inventory)
                {
                    var7 = this.mc.thePlayer.inventory;
                    var6 = var7.getItemStack();
                    ItemStack var10 = var1.getStack();
                    ItemStack var11;

                    if (var4 == 2)
                    {
                        if (var10 != null && var3 >= 0 && var3 < 9)
                        {
                            var11 = var10.copy();
                            var11.stackSize = var11.getMaxStackSize();
                            this.mc.thePlayer.inventory.setInventorySlotContents(var3, var11);
                            this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
                        }

                        return;
                    }

                    if (var4 == 3)
                    {
                        if (var7.getItemStack() == null && var1.getHasStack())
                        {
                            var11 = var1.getStack().copy();
                            var11.stackSize = var11.getMaxStackSize();
                            var7.setItemStack(var11);
                        }

                        return;
                    }

                    if (var4 == 4)
                    {
                        if (var10 != null)
                        {
                            var11 = var10.copy();
                            var11.stackSize = var3 == 0 ? 1 : var11.getMaxStackSize();
                            this.mc.thePlayer.dropPlayerItem(var11);
                            this.mc.playerController.func_78752_a(var11);
                        }

                        return;
                    }

                    if (var6 != null && var10 != null && var6.isItemEqual(var10))
                    {
                        if (var3 == 0)
                        {
                            if (var5)
                            {
                                var6.stackSize = var6.getMaxStackSize();
                            } else if (var6.stackSize < var6.getMaxStackSize())
                            {
                                ++var6.stackSize;
                            }
                        } else if (var6.stackSize <= 1)
                        {
                            var7.setItemStack((ItemStack) null);
                        } else
                        {
                            --var6.stackSize;
                        }
                    } else if (var10 != null && var6 == null)
                    {
                        var7.setItemStack(ItemStack.copyItemStack(var10));
                        var6 = var7.getItemStack();

                        if (var5)
                        {
                            var6.stackSize = var6.getMaxStackSize();
                        }
                    } else
                    {
                        var7.setItemStack((ItemStack) null);
                    }
                } else
                {
                    this.inventorySlots.slotClick(var1 == null ? var2 : var1.slotNumber, var3, var4, this.mc.thePlayer);

                    if (Container.func_94532_c(var3) == 2)
                    {
                        for (var8 = 0; var8 < 9; ++var8)
                        {
                            this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var8).getStack(), 36 + var8);
                        }
                    } else if (var1 != null)
                    {
                        var9 = this.inventorySlots.getSlot(var1.slotNumber).getStack();
                        this.mc.playerController.sendSlotPacket(var9, var1.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                }
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (this.mc.playerController.isInCreativeMode())
        {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents(true);
            this.searchField = new GuiTextField(this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            int var1 = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
            this.field_82324_x = new CreativeCrafting(this.mc);
            this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_82324_x);
            int var2 = CreativeTabs.creativeTabArray.length;

            if (var2 > 12)
            {
                this.buttonList.add(new GuiButton(101, this.guiLeft - 25, this.guiTop - 23, 20, 20, "<"));
                this.buttonList.add(new GuiButton(102, this.guiLeft + this.xSize + 5, this.guiTop - 23, 20, 20, ">"));
                this.maxPages = (var2 - 12) / 10 + 1;
            }

            GuiButton var3 = new GuiButton(5, this.guiLeft + 8, this.guiTop + 132, 72, 20, StringTranslate.getInstance().translateKey("以太Ⅱ物品百科"));
            var3.enabled = false;
            this.buttonList.add(var3);
            this.buttonList.add(new GuiButton(6, this.guiLeft + 85, this.guiTop + 132, 48, 20, "社区"));
            this.buttonList.add(new GuiButton(7, this.guiLeft + 138, this.guiTop + 132, 50, 20, "捐赠特区"));
        } else
        {
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
        {
            this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
        }

        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
        {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
            {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            } else
            {
                super.keyTyped(var1, var2);
            }
        } else
        {
            if (this.field_74234_w)
            {
                this.field_74234_w = false;
                this.searchField.setText("");
            }

            if (!this.checkHotbarKeys(var2))
            {
                if (this.searchField.textboxKeyTyped(var1, var2))
                {
                    this.updateCreativeSearch();
                } else
                {
                    super.keyTyped(var1, var2);
                }
            }
        }
    }

    private void updateCreativeSearch()
    {
        ContainerAetherCreative var1 = (ContainerAetherCreative) this.inventorySlots;
        var1.itemList.clear();
        Item[] var2 = Item.itemsList;
        int var3 = var2.length;
        int var4;

        for (var4 = 0; var4 < var3; ++var4)
        {
            Item var5 = var2[var4];

            if (var5 != null && var5.getCreativeTab() != null)
            {
                var5.getSubItems(var5.itemID, (CreativeTabs) null, var1.itemList);
            }
        }

        Enchantment[] var12 = Enchantment.enchantmentsList;
        var3 = var12.length;

        for (var4 = 0; var4 < var3; ++var4)
        {
            Enchantment var6 = var12[var4];

            if (var6 != null && var6.type != null)
            {
                Item.enchantedBook.func_92113_a(var6, var1.itemList);
            }
        }

        Iterator var13 = var1.itemList.iterator();
        String var7 = this.searchField.getText().toLowerCase();

        while (var13.hasNext())
        {
            ItemStack var8 = (ItemStack) var13.next();
            boolean var9 = false;
            Iterator var10 = var8.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

            while (true)
            {
                if (var10.hasNext())
                {
                    String var11 = (String) var10.next();

                    if (!var11.toLowerCase().contains(var7))
                    {
                        continue;
                    }

                    var9 = true;
                }

                if (!var9)
                {
                    var13.remove();
                }

                break;
            }
        }

        this.currentScroll = 0.0F;
        var1.scrollTo(0.0F);
    }

    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];

        if (var3 != null && var3.drawInForegroundOfTab())
        {
            this.fontRenderer.drawString(var3.getTranslatedTabLabel(), 8, 6, 4210752);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            int var4 = var1 - this.guiLeft;
            int var5 = var2 - this.guiTop;
            CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                CreativeTabs var9 = var6[var8];

                if (this.func_74232_a(var9, var4, var5))
                {
                    return;
                }
            }
        }

        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            int var4 = var1 - this.guiLeft;
            int var5 = var2 - this.guiTop;
            CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                CreativeTabs var9 = var6[var8];

                if (var9 != null && this.func_74232_a(var9, var4, var5))
                {
                    this.setCurrentCreativeTab(var9);
                    return;
                }
            }
        }

        super.mouseMovedOrUp(var1, var2, var3);
    }

    private boolean needsScrollBars()
    {
        return CreativeTabs.creativeTabArray[selectedTabIndex] == null ? false : selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerAetherCreative) this.inventorySlots).hasMoreThan1PageOfItemsInList();
    }

    private void setCurrentCreativeTab(CreativeTabs var1)
    {
        if (var1 != null)
        {
            int var2 = selectedTabIndex;
            selectedTabIndex = var1.getTabIndex();
            ContainerAetherCreative var3 = (ContainerAetherCreative) this.inventorySlots;
            this.field_94077_p.clear();
            var3.itemList.clear();
            var1.displayAllReleventItems(var3.itemList);

            if (var1 == CreativeTabs.tabInventory)
            {
                Container var4 = this.player.inventoryContainer;

                if (this.backupContainerSlots == null)
                {
                    this.backupContainerSlots = var3.inventorySlots;
                }

                var3.inventorySlots = new ArrayList();

                for (int var5 = 0; var5 < var4.inventorySlots.size() - 8; ++var5)
                {
                    SlotAetherCreativeInventory var6 = new SlotAetherCreativeInventory(this, (Slot) var4.inventorySlots.get(var5), var5);
                    var3.inventorySlots.add(var6);
                    int var7;
                    int var9;
                    int var8;

                    if (var5 >= 5 && var5 < 9)
                    {
                        var7 = var5 - 5;
                        var8 = var7 / 2;
                        var9 = var7 % 2;
                        var6.xDisplayPosition = 45 + var8 * 18;
                        var6.yDisplayPosition = 6 + var9 * 27;
                    } else if (var5 >= 0 && var5 < 5)
                    {
                        var6.yDisplayPosition = -2000;
                        var6.xDisplayPosition = -2000;
                    } else if (var5 < var4.inventorySlots.size())
                    {
                        var7 = var5 - 9;
                        var8 = var7 % 9;
                        var9 = var7 / 9;
                        var6.xDisplayPosition = 9 + var8 * 18;

                        if (var5 >= 36 && var5 <= 44)
                        {
                            var6.yDisplayPosition = 112;
                        } else if (var5 > 44 && var5 <= 48)
                        {
                            var6.xDisplayPosition = 81 + var8 * 18;
                            var6.yDisplayPosition = 6;
                        } else if (var5 > 48)
                        {
                            var6.xDisplayPosition = 81 + (var8 - 4) * 18;
                            var6.yDisplayPosition = 33;
                        } else
                        {
                            var6.yDisplayPosition = 54 + var9 * 18;
                        }
                    }
                }

                this.field_74235_v = new Slot(inventory, 0, 173, 112);
                var3.inventorySlots.add(this.field_74235_v);
            } else if (var2 == CreativeTabs.tabInventory.getTabIndex())
            {
                var3.inventorySlots = this.backupContainerSlots;
                this.backupContainerSlots = null;
            }

            if (this.searchField != null)
            {
                if (var1 == CreativeTabs.tabAllSearch)
                {
                    this.searchField.setVisible(true);
                    this.searchField.setCanLoseFocus(false);
                    this.searchField.setFocused(true);
                    this.searchField.setText("");
                    this.updateCreativeSearch();
                } else
                {
                    this.searchField.setVisible(false);
                    this.searchField.setCanLoseFocus(true);
                    this.searchField.setFocused(false);
                }
            }

            this.currentScroll = 0.0F;
            var3.scrollTo(0.0F);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0 && this.needsScrollBars())
        {
            int var2 = ((ContainerAetherCreative) this.inventorySlots).itemList.size() / 9 - 5 + 1;

            if (var1 > 0)
            {
                var1 = 1;
            }

            if (var1 < 0)
            {
                var1 = -1;
            }

            this.currentScroll = (float) ((double) this.currentScroll - (double) var1 / (double) var2);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            ((ContainerAetherCreative) this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        boolean var4 = Mouse.isButtonDown(0);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        int var7 = var5 + 175;
        int var8 = var6 + 18;
        int var9 = var7 + 14;
        int var10 = var8 + 112;

        if (!this.wasClicking && var4 && var1 >= var7 && var2 >= var8 && var1 < var9 && var2 < var10)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!var4)
        {
            this.isScrolling = false;
        }

        this.wasClicking = var4;

        if (this.isScrolling)
        {
            this.currentScroll = ((float) (var2 - var8) - 7.5F) / ((float) (var10 - var8) - 15.0F);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            ((ContainerAetherCreative) this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(var1, var2, var3);
        CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
        int var12 = tabPage * 10;
        int var13 = Math.min(var11.length, (tabPage + 1) * 10 + 2);

        if (tabPage != 0)
        {
            var12 += 2;
        }

        boolean var14 = false;

        for (int var15 = var12; var15 < var13; ++var15)
        {
            CreativeTabs var16 = var11[var15];

            if (var16 != null && this.renderCreativeInventoryHoveringText(var16, var1, var2))
            {
                var14 = true;
                break;
            }
        }

        if (!var14 && !this.renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, var1, var2))
        {
            this.renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, var1, var2);
        }

        if (this.field_74235_v != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, var1, var2))
        {
            this.drawCreativeTabHoveringText(StringTranslate.getInstance().translateKey("inventory.binSlot"), var1, var2);
        }

        if (this.maxPages != 0)
        {
            String var18 = String.format("%d / %d", new Object[]{Integer.valueOf(tabPage + 1), Integer.valueOf(this.maxPages + 1)});
            int var17 = this.fontRenderer.getStringWidth(var18);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            this.fontRenderer.drawString(var18, this.guiLeft + this.xSize / 2 - var17 / 2, this.guiTop - 38, -1);
            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] var5 = CreativeTabs.creativeTabArray;
        int var6 = var5.length;
        int var8 = tabPage * 10;
        var6 = Math.min(var5.length, (tabPage + 1) * 10 + 2);

        if (tabPage != 0)
        {
            var8 += 2;
        }

        int var7;

        for (var7 = var8; var7 < var6; ++var7)
        {
            CreativeTabs var9 = var5[var7];
            this.mc.renderEngine.bindTexture("/gui/allitems.png");

            if (var9 != null && var9.getTabIndex() != selectedTabIndex)
            {
                this.renderCreativeTab(var9);
            }
        }

        if (tabPage != 0)
        {
            if (var4 != CreativeTabs.tabAllSearch)
            {
                this.mc.renderEngine.bindTexture("/gui/allitems.png");
                this.renderCreativeTab(CreativeTabs.tabAllSearch);
            }

            if (var4 != CreativeTabs.tabInventory)
            {
                this.mc.renderEngine.bindTexture("/gui/allitems.png");
                this.renderCreativeTab(CreativeTabs.tabInventory);
            }
        }

        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/creative/" + var4.getBackgroundImageName());
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var10 = this.guiLeft + 175;
        var6 = this.guiTop + 18;
        var7 = var6 + 112;
        this.mc.renderEngine.bindTexture("/gui/allitems.png");

        if (var4.shouldHidePlayerInventory())
        {
            this.drawTexturedModalRect(var10, var6 + (int) ((float) (var7 - var6 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if (var4 != null && var4.getTabPage() == tabPage || var4 == CreativeTabs.tabAllSearch || var4 == CreativeTabs.tabInventory)
        {
            this.renderCreativeTab(var4);

            if (var4 == CreativeTabs.tabInventory)
            {
                GuiInventory.drawPlayerOnGui(this.mc, this.guiLeft + 25, this.guiTop + 46, 20, (float) (this.guiLeft + 43 - var2), (float) (this.guiTop + 45 - 30 - var3));
            }
        }
    }

    protected boolean func_74232_a(CreativeTabs var1, int var2, int var3)
    {
        if (var1.getTabPage() != tabPage && var1 != CreativeTabs.tabAllSearch && var1 != CreativeTabs.tabInventory)
        {
            return false;
        } else
        {
            int var4 = var1.getTabColumn();
            int var5 = 28 * var4;
            byte var6 = 0;

            if (var4 == 5)
            {
                var5 = this.xSize - 28 + 2;
            } else if (var4 > 0)
            {
                var5 += var4;
            }

            int var7;

            if (var1.isTabInFirstRow())
            {
                var7 = var6 - 32;
            } else
            {
                var7 = var6 + this.ySize;
            }

            return var2 >= var5 && var2 <= var5 + 28 && var3 >= var7 && var3 <= var7 + 32;
        }
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs var1, int var2, int var3)
    {
        int var4 = var1.getTabColumn();
        int var5 = 28 * var4;
        byte var6 = 0;

        if (var4 == 5)
        {
            var5 = this.xSize - 28 + 2;
        } else if (var4 > 0)
        {
            var5 += var4;
        }

        int var7;

        if (var1.isTabInFirstRow())
        {
            var7 = var6 - 32;
        } else
        {
            var7 = var6 + this.ySize;
        }

        if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, var2, var3))
        {
            this.drawCreativeTabHoveringText(var1.getTranslatedTabLabel(), var2, var3);
            return true;
        } else
        {
            return false;
        }
    }

    protected void renderCreativeTab(CreativeTabs var1)
    {
        boolean var2 = var1.getTabIndex() == selectedTabIndex;
        boolean var3 = var1.isTabInFirstRow();
        int var4 = var1.getTabColumn();
        int var5 = var4 * 28;
        int var6 = 0;
        int var7 = this.guiLeft + 28 * var4;
        int var8 = this.guiTop;
        byte var9 = 32;

        if (var2)
        {
            var6 += 32;
        }

        if (var4 == 5)
        {
            var7 = this.guiLeft + this.xSize - 28;
        } else if (var4 > 0)
        {
            var7 += var4;
        }

        if (var3)
        {
            var8 -= 28;
        } else
        {
            var6 += 64;
            var8 += this.ySize - 4;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
        this.zLevel = 100.0F;
        itemRenderer.zLevel = 100.0F;
        var7 += 6;
        var8 += 8 + (var3 ? 1 : -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ItemStack var10 = var1.getIconItemStack();
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRenderer.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
        }

        if (var1.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
        }

        if (var1.id == 101)
        {
            tabPage = Math.max(tabPage - 1, 0);
        } else if (var1.id == 102)
        {
            tabPage = Math.min(tabPage + 1, this.maxPages);
        }

        if (var1.id == 5)
        {
            int var2 = AetherGuiHandler.realLoreID;
            this.mc.displayGuiScreen(new GuiLore(this.mc.thePlayer.inventory, this.mc.thePlayer, 0));
        }

        if (var1.id == 6)
        {
            this.mc.displayGuiScreen(new GuiMenu(this.player, this));
        }

        if (var1.id == 7)
        {
            this.mc.displayGuiScreen(new GuiDonatorMenu(this.player, this));
        }
    }

    public int func_74230_h()
    {
        return selectedTabIndex;
    }

    public static InventoryBasic getInventory()
    {
        return inventory;
    }
}
