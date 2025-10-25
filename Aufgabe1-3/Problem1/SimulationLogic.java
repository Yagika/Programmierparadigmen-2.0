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
    public SimulationResult simulate(int num_years, double x, ArrayList<FlowerSpecies> plant_group, long randomSeed, boolean detailed) {
        Random rand = new Random(randomSeed);
        double totalBees = 0;
        double totalFood = 0;
        double reserve = 0.0;

        x = Math.max( Math.round(x), 10.0 );

        for (int year = 1; year <= num_years; year++) {
            for (FlowerSpecies plant : plant_group) {
                plant.resetForVegetation();
            }

            if (reserve > 1.0) {
                double boost = Math.floor(reserve * 0.02);
                if (boost >= 1.0) {
                    x += boost;
                    reserve -= boost * 0.5;
                }
            }

            double f = rand.nextDouble();
            double h = 0.0;
            double n = 0.0;

            if (detailed) System.out.printf("Year %d start: initial bees=%.0f, initial f=%.3f reserve=%.3f%n", year, x, f, reserve);

            // Vegetation period: 240 days
            for (int day = 1; day <= VEGETATION_DAYS; day++) {
                double d = rand.nextDouble() * 12.0; // today's sunshine 0..12
                h += d;

                f *= (0.95 + rand.nextDouble() * 0.1);
                f = Math.max(0.0, Math.min(f, 1.0));

                for (FlowerSpecies plant : plant_group) {
                    plant.moisture_threshold(f);
                    plant.bloom_time(h, d);
                }

                n = calculate_food_supply(plant_group);

                if (n < 0.0) n = 0.0;

                // pollination
                for (FlowerSpecies plant : plant_group) {
                    plant.pollination_probability(x, n, d);
                }

                reserve += n * 0.3;
                reserve = Math.min(reserve, 1e6);

                x = calculate_bees(x, n, reserve);

                if (detailed && (day <= 3 || day % 60 == 0)) {
                    System.out.printf(" Year %d Day %d: d=%.2f h=%.2f f=%.3f n=%.3f bees=%.0f reserve=%.2f%n",
                            year, day, d, h, f, n, x, reserve);
                }
            }

            // Winter: bee survival
            double winterMult = 0.6 + rand.nextDouble() * 0.3;
            x = Math.round(x * winterMult);

            if (x < 10) x = 10;

            double winterConsumption = Math.min(reserve, x * 0.5);
            reserve = Math.max(0.0, reserve - winterConsumption);

            // Plant resting phase
            for (FlowerSpecies plant : plant_group) {
                plant.resting_phase(rand);
            }

            totalBees += x;
            totalFood += n;

            if (detailed) {
                System.out.printf(" Year %d end: bees=%.0f (after winter=%.3f), last n=%.3f reserve=%.2f%n", year, x, winterMult, n, reserve);
            } else {
                System.out.printf("Year %d → Bees: %.0f | Food: %.3f | Reserve: %.2f%n", year, x, n, reserve);
            }
        }

        double avgBees = totalBees / num_years;
        double avgFood = totalFood / num_years;

        return new SimulationResult(Math.round(x), Math.round(avgBees), avgFood);
    }

    /**
     * Calculates the number of bees for the next day.
     */
    private double calculate_bees(double x, double n, double reserve) {
        if (x <= 0.0) return 10.0;
        double availableFood = n + reserve * 0.01;
        double demand = x * 0.2;
        double ratio = demand > 0 ? (availableFood / demand) : 0.0;

        double growthRate;
        if (ratio >= 1.1) {
            growthRate = 1.02 + 0.03 * Math.tanh((ratio - 1.0));
        } else if (ratio >= 0.8) {
            growthRate = 1.00;
        } else {
            growthRate = 0.98 * ratio + 0.6 * 0.1;
        }

        double newX = x * growthRate;

        if (Double.isNaN(newX) || Double.isInfinite(newX)) newX = 10.0;

        if (newX < 5.0) newX = 5.0;

        return Math.round(newX);
    }

    /**
     * Calculates the total food supply from all flower species.
     */
    private double calculate_food_supply(ArrayList<FlowerSpecies> plant_group) {
        double n = 0.0;
        for (FlowerSpecies f : plant_group) {
            double val = f.foodvalue();
            if (Double.isNaN(val) || val < 0) val = 0.0;
            n += val;
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