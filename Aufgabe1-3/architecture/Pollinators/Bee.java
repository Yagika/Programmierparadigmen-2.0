package architecture.Pollinators;

import architecture.Weather;

/**
 * Represents a generic bee species.
 * STYLE: object-oriented – serves as abstract base for all pollinator types.
 */
public abstract class Bee implements Pollinator {

    // NOTE: Using protected visibility allows subclasses to reuse fields.
    protected String name; //a way to differentiate between bees in console.
    public double population; // Number of bees of this kind in the population
    protected double activeFrom, activeTo; // Time of the year when bees are active. 0 <= activeFrom <= activeTo <= 240
    protected double x, y; // Coordinates of the bee nest
    public double activity; // Multiplier that bees get because of some condition (e.g. time of year). Use it to increase pollination for example
    protected double effectiveness; //some bees are more effective than others.

    //Somehow create flower preferences. Maybe color? Maybe not needed for us
    public double c_lower, c_upper; // Limits of preferred color intensity

    /**
     * Constructs a new bee species with biological and spatial parameters.
     */
    public Bee(String name, double population, double activeFrom, double activeTo, double x, double y, double c_lower, double c_upper, double effectiveness) {
        this.name = name;
        this.population = Math.max(5.0, population);
        int aF = (int) Math.round(Math.max(0, Math.min(240, activeFrom)));
        int aT = (int) Math.round(Math.max(0, Math.min(240, activeTo)));
        if (aT < aF) {
            int tmp = aF;
            aF = aT;
            aT = tmp;
        }
        this.activeFrom = aF;
        this.activeTo = aT;
        this.x = x;
        this.y = y;
        this.c_lower = Math.min(c_lower, c_upper);
        this.c_upper = Math.max(c_lower, c_upper);
        this.effectiveness = Math.max(0.3, Math.min(1.5, effectiveness));
        this.activity = 0.8;
    }

    // GOOD: class encapsulates full bee behavior and data
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getPopulation() {
        return population;
    }

    @Override
    public void setPopulation(double v) {
        // NOTE: population must never drop below 5
        this.population = Math.max(5.0, v);
    }

    @Override
    public double getActivity() {
        return activity;
    }

    @Override
    public double getEffectiveness() {
        return effectiveness;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    /**
     * Species-specific preference helpers (not in the interface).
     */
    public double getPrefLower() {
        return c_lower;
    }

    public double getPrefUpper() {
        return c_upper;
    }

    @Override
    public boolean isActive(int day) {
        return day >= activeFrom && day <= activeTo;
    }


    /**
     * Updates population growth or decline for the next day,
     * depending on available food and reserves.
     * BAD: updatePopulation uses hardcoded constants – low flexibility
     */
    @Override
    public void updatePopulation(double n, double reserve) {
        if (population <= 0.0) { population = 5.0; return; }

        double availableFood = Math.max(0.0, n) + Math.max(0.0, reserve) * 0.01;
        double demand = population * 0.2;
        double ratio = (demand > 0) ? availableFood / demand : 0.0;

        double growthRate;
        if (ratio >= 1.1) growthRate = 1.02 + 0.03 * Math.tanh(ratio - 1.0);
        else if (ratio >= 0.8) growthRate = 1.00;
        else growthRate = Math.max(0.6, 0.9 + 0.1 * (ratio / 0.8));

        double newPop = population * growthRate;
        if (!Double.isFinite(newPop)) newPop = 10.0;
        population = Math.max(5.0, newPop);
    }


    /**
     * Updates bee activity as a smooth bell-shaped function
     * of seasonal period and weather influence.
     */
    @Override
    public void updateActivity(int day, Weather weather) {
        if (isActive(day)) {
            double mid = (activeFrom + activeTo) / 2.0;
            double span = Math.max(1.0, activeTo - activeFrom);
            double norm = 1.0 - Math.min(1.0, Math.abs(day - mid) / (span / 2.0));
            // 0.5..1.5
            activity = 0.5 + Math.max(0, norm);
        } else {
            activity = 0.5;
        }

        if (weather != null) {
            switch (weather.event) {
                case WEATHER_SUNNY -> activity *= 1.0;
                case WEATHER_PARTLY_CLOUDY -> activity *= 0.9;
                case WEATHER_CLOUDY -> activity *= 0.8;
                case WEATHER_RAINY -> activity *= 0.6;
                case WEATHER_STORMY -> activity *= 0.3;
            }
        }

        activity = Math.max(0.1, Math.min(1.8, activity));
    }

}
