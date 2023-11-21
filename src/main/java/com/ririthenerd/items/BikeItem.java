package com.ririthenerd.items;

import com.ririthenerd.entities.BikeEntity;
import com.ririthenerd.entities.EntityManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class BikeItem extends Item {
    public BikeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BikeEntity bike = new BikeEntity(EntityManager.BIKE, context.getWorld());

        bike.setOwnerUUID(context.getPlayer().getUuid());
        bike.setLocked(false);
        bike.setPosition(new Vec3d(context.getBlockPos().getX(), context.getBlockPos().getY() + 1, context.getBlockPos().getZ()));
        bike.changeGear(true, 4);
        bike.changeGear(false, 2);

        context.getWorld().spawnEntity(bike);
        context.getPlayer().getMainHandStack().decrement(1);
        return super.useOnBlock(context);
    }
}