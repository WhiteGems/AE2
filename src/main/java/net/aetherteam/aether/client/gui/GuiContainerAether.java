package net.aetherteam.aether.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerAether extends GuiScreen
{
    protected static RenderItem itemRenderer = new RenderItem();

    protected int xSize = 176;

    protected int ySize = 166;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;
    private Slot theSlot;
    private Slot clickedSlot = null;

    private boolean isRightMouseClick = false;

    private ItemStack draggedStack = null;
    private int field_85049_r = 0;
    private int field_85048_s = 0;
    private Slot returningStackDestSlot = null;
    private long returningStackTime = 0L;

    private ItemStack returningStack = null;
    private Slot field_92033_y = null;
    private long field_92032_z = 0L;
    protected final Set field_94077_p = new HashSet();
    protected boolean field_94076_q;
    private int field_94071_C = 0;
    private int field_94067_D = 0;
    private boolean field_94068_E = false;
    private int field_94069_F;
    private long field_94070_G = 0L;
    private Slot field_94072_H = null;
    private int field_94073_I = 0;
    private boolean field_94074_J;
    private ItemStack field_94075_K = null;

    public GuiContainerAether(Container par1Container)
    {
        this.inventorySlots = par1Container;
        this.field_94068_E = true;
    }

    public void initGui()
    {
        super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = ((this.width - this.xSize) / 2);
        this.guiTop = ((this.height - this.ySize) / 2);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        int k = this.guiLeft;
        int l = this.guiTop;
        drawGuiContainerBackgroundLayer(par3, par1, par2);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(par1, par2, par3);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(k, l, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.theSlot = null;
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int j1 = 0; j1 < this.inventorySlots.inventorySlots.size(); j1++)
        {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(j1);
            drawSlotInventory(slot);

            if (isMouseOverSlot(slot, par1, par2))
            {
                this.theSlot = slot;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int k1 = slot.xDisplayPosition;
                int i1 = slot.yDisplayPosition;
                drawGradientRect(k1, i1, k1 + 16, i1 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        drawGuiContainerForegroundLayer(par1, par2);
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;

        if (itemstack != null)
        {
            byte b0 = 8;
            int i1 = this.draggedStack == null ? 8 : 16;
            String s = null;

            if ((this.draggedStack != null) && (this.isRightMouseClick))
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int(itemstack.stackSize / 2.0F);
            } else if ((this.field_94076_q) && (this.field_94077_p.size() > 1))
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.field_94069_F;

                if (itemstack.stackSize == 0)
                {
                    s = "" + EnumChatFormatting.YELLOW + "0";
                }
            }

            drawItemStack(itemstack, par1 - k - b0, par2 - l - i1, s);
        }

        if (this.returningStack != null)
        {
            float f1 = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f1 >= 1.0F)
            {
                f1 = 1.0F;
                this.returningStack = null;
            }

            int i1 = this.returningStackDestSlot.xDisplayPosition - this.field_85049_r;
            int l1 = this.returningStackDestSlot.yDisplayPosition - this.field_85048_s;
            int i2 = this.field_85049_r + (int) (i1 * f1);
            int j2 = this.field_85048_s + (int) (l1 * f1);
            drawItemStack(this.returningStack, i2, j2, (String) null);
        }

        GL11.glPopMatrix();

        if ((inventoryplayer.getItemStack() == null) && (this.theSlot != null) && (this.theSlot.getHasStack()))
        {
            ItemStack itemstack1 = this.theSlot.getStack();
            drawItemStackTooltip(itemstack1, par1, par2);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack par1ItemStack, int par2, int par3, String par4Str)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3, par4Str);
        itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3 - (this.draggedStack == null ? 0 : 8));
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3)
    {
        List list = par1ItemStack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); k++)
        {
            if (k == 0)
            {
                list.set(k, "§" + Integer.toHexString(par1ItemStack.getRarity().rarityColor) + (String) list.get(k));
            } else
            {
                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }

        func_102021_a(list, par2, par3);
    }

    protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3)
    {
        func_102021_a(Arrays.asList(new String[]{par1Str}), par2, par3);
    }

    protected void func_102021_a(List par1List, int par2, int par3)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String) iterator.next();
                int l = this.fontRenderer.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            if (i1 + k > this.width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > this.height)
            {
                j1 = this.height - k1 - 6;
            }

            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 0xFEFEFE) >> 1 | i2 & 0xFF000000;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); k2++)
            {
                String s1 = (String) par1List.get(k2);
                this.fontRenderer.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float paramFloat, int paramInt1, int paramInt2);

    protected void drawSlotInventory(Slot par1Slot)
    {
        int i = par1Slot.xDisplayPosition;
        int j = par1Slot.yDisplayPosition;
        ItemStack itemstack = par1Slot.getStack();
        boolean flag = false;
        boolean flag1 = (par1Slot == this.clickedSlot) && (this.draggedStack != null) && (!this.isRightMouseClick);
        ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
        String s = null;

        if ((par1Slot == this.clickedSlot) && (this.draggedStack != null) && (this.isRightMouseClick) && (itemstack != null))
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        } else if ((this.field_94076_q) && (this.field_94077_p.contains(par1Slot)) && (itemstack1 != null))
        {
            if (this.field_94077_p.size() == 1)
            {
                return;
            }

            if ((Container.func_94527_a(par1Slot, itemstack1, true)) && (this.inventorySlots.func_94531_b(par1Slot)))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.func_94525_a(this.field_94077_p, this.field_94071_C, itemstack, par1Slot.getStack() == null ? 0 : par1Slot.getStack().stackSize);

                if (itemstack.stackSize > itemstack.getMaxStackSize())
                {
                    s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }

                if (itemstack.stackSize > par1Slot.getSlotStackLimit())
                {
                    s = EnumChatFormatting.YELLOW + "" + par1Slot.getSlotStackLimit();
                    itemstack.stackSize = par1Slot.getSlotStackLimit();
                }
            } else
            {
                this.field_94077_p.remove(par1Slot);
                func_94066_g();
            }
        }

        this.zLevel = 100.0F;
        itemRenderer.zLevel = 100.0F;

        if (itemstack == null)
        {
            Icon icon = par1Slot.getBackgroundIconIndex();

            if (icon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.renderEngine.bindTexture("/gui/items.png");
                drawTexturedModelRectFromIcon(i, j, icon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i, j);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i, j, s);
        }

        itemRenderer.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private void func_94066_g()
    {
        ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

        if ((itemstack != null) && (this.field_94076_q))
        {
            this.field_94069_F = itemstack.stackSize;
            ItemStack itemstack1;
            int i;
            for (Iterator iterator = this.field_94077_p.iterator(); iterator.hasNext(); this.field_94069_F -= itemstack1.stackSize - i)
            {
                Slot slot = (Slot) iterator.next();
                itemstack1 = itemstack.copy();
                i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                Container.func_94525_a(this.field_94077_p, this.field_94071_C, itemstack1, i);

                if (itemstack1.stackSize > itemstack1.getMaxStackSize())
                {
                    itemstack1.stackSize = itemstack1.getMaxStackSize();
                }

                if (itemstack1.stackSize > slot.getSlotStackLimit())
                {
                    itemstack1.stackSize = slot.getSlotStackLimit();
                }
            }
        }
    }

    private Slot getSlotAtPosition(int par1, int par2)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); k++)
        {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

            if (isMouseOverSlot(slot, par1, par2))
            {
                return slot;
            }
        }

        return null;
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        boolean flag = par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100;
        Slot slot = getSlotAtPosition(par1, par2);
        long l = Minecraft.getSystemTime();
        this.field_94074_J = ((this.field_94072_H == slot) && (l - this.field_94070_G < 250L) && (this.field_94073_I == par3));
        this.field_94068_E = false;

        if ((par3 == 0) || (par3 == 1) || (flag))
        {
            int i1 = this.guiLeft;
            int j1 = this.guiTop;
            boolean flag1 = (par1 < i1) || (par2 < j1) || (par1 >= i1 + this.xSize) || (par2 >= j1 + this.ySize);
            int k1 = -1;

            if (slot != null)
            {
                k1 = slot.slotNumber;
            }

            if (flag1)
            {
                k1 = -999;
            }

            if ((this.mc.gameSettings.touchscreen) && (flag1) && (this.mc.thePlayer.inventory.getItemStack() == null))
            {
                this.mc.displayGuiScreen((GuiScreen) null);
                return;
            }

            if (k1 != -1)
            {
                if (this.mc.gameSettings.touchscreen)
                {
                    if ((slot != null) && (slot.getHasStack()))
                    {
                        this.clickedSlot = slot;
                        this.draggedStack = null;
                        this.isRightMouseClick = (par3 == 1);
                    } else
                    {
                        this.clickedSlot = null;
                    }
                } else if (!this.field_94076_q)
                {
                    if (this.mc.thePlayer.inventory.getItemStack() == null)
                    {
                        if (par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100)
                        {
                            handleMouseClick(slot, k1, par3, 3);
                        } else
                        {
                            boolean flag2 = (k1 != -999) && ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)));
                            byte b0 = 0;

                            if (flag2)
                            {
                                this.field_94075_K = ((slot != null) && (slot.getHasStack()) ? slot.getStack() : null);
                                b0 = 1;
                            } else if (k1 == -999)
                            {
                                b0 = 4;
                            }

                            handleMouseClick(slot, k1, par3, b0);
                        }

                        this.field_94068_E = true;
                    } else
                    {
                        this.field_94076_q = true;
                        this.field_94067_D = par3;
                        this.field_94077_p.clear();

                        if (par3 == 0)
                        {
                            this.field_94071_C = 0;
                        } else if (par3 == 1)
                        {
                            this.field_94071_C = 1;
                        }
                    }
                }
            }
        }

        this.field_94072_H = slot;
        this.field_94070_G = l;
        this.field_94073_I = par3;
    }

    protected void func_85041_a(int par1, int par2, int par3, long par4)
    {
        Slot slot = getSlotAtPosition(par1, par2);
        ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

        if ((this.clickedSlot != null) && (this.mc.gameSettings.touchscreen))
        {
            if ((par3 == 0) || (par3 == 1))
            {
                if (this.draggedStack == null)
                {
                    if (slot != this.clickedSlot)
                    {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                } else if ((this.draggedStack.stackSize > 1) && (slot != null) && (Container.func_94527_a(slot, this.draggedStack, false)))
                {
                    long i1 = Minecraft.getSystemTime();

                    if (this.field_92033_y == slot)
                    {
                        if (i1 - this.field_92032_z > 500L)
                        {
                            handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            handleMouseClick(slot, slot.slotNumber, 1, 0);
                            handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
                            this.field_92032_z = (i1 + 750L);
                            this.draggedStack.stackSize -= 1;
                        }
                    } else
                    {
                        this.field_92033_y = slot;
                        this.field_92032_z = i1;
                    }
                }
            }
        } else if ((this.field_94076_q) && (slot != null) && (itemstack != null) && (itemstack.stackSize > this.field_94077_p.size()) && (Container.func_94527_a(slot, itemstack, true)) && (slot.isItemValid(itemstack)) && (this.inventorySlots.func_94531_b(slot)))
        {
            this.field_94077_p.add(slot);
            func_94066_g();
        }
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        Slot slot = getSlotAtPosition(par1, par2);
        int l = this.guiLeft;
        int i1 = this.guiTop;
        boolean flag = (par1 < l) || (par2 < i1) || (par1 >= l + this.xSize) || (par2 >= i1 + this.ySize);
        int j1 = -1;

        if (slot != null)
        {
            j1 = slot.slotNumber;
        }

        if (flag)
        {
            j1 = -999;
        }

        if ((this.field_94074_J) && (slot != null) && (par3 == 0) && (this.inventorySlots.func_94530_a((ItemStack) null, slot)))
        {
            if (isShiftKeyDown())
            {
                if ((slot != null) && (slot.inventory != null) && (this.field_94075_K != null))
                {
                    Iterator iterator = this.inventorySlots.inventorySlots.iterator();

                    while (iterator.hasNext())
                    {
                        Slot slot1 = (Slot) iterator.next();

                        if ((slot1 != null) && (slot1.canTakeStack(this.mc.thePlayer)) && (slot1.getHasStack()) && (slot1.inventory == slot.inventory) && (Container.func_94527_a(slot1, this.field_94075_K, true)))
                        {
                            handleMouseClick(slot1, slot1.slotNumber, par3, 1);
                        }
                    }
                }
            } else
            {
                handleMouseClick(slot, j1, par3, 6);
            }

            this.field_94074_J = false;
            this.field_94070_G = 0L;
        } else
        {
            if ((this.field_94076_q) && (this.field_94067_D != par3))
            {
                this.field_94076_q = false;
                this.field_94077_p.clear();
                this.field_94068_E = true;
                return;
            }

            if (this.field_94068_E)
            {
                this.field_94068_E = false;
                return;
            }

            if ((this.clickedSlot != null) && (this.mc.gameSettings.touchscreen))
            {
                if ((par3 == 0) || (par3 == 1))
                {
                    if ((this.draggedStack == null) && (slot != this.clickedSlot))
                    {
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    boolean flag1 = Container.func_94527_a(slot, this.draggedStack, false);

                    if ((j1 != -1) && (this.draggedStack != null) && (flag1))
                    {
                        handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
                        handleMouseClick(slot, j1, 0, 0);

                        if (this.mc.thePlayer.inventory.getItemStack() != null)
                        {
                            handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
                            this.field_85049_r = (par1 - l);
                            this.field_85048_s = (par2 - i1);
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        } else
                        {
                            this.returningStack = null;
                        }
                    } else if (this.draggedStack != null)
                    {
                        this.field_85049_r = (par1 - l);
                        this.field_85048_s = (par2 - i1);
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }

                    this.draggedStack = null;
                    this.clickedSlot = null;
                }
            } else if ((this.field_94076_q) && (!this.field_94077_p.isEmpty()))
            {
                handleMouseClick((Slot) null, -999, Container.func_94534_d(0, this.field_94071_C), 5);
                Iterator iterator = this.field_94077_p.iterator();

                while (iterator.hasNext())
                {
                    Slot slot1 = (Slot) iterator.next();
                    handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.field_94071_C), 5);
                }

                handleMouseClick((Slot) null, -999, Container.func_94534_d(2, this.field_94071_C), 5);
            } else if (this.mc.thePlayer.inventory.getItemStack() != null)
            {
                if (par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100)
                {
                    handleMouseClick(slot, j1, par3, 3);
                } else
                {
                    boolean flag1 = (j1 != -999) && ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)));

                    if (flag1)
                    {
                        this.field_94075_K = ((slot != null) && (slot.getHasStack()) ? slot.getStack() : null);
                    }

                    handleMouseClick(slot, j1, par3, flag1 ? 1 : 0);
                }
            }
        }

        if (this.mc.thePlayer.inventory.getItemStack() == null)
        {
            this.field_94070_G = 0L;
        }

        this.field_94076_q = false;
    }

    private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
    {
        return isPointInRegion(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
    }

    protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int k1 = this.guiLeft;
        int l1 = this.guiTop;
        par5 -= k1;
        par6 -= l1;
        return (par5 >= par1 - 1) && (par5 < par1 + par3 + 1) && (par6 >= par2 - 1) && (par6 < par2 + par4 + 1);
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        if (par1Slot != null)
        {
            par2 = par1Slot.slotNumber;
        }

        this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4, this.mc.thePlayer);
    }

    protected void keyTyped(char par1, int par2)
    {
        if ((par2 == 1) || (par2 == this.mc.gameSettings.keyBindInventory.keyCode))
        {
            this.mc.thePlayer.closeScreen();
        }

        checkHotbarKeys(par2);

        if ((this.theSlot != null) && (this.theSlot.getHasStack()))
        {
            if (par2 == this.mc.gameSettings.keyBindPickBlock.keyCode)
            {
                handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
            } else if (par2 == this.mc.gameSettings.keyBindDrop.keyCode)
            {
                handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }

    protected boolean checkHotbarKeys(int par1)
    {
        if ((this.mc.thePlayer.inventory.getItemStack() == null) && (this.theSlot != null))
        {
            for (int j = 0; j < 9; j++)
            {
                if (par1 == 2 + j)
                {
                    handleMouseClick(this.theSlot, this.theSlot.slotNumber, j, 2);
                    return true;
                }
            }
        }

        return false;
    }

    public void onGuiClosed()
    {
        if (this.mc.thePlayer != null)
        {
            this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateScreen()
    {
        super.updateScreen();

        if ((!this.mc.thePlayer.isEntityAlive()) || (this.mc.thePlayer.isDead))
        {
            this.mc.thePlayer.closeScreen();
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.client.gui.GuiContainerAether
 * JD-Core Version:    0.6.2
 */