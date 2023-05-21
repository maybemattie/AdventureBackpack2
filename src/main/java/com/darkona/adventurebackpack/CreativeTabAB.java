package com.darkona.adventurebackpack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.darkona.adventurebackpack.init.ModItems;
import com.darkona.adventurebackpack.reference.ModInfo;

public class CreativeTabAB {

    public static final CreativeTabs TAB_AB = new CreativeTabs(ModInfo.MOD_ID) {

        @Override
        public Item getTabIconItem() {
            return ModItems.machete;
        }

        @Override
        public String getTabLabel() {
            return ModInfo.MOD_ID;
        }
    };
}
