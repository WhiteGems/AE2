package net.aetherteam.aether.client.renders.dungeon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.entities.dungeon.EntityRewardItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderRewardItem extends Render
{
    private static final ResourceLocation TEXTURE_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private RenderBlocks itemRenderBlocks = new RenderBlocks();
    private Random random = new Random();
    public boolean renderWithColor = true;
    public float zLevel = 0.0F;
    public static boolean renderInFrame = false;

    public RenderRewardItem()
    {
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void doRenderItem(EntityRewardItem rewardItem, double par2, double par4, double par6, float par8, float par9)
    {
        if (rewardItem.getPlayerName().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.username))
        {
            this.random.setSeed(187L);
            ItemStack itemstack = rewardItem.getEntityItem();

            if (itemstack.getItem() != null)
            {
                GL11.glPushMatrix();
                float f2 = this.shouldBob() ? MathHelper.sin(((float)rewardItem.age + par9) / 10.0F + rewardItem.hoverStart) * 0.1F + 0.1F : 0.0F;
                float f3 = (((float)rewardItem.age + par9) / 20.0F + rewardItem.hoverStart) * (180F / (float)Math.PI);
                byte b0 = this.getMiniBlockCount(itemstack);
                GL11.glTranslatef((float)par2, (float)par4 + f2, (float)par6);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                Block block = null;

                if (itemstack.itemID < Block.blocksList.length)
                {
                    block = Block.blocksList[itemstack.itemID];
                }

                if (!ForgeHooksClient.renderEntityItem(rewardItem, itemstack, f2, f3, this.random, this.renderManager.renderEngine, this.renderBlocks))
                {
                    int i;
                    float f4;
                    float f6;
                    float f5;
                    float f8;
                    int icon1;

                    if (itemstack.getItemSpriteNumber() == 0 && block != null && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
                    {
                        GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);

                        if (renderInFrame)
                        {
                            GL11.glScalef(1.25F, 1.25F, 1.25F);
                            GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                        }

                        this.renderManager.renderEngine.func_110577_a(TextureMap.field_110575_b);
                        f8 = 0.25F;
                        icon1 = block.getRenderType();

                        if (icon1 == 1 || icon1 == 19 || icon1 == 12 || icon1 == 2)
                        {
                            f8 = 0.5F;
                        }

                        GL11.glScalef(f8, f8, f8);

                        for (i = 0; i < b0; ++i)
                        {
                            GL11.glPushMatrix();

                            if (i > 0)
                            {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f8;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f8;
                                f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f8;
                                GL11.glTranslatef(f5, f4, f6);
                            }

                            f5 = 1.0F;
                            this.itemRenderBlocks.renderBlockAsItem(block, itemstack.getItemDamage(), f5);
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

                        this.renderManager.renderEngine.func_110577_a(TextureMap.field_110576_c);

                        for (icon1 = 0; icon1 < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++icon1)
                        {
                            this.random.setSeed(187L);
                            Icon l = itemstack.getItem().getIcon(itemstack, icon1);
                            f8 = 1.0F;

                            if (this.renderWithColor)
                            {
                                i = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, icon1);
                                f5 = (float)(i >> 16 & 255) / 255.0F;
                                f4 = (float)(i >> 8 & 255) / 255.0F;
                                f6 = (float)(i & 255) / 255.0F;
                                GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                                this.renderDroppedItem(rewardItem, l, b0, par9, f5 * f8, f4 * f8, f6 * f8);
                            }
                            else
                            {
                                this.renderDroppedItem(rewardItem, l, b0, par9, 1.0F, 1.0F, 1.0F);
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

                        Icon var23 = itemstack.getIconIndex();

                        if (itemstack.getItemSpriteNumber() == 0)
                        {
                            this.renderManager.renderEngine.func_110577_a(TextureMap.field_110575_b);
                        }
                        else
                        {
                            this.renderManager.renderEngine.func_110577_a(TextureMap.field_110576_c);
                        }

                        if (this.renderWithColor)
                        {
                            int var24 = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
                            f8 = (float)(var24 >> 16 & 255) / 255.0F;
                            float f9 = (float)(var24 >> 8 & 255) / 255.0F;
                            f5 = (float)(var24 & 255) / 255.0F;
                            f4 = 1.0F;
                            this.renderDroppedItem(rewardItem, var23, b0, par9, f8 * f4, f9 * f4, f5 * f4);
                        }
                        else
                        {
                            this.renderDroppedItem(rewardItem, var23, b0, par9, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glPopMatrix();
            }
        }
    }

    private void renderDroppedItem(EntityRewardItem par1EntityRewardItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7)
    {
        Tessellator tessellator = Tessellator.instance;
        par2Icon = this.renderBlocks.getIconSafe(par2Icon);
        float f4 = par2Icon.getMinU();
        float f5 = par2Icon.getMaxU();
        float f6 = par2Icon.getMinV();
        float f7 = par2Icon.getMaxV();
        float f8 = 1.0F;
        float f9 = 0.5F;
        float f10 = 0.25F;
        float f11;

        if (this.renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((((float)par1EntityRewardItem.age + par4) / 20.0F + par1EntityRewardItem.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float l = 0.0625F;
            f11 = 0.021875F;
            ItemStack f16 = par1EntityRewardItem.getEntityItem();
            int f17 = f16.stackSize;
            byte b0 = this.getMiniItemCount(f16);
            GL11.glTranslatef(-f9, -f10, -((l + f11) * (float)b0 / 2.0F));

            for (int k = 0; k < b0; ++k)
            {
                float f14;
                float f13;
                float f15;

                if (k > 0 && this.shouldSpreadItems())
                {
                    f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    f15 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(f13, f14, l + f11);
                }
                else
                {
                    GL11.glTranslatef(0.0F, 0.0F, l + f11);
                }

                this.renderManager.renderEngine.func_110577_a(f16.getItemSpriteNumber() == 0 ? TextureMap.field_110575_b : TextureMap.field_110576_c);
                GL11.glColor4f(par5, par6, par7, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f5, f6, f4, f7, par2Icon.getOriginX(), par2Icon.getOriginY(), l);

                if (f16 != null && f16.hasEffect())
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.func_110577_a(TEXTURE_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    f13 = 0.76F;
                    GL11.glColor4f(0.5F * f13, 0.25F * f13, 0.8F * f13, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    f14 = 0.125F;
                    GL11.glScalef(f14, f14, f14);
                    f15 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f15, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, l);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f14, f14, f14);
                    f15 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f15, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, l);
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
            for (int var25 = 0; var25 < par3; ++var25)
            {
                GL11.glPushMatrix();

                if (var25 > 0)
                {
                    f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var27 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f11, var27, var26);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)(0.0F - f9), (double)(0.0F - f10), 0.0D, (double)f4, (double)f7);
                tessellator.addVertexWithUV((double)(f8 - f9), (double)(0.0F - f10), 0.0D, (double)f5, (double)f7);
                tessellator.addVertexWithUV((double)(f8 - f9), (double)(1.0F - f10), 0.0D, (double)f5, (double)f6);
                tessellator.addVertexWithUV((double)(0.0F - f9), (double)(1.0F - f10), 0.0D, (double)f4, (double)f6);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
    }

    public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        int k = par3ItemStack.itemID;
        int l = par3ItemStack.getItemDamage();
        Icon icon = par3ItemStack.getIconIndex();
        Block block = k < Block.blocksList.length ? Block.blocksList[k] : null;
        float f;
        float f1;
        float f2;
        int j1;

        if (par3ItemStack.getItemSpriteNumber() == 0 && block != null && RenderBlocks.renderItemIn3d(Block.blocksList[k].getRenderType()))
        {
            par2RenderEngine.func_110577_a(TextureMap.field_110576_c);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            j1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, 0);
            f2 = (float)(j1 >> 16 & 255) / 255.0F;
            f = (float)(j1 >> 8 & 255) / 255.0F;
            f1 = (float)(j1 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(f2, f, f1, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.itemRenderBlocks.useInventoryTint = this.renderWithColor;
            this.itemRenderBlocks.renderBlockAsItem(block, l, 1.0F);
            this.itemRenderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        }
        else if (Item.itemsList[k].requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            par2RenderEngine.func_110577_a(par3ItemStack.getItemSpriteNumber() == 0 ? TextureMap.field_110575_b : TextureMap.field_110576_c);

            for (j1 = 0; j1 < Item.itemsList[k].getRenderPasses(l); ++j1)
            {
                Icon f4 = Item.itemsList[k].getIcon(par3ItemStack, j1);
                int k1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, j1);
                f = (float)(k1 >> 16 & 255) / 255.0F;
                f1 = (float)(k1 >> 8 & 255) / 255.0F;
                float f3 = (float)(k1 & 255) / 255.0F;

                if (this.renderWithColor)
                {
                    GL11.glColor4f(f, f1, f3, 1.0F);
                }

                this.renderIcon(par4, par5, f4, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            par2RenderEngine.func_110577_a(par3ItemStack.getItemSpriteNumber() == 0 ? TextureMap.field_110575_b : TextureMap.field_110576_c);
            icon = this.renderBlocks.getIconSafe(icon);
            j1 = Item.itemsList[k].getColorFromItemStack(par3ItemStack, 0);
            float var17 = (float)(j1 >> 16 & 255) / 255.0F;
            f2 = (float)(j1 >> 8 & 255) / 255.0F;
            f = (float)(j1 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(var17, f2, f, 1.0F);
            }

            this.renderIcon(par4, par5, icon, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        if (par3ItemStack != null)
        {
            if (!ForgeHooksClient.renderInventoryItem(this.renderBlocks, par2RenderEngine, par3ItemStack, this.renderWithColor, this.zLevel, (float)par4, (float)par5))
            {
                this.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);
            }

            if (par3ItemStack.hasEffect())
            {
                GL11.glDepthFunc(GL11.GL_GREATER);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                this.renderManager.renderEngine.func_110577_a(TEXTURE_GLINT);
                this.zLevel -= 50.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                this.renderGlint(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
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
        for (int j1 = 0; j1 < 2; ++j1)
        {
            if (j1 == 0)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if (j1 == 1)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float f = 0.00390625F;
            float f1 = 0.00390625F;
            float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F;
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4.0F;

            if (j1 == 1)
            {
                f4 = -1.0F;
            }

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
            tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par4 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
            tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + (float)par4) * f), (double)((f3 + 0.0F) * f1));
            tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
            tessellator.draw();
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        this.renderItemStack(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5, (String)null);
    }

    public void renderItemStack(FontRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack par3ItemStack, int par4, int par5, String par6Str)
    {
        if (par3ItemStack != null)
        {
            if (par3ItemStack.stackSize > 1 || par6Str != null)
            {
                String k = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                par1FontRenderer.drawStringWithShadow(k, par4 + 19 - 2 - par1FontRenderer.getStringWidth(k), par5 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (par3ItemStack.isItemDamaged())
            {
                int k1 = (int)Math.round(13.0D - (double)par3ItemStack.getItemDamageForDisplay() * 13.0D / (double)par3ItemStack.getMaxDamage());
                int l = (int)Math.round(255.0D - (double)par3ItemStack.getItemDamageForDisplay() * 255.0D / (double)par3ItemStack.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator tessellator = Tessellator.instance;
                int i1 = 255 - l << 16 | l << 8;
                int j1 = (255 - l) / 4 << 16 | 16128;
                this.renderQuad(tessellator, par4 + 2, par5 + 13, 13, 2, 0);
                this.renderQuad(tessellator, par4 + 2, par5 + 13, 12, 1, j1);
                this.renderQuad(tessellator, par4 + 2, par5 + 13, k1, 1, i1);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6)
    {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + 0), 0.0D);
        par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + par5), 0.0D);
        par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + par5), 0.0D);
        par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + 0), 0.0D);
        par1Tessellator.draw();
    }

    public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
        tessellator.draw();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderItem((EntityRewardItem)par1Entity, par2, par4, par6, par8, par9);
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

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return null;
    }
}
