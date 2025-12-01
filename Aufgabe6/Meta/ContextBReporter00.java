package Meta;

import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runtime reporter for Context B.
 * Uses reflection to extract program components, responsibilities and assertions.
 */
@Responsible("Yana")
public class ContextBReporter00 {

    public static void printReport(Class<?>[] components) {
        System.out.println("===== Kontext B: Datenextraktion =====");

        // 1. Namen aller Klassen/Interfaces/Annotationen
        System.out.println("\n-- (1) Programmbestandteile --");
        for (Class<?> c : components) {
            String kind = c.isAnnotation() ? "Annotation"
                    : (c.isInterface() ? "Interface" : "Klasse");
            System.out.println(kind + ": " + c.getName());
        }

        // 2. Zuordnung Name -> verantwortliche Person
        System.out.println("\n-- (2) Verantwortliche --");
        Map<String, MemberStats> perMember = new LinkedHashMap<>();
        for (Class<?> c : components) {
            String responsible = getResponsibleName(c);
            System.out.println(c.getName() + " -> " + responsible);
            if (responsible != null) {
                MemberStats s = perMember.get(responsible);
                if (s == null) {
                    s = new MemberStats();
                    perMember.put(responsible, s);
                }
                s.incComponents();
            }
        }

        // 3. Signaturen und Zusicherungen
        System.out.println("\n-- (3) Signaturen und Zusicherungen --");
        for (Class<?> c : components) {
            String kind = c.isAnnotation() ? "Annotation"
                    : (c.isInterface() ? "Interface" : "Klasse");
            System.out.println("\n" + kind + " " + c.getName());

            // Invarianten / History-Constraints (inkl. von Obertypen)
            printTypeAssertions(c);

            // nur für Klassen und Interfaces Methoden, für Klassen zusätzlich Konstruktoren
            if (!c.isAnnotation()) {
                printConstructorsAndAssertions(c, perMember);
                printMethodsAndAssertions(c, perMember);
            }
        }

        // 4–6. Statistiken pro Gruppenmitglied
        System.out.println("\n-- (4–6) Statistiken pro Gruppenmitglied --");
        for (Map.Entry<String, MemberStats> e : perMember.entrySet()) {
            String member = e.getKey();
            MemberStats s = e.getValue();
            System.out.println("Mitglied: " + member);
            System.out.println("  (4) Anzahl Klassen/Interfaces/Annotationen: " + s.components);
            System.out.println("  (5) Anzahl Methoden+Konstruktoren (nur Klassen): " + s.methodsAndConstructors);
            System.out.println("  (6) Anzahl Zusicherungen: " + s.assertions);
        }
    }

    private static String getResponsibleName(Class<?> c) {
        Responsible r = c.getAnnotation(Responsible.class);
        return r != null ? r.value() : null;
    }

    private static void printTypeAssertions(Class<?> c) {
        System.out.println("  Typ-Zusicherungen:");
        // eigene
        Invariant inv = c.getAnnotation(Invariant.class);
        if (inv != null) {
            System.out.println("    Invariant (eigene): " + inv.value());
        }
        HistoryConstraint hc = c.getAnnotation(HistoryConstraint.class);
        if (hc != null) {
            System.out.println("    HistoryConstraint (eigene): " + hc.value());
        }

        // von Oberklassen / Interfaces
        Class<?> sup = c.getSuperclass();
        while (sup != null) {
            Invariant invSup = sup.getAnnotation(Invariant.class);
            if (invSup != null) {
                System.out.println("    Invariant (geerbt von " + sup.getName() + "): " + invSup.value());
            }
            HistoryConstraint hcSup = sup.getAnnotation(HistoryConstraint.class);
            if (hcSup != null) {
                System.out.println("    HistoryConstraint (geerbt von " + sup.getName() + "): " + hcSup.value());
            }
            sup = sup.getSuperclass();
        }

        Class<?>[] ifs = c.getInterfaces();
        for (Class<?> ic : ifs) {
            Invariant invI = ic.getAnnotation(Invariant.class);
            if (invI != null) {
                System.out.println("    Invariant (geerbt von " + ic.getName() + "): " + invI.value());
            }
            HistoryConstraint hcI = ic.getAnnotation(HistoryConstraint.class);
            if (hcI != null) {
                System.out.println("    HistoryConstraint (geerbt von " + ic.getName() + "): " + hcI.value());
            }
        }
    }

    private static void printConstructorsAndAssertions(Class<?> c, Map<String, MemberStats> perMember) {
        if (c.isInterface()) return; // Interfaces haben keine Konstruktoren

        Constructor<?>[] ctors = c.getDeclaredConstructors();
        if (ctors.length == 0) return;

        String responsible = getResponsibleName(c);
        MemberStats stats = responsible != null ? perMember.get(responsible) : null;

        System.out.println("  Konstruktoren:");
        for (Constructor<?> ctor : ctors) {
            System.out.println("    " + signatureOf(ctor));

            Precondition pre = ctor.getAnnotation(Precondition.class);
            if (pre != null) {
                System.out.println("      Pre:  " + pre.value());
                if (stats != null) stats.addAssertions(1);
            }
            Postcondition post = ctor.getAnnotation(Postcondition.class);
            if (post != null) {
                System.out.println("      Post: " + post.value());
                if (stats != null) stats.addAssertions(1);
            }

            if (stats != null) {
                stats.addMethodsAndConstructors(1);
            }
        }
    }

    private static void printMethodsAndAssertions(Class<?> c, Map<String, MemberStats> perMember) {
        Method[] methods = c.getDeclaredMethods();
        if (methods.length == 0) return;

        String responsible = getResponsibleName(c);
        MemberStats stats = responsible != null ? perMember.get(responsible) : null;

        System.out.println("  Methoden:");
        for (Method m : methods) {
            System.out.println("    " + signatureOf(m));

            // eigene Zusicherungen
            Precondition pre = m.getAnnotation(Precondition.class);
            if (pre != null) {
                System.out.println("      Pre (eigene):  " + pre.value());
                if (stats != null) stats.addAssertions(1);
            }
            Postcondition post = m.getAnnotation(Postcondition.class);
            if (post != null) {
                System.out.println("      Post (eigene): " + post.value());
                if (stats != null) stats.addAssertions(1);
            }

            // geerbte Zusicherungen aus Oberklassen/Interfaces
            printInheritedMethodAssertions(c, m);

            if (stats != null) {
                stats.addMethodsAndConstructors(1);
            }
        }
    }

    private static void printInheritedMethodAssertions(Class<?> c, Method m) {
        Class<?> sup = c.getSuperclass();
        while (sup != null) {
            try {
                Method sm = sup.getDeclaredMethod(m.getName(), m.getParameterTypes());
                Precondition pre = sm.getAnnotation(Precondition.class);
                if (pre != null) {
                    System.out.println("      Pre (geerbt von " + sup.getName() + "): " + pre.value());
                }
                Postcondition post = sm.getAnnotation(Postcondition.class);
                if (post != null) {
                    System.out.println("      Post (geerbt von " + sup.getName() + "): " + post.value());
                }
            } catch (NoSuchMethodException ignore) {
                // kein gleicher Methodenkopf
            }
            sup = sup.getSuperclass();
        }

        Class<?>[] ifs = c.getInterfaces();
        for (Class<?> ic : ifs) {
            try {
                Method im = ic.getDeclaredMethod(m.getName(), m.getParameterTypes());
                Precondition pre = im.getAnnotation(Precondition.class);
                if (pre != null) {
                    System.out.println("      Pre (geerbt von " + ic.getName() + "): " + pre.value());
                }
                Postcondition post = im.getAnnotation(Postcondition.class);
                if (post != null) {
                    System.out.println("      Post (geerbt von " + ic.getName() + "): " + post.value());
                }
            } catch (NoSuchMethodException ignore) {
            }
        }
    }

    private static String signatureOf(Constructor<?> ctor) {
        StringBuilder sb = new StringBuilder();
        sb.append(ctor.getDeclaringClass().getSimpleName()).append("(");
        Class<?>[] params = ctor.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }

    private static String signatureOf(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getReturnType().getSimpleName())
                .append(" ")
                .append(m.getName())
                .append("(");
        Class<?>[] params = m.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }
}
