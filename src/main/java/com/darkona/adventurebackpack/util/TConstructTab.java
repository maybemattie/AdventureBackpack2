package com.darkona.adventurebackpack.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.handlers.GuiHandler;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import tconstruct.client.tabs.AbstractTab;

public class TConstructTab extends AbstractTab {

    public TConstructTab() {
        super(0, 0, 0, Wearing.getWearingWearable(Minecraft.getMinecraft().thePlayer));
    }

    @Override
    public void onTabClicked() {
        EntityClientPlayerMP playerMP = Minecraft.getMinecraft().thePlayer;
        int backpackType = 0;
        if (Wearing.getWearingBackpack(playerMP) != null) backpackType = GuiHandler.BACKPACK_WEARING;
        else if (Wearing.getWearingCopter(playerMP) != null) backpackType = GuiHandler.COPTER_WEARING;
        else if (Wearing.getWearingJetpack(playerMP) != null) backpackType = GuiHandler.JETPACK_WEARING;
        else return;
        FMLNetworkHandler.openGui(
                playerMP,
                AdventureBackpack.instance,
                backpackType,
                playerMP.getEntityWorld(),
                (int) playerMP.posX,
                (int) playerMP.posY,
                (int) playerMP.posZ);

    }

    @Override
    public boolean shouldAddToList() {
        return Wearing.isWearingWearable(Minecraft.getMinecraft().thePlayer);
    }
}
