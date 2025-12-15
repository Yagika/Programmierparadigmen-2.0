import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BeesAlgorithm {

    private static final Random RANDOM = new Random();
    private static final LocationGenerator LOCATION_GENERATOR = LocationGenerator.create();

    public static List<Solution> runBA(
            int a, ObjectiveFunction f, Bounds w, ComparisonFunction c,
            int t, int n, int m, int e, int p, int q, double s, int r)
    {
        // Stores all the best solutions found throughout all iterations.

        Comparator<Solution> solutionComparator = (sol1, sol2) -> {
            double r1 = sol1.getResult();
            double r2 = sol2.getResult();
            double betterResult = c.apply(r1, r2);

            if (betterResult == r1 && betterResult == r2) {
                // Case 1: Both results are numerically equal, or the comparison function
                // determined they are equivalent (e.g., both 1.0 for MAXIMIZER).
                return 0;
            } else if (betterResult == r1) {
                // Case 2: r1 is strictly better than r2 (comes first in the sort)
                return -1;
            } else { // betterResult == r2
                // Case 3: r2 is strictly better than r1 (comes second in the sort)
                return 1;
            }
        };

        // Initial Scouting (Global Search)
        // Generates n random locations (scout bees)
        List<Location> initialLocations = LOCATION_GENERATOR.generate(w, n, RANDOM);

        // Evaluate all initial locations
        final List<Solution>[] currentSolutions = new List[]{evaluateLocations(f, initialLocations)};
        List<Solution> bestOverallSolutions = new ArrayList<>(currentSolutions[0]);

        // Iterative Search Loop
        // Use IntStream to manage the t search steps
        IntStream.range(0, t).forEach(step -> {

//          (b) Recruitment

            // Select Best Fields (m)
            // Sort solutions using the ComparisonFunction 'c' to find the m best.

            List<Solution> topMSolutions = currentSolutions[0].stream()
                    .sorted(solutionComparator)
                    .limit(m)
                    .collect(Collectors.toList());

            List<Location> recruitedLocations = new ArrayList<>();

            // Split top m into e excellent and m-e others
            List<Solution> excellentSolutions = topMSolutions.stream().limit(e).collect(Collectors.toList());
            List<Solution> otherSolutions = topMSolutions.stream().skip(e).collect(Collectors.toList());

            // (b) Local search

            // Local Search on e excellent fields (p additional bees)

            recruitedLocations.addAll(
                    performLocalSearch(excellentSolutions, w, p, s, f, c, t));

            // Local Search on m-e other best fields (q additional bees)
            recruitedLocations.addAll(
                    performLocalSearch(otherSolutions, w, q, s, f, c, t));

            // c. Global Search (n-m new scout bees)
            int globalScoutCount = n - m;
            recruitedLocations.addAll(
                    LOCATION_GENERATOR.generate(w, globalScoutCount, RANDOM));

            List<Location> globalScoutLocations =
                    LOCATION_GENERATOR.generate(w, n - m, RANDOM);

            List<Solution> localFieldSolutions =
                    evaluateLocations(f, recruitedLocations);

            List<Solution> globalScoutSolutions =
                    evaluateLocations(f, globalScoutLocations);

            currentSolutions[0] = Stream.concat(localFieldSolutions.stream(), globalScoutSolutions.stream()).collect(Collectors.toList());

        });

        bestOverallSolutions.addAll(currentSolutions[0]);
        return bestOverallSolutions.stream()
                .distinct()
                .sorted(solutionComparator)
                .limit(r)
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Locations to a list of Solutions using the ObjectiveFunction f.
     * This is a pure functional map operation.
     */
    private static List<Solution> evaluateLocations(ObjectiveFunction f, List<Location> locations) {
        return locations.stream()
                .map(location -> new Solution(location, f.apply(location)))
                .collect(Collectors.toList());
    }

    /**
     * Performs local search on a list of solutions (fields).
     * Returns a list of the best Location found in each field.
     */
    private static List<Location> performLocalSearch(
            List<Solution> fields,
            Bounds globalBounds,
            int bees,
            double relativeSize,
            ObjectiveFunction f,
            ComparisonFunction c,
            int t
    ) {
        return fields.stream()
                .map(field -> {
                    Bounds localBounds =
                            createLocalBounds(field.getLocation(), globalBounds, relativeSize / t);

                    List<Location> candidates =
                            LOCATION_GENERATOR.generate(localBounds, bees, RANDOM);

                    // Include the original scout
                    candidates.add(field.getLocation());

                    // Pick the single best location in this field
                    return candidates.stream()
                            .map(loc -> new Solution(loc, f.apply(loc)))
                            .reduce((s1, s2) ->
                                    c.apply(s1.getResult(), s2.getResult()) == s1.getResult()
                                            ? s1 : s2
                            )
                            .get()
                            .getLocation();
                })
                .collect(Collectors.toList());
    }


    /**
     * Creates the local search bounds (s) centered around a given Location.
     * This ensures the search is constrained to the flower patch.
     * Currently not a funcrional style
     */
    private static Bounds createLocalBounds(Location center, Bounds globalBounds, double relativeSize) {
        int a = globalBounds.getDimension();
        double[][] localRanges = new double[a][2];
        double[] centerArgs = center.getCoordinates();

        for (int i = 0; i < a; i++) {
            double globalRange = globalBounds.getMax(i) - globalBounds.getMin(i);
            // Local range/radius is calculated based on the global range and relative size 's'
            double localSearchRadius = globalRange * relativeSize;

            // Calculate min and max for the local field, clamped by global bounds
            double localMin = Math.max(centerArgs[i] - localSearchRadius, globalBounds.getMin(i));
            double localMax = Math.min(centerArgs[i] + localSearchRadius, globalBounds.getMax(i));

            localRanges[i][0] = localMin;
            localRanges[i][1] = localMax;
        }
        return new Bounds(localRanges);
    }
}