package com.rirust;

import com.rirust.networking.NetworkHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    public static final String KEY_CATEGORY_BIKES = "key.category.bikes.shift";
    public static final String KEY_SHIFT_UP = "key.bikes.shift_up";
    public static final String KEY_SHIFT_DOWN = "key.bikes.shift_down";

    public static KeyBinding shiftUpKey;
    public static KeyBinding shiftDownKey;

    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(shiftUpKey.wasPressed()){
                ClientPlayNetworking.send(NetworkHandler.SHIFT_UP, PacketByteBufs.create());
            }

            if(shiftDownKey.wasPressed()){
                ClientPlayNetworking.send(NetworkHandler.SHIFT_DOWN, PacketByteBufs.create());
            }
        });
    }

    public static void register(){
        shiftUpKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SHIFT_UP,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP,
                KEY_CATEGORY_BIKES
        ));

        shiftDownKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SHIFT_DOWN,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                KEY_CATEGORY_BIKES
        ));

        registerKeyInputs();
    }

}
