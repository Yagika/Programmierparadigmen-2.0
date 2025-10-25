package Problem1.Pollinators;

// Class to implement bee species
public class Bee {

    // Not sure how to implement those
    public double population; // Number of bees in the population
    double activeFrom, activeTo; // Time of the year when bees are active. 0 <= activeFrom <= activeTo <= 240
    double f_lower, f_upper; // Moisture limits for nesting
    double h_lower, h_upper; // Sunlight limits for nesting
    double d_lower, d_upper; // Distance limits from food for nesting

    //Somehow create flower preferences?


    public Bee(double population, double activeFrom, double activeTo, double f_lower, double f_upper, double h_lower, double h_upper,
               double d_lower, double d_upper) {
        this.population = population;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
        this.f_lower = f_lower;
        this.f_upper = f_upper;
        this.h_lower = h_lower;
        this.h_upper = h_upper;
        this.d_lower = d_lower;
        this.d_upper = d_upper;
    }

    /**
     * Calculates the number of bees for the next day.
     */
    public double calculate_population(double population, double n, double reserve) {
        if (population <= 0.0) return 10.0;
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

        return Math.round(newPopulation);
    }


}
