import Problem1.FlowerGroup;
import Problem1.FlowerSpecies;
import Problem1.Pollinators.Bee;
import Problem1.SimulationLogic;
import Problem1.ObjectGenerator;
import Problem1.Weather;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

public class TESTING {

    //testing for weather.
    public static void main(String[] args) {

        Weather weather = new Weather(10.1, 30, Weather.WeatherEvents.WEATHER_SUNNY);
        Random rand = new Random(42);



        for(int i = 0; i < 100; i++){
            weather.NextTemperature(i, rand.nextGaussian(0, 2.0)); //std dev of 2.0 C
            weather.NextWeatherEvent(i, rand.nextDouble());

            System.out.println(weather);
        }

    }
}
