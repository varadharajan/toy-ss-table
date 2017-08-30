package in.varadharajan.toysstable;

import static org.junit.Assert.*;
import org.junit.Test;

public class SSTableTest {
    @Test
    public void testSSTable() {
        final int MAX_ENTRIES_PER_SSTABLE = 100;
        SSTable<String, String> sstable = new SSTable<String, String>(MAX_ENTRIES_PER_SSTABLE);
        sstable.put("key1", "value1");
        sstable.put("key2", "value2");
        sstable.compact();
        assertEquals("value2", sstable.get("key2").get());
        assertFalse(sstable.get("foobar").isPresent());
        sstable.put("key2", "foobar");
        sstable.compact();
        assertEquals("foobar", sstable.get("key2").get());
    }
}