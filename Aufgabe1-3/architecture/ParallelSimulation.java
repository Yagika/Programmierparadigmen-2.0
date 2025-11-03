package architecture;

import architecture.Pollinators.Bee;
import java.util.List;

/**
 * STYLE: parallel – demonstrates parallel population updates.
 * NOTE: bees != null, food >= 0, reserve >= 0
 */
public class ParallelSimulation {

    /**
     * Updates bee populations in parallel threads.
     * This showcases concurrency with parallel streams.
     *
     * @param bees list of bees to update
     * @param n daily food available
     * @param reserve current food reserve
     */
    public static void runParallelUpdate(List<Bee> bees, double n, double reserve) {
        System.out.println("\n[Parallel Simulation] Starting concurrent population updates...");

        // GOOD: uses parallelism safely – each bee updates its own internal state
        bees.parallelStream().forEach(b -> b.updatePopulation(n, reserve));

        // BAD: shared output (println) in parallel stream – may interleave lines
        bees.parallelStream().forEach(b ->
                System.out.printf("Thread updated %s → Pop=%.1f%n", b.getName(), b.getPopulation())
        );

        System.out.println("[Parallel Simulation] Updates complete.\n");
    }
}
