package linklist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

class CListTest {

    @Test
    void testConstructor() {
        CList<Integer> c= new CList<>();
        assertEquals("[]", c.toString());
        assertEquals("[]", c.toStringR());
        assertEquals(0, c.size());
    }

    @Test
    void testPrependAndToStringR() {
        CList<Integer> c= new CList<>();
        c.prepend(5);
        assertEquals("[5]", c.toString());
        assertEquals("[5]", c.toStringR());
        assertEquals(1, c.size());

        c.prepend(8);
        assertEquals("[8, 5]", c.toString());
        assertEquals("[5, 8]", c.toStringR());
        assertEquals(2, c.size());

        c.prepend(1);
        c.prepend(6);
        assertEquals("[6, 1, 8, 5]", c.toString());
        assertEquals("[5, 8, 1, 6]", c.toStringR());
        assertEquals(4, c.size());

        CList<String> s= new CList<>();
        s.prepend("b");
        s.prepend(null);
        assertEquals("[null, b]", s.toString());
        assertEquals("[b, null]", s.toStringR());
        assertEquals(2, s.size());

    }

    @Test
    void testAppend() {
        CList<Integer> c= new CList<>();
        c.append(5);
        assertEquals("[5]", c.toString());
        assertEquals("[5]", c.toStringR());
        assertEquals(1, c.size());

        c.append(7);
        assertEquals("[5, 7]", c.toString());
        assertEquals("[7, 5]", c.toStringR());
        assertEquals(2, c.size());

        c.append(2);
        c.append(4);
        assertEquals("[5, 7, 2, 4]", c.toString());
        assertEquals("[4, 2, 7, 5]", c.toStringR());
        assertEquals(4, c.size());

    }

    @Test
    void testChangeHeadToNext() {
        // TODO
        CList<Integer> c= new CList<>();
        CList<Integer>.Node n= c.changeHeadToNext();
        assertEquals(null, n);
        assertEquals("[]", c.toString());
        assertEquals("[]", c.toStringR());
        assertEquals(0, c.size());

        c.append(5);
        n= c.changeHeadToNext();
        assertEquals(c.head(), n);
        assertEquals("[5]", c.toString());
        assertEquals("[5]", c.toStringR());
        assertEquals(1, c.size());

        c= new CList<>();
        c.append(5);
        c.append(6);
        c.append(7);
        n= c.changeHeadToNext();
        assertEquals(6, n.data());
        assertEquals("[6, 7, 5]", c.toString());
        assertEquals("[5, 7, 6]", c.toStringR());
        assertEquals(3, c.size());
    }

    @Test
    void testGetNode() {
        CList<Integer> c= new CList<>();
        c.append(5);
        assertEquals(c.head(), c.getNode(0));

        c= new CList<>();
        for (int k= 0; k < 20; k= k + 1) {
            c.append(2 * k);
        }
        for (int k= 0; k < 20; k= k + 1) {
            assertEquals(2 * k, c.getNode(k).data());
        }
    }

    @Test
    void testRemove() {
        // test remove head of a list of length 1
        CList<Integer> c= new CList<>();
        c.append(5);
        c.remove(c.head());
        assertEquals("[]", c.toString());
        assertEquals("[]", c.toStringR());
        assertEquals(0, c.size());

        // test remove head of a list of length 2
        c= new CList<>();
        c.append(5);
        c.append(6);
        c.remove(c.head());
        assertEquals("[6]", c.toString());
        assertEquals("[6]", c.toStringR());
        assertEquals(1, c.size());

        // test remove tail of a list of length 2
        c= new CList<>();
        c.append(5);
        c.append(6);
        c.remove(c.tail());
        assertEquals("[5]", c.toString());
        assertEquals("[5]", c.toStringR());
        assertEquals(1, c.size());

        // test remove inner node, head, tail of a longer list
        c= new CList<>();
        for (int k= 0; k < 10; k= k + 1) {
            c.append(k);
        }

        c.remove(c.getNode(4));
        assertEquals("[0, 1, 2, 3, 5, 6, 7, 8, 9]", c.toString());
        assertEquals("[9, 8, 7, 6, 5, 3, 2, 1, 0]", c.toStringR());
        assertEquals(9, c.size());

        c.remove(c.head());
        assertEquals("[1, 2, 3, 5, 6, 7, 8, 9]", c.toString());
        assertEquals("[9, 8, 7, 6, 5, 3, 2, 1]", c.toStringR());
        assertEquals(8, c.size());

        c.remove(c.tail());
        assertEquals("[1, 2, 3, 5, 6, 7, 8]", c.toString());
        assertEquals("[8, 7, 6, 5, 3, 2, 1]", c.toStringR());
        assertEquals(7, c.size());
    }

    @Test
    void testInsertBefore() {
        CList<Integer> c= new CList<>();
        c.append(5);
        c.insertBefore(6, c.head());
        assertEquals("[6, 5]", c.toString());
        assertEquals("[5, 6]", c.toStringR());
        assertEquals(2, c.size());

        c= new CList<>();
        c.append(5);
        c.append(6);
        c.append(8);
        c.insertBefore(7, c.tail());
        assertEquals("[5, 6, 7, 8]", c.toString());
        assertEquals("[8, 7, 6, 5]", c.toStringR());
        assertEquals(4, c.size());
    }

    @Test
    void testEmptyStrings() {
        CList<String> c= new CList<>();
        c.append("");
        c.append("");
        c.append("");
        assertEquals("[, , ]", c.toString());
        assertEquals("[, , ]", c.toStringR());
        assertEquals(3, c.size());
    }

    @Test
    public void testIterator() {

        CList<Integer> d= new CList<>();
        for (int k= 0; k < 10; k= k + 1) {
            d.append(k + 10);
        }

        Iterator<Integer> dit= d.iterator();
        int k= 0;
        while (dit.hasNext()) {
            assertEquals((Integer) (k + 10), dit.next());
            k= k + 1;
        }

        assertEquals(10, k);
    }

    @Test
    public void testIterable() {

        CList<Integer> d= new CList<>();
        for (int k= 0; k < 10; k= k + 1) {
            d.append(k);
        }

        int tt= 0;
        for (int ob : d) {
            assertEquals(tt, ob);
            tt= tt + 1;
        }
    }
}
