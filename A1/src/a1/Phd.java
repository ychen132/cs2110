package a1;

/** NetId: yc2636. Time spent: 4 hours, 15 minutes. <br>
 * What I thought about this assignment: cool and helpful <br>
 * <br>
 * An instance maintains info about the Phd of a person. */

public class Phd {
    /** Name of the phd. */
    private String name;
    /** year Phd was awarded */
    private int year;
    /** month Phd was awarded */
    private int month;

    /** first advisor */
    private Phd firstAdvisor;
    /** second advisor */
    private Phd secondAdvisor;
    /** Number of advisees */
    private int numadv;

    /* Group A */
    /** Constructor: an instance for a person with name n, Phd year y, Phd month m. <br>
     * Its advisors are unknown, and it has no advisees.<br>
     * Precondition: n has at least 1 char, y > 1000, and m is in 1..12 */
    public Phd(String n, int y, int m) {
        assert n != null && n.length() >= 1;
        assert y > 1000;
        assert m > 0 && m < 13;
        name= n;
        year= y;
        month= m;
        firstAdvisor= null;
        secondAdvisor= null;
        numadv= 0;
    }

    /** Return name of the Phd */
    public String name() {
        return name;
    }

    /** Return the date on which this person got the Phd. In the form "month/year" */
    public String date() {
        String s= String.valueOf(month) + "/" + String.valueOf(year);
        return s;
    }

    /** Return the first advisor of the Phd */
    public Phd advisor1() {
        return firstAdvisor;
    }

    /** Return the second advisor of the Phd */
    public Phd advisor2() {
        return secondAdvisor;
    }

    /** Return the number of Phd advisees */
    public int nAdvisees() {
        return numadv;
    }

    /* Group b*/
    /** Make p the first advisor of this person.<br>
     * Precondition: the first advisor is unknown and p is not null. */
    public void setAdvisor1(Phd p) {
        assert firstAdvisor == null;
        assert p != null;
        firstAdvisor= p;
        p.numadv++ ;
    }

    /** Make p the second advisor of this person.<br>
     * Precondition: The first advisor (of this person) is known, the second advisor is unknown, p
     * is not null, and p is different from the first advisor. */
    public void setAdvisor2(Phd p) {
        assert firstAdvisor != null && secondAdvisor == null;
        assert p != null && p != firstAdvisor;
        secondAdvisor= p;
        p.numadv++ ;
    }

    /* Group c*/
    /** Constructor: a Phd with name n, Phd year y, Phd month m, first advisor a1, and second
     * advisor a2.<br>
     * Precondition: n has at least 1 char, y > 1000, m is in 1..12,a1 and a2 are not null, and a1
     * and a2 are different. */
    public Phd(String n, int y, int m, Phd a1, Phd a2) {
        assert n != null && n.length() >= 1;
        assert y > 1000;
        assert m >= 1 && m <= 12;
        assert a1 != null && a2 != null;
        assert a1 != a2;

        name= n;
        year= y;
        month= m;
        firstAdvisor= a1;
        secondAdvisor= a2;
        numadv= 0;
        a1.numadv++ ;
        a2.numadv++ ;
    }

    /*Group d*/
    /** Return whether the Phd has advisees or not */
    public boolean hasNoAdvisees() {
        return numadv == 0;
    }

    /** Return p is not null and this person got the Phd before p" */
    public boolean gotBefore(Phd p) {
        assert p != null;
        return year < p.year || year == p.year && month < p.month;
    }

    /** Return if this person and p are intellectual siblings */
    public boolean areSibs(Phd p) {
        assert p != null;
        return this != p && (firstAdvisor != null && (firstAdvisor == p.firstAdvisor ||
            firstAdvisor == p.secondAdvisor) ||
            secondAdvisor != null && (secondAdvisor == p.firstAdvisor ||
                secondAdvisor == p.secondAdvisor));
    }

}
