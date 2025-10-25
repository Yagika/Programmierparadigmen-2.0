package Problem1;

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
    //                              y,    c-,  c+,  f-,  f+,  h-,  h+,  q,    p
    private final double[][] F = {{10.0, 1.0, 10.0, 0.1, 0.9, 40, 350, 0.033, 0.5},
            {7.0, 2.0, 11.0, 0.2, 0.8, 37, 410, 0.060, 0.7},
            {11.0, 2.0, 9.0, 0.3, 0.9, 60, 450, 0.022, 0.5},
            {9.0, 3.4, 15.0, 0.1, 0.6, 55, 390, 0.0198, 0.5}};

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
                plantGroup.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8]));
            } else {
                //deals with randomly generated values
                double[] v = generateValues(rand);
                plantGroup.add(new FlowerSpecies(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8]));
            }
        }
        return plantGroup;
    }

    /**
     * Generates random flower parameters for variety.
     */
    private double[] generateValues(Random rand) {
        //set array with the size of the hardcoded array, if changes are needed
        double[] values = new double[9];

        //rand.nextDouble() * (max - min) + min;

        //y v[0]:
        values[0] = rand.nextDouble() * (20.0 - 3.0) + 3.0;
        //c_min v[1] and c_max v[2]:
        //1.0 < c_min < 6.0
        values[1] = rand.nextDouble() * (6.0 - 1.0) + 1.0;
        //c_min < c_max < 20.0
        values[2] = rand.nextDouble() * (20.0 - values[1]) + values[1];

        //f_min [v3] and f_max v[4]:
        //0 < f_min < f_max < 1
        values[3] = rand.nextDouble() / 2.0; //so f_min is 0.0-0.5 since rand goes from 0.0-1.0
        values[4] = rand.nextDouble() * (1.0 - values[3]) + values[3];

        //h_min [v5] and h_max [v6]:
        values[5] = rand.nextDouble() * (100.0 + 10.0) + 10.0; //so the min_bloomtime goes from 0.0-100.0
        values[6] = rand.nextDouble() * (500.0 - values[5]) + values[5];

        //q[7] 0 < q < 1/15 (0.0666)
        values[7] = rand.nextDouble() * (0.0666);
        //p[8] 0 < p < 1/h_max - h_min
        double denom = values[6] - values[5];
        if (denom < 1.0) denom = 1.0;
        values[8] = rand.nextDouble() * (1.0 / denom);

        return values;
    }

}