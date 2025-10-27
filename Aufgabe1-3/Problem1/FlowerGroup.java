package Problem1;

import Problem1.Pollinators.Bee;
import Problem1.ObjectGenerator;

import java.util.ArrayList;
import java.util.Random;

public class FlowerGroup {

    public int MAX_DISTANCE = 2000;

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
     * Use this method to simulate bees leaving the nest to pollinate different FlowerGroups that are a distance away.
     * @param speciesList
     * @param bees
     */

    public void pollinationProbability (ArrayList<FlowerSpecies> speciesList, ArrayList<Bee> bees, double d) {

        double total_bees = 0;
        //every type of bee goes to this one Flower Group (Patch of flowers)
        for(int i = 0; i < bees.size(); i++){

            double distance_x = Math.abs(this.x - bees.get(i).x);
            double distance_y = Math.abs(this.y - bees.get(i).y);
            //simple pythagoras easy as, gets the distance from the bees to the flowerGroup:
            //this is the distance for ALL the flowers in this one group
            double total_distance = Math.sqrt(Math.pow(distance_x, 2) + Math.pow(distance_y, 2));

            //Depending on the distance, the bees should get less effective, since less of them
            //would be travelling that far to these plants.
            //current_effectiveness = effectiveness * activity * (abs(total_distance - MAX_DISTANCE))/MAX_DISTANCE
            //this should insure a range between 0.0-1.0, in rare cases it might go over 1.0.
            double current_effectiveness = bees.get(i).effectiveness * bees.get(i).activity * (Math.abs(total_distance-MAX_DISTANCE)/MAX_DISTANCE);

            //based on the effectiveness we calculate the amount of bees that are going to this place:

            total_bees += bees.get(i).population * current_effectiveness;
        }

        for(int j = 0; j < speciesList.size(); j++){
            speciesList.get(j).pollination_probability(bees, total_bees, calculateTotalFood(), d);

        }

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

    public double calculateTotalFood () {
        n = 0.0;
        for (FlowerSpecies flower : speciesList) {
            n += flower.foodvalue();
        }
        return n;
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
