package Problem1;

import Problem1.Pollinators.Bee;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.util.*;
import java.util.ArrayList;

/*
Generate an object to handle a collection (Group) of flowerspecies.

an array should suffice?


We need to generate 3 groups. That means 3 ArrayLists that hold varying Flowerspecies objects. These ArrayLists should
have a size ranging from 10-25. These should probably have different kinds of Flowerspecies. So I am thinking lets do the
bare minimum and have about 12-15 per group.
 */

/**
 * Creates lists of flower species.
 * Some are predefined, others are randomly generated.
 */
public class ObjectGenerator {

    //hardcode some values, use an array for simplicity, yes everything has to be a double just live with it:
    //                              y,    c-,  c+,  f-,  f+,  h-,  h+,  q,    p  brightness
    private final double[][] F = {{10.0, 1.0, 10.0, 0.1, 0.9, 40, 350, 0.033, 0.5, 1.0},
            {7.0, 2.0, 11.0, 0.2, 0.8, 37, 410, 0.060, 0.7, 1.5},
            {11.0, 2.0, 9.0, 0.3, 0.9, 60, 450, 0.022, 0.5, 2.0},
            {9.0, 3.4, 15.0, 0.1, 0.6, 55, 390, 0.0198, 0.5, 2.0}};

    /**
     * Generates a group of flower species.
     *
     * @param num_group Used for random seed and group size.
     * @return List of generated Flowerspecies objects.
     */
    public ArrayList<FlowerSpecies> generatePlantGroups(int num_group) {
        //keep the randomness contained, so different people don't have different values
        Random rand = new Random(num_group);
        int initCapacity = 10 + (num_group % 16); // ensure between 10 and 25

        //initial capacity shouldn't really matter since its dynamic but what ever performance right?
        ArrayList<FlowerSpecies> plantGroup = new ArrayList<>();

        //create individual PlantSpecies objects.
        for (int i = 0; i < initCapacity; i++) {

            //deals with hardcoded values
            if (i < F.length) {
                double[] v = F[i];
                plantGroup.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9]));
            } else {
                //deals with randomly generated values
                double[] v = generateFlowerValues(rand);
                plantGroup.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9]));
            }
        }
        return plantGroup;
    }

    /**
     * Generates bees of different species
     */
    // TODO: Implement generation of bees
    public ArrayList<Bee> generateBees(int num_bees) {
        Random rand = new Random(num_bees);

        ArrayList<Bee> bees = new ArrayList<>();

        for (int i = 0; i < num_bees; i++) {
            double[] v = generateBeeValues(rand);
            bees.add(new Bee(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9], v[10]));
        }

        return bees;
    }

    /**
     * Generates random flower parameters for variety.
     */
    // Assignment asks to consider different approach to generate random values (Choose distribution that more closely
    // resembles nature)
    // TODO: Check how to generate random values (e.g. using Gaussian distribution)
    private double[] generateFlowerValues(Random rand) {
        //set array with the size of the hardcoded array, if changes are needed
        double[] values = new double[9];

        //rand.nextDouble() * (max - min) + min;

        //y v[0]:
        values[0] = rand.nextGaussian() * (20.0 - 3.0) + 3.0;
        //c_min v[1] and c_max v[2]:
        //1.0 < c_min < 6.0
        values[1] = rand.nextGaussian() * (6.0 - 1.0) + 1.0;
        //c_min < c_max < 20.0
        values[2] = rand.nextGaussian() * (20.0 - values[1]) + values[1];

        //f_min [v3] and f_max v[4]:
        //0 < f_min < f_max < 1
        values[3] = rand.nextGaussian() / 2.0; //so f_min is 0.0-0.5 since rand goes from 0.0-1.0
        values[4] = rand.nextGaussian() * (1.0 - values[3]) + values[3];

        //h_min [v5] and h_max [v6]:
        values[5] = rand.nextGaussian() * (100.0 + 10.0) + 10.0; //so the min_bloomtime goes from 0.0-100.0
        values[6] = rand.nextGaussian() * (500.0 - values[5]) + values[5];

        //q[7] 0 < q < 1/15 (0.0666)
        values[7] = rand.nextGaussian() * (0.0666);
        //p[8] 0 < p < 1/h_max - h_min
        double denom = values[6] - values[5];
        if (denom < 1.0) denom = 1.0;
        values[8] = rand.nextGaussian() * (1.0 / denom);

        values[9] = rand.nextGaussian();

        return values;
    }

    // TODO: Find out how to generate values here
    private double[] generateBeeValues(Random rand) {
        double[] values = new double[9];

        // Population of bees
        values[0] = 20.0 + rand.nextGaussian() * 75.0;

        // Active period of bees
        values[1] = rand.nextGaussian() * 120.0;
        values[2] = values[1] + rand.nextGaussian() * 240;

        // Moisture requirements for nesting
        values[3] = rand.nextGaussian() * 150.0;
        values[4] = 50.0 + rand.nextGaussian() * 150.0;

        // Sun requirements for nesting. Maybe it's better not to bother with these!
        values[5] = rand.nextGaussian();
        values[6] = rand.nextGaussian();

        // Distance limits between nest and food for nesting. Not sure how it's supposed to work
        values[7] = 5.0 + rand.nextGaussian() * 100.0;
        values[8] = values[7] + rand.nextGaussian() * 300.0;

        // Intensity limits for preferred colors. Leave it nextDouble for now
        values[9] = rand.nextDouble();
        values[10] = rand.nextDouble();

        return values;
    }

}