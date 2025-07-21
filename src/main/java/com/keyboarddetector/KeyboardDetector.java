package com.keyboarddetector;

import com.keyboarddetector.util.KeyMatchingUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.keyboarddetector.network.S2CInputPayload.CODEC;
import static com.keyboarddetector.network.S2CInputPayload.ID;

public class KeyboardDetector implements ModInitializer {

    public static final String MOD_ID = "keyboarddetector";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // 存储玩家按键状态
    public static final Map<UUID, Set<Byte>> playerKeyStates = new HashMap<>();

    // 网络通信标识符
    public static final ResourceLocation KEY_PRESSED_PACKET_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "key_pressed");

    @Override
    public void onInitialize() {
        LOGGER.info("KeyboardDetector mod initialized!");

        // 注册网络接收器
        PayloadTypeRegistry.playC2S().register(ID, CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ID, (payload, context) -> playerKeyStates.put(context.player().getUUID(), payload.asciiCodes()));

        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommand(dispatcher));
    }

    private void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("keyboarddetector")
                .then(Commands.literal("matchgroup")
                    .requires(source -> source.hasPermission(2)) // OP权限
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("formattedAscii", StringArgumentType.greedyString())
                            .executes(this::executeMatchGroup))))
                .then(Commands.literal("iskeydown")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("keyAscii", IntegerArgumentType.integer(48, 90)) //可自行根据需求调整范围，客户端端也要跟着调
                            .then(Commands.argument("keepStatic", BoolArgumentType.bool())
                                .executes(this::executeIsKeyDown)))))
                .then(Commands.literal("flush")
                    .requires(source -> source.hasPermission(2)) // OP权限
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(this::executeFlush)))
        );
    }

    private int executeIsKeyDown(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Player targetPlayer = EntityArgument.getPlayer(context, "player");
        byte asciiKey = (byte) IntegerArgumentType.getInteger(context, "keyAscii");
        boolean isStatic = BoolArgumentType.getBool(context, "keepStatic");
        UUID uuid = targetPlayer.getUUID();
        // 获取玩家最后按下的ASCII码，如果没有则为-1
        if (!playerKeyStates.containsKey(uuid) || !playerKeyStates.get(uuid).contains(asciiKey)) {
            throw new SimpleCommandExceptionType(Component.literal("玩家按下的键是: " + playerKeyStates.get(uuid) + " , 不包含 " + asciiKey)).create();
        }
        source.sendSuccess(() -> Component.literal("按键匹配成功: " + asciiKey), true);
        if (!isStatic) {
            playerKeyStates.clear();
        }
        return 1; // 成功，返1（红石信号）
    }

    private int executeFlush(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Player targetPlayer = EntityArgument.getPlayer(context, "player");
        UUID uuid = targetPlayer.getUUID();
        if (!playerKeyStates.containsKey(uuid)) {
            throw new SimpleCommandExceptionType(Component.literal("无可清空目标")).create();
        }
        source.sendSuccess(() -> Component.literal("已清空目标 " + targetPlayer + " 的按键"), true);
        playerKeyStates.clear();
        return 1; // 成功，返1（红石信号）
    }

    private int executeMatchGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Player targetPlayer = EntityArgument.getPlayer(context, "player");
        String formattedAscii = StringArgumentType.getString(context, "formattedAscii");
        try {
            List<Byte> expectedKeys = Arrays.stream(formattedAscii.split(",")).map(Byte::parseByte).toList();
            UUID uuid = targetPlayer.getUUID();
            Set<Byte> inputKeys = playerKeyStates.getOrDefault(uuid, Collections.emptySet());
            var pair = KeyMatchingUtil.matchAndRemove(inputKeys, expectedKeys);
            if (pair.getFirst().isEmpty()) {
                source.sendSuccess(() -> Component.literal("按键匹配成功: " + expectedKeys), true);
                return 1; // 成功，返1（红石信号）
            }
            throw new SimpleCommandExceptionType(Component.literal("缺少的键: " + pair.getFirst() + " 多余的键  " + pair.getSecond())).create();
        } catch (NumberFormatException e) {
            throw new SimpleCommandExceptionType(Component.literal("匹配字符串 " + formattedAscii + " 包含非法的 Key ID 或格式")).create();
        }
    }
}