package project20280.hashtable;

import project20280.interfaces.Entry;

import java.util.ArrayList;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
    // a fixed capacity array of UnsortedTableMap that serve as buckets
    private UnsortedTableMap<K, V>[] table; // initialized within createTable
    private int numCollisions = 0;

    /**
     * Creates a hash table with capacity 11 and prime factor 109345121.
     */
    public ChainHashMap() {
        super();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ChainHashMap(int cap) {
        super(cap);
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ChainHashMap(int cap, int p) {
        super(cap, p);
    }

    /**
     * Creates an empty table having length equal to current capacity.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    protected void createTable() {
        table = new UnsortedTableMap[capacity];
    }

    /**
     * Returns value associated with key k in bucket with hash value h. If no such
     * entry exists, returns null.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return associate value (or null, if no such entry)
     */
    @Override
    protected V bucketGet(int h, K k) {
        UnsortedTableMap<K,V> bucket = table[h];
        if(bucket == null)
        {
            return null;
        }else {
            return bucket.get(k);
        }
    }

    /**
     * Associates key k with value v in bucket with hash value h, returning the
     * previously associated value, if any.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @param v the value to be associated
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    /*protected V bucketPut(int h, K k, V v) {
        // TODO
        UnsortedTableMap<K,V> bucket = table[h];//get the bucket at index h, if no bucket create one
        if(bucket == null) {
            bucket=table[h] = new UnsortedTableMap<>();
        }
        int size = bucket.size();//get the old size
        V result = bucket.put(k, v);//insert key value into the bucket
        n += (bucket.size() - size);//update total number of elemenets
        return result;

    }*/
    protected V bucketPut(int h, K k, V v) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) {
            bucket = table[h] = new UnsortedTableMap<>();
        } else if (!bucket.isEmpty()) {
            numCollisions++; //increment collisions if there's already an item in the bucket
        }
        int oldSize = bucket.size();
        V answer = bucket.put(k, v);
        n += (bucket.size() - oldSize);
        return answer;
    }


    public boolean containsKey(K key) {
        int h = hashValue(key);
        UnsortedTableMap<K, V> bucket = table[h];
        return bucket != null && bucket.findIndex(key) != -1;
    }


    public int getCollisions() {
        return numCollisions;
    }

public double loadfactor()
{
    return (double)n / (double)capacity;
}


    public void clear() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                table[i].clear();  //clear each individual map
            }
        }
        n = 0;
    }


    public void putAll(ChainHashMap<String, Integer> map) {
        for (Entry<String, Integer> entry : map.entrySet()) {
            put((K)entry.getKey(), (V)entry.getValue());
        }
    }



    /**
     * Removes entry having key k from bucket with hash value h, returning the
     * previously associated value, if found.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return previous value associated with k (or null, if no such entry)
     */
    @Override
    protected V bucketRemove(int h, K k) {
        // TODO
        UnsortedTableMap<K,V> bucket = table[h];//get the bucket at index h, if no bucket return null
        if(bucket == null)
        {
            return null;
        }
        int size = bucket.size();//get the old size
        V result = bucket.remove(k);//try remove the entry with the key
        n -= (size = bucket.size());//if entry was removed update total
        return result;
    }

    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        /*
        for each element in (UnsortedTableMap []) table
            for each element in bucket:
                print element
        */
        ArrayList<Entry<K, V>> entries = new ArrayList<>();
        for (UnsortedTableMap<K, V> tm : table) {
            if (tm != null) {
                for (Entry<K, V> e : tm.entrySet()) {
                    entries.add(e);
                }
            }
        }
        return entries;
    }


    public String toString() {
        return entrySet().toString();
    }

    public static void main(String[] args) {
        ChainHashMap<Integer, String> m = new ChainHashMap<Integer, String>();
        m.put(1, "One");
        m.put(10, "Ten");
        m.put(11, "Eleven");
        m.put(20, "Twenty");

        System.out.println("m: " + m);

        m.remove(11);
        System.out.println("m: " + m);

    }


}
