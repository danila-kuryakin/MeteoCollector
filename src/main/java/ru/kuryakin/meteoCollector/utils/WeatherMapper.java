package ru.kuryakin.meteoCollector.utils;

import org.springframework.jdbc.core.RowMapper;
import ru.kuryakin.meteoCollector.models.Location;
import ru.kuryakin.meteoCollector.models.Weather;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

/***
 * Обработка Заброса из БД.
 */
public class WeatherMapper implements RowMapper {
    /***
     * @return Данные о погоде.
     */
    public Weather mapRow(ResultSet resultSet, int i) throws SQLException {
        Weather weather = new Weather();
        Date date = resultSet.getTimestamp("date");
        GregorianCalendar calendar = new GregorianCalendar();
        if (date != null) {
            calendar.setTime(resultSet.getTimestamp("date"));
        } else {
            calendar.setTime(new Date(0));
        }
        weather.setDate(calendar);

        weather.setId(resultSet.getLong("id"));
        weather.setTemperature(resultSet.getDouble("temperature"));
        weather.setPressure(resultSet.getDouble("pressure"));
        weather.setHumidity(resultSet.getDouble("humidity"));

        Location locale = Location.values()[resultSet.getInt("location")];
        weather.setLocation(locale);
        return weather;
    }

}
