package com.ririthenerd;

import com.ririthenerd.entities.BikeEntity;
import com.ririthenerd.entities.EntityManager;
import com.ririthenerd.items.ItemManager;
import com.ririthenerd.networking.NetworkHandler;
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