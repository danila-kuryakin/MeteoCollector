package ru.kuryakin.meteoCollector.dao;


import ru.kuryakin.meteoCollector.models.Weather;

public interface WeatherDAO {
    /***
     * Добавление данных о погоде в базу данных
     * @param weather - данные о погоде.
     */
    void createWeather(Weather weather);
}
