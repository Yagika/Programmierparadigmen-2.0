import Problem1.FlowerSpecies;
import Problem1.Pollinators.Bee;
import Problem1.SimulationLogic;
import Problem1.ObjectGenerator;

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
         final int NUM_BEES = 3; // Generate 3 wild bee species to simulate

         System.out.println("=== Bee & Flower Simulation ===");

         // TODO: Generate bees here (or maybe inside the loop)
         ArrayList<Bee> bees = generator.generateBees(NUM_BEES);

         for (int g = 1; g <= GROUPS; g++) {
             System.out.printf("%n--------  Group %d -------- %n", g);

             ArrayList<FlowerSpecies> group = generator.generatePlantGroups(g);

//             Random groupRand = new Random(g * 1234L);
//             double initialBees = 50.0 + groupRand.nextDouble() * 150.0;
//             initialBees = Math.round(initialBees);

             long seed = g * 1000L;
             boolean detailed = true;

             SimulationLogic.SimulationResult result = simulator.simulate(
                     YEARS, bees, group, seed, detailed);

             System.out.printf("Group %d results:%n", g);
             System.out.printf("  Years simulated: %d%n", YEARS);
//             System.out.printf("  Initial bees: %.0f%n", initialBees);
             System.out.printf("  Final bees: %.0f%n", result.finalBees);
             System.out.printf("  Average bees: %.0f%n", result.avgBees);
             System.out.printf("  Average food: %.4f%n", result.avgFood);


             System.out.println("-------------------------------");
         }

         System.out.println("\nAll groups finished.");
    }
}
