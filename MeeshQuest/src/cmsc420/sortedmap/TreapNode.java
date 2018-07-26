package cmsc420.sortedmap;

import java.util.SortedMap;
import java.util.Random;

public class TreapNode<K,V> implements SortedMap.Entry<K,V> {

    public K key;
    public V value;
    public int priority;
    public TreapNode<K,V> left = null;
    public TreapNode<K,V> right = null;
    public TreapNode<K,V> parent;

    public TreapNode(K key, V value, TreapNode<K,V> parent) {

        this.key = key;
        this.priority = new Random().nextInt() % 100;
        this.value = value;
        this.parent = parent;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreapNode)) {
            return false;
        }
        TreapNode<K,V> node = (TreapNode) obj;
        return (node.getKey().equals(key)
                && node.getValue().equals(value));
    }

    @Override
    public int hashCode() {
        int keyHash = (key == null ? 0: key.hashCode());
        int valueHash = (value == null ? 0 : value.hashCode());
        return keyHash ^ valueHash;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "key=" + key +
                ", value=" + value +
                ", left=" + left +
                ", right=" + right +
                ", parent=" + parent +
                ", priority=" + priority +
                '}';
    }
}



