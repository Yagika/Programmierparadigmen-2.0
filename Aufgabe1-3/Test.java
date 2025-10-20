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

         final int GROUPS = 5;
         final int YEARS = 25;

         System.out.println("=== Bee & Flower Simulation ===");

         for (int g = 1; g <= GROUPS; g++) {
             System.out.printf("%n--------  Group %d -------- %n", g);

             ArrayList<Flowerspecies> group = generator.generatePlantGroups(g);

             Random groupRand = new Random(g * 1234L);
             double initialBees = 50.0 + groupRand.nextDouble() * 150.0;
             initialBees = Math.round(initialBees);

             long seed = g * 1000L;
             boolean detailed = true;

             SimulationLogic.SimulationResult result = simulator.simulate(
                     YEARS, initialBees, group, seed, detailed);

             String filename = String.format("statistics_group%d.csv", g);
             try (FileWriter writer = new FileWriter(filename)) {
                 writer.write("group,years,initialBees,finalBees,avgBees,avgFood\n");
                 writer.write(String.format("%d,%d,%.0f,%.0f,%.0f,%.4f\n",
                         g, YEARS, initialBees, result.finalBees, result.avgBees, result.avgFood));

                 writer.write("\nspecies_index,final_y\n");
                 for (int i = 0; i < group.size(); i++) {
                     writer.write(String.format("%d,%.6f\n", i + 1, group.get(i).getY()));
                 }
             } catch (IOException e) {
                 System.err.println("Error writing CSV: " + e.getMessage());
             }

             System.out.printf("Group %d finished -> finalBees=%.0f avgBees=%.0f avgFood=%.2f (CSV: %s)%n",
                     g, result.finalBees, result.avgBees, result.avgFood, filename);
         }

         System.out.println("\nAll groups finished — CSV files created.");
    }
}
