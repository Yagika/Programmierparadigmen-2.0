package architecture.Pollinators;

import architecture.Weather;

/**
 * Represents bumblebees (robust and cold-tolerant).
 * STYLE: object-oriented – specialization with overridden behavior.
 */
public class BumbleBee extends Bee {

    public BumbleBee(String name, double population, double activeFrom, double activeTo,
                     double x, double y, double c_lower, double c_upper, double effectiveness) {
        super(name, population, activeFrom, activeTo, x, y, c_lower, c_upper,
                Math.min(1.5, effectiveness * 1.2));
    }

    @Override
    public void updateActivity(int day, Weather weather) {
        super.updateActivity(day, weather);
        // GOOD: strong weather tolerance, keeps system stable under variation
        if (weather != null && weather.temperature < 10)
            activity *= 1.2;
    }
}
