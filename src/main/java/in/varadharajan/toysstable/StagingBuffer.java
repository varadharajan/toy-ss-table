package in.varadharajan.toysstable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

public class StagingBuffer<K,V> {
    private List<Entity<K, V>> buffer = new ArrayList<Entity<K, V>>();

    public Entity<K,V> add(K key, V value) {
        Entity<K,V> entity = new Entity<K, V>(key, value);
        buffer.add(entity);
        return entity;
    }

    public Optional<V> get(K key) {
        return buffer.stream()
                .filter(entity -> entity.is(key))
                .map(entity -> entity.getValue())
                .findFirst();
    }

    public Segment<K,V> createSegment() {
        return new Segment<K, V>(buffer);
    }

    public void purge() {
        this.buffer = new ArrayList<Entity<K,V>>();
    }
}
