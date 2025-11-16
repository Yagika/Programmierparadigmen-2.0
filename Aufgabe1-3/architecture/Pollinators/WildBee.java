package architecture.Pollinators;

import architecture.Weather;

/**
 * Represents a general wild bee species.
 * STYLE: object-oriented – subclass specialization of Architecture.Classes.Bee.
 */
public class WildBee extends Bee {

    public WildBee(String name, double population, double activeFrom, double activeTo,
                   double x, double y, double c_lower, double c_upper, double effectiveness) {
        super(name, population, activeFrom, activeTo, x, y, c_lower, c_upper, effectiveness);
    }

    // Slightly more sensitive to weather than honeybees
    @Override
    public void updateActivity(int day, Weather weather) {
        super.updateActivity(day, weather);
        // BAD: too weather-sensitive – may cause instability in simulation
        if (weather != null && weather.event == Weather.WeatherEvents.WEATHER_STORMY)
            activity *= 0.5;
    }
}
