package net.aetherteam.aether.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherLore;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.containers.ContainerLore;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiLore extends InventoryEffectRenderer
{
    private static final ResourceLocation TEXTURE_LORE = new ResourceLocation("aether", "textures/gui/lore.png");
    public static ArrayList<AetherLore> lores = new ArrayList();
    private int type;

    public GuiLore(InventoryPlayer inventoryplayer, EntityPlayer player, int i)
    {
        super(new ContainerLore(inventoryplayer, true, player));
        this.xSize = 256;
        this.ySize = 195;
        this.type = i;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0)
        {
            if (Aether.proxy.getClientPlayer().capabilities.isCreativeMode)
            {
                this.mc.displayGuiScreen(new GuiContainerCreative(Aether.proxy.getClientPlayer()));
            }
            else
            {
                this.mc.displayGuiScreen(new GuiInventory(Aether.proxy.getClientPlayer()));
            }
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Book Of Lore:", 37, 18, 4210752);

        switch (this.type)
        {
            case 0:
                this.fontRenderer.drawString("Add Object", 37, 38, 4210752);
                break;

            case 1:
                this.fontRenderer.drawString("The Nether", 37, 38, 4210752);
                break;

            case 2:
                this.fontRenderer.drawString("The Aether", 37, 38, 4210752);
        }

        this.fontRenderer.drawString("Item : ", 46, 72, 4210752);
        ItemStack item = ((ContainerLore)this.inventorySlots).loreSlot.getStackInSlot(0);

        if (item != null)
        {
            Iterator i$ = lores.iterator();

            while (i$.hasNext())
            {
                AetherLore lore = (AetherLore)i$.next();

                if (lore.equals(item) && lore.type == this.type)
                {
                    this.fontRenderer.drawString(lore.name, 134, 14, 4210752);
                    this.fontRenderer.drawString(lore.line1, 134, 28, 4210752);
                    this.fontRenderer.drawString(lore.line2, 134, 38, 4210752);
                    this.fontRenderer.drawString(lore.line3, 134, 48, 4210752);
                    this.fontRenderer.drawString(lore.line4, 134, 58, 4210752);
                    this.fontRenderer.drawString(lore.line5, 134, 68, 4210752);
                    this.fontRenderer.drawString(lore.line6, 134, 78, 4210752);

                    if (this.mc.theWorld.provider.terrainType.getWorldTypeID() == 2)
                    {
                        ;
                    }

                    break;
                }
            }
        }

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.guiLeft - 20, this.guiTop, 20, 20, I18n.func_135053_a("X")));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_LORE);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
    }

    static
    {
        lores.add(new AetherLore(Block.stone, "Stone", "Found everywhere.", "Makes steps", "", "", "", "", 0));
        lores.add(new AetherLore(Block.grass, "Grass", "Found in light.", "Spreads to dirt.", "Flowers and trees", "will grow on it.", "Click with a hoe", "to make farmland", 0));
        lores.add(new AetherLore(Block.dirt, "Dirt", "Found everywhere.", "Grass, trees and", "flowers will grow", "on it.", "Click with a hoe", "to make farmland", 0));
        lores.add(new AetherLore(Block.cobblestone, "Cobblestone", "Found when mining", "stone and when", "water meets lava.", "Makes stone tools,", "cobble steps and", "furnaces", 0));
        lores.add(new AetherLore(Block.planks, "Wooden Planks", "Crafted from wood.", "Useful building", "material.", "Makes sticks, tools,", "boats, doors, chests", "and crafting tables", 0));
        lores.add(new AetherLore(Block.sapling, "Sapling", "Dropped by leaves.", "Grows a tree.", "", "", "", "", 0));
        lores.add(new AetherLore(Block.bedrock, "Bedrock / Adminium", "Not obtainable...", "The hardest known", "material; completely", "indestructible.", "", "", 0));
        lores.add(new AetherLore(Block.sand, "Sand", "Found by water.", "Falls when ", "unsupported.", "Smeltable into glass", "", "", 0));
        lores.add(new AetherLore(Block.gravel, "Gravel", "Found in rock.", "Falls when ", "unsupported.", "Chance to drop flint", "", "", 0));
        lores.add(new AetherLore(Block.oreGold, "Gold Ore", "Found in rock.", "Smeltable into", "gold ingots.", "Medium rarity", "", "", 0));
        lores.add(new AetherLore(Block.oreIron, "Iron Ore", "Found in rock.", "Smeltable into", "iron ingots.", "Common", "", "", 0));
        lores.add(new AetherLore(Block.wood, "Wood", "Found in trees.", "Craftable into", "planks.", "", "", "", 0));
        lores.add(new AetherLore(Block.sponge, "Sponge", "Not obtainable...", "Has no purpose", "", "", "", "", 0));
        lores.add(new AetherLore(Block.glass, "Glass", "Smelted from sand.", "Decorative block", "", "", "", "", 0));
        lores.add(new AetherLore(Block.blockLapis, "Lapis Lazuli", "Made from 9 lapis", "lazuli.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(Block.dispenser, "Dispenser", "Ejects items when ", "powered. Also", "shoots arrows", "", "", "", 0));
        lores.add(new AetherLore(Block.sandStone, "Sandstone", "Made from 4 sand.", "Also found below", "sand naturally.", "Decorative block", "", "", 0));
        lores.add(new AetherLore(Block.music, "Note Block", "Plays a note when", "powered. The", "block underneath", "defines the sound.", "Right click to", "change the note", 0));
        lores.add(new AetherLore(Block.railPowered, "Powered Rail", "Quickens minecarts", "when powered.", "Brakes minecarts", "when unpowered", "", "", 0));
        lores.add(new AetherLore(Block.railDetector, "Detector Rail", "Gives out power", "when a minecart is", "on it.", "", "", "", 0));
        lores.add(new AetherLore(Block.web, "Web", "Slows down", "anything that", "enters it", "", "", "", 0));
        lores.add(new AetherLore(Block.cloth, "Wool", "Found on sheep.", "Can be dyed.", "Decorative Block", "", "", "", 0));
        lores.add(new AetherLore(Block.plantYellow, "Dandelion", "Found on grass.", "Can make", "dandelion yellow", "dye", "", "", 0));
        lores.add(new AetherLore(Block.plantRed, "Rose", "Found on grass.", "Can make", "rose red dye", "", "", "", 0));
        lores.add(new AetherLore(Block.mushroomBrown, "Mushroom", "Found on grass", "and in caves.", "Used to make", "mushroom soup", "", "", 0));
        lores.add(new AetherLore(Block.mushroomRed, "Mushroom", "Found on grass", "and in caves.", "Used to make", "mushroom soup", "", "", 0));
        lores.add(new AetherLore(Block.blockGold, "Gold", "Crafted from 9", "gold ingots.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(Block.blockIron, "Iron", "Crafted from 9", "iron ingots.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(Block.stoneSingleSlab, "Half-Step", "Used for making", "stairs and such.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(Block.brick, "Bricks", "Decorative block", "", "", "", "", "", 0));
        lores.add(new AetherLore(Block.tnt, "TNT", "Made from sand", "and gunpowder.", "Will detonate when", "hit or powered.", "Handle with care", "", 0));
        lores.add(new AetherLore(Block.bookShelf, "Bookshelf", "A pleasant array", "of books.", "Decorative Block", "", "", "", 0));
        lores.add(new AetherLore(Block.cobblestoneMossy, "Mossy Cobblestone", "Found in dungeons.", "Deorative Block", "", "", "", "", 0));
        lores.add(new AetherLore(Block.obsidian, "Obsidian", "Formed from water", "and lava.", "Very hard block", "that is useful in", "making fortifications", "and nether portals", 0));
        lores.add(new AetherLore(Block.torchWood, "Torch", "Made from coal and", "sticks.", "Most common light", "source used for", "mining and lighting", "homes.", 0));
        lores.add(new AetherLore(Block.stairsWoodOak, "Wooden Stairs", "Made from wood.", "Useful for making", "staircases, but ", "more compact than", "half steps.", "", 0));
        lores.add(new AetherLore(Block.chest, "Chest", "Made from planks.", "Holds 27 slots.", "Can be joined to", "another chest to", "make a double", "chest.", 0));
        lores.add(new AetherLore(Block.blockDiamond, "Diamond", "Crafted from 9", "diamonds.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(Block.workbench, "Workbench", "Used to create all", "complex items.", "", "", "", "", 0));
        lores.add(new AetherLore(Block.furnaceIdle, "Furnace", "Takes coal, wood", "or lava as fuel and", "smelts sand, cobble", "iron, gold, clay", "and lots more", "", 0));
        lores.add(new AetherLore(Block.ladder, "Ladder", "Used to climb", "vertically", "upwards or", "downwards", "", "", 0));
        lores.add(new AetherLore(Block.rail, "Rail", "Allows minecarts", "to be placed and", "to move.", "You will need a lot", "of rails to make", "a minecart track", 0));
        lores.add(new AetherLore(Block.stairsCobblestone, "Cobblestone Stairs", "Made from cobble.", "Useful for making", "staircases, but ", "more compact than", "half steps.", "", 0));
        lores.add(new AetherLore(Block.lever, "Lever", "Gives a redstone", "current when on.", "Used as an input", "device for", "redstone circuits", "", 0));
        lores.add(new AetherLore(Block.pressurePlateStone, "Pressure Plate", "Gives a redstone", "current when", "stepped on by", "a living thing.", "", "", 0));
        lores.add(new AetherLore(Block.pressurePlatePlanks, "Pressure Plate", "Gives a redstone", "current when", "anything is on it", "", "", "", 0));
        lores.add(new AetherLore(Block.torchRedstoneActive, "Redstone Torch", "Gives a redstone", "current when", "unpowered, but", "does not when", "powered (NOT gate)", "", 0));
        lores.add(new AetherLore(Block.stoneButton, "Button", "Gives a redstone", "pulse for about", "a second when", "pushed", "", "", 0));
        lores.add(new AetherLore(Block.blockSnow, "Snow", "Melts when near", "light.", "Could be used as", "camouflage in a", "snowy biome", "", 0));
        lores.add(new AetherLore(Block.cactus, "Cactus", "Found in deserts.", "Hurts living things", "that touch it.", "Can be used as", "defenses and can", "be farmed", 0));
        lores.add(new AetherLore(Block.blockClay, "Clay", "Found in sand.", "Can be split into", "clay lumps and then", "smelted to make", "bricks", "", 0));
        lores.add(new AetherLore(Block.jukebox, "Jukebox", "Plays records", "found in dungeons.", "", "", "", "", 0));
        lores.add(new AetherLore(Block.fence, "Fence", "Stops anything", "from jumping over.", "Also used for poles.", "Start from the top", "and work down", "", 0));
        lores.add(new AetherLore(Block.pumpkin, "Pumpkin", "Found in small", "patches.", "Can be made into", "Jack-o-Lanterns", "", "", 0));
        lores.add(new AetherLore(Block.pumpkinLantern, "Jack-o-Lantern", "Made from pumpkins", "and torches.", "Useful lightsource", "and rather scary", "", "", 0));
        lores.add(new AetherLore(Block.trapdoor, "Trapdoor", "Opens upwards to", "allow access to", "space below", "", "", "", 0));
        lores.add(new AetherLore(Block.pistonBase, "Piston", "Extends when", "powered. Useful", "for traps, doors", "and machines.", "", "", 0));
        lores.add(new AetherLore(Block.pistonStickyBase, "Sticky Piston", "Extends when", "powered and pulls", "blocks when", "retracted. Useful", "in making doors", "and hidden blocks", 0));
        lores.add(new AetherLore(Block.leaves, "Leaves", "Grows on trees.", "Obtainable by", "using shears.", "Decorative block", "", "", 0));
        lores.add(new AetherLore(Item.shovelIron, "Iron Shovel", "Digs grass, dirt,", "sand and gravel.", "Normal Speed", "Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.pickaxeIron, "Iron Pickaxe", "Digs stone, cobble,", "and other rocks.", "Normal Speed", "Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.axeIron, "Iron Axe", "Chops wood and ", "planks.", "Normal Speed", "Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.swordIron, "Iron Sword", "For attacking", "mobs and animals.", "Normal Damage", "Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.hoeIron, "Iron Hoe", "Turns grass or", "dirt into farmland.", "Normal Speed", "Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.shovelWood, "Wooden Shovel", "Digs grass, dirt,", "sand and gravel.", "Very Slow Speed", "Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.pickaxeWood, "Wooden Pickaxe", "Digs stone, cobble,", "and other rocks.", "Very Slow Speed", "Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.axeWood, "Wooden Axe", "Chops wood and ", "planks.", "Very Slow Speed", "Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.swordWood, "Wooden Sword", "For attacking", "mobs and animals.", "Very Low Damage", "Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.hoeWood, "Wooden Hoe", "Turns grass or", "dirt into farmland.", "Very Slow Speed", "Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.shovelStone, "Stone Shovel", "Digs grass, dirt,", "sand and gravel.", "Slow Speed", "Average Uses", "", "", 0));
        lores.add(new AetherLore(Item.pickaxeStone, "Stone Pickaxe", "Digs stone, cobble,", "and other rocks.", "Slow Speed", "Average Uses", "", "", 0));
        lores.add(new AetherLore(Item.axeStone, "Stone Axe", "Chops wood and ", "planks.", "Slow Speed", "Average Uses", "", "", 0));
        lores.add(new AetherLore(Item.swordStone, "Stone Sword", "For attacking", "mobs and animals.", "Low Damage", "Average Uses", "", "", 0));
        lores.add(new AetherLore(Item.hoeStone, "Stone Hoe", "Turns grass or", "dirt into farmland.", "Slow Speed", "Average Uses", "", "", 0));
        lores.add(new AetherLore(Item.shovelGold, "Gold Shovel", "Digs grass, dirt,", "sand and gravel.", "Very Fast Speed", "Very Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.pickaxeGold, "Gold Pickaxe", "Digs stone, cobble,", "and other rocks.", "Very Fast Speed", "Very Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.axeGold, "Gold Axe", "Chops wood and ", "planks.", "Very Fast Speed", "Very Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.swordGold, "Gold Sword", "For attacking", "mobs and animals.", "Very High Damage", "Very Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.hoeGold, "Gold Hoe", "Turns grass or", "dirt into farmland.", "Very Fast Speed", "Very Few Uses", "", "", 0));
        lores.add(new AetherLore(Item.shovelDiamond, "Diamond Shovel", "Digs grass, dirt,", "sand and gravel.", "Fast Speed", "Very Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.pickaxeDiamond, "Diamond Pickaxe", "Digs stone, cobble,", "and other rocks.", "Fast Speed", "Very Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.axeDiamond, "Diamond Axe", "Chops wood and ", "planks.", "Fast Speed", "Very Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.swordDiamond, "Diamond Sword", "For attacking", "mobs and animals.", "High Damage", "Very Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.hoeDiamond, "Diamond Hoe", "Turns grass or", "dirt into farmland.", "Fast Speed", "Very Many Uses", "", "", 0));
        lores.add(new AetherLore(Item.flintAndSteel, "Steel and Flint", "Makes fires", "and activates", "Nether portals.", "", "", "", 0));
        lores.add(new AetherLore(Item.appleRed, "Red Apple", "Heals the user", "a small amount.", "Found in dungeons", "and from Notch", "", "", 0));
        lores.add(new AetherLore(Item.bow, "Bow", "Shoots arrows", "and can be used", "to make dispensers.", "Skeletons use", "bows too", "", 0));
        lores.add(new AetherLore(Item.arrow, "Arrow", "Ammo for the bow.", "Made from feathers,", "sticks and flints", "", "", "", 0));
        lores.add(new AetherLore(Item.coal, "Coal", "Found in rock.", "Very common.", "Makes torches", "and is used as", "fuel in the furnace", "", 0));
        lores.add(new AetherLore(Item.diamond, "Diamond", "Found deep under-", "ground. Very rare.", "Used for diamond", "tools, armour and", "jukeboxes", "", 0));
        lores.add(new AetherLore(Item.ingotIron, "Iron Ingot", "Refined Iron.", "Makes iron tools,", "armour, rails,", "minecarts, doors,", "buckets and", "compasses", 0));
        lores.add(new AetherLore(Item.ingotGold, "Gold Ingot", "Refined Gold.", "Makes gold tools,", "armour and ", "watches", "", "", 0));
        lores.add(new AetherLore(Item.stick, "Stick", "Made from 2 planks.", "Very important", "crafting material", "", "", "", 0));
        lores.add(new AetherLore(Item.bowlEmpty, "Bowl", "For holding soup", "", "", "", "", "", 0));
        lores.add(new AetherLore(Item.bowlSoup, "Mushroom Soup", "Tasty soup that", "heals a few hearts", "", "", "", "", 0));
        lores.add(new AetherLore(Item.silk, "String", "Dropped by", "spiders.", "Used for bows and", "fishing rods", "", "", 0));
        lores.add(new AetherLore(Item.feather, "Feather", "Dropped by", "chickens", "and zombies.", "Makes arrows", "", "", 0));
        lores.add(new AetherLore(Item.gunpowder, "Gunpowder", "Dropped by ghasts", "and creepers.", "Makes TNT", "", "", "", 0));
        lores.add(new AetherLore(Item.seeds, "Seeds", "Gained by cutting", "wild grass.", "Placeable in", "farmland to grow", "crops", "", 0));
        lores.add(new AetherLore(Item.wheat, "Wheat", "Produced when", "harvesting crops.", "Used to make bread", "and cake", "", "", 0));
        lores.add(new AetherLore(Item.bread, "Bread", "Delicious,", "nutritious", "bread.", "Heals a few hearts", "", "", 0));
        lores.add(new AetherLore(Item.helmetLeather, "Leather Helmet", "Wear it on your", "head.", "Awful Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.plateLeather, "Leather Chestplate", "Wear it on your", "chest.", "Awful Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.legsLeather, "Leather Greaves", "Wear it on your", "legs.", "Awful Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.bootsLeather, "Leather Boots", "Wear it on your", "feet.", "Awful Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.helmetGold, "Gold Helmet", "Wear it on your", "head.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.plateGold, "Gold Chestplate", "Wear it on your", "chest.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.legsGold, "Gold Greaves", "Wear it on your", "legs.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.bootsGold, "Gold Boots", "Wear it on your", "feet.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.helmetChain, "Chain Helmet", "Wear it on your", "head.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.plateChain, "Chain Chestplate", "Wear it on your", "chest.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.legsChain, "Chain Greaves", "Wear it on your", "legs.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.bootsChain, "Chain Boots", "Wear it on your", "feet.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.helmetIron, "Iron Helmet", "Wear it on your", "head.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.plateIron, "Iron Chestplate", "Wear it on your", "chest.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.legsIron, "Iron Greaves", "Wear it on your", "legs.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.bootsIron, "Iron Boots", "Wear it on your", "feet.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.helmetDiamond, "Diamond Helmet", "Wear it on your", "head.", "Great Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.plateDiamond, "Diamond Chestplate", "Wear it on your", "chest.", "Great Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.legsDiamond, "Diamond Greaves", "Wear it on your", "legs.", "Great Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.bootsDiamond, "Diamond Boots", "Wear it on your", "feet.", "Great Protection", "", "", "", 0));
        lores.add(new AetherLore(Item.flint, "Flint", "Found in gravel.", "Used in arrows", "and firelighters", "", "", "", 0));
        lores.add(new AetherLore(Item.porkRaw, "Raw Pork", "Dropped by pigs.", "Can be cooked", "or eaten raw", "", "", "", 0));
        lores.add(new AetherLore(Item.porkCooked, "Cooked Pork", "Dropped by pig", "zombies. Also", "obtainable from", "cooking pork.", "Heals a few hearts", "", 0));
        lores.add(new AetherLore(Item.painting, "Painting", "Made from sticks", "and cloth.", "Puts a random", "painting where", "you click", "", 0));
        lores.add(new AetherLore(Item.appleGold, "Golden Apple", "A ridiculously", "expensive apple", "which, despite being", "coated in gold,", "heals all hearts", "", 0));
        lores.add(new AetherLore(Item.sign, "Sign", "Made from sticks", "and planks.", "Can be placed on", "walls or the floor", "with your message", "", 0));
        lores.add(new AetherLore(Item.doorWood, "Wooden Door", "Made from planks.", "Allows you to shut", "out the creepers", "before they boom", "in your house", "", 0));
        lores.add(new AetherLore(Item.bucketEmpty, "Bucket", "Made from iron.", "Can pick up water", "and lava.", "If used on a cow,", "milk may be", "obtained", 0));
        lores.add(new AetherLore(Item.bucketWater, "Water Bucket", "Can be used to", "place a water", "source", "", "", "", 0));
        lores.add(new AetherLore(Item.bucketLava, "Lava Bucket", "Can be used to", "place a lava", "source", "", "", "", 0));
        lores.add(new AetherLore(Item.minecartEmpty, "Minecart", "Can be ridden in,", "but make sure the", "animals can\'t get", "to your cart", "", "", 0));
        lores.add(new AetherLore(Item.saddle, "Saddle", "Found in dungeons.", "Can be used to", "saddle a pig", "", "", "", 0));
        lores.add(new AetherLore(Item.doorIron, "Iron Door", "Made from iron.", "Behaves like a door", "but can only be", "opened by redstone", "", "", 0));
        lores.add(new AetherLore(Item.redstone, "Redstone", "Used to carry", "redstone currents", "in redstone circuits", "", "", "", 0));
        lores.add(new AetherLore(Item.snowball, "Snowball", "Found by digging", "snow with a spade.", "Can be thrown", "", "", "", 0));
        lores.add(new AetherLore(Item.boat, "Boat", "Can be ridden in", "to cross lakes", "and oceans", "", "", "", 0));
        lores.add(new AetherLore(Item.leather, "Leather", "Dropped by cows.", "Used in making", "leather armour", "", "", "", 0));
        lores.add(new AetherLore(Item.bucketMilk, "Milk Bucket", "Gained by using", "a bucket on a cow.", "Used in making cake.", "Heals a few hearts", "", "", 0));
        lores.add(new AetherLore(Item.brick, "Brick", "Smelted from clay.", "Used to make brick", "blocks", "", "", "", 0));
        lores.add(new AetherLore(Item.clay, "Clay", "Found in clay", "blocks.", "Can be smelted", "into bricks", "", "", 0));
        lores.add(new AetherLore(Item.reed, "Sugarcanes", "Found on dirt or", "grass by water.", "Makes paper for", "books and sugar ", "for cakes.", "Can be farmed", 0));
        lores.add(new AetherLore(Item.paper, "Paper", "Made from", "sugarcane.", "Used in books and", "maps", "", "", 0));
        lores.add(new AetherLore(Item.book, "Book", "Made from paper.", "Used to make", "bookcases", "", "", "", 0));
        lores.add(new AetherLore(Item.slimeBall, "Slime Ball", "Dropped by slimes.", "Used to make", "sticky pistons", "", "", "", 0));
        lores.add(new AetherLore(Item.minecartCrate, "Storage Minecart", "A minecart that", "carries a chest", "", "", "", "", 0));
        lores.add(new AetherLore(Item.minecartPowered, "Powered Minecart", "A minecart that", "pushes other carts", "when given coal", "", "", "", 0));
        lores.add(new AetherLore(Item.egg, "Egg", "Laid by chickens.", "Throw it to hatch", "a new chicken.", "Also used in", "making cake.", "", 0));
        lores.add(new AetherLore(Item.compass, "Compass", "Made from iron", "and redstone.", "Points to your", "spawnpoint", "", "", 0));
        lores.add(new AetherLore(Item.fishingRod, "Fishing Rod", "Made from sticks", "and string.", "Can be used for", "fishing or ", "pulling mobs around", "", 0));
        lores.add(new AetherLore(Item.pocketSundial, "Watch", "Made from gold", "and redstone.", "Tells the time", "", "", "", 0));
        lores.add(new AetherLore(Item.glowstone, "Lightstone Dust", "Dropped by", "lightstone.", "Can be crafted", "into lightstone", "", "", 0));
        lores.add(new AetherLore(Item.fishRaw, "Raw Fish", "Gained by fishing.", "Can be cooked or", "eaten raw", "", "", "", 0));
        lores.add(new AetherLore(Item.fishCooked, "Cooked Fish", "Gained by cooking", "raw fish.", "Heals a few hearts", "", "", "", 0));
        lores.add(new AetherLore(Item.dyePowder, "Dye", "Obtained from many", "places.", "Dyes can be mixed,", "added to wool", "and used on sheep", "", 0));
        lores.add(new AetherLore(Item.bone, "Bone", "Dropped by", "skeletons.", "Used to make", "bonemeal and to", "tame wolves", "", 0));
        lores.add(new AetherLore(Item.sugar, "Sugar", "Made from", "sugarcane.", "Used to make cake", "", "", "", 0));
        lores.add(new AetherLore(Item.cake, "Cake", "The cake is a lie.", "", "", "", "", "", 0));
        lores.add(new AetherLore(Item.bed, "Bed", "Made from planks", "and wool.", "Allows you to", "sleep until", "morning and set", "your spawnpoint", 0));
        lores.add(new AetherLore(Item.redstoneRepeater, "Repeater", "Made from stone", "and redstone.", "Repeats a signal", "with a delay, set", "by the toggle", "", 0));
        lores.add(new AetherLore(Item.cookie, "Cookie", "Made from cocoa", "beans and wheat.", "Heals a few hearts", "", "", "", 0));
        lores.add(new AetherLore(Item.map, "Map", "Made from paper", "and a compass.", "Makes a map of the", "area you are in", "", "", 0));
        lores.add(new AetherLore(Item.record13, "13", "Found in dungeons.", "Playable in jukebox.", "A rather odd tune", "", "", "", 0));
        lores.add(new AetherLore(Item.recordCat, "Cat", "Found in dungeons.", "Playable in jukebox.", "A very jolly tune", "", "", "", 0));
        lores.add(new AetherLore(Item.shears, "Shears", "Made from iron.", "Used to shear", "sheep and to get", "leaf blocks from", "trees.", "", 0));
        lores.add(new AetherLore(Block.netherrack, "Netherrack", "Main nether", " material.", "Burns forever", "", "", "", 0));
        lores.add(new AetherLore(Block.slowSand, "Slow Sand", "Found in patches", "Slows anything on it", "", "", "", "", 0));
        lores.add(new AetherLore(Block.glowStone, "Glowstone", "Found on the roof", "of the Nether.", "Drops 4 Glowstone", "dust.", "Used in Aether ", "portals", 0));
        lores.add(new AetherLore(Item.glowstone, "Glowstone Dust", "Obtained when mining", "a block of Glowstone.", "", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.IronRing, "Iron Ring", "Made from iron.", "Wear it in your", "ring slot.", "Purely decorative", "item", "", 0));
        lores.add(new AetherLore(AetherItems.GoldenRing, "Gold Ring", "Made from gold.", "Wear it in your", "ring slot.", "Purely decorative", "item", "", 0));
        lores.add(new AetherLore(AetherItems.IronPendant, "Iron Pendant", "Made from iron.", "Wear it in your", "pendant slot.", "Purely decorative", "item", "", 0));
        lores.add(new AetherLore(AetherItems.GoldenPendant, "Gold Pendant", "Made from gold.", "Wear it in your", "pendant slot.", "Purely decorative", "item", "", 0));
        lores.add(new AetherLore(AetherItems.LeatherGloves, "Leather Glove", "Wear them on your", "hands.", "Awful Protection", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.IronGloves, "Iron Glove", "Wear them on your", "hands.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.GoldenGloves, "Gold Glove", "Wear them on your", "hands.", "Bad Protection", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.DiamondGloves, "Diamond Glove", "Wear them on your", "hands.", "Great Protection", "", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.AetherDirt, "Aether Dirt", "A paler dirt.", "Aether grass", "and skyroot trees", "will grow on it", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.AetherGrass, 1, 0), "Aether Grass", "A paler grass.", "Skyroot trees will", "grow on it.", "Allows Aether mobs", "to spawn", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.AetherGrass, 1, 1), "Enchanted Grass", "This grass will", "increase the amount", "of drops from Berry", "Bushes which grow", "on it.", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.Holystone, 1, 0), "Holystone", "Main material of", "the Aether.", "Makes holystone", "tools and", "enchanters", "", 0));
        lores.add(new AetherLore(AetherBlocks.SkyrootPlank, "Skyroot Plank", "Made from skyroot.", "Used to make", "skyroot sticks and", "tools", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.GreenSkyrootSapling, "Skyroot Sapling", "Dropped by ", "skyroot leaves.", "Plants a skyroot", "tree", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.GoldenOakSapling, "Golden Oak Sapling", "Dropped by golden", "oak leaves.", "Plants a golden", "oak", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Quicksoil, "Quicksoil", "Found at the edge", "of islands.", "Speeds up anything", "on it.", "Use it with blue", "clouds for epicness", 0));
        lores.add(new AetherLore(AetherBlocks.AetherLog, "Skyroot Wood", "Wood from skyroot", "trees.", "Makes skyroot", "planks", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Icestone, "Icestone", "Found in Holystone.", "Freezes water", "around it on", "placement", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.GravititeOre, "Gravitite Ore", "Found under big", "islands.", "Floats upwards,", "and can be made", "into tools.", "Can be enchanted", 0));
        lores.add(new AetherLore(AetherBlocks.EnchantedGravitite, "Enchanted Gravitite", "Floats upwards", "when powered by", "redstone", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.Holystone, 1, 2), "Mossy Holystone", "Found in dungeons.", "Decorative block", "", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.Aercloud, 1, 1), "Blue Aercloud", "Found in clouds.", "When landed on,", "it will bounce you", "sky-high", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.Aercloud, 1, 0), "Cold Aercloud", "Found in clouds.", "Stops fall damage", "when landed on", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.Aercloud, 1, 2), "Gold Aercloud", "Found in clouds.", "Stops fall damage", "when landed on.", "Quite rare", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.AmbrosiumTorch, "Ambrosium Torch", "Made from skyroot", "sticks and", "ambrosium.", "Can be placed in", "the Aether", "", 0));
        lores.add(new AetherLore(AetherBlocks.DungeonStone, "Dungeon Stone", "Found in dungeons.", "Decorative block", "", "", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.LightDungeonStone, "Lit Dungeon Stone", "Found in dungeons.", "Emits a faint light.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Pillar, "Pillar", "Found in silver", "dungeons.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Altar, "Altar", "Made from Zanite", "and Holystone.", "Enchants items", "and repairs tools", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Incubator, "Incubator", "Made from skyroot", "planks and", "Holystone.", "Incubates Moa", "eggs until they ", "hatch", 0));
        lores.add(new AetherLore(AetherBlocks.ZaniteBlock, "Zanite Block", "Crafted with four", "Zanite Gemstones.", "Decorative block", "", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.PurpleFlower, "Purple Flower", "Common plant in", "the Aether.", "Can be crafted", "into dye.", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.WhiteFlower, "White Flower", "Common plant in", "the Aether.", "Can be crafted", "into dye.", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.Freezer, "Freezer", "Allows you to", "freeze certain items.", "Uses Icestone as", "a fuel source.", "", "", 0));
        lores.add(new AetherLore(AetherBlocks.QuicksoilGlass, "Quicksoil Glass", "Gained by enchanting", "Quicksoil blocks.", "Translucent, gives", "off small amount", "of light.", "", 0));
        lores.add(new AetherLore(AetherBlocks.HolystoneBrick, "Holystone Bricks", "Two can be crafted", "with four blocks of", "Holystone. Is purely", "decorative.", "Great for castles.", "", 0));
        lores.add(new AetherLore(AetherBlocks.HellfireWall, "Hellfire Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.AngelicWall, "Angelic Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.CarvedWall, "Carved Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.SkyrootFence, "Skyroot Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.IcestoneWall, "Icestone Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.MossyHolystoneWall, "Holystone Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.HolystoneWall, "Holystone Fence", "Six can be crafted", "with six blocks of", "Hellfire Stone. Is", "purely decorative.", "Great for containing", "animals.", 0));
        lores.add(new AetherLore(AetherBlocks.HellfireStairs, "Hellfire Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.AngelicStairs, "Angelic Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.CarvedStairs, "Carved Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.SkyrootStairs, "Skyroot Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.IcestoneStairs, "Icestone Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.MossyHolystoneStairs, "Holystone Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.HolystoneStairs, "Holystone Stairs", "Used to walk up", "elevated surfaces.", "Greatly functional", "and decorative", "blocks.", "", 0));
        lores.add(new AetherLore(AetherBlocks.BerryBushStem, "Berry Bush Stem", "Left over when", "harvesting a Berry", "Bush. These stems", "can regrow into", "full bushes again.", "", 0));
        lores.add(new AetherLore(AetherBlocks.BerryBush, "Berry Bush", "These bushes are", "found in clusters", "among the floating", "islands of the", "Aether realm.", "", 0));
        lores.add(new AetherLore(AetherBlocks.Present, "Present", "Found around the", "base of Christmas", "Trees. When opened,", "it will either", "spawn a nasty or", " nice surprise.", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.ChristmasLeaves, 1, 0), "Christmas Leaves", "Found on Christmas", "Trees. Adventurers", "have observed these", "leaves producing", "an array of snow", "flakes around them.", 0));
        lores.add(new AetherLore(new ItemStack(AetherBlocks.ChristmasLeaves, 1, 1), "Decorative Leaves", "Found on Christmas", "Trees. These leaves", "can be found with", "many decorations", "in them.", "", 0));
        lores.add(new AetherLore(AetherBlocks.Aerogel, "Aerogel", "Found in dungeons.", "Incredibly high", "TNT resistance", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.DartShooter, 1, 3), "Phoenix Dart Shooter", "Found in dungeons.", "Shoots flaming", "darts that", "burn mobs", "", "", 0));
        lores.add(new AetherLore(AetherItems.GummieSwet, "Gummy Swet", "Found in dungeons.", "Tasty swet", "flavoured", "gummy swets", "(May contain swet)", "", 0));
        lores.add(new AetherLore(AetherItems.FlamingSword, "Fire Sword", "Found in dungeons.", "A sword imbued", "with the power of", "fire", "", "", 0));
        lores.add(new AetherLore(AetherItems.LightningSword, "Lightning Sword", "Found in dungeons.", "A sword imbued", "with the power of", "lightning", "", "", 0));
        lores.add(new AetherLore(AetherItems.HolySword, "Holy Sword", "Found in dungeons.", "A holy sword that", "will deal extra", "damage to undead", "", "", 0));
        lores.add(new AetherLore(AetherItems.HammerOfNotch, "Hammer of Notch", "Found in dungeons.", "A hammer that", "has a special", "attack which hits", "lots of mobs", "", 0));
        lores.add(new AetherLore(AetherItems.LightningKnife, "Lightning Knife", "Found in dungeons.", "Throwable.", "Creates lightning", "on hit.", "", "", 0));
        lores.add(new AetherLore(AetherItems.PigSlayer, "Pig Slayer", "Found in Dungeons.", "Very good dagger.", "Kills pigs and", "pig zombies in", "1 hit", "", 0));
        lores.add(new AetherLore(AetherItems.VictoryMedal, "Medallion", "Dropped by ", "Valkyries.", "A sign of victory", "from the Valkyries", "that you need to ", "fight the boss", 0));
        lores.add(new AetherLore(AetherItems.HolystonePickaxe, "Holystone Pickaxe", "Digs holystone", "and Aether ores.", "Randomly gives", "extra ambrosium.", "Slow Speed", "Average Uses", 0));
        lores.add(new AetherLore(AetherItems.HolystoneAxe, "Holystone Axe", "Chops Skyroot", "and Gilded Oak.", "Randomly gives", "extra ambrosium.", "Slow Speed", "Average Uses", 0));
        lores.add(new AetherLore(AetherItems.HolystoneShovel, "Holystone Shovel", "Digs Aether dirt,", "and quicksoil.", "Randomly gives", "extra ambrosium.", "Slow Speed", "Average Uses", 0));
        lores.add(new AetherLore(AetherItems.HolystoneSword, "Holystone Sword", "For attacking", "mobs and animals.", "Randomly gives", "extra ambrosium.", "Slow Speed", "Average Uses", 0));
        lores.add(new AetherLore(AetherItems.SkyrootPickaxe, "Skyroot Pickaxe", "Digs Holystone", "and Aether ores.", "Randomly gives", "double drops.", "Very Slow Speed", "Few Uses", 0));
        lores.add(new AetherLore(AetherItems.SkyrootAxe, "Skyroot Axe", "Chops Skyroot", "and Gilded Oak.", "Randomly gives", "double drops.", "Very Slow Speed", "Few Uses", 0));
        lores.add(new AetherLore(AetherItems.SkyrootShovel, "Skyroot Shovel", "Digs Aether dirt,", "and quicksoil.", "Randomly gives", "double drops.", "Very Slow Speed", "Few Uses", 0));
        lores.add(new AetherLore(AetherItems.SkyrootSword, "Skyroot Sword", "For attacking", "mobs and animals.", "Randomly gives", "double drops.", "Very Slow Speed", "Few Uses", 0));
        lores.add(new AetherLore(AetherItems.ZanitePickaxe, "Zanite Pickaxe", "Digs Holystone", "and Aether ores.", "Power increases", "with damage.", "Normal Speed", "Many Uses", 0));
        lores.add(new AetherLore(AetherItems.ZaniteAxe, "Zanite Axe", "Chops Skyroot", "and Gilded Oak.", "Power increases", "with damage.", "Normal Speed", "Many Uses", 0));
        lores.add(new AetherLore(AetherItems.ZaniteShovel, "Zanite Shovel", "Digs Aether dirt,", "and quicksoil.", "Power increases", "with damage.", "Normal Speed", "Many Uses", 0));
        lores.add(new AetherLore(AetherItems.ZaniteSword, "Zanite Sword", "For attacking", "mobs and animals.", "Power increases", "with damage.", "Normal Speed", "Many Uses", 0));
        lores.add(new AetherLore(AetherItems.GravititePickaxe, "Gravitite Pickaxe", "Digs Holystone", "and Aether ores.", "Right click will", "lift mobs.", "Fast Speed", "Very Many Uses", 0));
        lores.add(new AetherLore(AetherItems.GravititeAxe, "Gravitite Axe", "Chops Skyroot", "and Gilded Oak.", "Right click will", "lift mobs.", "Fast Speed", "Very Many Uses", 0));
        lores.add(new AetherLore(AetherItems.GravititeShovel, "Gravitite Shovel", "Digs Aether dirt,", "and quicksoil.", "Right click will", "lift mobs.", "Fast Speed", "Very Many Uses", 0));
        lores.add(new AetherLore(AetherItems.GravititeSword, "Gravitite Sword", "For attacking", "mobs and animals.", "Right click will", "lift mobs.", "Fast Speed", "Very Many Uses", 0));
        lores.add(new AetherLore(AetherItems.AmbrosiumShard, "Ambrosium Shard", "Found in Holystone.", "Makes Ambrosium", "Torches and is", "the fuel of the", "Altar.", "Can enchant grass.", 0));
        lores.add(new AetherLore(AetherItems.ZaniteGemstone, "Zanite Gemstone", "Found in Holystone.", "Makes Zanite tools", "and enchanters", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.SkyrootStick, "Skyroot Stick", "Made from skyroot", "planks.", "Vital crafting", "item for Aether", "tools", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.SkyrootBucket, 1, 0), "Skyroot Bucket", "Made from skyroot.", "Can pick up water,", "milk and poison", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.SkyrootBucket, 1, Block.waterMoving.blockID), "Skyroot Water Bucket", "A skyroot bucket", "full of water", "", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.SkyrootBucket, 1, 1), "Skyroot Milk Bucket", "A skyroot bucket", "full of milk", "", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.GoldenAmber, "Golden Amber", "Dropped by golden", "oaks.", "Used to make", "golden darts", "", "", 0));
        lores.add(new AetherLore(AetherItems.MoaEgg, "Moa Egg", "Laid by Moas.", "Place in an", "incubator to", "hatch  it", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.Key, 1, 0), "Bronze Key", "Dropped by the", "Slider.", "Use it to gain", "access to the", "bronze treasure", "chest", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.Key, 1, 1), "Silver Key", "Dropped by the", "Grand Valkyrie.", "Use it to gain", "access to the silver", "treasure chest", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.Key, 1, 2), "Gold Key", "Dropped by the", "Sun Spririt", "Use it to gain", "access to the gold", "treasure chest", "", 0));
        lores.add(new AetherLore(AetherItems.AechorPetal, "Aechor Petal", "Dropped by Aechor", "Plants.", "Used to tame and", "feed Moas", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.DartShooter, 1, 0), "Dart Shooter", "Found in Dungeons.", "Shoots gold darts", "", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.DartShooter, 1, 1), "Enchanted Shooter", "Shoots enchanted", "darts", "", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.DartShooter, 1, 2), "Poison Shooter", "Shoots poison", "darts", "", "", "", "", 2));
        lores.add(new AetherLore(new ItemStack(AetherItems.Dart, 1, 0), "Golden Dart", "Found in Dungeons", "and crafted from", "golden orbs and", "skyroot sticks.", "Simplest ammo", "for the dart shooter", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.Dart, 1, 1), "Enchanted Dart", "Found in Dungeons.", "Enchantable from", "Golden Darts.", "Has more attack", "than a golden dart", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.Dart, 1, 2), "Poison Dart", "Found in Dungeons.", "Craftable from", "Golden Darts and", "poison buckets.", "Ammo for the dart", "shooter that poisons", 0));
        lores.add(new AetherLore(AetherItems.AetherMusicDisk, "Blue Music Disk", "Found in Dungeons.", "Can be played", "in jukeboxes.", "Plays the Aether", "tune", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.SkyrootBucket, 1, 3), "Bucket of Remedy", "Enchantable from", "bucket of poison.", "Cures poison", "", "", "", 0));
        lores.add(new AetherLore(new ItemStack(AetherItems.SkyrootBucket, 1, 2), "Bucket of Posion", "Found in Dungeons.", "Obtainable from", "Aechor Plants.", "Used to make poison", "darts", "", 0));
        lores.add(new AetherLore(AetherItems.CloudParachute, "Cloud Parachute", "Made from clouds.", "Will float the player", "gently down.", "Activates on click", "or when falling", "from the Aether", 0));
        lores.add(new AetherLore(AetherItems.GoldenCloudParachute, "Gold Cloud Parachute", "Made from gold", "clouds.", "Similar to Cloud", "Parachute, but", "has 4 uses.", "", 0));
        lores.add(new AetherLore(AetherItems.IronBubble, "Iron Bubble", "Found in dungeons.", "Allows you to", "breathe", "underwater forever", "", "", 0));
        lores.add(new AetherLore(AetherItems.VampireBlade, "Vampire Blade", "Found in dungeons.", "Powerful sword", "that drains the", "health of anything", "hit", "", 0));
        lores.add(new AetherLore(AetherItems.ShardOfLife, "Life Shard", "Found in Dungeons.", "Increases your", "maximum health by", "one heart", "", "", 0));
        lores.add(new AetherLore(AetherItems.ValkyrieCape, "Valkyrie Cape", "Found in Dungeons.", "While wearing this", "you will float", "gently to the ground", "and take no fall", "damage", 0));
        lores.add(new AetherLore(AetherItems.ValkyrieLance, "Valkyrie Lance", "Found in Dungeons.", "Powerful weapon", "with extended", "reach", "", "", 0));
        lores.add(new AetherLore(AetherItems.SwetCape, "Swet Cape", "Found in Dungeons.", "Wear it as a cape.", "Purely aesthetic", "item", "", "", 0));
        lores.add(new AetherLore(AetherItems.ZanitePendant, "Zanite Pendant", "Made from zanite.", "Wear it in your", "pendant slot.", "As it wears away", "it increases your", "mining speed", 0));
        lores.add(new AetherLore(AetherItems.ZaniteRing, "Zanite Ring", "Made from zanite.", "Wear it in your", "ring slot.", "As it wears away", "it increases your", "mining speed", 0));
        lores.add(new AetherLore(AetherItems.PhoenixHelmet, "Phoenix Helmet", "Found in dungeons.", "Protects the ", "wearer from any", "fire or lava damage.", "Weak to water, but", "it holds a secret", 0));
        lores.add(new AetherLore(AetherItems.PhoenixChestplate, "Phoenix Chestplate", "Found in dungeons.", "Protects the ", "wearer from any", "fire or lava damage.", "Weak to water, but", "it holds a secret", 0));
        lores.add(new AetherLore(AetherItems.PhoenixLeggings, "Phoenix Leggings", "Found in dungeons.", "Protects the ", "wearer from any", "fire or lava damage.", "Weak to water, but", "it holds a secret", 0));
        lores.add(new AetherLore(AetherItems.PhoenixBoots, "Phoenix Boots", "Found in dungeons.", "Protects the ", "wearer from any", "fire or lava damage.", "Weak to water, but", "it holds a secret", 0));
        lores.add(new AetherLore(AetherItems.HealingStone, "Healing Stone", "Gained by enchanting", "Holystone blocks.", "Heals four hearts", "of health.", "", "", 0));
        lores.add(new AetherLore(AetherItems.RedCape, "Red Cape", "Worn in cape", "slot.", "Purely decorative.", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.BlueCape, "Blue Cape", "Worn in cape", "slot.", "Purely decorative.", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.WhiteCape, "White Cape", "Worn in cape", "slot.", "Purely decorative.", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.YellowCape, "Yellow Cape", "Worn in cape", "slot.", "Purely decorative.", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.IcePendant, "Ice Pendant", "Worn in pendant", "slot.", "Freezes all water", "and lava sources", "around the player.", "", 0));
        lores.add(new AetherLore(AetherItems.IceRing, "Ice Ring", "Worn in ring", "slots.", "Freezes all water", "and lava sources", "around the player.", "", 0));
        lores.add(new AetherLore(AetherItems.AgilityCape, "Agility Cape", "Worn in cape", "slot.", "Gives the player", "the ability to", "walk up blocks", "without jumping.", 0));
        lores.add(new AetherLore(AetherItems.ValkyriePickaxe, "Valkyrie Pickaxe", "A powerful pickaxe", "which once belonged", "to a Valkyrie.", "Has extended reach.", "", "", 0));
        lores.add(new AetherLore(AetherItems.ValkyrieAxe, "Valkyrie Axe", "A powerful axe", "which once belonged", "to a Valkyrie.", "Has extended reach.", "", "", 0));
        lores.add(new AetherLore(AetherItems.ValkyrieShovel, "Valkyrie Shovel", "A powerful shovel", "which once belonged", "to a Valkyrie.", "Has extended reach.", "", "", 0));
        lores.add(new AetherLore(AetherItems.ObsidianHelmet, "Obsidian Helmet", "A cooled version of", "the Phoenix", "Helmet.", "Incredibly strong", "head armour", "", 0));
        lores.add(new AetherLore(AetherItems.ObsidianChestplate, "Obsidian Chestplate", "A cooled version of", "the Phoenix", "Chestplate.", "Incredibly strong", "chest armour", "", 0));
        lores.add(new AetherLore(AetherItems.ObsidianLeggings, "Obsidian Leggings", "A cooled version of", "the Phoenix", "Leggings.", "Incredibly strong", "leg armour", "", 0));
        lores.add(new AetherLore(AetherItems.ObsidianBoots, "Obsidian Boots", "A cooled version of", "the Phoenix", "Boots.", "Incredibly strong", "foot armour", "", 0));
        lores.add(new AetherLore(AetherItems.ZaniteHelmet, "Zanite Helmet", "Wear it on your", "head.", "Good Protection.", "Provides better", "protection when", "more damaged", 0));
        lores.add(new AetherLore(AetherItems.ZaniteChestplate, "Zanite Chestplate", "Wear it on your", "chest.", "Good Protection.", "Provides better", "protection when", "more damaged", 0));
        lores.add(new AetherLore(AetherItems.ZaniteLeggings, "Zanite Greaves", "Wear it on your", "legs.", "Good Protection.", "Provides better", "protection when", "more damaged", 0));
        lores.add(new AetherLore(AetherItems.ZaniteBoots, "Zanite Boots", "Wear it on your", "feet.", "Good Protection.", "Provides better", "protection when", "more damaged", 0));
        lores.add(new AetherLore(AetherItems.GravititeHelmet, "Gravitite Helmet", "Wear it on your", "head.", "Great Protection.", "Full set stops", "fall damage and", "jumps higher", 0));
        lores.add(new AetherLore(AetherItems.GravititeChestplate, "Gravitite Chestplate", "Wear it on your", "chest.", "Great Protection.", "Full set stops", "fall damage and", "jumps higher", 0));
        lores.add(new AetherLore(AetherItems.GravititeLeggings, "Gravitite Leggings", "Wear it on your", "legs.", "Great Protection.", "Full set stops", "fall damage and", "jumps higher", 0));
        lores.add(new AetherLore(AetherItems.GravititeBoots, "Gravitite Boots", "Wear it on your", "feet.", "Great Protection.", "Full set stops", "fall damage and", "jumps higher", 0));
        lores.add(new AetherLore(AetherItems.NeptuneHelmet, "Neptune Helmet", "Found in dungeons.", "Wear it on your", "head.", "Great Protection.", "Full set speeds", "up water movement", 0));
        lores.add(new AetherLore(AetherItems.NeptuneChestplate, "Neptune Chestplate", "Found in dungeons.", "Wear it on your", "chest.", "Great Protection.", "Full set speeds", "up water movement", 0));
        lores.add(new AetherLore(AetherItems.NeptuneLeggings, "Neptune Greaves", "Found in dungeons.", "Wear it on your", "legs.", "Great Protection.", "Full set speeds", "up water movement", 0));
        lores.add(new AetherLore(AetherItems.NeptuneBoots, "Neptune Boots", "Found in dungeons.", "Wear it on your", "feet.", "Great Protection.", "Full set speeds", "up water movement", 0));
        lores.add(new AetherLore(AetherItems.ZaniteGloves, "Zanite Gloves", "Wear them on your", "hands.", "Good Protection", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.GravititeGloves, "Gravitite Gloves", "Wear them on your", "hands.", "Great Protection.", "Full set stops", "fall damage and", "jumps higher", 0));
        lores.add(new AetherLore(AetherItems.ObsidianGloves, "Obsidian Gloves", "A cooled version of", "the Phoenix", "Glove.", "Incredibly strong", "hand armour", "", 0));
        lores.add(new AetherLore(AetherItems.PhoenixGloves, "Phoenix Gloves", "Found in dungeons.", "Protects the ", "wearer from any", "fire or lava damage.", "Weak to water, but", "it holds a secret", 0));
        lores.add(new AetherLore(AetherItems.NeptuneGloves, "Neptune Gloves", "Found in dungeons.", "Wear them on your", "hands.", "Great Protection", "", "", 0));
        lores.add(new AetherLore(AetherItems.RegenerationStone, "Regeneration Stone", "Use it in your", "accessory slots.", "Regenerates health", "over time", "", "", 0));
        lores.add(new AetherLore(AetherItems.InvisibilityCloak, "Invisibility Cloak", "Use it in your", "cloak slot.", "Makes you invisible", "", "", "", 0));
        lores.add(new AetherLore(AetherItems.CandyCaneSword, "Candy Cane Sword", "Found in Presents", "underneath many", "Christmas Trees.", "Generates Candy", "Canes when used.", "", 0));
        lores.add(new AetherLore(AetherItems.CandyCane, "Candy Cane", "Generated when", "you are using", "a Candy Sword.", "Replenishes", "hunger.", "", 0));
        lores.add(new AetherLore(AetherItems.GingerBreadMan, "Ginger Bread Man", "Found in Presents", "underneath many", "Christmas Trees.", "Replenishes", "hunger.", "", 0));
        lores.add(new AetherLore(AetherItems.BlueBerry, "Blue Berry", "Found in Berry", "Bushes around", "the Aether realm.", "Replenishes", "hunger.", "", 0));
        lores.add(new AetherLore(AetherItems.WhiteApple, "White Apple", "Found in Crystal", "Fruit Leaves in", "the Aether realm.", "Cures poison.", "", "", 0));
        lores.add(new AetherLore(AetherItems.SwettyBall, "Swetty Ball", "Dropped by the", "gelatine-like", "Swets. Right click", "dirt to grow", "grass.", "", 0));
        lores.add(new AetherLore(AetherItems.SentryBoots, "Sentry Boots", "Found in Bronze", "Dungeon chests.", "Negates the", "effect of Zephyr", "snowballs.", "", 0));
    }
}
