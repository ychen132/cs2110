package heap;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class HeapTest {

    /** Use assertEquals to check that all fields of mh are correct. <br>
     * This means that: <br>
     * ... (1) b.length, mh.size, and mh.map.size() are all equal. <br>
     * ... (2) for each i in 0..size-1, (b[i], p[i]) is the entry mh.b[i]. <br>
     * ... (3) For each i in 0..size-1, (b[i], i) is in map.
     *
     * Precondition: b.length = p.length, <br>
     * ... No two priorities in p differ by less than .0001, <br>
     * ... (b, p) is actually a good heap. */
    public <T> void check(T[] d, double p[], Heap<T> mh) {
        assert d.length == p.length;
        // check sizes
        assertEquals(d.length, mh.size);
        assertEquals(d.length, mh.map.size());

        // check values
        String stringB= toStringB(d);
        String stringC= toStringHeapValues(mh);
        assertEquals(stringB, stringC);

        // check priorities
        String stringBpriorities= toStringB(p);
        String stringCpriorities= toStringHeapPriorities(mh);
        assertEquals(stringBpriorities, stringCpriorities);

        // check map
        ArrayList<Integer> s= new ArrayList<>();
        for (int k= 0; k < d.length; k= k + 1) { s.add(k); }
        ArrayList<Integer> mhS= new ArrayList<>();
        for (int k= 0; k < d.length; k= k + 1) { mhS.add(mh.map.get(d[k])); }
        assertEquals(s, mhS);
    }

    /** Use assertEquals to check that expected val m1 and computed val m2 are equal. */
    public void check(Heap<Integer> m1, Heap<Integer> m2) {
        // check sizes
        assertEquals(m1.size, m2.size);
        assertEquals(m1.size, m2.map.size());

        // check values
        String stringM1= toStringHeapValues(m1);
        String stringM2= toStringHeapValues(m2);
        assertEquals(stringM1, stringM2);

        // check priorities
        String stringM1p= toStringHeapPriorities(m1);
        String stringM2p= toStringHeapPriorities(m2);
        assertEquals(stringM1p, stringM2p);

        // check fields of object Element
        assertEquals(true, m1.map.equals(m2.map));
    }

    /** = a list of values in b, separated by ", " and delimited by "[", "]" */
    public <V> String toStringB(V[] b) {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < b.length; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(b[h]);
        }
        // return res + "]";
        return resb.append(']').toString();
    }

    /** = a list of values in b, separated by ", " and delimited by "[", "]" */
    public String toStringB(double[] b) {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < b.length; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(b[h]);
        }
        return resb.append(']').toString();
    }

    /** = a list of values in mh.b[0..mh.size-1], separated by ", " and delimited by "[", "]" */
    public <V> String toStringHeapValues(Heap<V> mh) {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < mh.size; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(mh.b[h].value);
        }
        return resb.append(']').toString();

    }

    /** = a list of priorities in mh.b[0..mh.size-1], separated by ", " and delimited by "[", "]" */
    public <V> String toStringHeapPriorities(Heap<V> mh) {
        StringBuilder resb= new StringBuilder("[");
        for (int h= 0; h < mh.size; h= h + 1) {
            if (h > 0) resb.append(", ");
            resb.append(mh.b[h].priority);
        }
        return resb.append(']').toString();
    }

    /** Return a min-heap with the values of d added into it, in that order. <br>
     * The priorities are the values. */
    public Heap<Integer> minHeapify(Integer[] d) {
        Heap<Integer> m= new Heap<>(true);
        for (Integer e : d) {
            m.insert(e, e);
        }
        return m;
    }

    /** Return a max-heap with the values of d added into it, in that order. <br>
     * The priorities are the values. */
    public Heap<Integer> maxHeapify(Integer[] d) {
        Heap<Integer> m= new Heap<>(false);
        for (Integer e : d) {
            m.insert(e, e);
        }
        return m;
    }

    /** Return a min-heap with the values of d and corresponding <br>
     * priorities p added to it, in that order. */
    public Heap<Integer> minHeapify(Integer[] d, double[] p) {
        Heap<Integer> m= new Heap<>(true);
        for (int h= 0; h < d.length; h= h + 1) {
            int h1= h;
            m.insert(d[h1], p[h1]);
        }
        return m;
    }

    /** Return a max-heap with the values of d and corresponding <br>
     * priorities p added to it, in that order. */
    public Heap<Integer> maxHeapify(Integer[] d, double[] p) {
        Heap<Integer> m= new Heap<>(false);
        for (int h= 0; h < d.length; h= h + 1) {
            int h1= h;
            m.insert(d[h1], p[h1]);
        }
        return m;
    }

    /** Return a min-heap with the values of d and corresponding <br>
     * priorities p added to it, in that order. */
    public Heap<String> minHeapify(String[] d, double[] p) {
        Heap<String> m= new Heap<>(true);
        for (int h= 0; h < d.length; h= h + 1) {
            m.insert(d[h], p[h]);
        }
        return m;
    }

    /** Return a min-heap with values d without using add. <br>
     * Values used as priorities */
    public Heap<Integer> griesHeapifyMin(Integer[] d) {
        Heap<Integer> heap= new Heap<>(true);
        heap.b= heap.createPairArray(d.length);
        for (int k= 0; k < d.length; k= k + 1) {
            heap.b[k]= heap.new Pair(d[k], d[k]);
            heap.map.put(d[k], k);
        }
        heap.size= d.length;
        return heap;
    }

    /** Return a max-heap with values d without using add. <br>
     * Values used as priorities */
    public Heap<Integer> griesHeapifyMax(Integer[] d) {
        Heap<Integer> heap= new Heap<>(false);
        heap.b= heap.createPairArray(d.length);
        for (int k= 0; k < d.length; k= k + 1) {
            heap.b[k]= heap.new Pair(d[k], d[k]);
            heap.map.put(d[k], k);
        }
        heap.size= d.length;
        return heap;
    }

    /** Return a min-heap with values d and priorities p without using add. */
    public Heap<Integer> griesHeapifyMin(Integer[] d, double[] p) {
        Heap<Integer> heap= new Heap<>(true);
        heap.b= heap.createPairArray(d.length); // new Entry[d.length];
        for (int k= 0; k < d.length; k= k + 1) {
            heap.b[k]= heap.new Pair(d[k], p[k]);
            heap.map.put(d[k], k);
        }
        heap.size= d.length;
        return heap;
    }

    /** Return a max-heap with values d and priorities p without using add. */
    public Heap<Integer> griesHeapifyMax(Integer[] d, double[] p) {
        Heap<Integer> heap= new Heap<>(false);
        heap.b= heap.createPairArray(d.length);
        for (int k= 0; k < d.length; k= k + 1) {
            heap.b[k]= heap.new Pair(d[k], p[k]);
            heap.map.put(d[k], k);
        }
        heap.size= d.length;
        return heap;
    }

    @Test
    @Order(1)
    /** Test ensureSpace() without using Heap.add(). */
    public void test01EnsureSpace1() {
        /* Fields of class Heap as well as method ensureSpace
         * have access modifier protected, so they can be referenced from within
         * the class, from other classes in the same package, and from a
         * subclass. We made Heap final, so one cannot create subclasses of it.
         *
         * Method griesHeapifyMax (directly above) can create an
         * empty heap, store array b in field b, fix field size, and also fix
         * the map accordingly. The four methods above with gries in their name
         * work like that.
         *
         * The code below creates a heap with 6 elements in it.
         * The size and capacity of array hp.b are both 6. The loop checks that
         * calling ensureSpace with size < 6 does not change the size
         * of the heap. The code after the loop checks that the capacity
         * is doubled and that the right values are in the beginning of
         * the new array. */
        Integer[] b= { 0, 1, 2, 3, 4, 5 };
        Heap<Integer> hp= griesHeapifyMax(b);
        // Check all values for which size should not change
        for (int k= 0; k < b.length; k= k + 1) {
            hp.size= k;
            hp.ensureSpace();
            assertEquals(k, hp.size);
        }

        // check if capacity is doubled and the array is copied
        hp.size= b.length;
        int oldSize= hp.size;
        int oldCapacity= hp.size;
        hp.ensureSpace();
        assertEquals(oldSize * 2, hp.b.length); // capacity
        assertEquals(b.length, hp.size);  // size
        for (int k= 0; k < b.length; k= k + 1) {
            assertEquals(b[k], hp.b[k].value);
            assertEquals(b[k], hp.b[k].priority, .01);
        }
    }

    @Test
    @Order(10)
    /** Test whether insert works in a min-heap when the priority of the value <br>
     * being inserted is >= priorities of other values in the heap. <br>
     * To test, we insert 3 values and then test.
     *
     * Also, test whether it throws the exception. */
    public void test10Insert() {
        Heap<Integer> mh= minHeapify(new Integer[] { 5, 7, 9 });
        check(new Integer[] { 5, 7, 9 }, new double[] { 5.0, 7.0, 9.0 }, mh);

        Heap<Integer> mh2= minHeapify(new Integer[] { 4, 7, 8 });
        assertThrows(IllegalArgumentException.class, () -> { mh2.insert(4, 4.0); });
    }

    @Test
    @Order(14)
    /** Test that insert calls ensureSpace() before increasing size */
    public void test14InsertAndEnsureSpace() {
        Integer[] b= { 0, 1, 2, 3, 4, 5 };
        Heap<Integer> hp= griesHeapifyMax(b);
        // hp.size is 6.
        try {
            hp.insert(7, 7.0);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("It looks like add increased size before calling ensureSpace()");
        }

    }

    @Test
    @Order(16)
    /** Test that inserting integers 10, 9, 8, ..., 0 into a max-heap, <br>
     * with priorities same as values, works. <br>
     * This will test that ensureSpace doubles the space. */
    public void test16InsertAndEnsureSpace() {
        Heap<Integer> mh= new Heap<>(false);
        Integer[] b= new Integer[11];
        double[] p= new double[11];
        for (int k= 10; 0 <= k; k= k - 1) {
            int k1= k;
            mh.insert(k1, k1);
            b[10 - k]= k;
            p[10 - k]= k;
        }
        check(b, p, mh);
        assertEquals(20, mh.b.length);
    }

    @Test
    @Order(20)
    /** Test Heap.swap. <br>
     * This is done using a min-heap of 4 values. */
    public void test20Swap() {
        Heap<Integer> mh= minHeapify(new Integer[] { 5, 7, 8, 9 });
        mh.swap(0, 1); // should be {7, 5, 8, 9}
        mh.swap(1, 2); // should be {7, 8, 5, 9}
        mh.swap(0, 3); // should be {9, 8, 5, 7}
        mh.swap(2, 2); // should be {9, 8, 5, 7}
        check(new Integer[] { 9, 8, 5, 7 }, new double[] { 9.0, 8.0, 5.0, 7.0 }, mh);
    }

    @Test
    @Order(30)
    /** Test add and bubble up. */
    public void test30InsertMinHeap_BubbleUp() {
        Heap<Integer> mh= griesHeapifyMin(new Integer[] { 3, 6, 8 });
        // String msg= "Adding 5 into heap [3, 6, 8]";
        mh.insert(5, 5.0);
        check(new Integer[] { 3, 5, 8, 6 }, new double[] { 3.0, 5.0, 8.0, 6.0 }, mh);

        Heap<Integer> mh1= griesHeapifyMin(new Integer[] { 3, 5, 8, 6 });
        // String msg1= "Adding 4 into heap [3, 5, 6, 8]";
        mh1.insert(4, 4.0);
        check(new Integer[] { 3, 4, 8, 6, 5 }, new double[] { 3.0, 4.0, 8.0, 6.0, 5.0 }, mh1);

        Heap<Integer> mh2= griesHeapifyMin(new Integer[] { 3, 6, 8 });
        mh2.insert(5, 5.0);
        check(new Integer[] { 3, 5, 8, 6 }, new double[] { 3.0, 5.0, 8.0, 6.0 }, mh2);

        Heap<Integer> mh3= griesHeapifyMin(new Integer[] { 3, 5, 6, 8 });
        mh3.insert(4, 4.0);
        check(new Integer[] { 3, 4, 6, 8, 5 }, new double[] { 3.0, 4.0, 6.0, 8.0, 5.0 }, mh3);

        Heap<Integer> mh4= griesHeapifyMin(new Integer[] { 3, 4, 8, 6, 5 });
        // String msg4= "Adding 1 into heap [3, 4, 8, 6, 5]";
        mh4.insert(1, 1.0);
        check(new Integer[] { 1, 4, 3, 6, 5, 8 }, new double[] { 1.0, 4.0, 3.0, 6.0, 5.0, 8.0 },
            mh4);
    }

    @Test
    @Order(32)
    /** Test insert and bubble up using a max heap. */
    public void test32InsertMaxHeap_BubbleUp() {
        Heap<Integer> mh1= griesHeapifyMax(new Integer[] { 8, 5, 6, 3 });
        // String msg1= "Adding 9 into heap [8, 5, 6, 3]";
        mh1.insert(9, 9.0);
        check(new Integer[] { 9, 8, 6, 3, 5 }, new double[] { 9.0, 8.0, 6.0, 3.0, 5.0 }, mh1);

        Heap<Integer> mh= griesHeapifyMax(new Integer[] { 8, 3, 6 });
        // String msg= "Adding 5 into heap [8, 3, 6]";
        mh.insert(5, 5.0);
        check(new Integer[] { 8, 5, 6, 3 }, new double[] { 8.0, 5.0, 6.0, 3.0 }, mh);

        Heap<Integer> mh2= griesHeapifyMax(new Integer[] { 8, 3, 6 });
        mh2.insert(2, 2.0);
        check(new Integer[] { 8, 3, 6, 2 }, new double[] { 8.0, 3.0, 6.0, 2.0 }, mh2);

        Heap<Integer> mh3= griesHeapifyMax(new Integer[] { 8, 5, 6, 4 });
        mh3.insert(7, 7.0);
        check(new Integer[] { 8, 7, 6, 4, 5 }, new double[] { 8.0, 7.0, 6.0, 4.0, 5.0 }, mh3);

        Heap<Integer> mh4= griesHeapifyMax(new Integer[] { 8, 5, 6, 4 });
        // String msg4= "Adding 1 into heap [8, 5, 6, 4]";
        mh4.insert(1, 1.0);
        check(new Integer[] { 8, 5, 6, 4, 1 }, new double[] { 8.0, 5.0, 6.0, 4.0, 1.0 }, mh4);
    }

    @Test
    @Order(34)
    /** Test insert and bubble up with duplicate priorities */
    public void test34InsertMin_BubbleUpDuplicatePriorities() {
        Heap<Integer> mh= griesHeapifyMin(new Integer[] { 4 });
        // String msg= "Adding (2, 4.0) into heap []";
        mh.insert(2, 4.0);
        check(new Integer[] { 4, 2 }, new double[] { 4.0, 4.0 }, mh);

        Heap<Integer> mh1= griesHeapifyMin(new Integer[] { 4, 2 }, new double[] { 4.0, 4.0 });
        // String msg1= "Adding (1, 4.0) into heap [4,2] --all priorities 4.0";
        mh1.insert(1, 4.0);
        check(new Integer[] { 4, 2, 1 }, new double[] { 4.0, 4.0, 4.0 }, mh1);

        Heap<Integer> mh2= griesHeapifyMin(new Integer[] { 4, 2, 1 },
            new double[] { 4.0, 4.0, 4.0 });
        // String msg2= "Adding (0, 4.0) into heap [4, 2, 1] --all priorities 4.0";
        mh2.insert(0, 4.0);
        check(new Integer[] { 4, 2, 1, 0 }, new double[] { 4.0, 4.0, 4.0, 4.0 }, mh2);
    }

    @Test
    @Order(36)
    /** Test insert and bubble up with duplicate priorities */
    public void test36InsertMax_BubbleUpDuplicatePriorities() {
        Heap<Integer> mh= griesHeapifyMax(new Integer[] { -4 });
        // String msg= "Adding (2, -4.0) into heap []";
        mh.insert(2, -4.0);
        check(new Integer[] { -4, 2 }, new double[] { -4.0, -4.0 }, mh);

        Heap<Integer> mh1= griesHeapifyMax(new Integer[] { 4, 2 }, new double[] { -4.0, -4.0 });
        // String msg1= "Adding (1, -4.0) into heap [4,2] --all priorities -4.0";
        mh1.insert(1, -4.0);
        check(new Integer[] { 4, 2, 1 }, new double[] { -4.0, -4.0, -4.0 }, mh1);

        Heap<Integer> mh2= griesHeapifyMax(new Integer[] { 4, 2, 1 },
            new double[] { -4.0, -4.0, -4.0 });
        // String msg2= "Adding (0, -4.0) into heap [4, 2, 1] --all priorities -4.0";
        mh2.insert(0, -4.0);
        check(new Integer[] { 4, 2, 1, 0 }, new double[] { -4.0, -4.0, -4.0, -4.0 }, mh2);
    }

    @Test
    @Order(40)
    /** Test peek. */
    public void test40MinPeek() {
        Heap<Integer> mh= griesHeapifyMin(new Integer[] { 1, 3 });
        // String msg= "Testing peek on heap [1, 3] --values are priorities";
        assertEquals(Integer.valueOf(1), mh.peek());
        check(new Integer[] { 1, 3 }, new double[] { 1.0, 3.0 }, mh);

        Heap<Integer> mh1= griesHeapifyMin(new Integer[] {});
        assertThrows(NoSuchElementException.class, () -> { mh1.peek(); });
    }

    @Test
    @Order(32)
    /** Test peek. */
    public void test42MaxPeek() {
        Heap<Integer> mh= griesHeapifyMax(new Integer[] { 3, 1 });
        // String msg= "Testing peek on heap [3, 1] --values are priorities";
        assertEquals(Integer.valueOf(3), mh.peek());
        check(new Integer[] { 3, 1 }, new double[] { 3.0, 1.0 }, mh);

        Heap<Integer> mh1= griesHeapifyMin(new Integer[] {});
        assertThrows(NoSuchElementException.class, () -> { mh1.peek(); });
    }

    @Test
    @Order(50)
    /** Test that when bubbling down with two children with same priority, <br>
     * the left one is used, without using poll. */
    public void test50MinBubbleDown() {
        Integer[] c= { 0, 1, 2 };
        double[] p= { 8.0, 5.0, 5.0 };
        Heap<Integer> h= griesHeapifyMin(c, p);
        h.bubbleDown(0);

        Integer[] cexp= { 1, 0, 2 };
        double[] pexp= { 5.0, 8.0, 5.0 };
        Heap<Integer> hexp= griesHeapifyMin(cexp, pexp);

        check(h, hexp);
    }

    @Test
    @Order(51)
    /** Test bubbleDown without using poll. <br>
     * The heap is filled to capacity, and there are no duplicate priorities. */
    public void test51MinBubbleDown() {
        Integer[] values= { 10, 3, 2, 4, 5, 9, 1, 6, 7, 8 };
        double[] priorities= { 10, 3, 2, 4, 5, 9, 1, 6, 7, 8 };
        Heap<Integer> mh= griesHeapifyMin(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 2, 3, 1, 4, 5, 9, 10, 6, 7, 8 };
        double[] prioritiesRes= { 2, 3, 1, 4, 5, 9, 10, 6, 7, 8 };
        Heap<Integer> mhRes= griesHeapifyMin(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(52)
    /** Test bubbleDown without using poll. Should bubble down only once. <br>
     * The heap is filled to capacity, and there are no duplicate priorities. */
    public void test52MinBubbleDown() {
        Integer[] values= { 10, 3, 2, 4, 5, 12, 11, 6, 7, 8 };
        double[] priorities= { 10, 3, 2, 4, 5, 12, 11, 6, 7, 8 };
        Heap<Integer> mh= griesHeapifyMin(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 2, 3, 10, 4, 5, 12, 11, 6, 7, 8 };
        double[] prioritiesRes= { 2, 3, 10, 4, 5, 12, 11, 6, 7, 8 };
        Heap<Integer> mhRes= griesHeapifyMin(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(53)
    /** Test bubbleDown right child. <br>
     * This checks the case: left child has bigger priority but right child has smaller one.<br>
     * With bubbling down twice. */
    public void test53MinBubbleDownRight() {
        Integer[] values= { 5, 6, 3, 8, 9, 7, 4 };
        double[] priorities= { 5, 6, 3, 8, 9, 7, 4 };
        Heap<Integer> mh= griesHeapifyMin(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 3, 6, 4, 8, 9, 7, 5 };
        double[] prioritiesRes= { 3, 6, 4, 8, 9, 7, 5 };
        Heap<Integer> mhRes= griesHeapifyMin(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(54)
    /** Test bubbleDown right child. <br>
     * This checks the case: left child has bigger priority but right child has smaller one.<br>
     * The heap is filled to capacity. */
    public void test54MinBubble_DownRight() {
        Integer[] values= { 4, 2, 3 };
        double[] priorities= { 4.0, 5.0, 3.0 };
        Heap<Integer> mh= griesHeapifyMin(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 3, 2, 4 };
        double[] prioritiesRes= { 3.0, 5.0, 4.0 };
        Heap<Integer> mhRes= griesHeapifyMin(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(56)
    /** Test bubbleDown without using poll. Should bubble down only once.<br>
     * The heap is filled to capacity, and there are no duplicate priorities. */
    public void test56MaxBubbleDown() {
        Integer[] values= { 10, 3, 2, 4, 5, 12, 11, 6, 7, 8 };
        double[] priorities= { -10, -3, -2, -4, -5, -12, -11, -6, -7, -8 };
        Heap<Integer> mh= griesHeapifyMax(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 2, 3, 10, 4, 5, 12, 11, 6, 7, 8 };
        double[] prioritiesRes= { -2, -3, -10, -4, -5, -12, -11, -6, -7, -8 };
        Heap<Integer> mhRes= griesHeapifyMin(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(57)
    /** Test bubbleDown right child. <br>
     * This checks the case: left child has bigger priority but right child has <br>
     * smaller one. With bubbling down twice. */
    public void test57MaxBubbleDownRight() {
        Integer[] values= { 5, 6, 3, 8, 9, 7, 4 };
        double[] priorities= { -5, -6, -3, -8, -9, -7, -4 };
        Heap<Integer> mh= griesHeapifyMax(values, priorities);
        mh.bubbleDown(0);
        Integer[] valuesRes= { 3, 6, 4, 8, 9, 7, 5 };
        double[] prioritiesRes= { -3, -6, -4, -8, -9, -7, -5 };
        Heap<Integer> mhRes= griesHeapifyMax(valuesRes, prioritiesRes);
        check(mhRes, mh);
    }

    @Test
    @Order(60)
    /** Test poll and bubbledown with no duplicate priorities. */
    public void test60Poll_BubbleDown_NoDups() {
        Heap<Integer> mh= minHeapify(new Integer[] { 5 });
        Integer res= mh.poll();
        assertEquals(Integer.valueOf(5), res);
        check(new Integer[] {}, new double[] {}, mh);

        Heap<Integer> mh1= minHeapify(new Integer[] { 5, 6 });
        Integer res1= mh1.poll();
        assertEquals(Integer.valueOf(5), res1);
        check(new Integer[] { 6 }, new double[] { 6.0 }, mh1);

        // this requires comparing lchild and rchild and using lchild
        Heap<Integer> mh2= minHeapify(new Integer[] { 4, 5, 6, 7, 8, 9 });
        Integer res2= mh2.poll();
        assertEquals(Integer.valueOf(4), res2);
        check(new Integer[] { 5, 7, 6, 9, 8 }, new double[] { 5.0, 7.0, 6.0, 9.0, 8.0 }, mh2);

        // this requires comparing lchild and rchild and using rchild
        Heap<Integer> mh3= minHeapify(new Integer[] { 4, 6, 5, 7, 8, 9 });
        Integer res3= mh3.poll();
        assertEquals(Integer.valueOf(4), res3);
        check(new Integer[] { 5, 6, 9, 7, 8 }, new double[] { 5.0, 6.0, 9.0, 7.0, 8.0 }, mh3);

        // this requires bubbling down when only one child
        Heap<Integer> mh4= minHeapify(new Integer[] { 4, 5, 6, 7, 8 });
        Integer res4= mh4.poll();
        assertEquals(Integer.valueOf(4), res4);
        check(new Integer[] { 5, 7, 6, 8 }, new double[] { 5.0, 7.0, 6.0, 8.0 }, mh4);

        Heap<Integer> mh5= minHeapify(new Integer[] { 2, 4, 3, 6, 7, 8, 9 });
        Integer res5= mh5.poll();
        assertEquals(Integer.valueOf(2), res5);
        check(new Integer[] { 3, 4, 8, 6, 7, 9 }, new double[] { 3.0, 4.0, 8.0, 6.0, 7.0, 9.0 },
            mh5);

        Heap<Integer> mh6= minHeapify(new Integer[] { 2, 4, 3, 6, 7, 9, 8 });
        Integer res6= mh6.poll();
        assertEquals(Integer.valueOf(2), res6);
        check(new Integer[] { 3, 4, 8, 6, 7, 9 }, new double[] { 3.0, 4.0, 8.0, 6.0, 7.0, 9.0 },
            mh6);

        Heap<Integer> mh7= new Heap<>(true);
        assertThrows(NoSuchElementException.class, () -> { mh7.poll(); });
    }

    @Test
    @Order(62)
    /** Test that polling from an array filled to capacity works. <br>
     * We do this with all priorities the same so bubbling down does nothing. */
    public void test62Poll() {
        Integer[] b= new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        double[] p= new double[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        Heap<Integer> mh= griesHeapifyMin(b, p);
        int v= mh.poll();
        assertEquals(0, v);

        Integer[] bRes= new Integer[] { 9, 1, 2, 3, 4, 5, 6, 7, 8 };
        double[] pRes= new double[] { 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        check(bRes, pRes, mh);
    }

    @Test
    @Order(64)
    /** Test that polling from an array of size 3 works where a bubble down is necessary. */
    public void test64Poll() {
        Integer[] b= new Integer[] { 1, 2, 3 };
        double[] p= new double[] { 1.0, 2.0, 3.0 };
        Heap<Integer> mh= griesHeapifyMin(b, p);
        int v= mh.poll();
        assertEquals(1, v);

        Integer[] bRes= new Integer[] { 2, 3 };
        double[] pRes= new double[] { 2.0, 3.0 };
        check(bRes, pRes, mh);
    }

    @Test
    @Order(66)
    /** Test bubble-up and bubble-down with duplicate priorities. */
    public void test66testDuplicatePriorities() {
        // values should not bubble up or down past ones with duplicate priorities.
        // First two check bubble up
        Heap<Integer> mh1= minHeapify(new Integer[] { 6 }, new double[] { 4.0 });
        mh1.insert(5, 4.0);
        check(new Integer[] { 6, 5 }, new double[] { 4.0, 4.0 }, mh1);

        Heap<Integer> mh2= minHeapify(new Integer[] { 7, 6 }, new double[] { 4.0, 4.0 });
        mh2.insert(3, 4.0);
        check(new Integer[] { 7, 6, 3 }, new double[] { 4.0, 4.0, 4.0 }, mh2);

        // Check bubble up
        Heap<Integer> mh3= minHeapify(new Integer[] { 5, 6, 7 }, new double[] { 4.0, 4.0, 4.0 });
        mh3.poll();
        check(new Integer[] { 7, 6 }, new double[] { 4.0, 4.0 }, mh3);

        // Check bubble up
        Heap<Integer> mh4= minHeapify(new Integer[] { 5, 7, 6, 8 },
            new double[] { 4.0, 4.0, 4.0, 4.0 });
        mh4.poll();
        check(new Integer[] { 8, 7, 6 }, new double[] { 4.0, 4.0, 4.0 }, mh4);

    }

    @Test
    @Order(70)
    /** Test changePriority in a max-heap. */
    public void test70MaxChangePriority() {
        // First three: bubble up tests
        Heap<Integer> mh1= maxHeapify(new Integer[] { 9, 7, 6, 5, 3, 2, 1 });
        mh1.changePriority(5, 6.0);
        check(new Integer[] { 9, 7, 6, 5, 3, 2, 1 },
            new double[] { 9.0, 7.0, 6.0, 6.0, 3.0, 2.0, 1.0 }, mh1);

        Heap<Integer> mh3= maxHeapify(new Integer[] { 9, 7, 6, 5, 3, 2, 1 });
        mh3.changePriority(5, 9.0);
        check(new Integer[] { 9, 5, 6, 7, 3, 2, 1 },
            new double[] { 9.0, 9.0, 6.0, 7.0, 3.0, 2.0, 1.0 }, mh3);

        Heap<Integer> mh5= maxHeapify(new Integer[] { 9, 7, 6, 5, 3, 2, 1 });
        mh5.changePriority(2, 6.0);
        check(new Integer[] { 9, 7, 6, 5, 3, 2, 1 },
            new double[] { 9.0, 7.0, 6.0, 5.0, 3.0, 6.0, 1.0 }, mh5);

        // second three: bubble down tests
        Heap<Integer> mh2= maxHeapify(new Integer[] { 9, 7, 6, 5, 3, 2, 1 });
        mh2.changePriority(9, 3.0);
        check(new Integer[] { 7, 5, 6, 9, 3, 2, 1 },
            new double[] { 7.0, 5.0, 6.0, 3.0, 3.0, 2.0, 1.0 }, mh2);

        Heap<Integer> mh4= maxHeapify(new Integer[] { 9, 7, 6, 5, 3, 2, 1 });
        mh4.changePriority(6, 0.0);
        check(new Integer[] { 9, 7, 2, 5, 3, 6, 1 },
            new double[] { 9.0, 7.0, 2.0, 5.0, 3.0, 0.0, 1.0 }, mh4);

        Heap<Integer> mh6= maxHeapify(new Integer[] { 8, 7, 6, 5, 3, 2, 1 });
        mh6.changePriority(1, 9.0);
        check(new Integer[] { 1, 7, 8, 5, 3, 2, 6 },
            new double[] { 9.0, 7.0, 8.0, 5.0, 3.0, 2.0, 6.0 }, mh6);

        // throw exception test
        Heap<Integer> mh7= new Heap<>(true);
        mh7.insert(5, 5.0);
        assertThrows(IllegalArgumentException.class, () -> { mh7.changePriority(6, 5.0); });
    }

    @Test
    @Order(72)
    /** Test changePriority in a min-heap. */
    public void test72MinChangePriority() {
        // First three: bubble up tests
        Heap<Integer> mh1= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh1.changePriority(5, 4.0);
        check(new Integer[] { 1, 2, 3, 5, 6, 7, 9 },
            new double[] { 1.0, 2.0, 3.0, 4.0, 6.0, 7.0, 9.0 }, mh1);

        Heap<Integer> mh2= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh2.changePriority(2, 1.0);
        check(new Integer[] { 1, 2, 3, 5, 6, 7, 9 },
            new double[] { 1.0, 1.0, 3.0, 5.0, 6.0, 7.0, 9.0 }, mh2);

        Heap<Integer> mh3= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh3.changePriority(5, 1.0);
        check(new Integer[] { 1, 5, 3, 2, 6, 7, 9 },
            new double[] { 1.0, 1.0, 3.0, 2.0, 6.0, 7.0, 9.0 }, mh3);

        // second three: bubble down tests
        Heap<Integer> mh4= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh4.changePriority(2, 5.0);
        check(new Integer[] { 1, 2, 3, 5, 6, 7, 9 },
            new double[] { 1.0, 5.0, 3.0, 5.0, 6.0, 7.0, 9.0 }, mh4);

        Heap<Integer> mh5= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh5.changePriority(2, 6.0);
        check(new Integer[] { 1, 5, 3, 2, 6, 7, 9 },
            new double[] { 1.0, 5.0, 3.0, 6.0, 6.0, 7.0, 9.0 }, mh5);

        Heap<Integer> mh6= minHeapify(new Integer[] { 1, 2, 3, 5, 6, 7, 9 });
        mh6.changePriority(1, 7.0);
        check(new Integer[] { 2, 5, 3, 1, 6, 7, 9 },
            new double[] { 2.0, 5.0, 3.0, 7.0, 6.0, 7.0, 9.0 }, mh6);

        // throw exception test
        Heap<Integer> mh7= new Heap<>(true);
        mh7.insert(5, 5.0);
        assertThrows(IllegalArgumentException.class, () -> { mh7.changePriority(6, 5.0); });
    }

    @Test
    @Order(80)
    /** Test a few calls with Strings */
    public void test80Strings() {
        Heap<String> mh= new Heap<>(true);
        check(new String[] {}, new double[] {}, mh);
        mh.insert("abc", 5.0);
        check(new String[] { "abc" }, new double[] { 5.0 }, mh);
        mh.insert(null, 3.0);
        check(new String[] { null, "abc" }, new double[] { 3.0, 5.0 }, mh);
        mh.insert("", 2.0);
        check(new String[] { "", "abc", null }, new double[] { 2.0, 5.0, 3.0 }, mh);
        String p= mh.poll();
        check(new String[] { null, "abc" }, new double[] { 3.0, 5.0 }, mh);
        mh.changePriority(null, 7.0);
        check(new String[] { "abc", null }, new double[] { 5.0, 7.0 }, mh);
    }

    @Test
    @Order(82)
    /** Test using values in 0..999 and random values for the priorities. <br>
     * There will be duplicate priorities. */
    public void test82BigTests() {
        // The values to put in Heap
        int[] b= new int[1000];
        for (int k= 0; k < b.length; k= k + 1) {
            b[k]= k;
        }

        Random rand= new Random(52);

        // bp: priorities of the values
        double[] bp= new double[b.length];
        for (int k= 0; k < bp.length; k= k + 1) {
            bp[k]= (int) (rand.nextDouble() * bp.length / 3);
        }

        // Build the Heap and map to be able to get priorities easily
        Heap<Integer> mh= new Heap<>(true);
        HashMap<Integer, Double> hashMap= new HashMap<>();
        for (int k= 0; k < b.length; k= k + 1) {
            mh.insert(b[k], bp[k]);
            hashMap.put(b[k], bp[k]);
        }

        // Poll the heap into array bpoll
        int[] bpoll= new int[b.length];
        pollHeap(mh, b);

        // Check that the priorities of the polled values are in order.
        Double previousPriority= hashMap.get(bpoll[0]);
        boolean inOrder= true;
        for (int k= 1; k < bpoll.length; k= k + 1) {
            Double p= hashMap.get(bpoll[k]);
            inOrder= inOrder && previousPriority <= p;
            previousPriority= p;
        }
        boolean finalInOrder= inOrder;
        assertEquals("Polled values are in order", true, finalInOrder);
    }

    /** Poll all elements of m into b. <br>
     * Precondition m and b are the same size. */
    public void pollHeap(Heap<Integer> m, int[] b) {
        for (int k= 0; k < b.length; k= k + 1) {
            b[k]= m.poll();
        }
    }

}
