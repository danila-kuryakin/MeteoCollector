package ru.kuryakin.meteoCollector.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import ru.kuryakin.meteoCollector.collector.TimerRunner;
import ru.kuryakin.meteoCollector.dao.TemplateWeatherDAO;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Controller
public class WeatherController {

    /***
     * Чтение настроек из файла и подключение к БД.
     * Состав файла: database.txt
     *     Строка 1 - Driver Class Name
     *     Строка 2 - Url
     *     Строка 3 - Username
     *     Строка 4 - Password
     * @return Подключение к БД. В случае ошибки null.
     */
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try(FileReader fr = new FileReader("database.txt"))
        {
            BufferedReader reader = new BufferedReader(fr);

            dataSource.setDriverClassName(reader.readLine());
            dataSource.setUrl(reader.readLine());
            dataSource.setUsername(reader.readLine());
            dataSource.setPassword(reader.readLine());
            return dataSource;
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /***
     * Создание JDBC шаблона.
     * @return Шаблон с sql запросами.
     */
    @Bean
    public TemplateWeatherDAO jdbcTemplateWeatherDao(){
        return new TemplateWeatherDAO(dataSource());
    }

    /***
     * Создание таймера задач для сборки данных с метео станций.
     * @return Таймер задач.
     */
    @Bean
    public TimerRunner timerRunner(){
        return new TimerRunner(jdbcTemplateWeatherDao());
    }
}
