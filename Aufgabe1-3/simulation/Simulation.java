package simulation;

import architecture.*;
import architecture.Pollinators.Bee;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Handles the main simulation logic:
 * - simulates weather
 * - calculates food supply and bee population
 * - updates plant states
 *
 * STYLE: mixed – OO + procedural
 */
public class Simulation {

    private static final int VEGETATION_DAYS = 240;

    /**
     * Runs the simulation for a given number of years and prints daily results for year 1.
     * Returns summary statistics for the whole run.
     *
     * BAD: The control flow of this method is not completely clear, we simulate everything at the same time. This results
     * in reduced modularization. This might hurt overall cohesion of the module.
     */
    public SimulationResult simulate(int num_years,
                                     ArrayList<Bee> bees,
                                     ArrayList<FlowerGroup> flowerGroups,
                                     long randomSeed,
                                     boolean detailed) {
        Random rand = new Random(randomSeed);
        double reserve = 0.0;

        double lastYearBees = 0.0;
        double totalFoodAcc = 0.0;

        for (int year = 1; year <= num_years; year++) {

            // Reset plants for a new vegetation season
            for (FlowerGroup fg : flowerGroups) fg.resetForVegetation();

            Weather weather = new Weather(12.0, 45.0, Weather.WeatherEvents.WEATHER_PARTLY_CLOUDY);
            double f = clamp(0.5 + rand.nextGaussian() * 0.08, 0.0, 1.0);
            double h = 0.0;

            if (detailed) {
                System.out.printf("%n=== YEAR %d START ===%n", year);
                System.out.println("Day  |  Temp  | Sun(d) | CumSun(h) | Moist(f) |  Food(n)  |  Reserve  |  TotalBees  | Bees{ name:pop ... }");
                System.out.println("-----+--------+--------+-----------+----------+-----------+-----------+-------------+---------------------");
            }

            for (int day = 1; day <= VEGETATION_DAYS; day++) {
                weather.NextWeatherEvent(day, rand.nextDouble());
                weather.NextTemperature(day, rand.nextGaussian()); // std ~ 1.0 °C

                double d = clamp(6.0 + rand.nextGaussian() * 2.5, 0.0, 12.0);
                h += d;

                f *= (0.995 + rand.nextGaussian() * 0.01);
                switch (weather.event) {
                    case WEATHER_RAINY -> f += 0.03;
                    case WEATHER_STORMY -> f += 0.05;
                    case WEATHER_SUNNY -> f -= 0.015;
                    default -> {}
                }
                f = clamp(f, 0.0, 1.0);

                // Plants update
                for (FlowerGroup fg : flowerGroups) {
                    for (FlowerSpecies fs : fg.speciesList) {
                        fs.moisture_threshold(f);
                        fs.bloom_time(h, d);
                    }
                }

                // Bees update (season + weather)
                for (Bee bee : bees) {
                    bee.updateActivity(day, weather);
                }

                // Pollination by groups
                for (FlowerGroup fg : flowerGroups) {
                    fg.applyPollination(bees, day, d);
                }

                // Food
                double n = 0.0;
                for (FlowerGroup fg : flowerGroups) n += fg.totalFood();
                totalFoodAcc += n;

                // Architecture.Classes.Bee population update based on n and reserve
                for (Bee bee : bees) {
                    bee.updatePopulation(n, reserve);
                }

                // Reserve accumulation
                reserve = min(1e6, reserve + n * 0.25);

                // Daily output
                if (detailed) {
                    StringBuilder beeBrief = new StringBuilder();
                    for (Bee b : bees) {
                        beeBrief.append(b.getName())
                                .append(":").append(Math.round(b.population))
                                .append(" ");
                    }
                    System.out.printf("%3d  | %6.1f | %6.2f | %9.2f | %8.3f | %9.2f | %9.2f | %11.0f | %s%n",
                            day, weather.temperature, d, h, f, n, reserve, calculate_total_bees(bees), beeBrief);
                }
            }

            // Winter survival + reserve consumption
            double winterMult = 0.6 + rand.nextDouble() * 0.3;
            double totalBees = 0.0;
            for (Bee bee : bees) {
                bee.population = Math.max(10, Math.round(bee.population * winterMult));
                double winterConsumption = Math.min(reserve, bee.population * 0.5);
                reserve = Math.max(0.0, reserve - winterConsumption);
                totalBees += bee.population;
            }

            // Plants resting phase
            for (FlowerGroup fg : flowerGroups) {
                fg.resting_phase(rand);
            }

            lastYearBees = totalBees;

            if (detailed) {
                System.out.printf("=== YEAR %d END === Bees: %.0f | Reserve: %.2f%n", year, lastYearBees, reserve);
            }
        }

        double avgBees = lastYearBees / Math.max(1, num_years);
        double avgFood = totalFoodAcc / Math.max(1, num_years);
        return new SimulationResult(Math.round(lastYearBees), Math.round(avgBees), avgFood);
    }

    /**
     * Sums populations over all bees.
     */
    private double calculate_total_bees(ArrayList<Bee> bees) {
        double total = 0.0;
        for (Bee bee : bees) total += bee.population;
        return total;
    }

    private static double clamp(double v, double lo, double hi) {
        return max(lo, min(hi, v));
    }
    // NOTE: Integration of functional and parallel modules for assignment 3.

    public void runFunctionalAndParallelExamples(ArrayList<Bee> bees) {
        // STYLE: functional – use pure stream-based calculations
        double avgEff = architecture.FunctionalStats.calculateAverageEffectiveness(bees);
        long activeNow = architecture.FunctionalStats.countActiveBees(bees, 100);

        System.out.printf("%n[Functional Analysis] Avg effectiveness: %.2f | Active on day 100: %d%n", avgEff, activeNow);

        // STYLE: parallel – delegate to a separate class handling concurrency
        architecture.ParallelSimulation.runParallelUpdate(bees, 1000, 500);
    }

    /**
     * Container for summary results.
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