import java.util.LinkedList;

public class HashMap<K, V> {

    private LinkedList<Node<K, V>>[] table;
    private int capacity;
    private int size = 0;
    private final int DEFAULT_CAPACITY = 16;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        table = new LinkedList[capacity];
        this.capacity = capacity;
    }

    int getHash(Object key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    int getIndex(int hash) {
        return hash & (capacity - 1);
    }

    public void put(K key, V value) {
        int hash = getHash(key);
        int index = getIndex(hash);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (Node<K, V> node : table[index]) {
            if (node.getHash() == hash && (node.getKey() == key || (key != null && key.equals(node.getKey())))) {
                node.setValue(value);
                return;
            }
        }

        table[index].add(new Node<>(hash, key, value));
        size++;
        resize();
    }

    public V get(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);

        if (table[index] == null) {
            return null;
        }

        for (Node<K, V> node : table[index]) {
            if (node.getHash() == hash && (node.getKey() == key || (key != null && key.equals(node.getKey())))) {
                return node.getValue();
            }
        }
        return null;
    }

    public void remove(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);

        if (table[index] == null) {
            return;
        }

        boolean removed = table[index].removeIf(node -> node.getHash() == hash && (node.getKey() == key || (key != null && key.equals(node.getKey()))));

        if (removed) {
            size--;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if ((float) size / capacity >= DEFAULT_LOAD_FACTOR) {

            int oldCapacity = capacity;
            capacity = capacity * 2;
            LinkedList<Node<K, V>>[] oldTable = table;
            table = new LinkedList[capacity];
            size = 0;

            for (int i = 0; i < oldCapacity; i++) {
                LinkedList<Node<K, V>> bucket = oldTable[i];
                if (bucket != null) {
                    for (Node<K, V> node : bucket) {
                        put(node.getKey(), node.getValue());
                    }
                }
            }
        }
    }

}
