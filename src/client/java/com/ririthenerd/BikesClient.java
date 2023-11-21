package com.ririthenerd;

import com.ririthenerd.bike.BikeEntityModel;
import com.ririthenerd.bike.BikeEntityRenderer;
import com.ririthenerd.entities.EntityManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BikesClient implements ClientModInitializer {

	public static final EntityModelLayer MODEL_BIKE_LAYER = new EntityModelLayer(new Identifier(Bikes.MODID, "bike"), "main");
	@Override
	public void onInitializeClient() {
		KeyHandler.register();

		EntityRendererRegistry.register(EntityManager.BIKE, (context) -> new BikeEntityRenderer(context, MODEL_BIKE_LAYER));
		EntityModelLayerRegistry.registerModelLayer(MODEL_BIKE_LAYER, BikeEntityModel::getTexturedModelData);
	}
}