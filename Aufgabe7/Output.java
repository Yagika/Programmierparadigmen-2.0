//import java.util.*;
//
///**
// * Handles all observable side effects (console output).
// *
// * This class marks the boundary between functional computation
// * and imperative interaction with the environment.
// */
//public final class Output {
//
//    private Output() {}
//
//    /** Prints a section header. */
//    public static void header(String title) {
//        System.out.println("\n=== " + title + " ===");
//    }
//
//    /** Prints points together with their objective values. */
//    public static void points(List<BeesAlgorithm.Cand> xs) {
//        for (int i = 0; i < xs.size(); i++) {
//            System.out.printf(
//                    Locale.US,
//                    "%2d) x=%s   f(x)=%.10f%n",
//                    i + 1,
//                    Arrays.toString(xs.get(i).x()),
//                    xs.get(i).fx()
//            );
//        }
//    }
//}
