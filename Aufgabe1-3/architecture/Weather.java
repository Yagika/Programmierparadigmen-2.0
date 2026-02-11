package architecture;

/**
 * Simple weather model with discrete events and temperature evolution.
 * Weather influences pollinator activity and soil moisture dynamics.
 *
 * STYLE: object-oriented – encapsulates state machine and derived quantities.
 */
public class Weather {

    private double temperature;
    private double previous_temp;
    private double humidity;
    private WeatherEvents event;

    /**
     * Enumeration of all weather event types.
     */
    public enum WeatherEvents {
        WEATHER_SUNNY,
        WEATHER_PARTLY_CLOUDY,
        WEATHER_CLOUDY,
        WEATHER_RAINY,
        WEATHER_STORMY,
    }

    public Weather(double temperature, double humidity, WeatherEvents event) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.previous_temp = temperature;
        this.event = event;
    }

    /**
     * Determines the next weather event based on random chance
     * and the current state of the weather system.
     */
    public void NextWeatherEvent(int day, double random) {

        switch (event) {
            case WEATHER_SUNNY:
                if (random < 0.7) break; // KEEP IT SUNNY
                else if (random < 0.9) event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                else event = WeatherEvents.WEATHER_CLOUDY;
                break;

            case WEATHER_PARTLY_CLOUDY:
                if (random < 0.3) event = WeatherEvents.WEATHER_SUNNY;
                else if (random < 0.6) break; // KEEP IT PARTLY_CLOUDY
                else event = WeatherEvents.WEATHER_CLOUDY;
                break;

            case WEATHER_CLOUDY:
                if (random < 0.1) event = WeatherEvents.WEATHER_SUNNY; // 10% chance SUNNY
                else if (random < 0.2) event = WeatherEvents.WEATHER_PARTLY_CLOUDY; // 10% chance PARTLY CLOUDY
                else if (random < 0.5) break; // KEEP IT CLOUDY 30% KEEP THE SAME
                else if (random < 0.8) event = WeatherEvents.WEATHER_RAINY;
                else event = WeatherEvents.WEATHER_STORMY;
                break;

            case WEATHER_RAINY:
                if (random < 0.1) event = WeatherEvents.WEATHER_SUNNY;
                else if (random > 0.1 && random < 0.2) event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                else if (random < 0.4) event = WeatherEvents.WEATHER_CLOUDY;
                else if (random < 0.8) break; // KEEP IT RAINY
                else event = WeatherEvents.WEATHER_STORMY;
                break;

            case WEATHER_STORMY:
                if (random < 0.2) event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                else if (random < 0.3) event = WeatherEvents.WEATHER_CLOUDY;
                else if (random < 0.8) event = WeatherEvents.WEATHER_RAINY;
                else break; // KEEP IT STORMY
                break;

            default:
                break;
        }

    }


    /**
     * Updates temperature depending on weather event.
     */
    public void NextTemperature(int day, double random) {
        double base = previous_temp + random;
        switch (event) {
            case WEATHER_SUNNY -> temperature = base + 3.0;
            case WEATHER_PARTLY_CLOUDY -> temperature = base + 1.0;
            case WEATHER_CLOUDY -> temperature = base;
            case WEATHER_RAINY -> temperature = base - 1.0;
            case WEATHER_STORMY -> temperature = base - 2.0;
        }
        previous_temp = temperature;
    }


    private String getWeatherType(WeatherEvents event) {
        return switch (event) {
            case WEATHER_SUNNY -> "Sunny";
            case WEATHER_PARTLY_CLOUDY -> "Partly Cloudy";
            case WEATHER_CLOUDY -> "Cloudy";
            case WEATHER_RAINY -> "Rainy";
            case WEATHER_STORMY -> "Storm";
        };
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        int temp = (int) (temperature * 100.0);
        double shortDouble = ((double) temp) / 100.0;

        return "Weather is currently: " + getWeatherType(event) + " with " + shortDouble + "°C and " + humidity + "% humidity";
    }

    public WeatherEvents getEvent() {
        return event;
    }
}
