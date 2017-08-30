# toy-ss-table
A toy implementation of SSTable for learning purposes. Please refer to implementations like LevelDB / bitcask / Cassandra for production usecases.

# Sample Usage

```java
final String SSTABLE_DIR = "/tmp/sstable";
SSTable sstable = new SSTable(SSTABLE_DIR);
sstable.put("key1", "value1");
sstable.put("key2", "value2");
sstable.compact();
System.out.println(sstable.get("key2"));
```
