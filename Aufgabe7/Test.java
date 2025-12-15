import java.util.Comparator;
import java.util.function.ToDoubleFunction;

/**
 * Defines optimization problems and composes algorithm parameters.
 */
public class Test {

    /**
     * Work distribution:
     * - BeesAlgorithm: functional algorithm design and implementation – Yana
     * - Test: problem definition and parameter tuning – Dominik
     * - Output: side effect isolation and formatting – Aleksandr
     */
    public static void main(String[] args) {

        /* Task 1: sinus in degrees, maximize */
        Output.header("Sinus, maximize, [-1800,1800]");
        ToDoubleFunction<double[]> sinDeg =
                x -> Math.sin(Math.toRadians(x[0]));

        Output.points(
                BeesAlgorithm.run(
                        sinDeg,
                        Comparator.naturalOrder(),
                        new double[][]{{-1800, 1800}},
                        120, 60, 15, 5, 20, 7,
                        0.06, 10, 42L));

        /* Task 2: concentric rings, maximize */
        Output.header("Concentric rings, maximize");
        ToDoubleFunction<double[]> rings =
                x -> Math.cos(Math.hypot(x[0], x[1]));

        Output.points(
                BeesAlgorithm.run(
                        rings,
                        Comparator.naturalOrder(),
                        new double[][]{{-30, 30}, {-30, 30}},
                        160, 80, 20, 6, 25, 9,
                        0.08, 10, 7L));

        /* Task 3: polynomial roots, minimize |f(x)| */
        Output.header("Polynomial roots, minimize |f(x)|");
        ToDoubleFunction<double[]> poly =
                x -> (x[0] - 10) * (x[0] - 3)
                        * (x[0] + 7) * (x[0] + 15);

        Output.points(
                BeesAlgorithm.run(
                        poly,
                        Comparator.comparingDouble((Double d) -> Math.abs(d)).reversed(),
                        new double[][]{{-50, 50}},
                        140, 70, 18, 6, 22, 8,
                        0.05, 10, 2025L));
    }
}
