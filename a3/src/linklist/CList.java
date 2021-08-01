package linklist;

/*  Name(s):Yixie Chen
 * Netid(s):yc2636
 * What I thought about this assignment:
 * It is very helpful and fun to work on!
 *
 */

/** An instance is a circular doubly linked list. */
public class CList<E> {
    /** Replace "-1" by the time you spent on A3 in hours.<br>
     * Example: for 3 hours 15 minutes, use 3.25<br>
     * Example: for 4 hours 30 minutes, use 4.50<br>
     * Example: for 5 hours, use 5 or 5.0 */
    public static double timeSpent= 4.5;

    /** Fields head, tail and size implement a circular doubly linked list<br>
     * If the list is empty, head and tail are null.<br>
     *
     * Suppose the list is not empty. <br>
     * Then head points at the first node of the list, field next of each node points<br>
     * at the next node except that field next of the last node points at the first node.<br>
     *
     * Likewise, tail points at the last node of the list, field prev of each node<br>
     * points at the previous node but field prev of the first node points at the last node.<br>
     *
     * Field size contains the number of elements in the list. */
    private Node head, tail;

    /** Length of this circular linked list. */
    private int size;

    /** Constructor: an empty circular doubly linked list.. */
    public CList() {
        // Do not change this method.
    }

    /** = the length of this circular doubly linked list. <br>
     * This function takes constant time. */
    public int size() { // do not change this method
        return size;
    }

    /** = First node of this list (null if the list is empty). */
    public Node head() { // do not change this method
        return head;
    }

    /** = Last node of this list (null if the list is empty). */
    public Node tail() { // do not change this method
        return tail;
    }

    /** Return the data in node n of this list. <br>
     * Precondition: n is a node of this list; it may not be null. */
    public E data(Node n) { // do not change this method
        assert n != null;
        return n.data;
    }

    /** Return a representation of this circular linked list: its data, <br>
     * with adjacent ones separated by ", ", "[" at the beginning, and "]" at the end. <br>
     * Takes time proportional to the length of this list.<br>
     * E.g. for the list containing 3 7 8 in that order, the result is "[3, 7, 8]". <br>
     * E.g. for the list containing two empty strings, the result is "[, ]" */
    @Override
    public String toString() {
        if (head == null) return "[]";
        StringBuilder sb= new StringBuilder("[" + head.data);
        Node n= head.next;
        // inv: res contains data in nodes before node n (all of them if n = head),
        // with '[' at the beginning and ", " after each except for the last data item.
        while (n != head) {
            sb.append(", ");
            sb.append(n.data);
            n= n.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /** Return a representation of this circular linked list: its data in reverse order, <br>
     * with adjacent ones separated by ", ", "[" at the beginning, and "]" at the end. <br>
     * Takes time proportional to the length of this list. <br>
     * E.g. for the list containing 3 7 8 in that order, the result is "[8, 7, 3]". <br>
     * E.g. for the list containing two empty strings, the result is "[, ]". */
    public String toStringR() { // Note:
        // TODO 1. In writing this, do NOT use fields size and head and the next fields.
        // Use only field tail and the prev and data fields.
        // Use the same scheme as in toString.

        // You can't test this fully until #2, prepend, is written.
        // Extreme case to watch out for: E is String and data items are the empty string.
        if (tail == null) return "[]";
        StringBuilder sb= new StringBuilder("[" + tail.data);
        Node n= tail.prev;
        while (n != tail) {
            sb.append(", ");
            sb.append(n.data);
            n= n.prev;
        }
        sb.append("]");
        return sb.toString();
        // throw new UnsupportedOperationException();
    }

    /** Insert v at the beginning of the list. <br>
     * This operation takes constant time.<br>
     * E.g. if the list is [8, 7, 4], prepend(2) changes this list to [2, 8, 7, 4]. */
    public void prepend(E v) {
        // TODO 2. After writing this method, test this method and method
        // toStringR throughly before starting on the next method.
        // These two must be correct in order to be able to write and test all others.
        if (head == null) {
            head= new Node(null, v, null);
            head.prev= head;
            head.next= head;
            tail= head;
        } else {
            Node n= new Node(tail, v, head);
            head.prev= n;
            tail.next= n;
            head= n;
        }
        size++ ;
    }

    /** Change the head of this list to head.next and return the new head.<br>
     * Thus, the head becomes the tail.<br>
     * E.g. if the list is [] or [5], the list is unchanged.<br>
     * E.g. With this list is [5, 3, 4, 6], calling the method <br>
     * .... changes the list to [3, 4, 6, 5]. <br>
     * This method takes constant time. */
    public Node changeHeadToNext() {
        // TODO 3.
        if (head == null || head == tail) { return head; }
        head= head.next;
        tail= tail.next;
        return head;
        // throw new UnsupportedOperationException();
    }

    /** Add v to the end of this list. <br>
     * This operation takes constant time.<br>
     * E.g. if the list is [8, 7, 4], append(2) changes this list to [8, 7, 4, 2]. <br>
     * E.g. if the list is ["AB"], append(null) changes the list to ["AB", null]. */
    public void append(E v) {
        // TODO 4. After writing writing this method, test it thoroughly before
        // moving on to the next one.
        if (head == null) {
            head= new Node(null, v, null);
            head.next= head;
            head.prev= head;
            tail= head;
        } else {
            Node n= new Node(tail, v, head);
            tail.next= n;
            head.prev= n;
            tail= n;
        }
        size++ ;
    }

    /** Return node number h. <br>
     * Precondition: 0 <= h < size of the list. <br>
     * Note: If h is 0, return first node; if h = 1, return second node, ... */
    public Node getNode(int h) {
        // TODO 5. This method should take time proportional to min(h, size-h).
        // For example, if h < size/2, search from the beginning of the
        // list, otherwise search from the end of the list.
        // If h = size/2, search from either end; it doesn't matter.
        assert h >= 0;
        assert h < size;
        if (h < size / 2) {
            Node n= head;
            if (h == 0) { return head; }
            for (int i= 0; i < h; i++ ) {
                n= n.next;
            }
            return n;
        } else {
            Node n= tail;
            if (h == size - 1) { return tail; }
            for (int i= size - 1; i > h; i-- ) {
                n= n.prev;
            }
            return n;
        }

        // throw new UnsupportedOperationException();
    }

    /** Remove node n from this list. <br>
     * This operation must take constant time. <br>
     * Precondition: n must be a node of this list; it may not be null. */
    public void remove(Node n) {
        // TODO 6. Make sure this method takes constant time.
        assert size != 0 && n != null;
        if (head == n && size == 1) {
            head= null;
            tail= null;
        } else if (head == n && size > 1) {
            head= head.next;
            tail.next= head;
            head.prev= tail;
        } else if (tail == n) {
            tail.prev.next= head;
            head.prev= tail.prev;
            tail= tail.prev;
        } else {
            n.prev.next= n.next;
            n.next.prev= n.prev;
        }
        size-- ;
    }

    /** Insert v in a new node before node n. <br>
     * This operation takes constant time. <br>
     * Precondition: n must be a node of this list; it may not be null.<br>
     * E.g. if the list is [3, 8, 2], n points to the node with 8 in it, <br>
     * and v is 1, the list is changed to [3, 1, 8, 2] */
    public void insertBefore(E v, Node n) {
        // TODO 7. Make sure this method takes constant time.
        assert n != null && size != 0;
        if (n == head) {
            Node add= new Node(tail, v, head);
            tail.next= add;
            head.prev= add;
            head= add;

        } else {
            Node add= new Node(n.prev, v, n);
            n.prev.next= add;
            n.prev= add;
        }
        size++ ;
    }

    /*********************/

    /** An instance is a node of this list. */
    public class Node {
        private Node prev; // Previous node on list (tail if this is the first node).
        private E data;    // The data in this node
        private Node next; // Next node on list (head if this is the last node).

        /** Constructor: an instance with previous node p (can be null), data v, and <br>
         * next node s (can be null). */
        Node(Node p, E v, Node s) {
            prev= p;
            data= v;
            next= s;
        }

        /** = the previous node in the list (tail if this is the first node of the list). */
        public Node prev() {
            return prev;
        }

        /** = the data in this node. */
        public E data() {
            return data;
        }

        /** = the next node in this list (head if this is the last node of this list). */
        public Node next() {
            return next;
        }
    }
}
