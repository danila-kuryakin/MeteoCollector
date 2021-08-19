package ru.kuryakin.meteoCollector.collector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.kuryakin.meteoCollector.dao.TemplateWeatherDAO;
import ru.kuryakin.meteoCollector.models.Location;
import ru.kuryakin.meteoCollector.models.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/***
 * Сбор данных с метеостанций в определенное время.
 */
public class Collector extends TimerTask {

    /***
     * Список url.
     */
    private List<String> urls;
    /***
     * Формат вывода даты.
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[dd.MM.yy HH:mm:ss] ");
    /***
     * Шаблон с sql запросами.
     */
    private TemplateWeatherDAO weatherDAO;

    /***
     * Конструктор.
     * @param weatherDAO - шаблон с sql запросами.
     */
    public Collector(TemplateWeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
        this.urls = readFile();
    }

    /***
     * Запуск сборщика данных с метеостанций.
     */
    @Override
    public void run() {
        System.out.println(this.dateFormat.format(new Date()) + "Start collector");
        for (String url:this.urls) {
            if (!getWeatherFromController(url))
                System.out.println(String.format(this.dateFormat.format(new Date()) + "Collector don't get weather data at the ip(%s)", url));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Чтение из файла адресов метео станций.
     * @return Список строк содержащих адреса метеостанций.
     */
    private List<String> readFile(){
        List<String> urls = new ArrayList<>();
        try(FileReader fr = new FileReader("stations.txt"))
        {
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            while (line != null) {
                urls.add(line);
                line = reader.readLine();
            }
            return urls;
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /***
     * Чтение данных метеостанции и запись в БД.
     * @param url - адрес метео станции
     * @return Прочитаны данные (true) или нет (false).
     */
    public boolean getWeatherFromController(String url){
        JSONParser parser = new JSONParser();
        boolean sensorStatus = false;
        Location location;
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
            Elements listNews = doc.select("body");
            Weather weather = new Weather();
            JSONObject jsonWeather = (JSONObject) parser.parse(listNews.text());
            Object tempObject = jsonWeather.get("Temperature");
            if (tempObject instanceof Double)
                weather.setTemperature((Double) tempObject);
            else {
                System.out.println(dateFormat.format(new Date()) + "Fail: Temperature not recognized");
            }

            Object humidObject = jsonWeather.get("Humidity");
            if (humidObject instanceof Double) {
                weather.setHumidity((Double) humidObject);
                sensorStatus = true;
            }
            Object pressureObject = jsonWeather.get("Pressure");
            if (pressureObject instanceof Double) {
                weather.setPressure((Double) pressureObject);
                sensorStatus = true;
            }
            if (!sensorStatus){
                System.out.println(dateFormat.format(new Date()) + "Fail: Pressure or Humidity not recognized");
            }

            Object locationObject = jsonWeather.get("Location");
            location = Location.valueOf(String.valueOf(locationObject));
            boolean locationExists = false;
            for (Location value:Location.values()) {
                if (location.equals(value)) {
                    weather.setLocation(location);
                    locationExists = true;
                }
            }
            if (!locationExists){
                System.out.println(dateFormat.format(new Date()) + "Fail: Location does not exists");
            }
            this.weatherDAO.createWeather(weather);
            return true;
        }catch (ConnectException e){
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

