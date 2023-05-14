package com.darkona.adventurebackpack.proxy;

import net.minecraft.nbt.NBTTagCompound;

public interface IProxy {

    void init();

    void registerKeybindings();

    void synchronizePlayer(int id, NBTTagCompound compound);
}
