package com.keyboarddetector.util;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyMatchingUtil {

    public static Pair<Collection<Byte>, Collection<Byte>> matchMissingAndExtra(Collection<Byte> input, Collection<Byte> expected) {
        List<Byte> missing = new ArrayList<>();
        List<Byte> extra = new ArrayList<>();
        for (Byte key : expected) {
            if (!input.contains(key)) {
                missing.add(key);
            }
        }
        for (Byte key : input) {
            if (!expected.contains(key)) {
                extra.add(key);
            }
        }
        return Pair.of(missing, extra);
    }

    public static Pair<Collection<Byte>, Collection<Byte>> matchAndRemove(Collection<Byte> input, Collection<Byte> expected) {
        var pair = matchMissingAndExtra(input, expected);
        for (Byte key : expected) {
            input.remove(key);
        }
        return pair;
    }
}
