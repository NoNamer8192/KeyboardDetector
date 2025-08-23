package com.keyboarddetector.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.keyboarddetector.KeyboardDetector.*;

public record S2CInputPayloadForKeyTap(Set<Byte> asciiCodesKeyTap) implements CustomPacketPayload {

    public static final Type<S2CInputPayloadForKeyTap> ID_KEYTAP = new Type<>(KEY_TAPPED_PACKET_ID);
    public static final StreamCodec<FriendlyByteBuf, S2CInputPayloadForKeyTap> CODEC_KEYTAP = StreamCodec.of(
        (buf, payloadKeyTap) -> {
            buf.writeByte(payloadKeyTap.asciiCodesKeyTap().size());
            for (int key : payloadKeyTap.asciiCodesKeyTap()) {
                buf.writeByte(key);
            }
        },
        buf -> {
            byte size = buf.readByte();
            Set<Byte> keys = new HashSet<>(size);
            for (int i = 0; i < size; i++) {
                keys.add(buf.readByte());
            }
            return new S2CInputPayloadForKeyTap(keys);
        }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID_KEYTAP;
    }
}
