package Problem1.Pollinators;

import Problem1.Weather;

// Class to implement bee species
public class Bee {


    public String name; //a way to differentiate between bees in console.
    public double population; // Number of bees of this kind in the population
    public double activeFrom, activeTo; // Time of the year when bees are active. 0 <= activeFrom <= activeTo <= 240

    public double x, y; // Coordinates of the bee nest

    public double activity; // Multiplier that bees get because of some condition (e.g. time of year). Use it to increase pollination for example
    public double effectiveness; //some bees are more effective than others.

    //Somehow create flower preferences. Maybe color? Maybe not needed for us
    public double c_lower, c_upper; // Limits of preferred color intensity


    public Bee(double population, double activeFrom, double activeTo, double x, double y, double c_lower, double c_upper, double effectiveness) {
        this.population = population;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
        this.x = x;
        this.y = y;
        this.c_lower = c_lower;
        this.c_upper = c_upper;
        this.effectiveness = effectiveness;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    //calculate the activity of bees depending on weather condition.
    //they work less the colder it is, simplified in weather events.
    public void calculate_activity(int day, Weather weather){

        switch(weather.event) {
            case WEATHER_SUNNY -> activity = 1.0;
            case WEATHER_PARTLY_CLOUDY -> activity = 0.8;
            case WEATHER_CLOUDY -> activity = 0.7;
            case WEATHER_RAINY -> activity = 0.5;
            case WEATHER_STORMY -> activity = 0.2;
        }

    }

}
