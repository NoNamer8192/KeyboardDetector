package com.keyboarddetector;

import com.keyboarddetector.network.S2CInputPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class KeyboardDetectorClient implements ClientModInitializer {

    private static boolean isKeyPressed(int keyCode) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyCode);
    }

    @Override
    public void onInitializeClient() {

        // 注册客户端tick事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            Set<Byte> pressedKeys = new HashSet<>();
            for (byte ascii = 8; ascii < 124; ascii++) {
                if (isKeyPressed(ascii)) {
                    pressedKeys.add(ascii);
                }
            }
            if (!pressedKeys.isEmpty()) {
                ClientPlayNetworking.send(new S2CInputPayload(pressedKeys));
            }
        });
    }
}
