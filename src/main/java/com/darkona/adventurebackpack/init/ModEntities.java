package com.darkona.adventurebackpack.init;

import com.darkona.adventurebackpack.AdventureBackpack;
import com.darkona.adventurebackpack.entity.EntityFriendlySpider;
import com.darkona.adventurebackpack.entity.EntityInflatableBoat;

import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

    public static void init() {
        int counter = 0;
        EntityRegistry.registerModEntity(
                EntityInflatableBoat.class,
                "inflatableBoat",
                counter++,
                AdventureBackpack.instance,
                64,
                1,
                true);
        EntityRegistry.registerModEntity(
                EntityFriendlySpider.class,
                "rideableSpider",
                counter++,
                AdventureBackpack.instance,
                64,
                2,
                true);
    }
}
