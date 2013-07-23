package net.aetherteam.aether.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.client.PlayerBaseAetherClient;
import net.aetherteam.aether.client.gui.donator.GuiDonatorMenu;
import net.aetherteam.aether.client.gui.social.GuiMenu;
import net.aetherteam.aether.containers.ContainerAetherCreative;
import net.aetherteam.aether.containers.InventoryAether;
import net.aetherteam.aether.containers.SlotAetherCreativeInventory;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StringTranslate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiAetherContainerCreative extends InventoryEffectRenderer
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

    public GuiAetherContainerCreative(EntityPlayer par1EntityPlayer)
    {
        super(new ContainerAetherCreative(par1EntityPlayer));
        this.player = par1EntityPlayer;
        par1EntityPlayer.openContainer = this.inventorySlots;
        this.l = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
        this.ySize = 158;
        this.xSize = 195;
    }

    public void updateScreen()
    {
        if (!this.g.playerController.h())
        {
            this.g.displayGuiScreen(new GuiInventory(this.g.thePlayer));
        }
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        this.field_74234_w = true;
        boolean flag = par4 == 1;
        par4 = (par2 == -999) && (par4 == 0) ? 4 : par4;
        int l;

        if ((par1Slot == null) && (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (par4 != 5))
        {
            InventoryPlayer inventoryplayer = this.g.thePlayer.bK;

            if (inventoryplayer.getItemStack() != null)
            {
                if (par3 == 0)
                {
                    this.g.thePlayer.c(inventoryplayer.getItemStack());
                    this.g.playerController.a(inventoryplayer.getItemStack());
                    inventoryplayer.setItemStack((ItemStack)null);
                }

                if (par3 == 1)
                {
                    ItemStack itemstack = inventoryplayer.getItemStack().splitStack(1);
                    this.g.thePlayer.c(itemstack);
                    this.g.playerController.a(itemstack);

                    if (inventoryplayer.getItemStack().stackSize == 0)
                    {
                        inventoryplayer.setItemStack((ItemStack)null);
                    }
                }
            }
        }
        else if ((par1Slot == this.field_74235_v) && (flag))
        {
            for (l = 0; l < this.g.thePlayer.bL.getInventory().size();)
            {
                this.g.playerController.a((ItemStack)null, l);
                l++;
                continue;

                if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex())
                {
                    if (par1Slot == this.field_74235_v)
                    {
                        this.g.thePlayer.bK.setItemStack((ItemStack)null);
                    }
                    else if ((par4 == 4) && (par1Slot != null) && (par1Slot.getHasStack()))
                    {
                        ItemStack itemstack1 = par1Slot.decrStackSize(par3 == 0 ? 1 : par1Slot.getStack().getMaxStackSize());
                        this.g.thePlayer.c(itemstack1);
                        this.g.playerController.a(itemstack1);
                    }
                    else if ((par4 == 4) && (this.g.thePlayer.bK.getItemStack() != null))
                    {
                        this.g.thePlayer.c(this.g.thePlayer.bK.getItemStack());
                        this.g.playerController.a(this.g.thePlayer.bK.getItemStack());
                        this.g.thePlayer.bK.setItemStack((ItemStack)null);
                    }
                    else
                    {
                        this.g.thePlayer.bL.slotClick(par1Slot == null ? par2 : SlotAetherCreativeInventory.func_75240_a((SlotAetherCreativeInventory)par1Slot).slotNumber, par3, par4, this.g.thePlayer);
                        this.g.thePlayer.bL.detectAndSendChanges();
                    }

                    PacketDispatcher.sendPacketToServer(AetherPacketHandler.sendAccessoryChange(Aether.getClientPlayer(this.player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(this.player.username), (byte)0));
                }
                else if ((par4 != 5) && (par1Slot.inventory == inventory))
                {
                    InventoryPlayer inventoryplayer = this.g.thePlayer.bK;
                    ItemStack itemstack = inventoryplayer.getItemStack();
                    ItemStack itemstack2 = par1Slot.getStack();

                    if (par4 == 2)
                    {
                        if ((itemstack2 != null) && (par3 >= 0) && (par3 < 9))
                        {
                            ItemStack itemstack3 = itemstack2.copy();
                            itemstack3.stackSize = itemstack3.getMaxStackSize();
                            this.g.thePlayer.bK.setInventorySlotContents(par3, itemstack3);
                            this.g.thePlayer.bL.detectAndSendChanges();
                        }

                        return;
                    }

                    if (par4 == 3)
                    {
                        if ((inventoryplayer.getItemStack() == null) && (par1Slot.getHasStack()))
                        {
                            ItemStack itemstack3 = par1Slot.getStack().copy();
                            itemstack3.stackSize = itemstack3.getMaxStackSize();
                            inventoryplayer.setItemStack(itemstack3);
                        }

                        return;
                    }

                    if (par4 == 4)
                    {
                        if (itemstack2 != null)
                        {
                            ItemStack itemstack3 = itemstack2.copy();
                            itemstack3.stackSize = (par3 == 0 ? 1 : itemstack3.getMaxStackSize());
                            this.g.thePlayer.c(itemstack3);
                            this.g.playerController.a(itemstack3);
                        }

                        return;
                    }

                    if ((itemstack != null) && (itemstack2 != null) && (itemstack.isItemEqual(itemstack2)))
                    {
                        if (par3 == 0)
                        {
                            if (flag)
                            {
                                itemstack.stackSize = itemstack.getMaxStackSize();
                            }
                            else if (itemstack.stackSize < itemstack.getMaxStackSize())
                            {
                                itemstack.stackSize += 1;
                            }
                        }
                        else if (itemstack.stackSize <= 1)
                        {
                            inventoryplayer.setItemStack((ItemStack)null);
                        }
                        else
                        {
                            itemstack.stackSize -= 1;
                        }
                    }
                    else if ((itemstack2 != null) && (itemstack == null))
                    {
                        inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
                        itemstack = inventoryplayer.getItemStack();

                        if (flag)
                        {
                            itemstack.stackSize = itemstack.getMaxStackSize();
                        }
                    }
                    else
                    {
                        inventoryplayer.setItemStack((ItemStack)null);
                    }
                }
                else
                {
                    this.inventorySlots.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.g.thePlayer);

                    if (Container.func_94532_c(par3) == 2)
                    {
                        for (int l = 0; l < 9; l++)
                        {
                            this.g.playerController.a(this.inventorySlots.getSlot(45 + l).getStack(), 36 + l);
                        }
                    }

                    if (par1Slot != null)
                    {
                        ItemStack itemstack1 = this.inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                        this.g.playerController.a(itemstack1, par1Slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
                    }
                }
            }
        }
    }

    public void initGui()
    {
        if (this.g.playerController.h())
        {
            super.initGui();
            this.k.clear();
            Keyboard.enableRepeatEvents(true);
            this.searchField = new GuiTextField(this.m, this.e + 82, this.guiTop + 6, 89, this.m.FONT_HEIGHT);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            int i = selectedTabIndex;
            selectedTabIndex = -1;
            setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
            this.field_82324_x = new CreativeCrafting(this.g);
            this.g.thePlayer.bL.addCraftingToCrafters(this.field_82324_x);
            int tabCount = CreativeTabs.creativeTabArray.length;

            if (tabCount > 12)
            {
                this.k.add(new GuiButton(101, this.e - 25, this.guiTop - 23, 20, 20, "<"));
                this.k.add(new GuiButton(102, this.e + this.xSize + 5, this.guiTop - 23, 20, 20, ">"));
                this.maxPages = ((tabCount - 12) / 10 + 1);
            }

            GuiButton bookOfLore = new GuiButton(5, this.e + 8, this.guiTop + 132, 72, 20, StringTranslate.getInstance().translateKey("Book of Lore"));
            bookOfLore.enabled = false;
            this.k.add(bookOfLore);
            this.k.add(new GuiButton(6, this.e + 85, this.guiTop + 132, 48, 20, "Social"));
            this.k.add(new GuiButton(7, this.e + 138, this.guiTop + 132, 50, 20, "Donator"));
        }
        else
        {
            this.g.displayGuiScreen(new GuiInventory(this.g.thePlayer));
        }
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();

        if ((this.g.thePlayer != null) && (this.g.thePlayer.bK != null))
        {
            this.g.thePlayer.bL.removeCraftingFromCrafters(this.field_82324_x);
        }

        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char par1, int par2)
    {
        if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex())
        {
            if (GameSettings.isKeyDown(this.g.gameSettings.keyBindChat))
            {
                setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            }
            else
            {
                super.keyTyped(par1, par2);
            }
        }
        else
        {
            if (this.field_74234_w)
            {
                this.field_74234_w = false;
                this.searchField.setText("");
            }

            if (!checkHotbarKeys(par2))
            {
                if (this.searchField.textboxKeyTyped(par1, par2))
                {
                    updateCreativeSearch();
                }
                else
                {
                    super.keyTyped(par1, par2);
                }
            }
        }
    }

    private void updateCreativeSearch()
    {
        ContainerAetherCreative containercreative = (ContainerAetherCreative)this.inventorySlots;
        containercreative.itemList.clear();
        Item[] aitem = Item.itemsList;
        int i = aitem.length;

        for (int j = 0; j < i; j++)
        {
            Item item = aitem[j];

            if ((item != null) && (item.getCreativeTab() != null))
            {
                item.getSubItems(item.itemID, (CreativeTabs)null, containercreative.itemList);
            }
        }

        Enchantment[] aenchantment = Enchantment.enchantmentsList;
        i = aenchantment.length;

        for (j = 0; j < i; j++)
        {
            Enchantment enchantment = aenchantment[j];

            if ((enchantment != null) && (enchantment.type != null))
            {
                Item.enchantedBook.func_92113_a(enchantment, containercreative.itemList);
            }
        }

        Iterator iterator = containercreative.itemList.iterator();
        String s = this.searchField.getText().toLowerCase();

        while (iterator.hasNext())
        {
            ItemStack itemstack = (ItemStack)iterator.next();
            boolean flag = false;
            Iterator iterator1 = itemstack.getTooltip(this.g.thePlayer, this.g.gameSettings.advancedItemTooltips).iterator();

            while (iterator1.hasNext())
            {
                String s1 = (String)iterator1.next();

                if (s1.toLowerCase().contains(s))
                {
                    flag = true;
                }
            }

            if (!flag)
            {
                iterator.remove();
            }
        }

        this.currentScroll = 0.0F;
        containercreative.scrollTo(0.0F);
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];

        if ((creativetabs != null) && (creativetabs.drawInForegroundOfTab()))
        {
            this.m.drawString(creativetabs.getTranslatedTabLabel(), 8, 6, 4210752);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            int l = par1 - this.e;
            int i1 = par2 - this.guiTop;
            CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
            int j1 = acreativetabs.length;

            for (int k1 = 0; k1 < j1; k1++)
            {
                CreativeTabs creativetabs = acreativetabs[k1];

                if (func_74232_a(creativetabs, l, i1))
                {
                    return;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            int l = par1 - this.e;
            int i1 = par2 - this.guiTop;
            CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
            int j1 = acreativetabs.length;

            for (int k1 = 0; k1 < j1; k1++)
            {
                CreativeTabs creativetabs = acreativetabs[k1];

                if ((creativetabs != null) && (func_74232_a(creativetabs, l, i1)))
                {
                    setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    private boolean needsScrollBars()
    {
        if (CreativeTabs.creativeTabArray[selectedTabIndex] == null)
        {
            return false;
        }

        return (selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()) && (CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()) && (((ContainerAetherCreative)this.inventorySlots).hasMoreThan1PageOfItemsInList());
    }

    private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs)
    {
        if (par1CreativeTabs == null)
        {
            return;
        }

        int i = selectedTabIndex;
        selectedTabIndex = par1CreativeTabs.getTabIndex();
        ContainerAetherCreative containercreative = (ContainerAetherCreative)this.inventorySlots;
        this.field_94077_p.clear();
        containercreative.itemList.clear();
        par1CreativeTabs.displayAllReleventItems(containercreative.itemList);

        if (par1CreativeTabs == CreativeTabs.tabInventory)
        {
            Container container = this.player.inventoryContainer;

            if (this.backupContainerSlots == null)
            {
                this.backupContainerSlots = containercreative.inventorySlots;
            }

            containercreative.inventorySlots = new ArrayList();

            for (int j = 0; j < container.inventorySlots.size() - 8; j++)
            {
                SlotAetherCreativeInventory slotcreativeinventory = new SlotAetherCreativeInventory(this, (Slot)container.inventorySlots.get(j), j);
                containercreative.inventorySlots.add(slotcreativeinventory);

                if ((j >= 5) && (j < 9))
                {
                    int k = j - 5;
                    int l = k / 2;
                    int i1 = k % 2;
                    slotcreativeinventory.xDisplayPosition = (45 + l * 18);
                    slotcreativeinventory.yDisplayPosition = (6 + i1 * 27);
                }
                else if ((j >= 0) && (j < 5))
                {
                    slotcreativeinventory.yDisplayPosition = -2000;
                    slotcreativeinventory.xDisplayPosition = -2000;
                }
                else if (j < container.inventorySlots.size())
                {
                    int k = j - 9;
                    int l = k % 9;
                    int i1 = k / 9;
                    slotcreativeinventory.xDisplayPosition = (9 + l * 18);

                    if ((j >= 36) && (j <= 44))
                    {
                        slotcreativeinventory.yDisplayPosition = 112;
                    }
                    else if ((j > 44) && (j <= 48))
                    {
                        slotcreativeinventory.xDisplayPosition = (81 + l * 18);
                        slotcreativeinventory.yDisplayPosition = 6;
                    }
                    else if (j > 48)
                    {
                        slotcreativeinventory.xDisplayPosition = (81 + (l - 4) * 18);
                        slotcreativeinventory.yDisplayPosition = 33;
                    }
                    else
                    {
                        slotcreativeinventory.yDisplayPosition = (54 + i1 * 18);
                    }
                }
            }

            this.field_74235_v = new Slot(inventory, 0, 173, 112);
            containercreative.inventorySlots.add(this.field_74235_v);
        }
        else if (i == CreativeTabs.tabInventory.getTabIndex())
        {
            containercreative.inventorySlots = this.backupContainerSlots;
            this.backupContainerSlots = null;
        }

        if (this.searchField != null)
        {
            if (par1CreativeTabs == CreativeTabs.tabAllSearch)
            {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                updateCreativeSearch();
            }
            else
            {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }

        this.currentScroll = 0.0F;
        containercreative.scrollTo(0.0F);
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if ((i != 0) && (needsScrollBars()))
        {
            int j = ((ContainerAetherCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            this.currentScroll = ((float)(this.currentScroll - i / j));

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            ((ContainerAetherCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        boolean flag = Mouse.isButtonDown(0);
        int k = this.e;
        int l = this.guiTop;
        int i1 = k + 175;
        int j1 = l + 18;
        int k1 = i1 + 14;
        int l1 = j1 + 112;

        if ((!this.wasClicking) && (flag) && (par1 >= i1) && (par2 >= j1) && (par1 < k1) && (par2 < l1))
        {
            this.isScrolling = needsScrollBars();
        }

        if (!flag)
        {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling)
        {
            this.currentScroll = ((par2 - j1 - 7.5F) / (l1 - j1 - 15.0F));

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            ((ContainerAetherCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(par1, par2, par3);
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int start = tabPage * 10;
        int i2 = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);

        if (tabPage != 0)
        {
            start += 2;
        }

        boolean rendered = false;

        for (int j2 = start; j2 < i2; j2++)
        {
            CreativeTabs creativetabs = acreativetabs[j2];

            if ((creativetabs != null) && (renderCreativeInventoryHoveringText(creativetabs, par1, par2)))
            {
                rendered = true;
                break;
            }
        }

        if ((!rendered) && (!renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, par1, par2)))
        {
            renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, par1, par2);
        }

        if ((this.field_74235_v != null) && (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) && (isPointInRegion(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, par1, par2)))
        {
            drawCreativeTabHoveringText(StringTranslate.getInstance().translateKey("inventory.binSlot"), par1, par2);
        }

        if (this.maxPages != 0)
        {
            String page = String.format("%d / %d", new Object[] { Integer.valueOf(tabPage + 1), Integer.valueOf(this.maxPages + 1) });
            int width = this.m.getStringWidth(page);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.zLevel = 300.0F;
            a.field_94147_f = 300.0F;
            this.m.drawString(page, this.e + this.xSize / 2 - width / 2, this.guiTop - 38, -1);
            this.zLevel = 0.0F;
            a.field_94147_f = 0.0F;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int k = acreativetabs.length;
        int start = tabPage * 10;
        k = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);

        if (tabPage != 0)
        {
            start += 2;
        }

        for (int l = start; l < k; l++)
        {
            CreativeTabs creativetabs1 = acreativetabs[l];
            this.g.renderEngine.b("/gui/allitems.png");

            if ((creativetabs1 != null) && (creativetabs1.getTabIndex() != selectedTabIndex))
            {
                renderCreativeTab(creativetabs1);
            }
        }

        if (tabPage != 0)
        {
            if (creativetabs != CreativeTabs.tabAllSearch)
            {
                this.g.renderEngine.b("/gui/allitems.png");
                renderCreativeTab(CreativeTabs.tabAllSearch);
            }

            if (creativetabs != CreativeTabs.tabInventory)
            {
                this.g.renderEngine.b("/gui/allitems.png");
                renderCreativeTab(CreativeTabs.tabInventory);
            }
        }

        this.g.renderEngine.b("/net/aetherteam/aether/client/sprites/gui/creative/" + creativetabs.getBackgroundImageName());
        drawTexturedModalRect(this.e, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i1 = this.e + 175;
        k = this.guiTop + 18;
        l = k + 112;
        this.g.renderEngine.b("/gui/allitems.png");

        if (creativetabs.shouldHidePlayerInventory())
        {
            drawTexturedModalRect(i1, k + (int)((l - k - 17) * this.currentScroll), 'è' + (needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if ((creativetabs == null) || (creativetabs.getTabPage() != tabPage))
        {
            if ((creativetabs != CreativeTabs.tabAllSearch) && (creativetabs != CreativeTabs.tabInventory))
            {
                return;
            }
        }

        renderCreativeTab(creativetabs);

        if (creativetabs == CreativeTabs.tabInventory)
        {
            GuiInventory.drawPlayerOnGui(this.g, this.e + 25, this.guiTop + 46, 20, this.e + 43 - par2, this.guiTop + 45 - 30 - par3);
        }
    }

    protected boolean func_74232_a(CreativeTabs par1CreativeTabs, int par2, int par3)
    {
        if (par1CreativeTabs.getTabPage() != tabPage)
        {
            if ((par1CreativeTabs != CreativeTabs.tabAllSearch) && (par1CreativeTabs != CreativeTabs.tabInventory))
            {
                return false;
            }
        }

        int k = par1CreativeTabs.getTabColumn();
        int l = 28 * k;
        byte b0 = 0;

        if (k == 5)
        {
            l = this.xSize - 28 + 2;
        }
        else if (k > 0)
        {
            l += k;
        }

        int i1;
        int i1;

        if (par1CreativeTabs.isTabInFirstRow())
        {
            i1 = b0 - 32;
        }
        else
        {
            i1 = b0 + this.ySize;
        }

        return (par2 >= l) && (par2 <= l + 28) && (par3 >= i1) && (par3 <= i1 + 32);
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs par1CreativeTabs, int par2, int par3)
    {
        int k = par1CreativeTabs.getTabColumn();
        int l = 28 * k;
        byte b0 = 0;

        if (k == 5)
        {
            l = this.xSize - 28 + 2;
        }
        else if (k > 0)
        {
            l += k;
        }

        int i1;
        int i1;

        if (par1CreativeTabs.isTabInFirstRow())
        {
            i1 = b0 - 32;
        }
        else
        {
            i1 = b0 + this.ySize;
        }

        if (isPointInRegion(l + 3, i1 + 3, 23, 27, par2, par3))
        {
            drawCreativeTabHoveringText(par1CreativeTabs.getTranslatedTabLabel(), par2, par3);
            return true;
        }

        return false;
    }

    protected void renderCreativeTab(CreativeTabs par1CreativeTabs)
    {
        boolean flag = par1CreativeTabs.getTabIndex() == selectedTabIndex;
        boolean flag1 = par1CreativeTabs.isTabInFirstRow();
        int i = par1CreativeTabs.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.e + 28 * i;
        int i1 = this.guiTop;
        byte b0 = 32;

        if (flag)
        {
            k += 32;
        }

        if (i == 5)
        {
            l = this.e + this.xSize - 28;
        }
        else if (i > 0)
        {
            l += i;
        }

        if (flag1)
        {
            i1 -= 28;
        }
        else
        {
            k += 64;
            i1 += this.ySize - 4;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        drawTexturedModalRect(l, i1, j, k, 28, b0);
        this.zLevel = 100.0F;
        a.field_94147_f = 100.0F;
        l += 6;
        i1 += 8 + (flag1 ? 1 : -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ItemStack itemstack = par1CreativeTabs.getIconItemStack();
        a.b(this.m, this.g.renderEngine, itemstack, l, i1);
        a.c(this.m, this.g.renderEngine, itemstack, l, i1);
        GL11.glDisable(GL11.GL_LIGHTING);
        a.field_94147_f = 0.0F;
        this.zLevel = 0.0F;
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            this.g.displayGuiScreen(new GuiAchievements(this.g.statFileWriter));
        }

        if (par1GuiButton.id == 1)
        {
            this.g.displayGuiScreen(new GuiStats(this, this.g.statFileWriter));
        }

        if (par1GuiButton.id == 101)
        {
            tabPage = Math.max(tabPage - 1, 0);
        }
        else if (par1GuiButton.id == 102)
        {
            tabPage = Math.min(tabPage + 1, this.maxPages);
        }

        if (par1GuiButton.id == 5)
        {
            int guiID = AetherGuiHandler.realLoreID;
            this.g.displayGuiScreen(new GuiLore(this.g.thePlayer.bK, this.g.thePlayer, 0));
        }

        if (par1GuiButton.id == 6)
        {
            this.g.displayGuiScreen(new GuiMenu(this.player, this));
        }

        if (par1GuiButton.id == 7)
        {
            this.g.displayGuiScreen(new GuiDonatorMenu(this.player, this));
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

