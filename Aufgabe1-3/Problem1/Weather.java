package Problem1;

import java.util.Random;

public class Weather {

    public double temperature;
    public double previous_temp;
    public double humidity;
    public WeatherEvents event;

    //these events happen depending on a few factors:
    public enum WeatherEvents{
        WEATHER_SUNNY,
        WEATHER_PARTLY_CLOUDY,
        WEATHER_CLOUDY,
        WEATHER_RAINY,
        WEATHER_STORMY,
    }

    //gets the weather for the next day based on the current day.
    //random MUST be between 0.0 and 1.0, so use random.nextDouble()
    public void NextWeatherEvent(int day, double random){

        switch(event){
            case WEATHER_SUNNY:
                if (random < 0.7){
                    break; //KEEP IT SUNNY
                }else if(random > 0.7 && random < 0.9){
                    event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                }else event = WeatherEvents.WEATHER_CLOUDY;
                break;

            case WEATHER_PARTLY_CLOUDY:
                if(random < 0.3){
                    event = WeatherEvents.WEATHER_SUNNY;
                }else if(random > 0.3 && random < 0.6){
                    break; //KEEP IT PARTLY_CLOUDY
                }else if(random > 0.6){
                    event = WeatherEvents.WEATHER_CLOUDY;
                }
                break;

            case WEATHER_CLOUDY:
                if(random < 0.1){ //10% chance SUNNY
                    event = WeatherEvents.WEATHER_SUNNY;
                }else if(random > 0.1 && random < 0.2){ //10% chance PARTLY CLOUDY
                    event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                }else if(random > 0.2 && random < 0.5){ //30% KEEP THE SAME
                    break; //KEEP IT CLOUDY
                }else if(random > 0.5 && random < 0.8){
                    event = WeatherEvents.WEATHER_RAINY;
                }else if(random > 0.8){
                    event = WeatherEvents.WEATHER_STORMY;
                }
                break;

            case WEATHER_RAINY:
                if(random < 0.1){
                    event = WeatherEvents.WEATHER_SUNNY;
                }else if(random > 0.1 && random < 0.2){
                    event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                }else if(random > 0.2 && random < 0.4){
                    event = WeatherEvents.WEATHER_CLOUDY;
                }else if(random > 0.4 && random < 0.8){
                    break; //KEEP IT RAINY
                }else if(random > 0.8){
                    event = WeatherEvents.WEATHER_STORMY;
                }
                break;

            case WEATHER_STORMY:
                if(random < 0.2){
                    event = WeatherEvents.WEATHER_PARTLY_CLOUDY;
                }else if(random > 0.2 && random < 0.3){
                    event = WeatherEvents.WEATHER_CLOUDY;
                }else if(random > 0.3 && random < 0.8){
                    event = WeatherEvents.WEATHER_RAINY;
                }else if(random > 0.8){
                    break; //KEEP IT STORMY
                }
                break;

            default:
                break;
        }

    }


    //calculates temperature based on current weather event, and on previous temperature.
    public void NextTemperature(int day, double random){
        double tempChange = random * 0.9 + (previous_temp);

        switch (event) {
            case WEATHER_SUNNY:
                temperature = tempChange + 4.0; break;
            case WEATHER_PARTLY_CLOUDY:
                temperature = tempChange; break;
            case WEATHER_CLOUDY:
                temperature = tempChange - 0.5; break;
            case WEATHER_RAINY:
                temperature = tempChange - 1; break;
            case WEATHER_STORMY:
                temperature = tempChange - 2; break;
            default:
                temperature = random * 0.4 + (previous_temp * 0.6); break;
        };
    }


    private String getWeatherType(WeatherEvents event){
        return switch (event) {
            case WEATHER_SUNNY -> "Sunny";
            case WEATHER_PARTLY_CLOUDY -> "Partly Cloudy";
            case WEATHER_CLOUDY -> "Cloudy";
            case WEATHER_RAINY -> "Rainy";
            case WEATHER_STORMY -> "Storm";
            default -> "Unknown";
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
    public double gethumidity() {
        return humidity;
    }


    //CONSTRUCTOR
    public Weather(double temperature, double humidity, WeatherEvents event) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.previous_temp = temperature;
        this.event = event;
    }

    @Override
    public String toString() {
        int temp = (int)(temperature*100.0);
        double shortDouble = ((double)temp)/100.0;

        return "Weather is currently: " + getWeatherType(event) + " with " + shortDouble + "°C and " + humidity + "% humidity";
    }


}
