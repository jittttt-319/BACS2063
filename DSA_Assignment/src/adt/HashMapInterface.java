package adt;

import java.util.Iterator;

public interface HashMapInterface<K, V> {

    void put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    int size();

    boolean isEmpty();

    void clear();

    Object[] keySet();

    Object[] values();

    Object[] entrySet();

    Object[] sortByKey();

    // Add this method to provide an iterator over the entries
    Iterator<HashMap.Entry<K, V>> iterator();
}
