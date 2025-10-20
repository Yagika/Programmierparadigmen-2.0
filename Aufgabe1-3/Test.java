import Problem1.Flowerspecies;
import Problem1.SimulationLogic;
import Problem1.ObjectGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * ### Assignment 1 ###
 *
 * Team Collaboration Overview:
 * Each work package lists the main responsible person.
 * Other team members provided assistance and testing.
 *
 * - Project structure: Dominik, Yana, and Aleksandr
 * - FlowerSpecies module: Dominik
 * - ObjectGenerator module: Dominik
 * - SimulationLogic: Aleksandr & Yana
 * - Test: Aleksandr & Yana
 * - Statistics and result analysis: Yana
 * - Documentation: Yana & Dominik
 *
 * Additional Contributions:
 * - Code optimization and debugging: Dominik
 * - Review and final adjustments: Aleksandr
 * - Data validation and output formatting: Yana
 *
 * This project simulates an ecological system of bees and flower species over several years,
 * focusing on population dynamics, blooming behavior, and environmental influence.
 */

 /**
 * Main class to run the bee–flower ecosystem simulation.
 * Reads user input and shows yearly and summary results.
 */
public class Test {

    public static void main(String[] args) {

        ObjectGenerator generator = new ObjectGenerator();
        SimulationLogic simulator = new SimulationLogic();

        final int GROUPS = 3;
        final int RUNS_PER_GROUP = 10;
        final int YEARS = 25;

        System.out.println("=== Bee & Flower Simulation ===");

        for (int g = 1; g <= GROUPS; g++) {
            System.out.printf("%n--- Processing Group %d --- %n", g);


            for (int run = 1; run <= RUNS_PER_GROUP; run++) {
                ArrayList<Flowerspecies> group = generator.generatePlantGroups(g);

                double initialBees = 50.0 + (new Random(g * 100 + run)).nextDouble() * 150.0;

                long seed = g * 1000L + run; // weather seed
                boolean detailed = (g == 1 && run == 1);

                SimulationLogic.SimulationResult result = simulator.simulate(YEARS, initialBees, group, seed, detailed);

                // Save basic summary statistics
                String filename = String.format("statistics_group%d_run%d.csv", g, run);
                try (FileWriter writer = new FileWriter(filename)) {
                    writer.write("group,run,years,initialBees,finalBees,avgBees,avgFood\n");
                    writer.write(String.format("%d,%d,%d,%.4f,%.4f,%.4f,%.4f\n",
                            g, run, YEARS, initialBees, result.finalBees, result.avgBees, result.avgFood));

                    writer.write("species_index,final_y\n");
                    for (int i = 0; i < group.size(); i++) {
                        writer.write(String.format("%d,%.6f\n", i + 1, group.get(i).getY()));
                    }
                } catch (IOException e) {
                    System.err.println("Error writing CSV: " + e.getMessage());
                }

                System.out.printf("Group %d Run %d -> finalBees=%.2f avgBees=%.2f avgFood=%.2f (CSV: %s)%n",
                        g, run, result.finalBees, result.avgBees, result.avgFood, filename);
            }
        }

        System.out.println("\nAll simulations finished. CSV statistics files created for each run.");
    }
}
