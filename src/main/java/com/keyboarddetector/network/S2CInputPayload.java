package com.keyboarddetector.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.keyboarddetector.KeyboardDetector.KEY_PRESSED_PACKET_ID;

public record S2CInputPayload(Set<Byte> asciiCodes) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CInputPayload> ID = new CustomPacketPayload.Type<>(KEY_PRESSED_PACKET_ID);
    public static final StreamCodec<FriendlyByteBuf, S2CInputPayload> CODEC = StreamCodec.of(
        (buf, payload) -> {
            buf.writeByte(payload.asciiCodes().size());
            for (int key : payload.asciiCodes()) {
                buf.writeByte(key);
            }
        },
        buf -> {
            byte size = buf.readByte();
            Set<Byte> keys = new HashSet<>(size);
            for (int i = 0; i < size; i++) {
                keys.add(buf.readByte());
            }
            return new S2CInputPayload(keys);
        }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
