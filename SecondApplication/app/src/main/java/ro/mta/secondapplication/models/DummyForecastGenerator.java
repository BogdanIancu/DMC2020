package ro.mta.secondapplication.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class DummyForecastGenerator {
    private static Random random = new Random();

    public static List<WeatherForecast> getForecast() {
        List<WeatherForecast> list = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            WeatherForecast forecast = new WeatherForecast();
            int temperature = random.nextInt(31);
            forecast.setTemperature(temperature);
            String conditions = (temperature % 2 == 0) ? "clear sky" : "clouds";
            forecast.setConditions(conditions);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            forecast.setDate(calendar.getTime());
            list.add(forecast);
        }

        return list;
    }
}
