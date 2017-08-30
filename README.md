# toy-ss-table
A toy implementation of SSTable for learning purposes. 
Please refer to implementations like LevelDB / bitcask / Cassandra for production usecases.

# Sample Usage

```java
final int MAX_ENTRIES_PER_SSTABLE = 100;
SSTable<String, String> sstable = new SSTable<String, String>(MAX_ENTRIES_PER_SSTABLE);
sstable.put("key1", "value1");
sstable.put("key2", "value2");
sstable.compact();
System.out.println(sstable.get("key2"));
```

# Implementation
This project is to understand and experiment with the data structures that powers LSM / SSTables based DB engines.
This project is purely an in-memory architecture with no persistence as of now making it a pointless project.

An SSTable contains a list of segments along with its metadata, and a staging area. Data inserted using `sstable.put()` method is stored temporarily on staging area.
On invocation of `sstable.compact()`, the elements in staging area are sorted based on the key and transformed into a segment. The segments themselves are stored and scanned based on reverse chronological order.
Each segment also contains an index for better projection mechanisms.
