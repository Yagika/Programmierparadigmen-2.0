import Architecture.Classes.*;
import Architecture.interfaces.*;

import java.util.Date;


/**
 * - Bee-Typen (Honeybee, Bumblebee, ...) sind keine FlowerFly: unterschiedliche Insektenordnungen.
 * - FlowerFly ist kein WildBee: WildBee bezieht sich nur auf Wildbienen, nicht auf andere Pollinatoren.
 * - SocialBee ist kein WildBee: soziale Arten können, müssen aber nicht Wildbienen sein (z.B. Honeybee).
 */
public class Test4 {
    /**
     * Kurzbeschreibung der Aufgabenaufteilung:
     * - Dominik: Grundstruktur der Interfaces und Obertypen (Observation, Wasp, Bee, Pollinator, WildBee, ...)
     * - Aleksandr: konkrete Bienenklassen (Honeybee, Bumblebee, OsmiaCornuta, LasioglossumCalceatum, AndrenaBucephala)
     * - Yana: FlowerFly, Iteratormethoden (Stub-Implementierungen), Testfälle in dieser Klasse.
     */
    public static void main(String[] args) {
        Date now = new Date();

        // --- Beobachtungen anlegen ---
        Observation o1 = new Honeybee("Honigbiene im свStock", now, 900);
        Observation o2 = new Bumblebee("Hummel an Blüte", now, 905);
        Observation o3 = new OsmiaCornuta("Mauerbiene an Krokus", now, 910);
        Observation o4 = new LasioglossumCalceatum("Lasioglossum sozial", now, 915, true, false);
        Observation o5 = new LasioglossumCalceatum("Lasioglossum solitär", now, 920, false, false);
        Observation o6 = new AndrenaBucephala("Andrena communal", now, 925, true, false, false);
        Observation o7 = new FlowerFly("Schwebfliege", now, 930);

        System.out.println("=== Alle Beobachtungen als Observation ===");
        printObservation(o1);
        printObservation(o2);
        printObservation(o3);
        printObservation(o4);
        printObservation(o5);
        printObservation(o6);
        printObservation(o7);

        System.out.println("\n=== Pollinator-Ersetzbarkeit ===");
        Pollinator p1 = (Pollinator) o1; // Honeybee
        Pollinator p2 = (Pollinator) o2; // Bumblebee
        Pollinator p3 = (Pollinator) o3; // OsmiaCornuta
        Pollinator p4 = (Pollinator) o7; // FlowerFly
        printObservation(p1);
        printObservation(p2);
        printObservation(p3);
        printObservation(p4);

        System.out.println("\n=== SocialBee-Ersetzbarkeit ===");
        SocialBee s1 = (SocialBee) o1;   // Honeybee
        SocialBee s2 = (SocialBee) o2;   // Bumblebee
        SocialBee s3 = (SocialBee) o4;   // LasioglossumCalceatum (sozial)
        printSocial(s1);
        printSocial(s2);
        printSocial(s3);

        System.out.println("\n=== SolitaryBee / CommunalBee / WildBee-Ersetzbarkeit ===");
        SolitaryBee sol1 = (SolitaryBee) o3; // OsmiaCornuta
        SolitaryBee sol2 = (SolitaryBee) o5; // LasioglossumCalceatum (solitär)
        CommunalBee c1  = (CommunalBee) o6;  // AndrenaBucephala (kommunal)
        WildBee w1      = (WildBee) o2;      // Bumblebee
        WildBee w2      = (WildBee) sol1;    // OsmiaCornuta ist über SolitaryBee auch WildBee

        printSolitary(sol1);
        printSolitary(sol2);
        printCommunal(c1);
        printWild(w1);
        printWild(w2);

        System.out.println("\n=== sameBee() Tests (Iteratoren sind nie null) ===");
        testSameBee((Bee) o1);
        testSameBee((Bee) o2);
        testSameBee((Bee) o3);

        System.out.println("\n=== remove() / valid() Test ===");
        System.out.println("Vor remove(): o1 valid=" + o1.valid());
        o1.remove();
        System.out.println("Nach remove(): o1 valid=" + o1.valid());
        System.out.println("Andere Beobachtungen bleiben gültig: o2 valid=" + o2.valid());

        System.out.println("\n=== earlier() / later() Iteratoren sind nicht null ===");
        System.out.println("o2.earlier() == null ? " + (o2.earlier() == null));
        System.out.println("o2.later()   == null ? " + (o2.later() == null));
        System.out.println("hasNext() auf earlier(): " + o2.earlier().hasNext());
        System.out.println("hasNext() auf later():   " + o2.later().hasNext());

    }

    private static void printObservation(Observation o) {
        System.out.println(String.format(
                "%-22s @ %s %4d : %-30s valid=%s",
                o.getClass().getSimpleName(),
                o.getDate(),
                o.getTime(),
                o.getDescription(),
                o.valid()
        ));
    }

    private static void printSocial(SocialBee s) {
        System.out.println(s.getClass().getSimpleName()
                + " ist SocialBee, social().hasNext() = " + s.social().hasNext());
    }

    private static void printSolitary(SolitaryBee s) {
        System.out.println(s.getClass().getSimpleName()
                + " ist SolitaryBee, solitary().hasNext() = " + s.solitary().hasNext());
    }

    private static void printCommunal(CommunalBee c) {
        System.out.println(c.getClass().getSimpleName()
                + " ist CommunalBee, communal().hasNext() = " + c.communal().hasNext());
    }

    private static void printWild(WildBee w) {
        System.out.println(w.getClass().getSimpleName()
                + " ist WildBee, wild(false).hasNext() = " + w.wild(false).hasNext());
    }

    private static void testSameBee(Bee b) {
        System.out.println(b.getClass().getSimpleName()
                + " sameBee().hasNext()       = " + b.sameBee().hasNext());
        System.out.println(b.getClass().getSimpleName()
                + " sameBee(true).hasNext()   = " + b.sameBee(true).hasNext());
        System.out.println(b.getClass().getSimpleName()
                + " sameBee(now,now).hasNext()= "
                + b.sameBee(new Date(), new Date()).hasNext());
    }
}