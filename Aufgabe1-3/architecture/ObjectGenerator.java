package architecture;

import architecture.Pollinators.Bee;
import architecture.Pollinators.BumbleBee;
import architecture.Pollinators.Honeybee;
import architecture.Pollinators.WildBee;

import java.util.*;
import java.util.ArrayList;

/**
 * Utility class for generating randomized simulation objects:
 * - Flower groups with multiple plant species,
 * - Wild bees and honeybees with realistic parameter distributions.
 * <p>
 * STYLE: procedural paradigm – provides utility functions independent of state.
 * We don't create an instance of ObjectGenerator, rather we call the methods directly from the class
 */
public class ObjectGenerator {

    public static int MAX_DISTANCE = 2000;
    //hardcode some values, use an array for simplicity, yes everything has to be a double just live with it:
    //                              y,    c-,  c+,  f-,  f+,  h-,  h+,  q,    p  brightness
    private static final double[][] F = {{10.0, 1.0, 10.0, 0.1, 0.9, 40, 350, 0.033, 0.5, 1.0},
            {7.0, 2.0, 11.0, 0.2, 0.8, 37, 410, 0.060, 0.7, 1.5},
            {11.0, 2.0, 9.0, 0.3, 0.9, 60, 450, 0.022, 0.5, 2.0},
            {9.0, 3.4, 15.0, 0.1, 0.6, 55, 390, 0.0198, 0.5, 2.0}};

    /**
     * Generates a group of flower species.
     *
     * @param num_group Used for random seed and group size.
     * @return FlowerGroup object that represents a collection of flower species with their location coordinates.
     */
    public static FlowerGroup generateFlowerGroup(int num_group) {
        //keep the randomness contained, so different people don't have different values
        Random rand = new Random(num_group);
        int initCapacity = 10 + (num_group % 16); // ensure between 10 and 25

        //initial capacity shouldn't really matter since its dynamic but what ever performance right?
        ArrayList<FlowerSpecies> flowerSpecies = new ArrayList<>();

        //create individual PlantSpecies objects.
        for (int i = 0; i < initCapacity; i++) {

            //deals with hardcoded values
            if (i < F.length) {
                double[] v = F[i];
                flowerSpecies.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9]));
            } else {
                //deals with randomly generated values
                double[] v = generateFlowerValues(rand);
                flowerSpecies.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9]));
            }
        }

        //distance seems to be between 0 and 1000
        double x = rand.nextDouble() * MAX_DISTANCE;
        double y = rand.nextDouble() * MAX_DISTANCE;

        return new FlowerGroup(flowerSpecies, x, y);
    }

    /**
     * Generates a list of wild bee species.
     */
    public static ArrayList<Bee> generateWildBees(int num) {
        Random rand = new Random(7777 + num);
        ArrayList<Bee> bees = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double[] v = generateBeeValues(rand);
            int type = rand.nextInt(3);
            Bee bee;
            switch (type) {
                case 0 -> bee = new WildBee("WildBee-" + i, v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7]);
                case 1 -> bee = new BumbleBee("BumbleBee-" + i, v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7]);
                default -> bee = new Honeybee("HoneyBee-" + i, v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7]);
            }
            bees.add(bee);
        }
        return bees;
    }

    /**
     * Generates a list of honeybee populations.
     */
    public static ArrayList<Honeybee> generateHoneyBees(int num) {
        Random rand = new Random(8888 + num);
        ArrayList<Honeybee> bees = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double[] v = generateBeeValues(rand);
            bees.add(new Honeybee("Honeybee-" + i,
                    Math.max(50, v[0] * 1.5), v[1], v[2],
                    v[3], v[4], v[5], v[6], Math.max(0.4, v[7] * 0.9)));
        }
        return bees;
    }

    /**
     * Returns a bounded Gaussian random number within [min, max].
     */
    private static double boundedGaussian(Random r, double mean, double std, double min, double max) {
        double val = mean + r.nextGaussian() * std;
        if (val < min) val = min + r.nextDouble() * (mean - min);
        if (val > max) val = max - r.nextDouble() * (max - mean);
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Generates random flower parameters for variety in the simulation.
     */
    private static double[] generateFlowerValues(Random rand) {
        double[] v = new double[10];
        v[0] = boundedGaussian(rand, 9.0, 3.0, 3.0, 20.0);        // y
        v[1] = boundedGaussian(rand, 2.0, 0.8, 1.0, 6.0);         // c_lower
        v[2] = boundedGaussian(rand, 10.0, 3.0, v[1] + 0.5, 20.0);// c_upper
        v[3] = boundedGaussian(rand, 0.3, 0.15, 0.05, 0.7);       // f_lower
        v[4] = boundedGaussian(rand, 0.7, 0.15, v[3] + 0.1, 0.95);// f_upper
        v[5] = boundedGaussian(rand, 80.0, 30.0, 20.0, 200.0);    // h_lower
        v[6] = boundedGaussian(rand, 320.0, 60.0, v[5] + 50, 500.0);// h_upper
        v[7] = boundedGaussian(rand, 0.03, 0.01, 0.005, 0.06);    // q
        double denom = Math.max(60.0, v[6] - v[5]);
        v[8] = boundedGaussian(rand, 0.5 / denom, 0.2 / denom, 0.1 / denom, 1.0 / denom); // p
        v[9] = boundedGaussian(rand, 1.4, 0.5, 0.5, 2.2);         // brightness
        return v;
    }

    /**
     * Generates random parameter sets for bee species.
     */
    private static double[] generateBeeValues(Random rand) {
        double[] v = new double[8];
        v[0] = boundedGaussian(rand, 80.0, 35.0, 20.0, 200.0); // population
        double a1 = boundedGaussian(rand, 40.0, 25.0, 0.0, 180.0);
        double a2 = boundedGaussian(rand, 160.0, 25.0, a1 + 10.0, 240.0);
        v[1] = a1;
        v[2] = a2;
        v[3] = rand.nextDouble() * MAX_DISTANCE;
        v[4] = rand.nextDouble() * MAX_DISTANCE;
        double cL = boundedGaussian(rand, 1.0, 0.6, 0.4, 1.6);
        double cU = boundedGaussian(rand, 1.8, 0.6, cL + 0.2, 2.4);
        v[5] = cL;
        v[6] = cU;
        v[7] = boundedGaussian(rand, 0.85, 0.2, 0.5, 1.2);
        return v;
    }

}