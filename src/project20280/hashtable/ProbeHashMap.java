package project20280.hashtable;

import project20280.interfaces.Entry;

import java.util.ArrayList;

public class ProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private final MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);

    public ProbeHashMap() {
        super();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ProbeHashMap(int cap) {
        super(cap);
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ProbeHashMap(int cap, int p) {
        super(cap, p);
    }

    @Override
    protected void createTable() {
        table = new MapEntry[capacity];
    }

    private boolean isAvailable(int j) {
        return (table[j] == null || table[j] == DEFUNCT);
    }
    private int findSlot(int h, K k) {
        int available = -1; //initialize with no available slot found yet


        for (int i = 0; i < table.length; i++) {
            int j = (h + i) % capacity; //calculate index cyclically based on i

            //check if the current slot is available
            if (isAvailable(j)) {
                //mark the first found available slot
                if (available == -1) available = j;
                //gf the current slot is empty stop the search
                if (table[j] == null) break;
            } else if (table[j] != null && table[j].getKey().equals(k)) {
                //if the current slotss key matches the searched key return the slot index
                return j;
            }
        }

        // if the key was not found return the negated index of the first available slot minus 1
        return -(available + 1);
    }

    @Override
    protected V bucketGet(int h, K k) {
        // TODO
        int i = findSlot(h,k);
        if(i < 0) {
            return null;
        }

        return table[i].getValue();
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        // TODO
        int i = findSlot(h, k);

            if(i >= 0) {
                return table[i].setValue(v);
            }
                table[-(i+1)] = new MapEntry<>(k, v);
                n++;



        return null;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        // TODO
        int i = findSlot(h,k);
        if(i < 0) {
            return null;
        }
        V result = table[i].getValue();
        table[i] = DEFUNCT;
        n--;
        return result;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K,V>> buffer = new ArrayList<>( );
        for (int i=0; i < capacity; i++) {
            if (!isAvailable(i)){
                buffer.add(table[i]);
            }
        }
        return buffer;
    }
}
