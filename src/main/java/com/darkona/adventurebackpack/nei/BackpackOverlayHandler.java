package com.darkona.adventurebackpack.nei;

import codechicken.nei.recipe.DefaultOverlayHandler;
import com.darkona.adventurebackpack.inventory.SlotBackpack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

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
