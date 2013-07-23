package net.aetherteam.aether.oldcode;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiSunAltar extends GuiScreen
{
    private World worldObj;
    private GuiSunAltarSlider slider;

    public GuiSunAltar(World var1)
    {
        this.worldObj = var1;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        StringTranslate var1 = StringTranslate.getInstance();
        this.slider = new GuiSunAltarSlider(this.worldObj, 1, this.width / 2 - 75, this.height / 2, var1.translateKey("block.SunAltar.time"), 0.0F);
        this.buttonList.add(this.slider);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        this.drawBackgroundLayer(var3, var1, var2);
        super.drawScreen(var1, var2, var3);
    }

    protected void drawBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/aether/gui/sunaltar.png");
        short var4 = 175;
        byte var5 = 78;
        int var6 = (this.width - var4) / 2;
        int var7 = (this.height - var5) / 2;
        this.drawTexturedModalRect(var6, var7, 0, 0, var4, var5);
        this.fontRenderer.drawString("太阳祭坛", (this.width - this.fontRenderer.getStringWidth("太阳祭坛")) / 2, var7 + 20, 4210752);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
