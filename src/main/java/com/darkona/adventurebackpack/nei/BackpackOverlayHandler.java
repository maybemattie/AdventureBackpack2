package com.darkona.adventurebackpack.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import codechicken.nei.recipe.DefaultOverlayHandler;

import com.darkona.adventurebackpack.inventory.SlotBackpack;

public class BackpackOverlayHandler extends DefaultOverlayHandler {

    public BackpackOverlayHandler() {
        super(127, 55);
    }

    @Override
    public boolean canMoveFrom(Slot slot, GuiContainer gui) {
        if (slot instanceof SlotBackpack) {
            return true;
        }
        return super.canMoveFrom(slot, gui);
    }
}
