package in.varadharajan.toysstable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Segment<K,V> {
    private List<Entity<K,V>> data;
    private Long createdAt;

    public Segment(List<Entity<K, V>> data) {
        this.data = data;
        this.createdAt = System.currentTimeMillis();
    }

    public Optional<V> get(K key) {
        return data.stream()
                .filter(entity -> entity.is(key))
                .map(entity -> entity.getValue())
                .findFirst();
    }

    public int compareTo(Segment<K, V> right) {
        return -1 * this.createdAt.compareTo(right.createdAt);
    }

    public Stream<Entity<K,V>> values() {
        return this.data.stream();
    }
}
