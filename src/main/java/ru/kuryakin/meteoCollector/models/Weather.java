package ru.kuryakin.meteoCollector.models;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/***
 * Данные о погоде.
 */
public class Weather {

    /***
     * Формат вывода даты.
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");

    private long id;
    private GregorianCalendar date;
    private Double temperature, humidity, pressure;
    private Location location;

    public Weather() {
    }

    public Weather(long id, GregorianCalendar date, Double temperature, Double humidity, Double pressure, Location location) {
        this.id = id;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", date=" + dateFormat.format(date.getTime()) +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", location=" + location +
                '}';
    }
}
