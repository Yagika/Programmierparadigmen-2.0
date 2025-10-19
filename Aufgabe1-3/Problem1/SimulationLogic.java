package Problem1;
import java.util.ArrayList;
import java.util.Random;

//use this class to write the methods that are used for simulating the logic in Test.java.

public class SimulationLogic {

    public void simulate(int num_years, double x, ArrayList<Flowerspecies> plant_group) {

        Random rand = new Random();

        double n = 0; //Food supply (Nahrungsangebot)
        double d;     //Sunshine hours in a day
        double h; //Sunshine hours in a season

        for (int j = 0; j < num_years; j++) {

            //Set soil moisture in the beginning of the vegetation period
            //0 <= f <= 1
            double f = rand.nextDouble();

            h = 0;

            //Simulate one year (vegetation period (240 days) and winter)
            for (int k = 0; k < 240; k++) {

                //Calculate food supply (Nahrungsangebot)
                n = calculate_food_supply(plant_group, n);

                //Update the number of bees
                x = calculate_bees(x, n);

                //Update f. Changes daily by up to 10% (0.9 to 1.1)
                f *= (rand.nextDouble() * 0.2 + 0.9);

                //Calculate number of sunny hours in a day
                d = rand.nextDouble() * 12;

                //Sum up d to get number of sunny hours per vegetation period
                h += d;

                //Perform calculations for each plant species
                for (Flowerspecies plant : plant_group) {
                    plant.moisture_threshold(f);
                    plant.bloom_time(h, d);
                    plant.pollination_probability(x, n, d);
                }

            }
            //Bees dying during winter
            x *= (rand.nextDouble() * 0.2 + 0.1);
            //Resting phase for plants
            for (Flowerspecies plant : plant_group) {
                plant.resting_phase(1);
            }

        }
    }

    public double calculate_bees(double x, double n) {
        if (n >= x) {
            x = 1.03 * x;
        } else {
            x = (6 * n / x - 3) * x;
        }
        return x;
    }

    public double calculate_food_supply (ArrayList<Flowerspecies> plant_group, double n) {
        for (Flowerspecies flowerspecies : plant_group) {
            n += flowerspecies.foodvalue();
        }
        return n;
    }
}
