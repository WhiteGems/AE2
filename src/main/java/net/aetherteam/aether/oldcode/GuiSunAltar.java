package net.aetherteam.aether.oldcode;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiSunAltar extends GuiScreen
{
    private World worldObj;
    private GuiSunAltarSlider slider;

    public GuiSunAltar(World world)
    {
        this.worldObj = world;
    }

    public void initGui()
    {
        super.initGui();
        StringTranslate var1 = StringTranslate.getInstance();
        this.slider = new GuiSunAltarSlider(this.worldObj, 1, this.height / 2 - 75, this.i / 2, var1.translateKey("block.SunAltar.time"), 0.0F);
        this.k.add(this.slider);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawBackgroundLayer(par3, par1, par2);
        super.drawScreen(par1, par2, par3);
    }

    protected void drawBackgroundLayer(float f, int ia, int ib)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.g.renderEngine.b("/aether/gui/sunaltar.png");
        int xSize = 175;
        int ySize = 78;
        int j = (this.height - xSize) / 2;
        int k = (this.i - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        this.m.drawString("Sun Altar", (this.height - this.m.getStringWidth("Sun Altar")) / 2, k + 20, 4210752);
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}

