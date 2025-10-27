package testArchitecture;

import architecture.Weather;
import java.util.Random;

/**
 * Test for the Weather model.
 *
 * STYLE: procedural – quick standalone check independent of the main simulation.
 */
public class TestWeather {

    public static void main(String[] args) {

        Weather weather = new Weather(10.0, 30.0, Weather.WeatherEvents.WEATHER_SUNNY);
        Random rand = new Random(42);

        for (int i = 0; i < 100; i++) {
            weather.NextTemperature(i, rand.nextGaussian() * 2.0); //std dev of 2.0 C
            weather.NextWeatherEvent(i, rand.nextDouble());
            System.out.println(weather);
        }

    }
}
