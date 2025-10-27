import architecture.FlowerGroup;
import architecture.Pollinators.Bee;
import simulation.Simulation;
import architecture.ObjectGenerator;

import java.util.ArrayList;


/**
 * ### Assignment 1 ###
 * <p>
 * Team Collaboration Overview:
 * Each work package lists the main responsible person.
 * Other team members provided assistance and testing.
 * <p>
 * - Project structure: Dominik, Yana, and Aleksandr
 * - FlowerSpecies module: Dominik
 * - ObjectGenerator module: Dominik
 * - SimulationLogic: Aleksandr & Yana
 * - Test: Aleksandr & Yana
 * - Statistics and result analysis: Yana
 * - Documentation: Yana & Dominik
 * <p>
 * Additional Contributions:
 * - Code optimization and debugging: Dominik
 * - Review and final adjustments: Aleksandr
 * - Data validation and output formatting: Yana
 * <p>
 * This project simulates an ecological system of bees and flower species over several years,
 * focusing on population dynamics, blooming behavior, and environmental influence.
 * <p>
 * <p>
 * ### Assignment 2 ###
 * <p>
 * Team Collaboration Overview:
 * Each work package lists the main responsible person.
 * Other team members provided assistance and testing.
 * <p>
 * - Project structure: Dominik, Yana, and Aleksandr
 * - architecture:
 * ----- pollinators:
 * ---------- Bee: Dominik
 * ---------- HoneyBee: Yana
 * ---------- Pollinator: Yana
 * ----- FlowerSpecies: Dominik
 * ----- FlowerGroup: Aleksandr
 * ----- ObjectGenerator: Aleksandr & Dominik
 * ----- Weather: Dominik
 * - simulation:
 * ----- Simulation: Yana & Aleksandr
 * - testArchitecture:
 * ----- TestWeather: Dominik
 * - Test: Yana & Aleksandr
 * -
 * - Documentation: Yana & Aleksandr
 * <p>
 * Additional Contributions:
 * - Code optimization and debugging: Dominik
 * - Review and final adjustments: Yana
 * - Data validation and output formatting: Aleksandr
 * <p>
 * This project simulates an ecological system of bees and flower species over several years,
 * focusing on population dynamics, blooming behavior, and environmental influence.
 */

/**
 * Main class to run the bee–flower ecosystem simulation.
 * Reads user input and shows yearly and summary results.
 *
 * STYLE: mixed – OO + procedural
 */
public class Test {

    public static void main(String[] args) {
        ObjectGenerator generator = new ObjectGenerator();
        Simulation simulator = new Simulation();

        final int GROUPS = 5;
        final int YEARS = 10;
        final int NUM_BEES = 3;

        System.out.println("=== Bee & Flower Simulation (Abgabe 2) ===");

        ArrayList<Bee> bees = generator.generateWildBees(NUM_BEES);

        ArrayList<FlowerGroup> flowerGroups = new ArrayList<>();
        for (int g = 1; g <= GROUPS; g++) {
            flowerGroups.add(generator.generateFlowerGroup(g));
        }

        long seed = 1000L;
        boolean detailed = true;

        Simulation.SimulationResult result =
                simulator.simulate(YEARS, bees, flowerGroups, seed, detailed);

        System.out.println("\n--- Summary ---");
        System.out.printf("Years simulated: %d%n", YEARS);
        System.out.printf("Final bees: %.0f%n", result.finalBees);
        System.out.printf("Avg bees: %.0f%n", result.avgBees);
        System.out.printf("Avg food: %.4f%n", result.avgFood);
    }
}
