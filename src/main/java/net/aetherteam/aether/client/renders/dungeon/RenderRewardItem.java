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
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderRewardItem extends Render
{
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

    public void doRenderItem(EntityRewardItem var1, double var2, double var4, double var6, float var8, float var9)
    {
        if (var1.getPlayerName().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.username))
        {
            this.random.setSeed(187L);
            ItemStack var10 = var1.getEntityItem();

            if (var10.getItem() != null)
            {
                GL11.glPushMatrix();
                float var11 = this.shouldBob() ? MathHelper.sin(((float) var1.age + var9) / 10.0F + var1.hoverStart) * 0.1F + 0.1F : 0.0F;
                float var12 = (((float) var1.age + var9) / 20.0F + var1.hoverStart) * (180F / (float) Math.PI);
                byte var13 = this.getMiniBlockCount(var10);
                GL11.glTranslatef((float) var2, (float) var4 + var11, (float) var6);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                Block var18 = null;

                if (var10.itemID < Block.blocksList.length)
                {
                    var18 = Block.blocksList[var10.itemID];
                }

                if (!ForgeHooksClient.renderEntityItem(var1, var10, var11, var12, this.random, this.renderManager.renderEngine, this.renderBlocks))
                {
                    int var14;
                    float var15;
                    int var20;
                    float var19;
                    float var17;
                    float var16;

                    if (var10.getItemSpriteNumber() == 0 && var18 != null && RenderBlocks.renderItemIn3d(Block.blocksList[var10.itemID].getRenderType()))
                    {
                        GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

                        if (renderInFrame)
                        {
                            GL11.glScalef(1.25F, 1.25F, 1.25F);
                            GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                        }

                        this.loadTexture("/terrain.png");
                        var19 = 0.25F;
                        var20 = var18.getRenderType();

                        if (var20 == 1 || var20 == 19 || var20 == 12 || var20 == 2)
                        {
                            var19 = 0.5F;
                        }

                        GL11.glScalef(var19, var19, var19);

                        for (var14 = 0; var14 < var13; ++var14)
                        {
                            GL11.glPushMatrix();

                            if (var14 > 0)
                            {
                                var16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
                                var15 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
                                var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
                                GL11.glTranslatef(var16, var15, var17);
                            }

                            var16 = 1.0F;
                            this.itemRenderBlocks.renderBlockAsItem(var18, var10.getItemDamage(), var16);
                            GL11.glPopMatrix();
                        }
                    } else if (var10.getItem().requiresMultipleRenderPasses())
                    {
                        if (renderInFrame)
                        {
                            GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                            GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                        } else
                        {
                            GL11.glScalef(0.5F, 0.5F, 0.5F);
                        }

                        this.loadTexture("/gui/items.png");

                        for (var20 = 0; var20 < var10.getItem().getRenderPasses(var10.getItemDamage()); ++var20)
                        {
                            this.random.setSeed(187L);
                            Icon var21 = var10.getItem().getIcon(var10, var20);
                            var19 = 1.0F;

                            if (this.renderWithColor)
                            {
                                var14 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var20);
                                var16 = (float) (var14 >> 16 & 255) / 255.0F;
                                var15 = (float) (var14 >> 8 & 255) / 255.0F;
                                var17 = (float) (var14 & 255) / 255.0F;
                                GL11.glColor4f(var16 * var19, var15 * var19, var17 * var19, 1.0F);
                                this.renderDroppedItem(var1, var21, var13, var9, var16 * var19, var15 * var19, var17 * var19);
                            } else
                            {
                                this.renderDroppedItem(var1, var21, var13, var9, 1.0F, 1.0F, 1.0F);
                            }
                        }
                    } else
                    {
                        if (renderInFrame)
                        {
                            GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                            GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                        } else
                        {
                            GL11.glScalef(0.5F, 0.5F, 0.5F);
                        }

                        Icon var23 = var10.getIconIndex();

                        if (var10.getItemSpriteNumber() == 0)
                        {
                            this.loadTexture("/terrain.png");
                        } else
                        {
                            this.loadTexture("/gui/items.png");
                        }

                        if (this.renderWithColor)
                        {
                            int var24 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
                            var19 = (float) (var24 >> 16 & 255) / 255.0F;
                            float var22 = (float) (var24 >> 8 & 255) / 255.0F;
                            var16 = (float) (var24 & 255) / 255.0F;
                            var15 = 1.0F;
                            this.renderDroppedItem(var1, var23, var13, var9, var19 * var15, var22 * var15, var16 * var15);
                        } else
                        {
                            this.renderDroppedItem(var1, var23, var13, var9, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }

                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glPopMatrix();
            }
        }
    }

    private void renderDroppedItem(EntityRewardItem var1, Icon var2, int var3, float var4, float var5, float var6, float var7)
    {
        Tessellator var8 = Tessellator.instance;

        if (var2 == null)
        {
            var2 = this.renderManager.renderEngine.getMissingIcon(var1.getEntityItem().getItemSpriteNumber());
        }

        float var9 = var2.getMinU();
        float var10 = var2.getMaxU();
        float var11 = var2.getMinV();
        float var12 = var2.getMaxV();
        float var13 = 1.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;
        float var16;

        if (this.renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else
            {
                GL11.glRotatef((((float) var1.age + var4) / 20.0F + var1.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float var17 = 0.0625F;
            var16 = 0.021875F;
            ItemStack var18 = var1.getEntityItem();
            int var19 = var18.stackSize;
            byte var20 = this.getMiniItemCount(var18);
            GL11.glTranslatef(-var14, -var15, -((var17 + var16) * (float) var20 / 2.0F));

            for (int var21 = 0; var21 < var20; ++var21)
            {
                float var24;
                float var23;
                float var22;

                if (var21 > 0 && this.shouldSpreadItems())
                {
                    var22 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    var23 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    var24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(var22, var23, var17 + var16);
                } else
                {
                    GL11.glTranslatef(0.0F, 0.0F, var17 + var16);
                }

                if (var18.getItemSpriteNumber() == 0)
                {
                    this.loadTexture("/terrain.png");
                } else
                {
                    this.loadTexture("/gui/items.png");
                }

                GL11.glColor4f(var5, var6, var7, 1.0F);
                ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, var2.getSheetWidth(), var2.getSheetHeight(), var17);

                if (var18 != null && var18.hasEffect())
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.bindTexture("%blur%/misc/glint.png");
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    var22 = 0.76F;
                    GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    var23 = 0.125F;
                    GL11.glScalef(var23, var23, var23);
                    var24 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(var24, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var17);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(var23, var23, var23);
                    var24 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-var24, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var17);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        } else
        {
            for (int var25 = 0; var25 < var3; ++var25)
            {
                GL11.glPushMatrix();

                if (var25 > 0)
                {
                    var16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var27 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var16, var26, var27);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(var5, var6, var7, 1.0F);
                var8.startDrawingQuads();
                var8.setNormal(0.0F, 1.0F, 0.0F);
                var8.addVertexWithUV((double) (0.0F - var14), (double) (0.0F - var15), 0.0D, (double) var9, (double) var12);
                var8.addVertexWithUV((double) (var13 - var14), (double) (0.0F - var15), 0.0D, (double) var10, (double) var12);
                var8.addVertexWithUV((double) (var13 - var14), (double) (1.0F - var15), 0.0D, (double) var10, (double) var11);
                var8.addVertexWithUV((double) (0.0F - var14), (double) (1.0F - var15), 0.0D, (double) var9, (double) var11);
                var8.draw();
                GL11.glPopMatrix();
            }
        }
    }

    public void renderItemIntoGUI(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5)
    {
        int var6 = var3.itemID;
        int var7 = var3.getItemDamage();
        Icon var8 = var3.getIconIndex();
        Block var12 = var6 < Block.blocksList.length ? Block.blocksList[var6] : null;
        float var10;
        float var11;
        float var9;
        int var13;

        if (var3.getItemSpriteNumber() == 0 && var12 != null && RenderBlocks.renderItemIn3d(Block.blocksList[var6].getRenderType()))
        {
            var2.bindTexture("/terrain.png");
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (var4 - 2), (float) (var5 + 3), -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            var13 = Item.itemsList[var6].getColorFromItemStack(var3, 0);
            var11 = (float) (var13 >> 16 & 255) / 255.0F;
            var9 = (float) (var13 >> 8 & 255) / 255.0F;
            var10 = (float) (var13 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(var11, var9, var10, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.itemRenderBlocks.useInventoryTint = this.renderWithColor;
            this.itemRenderBlocks.renderBlockAsItem(var12, var7, 1.0F);
            this.itemRenderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else if (Item.itemsList[var6].requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            var2.bindTexture(var3.getItemSpriteNumber() == 0 ? "/terrain.png" : "/gui/items.png");

            for (var13 = 0; var13 < Item.itemsList[var6].getRenderPasses(var7); ++var13)
            {
                Icon var14 = Item.itemsList[var6].getIcon(var3, var13);
                int var15 = Item.itemsList[var6].getColorFromItemStack(var3, var13);
                var9 = (float) (var15 >> 16 & 255) / 255.0F;
                var10 = (float) (var15 >> 8 & 255) / 255.0F;
                float var16 = (float) (var15 & 255) / 255.0F;

                if (this.renderWithColor)
                {
                    GL11.glColor4f(var9, var10, var16, 1.0F);
                }

                this.renderIcon(var4, var5, var14, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        } else
        {
            GL11.glDisable(GL11.GL_LIGHTING);

            if (var3.getItemSpriteNumber() == 0)
            {
                var2.bindTexture("/terrain.png");
            } else
            {
                var2.bindTexture("/gui/items.png");
            }

            if (var8 == null)
            {
                var8 = var2.getMissingIcon(var3.getItemSpriteNumber());
            }

            var13 = Item.itemsList[var6].getColorFromItemStack(var3, 0);
            float var17 = (float) (var13 >> 16 & 255) / 255.0F;
            var11 = (float) (var13 >> 8 & 255) / 255.0F;
            var9 = (float) (var13 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(var17, var11, var9, 1.0F);
            }

            this.renderIcon(var4, var5, var8, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void renderItemAndEffectIntoGUI(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5)
    {
        if (var3 != null)
        {
            if (!ForgeHooksClient.renderInventoryItem(this.renderBlocks, var2, var3, this.renderWithColor, this.zLevel, (float) var4, (float) var5))
            {
                this.renderItemIntoGUI(var1, var2, var3, var4, var5);
            }

            if (var3.hasEffect())
            {
                GL11.glDepthFunc(GL11.GL_GREATER);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                var2.bindTexture("%blur%/misc/glint.png");
                this.zLevel -= 50.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                this.renderGlint(var4 * 431278612 + var5 * 32178161, var4 - 2, var5 - 2, 20, 20);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                this.zLevel += 50.0F;
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
        }
    }

    private void renderGlint(int var1, int var2, int var3, int var4, int var5)
    {
        for (int var6 = 0; var6 < 2; ++var6)
        {
            if (var6 == 0)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if (var6 == 1)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float var7 = 0.00390625F;
            float var8 = 0.00390625F;
            float var9 = (float) (Minecraft.getSystemTime() % (long) (3000 + var6 * 1873)) / (3000.0F + (float) (var6 * 1873)) * 256.0F;
            float var10 = 0.0F;
            Tessellator var11 = Tessellator.instance;
            float var12 = 4.0F;

            if (var6 == 1)
            {
                var12 = -1.0F;
            }

            var11.startDrawingQuads();
            var11.addVertexWithUV((double) (var2 + 0), (double) (var3 + var5), (double) this.zLevel, (double) ((var9 + (float) var5 * var12) * var7), (double) ((var10 + (float) var5) * var8));
            var11.addVertexWithUV((double) (var2 + var4), (double) (var3 + var5), (double) this.zLevel, (double) ((var9 + (float) var4 + (float) var5 * var12) * var7), (double) ((var10 + (float) var5) * var8));
            var11.addVertexWithUV((double) (var2 + var4), (double) (var3 + 0), (double) this.zLevel, (double) ((var9 + (float) var4) * var7), (double) ((var10 + 0.0F) * var8));
            var11.addVertexWithUV((double) (var2 + 0), (double) (var3 + 0), (double) this.zLevel, (double) ((var9 + 0.0F) * var7), (double) ((var10 + 0.0F) * var8));
            var11.draw();
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5)
    {
        this.renderItemStack(var1, var2, var3, var4, var5, (String) null);
    }

    public void renderItemStack(FontRenderer var1, RenderEngine var2, ItemStack var3, int var4, int var5, String var6)
    {
        if (var3 != null)
        {
            if (var3.stackSize > 1 || var6 != null)
            {
                String var7 = var6 == null ? String.valueOf(var3.stackSize) : var6;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                var1.drawStringWithShadow(var7, var4 + 19 - 2 - var1.getStringWidth(var7), var5 + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (var3.isItemDamaged())
            {
                int var12 = (int) Math.round(13.0D - (double) var3.getItemDamageForDisplay() * 13.0D / (double) var3.getMaxDamage());
                int var8 = (int) Math.round(255.0D - (double) var3.getItemDamageForDisplay() * 255.0D / (double) var3.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator var9 = Tessellator.instance;
                int var10 = 255 - var8 << 16 | var8 << 8;
                int var11 = (255 - var8) / 4 << 16 | 16128;
                this.renderQuad(var9, var4 + 2, var5 + 13, 13, 2, 0);
                this.renderQuad(var9, var4 + 2, var5 + 13, 12, 1, var11);
                this.renderQuad(var9, var4 + 2, var5 + 13, var12, 1, var10);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private void renderQuad(Tessellator var1, int var2, int var3, int var4, int var5, int var6)
    {
        var1.startDrawingQuads();
        var1.setColorOpaque_I(var6);
        var1.addVertex((double) (var2 + 0), (double) (var3 + 0), 0.0D);
        var1.addVertex((double) (var2 + 0), (double) (var3 + var5), 0.0D);
        var1.addVertex((double) (var2 + var4), (double) (var3 + var5), 0.0D);
        var1.addVertex((double) (var2 + var4), (double) (var3 + 0), 0.0D);
        var1.draw();
    }

    public void renderIcon(int var1, int var2, Icon var3, int var4, int var5)
    {
        Tessellator var6 = Tessellator.instance;
        var6.startDrawingQuads();
        var6.addVertexWithUV((double) (var1 + 0), (double) (var2 + var5), (double) this.zLevel, (double) var3.getMinU(), (double) var3.getMaxV());
        var6.addVertexWithUV((double) (var1 + var4), (double) (var2 + var5), (double) this.zLevel, (double) var3.getMaxU(), (double) var3.getMaxV());
        var6.addVertexWithUV((double) (var1 + var4), (double) (var2 + 0), (double) this.zLevel, (double) var3.getMaxU(), (double) var3.getMinV());
        var6.addVertexWithUV((double) (var1 + 0), (double) (var2 + 0), (double) this.zLevel, (double) var3.getMinU(), (double) var3.getMinV());
        var6.draw();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderItem((EntityRewardItem) var1, var2, var4, var6, var8, var9);
    }

    public boolean shouldSpreadItems()
    {
        return true;
    }

    public boolean shouldBob()
    {
        return true;
    }

    public byte getMiniBlockCount(ItemStack var1)
    {
        byte var2 = 1;

        if (var1.stackSize > 1)
        {
            var2 = 2;
        }

        if (var1.stackSize > 5)
        {
            var2 = 3;
        }

        if (var1.stackSize > 20)
        {
            var2 = 4;
        }

        if (var1.stackSize > 40)
        {
            var2 = 5;
        }

        return var2;
    }

    public byte getMiniItemCount(ItemStack var1)
    {
        byte var2 = 1;

        if (var1.stackSize > 1)
        {
            var2 = 2;
        }

        if (var1.stackSize > 15)
        {
            var2 = 3;
        }

        if (var1.stackSize > 31)
        {
            var2 = 4;
        }

        return var2;
    }
}
