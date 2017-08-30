package in.varadharajan.toysstable;

import java.util.Optional;

public interface SSTableInterface<K,V> {
    void put(K key, V value);
    Optional<V> get(K key);
    void compact();
}
