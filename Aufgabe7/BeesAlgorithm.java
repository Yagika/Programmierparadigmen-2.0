import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Functional formulation of the Bees Algorithm.
 * <p>
 * The algorithm is expressed as transformations of candidate populations.
 * Search behavior emerges from function composition rather than object interaction.
 */
public final class BeesAlgorithm {

    private BeesAlgorithm() {
    }

    /**
     * A candidate solution consisting of a point in the search space
     * together with its evaluated objective value.
     * <p>
     * This record represents an immutable observation of the objective function.
     */
    public record Cand(double[] x, double fx) {
        public Cand {
            x = Arrays.copyOf(x, x.length);
        }
    }
    /**
     * Immutable state of a local search patch.
     * <p>
     * It captures the currently best known candidate,
     * the bounds of the patch, and the number of consecutive
     * iterations without improvement.
     */
    private record PatchState(
            Cand best,
            double[][] bounds,
            int stagnation
    ) {
    }

    /**
     * Executes the Bees Algorithm for a given optimization problem.
     * <p>
     * The algorithm alternates between global exploration
     * and localized exploitation of promising regions.
     *
     * @return the r the best candidates found during the search
     */
    public static List<Cand> run(
            ToDoubleFunction<double[]> f,
            Comparator<Double> valueCmp,
            double[][] bounds,
            int t, int n, int m, int e, int p, int q,
            double s,
            int r,
            long seed
    ) {

        Comparator<Cand> candCmp =
                (a, b) -> valueCmp.compare(a.fx(), b.fx());

        SplittableRandom rng = new SplittableRandom(seed);

        List<Cand> archive = List.of();

        final int STAGNATION_LIMIT = 4;
        final double SHRINK_FACTOR = 0.6;

        for (int step = 0; step < t; step++) {

            List<Cand> scouts =
                    sample(n, () -> eval(f, randomPoint(bounds, rng)));

            List<Cand> selected =
                    scouts.stream()
                            .sorted(candCmp.reversed())
                            .limit(m)
                            .toList();

            List<Cand> local =
                    IntStream.range(0, selected.size())
                            .mapToObj(i -> {

                                Cand center = selected.get(i);
                                int recruits = (i < e) ? p : q;

                                PatchState initial =
                                        new PatchState(
                                                center,
                                                patch(bounds, center.x(), s),
                                                0);

                                return iteratePatch(
                                        initial,
                                        recruits,
                                        f,
                                        candCmp,
                                        rng,
                                        STAGNATION_LIMIT,
                                        SHRINK_FACTOR
                                );
                            })
                            .toList();

            List<Cand> global =
                    sample(n - m, () -> eval(f, randomPoint(bounds, rng)));

            archive =
                    Stream.concat(archive.stream(),
                                    Stream.concat(local.stream(), global.stream()))
                            .sorted(candCmp.reversed())
                            .limit(r)
                            .collect(Collectors.toList());
        }

        return archive;
    }

    /* ---------- Local patch iteration ---------- */

    /**
     * Iteratively explores a local search patch.
     * <p>
     * The patch is shrunk around improving candidates.
     * If no improvement is observed for a fixed number of iterations,
     * the patch is abandoned.
     */
    private static Cand iteratePatch(
            PatchState state,
            int recruits,
            ToDoubleFunction<double[]> f,
            Comparator<Cand> cmp,
            SplittableRandom rng,
            int stagnationLimit,
            double shrink
    ) {
        PatchState current = state;

        while (current.stagnation < stagnationLimit) {

            final Cand best = current.best;
            final double[][] bounds = current.bounds;

            Cand bestLocal =
                    bestOf(
                            Stream.concat(
                                    Stream.of(best),
                                    sampleStream(recruits,
                                            () -> eval(f, randomPoint(bounds, rng)))),
                            cmp);

            boolean improved =
                    cmp.compare(bestLocal, best) > 0;

            current =
                    improved
                            ? new PatchState(
                            bestLocal,
                            shrinkPatch(bounds, bestLocal.x(), shrink),
                            0)
                            : new PatchState(
                            best,
                            bounds,
                            current.stagnation + 1);
        }

        return current.best;
    }

    /* ---------- Functional helpers ---------- */

    /**
     * Evaluates the objective function at a given point.
     * <p>
     * The result is captured as an immutable candidate.
     */
    private static Cand eval(ToDoubleFunction<double[]> f, double[] x) {
        return new Cand(x, f.applyAsDouble(x));
    }

    /**
     * Generates a finite list of elements using a supplier.
     * <p>
     * This represents repeated independent sampling.
     */
    private static <T> List<T> sample(int k, Supplier<T> s) {
        return sampleStream(k, s).collect(Collectors.toList());
    }

    /**
     * Stream-based variant of sampling.
     * <p>
     * Used to compose sampling with further stream operations.
     */
    private static <T> Stream<T> sampleStream(int k, Supplier<T> s) {
        return IntStream.range(0, k).mapToObj(i -> s.get());
    }

    /**
     * Selects the best element of a stream according to a comparator.
     * <p>
     * The stream is assumed to be finite and non-empty.
     */
    private static <T> T bestOf(Stream<T> xs, Comparator<T> cmp) {
        return xs.max(cmp).orElseThrow();
    }

    /**
     * Draws a uniformly random point from the given bounds.
     * <p>
     * Each dimension is sampled independently.
     */
    private static double[] randomPoint(double[][] bounds, SplittableRandom rng) {
        double[] x = new double[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            x[i] = bounds[i][0]
                    + rng.nextDouble() * (bounds[i][1] - bounds[i][0]);
        }
        return x;
    }

    /**
     * Builds a local search patch around a center point.
     * <p>
     * The patch size is defined relative to the global bounds.
     */
    private static double[][] patch(double[][] world, double[] center, double s) {
        double ss = Math.max(0.0, Math.min(1.0, s));
        double[][] b = new double[world.length][2];

        for (int i = 0; i < world.length; i++) {
            double radius = (world[i][1] - world[i][0]) * ss / 2.0;
            b[i][0] = Math.max(world[i][0], center[i] - radius);
            b[i][1] = Math.min(world[i][1], center[i] + radius);
        }
        return b;
    }


    /**
     * Shrinks an existing patch around a new center.
     * <p>
     * This concentrates the search on increasingly promising regions.
     */
    private static double[][] shrinkPatch(
            double[][] current,
            double[] center,
            double factor
    ) {
        double[][] b = new double[current.length][2];

        for (int i = 0; i < current.length; i++) {
            double width = current[i][1] - current[i][0];
            double radius = width * factor / 2.0;

            double lo = center[i] - radius;
            double hi = center[i] + radius;

            b[i][0] = Math.max(current[i][0], lo);
            b[i][1] = Math.min(current[i][1], hi);

            if (b[i][0] > b[i][1]) {
                double mid = (current[i][0] + current[i][1]) / 2.0;
                b[i][0] = mid;
                b[i][1] = mid;
            }
        }
        return b;
    }
}
