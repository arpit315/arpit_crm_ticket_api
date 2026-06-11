package com.arpit.crm_ticketing_api.cache;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LruCache<K, V> {

    private static final int MAX_SIZE = 100;

    private final Map<K, V> cache =
            new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(
                        Map.Entry<K, V> eldest
                ) {
                    return size() > MAX_SIZE;
                }
            };

    public synchronized V get(K key) {
        return cache.get(key);
    }
    public synchronized void remove(K key) {
        cache.remove(key);
    }

    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}