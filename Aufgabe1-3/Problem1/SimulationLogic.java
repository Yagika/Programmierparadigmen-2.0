package Problem1;

import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the main simulation logic:
 * - simulates weather
 * - calculates food supply and bee population
 * - updates plant states
 */
public class SimulationLogic {
    private static final int VEGETATION_DAYS = 240;

    /**
     * Runs the simulation for a given number of years.
     * @param num_years number of simulated years
     * @param x initial bee population
     * @param plant_group list of flower species (each run must pass a fresh copy)
     * @param randomSeed seed for weather/randomness (to allow reproducible runs)
     * @param detailed if true - prints daily and yearly intermediate results
     * @return statistics about the simulation (average and final values)
     */
    public SimulationResult simulate(int num_years, double x, ArrayList<Flowerspecies> plant_group, long randomSeed, boolean detailed) {
        Random rand = new Random(randomSeed);
        double totalBees = 0;
        double totalFood = 0;

        for (int year = 1; year <= num_years; year++) {
            // At start of vegetation period, b and s are zero
            for (Flowerspecies plant : plant_group) {
                plant.resetForVegetation();
            }
            double f = rand.nextDouble();
            double h = 0.0;
            double n = 0.0;

            if (detailed) System.out.printf("Year %d start: initial bees=%.2f, initial f=%.3f%n", year, x, f);

            // Vegetation period: 240 days
            for (int day = 1; day <= VEGETATION_DAYS; day++) {
                double d = rand.nextDouble() * 12.0; // today's sunshine 0..12
                h += d;

                double mult = (rand.nextDouble() * 0.2) + 0.9; // update soil moisture
                f *= mult;
                if (f < 0.0) f = 0.0;
                if (f > 1.0) f = 1.0;

                for (Flowerspecies plant : plant_group) {
                    plant.moisture_threshold(f);
                    plant.bloom_time(h, d);
                }

                n = calculate_food_supply(plant_group);

                for (Flowerspecies plant : plant_group) {
                    plant.pollination_probability(x, n, d);
                }

                x = calculate_bees(x, n); // bees after pollination

                if (detailed && (day <= 3 || day % 60 == 0)) {

                    System.out.printf(" Year %d Day %d: d=%.2f h=%.2f f=%.3f n=%.3f bees=%.2f%n",
                            year, day, d, h, f, n, x);
                }
            }

            // Winter: bee death and plant resting
            double winterMult = rand.nextDouble() * 0.2 + 0.1;
            x *= winterMult;

            // Plant resting phase
            for (Flowerspecies plant : plant_group) {
                plant.resting_phase(rand);
            }

            totalBees += x;
            totalFood += n;

            if (detailed) {
                System.out.printf(" Year %d end: bees=%.2f (after winterMult=%.3f), last n=%.3f%n", year, x, winterMult, n);
            } else {
                System.out.printf("Year %d → Bees: %.2f | Food: %.2f%n", year, x, n);
            }
        }


        double avgBees = totalBees / num_years;
        double avgFood = totalFood / num_years;
        return new SimulationResult(x, avgBees, avgFood);
    }

    /**
     * Calculates the number of bees for the next day.
     */
    private double calculate_bees(double x, double n) {
        if (x <= 0.0) return 0.0;
        if (n >= x) {
            x = 1.03 * x;
        } else {
            x = (6.0 * n / x - 3.0) * x;
        }
        if (Double.isNaN(x) || Double.isInfinite(x)) return 0.0;
        return Math.max(x, 0.0);
    }

    /**
     * Calculates the total food supply from all flower species.
     */
    private double calculate_food_supply(ArrayList<Flowerspecies> plant_group) {
        double n = 0.0;
        for (Flowerspecies f : plant_group) {
            n += f.foodvalue();
        }
        return n;
    }
    /**
     * Result container.
     */
    public static class SimulationResult {
        public double finalBees;
        public double avgBees;
        public double avgFood;

        public SimulationResult(double finalBees, double avgBees, double avgFood) {
            this.finalBees = finalBees;
            this.avgBees = avgBees;
            this.avgFood = avgFood;
        }
    }
}
