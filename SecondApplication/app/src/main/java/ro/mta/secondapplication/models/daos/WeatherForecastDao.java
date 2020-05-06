package ro.mta.secondapplication.models.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ro.mta.secondapplication.models.WeatherForecast;

@Dao
public interface WeatherForecastDao {
    @Query("SELECT * FROM weatherforecast")
    List<WeatherForecast> getAll();

    @Insert
    void insert(WeatherForecast... forecasts);

    @Delete
    void delete(WeatherForecast... forecasts);

    @Query("DELETE FROM weatherforecast")
    void deleteAll();
}
