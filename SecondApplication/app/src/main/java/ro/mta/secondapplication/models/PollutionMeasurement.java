package ro.mta.secondapplication.models;

import java.io.Serializable;
import java.util.Date;

public class PollutionMeasurement implements Serializable {
    private String area;
    private Date date;
    private int value;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
