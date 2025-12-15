import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Test {

    /* * Work Distribution:
     * Aleksandr: BeesAlgorithm logic (runBA); Task 2.
     * Yana: Defined Location, Bounds, and Solution classes; Task 1.
     * Dominik:  LocationGenerator, performLocalSearch; Task 3; Output formatting and parameter tuning.
     */

    // --- 1. Define Concrete Functional Forms (f and c) ---

    // Functional style comment: This function determines the better of two results
    // by selecting the maximum value.
    private static final ComparisonFunction MAXIMIZER = (r1, r2) -> Math.max(r1, r2);
    private static final ComparisonFunction MINIMIZER = (r1, r2) -> Math.min(r1, r2);
    private static final ComparisonFunction ZEROS = (Double r1, Double r2) -> (Math.abs(r1) < Math.abs(r2) ? r1 : r2);

    // Functional style comment: This function maps a single-argument Location
    private static final ObjectiveFunction SINE_FUNCTION = (location) -> {
        double x_degrees = location.getCoordinates()[0];
        return Math.sin(Math.toRadians(x_degrees));
    };

    private static final ObjectiveFunction SQUARES = (location -> {
        double x1 = location.getCoordinates()[0];
        double x2 = location.getCoordinates()[1];
        double x3 = location.getCoordinates()[2];
        return Math.pow(x1 - 10, 2) * Math.pow(x2 - 200, 2) * Math.pow(x3 + 400, 2);
    });

    private static final ObjectiveFunction COSINE_SQUARED = (location -> {
        double x_degrees = location.getCoordinates()[0];
        return  Math.pow(Math.cos(Math.toRadians(x_degrees)), 2);});

//    private static final ObjectiveFunction SQUARES_FUNCTION = (Location location) -> {}

    // Task 1 Execution
    public static void runTask1() {
        System.out.println("### Task 1: Sine Maximization ###");

        // 20-60 second constraint requires efficient parameter tuning
        // Target: Maxima at 90° (+/- k*360°)

        int a = 1; // 1 parameter

        // Boundaries w: [-1800.0, 1800.0] (5 full cycles in each direction) [cite: 62]
        double[][] w_ranges = new double[][]{
                {-1800.0, 1800.0}
        };
        Bounds w = new Bounds(w_ranges);

        // Tuned BA Parameters
        int t = 3000;    // Search steps
        int n = 200;     // Scout bees
        int m = 30;     // Best fields
        int e = 10;      // Excellent fields
        int p = 30;     // Recruited for excellent fields
        int q = 50;     // Recruited for other fields
        double s = 0.9; // Relative size of the field
        int r = 10;     // 10 results to return

        List<Solution> results = BeesAlgorithm.runBA(
                a, SINE_FUNCTION, w, MAXIMIZER, t, n, m, e, p, q, s, r);

        // Output Results
        System.out.println("Goal: Find 10 Maxima (Expected Max: 1.0) in [-1800°, 1800°]");
        System.out.printf("Total iterations (t): %d. Total search budget: %d (n + e*p + (m-e)*q) calls per step.%n",
                t, n + e*p + (m-e)*q);

        System.out.println("\n| # | Location (Degrees) | Function Value |");
        System.out.println("|---|--------------------|----------------|");
        IntStream.range(0, results.size())
                .forEach(i -> {
                    Solution sol = results.get(i);
                    double angle = sol.getLocation().getCoordinates()[0];
                    System.out.printf("| %d | %18.4f | %14.8f |%n",
                            i + 1, angle, sol.getResult());
                });
        System.out.println("-------------------------------------------------\n");
    }

    // --- Placeholder for other tasks ---
    public static void runTask2() {
        System.out.println("### Task 2: Polynomial zeros ###");

        // 20-60 second constraint requires efficient parameter tuning
        // Target: Maxima at 90° (+/- k*360°)

        int a = 2; // 1 parameter

        // Boundaries w: [-100, 100] (5 full cycles in each direction) [cite: 62]
        double[][] w_ranges = new double[][]{
                {-1800.0, 1800.0},
                {-1800.0, 1800.0},
                {-1800.0, 1800.0}
        };
        Bounds w = new Bounds(w_ranges);

        // Tuned BA Parameters
        int t = 5000;    // Search steps
        int n = 100;     // Scout bees
        int m = 10;     // Best fields
        int e = 3;      // Excellent fields
        int p = 20;     // Recruited for excellent fields
        int q = 40;     // Recruited for other fields
        double s = 0.9; // Relative size of the field
        int r = 3;     // 10 results to return

        List<Solution> results = BeesAlgorithm.runBA(
                a, SQUARES, w, ZEROS, t, n, m, e, p, q, s, r);

        // Output Results
        System.out.println("Goal: Find 10 Maxima (Expected Max: 1.0) in [-1800°, 1800°]");
        System.out.printf("Total iterations (t): %d. Total search budget: %d (n + e*p + (m-e)*q) calls per step.%n",
                t, n + e*p + (m-e)*q);

        System.out.println("\n| # | --- Location --- | Function Value |");
        System.out.println("|---|--------------------|----------------|");
        IntStream.range(0, results.size())
                .forEach(i -> {
                    Solution sol = results.get(i);
                    double[] coordinates = sol.getLocation().getCoordinates();
                    System.out.printf("| %d | %s | %14.8f |%n",
                            i + 1, Arrays.toString(coordinates), sol.getResult());
                });
        System.out.println("-------------------------------------------------\n");
    }
    public static void runTask3() {

        System.out.println("### Task 3: Cosine squared ###");

        // 20-60 second constraint requires efficient parameter tuning
        // Target: Maxima at 90° (+/- k*360°)

        int a = 2; // 1 parameter

        // Boundaries w: [-100, 100] (5 full cycles in each direction) [cite: 62]
        double[][] w_ranges = new double[][]{
                {-1800.0, 1800.0},
                {-1800.0, 1800.0},
                {-1800.0, 1800.0}
        };
        Bounds w = new Bounds(w_ranges);

        // Tuned BA Parameters
        int t = 20000;    // Search steps
        int n = 100;     // Scout bees
        int m = 10;     // Best fields
        int e = 3;      // Excellent fields
        int p = 20;     // Recruited for excellent fields
        int q = 40;     // Recruited for other fields
        double s = 0.6; // Relative size of the field
        int r = 10;     // 10 results to return

        List<Solution> results = BeesAlgorithm.runBA(
                a, COSINE_SQUARED, w, MINIMIZER, t, n, m, e, p, q, s, r);

        // Output Results
        System.out.println("Goal: Find 10 Maxima (Expected Max: 1.0) in [-1800°, 1800°]");
        System.out.printf("Total iterations (t): %d. Total search budget: %d (n + e*p + (m-e)*q) calls per step.%n",
                t, n + e*p + (m-e)*q);

        System.out.println("\n| # | --- Location --- | Function Value |");
        System.out.println("|---|--------------------|----------------|");
        IntStream.range(0, results.size())
                .forEach(i -> {
                    Solution sol = results.get(i);
                    double coordinates = sol.getLocation().getCoordinates()[0];
                    System.out.printf("| %d | %18.4f | %.8f |%n",
                            i + 1, coordinates, sol.getResult());
                });
        System.out.println("-------------------------------------------------\n");
    }

    public static void main(String[] args) {

//        runTask1();
//         runTask2();
         runTask3();
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Execution Time: %.2f seconds", (endTime - startTime) / 1000.0));
    }
}