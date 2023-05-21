package com.darkona.adventurebackpack.nei;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

import com.darkona.adventurebackpack.client.gui.GuiAdvBackpack;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.GuiRecipe;

public class BackpackStackPositioner implements IStackPositioner {

    @Override
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> stacks) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiRecipe) {
            GuiRecipe recipeGui = (GuiRecipe) Minecraft.getMinecraft().currentScreen;
            if (!(recipeGui.firstGui instanceof GuiAdvBackpack)) {
                return stacks;
            }
            for (PositionedStack stack : stacks) {
                stack.relx += 127;
                stack.rely += 55;
            }
        }
        return stacks;
    }
}
