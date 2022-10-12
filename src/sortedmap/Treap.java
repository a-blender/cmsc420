package cmsc420.sortedmap;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Treap implementation, lots of code taken from E. Wang's AVL-g tree implementation.
 */
public class Treap<K, V> extends AbstractMap<K, V> implements
        SortedMap<K, V> {
   
    private Comparator<? super K> comparator = null;
    private TreapNode<K, V> root = null;
    private long size = 0;
    private int modCount = 0;
    private EntrySet entrySet = null;
    private KeySet keySet = null;
    private Values values = null;
    private static Random rand = new Random();

    public Treap() {
    }

    public Treap(final Comparator<? super K> comp) {
        this.comparator = comp;
    }

    public Comparator<? super K> comparator() {
        return comparator;
    }

    public void clear() {
        modCount++;
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        if (size > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else
            return (int) size;
    }

    public boolean containsKey(Object key) {
        if (key == null)
            throw new NullPointerException();
        return getNode(key) != null;
    }

    public boolean containsValue(Object value) {
        if (value == null)
            throw new NullPointerException();
        return nodeContainsValue(root, value);
    }

    public V get(Object key) {
        if (key == null)
            throw new NullPointerException();

        TreapNode<K, V> p = getNode(key);
        return (p == null ? null : p.value);
    }

    public V put(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();

        TreapNode<K, V> t = root;
        if (t == null) {
            root = new TreapNode<K, V>(key, value, comparator);
            size = 1;
            modCount++;
            return null;
        }
        TreapNode<K, V> e = new TreapNode<K, V>(key, value, comparator);
        V oldValue = root.add(e);

        modCount++;
        if (oldValue == null) {
            fixAfterModification(e);
            size++;
            return null;
        } else {
            return oldValue;
        }
    }

    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public K firstKey() {
        return key(getFirstNode());
    }

    public K lastKey() {
        return key(getLastNode());
    }

    public Set<Entry<K, V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet());
    }

    public Set<K> keySet() {
    	KeySet ks = keySet;
    	return (ks != null) ? ks : (keySet = new KeySet());
    }

    public Collection<V> values() {
    	Collection<V> vs = values;
        return (vs != null) ? vs : (values = new Values());
    }

    public SortedMap<K, V> headMap(K toKey) {
        return new SubMap<K, V>(this, null, toKey);
    }

    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return new SubMap<K, V>(this, fromKey, toKey);
    }

    public SortedMap<K, V> tailMap(K fromKey) {
    	return new SubMap<K, V>(this, fromKey, null);
    }

    static final class TreapNode<K, V> implements Entry<K, V> {
        private K key;
        private V value;
        private int priority;
        public TreapNode<K, V> left = null;
        public TreapNode<K, V> right = null;
        public TreapNode<K, V> parent = null;
        Comparator<? super K> comparator;
        
        TreapNode(K key, V value, Comparator<? super K> comp) {
            this.key = key;
            this.value = value;
            this.parent = null;
            this.comparator = comp;
            priority = rand.nextInt();
        }

        public V add(TreapNode<K, V> node) {
            int cmp = compare(node.key, this.key);
            if (cmp < 0) {
                if (left == null) {
                    left = node;
                    left.parent = this;
                    return null;
                } else {
                    V ret = this.left.add(node);
                    return ret;
                }
            } else if (cmp > 0) {
                if (right == null) {
                    right = node;
                    right.parent = this;
                    return null;
                } else {
                    V ret = this.right.add(node);
                    return ret;
                }
            } else {
                return this.setValue(node.value);
            }
        }

        public int hashCode() {
            int keyHash = (key == null ? 0 : key.hashCode());
            int valueHash = (value == null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Entry<?, ?> e = (Entry<?, ?>) o;

            return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
        }

        public String toString() {
            return key + "=" + value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @SuppressWarnings({ "unchecked" })
        private int compare(Object k1, Object k2) {
            return comparator == null ? ((Comparable<? super K>) k1)
                    .compareTo((K) k2) : comparator.compare((K) k1, (K) k2);
        }

        public Node buildXmlNode(final Node parent) {
            final Element e = parent.getOwnerDocument().createElement("node");
            e.setAttribute("key", key.toString());
            e.setAttribute("value", value.toString());
            e.setAttribute("priority", Integer.toString(priority));

            if (left != null) {
                e.appendChild(left.buildXmlNode(e));
            } else {
                e.appendChild(e.getOwnerDocument().createElement("emptyChild"));
            }

            if (right != null) {
                e.appendChild(right.buildXmlNode(e));
            } else {
                e.appendChild(e.getOwnerDocument().createElement("emptyChild"));
            }
            return e;
        }
        
    }

    private final TreapNode<K, V> getNode(Object key) {
        TreapNode<K, V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    private final boolean nodeContainsValue(TreapNode<K, V> node, Object value) {
        if (node == null)
            return false;

        if (node.value.equals(value))
            return true;
        else
            return nodeContainsValue(node.left, value)
                    || nodeContainsValue(node.right, value);
    }

    private final TreapNode<K, V> getFirstNode() {
        TreapNode<K, V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    private final TreapNode<K, V> getLastNode() {
        TreapNode<K, V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }

    private final NodeIterator getNodeIterator() {
        return new NodeIterator(getFirstNode());
    }

    private final ReverseNodeIterator getReverseNodeIterator() {
        return new ReverseNodeIterator(getLastNode());
    }

    private static <K, V> TreapNode<K, V> successor(TreapNode<K, V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreapNode<K, V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreapNode<K, V> p = t.parent;
            TreapNode<K, V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private static <K, V> TreapNode<K, V> predecessor(TreapNode<K, V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            TreapNode<K, V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            TreapNode<K, V> p = t.parent;
            TreapNode<K, V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    private void fixAfterModification(TreapNode<K, V> e) {
    	if (e.left != null && e.left.priority > e.priority) {
    		e = rotateRight(e);
    	}
    	if (e.right != null && e.right.priority > e.priority) {
    		e = rotateLeft(e);
    	}

        if (e.parent != null)
            fixAfterModification(e.parent);
        else
            this.root = e;
    }

    private TreapNode<K, V> rotateRight(TreapNode<K, V> p) {
        if (p == null)
            return null;

        TreapNode<K, V> l = p.left;
        p.left = l.right;
        if (l.right != null)
            l.right.parent = p;
        l.parent = p.parent;
        if (p.parent != null) {
            if (p.parent.right == p)
                p.parent.right = l;
            else
                p.parent.left = l;
        }
        l.right = p;
        p.parent = l;

        return l;
    }

    private TreapNode<K, V> rotateLeft(TreapNode<K, V> p) {
        if (p == null)
            return null;

        TreapNode<K, V> r = p.right;
        p.right = r.left;
        if (r.left != null)
            r.left.parent = p;
        r.parent = p.parent;
        if (p.parent != null) {
            if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
        }
        r.left = p;
        p.parent = r;
        return r;
    }

    private static <K> K key(Entry<K, ?> e) {
        if (e == null)
            throw new NoSuchElementException();
        return e.getKey();
    }

    @SuppressWarnings("unchecked")
    private final int compare(Object k1, Object k2) {
        return comparator == null ? ((Comparable<? super K>) k1)
                .compareTo((K) k2) : comparator.compare((K) k1, (K) k2);
    }

    class EntrySet extends AbstractSet<Entry<K, V>> {
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator(getFirstNode());
        }

        public boolean add(Entry<K, V> o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Entry<K, V>> c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            Treap.this.clear();
        }

        public int size() {
            return Treap.this.size();
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            @SuppressWarnings("unchecked")
            Entry<K, V> entry = (Entry<K, V>) o;
            V value = entry.getValue();
            TreapNode<K, V> p = getNode(entry.getKey());
            return p != null && valEquals(p.getValue(), value);
        }

        public boolean equals(final Object other) {
            if (other == null)
                return false;
            int i = ((Collection<?>) other).size(), j = size();
            return ((Collection<?>) other).containsAll(this) && i == j;
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }
    }

    class KeySet extends AbstractSet<K> {
        public Iterator<K> iterator() {
            return new KeyIterator(getFirstNode());
        }

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            return Treap.this.containsKey(o);
        }

        public void clear() {
            Treap.this.clear();
        }

        public int size() {
            return Treap.this.size();
        }

        public boolean equals(final Object other) {
            if (other == null)
                return false;
            int i = ((Collection<?>) other).size(), j = size();
            return ((Collection<?>) other).containsAll(this) && i == j;
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }
    }

    class Values extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return new ValueIterator(getFirstNode());
        }

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Object o) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            Treap.this.clear();
        }

        public int size() {
            return Treap.this.size();
        }

        public boolean contains(Object o) {
            return Treap.this.containsValue(o);
        }

        public boolean equals(final Object other) {
            if (other == null)
                return false;
            int i = ((Collection<?>) other).size(), j = size();
            return ((Collection<?>) other).containsAll(this) && i == j;
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }
    }

    abstract class PrivateNodeIterator<T> implements Iterator<T> {
        TreapNode<K, V> next;
        TreapNode<K, V> lastReturned;
        int expectedModCount;

        public PrivateNodeIterator(TreapNode<K, V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }

        public final boolean hasNext() {
            return next != null;
        }

        final TreapNode<K, V> nextNode() {
            TreapNode<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            next = successor(e);
            lastReturned = e;
            return e;
        }

        final TreapNode<K, V> prevNode() {
            TreapNode<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            next = predecessor(e);
            lastReturned = e;
            return e;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    final class NodeIterator extends PrivateNodeIterator<TreapNode<K, V>> {
        NodeIterator(TreapNode<K, V> first) {
            super(first);
        }

        public TreapNode<K, V> next() {
            return nextNode();
        }
    }

    final class ReverseNodeIterator extends PrivateNodeIterator<TreapNode<K, V>> {
        ReverseNodeIterator(TreapNode<K, V> last) {
            super(last);
        }

        public TreapNode<K, V> next() {
            return prevNode();
        }
    }

    final class EntryIterator extends PrivateNodeIterator<Entry<K, V>> {
        EntryIterator(TreapNode<K, V> first) {
            super(first);
        }

        public Entry<K, V> next() {
            return nextNode();
        }
    }

    final class KeyIterator extends PrivateNodeIterator<K> {
        KeyIterator(TreapNode<K, V> first) {
            super(first);
        }

        public K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends PrivateNodeIterator<V> {
        ValueIterator(TreapNode<K, V> first) {
            super(first);
        }

        public V next() {
            return nextNode().value;
        }
    }

    private final static boolean valEquals(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
    }

    @SuppressWarnings("hiding")
    final class SubMap<K, V> extends AbstractMap<K, V> implements
            SortedMap<K, V> {
        final Treap<K, V> m;
        final K low;
        final K high;
        EntrySetView entrySetView = null;

        SubMap(Treap<K, V> m, K low, K high) {
            if (low == null && high == null)
                throw new IllegalArgumentException();

            if (low != null && high != null)
                if (m.compare(low, high) > 0)
                    throw new IllegalArgumentException();

            this.m = m;
            this.low = low;
            this.high = high;
        }

        public Comparator<? super K> comparator() {
            return m.comparator();
        }

        public final V put(K key, V value) {
            if (!inRange(key))
                throw new IllegalArgumentException("key out of range");
            return m.put(key, value);
        }

        public final V remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public K firstKey() {
            return key(getFirstNode());
        }

        TreapNode<K, V> getFirstNode() {
            if (low == null) {
                TreapNode<K, V> first = m.getFirstNode();
                if (compare(first.getKey(), high) < 0)
                    return first;
                else
                    return null;
            } else {
                Iterator<TreapNode<K, V>> i = m.getNodeIterator();
                TreapNode<K, V> e;
                while (i.hasNext()) {
                    e = i.next();
                    int cmp = m.compare(e.getKey(), low);
                    if (cmp >= 0)
                        return e;
                }
                return null;
            }
        }

        public K lastKey() {
            return key(getLastNode());
        }

        final Entry<K, V> getLastNode() {
            if (high == null) {
                TreapNode<K, V> last = m.getLastNode();
                if (compare(last.getKey(), low) >= 0)
                    return last;
                else
                    return null;
            } else {
                Iterator<TreapNode<K, V>> i = m.getReverseNodeIterator();
                Entry<K, V> e;
                while (i.hasNext()) {
                    e = i.next();
                    int cmp = m.compare(e.getKey(), high);
                    if (cmp < 0)
                        return e;
                }
                return null;
            }
        }

        public Set<Entry<K, V>> entrySet() {
            EntrySetView esv = entrySetView;
            return (esv != null) ? esv : (entrySetView = new EntrySetView());
        }

        public SortedMap<K, V> headMap(K toKey) {
            if (!inRange(toKey))
                throw new IllegalArgumentException();

            return new SubMap<K, V>(m, low, toKey);
        }

        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            if (!inRange(fromKey) || !inRange(toKey))
                throw new IllegalArgumentException();

            return new SubMap<K, V>(m, fromKey, toKey);
        }

        public SortedMap<K, V> tailMap(K fromKey) {
            if (!inRange(fromKey))
                throw new IllegalArgumentException();

            return new SubMap<K, V>(m, fromKey, high);
        }

        final boolean tooLow(Object key) {
            if (low != null) {
                int c = m.compare(key, low);
                if (c < 0)
                    return true;
            }
            return false;
        }

        final boolean tooHigh(Object key) {
            if (high != null) {
                int c = m.compare(key, high);
                if (c >= 0)
                    return true;
            }
            return false;
        }

        final boolean inRange(Object key) {
            return !tooLow(key) && !tooHigh(key);
        }

        public boolean equals(final Object other) {
            if (other == this)
                return true;
            else if (other instanceof SubMap) {
                @SuppressWarnings("unchecked")
                SubMap<?, ?> otherMap = (SubMap<?, ?>) other;
                return otherMap.m.equals(m) && low == null
                        ^ low.equals(otherMap.low) && high == null
                        ^ high.equals(otherMap.low);
            } else if (other instanceof Map) {
                Map<?, ?> otherMap = (Map<?, ?>) other;
                return entrySet().containsAll(otherMap.entrySet())
                        && otherMap.size() == size();
            } else
                return false;
        }

        class EntrySetView extends AbstractSet<Entry<K, V>> {
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {
                    int expectedModCount = m.modCount;
                    TreapNode<K, V> next = getFirstNode();
                    @SuppressWarnings("unused")
					TreapNode<K, V> lastReturned = null;

                    public boolean hasNext() {
                        if (next != null)
                            return inRange(next.key);
                        else
                            return false;
                    }

                    public Entry<K, V> next() {
                        TreapNode<K, V> e = next;
                        if (e == null)
                            throw new NoSuchElementException();
                        if (m.modCount != expectedModCount)
                            throw new ConcurrentModificationException();

                        next = successor(e);
                        if (next != null && !inRange(next.key))
                            next = null;

                        lastReturned = e;
                        return e;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public int size() {
                int size = 0;
                Iterator<Entry<K, V>> i = iterator();
                while (i.hasNext()) {
                    size++;
                    i.next();
                }
                return size;
            }

            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }
        }
    }

    public void createXml(final Node parent) {
        final Element rootNode = parent.getOwnerDocument().createElement(
                "treap");
        rootNode.setAttribute("cardinality", String.valueOf(size()));
        rootNode.appendChild(root == null ? parent.getOwnerDocument()
                .createElement("emptyChild") : root.buildXmlNode(rootNode));
        parent.appendChild(rootNode);
    }
}
