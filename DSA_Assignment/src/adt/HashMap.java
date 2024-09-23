package adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMap<K extends Comparable<K>, V> implements HashMapInterface<K, V>, Iterable<HashMap.Entry<K, V>> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    // Entry class for key-value pairs
    public static class Entry<K, V> {

        K key;
        V value;
        Entry<K, V> next; // Pointer to the next entry in the linked list

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    // Each element in the table is the head of a linked list of entries
    private Entry<K, V>[] table;
    private int numberOfEntries;

    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new Entry[INITIAL_CAPACITY];
        numberOfEntries = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (isEmpty()) {
            int index = getHashIndex(key);
            table[index] = new Entry<>(key, value);
            numberOfEntries++;
            return;
        }

        int index = getHashIndex(key);

        // Traverse the linked list at the index
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value; // Update value if key already exists
                return;
            }
            current = current.next;
        }

        // If key doesn't exist, add a new entry at the beginning of the list
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        numberOfEntries++;

        if ((float) numberOfEntries / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V get(K key) {
        int index = getHashIndex(key);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public V remove(K key) {
        int index = getHashIndex(key);

        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        // Traverse the linked list at the index
        while (current != null) {
            if (current.key.equals(key)) {
                V value = current.value;

                if (prev == null) {
                    // If the entry to remove is the first in the list
                    table[index] = current.next;
                } else {
                    // Link the previous entry to the next, skipping current
                    prev.next = current.next;
                }

                numberOfEntries--;
                return value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getHashIndex(key);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        table = new Entry[INITIAL_CAPACITY];
        numberOfEntries = 0;
    }

    @Override
    public Object[] keySet() {
        Object[] keys = new Object[numberOfEntries];
        int index = 0;
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                keys[index++] = current.key;
                current = current.next;
            }
        }
        return keys;
    }

    @Override
    public Object[] values() {
        Object[] values = new Object[numberOfEntries];
        int index = 0;
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                values[index++] = current.value;
                current = current.next;
            }
        }
        return values;
    }

    @Override
    public Object[] entrySet() {
        Object[] entries = new Object[numberOfEntries];
        int index = 0;
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                entries[index++] = current;
                current = current.next;
            }
        }
        return entries;
    }

    private int getHashIndex(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        numberOfEntries = 0;

        for (Entry<K, V> entry : oldTable) {
            Entry<K, V> current = entry;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    @Override
    public Object[] sortByKey() {
        Object[] entries = entrySet(); // Get entries as Object[]
        int n = numberOfEntries;
        boolean swapped;

        do {
            swapped = false;

            for (int i = 0; i < n - 1; i++) {
                @SuppressWarnings("unchecked")
                Entry<K, V> entry1 = (Entry<K, V>) entries[i];
                @SuppressWarnings("unchecked")
                Entry<K, V> entry2 = (Entry<K, V>) entries[i + 1];

                if (entry1.getKey().compareTo(entry2.getKey()) > 0) {
                    // Swap entries[i] and entries[i + 1]
                    Object temp = entries[i];
                    entries[i] = entries[i + 1];
                    entries[i + 1] = temp;
                    swapped = true;
                }
            }
            //After put the largest at the back of the array and reduce the range
            n--; // Reduce the range for the next pass
        } while (swapped);

        return entries;
    }

    // Implementing Iterable interface
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashMapIterator();
    }

    // Internal iterator class
    private class HashMapIterator implements Iterator<Entry<K, V>> {

        private int currentIndex;
        private Entry<K, V> currentEntry;

        HashMapIterator() {
            currentIndex = 0;
            currentEntry = null;
        }

        @Override
        public boolean hasNext() {
            // Check if the current entry is not null and has a next entry
            if (currentEntry != null && currentEntry.next != null) {
                return true;
            }

            // Check the remaining buckets
            while (currentIndex < table.length) {
                if (table[currentIndex] != null) {
                    return true;
                }
                currentIndex++;
            }

            return false;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Move to the next entry in the list
            if (currentEntry == null || currentEntry.next == null) {
                currentEntry = table[currentIndex++];
            } else {
                currentEntry = currentEntry.next;
            }

            return currentEntry;
        }
    }
}
