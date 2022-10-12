package cmsc420.sortedmap;

import java.util.AbstractMap;
import java.util.SortedMap;
import java.util.Random;

public class TreapNode<K,V> extends AbstractMap.SimpleEntry<K,V>
        implements SortedMap.Entry<K,V> {

    public K key;
    public V value;
    public int priority;
    public TreapNode<K,V> left = null;
    public TreapNode<K,V> right = null;
    public TreapNode<K,V> parent;

    public TreapNode(K key, V value, TreapNode<K,V> parent) {

        super(key, value);
        this.key = key;
        this.value = value;
        this.priority = new Random().nextInt() % 100;
        // this.priority = 1;
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
    public boolean equals(Object o) {

        if (this == null && o == null) {
            return true;
        }

        if (!(o instanceof SortedMap.Entry)) {
            return false;
        }
        SortedMap.Entry<K,V> node = (SortedMap.Entry<K,V>) o;
        if (key != null && node.getKey() != null) {
            if (value != null && node.getValue() != null) {
                return (key.equals(node.getKey()) && value.equals(node.getValue()));
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString(){
        return key + "=" + value;
    }
}



