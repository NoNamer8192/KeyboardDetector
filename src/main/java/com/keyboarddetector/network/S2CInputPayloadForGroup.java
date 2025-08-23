package com.keyboarddetector.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.keyboarddetector.KeyboardDetector.KEY_GROUP_PACKET_ID;

public record S2CInputPayloadForGroup(Set<Byte> asciiCodesGroup) implements CustomPacketPayload {

    public static final Type<S2CInputPayloadForGroup> ID_GROUP = new Type<>(KEY_GROUP_PACKET_ID);
    public static final StreamCodec<FriendlyByteBuf, S2CInputPayloadForGroup> CODEC_GROUP = StreamCodec.of(
        (buf, payloadForGroup) -> {
            buf.writeByte(payloadForGroup.asciiCodesGroup().size());
            for (int key : payloadForGroup.asciiCodesGroup()) {
                buf.writeByte(key);
            }
        },
        buf -> {
            byte size = buf.readByte();
            Set<Byte> keys = new HashSet<>(size);
            for (int i = 0; i < size; i++) {
                keys.add(buf.readByte());
            }
            return new S2CInputPayloadForGroup(keys);
        }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID_GROUP;
    }
}
