package ru.kuryakin.meteoCollector.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.kuryakin.meteoCollector.models.Weather;
import ru.kuryakin.meteoCollector.utils.WeatherMapper;

import javax.sql.DataSource;
import java.util.GregorianCalendar;
import java.util.List;

/***
 * Реализация интерфейса WeatherDAO.
 */
public class TemplateWeatherDAO implements WeatherDAO{


    private JdbcTemplate jdbcTemplate;

    public TemplateWeatherDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /***
     * Добавление данных о погоде в базу данных
     * @param weather - данные о погоде.
     */
    public void createWeather(Weather weather) {
        String sql = "insert into weather (temperature, humidity, pressure, date, location) values(?, ?, ?, ?, ?);";
        GregorianCalendar date = new GregorianCalendar();
        jdbcTemplate.update(sql, weather.getTemperature(), weather.getHumidity(), weather.getPressure(),
                date, weather.getLocation().getId());
    }
}
