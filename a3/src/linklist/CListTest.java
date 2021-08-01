package linklist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CListTest {

    @Test
    public void testConstructor() {
        CList<Integer> a= new CList<>();
        assertEquals("[]", a.toString());
        assertEquals("[]", a.toStringR());
        assertEquals(0, a.size());
    }

    @Test
    public void testPrepend() {
        CList<Integer> b= new CList<>();
        // prepend to empty list//
        b.prepend(0);
        assertEquals("[0]", b.toString());
        assertEquals("[0]", b.toStringR());
        assertEquals(1, b.size());
        // prepend to one element//
        b.prepend(18);
        assertEquals("[18, 0]", b.toString());
        assertEquals("[0, 18]", b.toStringR());
        assertEquals(2, b.size());
        // prepend multiple elements//
        b.prepend(314);
        assertEquals("[314, 18, 0]", b.toString());
        assertEquals("[0, 18, 314]", b.toStringR());
        assertEquals(3, b.size());
        // prepend same value to list//
        b.prepend(314);
        assertEquals("[314, 314, 18, 0]", b.toString());
        assertEquals("[0, 18, 314, 314]", b.toStringR());
        assertEquals(4, b.size());
    }

    @Test
    public void testChangedHeadToNext() {
        // create a link list with 0 elements, assert change head//
        CList<Integer> c= new CList<>();
        c.changeHeadToNext();
        assertEquals("[]", c.toString());
        assertEquals("[]", c.toStringR());
        assertEquals(0, c.size());
        // add one more element//
        c.prepend(18);
        c.changeHeadToNext();
        assertEquals("[18]", c.toString());
        assertEquals("[18]", c.toStringR());
        assertEquals(1, c.size());
        // add another element//
        c.prepend(314);
        c.changeHeadToNext();
        assertEquals("[18, 314]", c.toString());
        assertEquals("[314, 18]", c.toStringR());
        assertEquals(2, c.size());
        // add a lot of elements//
        c.prepend(720);
        c.prepend(520);
        assertEquals("[520, 720, 18, 314]", c.toString());
        assertEquals("[314, 18, 720, 520]", c.toStringR());
        assertEquals(4, c.size());
        // asserting using change head;
        c.changeHeadToNext();
        assertEquals("[720, 18, 314, 520]", c.toString());
        assertEquals("[520, 314, 18, 720]", c.toStringR());
        assertEquals(4, c.size());
    }

    @Test
    public void testAppend() {
        CList<Integer> d= new CList<>();
        // prepend to empty list//
        d.append(0);
        assertEquals("[0]", d.toString());
        assertEquals("[0]", d.toStringR());
        assertEquals(1, d.size());
        // prepend to one element//
        d.append(18);
        assertEquals("[18, 0]", d.toStringR());
        assertEquals("[0, 18]", d.toString());
        assertEquals(2, d.size());
        // prepend multiple elements//
        d.append(314);
        assertEquals("[314, 18, 0]", d.toStringR());
        assertEquals("[0, 18, 314]", d.toString());
        assertEquals(3, d.size());
        // prepend same value to list//
        d.append(314);
        assertEquals("[314, 314, 18, 0]", d.toStringR());
        assertEquals("[0, 18, 314, 314]", d.toString());
        assertEquals(4, d.size());
    }

    @Test
    public void testgetNode() {
        CList<Integer> e= new CList<>();
        e.append(1);
        // get Node of one element list//
        assertEquals("[1]", e.toString());
        assertEquals("[1]", e.toStringR());
        assertEquals(1, e.size());
        assertEquals(1, e.getNode(0).data());

        e.append(2);
        assertEquals("[1, 2]", e.toString());
        assertEquals("[2, 1]", e.toStringR());
        assertEquals(2, e.size());
        assertEquals(2, e.getNode(1).data());
        assertEquals(1, e.getNode(0).data());

        e.prepend(1);
        assertEquals("[1, 1, 2]", e.toString());
        assertEquals("[2, 1, 1]", e.toStringR());
        assertEquals(3, e.size());
        assertEquals(1, e.getNode(1).data());
        assertEquals(1, e.getNode(0).data());

        e.append(-2);
        e.append(3);
        assertEquals("[1, 1, 2, -2, 3]", e.toString());
        assertEquals("[3, -2, 2, 1, 1]", e.toStringR());
        assertEquals(5, e.size());
        assertEquals(1, e.getNode(1).data());  // get Node in first half//
        assertEquals(-2, e.getNode(3).data()); // get Node in last half//
        assertEquals(2, e.getNode(2).data());  // get Node of middle element//
    }

    @Test
    public void testRemove() {
        // append to list
        CList<Integer> f= new CList<>();
        f.prepend(1);
        f.prepend(3);
        f.prepend(5);
        f.prepend(7);
        f.prepend(9);
        assertEquals("[9, 7, 5, 3, 1]", f.toString());
        assertEquals("[1, 3, 5, 7, 9]", f.toStringR());
        assertEquals(5, f.size());

        // start deleting
        f.remove(f.head());
        assertEquals("[7, 5, 3, 1]", f.toString());
        assertEquals("[1, 3, 5, 7]", f.toStringR());
        assertEquals(4, f.size());

        f.remove(f.head().next());
        assertEquals("[7, 3, 1]", f.toString());
        assertEquals("[1, 3, 7]", f.toStringR());
        assertEquals(3, f.size());

        f.remove(f.head().next());
        assertEquals("[7, 1]", f.toString());
        assertEquals("[1, 7]", f.toStringR());
        assertEquals(2, f.size());

        f.remove(f.head());
        assertEquals("[1]", f.toString());
        assertEquals("[1]", f.toStringR());
        assertEquals(1, f.size());

        f.append(7);
        assertEquals("[1, 7]", f.toString());
        assertEquals("[7, 1]", f.toStringR());
        assertEquals(2, f.size());

        f.remove(f.head().next());
        assertEquals("[1]", f.toString());
        assertEquals("[1]", f.toStringR());
        assertEquals(1, f.size());

        f.append(7);
        assertEquals("[1, 7]", f.toString());
        assertEquals("[7, 1]", f.toStringR());
        assertEquals(2, f.size());

        f.remove(f.tail());
        assertEquals("[1]", f.toString());
        assertEquals("[1]", f.toStringR());
        assertEquals(1, f.size());
    }

    @Test
    public void testinsertBefore() {
        // Initialize the list;
        CList<Integer> g= new CList<>();
        g.prepend(1);
        assertEquals("[1]", g.toString());
        assertEquals("[1]", g.toStringR());
        assertEquals(1, g.size());

        g.insertBefore(5, g.head());
        assertEquals("[5, 1]", g.toString());
        assertEquals("[1, 5]", g.toStringR());
        assertEquals(2, g.size());

        g.insertBefore(4, g.head().next());
        assertEquals("[5, 4, 1]", g.toString());
        assertEquals("[1, 4, 5]", g.toStringR());
        assertEquals(3, g.size());

        g.insertBefore(2, g.tail());
        assertEquals("[5, 4, 2, 1]", g.toString());
        assertEquals("[1, 2, 4, 5]", g.toStringR());
        assertEquals(4, g.size());

    }
}
