
import classes.*;
import interfaces.*;
import Sets.*;

import java.util.Iterator;

/**
 * Test class for Aufgabe 5.
 * Creates all required container instances, inserts elements,
 * copies order relations, checks constraints and calls all methods
 * mentioned in the task sheet.
 *
 * Group work distribution:
 * Yana: interfaces OrdSet, Ordered, Modifiable; classes ISet, OSet.
 * Dominik: MSet, Bee hierarchy (Bee, WildBee, HoneyBee), Num.
 * Aleksander: Test and helper methods.
 */
public class Test5 {

    public static void main(String[] args) {
        System.out.println("===== Programmierparadigmen - Aufgabe 5 Test =====");

        // ***************** 1 ******************
        System.out.println("\n--- 1) Creating containers and basic orders ---");

        // ISet
        ISet<Num> iNum = new ISet<>(null);
        ISet<Bee> iBee = new ISet<>(null);
        ISet<WildBee> iWildBee = new ISet<>(null);
        ISet<HoneyBee> iHoneyBee = new ISet<>(null);

        // OSet
        OSet<Num> oNum = new OSet<>(null);
        OSet<Bee> oBee = new OSet<>(null);
        OSet<WildBee> oWildBee = new OSet<>(null);
        OSet<HoneyBee> oHoneyBee = new OSet<>(null);

        // MSet
        MSet<Num, Num> mNum = new MSet<>(null);
        MSet<WildBee, Integer> mWildBee = new MSet<>(null);
        MSet<HoneyBee, String> mHoneyBee = new MSet<>(null);


        Num n1 = new Num(1);
        Num n2 = new Num(2);
        Num n3 = new Num(3);

        WildBee w1 = new WildBee("wild-1", 10);
        WildBee w2 = new WildBee("wild-2", 15);
        WildBee w3 = new WildBee("wild-3", 20);

        HoneyBee h1 = new HoneyBee("honey-1", "A");
        HoneyBee h2 = new HoneyBee("honey-2", "B");
        HoneyBee h3 = new HoneyBee("honey-3", "C");

        Bee b1 = w1;
        Bee b2 = h1;
        Bee b3 = new Bee("generic-bee");

        fillLinearOrder(iNum, n1, n2, n3);
        fillLinearOrder(oNum, n1, n2, n3);
        fillLinearOrder(mNum, n1, n2, n3);

        fillLinearOrder(iWildBee, w1, w2, w3);
        fillLinearOrder(oWildBee, w1, w2, w3);
        fillLinearOrder(mWildBee, w1, w2, w3);

        fillLinearOrder(iHoneyBee, h1, h2, h3);
        fillLinearOrder(oHoneyBee, h1, h2, h3);
        fillLinearOrder(mHoneyBee, h1, h2, h3);

        fillLinearOrder(iBee, b1, b2, b3);
        fillLinearOrder(oBee, b1, b2, b3);

        // sizes + elements
        printSize("iNum", iNum);
        printSize("oNum", oNum);
        printSize("mNum", mNum);

        printSize("iWildBee", iWildBee);
        printSize("oWildBee", oWildBee);
        printSize("mWildBee", mWildBee);

        printSize("iHoneyBee", iHoneyBee);
        printSize("oHoneyBee", oHoneyBee);
        printSize("mHoneyBee", mHoneyBee);

        printSize("iBee", iBee);
        printSize("oBee", oBee);

        // ***************** 2 ******************
        System.out.println("\n--- 2) Using a1,a2,b1,b2,c1,c2 and copying order relations ---");

        ISet<Bee> a1 = iBee;
        OSet<Bee> a2 = oBee;
        MSet<WildBee, Integer> b1Wild = mWildBee;
        MSet<HoneyBee, String> b2Honey = mHoneyBee;
        OSet<WildBee> c1 = oWildBee;
        ISet<HoneyBee> c2 = iHoneyBee;

        System.out.println("Elements in c1 (OSet<WildBee>):");
        for (WildBee w : c1) {
            System.out.println("  " + w + ", length=" + w.length());
        }
        copyRelations(c1, a1);
        copyRelations(c1, b1Wild);

        System.out.println("Elements in c2 (ISet<HoneyBee>):");
        for (HoneyBee h : c2) {
            System.out.println("  " + h + ", sort=" + h.sort());
        }
        copyRelations(c2, a2);
        copyRelations(c2, b2Honey);

        // ***************** 3 ******************
        System.out.println("\n--- 3) check / checkForced compatibility tests ---");

        System.out.println("Using ISet<Num> and MSet<Num,Num> as constraints for OSet<Num>:");
        oNum.check(iNum);
        oNum.checkForced(mNum);

        System.out.println("Using ISet<Bee> as constraint for ISet<WildBee> and OSet<HoneyBee>:");
        iWildBee.check(iBee);
        oHoneyBee.checkForced(iBee);

        // ***************** 4 ******************
        System.out.println("\n--- 4) Subtype relationships / explanations ---");
        explainSubtyping();

        // ***************** 5/6 ******************
        System.out.println("\n--- 5) Exercising all methods ---");
        exerciseAllMethods(iNum, oNum, mNum,
                iWildBee, oWildBee, mWildBee,
                iHoneyBee, oHoneyBee, mHoneyBee,
                iBee, oBee);

        System.out.println("\n===== End of tests =====");
    }

    /**
     * Print container size and all elements via iterator.
     */
    private static <E, R> void printSize(String name, OrdSet<E, R> set) {
        System.out.println(name + " size = " + set.size());
        System.out.print(name + " elements: ");
        boolean first = true;
        for (E e : set) {
            if (!first) {
                System.out.print(", ");
            }
            first = false;
            System.out.print(e);
        }
        System.out.println();
    }

    /**
     * Create a simple linear order e0 < e1 < ... < en-1.
     * Elements are inserted automatically by setBefore.
     */
    @SafeVarargs
    private static <E, R> void fillLinearOrder(OrdSet<E, R> set, E... elements) {
        for (int i = 0; i + 1 < elements.length; i++) {
            set.setBefore(elements[i], elements[i + 1]);
        }
    }

    /**
     * Copy all order relations from source to target.
     * source element type S, target element type T, with S extends T.
     */
    private static <T, S extends T, R1, R2> void copyRelations(
            OrdSet<S, R1> source,
            OrdSet<T, R2> target) {

        for (S x : source) {
            for (S y : source) {
                if (x == y) {
                    continue;
                }
                R1 res = source.before(x, y);
                if (res != null) {
                    target.setBefore(x, y);
                }
            }
        }
    }

    // ***************** 4 ******************
    /**
     * Point 4: explain subtype relationships between ISet, OSet and MSet.
     *
     * In this implementation there are no subtype relationships between
     * ISet<E>, OSet<E> and MSet<E,X>, because:
     * - they use different result types R for before(E,E)
     *   (Iterator<E> vs Slice<E>),
     * - MSet has extra operations plus/minus that do not exist in ISet/OSet,
     * - a common supertype would require incompatible generic signatures.
     *
     * This is printed to the console instead of being encoded in the type
     * hierarchy.
     */

    private static void explainSubtyping() {
        System.out.println("There are no subtype relationships between ISet, OSet and MSet.");
        System.out.println("Reason: they use different result types R for before(E,E),");
        System.out.println("and they have different additional operations (plus/minus only on MSet).");
        System.out.println("A subtype relation would break the required generic method signatures.");
    }

    // ***************** 5/6 ******************

    private static void exerciseAllMethods(
            ISet<Num> iNum, OSet<Num> oNum, MSet<Num, Num> mNum,
            ISet<WildBee> iWildBee, OSet<WildBee> oWildBee, MSet<WildBee, Integer> mWildBee,
            ISet<HoneyBee> iHoneyBee, OSet<HoneyBee> oHoneyBee, MSet<HoneyBee, String> mHoneyBee,
            ISet<Bee> iBee, OSet<Bee> oBee) {

        System.out.println("iNum.before(first, last):");
        Num firstNum = null;
        Num lastNum = null;
        for (Num n : iNum) {
            if (firstNum == null) {
                firstNum = n;
            }
            lastNum = n;
        }
        if (firstNum != null && lastNum != null && firstNum != lastNum) {
            Iterator<Num> between = iNum.before(firstNum, lastNum);
            if (between != null) {
                while (between.hasNext()) {
                    System.out.println("  between: " + between.next());
                }
            }
        }

        System.out.println("oNum.before(first, last):");
        OSet.Slice<Num> numSlice = oNum.before(firstNum, lastNum);
        if (numSlice != null) {
            Boolean rel = numSlice.before(firstNum, lastNum);
            System.out.println("  slice.before(first, last) = " + rel);
        }

        System.out.println("Applying plus/minus on mNum:");
        mNum.plus(new Num(10));
        mNum.minus(new Num(5));

        System.out.println("Applying plus/minus on mWildBee:");
        mWildBee.plus(2);
        mWildBee.minus(1);

        System.out.println("Applying plus/minus on mHoneyBee:");
        mHoneyBee.plus("-X");
        mHoneyBee.minus("X");

        Num nx = new Num(5);
        System.out.println("Num add/subtract: " + nx.add(new Num(3)) + ", " + nx.subtract(new Num(2)));

        WildBee wx = new WildBee("test-wild", 12);
        System.out.println("WildBee add/subtract (length): "
                + wx.add(3).length() + ", " + wx.subtract(2).length());

        HoneyBee hx = new HoneyBee("test-honey", "Q");
        System.out.println("HoneyBee add/subtract (sort): "
                + hx.add("-R").sort() + ", " + hx.subtract("R").sort());

        System.out.println("check/checkForced on iWildBee with iBee as constraint:");
        iWildBee.check(iBee);
        iWildBee.checkForced(iBee);

        System.out.println("check/checkForced on oHoneyBee with iBee as constraint:");
        oHoneyBee.check(iBee);
        oHoneyBee.checkForced(iBee);

        System.out.println("Iterating over all containers (Bee-related):");
        printSize("iBee", iBee);
        printSize("oBee", oBee);
        printSize("iWildBee", iWildBee);
        printSize("oWildBee", oWildBee);
        printSize("iHoneyBee", iHoneyBee);
        printSize("oHoneyBee", oHoneyBee);
    }
}
