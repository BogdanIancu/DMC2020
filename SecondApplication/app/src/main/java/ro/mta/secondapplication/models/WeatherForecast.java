package ro.mta.secondapplication.models;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class WeatherForecast {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    private int temperature;
    @ColumnInfo(name = "felt_temperature")
    private int feltTemperature;
    private String conditions;
    private Bitmap image;
    private Date date;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFeltTemperature() {
        return feltTemperature;
    }

    public void setFeltTemperature(int feltTemperature) {
        this.feltTemperature = feltTemperature;
    }
}
