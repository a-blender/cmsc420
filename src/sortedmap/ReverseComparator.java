package cmsc420.sortedmap;

import java.util.Comparator;

/**
 * Reverse comparator for comparing Treap keys
 */
public class ReverseComparator implements Comparator<String> {

    /**
     * Overridden compare method
     * @param key1 = treap key 1
     * @param key2 = treap key 2
     * @return
     */
    public int compare(String key1, String key2) {
        return key2.compareTo(key1);
    }
}

