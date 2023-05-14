package com.darkona.adventurebackpack.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemAdventureSuit extends ArmorAB {

    public ItemAdventureSuit() {
        super(1, 1);
        setMaxDamage(Items.leather_chestplate.getMaxDamage() + 70);
        setUnlocalizedName("adventureSuit");
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.isItemEqual(new ItemStack(Items.leather));
    }
}
