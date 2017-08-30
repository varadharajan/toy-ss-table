package in.varadharajan.toysstable;

public class Entity<K,V> {
    private K key;
    private V value;
    private Long createdAt;

    public Entity(K key, V value) {
        this.key = key;
        this.value = value;
        createdAt = System.currentTimeMillis();
    }

    public boolean is(K key) {
        return this.key.equals(key);
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    public int compareTo(Entity<K, V> right) {
        return -1 * this.createdAt.compareTo(right.createdAt);
    }
}
