package com.darkona.adventurebackpack.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkona.adventurebackpack.block.TileAdventureBackpack;
import com.darkona.adventurebackpack.common.Constants;
import com.darkona.adventurebackpack.common.Constants.Source;
import com.darkona.adventurebackpack.config.ConfigHandler;
import com.darkona.adventurebackpack.init.ModNetwork;
import com.darkona.adventurebackpack.inventory.ContainerBackpack;
import com.darkona.adventurebackpack.inventory.IInventoryBackpack;
import com.darkona.adventurebackpack.inventory.InventoryBackpack;
import com.darkona.adventurebackpack.network.HiddenPacket;
import com.darkona.adventurebackpack.network.PlayerActionPacket;
import com.darkona.adventurebackpack.network.SleepingBagPacket;
import com.darkona.adventurebackpack.reference.LoadedMods;
import com.darkona.adventurebackpack.util.Resources;
import com.darkona.adventurebackpack.util.TinkersUtils;

import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerTooltipHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvBackpack extends GuiWithTanks {

    private static final ResourceLocation TEXTURE = Resources.guiTextures("guiBackpackNew");
    private static final int TINKERS_SLOT = 38; // ContainerBackpack.CRAFT_MATRIX_EMULATION[4]

    private static final GuiImageButtonNormal bedButton = new GuiImageButtonNormal(5, 90, 18, 18);
    private static final GuiImageButtonNormal equipButton = new GuiImageButtonNormal(5, 90, 18, 18);
    private static final GuiImageButtonNormal unequipButton = new GuiImageButtonNormal(5, 90, 18, 18);
    private static final GuiImageButtonNormal hideButton = new GuiImageButtonNormal(5, 71, 18, 18);
    private static final GuiImageButtonNormal unhideButton = new GuiImageButtonNormal(5, 71, 18, 18);
    private static final GuiTank tankLeft = new GuiTank(25, 7, 100, 16, ConfigHandler.typeTankRender);
    private static final GuiTank tankRight = new GuiTank(207, 7, 100, 16, ConfigHandler.typeTankRender);

    private final IInventoryBackpack inventory;

    private boolean isHoldingSpace;

    public GuiAdvBackpack(EntityPlayer player, TileAdventureBackpack tileBackpack, Source source) {
        super(new ContainerBackpack(player, tileBackpack, source));
        this.player = player;
        inventory = tileBackpack;
        this.source = source;
        xSize = 248;
        ySize = 207;
    }

    public GuiAdvBackpack(EntityPlayer player, InventoryBackpack inventoryBackpack, Source source) {
        super(new ContainerBackpack(player, inventoryBackpack, source));
        this.player = player;
        inventory = inventoryBackpack;
        this.source = source;
        xSize = 248;
        ySize = 207;
    }

    private boolean isBedButtonCase() {
        return source == Source.TILE
                || (ConfigHandler.portableSleepingBag && source == Source.WEARING && GuiScreen.isShiftKeyDown());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);
        this.mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Buttons and button highlight
        if (isBedButtonCase()) {
            if (bedButton.inButton(this, mouseX, mouseY)) bedButton.draw(this, 20, 227);
            else bedButton.draw(this, 1, 227);
        } else if (source == Source.WEARING) {
            if (unequipButton.inButton(this, mouseX, mouseY)) unequipButton.draw(this, 96, 227);
            else unequipButton.draw(this, 77, 227);
        } else if (source == Source.HOLDING) {
            if (equipButton.inButton(this, mouseX, mouseY)) equipButton.draw(this, 96, 208);
            else equipButton.draw(this, 77, 208);
        }

        // If already hidden, show unhide button, else show hide button
        if (source == Source.WEARING) {
            if (inventory.isHidden()) {
                if (unhideButton.inButton(this, mouseX, mouseY)) unhideButton.draw(this, 134, 208);
                else unhideButton.draw(this, 115, 208);
            } else {
                if (hideButton.inButton(this, mouseX, mouseY)) hideButton.draw(this, 134, 227);
                else hideButton.draw(this, 115, 227);
            }
        }

        if (LoadedMods.TCONSTRUCT && ConfigHandler.tinkerToolsMaintenance) {
            if (inventory.getStackInSlot(TINKERS_SLOT) == null) {
                this.mc.getTextureManager().bindTexture(TinkersUtils.GUI_ICONS);
                this.drawTexturedModalRect(this.guiLeft + 169, this.guiTop + 77, 0, 233, 18, 18);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        inventory.openInventory();
        FluidTank lft = inventory.getLeftTank();
        FluidTank rgt = inventory.getRightTank();
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);

        tankLeft.draw(this, lft);
        tankRight.draw(this, rgt);

        GL11.glPopAttrib();
    }

    @Override
    protected GuiImageButtonNormal getEquipButton() {
        return equipButton;
    }

    @Override
    protected GuiImageButtonNormal getUnequipButton() {
        return unequipButton;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (isBedButtonCase() && bedButton.inButton(this, mouseX, mouseY)) {
            if (source == Source.TILE) {
                TileAdventureBackpack te = (TileAdventureBackpack) inventory;
                ModNetwork.net
                        .sendToServer(new SleepingBagPacket.SleepingBagMessage(true, te.xCoord, te.yCoord, te.zCoord));
            } else {
                int posX = MathHelper.floor_double(player.posX);
                int posY = MathHelper.floor_double(player.posY) - 1;
                int posZ = MathHelper.floor_double(player.posZ);
                ModNetwork.net.sendToServer(new SleepingBagPacket.SleepingBagMessage(false, posX, posY, posZ));
            }
        } else if (hideButton.inButton(this, mouseX, mouseY)) {
            if (source != Source.WEARING) return;
            ModNetwork.net.sendToServer(new HiddenPacket.HiddenPacketMessage(this.inventory.isHidden()));
            if (inventory.isHidden()) {
                ((InventoryBackpack) inventory).setUnhidden();
            } else {
                ((InventoryBackpack) inventory).setHidden();
            }
        } else {
            super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!isHoldingSpace) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                isHoldingSpace = true;
                ModNetwork.net.sendToServer(new PlayerActionPacket.ActionMessage(PlayerActionPacket.GUI_HOLDING_SPACE));
                inventory.getExtendedProperties().setBoolean(Constants.TAG_HOLDING_SPACE, true);
            }
        } else {
            if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                isHoldingSpace = false;
                ModNetwork.net
                        .sendToServer(new PlayerActionPacket.ActionMessage(PlayerActionPacket.GUI_NOT_HOLDING_SPACE));
                inventory.getExtendedProperties().removeTag(Constants.TAG_HOLDING_SPACE);
            }
        }
    }

    public List<String> getHiddenTooltip() {
        ArrayList<String> tooltips = new ArrayList<>();
        if (inventory.isHidden()) {
            tooltips.add("Show backpack");
        } else {
            tooltips.add("Hide backpack");
        }
        return tooltips;
    }

    /**
     * An instance of this class will handle tooltips for all instances of GuiAdvBackpack
     */
    public static class TooltipHandler implements IContainerTooltipHandler {

        @Override
        public List<String> handleTooltip(GuiContainer gui, int mouseX, int mouseY, List<String> currenttip) {
            if (gui.getClass() != GuiAdvBackpack.class) return currenttip;

            GuiWithTanks backpackGui = (GuiWithTanks) gui;

            // Fluid tank tooltips
            if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0) {
                if (tankLeft.inTank(backpackGui, mouseX, mouseY)) currenttip.addAll(tankLeft.getTankTooltip());

                if (tankRight.inTank(backpackGui, mouseX, mouseY)) currenttip.addAll(tankRight.getTankTooltip());
            }

            // Hidden tooltip
            if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.isEmpty()) {
                if (!hideButton.inButton(backpackGui, mouseX, mouseY)) return currenttip;
                currenttip.addAll(((GuiAdvBackpack) gui).getHiddenTooltip());
            }

            return currenttip;
        }

        /**
         * Required by IContainerTooltipHandler implementation but not needed here
         */
        @Override
        public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
            return currenttip;
        }

        /**
         * Required by IContainerTooltipHandler implementation but not needed here
         */
        @Override
        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey,
                List<String> currenttip) {
            return currenttip;
        }
    }

    static {
        // Only instantiate TooltipHandler if enabled in config.
        if (ConfigHandler.tanksHoveringText) {
            GuiContainerManager.addTooltipHandler(new TooltipHandler());
        }
    }
}
