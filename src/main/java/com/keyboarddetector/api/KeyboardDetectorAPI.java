package com.keyboarddetector.api;

import com.keyboarddetector.event.PlayerKeyMatchEvent;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;

/**
 * KeyboardDetector的公共API类，供其他插件调用
 */
public class KeyboardDetectorAPI {
    /**
     * 获取按键匹配事件，供其他插件注册监听器
     * @return 按键匹配事件实例
     */
    public static Event<PlayerKeyMatchEvent.Callback> getKeyMatchEvent() {
        return PlayerKeyMatchEvent.EVENT;
    }
    
    /**
     * 注册按键匹配监听器
     * @param listener 监听器回调函数
     */
    public static void registerKeyMatchListener(PlayerKeyMatchEvent.Callback listener) {
        PlayerKeyMatchEvent.EVENT.register(listener);
    }
    
    /**
     * 检查按键集合是否包含指定的值
     * @param pressedKeys 按下的按键集合
     * @param targetKey 要检查的目标按键
     * @return 如果包含目标按键则返回true
     */
    public static boolean containsKey(Collection<Byte> pressedKeys, byte targetKey) {
        if (pressedKeys == null || pressedKeys.isEmpty()) {
            return false;
        }
        return pressedKeys.contains(targetKey);
    }
    
    /**
     * 检查按键集合是否包含所有指定的值
     * @param pressedKeys 按下的按键集合
     * @param targetKeys 要检查的目标按键集合
     * @return 如果包含所有目标按键则返回true
     */
    public static boolean containsAllKeys(Collection<Byte> pressedKeys, Collection<Byte> targetKeys) {
        if (pressedKeys == null || targetKeys == null) {
            return false;
        }
        return pressedKeys.containsAll(targetKeys);
    }
}