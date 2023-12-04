package com.rirust.networking;

import com.rirust.Bikes;
import com.rirust.networking.packets.shifting.ShiftDownC2SPacket;
import com.rirust.networking.packets.shifting.ShiftUpC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkHandler {

    public static final Identifier SHIFT_UP = new Identifier(Bikes.MODID, "shiftup");
    public static final Identifier SHIFT_DOWN = new Identifier(Bikes.MODID, "shiftdown");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(SHIFT_UP, ShiftUpC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SHIFT_DOWN, ShiftDownC2SPacket::receive);
    }
}
