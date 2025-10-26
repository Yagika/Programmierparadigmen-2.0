package Problem1.Pollinators;

// Class to implement bee species
public class Bee {

    public double population; // Number of bees of this kind in the population
    double activeFrom, activeTo; // Time of the year when bees are active. 0 <= activeFrom <= activeTo <= 240

    double x, y; // Coordinates of the bee nest

    public double activity; // Multiplier that bees get because of some condition (e.g. time of year). Use it to increase pollination for example

    //Somehow create flower preferences. Maybe color?
    public double c_lower, c_upper; // Limits of preferred color intensity


    public Bee(double population, double activeFrom, double activeTo, double x, double y, double c_lower, double c_upper) {
        this.population = population;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
        this.x = x;
        this.y = y;
        this.c_lower = c_lower;
        this.c_upper = c_upper;
    }

    /**
     * Calculates the number of bees for the next day.
     */
    public void calculate_population(double population, double n, double reserve) {
        if (population <= 0.0) return;
        double availableFood = n + reserve * 0.01;
        double demand = population * 0.2;
        double ratio = demand > 0 ? (availableFood / demand) : 0.0;

        double growthRate;
        if (ratio >= 1.1) {
            growthRate = 1.02 + 0.03 * Math.tanh((ratio - 1.0));
        } else if (ratio >= 0.8) {
            growthRate = 1.00;
        } else {
            growthRate = 0.98 * ratio + 0.6 * 0.1;
        }

        double newPopulation = population * growthRate;

        if (Double.isNaN(newPopulation) || Double.isInfinite(newPopulation)) newPopulation = 10.0;

        if (newPopulation < 5.0) newPopulation = 5.0;

        this.population = newPopulation;
    }

    // TODO: maybe make it so that the multiplier increases towards the middle of the active period, and decreases afterwards
    public void calculate_multiplier (int current_day) {
        if (activeFrom <= current_day  && current_day <= activeTo) {this.activity = 1.5;}
        else {this.activity = 0.75;}
    }

}
