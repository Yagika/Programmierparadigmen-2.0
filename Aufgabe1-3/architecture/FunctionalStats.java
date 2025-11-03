package architecture;

import architecture.Pollinators.Bee;
import java.util.List;

/**
 * STYLE: functional – stateless computation using streams and higher-order functions.
 * NOTE: bees != null
 */
public class FunctionalStats {

    /**
     * Calculates average effectiveness using Java streams.
     * Demonstrates referential transparency and immutability.
     */
    public static double calculateAverageEffectiveness(List<Bee> bees) {
        // GOOD: functional, no side effects
        return bees.stream()
                .mapToDouble(Bee::getEffectiveness)
                .average()
                .orElse(0.0);
    }

    /**
     * Counts how many bees are active on a specific day.
     * Uses stream filtering and pure predicates.
     */
    public static long countActiveBees(List<Bee> bees, int day) {
        // GOOD: functional composition (filter + count)
        return bees.stream()
                .filter(b -> b.isActive(day))
                .count();
    }
}
