package cmsc420.sortedmap;

import java.util.*;
import java.lang.UnsupportedOperationException;

/**
 * Treap structure (Map/SortedMap interfaces)
 *
 * Map Interface
 * Implements: clear, containsKey, containsValue, equals, get, hashCode, isEmpty, put, putAll, size
 * Doesn't implement: remove, keySet, values
 *
 * Sorted Map Interface
 * Implements: comparator, subMap, firstKey, lastKey, entrySet
 * Doesn't implement: headMap, tailMap
 *
 */
public class Treap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {

    private TreapNode<K,V> root;                 // treap root node
    private int size;                       // number of nodes in the treap
    private int modCount;                   // number of operations performed
    private Comparator<K> comparator;       // generic comparator

    // CONSTRUCTORS

    public Treap() {
        this.root = null;
        this.size = 0;
        this.modCount = 0;
        comparator = null;
    }

    public Treap(Comparator<K> comparator) {
        this.root = null;
        this.size = 0;
        this.modCount = 0;
        this.comparator = comparator;
    }

    // GETTER METHODS

    /**
     * Returns the root node of the Treap
     * @return
     */
    public TreapNode<K, V> getRoot() {
        return root;
    }

    /**
     * Returns the number of operations already made on the Treap
     * @return
     */
    public int getModCount() {
        return modCount;
    }

    // IMPLEMENTED METHODS (Map Interface)

    /**
     * Checks if the Treap contains a node with the given key
     *
     * @param key
     * @return
     */
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    /**
     * Checks if the Treap contains a node with the given value
     * @param value
     * @return
     */
    public boolean containsValue(Object value) {

        // code taken from treemap.java
        for (TreapNode<K,V> node = getFirstEntry(); node != null; node = successor(node)) {
            if (value.equals(node.value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets value from the Treap node with the given key
     * Returns null if the key doesn't exist
     * @param key
     * @return
     */
    @Override
    public V get(Object key) {

        if (key == null) {
            throw new NullPointerException();
        }
        if (comparator != null) {
            return getUsingComparator(key);
        }
        TreapNode<K,V> node = root;
        Comparable<? super K> k = (Comparable<? super K>) key;

        while (node != null) {
            int result = k.compareTo(node.key);
            if (result < 0) {
                node = node.left;
            } else if (result > 0) {
                node = node.right;
            } else return node.value;
        }
        return null;
    }

    public V getUsingComparator(Object key) {

        TreapNode<K,V> node = root;
        K k = (K) key;
        while (node != null) {
            int result = comparator.compare(k, node.key);
            if (result < 0) {
                node = node.left;
            } else if (result > 0) {
                node = node.right;
            } else return node.value;
        }
        return null;

    }

    /**
     * Checks whether the Treap is empty or not
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Inserts a node with a key and value into the Treap
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {

        // my Treap does not accept null node values
        if (key == null | value == null) {
            throw new NullPointerException();
        }
        if (comparator == null) {
            root = insert(root, key, value);
        }
        else {
            insertUsingComparator(root, key, value);
        }
        size++;
        modCount++;
        return null;
    }

    /**
     * Recursive implementation of insertion in Treap
     * Taken from geeksforgeeks.com
     */
    public TreapNode<K,V> insert(TreapNode<K, V> root, K key, V value)
    {
        // If root is NULL, create a new node and return it
        if (root == null){
            return new TreapNode(key, value, null);
        }

        Comparable<? super K> k = (Comparable<? super K>) key;
        int greaterThanRoot = k.compareTo(root.key);

        // If key is smaller than root (reverse ascii)
        if (greaterThanRoot > 0)
        {
            // Insert in left subtree
            root.left = insert(root.left, key, value);

            // Fix Max Heap property if it is violated
            if (root.left.priority > root.priority) {
                root = rightRotate(root);
            }
        }
        else  // If key is greater (reverse ascii)
        {
            // Insert in right subtree
            root.right = insert(root.right, key, value);

            // Fix Max Heap property if it is violated
            if (root.right.priority > root.priority) {
                root = leftRotate(root);
            }
        }
        return root;
    }

    /**
     * Recursive implementation of insertion in Treap
     * Taken from geeksforgeeks.com
     */
    public TreapNode<K,V> insertUsingComparator(TreapNode<K, V> root, K key, V value)
    {
        // If root is NULL, create a new node and return it
        if (root == null){
            return new TreapNode(key, value, null);
        }
        int greaterThanRoot = comparator.compare(key, root.key);

        // If key is smaller than root (reverse ascii)
        if (greaterThanRoot > 0)
        {
            // Insert in left subtree
            root.left = insert(root.left, key, value);

            // Fix Max Heap property if it is violated
            if (root.left.priority > root.priority) {
                root = rightRotate(root);
            }
        }
        else  // If key is greater (reverse ascii)
        {
            // Insert in right subtree
            root.right = insert(root.right, key, value);

            // Fix Max Heap property if it is violated
            if (root.right.priority > root.priority) {
                root = leftRotate(root);
            }
        }
        return root;
    }



    // TO DO: do putAll() after you finish entrySet()
    //**********************************************************
    /*
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    } */
    //**********************************************************

    /**
     * Returns the number of nodes in the Treap
     *
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    // IMPLEMENTED METHODS (SortedMap Interface)

    /**
     * Returns the Treap's comparator or null if natural ordering
     *
     * @return
     */
    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    /**
     * Creates an EntrySet instance, which backs the Treap
     * @return
     */
    @Override
    public Set<SortedMap.Entry<K,V>> entrySet() {
        return new EntrySet<K,V>(this, size);
    }

    /**
     * Returns the first key inserted into the Treap
     * @return
     */
    @Override
    public K firstKey() {
        return getFirstEntry().key;
    }

    /**
     * Returns the last key inserted into the Treap
     *
     * @return
     */
    @Override
    public K lastKey() {
        return getLastEntry().key;
    }

    // TO DO: subMap
    //******************************************************
    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return null;
    }
    //******************************************************

    // HELPER METHODS

    /**
     * Utility function to left rotate the subtree with root x
     */
    public TreapNode<K,V> leftRotate(TreapNode<K,V> p)
    {
        TreapNode<K,V> r = null;
        if (p != null) {
            r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if(p.parent == null) {
                root = r;
            }
            else if (p.parent.left == p) {
                p.parent.left = r;
            }
            else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
        // Return new root
        return r;
    }

    /**
     * Utility function to right rotate the subtree with root y
     */
    public TreapNode<K,V> rightRotate(TreapNode<K,V> p)
    {
        TreapNode<K,V> l = null;
        if (p != null) {
            l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            l.parent = p.parent;
            if(p.parent == null) {
                root = l;
            }
            else if (p.parent.right == p) {
                p.parent.right = l;
            }
            else {
                p.parent.left = l;
            }
            l.right = p;
            p.parent = l;
        }
        return l;
    }

    public TreapNode<K,V> successor(TreapNode<K,V> node) {

        // code here is from the treemap.java
        if (node == null) {
            return null;
        }
        else if (node.right != null) {
            TreapNode<K,V> p = node.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        else {
            TreapNode<K,V> p = node.parent;
            TreapNode<K,V> ch = node;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    public TreapNode<K,V> predecessor(TreapNode<K,V> node) {

        // code here is from the treemap.java
        if (node == null) {
            return null;
        }
        else if (node.left != null) {
            TreapNode<K,V> p = node.left;
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        else {
            TreapNode<K,V> p = node.parent;
            TreapNode<K,V> ch = node;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    public TreapNode<K,V> getFirstEntry() {
        TreapNode<K,V> node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public TreapNode<K,V> getLastEntry() {
        TreapNode<K,V> node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // UNSUPPORTED METHODS (Map/SortedMap Interface)

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }





    // INNER CLASSES (EntrySet, TreapIterator, and EntryIterator)

    /**
     * EntrySet class is the backing structure for the Treap
     * @param <K>
     * @param <V>
     */
    protected class EntrySet<K,V> extends AbstractSet<Map.Entry<K,V>>
            implements Set<Map.Entry<K,V>> {

        private Treap treap;
        private int size;

        public EntrySet(Treap treap, int size) {
            this.treap = treap;
            this.size = size;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Treap.EntryIterator();
        }

        public boolean add(TreapNode<K,V> entry) {
            treap.put(entry.key, entry.value);
            return true;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof TreapNode)) {
                return false;
            }
            TreapNode<K,V> node = (TreapNode<K,V>) o;
            V value = node.getValue();              // value of treap node parameter
            V value2 = (V) get(node.getKey());      // value of node found
            return (value2 != null && value.equals(value2));
        }

        @Override
        public boolean isEmpty() {
            return treap.isEmpty();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /*
    protected class SubMap<K,V> extends AbstractSet<Map.Entry<K,V>>
            implements Set<Map.Entry<K,V>> {

        private K startKey;
        private K endKey;

        public SubMap(K startKey, K endKey) {
            this.startKey = startKey;
            this.endKey = endKey;

            Treap submap = new Treap();
            for (int i = startKey; i < endKey; i++) {

            }
        }

    } */

    // INNER TREAP ITERATOR CLASS (WRAPPER)

    /**
     * TreapIterator is the base class for Treap iterator
     */
    abstract protected class TreapIterator implements Iterator<Map.Entry<K,V>> {

        TreapNode<K,V> next;
        TreapNode<K,V> prev;
        int expModCount;

        public TreapIterator() {
            next = getFirstEntry();             // treap node to start iterating from
            prev = null;
            expModCount = modCount;             // current number of mods
        }

        public TreapIterator(TreapNode<K,V> first) {
            next = first;                       // treap node to start iterating from
            expModCount = modCount;             // current number of mods
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public TreapNode<K,V> next() {
            TreapNode<K,V> node = next;
            if (node == null) {
                throw new NoSuchElementException("next error");
            }
            if (modCount != expModCount) {
                throw new ConcurrentModificationException("next error");
            }
            next = successor(node);
            prev = node;
            return node;
        }
    }

    /**
     * EntryIterator class enables easy iteration through Treap items
     * Extends TreapIterator<SortedMap.Entry<K,V>>
     */
    protected class EntryIterator extends TreapIterator<SortedMap.Entry<K,V>> {

        EntryIterator() {
            super();
        }

        @Override
        public SortedMap.Entry<K,V> next() {
            return nextEntry();
        }
    }

    /**
     * SubmapIterator class enables easy iteration through a Treap submap
     * Extends TreapIterator<SortedMap.Entry<K,V>>
     */
    protected class SubmapIterator extends TreapIterator<SortedMap.Entry<K,V>> {

        SubmapIterator(TreapNode<K,V> first) {
            super(first);
        }

        @Override
        public SortedMap.Entry<K,V> next() {
            return nextEntry();
        }
    }

}