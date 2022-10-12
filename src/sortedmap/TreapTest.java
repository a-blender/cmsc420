package cmsc420.sortedmap;

import java.util.*;

public class TreapTest {

    private TreeMap<String, String> map;
    private TreeMap<Integer, Integer> map2;
    private Treap treap;
    private Treap treap2;


    public TreapTest() {

        map = new TreeMap();
        treap = new Treap();
        map2 = new TreeMap();
        treap2 = new Treap();

        test1();
        test2();

        printTreeMap(map);
        printTreap(treap);

        test3();
        test4();
        test5();
        test6();
        // test7();
        test8();

        // test9();
        // test10();
        // test 11();
        // test12();
        // test13();
        // test14();

        test15();
    }

    public void test1() {

        System.out.println("test 1 *******************************");
        map.put("1", "10");
        map.put("2", "11");
        map.put("3", "12");
        map.put("4", "13");
    }

    public void test2() {

        System.out.println("test 2 *******************************");
        treap.put("1", "10");
        treap.put("2", "11");
        treap.put("3", "12");
        treap.put("4", "13");
    }

    public void test3() {

        System.out.println("test 3 *******************************");

        System.out.println("treemap contains key a: " + map.containsKey("a"));
        System.out.println("treap contains key a: " + treap.containsKey("a"));

        System.out.println("treemap contains key g: " + map.containsKey("g"));
        System.out.println("treap contains key g: " + treap.containsKey("g"));

        System.out.println("treap contains key 4: " + treap.containsKey("4"));
        System.out.println("treap contains value a: " + treap.containsValue("a"));
        System.out.println("treap contains value g1: " + treap.containsValue("g1"));
        System.out.println("treap contains value z: " + treap.containsValue("z"));
    }

    public void test4() {

        System.out.println("test 4 *******************************");

        System.out.println("treemap key=a has value="+map.get("a"));
        System.out.println("treap key=a has value="+treap.get("a"));

        System.out.println("treemap key=c has value="+treap.get("c"));
        System.out.println("treap key=c has value="+treap.get("c"));

        System.out.println("treemap key=f has value="+treap.get("f"));
        System.out.println("treap key=f has value="+treap.get("f"));
    }

    public void test5() {

        System.out.println("test 5 *******************************");

        System.out.println("treap first key: "+treap.firstKey());
        System.out.println("treemap first key: "+map.firstKey());

        System.out.println("treap last key: "+treap.lastKey());
        System.out.println("treemap last key: "+map.lastKey());
    }

    public void test6() {

        System.out.println("test 6 *******************************");

        System.out.println("treap size: "+treap.size());
        System.out.println("treemap size: "+map.size());

        System.out.println("treap is empty: "+treap.isEmpty());
        System.out.println("treemap is empty: "+map.isEmpty());
    }

    public void test7() {

        System.out.println("test 7 *******************************");

        SortedMap<String,String> submap1 = map.subMap("h", "d");
        Set<SortedMap.Entry<String,String>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap node: "+itr1.next());
        }

        submap1.put("f", "7");

        Iterator<SortedMap.Entry<String,String>> itr3 = es1.iterator();
        System.out.println(itr3.hasNext());
        while (itr3.hasNext()) {
            System.out.println("Treemap submap node: "+itr3.next());
        }

        SortedMap<String,String> submap2 = treap.subMap("h", "d");
        Set<SortedMap.Entry<String,String>> es2 = submap2.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr2 = es2.iterator();
        System.out.println(itr2.hasNext());
        while (itr2.hasNext()) {
            System.out.println("TREAP submap node: "+itr2.next());
        }

        submap2.put("f", "7");

        Iterator<SortedMap.Entry<String,String>> itr4 = es2.iterator();
        System.out.println(itr4.hasNext());
        while (itr4.hasNext()) {
            System.out.println("TREAP submap node: "+itr4.next());
        }
    }

    public void test8() {

        System.out.println("test 8 *******************************");
        System.out.println("toString HashCode Equals MOTHERFUCKUHHHHHHHHHH");

        System.out.println("Treemap tostring: "+map.toString());
        System.out.println("Treap tostring: "+treap.toString());

        System.out.println("Treemap hashcode: "+map.hashCode());
        System.out.println("Treap hashcode: "+treap.hashCode());

        System.out.println("Equals? "+map.equals(treap));
    }

    public void test9() {

        System.out.println("test 9 *******************************");

        /*
        create entryset of a treap
        create entryset of a submap of a treap
        insert -> treap: should REFLECT in the submap
         */

        SortedMap<String,String> submap1 = map.subMap("a", "d");
        map.put("a", "100");

        printTreeMap(map);

        Set<SortedMap.Entry<String,String>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr1.next());
        }

        // does the treap behave the same way?

        SortedMap<String,String> submap2 = treap.subMap("a", "d");
        treap.put("a", "100");

        printTreap(treap);

        Set<SortedMap.Entry<String,String>> es2 = submap2.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr2 = es2.iterator();
        System.out.println(itr2.hasNext());
        while (itr2.hasNext()) {
            System.out.println("TREAP submap entryset: "+itr2.next());
        }

        System.out.println("MORE THINGS");
        System.out.println("tree firstkey: "+submap1.firstKey());
        System.out.println("submap firstkey: "+submap2.firstKey());

        System.out.println("tree lastkey: "+submap1.lastKey());
        System.out.println("submap lastkey: "+submap2.lastKey());

        System.out.println("tree size: "+submap1.size());
        System.out.println("submap size: "+submap2.size());
    }

    public void test10() {

        System.out.println("test 10 *******************************");
        System.out.println("BREAK SHIT TIME!!!");

        SortedMap<String,String> submap1 = map.subMap("b", "h");

        // do a lot of things
        map.put("a", "1");
        map.put("a", "111111111111111111111111111111");
        System.out.println(map.firstKey());
        System.out.println(map.lastKey());
        map.put("c", "0");
        map.put("g", "fuckingblahfehiwofheiwoafew");
        map.put("h", "fuckingmoreblah");
        map.put("i", "Bad Node");
        submap1.put("d", "7000");
        map.put("f", "I WANT TO FUCK YOU");
        System.out.println(submap1.firstKey());
        System.out.println(submap1.lastKey());
        System.out.println(map.size());
        System.out.println(submap1.size());

        printTreeMap(map);

        Set<SortedMap.Entry<String,String>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr1.next());
        }

        System.out.println("\nDoes the Treap behave the same way?\n");

        SortedMap<String,String> submap2 = treap.subMap("b", "h");

        // do a fuck ton of things

        treap.put("a", "1");
        treap.put("a", "111111111111111111111111111111");
        System.out.println(treap.firstKey());
        System.out.println(treap.lastKey());
        treap.put("c", "0");
        treap.put("g", "fuckingblahfehiwofheiwoafew");
        treap.put("h", "fuckingmoreblah");
        treap.put("i", "Bad Node");
        treap.put("f", "I WANT TO FUCK YOU");
        System.out.println(submap2.firstKey());
        System.out.println(submap2.lastKey());
        System.out.println(treap.size());
        System.out.println(submap2.size());

        printTreap(treap);

        Set<SortedMap.Entry<String,String>> es2 = submap2.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr2 = es2.iterator();
        System.out.println(itr2.hasNext());
        while (itr2.hasNext()) {
            System.out.println("TREAP submap entryset: "+itr2.next());
        }
    }

    public void test11() {

        System.out.println("test 11 *******************************");
        System.out.println("HELLO. TEST PUT.");

        SortedMap<String,String> submap1 = map.subMap("a", "c");

        printTreeMap(map);
        System.out.println("Treemap shit.");
        System.out.println("size: "+map.size());
        System.out.println("subsize: "+ submap1.size());
        // System.out.println("first key: "+ submap1.firstKey());
        // System.out.println("last key: "+ submap1.lastKey());
        System.out.println("empty? "+ submap1.isEmpty());
        System.out.println("get key=1: "+ submap1.get("1"));
        System.out.println("get key=a: "+ submap1.get("a"));
        System.out.println("get key=d: "+ submap1.get("d"));
        System.out.println("get key=blah: "+ submap1.get("blah"));

        Set<SortedMap.Entry<String,String>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr1.next());
        }

        // ADD ONE THING
        System.out.println("ADDING A THING... ");
        submap1.put("a", "thisisawesome");
        printTreeMap(map);
        System.out.println("Treemap shit.");
        System.out.println("size: "+map.size());
        System.out.println("subsize: "+ submap1.size());
        // System.out.println("first key: "+ submap1.firstKey());
        // System.out.println("last key: "+ submap1.lastKey());
        System.out.println("empty? "+ submap1.isEmpty());
        System.out.println("get key=1: "+ submap1.get("1"));
        System.out.println("get key=a: "+ submap1.get("a"));
        System.out.println("get key=d: "+ submap1.get("d"));
        System.out.println("get key=blah: "+ submap1.get("blah"));

        Set<SortedMap.Entry<String,String>> es2 = submap1.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr2 = es2.iterator();
        System.out.println(itr2.hasNext());
        while (itr2.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr2.next());
        }

        System.out.println("\nDoes the Treap behave the same way?\n");

        SortedMap<String,String> submap2 = treap.subMap("a", "c");

        printTreap(treap);
        System.out.println("TREAP shit.");
        System.out.println("size: "+treap.size());
        System.out.println("subsize: "+ submap2.size());
        // System.out.println("first key: "+ submap2.firstKey());
        // System.out.println("last key: "+ submap2.lastKey());
        System.out.println("empty? "+ submap2.isEmpty());
        System.out.println("get key=1: "+ submap2.get("1"));
        System.out.println("get key=a: "+ submap2.get("a"));
        System.out.println("get key=d: "+ submap2.get("d"));
        System.out.println("get key=blah: "+ submap2.get("blah"));

        Set<SortedMap.Entry<String,String>> es3 = submap2.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr3 = es3.iterator();
        System.out.println(itr3.hasNext());
        while (itr3.hasNext()) {
            System.out.println("TREAP submap entryset: "+itr3.next());
        }

        // ADD ONE THING
        System.out.println("ADDING A THING... ");
        submap2.put("a", "thisisawesome");
        printTreap(treap);
        System.out.println("TREAP shit.");
        System.out.println("size: "+treap.size());
        System.out.println("subsize: "+ submap2.size());
        // System.out.println("first key: "+ submap1.firstKey());
        // System.out.println("last key: "+ submap1.lastKey());
        System.out.println("empty? "+ submap2.isEmpty());
        System.out.println("get key=1: "+ submap2.get("1"));
        System.out.println("get key=a: "+ submap2.get("a"));
        System.out.println("get key=d: "+ submap2.get("d"));
        System.out.println("get key=blah: "+ submap2.get("blah"));

        Set<SortedMap.Entry<String,String>> es4 = submap2.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr4 = es4.iterator();
        System.out.println(itr4.hasNext());
        while (itr4.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr4.next());
        }
    }

    public void test12() {

        System.out.println("test 12 *******************************");
        System.out.println("TEST toString, HashCode, and Equals of TreeMap and Treap!!!");

        printTreeMap(map);

        SortedMap<String,String> submap1 = map.subMap("a", "c");
        map.put("b", "2");
        submap1.put("b", "fuckingfuck");
        map.put("d", "7000");
        submap1.get("d");
        map.put("f", "100");
        map.put("g", "200");
        map.put("a", "fuck");

        System.out.println("\nDoes the Treap behave the same way?\n");

        printTreap(treap);

        SortedMap<String,String> submap2 = treap.subMap("a", "c");
        treap.put("b", "2");
        submap2.put("b", "fuckingfuck");
        treap.put("d", "7000");
        submap2.get("d");
        treap.put("f", "100");
        treap.put("g", "200");
        treap.put("a", "fuck");

        System.out.println("TreeMap toString: "+map.toString());
        System.out.println("Treap toString: "+treap.toString());
        System.out.println("TreeMap hashcode: "+map.hashCode());
        System.out.println("Treap hashcode: "+treap.hashCode());
        System.out.println("Equals? "+map.equals(treap));
    }

    public void test13() {

        System.out.println("test 13 *******************************");
        System.out.println("Entryset size isn't being updated...");

        printTreeMap(map);

        map.put("b", "2");
        map.put("d", "7000");
        map.put("f", "100");
        map.put("g", "200");
        map.put("a", "fuck");

        System.out.println("\nDoes the Treap behave the same way?\n");

        printTreap(treap);

        treap.put("b", "2");
        treap.put("d", "7000");
        treap.put("f", "100");
        treap.put("g", "200");
        treap.put("a", "fuck");

        System.out.println("printing newly added shitttt");
        printTreeMap(map);
        printTreap(treap);

        System.out.println("TreeMap size: "+map.size());
        System.out.println("TREAP size: "+treap.size());
    }

    public void test14() {

        System.out.println("test 14 *******************************");
        System.out.println("CHECK SUBMAP INSERT...");

        map2.put(1,1);
        map2.put(2,2);
        map2.put(3,3);
        map2.put(4,4);
        treap2.put(1,1);
        treap2.put(2,2);
        treap2.put(3,3);
        treap2.put(4,4);

        SortedMap<Integer,Integer> submap1 = map2.subMap(5, 15);

        printTreeMap(map2);

        System.out.println("Treemap shit.");
        map2.put(13, 13);
        map2.put(19, 19);

        Set<SortedMap.Entry<Integer,Integer>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<Integer,Integer>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr1.next());
        }

        System.out.println("\nDoes the Treap behave the same way?\n");

        SortedMap<Integer,Integer> submap2 = treap2.subMap(5, 15);

        printTreap(treap2);

        System.out.println("TREAP shit.");
        treap2.put(13, 13);
        treap2.put(19, 19);

        Set<SortedMap.Entry<Integer,Integer>> es3 = submap2.entrySet();
        Iterator<SortedMap.Entry<Integer,Integer>> itr3 = es3.iterator();
        System.out.println(itr3.hasNext());
        while (itr3.hasNext()) {
            System.out.println("TREAP submap entryset: "+itr3.next());
        }
    }

    public void test15() {

        System.out.println("test 15 *******************************");
        System.out.println("CHECK SUBMAP INSERT...");

        map2.put(1,1);
        map2.put(2,2);
        map2.put(3,3);
        map2.put(4,4);
        treap2.put(1,1);
        treap2.put(2,2);
        treap2.put(3,3);
        treap2.put(4,4);

        SortedMap<Integer,Integer> submap1 = map2.subMap(5, 15);

        printTreeMap(map2);

        System.out.println("Treemap shit.");
        map2.put(5, 5);
        map2.put(10, 10);

        Set<SortedMap.Entry<Integer,Integer>> es1 = submap1.entrySet();
        Iterator<SortedMap.Entry<Integer,Integer>> itr1 = es1.iterator();
        System.out.println(itr1.hasNext());
        while (itr1.hasNext()) {
            System.out.println("Treemap submap entryset: "+itr1.next());
        }

        System.out.println("\nDoes the Treap behave the same way?\n");

        SortedMap<Integer,Integer> submap2 = treap2.subMap(5, 15);
        printTreap(treap2);

        System.out.println("TREAP shit.");
        treap2.put(5, 5);
        treap2.put(10, 10);

        Set<SortedMap.Entry<Integer,Integer>> es3 = submap2.entrySet();
        Iterator<SortedMap.Entry<Integer,Integer>> itr3 = es3.iterator();
        System.out.println(itr3.hasNext());
        while (itr3.hasNext()) {
            System.out.println("TREAP submap entryset: "+itr3.next());
        }

        System.out.println("Treemap size: "+map2.size());
        System.out.println("TREAP size: "+treap2.size());
        System.out.println("Treemap subsize: "+es1.size());
        System.out.println("TREAP subsize: "+es3.size());

        System.out.println("TESTING THE IMPORTANT SHIT**************************");
        System.out.println("TreeMap toString: "+submap1.toString());
        System.out.println("Treap toString: "+submap2.toString());
        System.out.println("TreeMap hashcode: "+submap1.hashCode());
        System.out.println("Treap hashcode: "+submap2.hashCode());
        System.out.println("Equals? "+submap1.equals(submap2));

        System.out.println("Even more shit***************************************");
        SortedMap<Integer,Integer> submap3 = submap1.subMap(6, 14);
        System.out.println("Treemap SS: "+submap3.toString());

        SortedMap<Integer,Integer> submap4 = submap2.subMap(6, 14);
        System.out.println("Treap SS: "+submap4.toString());
    }

    public void printTreeMap(TreeMap printmap) {

        System.out.println("printTreeMap ***************************");

        Set<SortedMap.Entry<String,String>> entryset = printmap.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr = entryset.iterator();
        while (itr.hasNext()) {
            System.out.println("TreeMap node: " + itr.next());
        }
    }

    public void printTreap(Treap printtreap) {

        System.out.println("printTreap ***************************");

        Set<SortedMap.Entry<String,String>> entryset = printtreap.entrySet();
        Iterator<SortedMap.Entry<String,String>> itr = entryset.iterator();
        while (itr.hasNext()) {
            System.out.println("Treap node: " + itr.next());
        }
    }

    public void clear() {

        treap.clear();
        map.clear();
    }
}
