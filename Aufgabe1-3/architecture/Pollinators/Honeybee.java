package architecture.Pollinators;

import architecture.Weather;

/**
 * Represents a honeybee population.
 * Honeybees are less effective pollinators than wild bees
 */
public class Honeybee extends Bee {

    /**
     * Constructs a honeybee population with slightly reduced effectiveness
     * but larger colony size compared to wild bees.
     */
    public Honeybee(String name, double population, double activeFrom, double activeTo,
                    double x, double y, double c_lower, double c_upper, double effectiveness) {
        super(name, population, activeFrom, activeTo, x, y, c_lower, c_upper,
                Math.min(1.0, effectiveness * 0.85));
    }

    // STYLE: Object-oriented paradigm – subclass overrides behavior of base class.
    @Override
    public void updateActivity(int day, Weather weather) {
        super.updateActivity(day, weather);
        // GOOD: override reduces sensitivity to bad weather – realistic
        if (weather != null && weather.getEvent() == Weather.WeatherEvents.WEATHER_RAINY)
            scaleActivity(0.9);
    }
}