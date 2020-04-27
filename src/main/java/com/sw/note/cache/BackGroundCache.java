package com.sw.note.cache;

import com.google.common.collect.Maps;
import com.sw.note.model.BackgroundData;

import java.util.Map;

public class BackGroundCache {
    private static Map<String, BackgroundData> backgroundDataMap = Maps.newConcurrentMap();

    public static Map<String, BackgroundData> all() {
        return backgroundDataMap;
    }

    public static BackgroundData find(String url) {
        return backgroundDataMap.get(url);
    }

    public static void set(String url, BackgroundData backgroundData) {
        backgroundDataMap.put(url, backgroundData);
    }

    public static void clear() {
        long nowMills = System.currentTimeMillis();
        backgroundDataMap.forEach((key, value) -> {
            if (nowMills - value.getMills() > 60 * 60 * 1000) {
                backgroundDataMap.remove(key);
            }
        });
    }

    public static void empty() {
        backgroundDataMap.clear();
    }

}
