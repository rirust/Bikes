package com.rirust.entities;

import com.rirust.Bikes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityManager {

    public static final EntityType<BikeEntity> BIKE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Bikes.MODID, "bike_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BikeEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f)).build());

}
