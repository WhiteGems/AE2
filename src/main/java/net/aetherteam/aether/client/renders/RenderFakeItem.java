package net.aetherteam.aether.client.renders;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.entities.altar.EntityFakeItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderFakeItem extends RenderManager
{
    private RenderEngine itemRenderBlocks = new RenderEngine();

    private Random random = new Random();
    public boolean renderWithColor = true;

    public float zLevel = 0.0F;
    public static boolean renderInFrame = false;

    public RenderFakeItem()
    {
        this.d = 0.15F;
        this.e = 0.75F;
    }

    public void doRenderItem(EntityFakeItem par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.random.setSeed(187L);
        ItemStack itemstack = par1Entity.getEntityItem();

        if (itemstack.getItem() != null)
        {
            GL11.glPushMatrix();
            float f2 = shouldBob() ? MathHelper.sin((par1Entity.age + par9) / 10.0F + par1Entity.hoverStart) * 0.1F + 0.1F : 0.0F;
            float f3 = ((par1Entity.age + par9) / 20.0F + par1Entity.hoverStart) * (180F / (float)Math.PI);
            byte b0 = getMiniBlockCount(itemstack);
            GL11.glTranslatef((float)par2, (float)par4 + f2, (float)par6);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            Block block = null;

            if (itemstack.itemID < Block.blocksList.length)
            {
                block = Block.blocksList[itemstack.itemID];
            }

            if (!ForgeHooksClient.renderEntityItem(par1Entity, itemstack, f2, f3, this.random, this.b.e, this.c))
            {
                if ((itemstack.getItemSpriteNumber() == 0) && (block != null) && (RenderEngine.a(Block.blocksList[itemstack.itemID].getRenderType())))
                {
                    GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);

                    if (renderInFrame)
                    {
                        GL11.glScalef(1.25F, 1.25F, 1.25F);
                        GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    }

                    a("/terrain.png");
                    float f7 = 0.25F;
                    int j = block.getRenderType();

                    if ((j == 1) || (j == 19) || (j == 12) || (j == 2))
                    {
                        f7 = 0.5F;
                    }

                    GL11.glScalef(f7, f7, f7);

                    for (int i = 0; i < b0; i++)
                    {
                        GL11.glPushMatrix();

                        if (i > 0)
                        {
                            float f5 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            float f4 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f7;
                            GL11.glTranslatef(f5, f4, f6);
                        }

                        float f5 = 1.0F;
                        this.itemRenderBlocks.a(block, itemstack.getItemDamage(), f5);
                        GL11.glPopMatrix();
                    }
                }
                else if (itemstack.getItem().requiresMultipleRenderPasses())
                {
                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    a("/gui/items.png");

                    for (int k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); k++)
                    {
                        this.random.setSeed(187L);
                        Icon icon = itemstack.getItem().getIcon(itemstack, k);
                        float f8 = 1.0F;

                        if (this.renderWithColor)
                        {
                            int i = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, k);
                            float f5 = (i >> 16 & 0xFF) / 255.0F;
                            float f4 = (i >> 8 & 0xFF) / 255.0F;
                            float f6 = (i & 0xFF) / 255.0F;
                            GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                            renderDroppedItem(par1Entity, icon, b0, par9, f5 * f8, f4 * f8, f6 * f8);
                        }
                        else
                        {
                            renderDroppedItem(par1Entity, icon, b0, par9, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }
                else
                {
                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    Icon icon1 = itemstack.getIconIndex();

                    if (itemstack.getItemSpriteNumber() == 0)
                    {
                        a("/terrain.png");
                    }
                    else
                    {
                        a("/gui/items.png");
                    }

                    if (this.renderWithColor)
                    {
                        int l = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
                        float f8 = (l >> 16 & 0xFF) / 255.0F;
                        float f9 = (l >> 8 & 0xFF) / 255.0F;
                        float f5 = (l & 0xFF) / 255.0F;
                        float f4 = 1.0F;
                        renderDroppedItem(par1Entity, icon1, b0, par9, f8 * f4, f9 * f4, f5 * f4);
                    }
                    else
                    {
                        renderDroppedItem(par1Entity, icon1, b0, par9, 1.0F, 1.0F, 1.0F);
                    }
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7)
    {
        Rect2i tessellator = Rect2i.rectX;

        if (par2Icon == null)
        {
            par2Icon = this.b.e.b(par1EntityItem.getEntityItem().getItemSpriteNumber());
        }

        float f4 = par2Icon.getMinU();
        float f5 = par2Icon.getMaxU();
        float f6 = par2Icon.getMinV();
        float f7 = par2Icon.getMaxV();
        float f8 = 1.0F;
        float f9 = 0.5F;
        float f10 = 0.25F;

        if (this.b.l.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef(((par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float f12 = 0.0625F;
            float f11 = 0.021875F;
            ItemStack itemstack = par1EntityItem.getEntityItem();
            int j = itemstack.stackSize;
            byte b0 = getMiniItemCount(itemstack);
            GL11.glTranslatef(-f9, -f10, -((f12 + f11) * b0 / 2.0F));

            for (int k = 0; k < b0; k++)
            {
                if ((k > 0) && (shouldSpreadItems()))
                {
                    float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(x, y, f12 + f11);
                }
                else
                {
                    GL11.glTranslatef(0.0F, 0.0F, f12 + f11);
                }

                if (itemstack.getItemSpriteNumber() == 0)
                {
                    a("/terrain.png");
                }
                else
                {
                    a("/gui/items.png");
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                IImageBuffer.a(tessellator, f5, f6, f4, f7, par2Icon.getSheetWidth(), par2Icon.getSheetHeight(), f12);

                if ((itemstack != null) && (itemstack.hasEffect()))
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.b.e.b("%blur%/misc/glint.png");
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f13 = 0.76F;
                    GL11.glColor4f(0.5F * f13, 0.25F * f13, 0.8F * f13, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f14 = 0.125F;
                    GL11.glScalef(f14, f14, f14);
                    float f15 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f15, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    IImageBuffer.a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f14, f14, f14);
                    f15 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f15, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    IImageBuffer.a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f12);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int l = 0; l < par3; l++)
            {
                GL11.glPushMatrix();

                if (l > 0)
                {
                    float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f11, f16, f17);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180.0F - this.b.j, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                tessellator.b();
                tessellator.b(0.0F, 1.0F, 0.0F);
                tessellator.a(0.0F - f9, 0.0F - f10, 0.0D, f4, f7);
                tessellator.a(f8 - f9, 0.0F - f10, 0.0D, f5, f7);
                tessellator.a(f8 - f9, 1.0F - f10, 0.0D, f5, f6);
                tessellator.a(0.0F - f9, 1.0F - f10, 0.0D, f4, f6);
                tessellator.getRectX();
                GL11.glPopMatrix();
            }
        }
    }

    public void renderItemIntoGUI(FontRenderer par1FontRenderer, Tessellator par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        int k = par3ItemStack.itemID;
        int l = par3ItemStack.getItemDamage();
        Icon icon = par3ItemStack.getIconIndex();
        Block block = k < Block.blocksList.length ? Block.blocksList[k] : null;

        if ((par3ItemStack.getItemSpriteNumber() == 0) && (block != null) && (RenderEngine.a(Block.blocksList[k].getRenderType())))
        {
            par2RenderEngine.b("/terrain.png");
            GL11.glPushMatrix();
            GL11.glTranslatef(par4 - 2, par5 + 3, -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int i1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, 0);
            float f2 = (i1 >> 16 & 0xFF) / 255.0F;
            float f = (i1 >> 8 & 0xFF) / 255.0F;
            float f1 = (i1 & 0xFF) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(f2, f, f1, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.itemRenderBlocks.textureNameToImageMap = this.renderWithColor;
            this.itemRenderBlocks.a(block, l, 1.0F);
            this.itemRenderBlocks.textureNameToImageMap = true;
            GL11.glPopMatrix();
        }
        else if (Item.itemsList[k].requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            par2RenderEngine.b(par3ItemStack.getItemSpriteNumber() == 0 ? "/terrain.png" : "/gui/items.png");

            for (int j1 = 0; j1 < Item.itemsList[k].getRenderPasses(l); j1++)
            {
                Icon icon1 = Item.itemsList[k].getIcon(par3ItemStack, j1);
                int k1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, j1);
                float f = (k1 >> 16 & 0xFF) / 255.0F;
                float f1 = (k1 >> 8 & 0xFF) / 255.0F;
                float f3 = (k1 & 0xFF) / 255.0F;

                if (this.renderWithColor)
                {
                    GL11.glColor4f(f, f1, f3, 1.0F);
                }

                renderIcon(par4, par5, icon1, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);

            if (par3ItemStack.getItemSpriteNumber() == 0)
            {
                par2RenderEngine.b("/terrain.png");
            }
            else
            {
                par2RenderEngine.b("/gui/items.png");
            }

            if (icon == null)
            {
                icon = par2RenderEngine.b(par3ItemStack.getItemSpriteNumber());
            }

            int j1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, 0);
            float f4 = (j1 >> 16 & 0xFF) / 255.0F;
            float f2 = (j1 >> 8 & 0xFF) / 255.0F;
            float f = (j1 & 0xFF) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(f4, f2, f, 1.0F);
            }

            renderIcon(par4, par5, icon, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, Tessellator par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        if (par3ItemStack != null)
        {
            if (!ForgeHooksClient.renderInventoryItem(this.c, par2RenderEngine, par3ItemStack, this.renderWithColor, this.zLevel, par4, par5))
            {
                renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);
            }

            if (par3ItemStack.hasEffect())
            {
                GL11.glDepthFunc(GL11.GL_GREATER);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                par2RenderEngine.b("%blur%/misc/glint.png");
                this.zLevel -= 50.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                renderGlint(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                this.zLevel += 50.0F;
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
        }
    }

    private void renderGlint(int par1, int par2, int par3, int par4, int par5)
    {
        for (int j1 = 0; j1 < 2; j1++)
        {
            if (j1 == 0)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if (j1 == 1)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float f = 0.0039063F;
            float f1 = 0.0039063F;
            float f2 = (float)(Minecraft.getSystemTime() % (3000 + j1 * 1873)) / (3000.0F + j1 * 1873) * 256.0F;
            float f3 = 0.0F;
            Rect2i tessellator = Rect2i.rectX;
            float f4 = 4.0F;

            if (j1 == 1)
            {
                f4 = -1.0F;
            }

            tessellator.b();
            tessellator.a(par2 + 0, par3 + par5, this.zLevel, (f2 + par5 * f4) * f, (f3 + par5) * f1);
            tessellator.a(par2 + par4, par3 + par5, this.zLevel, (f2 + par4 + par5 * f4) * f, (f3 + par5) * f1);
            tessellator.a(par2 + par4, par3 + 0, this.zLevel, (f2 + par4) * f, (f3 + 0.0F) * f1);
            tessellator.a(par2 + 0, par3 + 0, this.zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
            tessellator.getRectX();
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, Tessellator par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        renderItemStack(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5, (String)null);
    }

    public void renderItemStack(FontRenderer par1FontRenderer, Tessellator par2RenderEngine, ItemStack par3ItemStack, int par4, int par5, String par6Str)
    {
        if (par3ItemStack != null)
        {
            if ((par3ItemStack.stackSize > 1) || (par6Str != null))
            {
                String s1 = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                par1FontRenderer.drawStringWithShadow(s1, par4 + 19 - 2 - par1FontRenderer.getStringWidth(s1), par5 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (par3ItemStack.isItemDamaged())
            {
                int k = (int)Math.round(13.0D - par3ItemStack.getItemDamageForDisplay() * 13.0D / par3ItemStack.getMaxDamage());
                int l = (int)Math.round(255.0D - par3ItemStack.getItemDamageForDisplay() * 255.0D / par3ItemStack.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Rect2i tessellator = Rect2i.rectX;
                int i1 = 255 - l << 16 | l << 8;
                int j1 = (255 - l) / 4 << 16 | 0x3F00;
                renderQuad(tessellator, par4 + 2, par5 + 13, 13, 2, 0);
                renderQuad(tessellator, par4 + 2, par5 + 13, 12, 1, j1);
                renderQuad(tessellator, par4 + 2, par5 + 13, k, 1, i1);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderQuad(Rect2i par1Tessellator, int par2, int par3, int par4, int par5, int par6)
    {
        par1Tessellator.b();
        par1Tessellator.d(par6);
        par1Tessellator.a(par2 + 0, par3 + 0, 0.0D);
        par1Tessellator.a(par2 + 0, par3 + par5, 0.0D);
        par1Tessellator.a(par2 + par4, par3 + par5, 0.0D);
        par1Tessellator.a(par2 + par4, par3 + 0, 0.0D);
        par1Tessellator.getRectX();
    }

    public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
    {
        Rect2i tessellator = Rect2i.rectX;
        tessellator.b();
        tessellator.a(par1 + 0, par2 + par5, this.zLevel, par3Icon.getMinU(), par3Icon.getMaxV());
        tessellator.a(par1 + par4, par2 + par5, this.zLevel, par3Icon.getMaxU(), par3Icon.getMaxV());
        tessellator.a(par1 + par4, par2 + 0, this.zLevel, par3Icon.getMaxU(), par3Icon.getMinV());
        tessellator.a(par1 + 0, par2 + 0, this.zLevel, par3Icon.getMinU(), par3Icon.getMinV());
        tessellator.getRectX();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderItem((EntityFakeItem)par1Entity, par2, par4, par6, par8, par9);
    }

    public boolean shouldSpreadItems()
    {
        return true;
    }

    public boolean shouldBob()
    {
        return true;
    }

    public byte getMiniBlockCount(ItemStack stack)
    {
        byte ret = 1;

        if (stack.stackSize > 1)
        {
            ret = 2;
        }

        if (stack.stackSize > 5)
        {
            ret = 3;
        }

        if (stack.stackSize > 20)
        {
            ret = 4;
        }

        if (stack.stackSize > 40)
        {
            ret = 5;
        }

        return ret;
    }

    public byte getMiniItemCount(ItemStack stack)
    {
        byte ret = 1;

        if (stack.stackSize > 1)
        {
            ret = 2;
        }

        if (stack.stackSize > 15)
        {
            ret = 3;
        }

        if (stack.stackSize > 31)
        {
            ret = 4;
        }

        return ret;
    }
}

