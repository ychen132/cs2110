package a1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PhdTest {

    @Test
    public void testConstructor1() {
        Phd a= new Phd("John Snow", 1820, 7);
        Phd b= new Phd("Sir Allen", 1940, 6);
        assertEquals("John Snow", a.name());
        assertEquals("Sir Allen", b.name());
        assertEquals("7/1820", a.date());
        assertEquals("6/1940", b.date());
        assertEquals(null, a.advisor1());
        assertEquals(null, a.advisor2());
        assertEquals(null, b.advisor1());
        assertEquals(null, b.advisor2());
        assertEquals(0, a.nAdvisees());
        assertEquals(0, b.nAdvisees());
    }

    @Test
    public void testSetAdvisor() {
        Phd a= new Phd("John Snow", 1820, 8);
        Phd b= new Phd("Sir Allen", 1940, 6);
        Phd c= new Phd("Run Out of Name", 1820, 7);
        Phd d= new Phd("Hmmmm", 1940, 9);
        a.setAdvisor1(b);
        assertEquals(b, a.advisor1());
        assertEquals(1, b.nAdvisees());
        a.setAdvisor2(c);
        assertEquals(c, a.advisor2());
        assertEquals(1, c.nAdvisees());
        d.setAdvisor1(b);
        assertEquals(2, b.nAdvisees());

    }

    @Test
    public void testConstructor2() {
        Phd a= new Phd("John Snow", 1820, 7);
        Phd b= new Phd("Sir Allen", 1940, 6);
        Phd c= new Phd("John Cena", 1980, 12, a, b);
        assertEquals(c.name(), "John Cena");
        assertEquals(c.date(), "12/1980");
        assertEquals(c.advisor1(), a);
        assertEquals(c.advisor2(), b);
        assertEquals(c.nAdvisees(), 0);
        assertEquals(a.nAdvisees(), 1);
        assertEquals(b.nAdvisees(), 1);

    }

    @Test
    public void testGroupD() {
        Phd a= new Phd("John Snow", 1820, 8);
        Phd b= new Phd("Sir Allen", 1940, 8);
        Phd c= new Phd("Run Out of Name", 1820, 7);
        Phd d= new Phd("Hmmmm", 1940, 9);
        Phd e= new Phd("what", 2000, 8);
        Phd f= new Phd("So many testing", 1820, 9);
        Phd g= new Phd("I dont know what to type", 1940, 5);
        Phd h= new Phd("dummy", 2020, 4);
        assertEquals(a.gotBefore(e), true);
        assertEquals(a.gotBefore(f), true);
        assertEquals(a.gotBefore(c), false);

        assertEquals(a.gotBefore(b), true);
        assertEquals(a.gotBefore(g), true);
        assertEquals(a.gotBefore(d), true);

        assertEquals(b.gotBefore(a), false);
        assertEquals(b.gotBefore(c), false);
        assertEquals(b.gotBefore(f), false);

        assertEquals(a.hasNoAdvisees(), true);
        assertEquals(a.areSibs(b), false);
        b.setAdvisor1(a);
        assertEquals(a.hasNoAdvisees(), false);

        c.setAdvisor1(a);
        assertEquals(b.areSibs(b), false);

        assertEquals(b.areSibs(c), true);

        b.setAdvisor2(g);
        d.setAdvisor1(h);
        d.setAdvisor2(g);
        assertEquals(b.areSibs(d), true);

        e.setAdvisor1(g);
        f.setAdvisor1(h);
        f.setAdvisor2(c);
        assertEquals(d.areSibs(e), true);
        assertEquals(c.areSibs(e), false);
        assertEquals(f.areSibs(b), false);

    }

}
