package Problem1;

import Problem1.Pollinators.Bee;

import java.util.ArrayList;
import java.util.Random;

public class FlowerGroup {

    ArrayList<FlowerSpecies> speciesList;
    double x, y; // Coordinates
    double p; // number of bees that will pollinate this plant
    double n; // Total food supply from this group



    public FlowerGroup(ArrayList<FlowerSpecies> speciesList, double x, double y) {
        this.speciesList = speciesList;
        this.x = x;
        this.y = y;
    }

    /**
     * Probability that specific kind of bee will pollinate this flower group depending on the distance from the nest
     * @param speciesList
     * @param bees
     */
    public void pollinationProbability (ArrayList<FlowerSpecies> speciesList, ArrayList<Bee> bees) {


    }

    /**
     * Resets the blooming in the beginning of the new year
     */
    public void resetForVegetation() {
        for (FlowerSpecies plant : speciesList) {
            plant.b = 0.0;
            plant.s = 0.0;
        }
    }

    public void calculateTotalFood () {
        n = 0.0;
        for (FlowerSpecies flower : speciesList) {
            n += flower.foodvalue();
        }
    }

    /**
     * Simulates the resting (winter) phase: y is multiplied by s and by a random
     * factor in [c_lower, c_upper]. Random is passed from outside to control seeding.
     */
    public void resting_phase(Random rand) {
        //parse a number that stays the same, so that everyone gets the same outcome and the data
        //can be recreated.
        for (FlowerSpecies flowerSpecies : speciesList) {
            double randomDouble = rand.nextDouble() * (flowerSpecies.c_upper - flowerSpecies.c_lower) + flowerSpecies.c_lower;

            flowerSpecies.y = flowerSpecies.y * flowerSpecies.s * randomDouble;
            if (flowerSpecies.y < 0) flowerSpecies.y = 0;
        }
    }
}
