package com.rirust.items;

import com.rirust.Bikes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemManager {

    public static final Item BIKE_ITEM = new BikeItem(new FabricItemSettings().maxCount(1));
    public static final Item BIKE_LOCK_ITEM = new BikeLockItem(new FabricItemSettings().maxCount(1));

    public static void registerItems(){
        Registry.register(Registries.ITEM, new Identifier(Bikes.MODID, "bike_item"), BIKE_ITEM);
        Registry.register(Registries.ITEM, new Identifier(Bikes.MODID, "bike_lock_item"), BIKE_LOCK_ITEM);
    }

}