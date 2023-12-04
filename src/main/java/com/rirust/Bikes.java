package com.rirust;

import com.rirust.entities.BikeEntity;
import com.rirust.entities.EntityManager;
import com.rirust.items.ItemManager;
import com.rirust.networking.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class Bikes implements ModInitializer {

	public static final String MODID = "bikes";

	@Override
	public void onInitialize() {
		NetworkHandler.registerC2SPackets();
		ItemManager.registerItems();

		FabricDefaultAttributeRegistry.register(EntityManager.BIKE, BikeEntity.createBikeAttributes());
	}
}