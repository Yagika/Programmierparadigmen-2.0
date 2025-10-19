import Problem1.Flowerspecies;
import Problem1.SimulationLogic;
import Problem1.objectGenerator;
import Problem1.Flowerspecies;
import Problem1.objectGenerator;

import java.util.ArrayList;
import java.util.Random;

public class Test{

    public static void main(String[] args){

        double n = 0;
        int num_years;
        double x;
        Random rand = new Random();

        //create object to create a group
        objectGenerator obj = new objectGenerator();

        //store the created Arraylist where all the generated Flowerspecies objects are
        ArrayList<Flowerspecies> Group1 = new ArrayList<Flowerspecies>();

        // TODO: create several groups of plants and run simulations on each

        Group1 = obj.generatePlantGroups(1);

        //Run 10 simulation with different weather conditions
        for (int i = 0; i < 1; i++) {
            //Define simulation parameters

            //What should be the number of bees?
            x = rand.nextDouble() * 100;

            //Simulate 25 years
            num_years = 25;

            SimulationLogic simulator = new SimulationLogic();

            //Run simulation
            simulator.simulate(num_years, x, Group1);

            System.out.println("Results of the " + i + " simulation");
            for (int j = 0; j < Group1.size(); j++) {
                System.out.println(Group1.get(j).toString());
            }
        }

        // TODO: Save information about the simulations

    }


}
