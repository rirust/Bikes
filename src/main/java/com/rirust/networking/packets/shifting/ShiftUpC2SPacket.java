package com.rirust.networking.packets.shifting;

import com.rirust.entities.BikeEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ShiftUpC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSend){
        if(player.hasVehicle()){
            if(player.getVehicle() instanceof BikeEntity){
                BikeEntity bike = (BikeEntity) player.getVehicle();
                bike.changeGear(true, 1);
            }
        }
    }
}
