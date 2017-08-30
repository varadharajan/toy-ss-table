package in.varadharajan.toysstable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SSTable<K,V> implements SSTableInterface<K,V> {
    Comparator<Segment<K,V>> segmentComparator =
            (Segment<K,V> left, Segment<K,V> right) -> left.compareTo(right);

    private TreeSet<Segment<K,V>> segments = new TreeSet<Segment<K,V>>(segmentComparator);
    private StagingBuffer<K,V> stagingBuffer = new StagingBuffer<K, V>();
    private int max_entries;

    public SSTable(int max_entries) {
        this.max_entries = max_entries;
    }

    @Override
    public void put(K key, V value) {
        stagingBuffer.add(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> value = stagingBuffer.get(key);
        if(!value.isPresent())
        {
            value = searchSegments(key);
        }
        return value;
    }

    @Override
    public void compact() {
        generateSegmentFromStagingBuffer();
        compactSegments();
    }

    private static <T> Predicate<T> distinctByEntityKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> state = new ConcurrentHashMap<>();
        return t -> state.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void compactSegments() {
        Comparator<Entity<K,V>> entityComparator =
                (Entity<K,V> left, Entity<K,V> right) -> left.compareTo(right);

        List<Entity<K, V>> sortedList = segments.stream()
                .sequential()
                .flatMap(segment -> segment.values())
                .filter(distinctByEntityKey(entity -> entity.getValue()))
                .sorted(entityComparator).collect(toList());

        for(int i = 0 ; i < sortedList.size(); i+=max_entries) {
            int max = i+max_entries >= sortedList.size() ? sortedList.size() : i + max_entries;
            List<Entity<K, V>> entities = sortedList.subList(i, max);
            this.segments.add(new Segment<K, V>(entities));
        }
    }

    private void generateSegmentFromStagingBuffer() {
        Segment<K,V> segment = stagingBuffer.createSegment();
        segments.add(segment);
        stagingBuffer.purge();
    }

    private Optional<V> searchSegments(K key) {
        Optional<Optional<V>> v = segments.stream()
                .map(segment -> segment.get(key))
                .filter(Optional::isPresent)
                .findFirst();
        return v.isPresent() ? v.get() : Optional.empty();
    }
}
