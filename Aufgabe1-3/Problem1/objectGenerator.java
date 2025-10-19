package Problem1;

import java.util.*;
import java.util.ArrayList;
import Problem1.Flowerspecies;

/*
Generate an object to handle a collection (Group) of flowerspecies.

an array should suffice?


We need to generate 3 groups. That means 3 ArrayLists that hold varying Flowerspecies objects. These ArrayLists should
have a size ranging from 10-25. These should probably have different kinds of Flowerspecies. So I am thinking lets do the
bare minimum and have about 12-15 per group.
 */

public class objectGenerator{

    //hardcode some values, use an array for simplicity, yes. everything has to be a double just live with it:
    //                            y,    c-,     c+,     f-,     f+,     h-,     h+,     q,      p
    double[][] F = {            {10.0,  1.0,    10.0,   0.1,    0.9,    40,     350,    0.033,  0.5},
                                {7.0,   2.0,    11.0,   0.2,    0.8,    37,     410,    0.060,  0.7},
                                {11.0,  2.0,    9.0,   0.3,    0.9,    60,    450,    0.022,  0.5},
                                {9.0,   3.4,    15.0,   0.1,    0.6,    55,    390,    0.0198,  0.5}};

    //Returns an ArrayList filled with FlowerSpecies objects.
    public ArrayList<Flowerspecies> generatePlantGroups(int num_group){
        //keep the randomness contained, so different people don't have different values
        Random rand = new Random(num_group);
        int initCapacity = 12 + num_group + 1;

        //initial capacity shouldn't really matter since its dynamic but what ever performance right?
        ArrayList<Flowerspecies> plantGroup = new ArrayList<Flowerspecies>(initCapacity);

        //create individual PlantSpecies objects.
        for(int i = 0; i < initCapacity; i++){

            if(i < F.length){
                Flowerspecies species = new Flowerspecies(F[i][0],F[i][1],F[i][2],F[i][3],F[i][4],F[i][5],F[i][6],F[i][7],F[i][8]);
                plantGroup.add(species);
            }

        }


        return plantGroup;
    }

}