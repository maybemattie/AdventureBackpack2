package com.darkona.adventurebackpack.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import com.darkona.adventurebackpack.init.ModNetwork;
import com.darkona.adventurebackpack.network.GUIPacket;
import com.darkona.adventurebackpack.reference.BackpackTypes;

import tconstruct.client.tabs.AbstractTab;

public class TConstructTab extends AbstractTab {

    public TConstructTab() {
        super(5, 0, 0, BackpackUtils.createBackpackStack(BackpackTypes.STANDARD));
    }

    @Override
    public void onTabClicked() {
        EntityClientPlayerMP playerMP = Minecraft.getMinecraft().thePlayer;
        byte backpackType = 0;
        if (Wearing.getWearingBackpack(playerMP) != null) backpackType = GUIPacket.BACKPACK_GUI;
        else if (Wearing.getWearingCopter(playerMP) != null) backpackType = GUIPacket.COPTER_GUI;
        else if (Wearing.getWearingJetpack(playerMP) != null) backpackType = GUIPacket.JETPACK_GUI;
        else return;
        ModNetwork.net.sendToServer(new GUIPacket.GUImessage(backpackType, GUIPacket.FROM_WEARING));

    }

    @Override
    public boolean shouldAddToList() {
        return Wearing.isWearingWearable(Minecraft.getMinecraft().thePlayer);
    }
}
