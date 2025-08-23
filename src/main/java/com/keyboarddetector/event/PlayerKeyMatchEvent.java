package com.keyboarddetector.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import java.util.Collection;

/**
 * 当玩家按下的按键匹配指定值时触发的事件
 */
public class PlayerKeyMatchEvent {
    // 创建事件实例，使其他插件可以监听
    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(Callback.class,
            (listeners) -> (player, matchedKeys) -> {
                for (Callback listener : listeners) {
                    listener.onPlayerKeyMatch(player, matchedKeys);
                }
            });

    /**
     * 回调接口，用于处理事件
     */
    @FunctionalInterface
    public interface Callback {
        /**
         * 当玩家按键匹配成功时调用
         * @param player 触发事件的玩家
         * @param matchedKeys 匹配成功的按键集合
         */
        void onPlayerKeyMatch(ServerPlayer player, Collection<Byte> matchedKeys);
    }
}