package Problem1;

import Problem1.Pollinators.Bee;

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
     * @param bees list of bee species
     * @param plant_group list of flower species (each run must pass a fresh copy)
     * @param randomSeed seed for weather/randomness (to allow reproducible runs)
     * @param detailed if true - prints daily and yearly intermediate results
     * @return statistics about the simulation (average and final values)
     */

    // TODO: implement simulation of bees
    // TODO: somehow deal with distances and nests. Maybe create a map where we place flowers and where bees can create nests?
    public SimulationResult simulate(int num_years, ArrayList<Bee> bees, ArrayList<FlowerSpecies> plant_group, long randomSeed, boolean detailed) {
        Random rand = new Random(randomSeed);
        double totalBees = 0;
        double totalFood = 0;
        double reserve = 0.0;

//        x = Math.max( Math.round(x), 10.0 );

        for (int year = 1; year <= num_years; year++) {
            for (FlowerSpecies plant : plant_group) {
                plant.resetForVegetation();
            }

            // TODO: not sure what to do with this right now
//            if (reserve > 1.0) {
//                double boost = Math.floor(reserve * 0.02);
//                if (boost >= 1.0) {
//                    x += boost;
//                    reserve -= boost * 0.5;
//                }
//            }

            double f = rand.nextDouble();
            double h = 0.0;
            double n = 0.0;

            if (detailed) System.out.printf("Year %d start: initial bees=%.0f, initial f=%.3f reserve=%.3f%n", year, totalBees, f, reserve);

            // Vegetation period: 240 days
            for (int day = 1; day <= VEGETATION_DAYS; day++) {
                double d = rand.nextGaussian() * 12.0; // today's sunshine 0..12
                // TODO: maybe here add temperature simulation. Designate this as a procedural style block?
                h += d;

                f *= (0.95 + rand.nextGaussian() * 0.1);
                f = Math.max(0.0, Math.min(f, 1.0));

                for (FlowerSpecies plant : plant_group) {
                    plant.moisture_threshold(f);
                    plant.bloom_time(h, d);
                }

                for (Bee bee : bees) {
                    bee.calculate_population(h, f, reserve);
                    bee.calculate_multiplier(day);
                }

                // Calculate total number of bees
                // TODO: check what to do with this. How useful is this if we have different kinds of bees now?
                totalBees = calculate_total_bees(bees);

                n = calculate_food_supply(plant_group);

                if (n < 0.0) n = 0.0;

                // pollination
                // TODO: check what to do with this
                for (FlowerSpecies plant : plant_group) {
                    plant.pollination_probability(bees, totalBees, n, d);
                }

                reserve += n * 0.3;
                reserve = Math.min(reserve, 1e6);

                // Recalculate population of bees for every wild bee species
                // TODO: improve this calculation (maybe take into account competition for food or smth)
                for (Bee bee: bees) {
                    bee.calculate_population(bee.population, n, reserve);
                }

                if (detailed && (day <= 3 || day % 60 == 0)) {
                    System.out.printf(" Year %d Day %d: d=%.2f h=%.2f f=%.3f n=%.3f bees=%.0f reserve=%.2f%n",
                            year, day, d, h, f, n, totalBees, reserve);
                }
            }

            // Winter: bee survival
            // TODO: Check what to do with this (maybe different bees should have different death rates during winter)
            double winterMult = 0.6 + rand.nextDouble() * 0.3;
            for (Bee bee: bees) {
                bee.population = Math.round(bee.population * winterMult);

                if (bee.population < 10) bee.population = 10;

                double winterConsumption = Math.min(reserve, bee.population * 0.5);
                reserve = Math.max(0.0, reserve - winterConsumption);

                // Plant resting phase
                for (FlowerSpecies plant : plant_group) {
                    plant.resting_phase(rand);
                }
            }

            // Maybe recalculate number of bees at the end of the day
            totalBees = calculate_total_bees(bees);
            totalFood += n;

            if (detailed) {
                System.out.printf(" Year %d end: bees=%.0f (after winter=%.3f), last n=%.3f reserve=%.2f%n", year, totalBees, winterMult, n, reserve);
            } else {
                System.out.printf("Year %d → Bees: %.0f | Food: %.3f | Reserve: %.2f%n", year, totalBees, n, reserve);
            }
        }

        double avgBees = totalBees / num_years;
        double avgFood = totalFood / num_years;

        return new SimulationResult(Math.round(totalBees), Math.round(avgBees), avgFood);
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
     * Calculate total number of bees
     * @param bees Array list of bees
     * @return total number of bees of all kinds
     */
    private double calculate_total_bees(ArrayList<Bee> bees) {
        double totalBees = 0.0;

        for (Bee bee : bees) {
            totalBees += bee.population;
        }

        return totalBees;
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