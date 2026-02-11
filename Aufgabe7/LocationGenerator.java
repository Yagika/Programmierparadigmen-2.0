import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FunctionalInterface
/**
 * Custom functional form (Higher-Order Function) for generating a set of 
 * random Locations within specified Bounds.
 * Implements Function<Bounds, List<Location>> (implicitly).
 */
public interface LocationGenerator {

    // We pass the number of locations needed (n, p, or q) and a Random instance
    // to the generator to make it purely functional (no reliance on external state).
    List<Location> generate(Bounds bounds, int count, Random random);

    /**
     * Factory method to create a concrete LocationGenerator.
     * This method implements the core logic for generating random points.
     */
    static LocationGenerator create() {
        // The lambda represents the actual location generation logic.
        return (bounds, count, random) ->
                // 1. Create a stream of 'count' locations
                IntStream.range(0, count)
                        // 2. Map each element to a new Location object
                        .mapToObj(i -> generateSingleLocation(bounds, random))
                        // 3. Collect them into a list
                        .collect(Collectors.toList());
    }

    // --- Helper for Single Location Generation (Stateless) ---
    private static Location generateSingleLocation(Bounds bounds, Random random) {
        double[] arguments = new double[bounds.getDimension()];

        // Iterate over each dimension/argument
        for (int i = 0; i < bounds.getDimension(); i++) {
            double min = bounds.getMin(i);
            double max = bounds.getMax(i);

            // Random value between min and max: min + (max - min) * random.nextDouble()
            arguments[i] = min + (max - min) * random.nextDouble();
        }

        return new Location(arguments);
    }
}