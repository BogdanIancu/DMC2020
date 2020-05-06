package ro.mta.secondapplication.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ro.mta.secondapplication.models.converters.Converters;
import ro.mta.secondapplication.models.daos.WeatherForecastDao;

@Database(entities = { WeatherForecast.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherForecastDao weatherForecastDao();
}
