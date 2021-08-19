package ru.kuryakin.meteoCollector.collector;

import ru.kuryakin.meteoCollector.dao.TemplateWeatherDAO;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

/***
 * Запуск таймера для сборки данных с погодных станций.
 */
public class TimerRunner{
    /***
     * Разница в минутах между чтение м данных
     */
    public static final int MINUTES_DELAY = 5;
    /***
     * Шаблон с sql запросами.
     */
    private TemplateWeatherDAO weatherDAO;

    /***
     * Конструктор.
     * @param weatherDAO - шаблон с sql запросами.
     */
    public TimerRunner(TemplateWeatherDAO weatherDAO){
        this.weatherDAO = weatherDAO;
    }

    /***
     * Запуск таймера задач для чтения данных с метео стаций.
     */
    public void run() {
        TimerTask timerTask = new Collector(weatherDAO);
        Timer timer = new Timer("CollectorWeatherTimer", true);
        if (MINUTES_DELAY > 0) {
            long delay = synchronizer(MINUTES_DELAY);
            timer.scheduleAtFixedRate(timerTask, delay, MINUTES_DELAY * 60 * 1000);

        }
    }

    /***
     * Синхронизация по времени. Расчет временной задержки перед первым включением.
     * Поиск ближайшего по времени числа кратного числу в переменной period.
     * @param period - Кратное число. Например, если period = 5, то подходят числа 5, 10,15 и т.д.
     * @return Задержка в миллисекундах.
     */
    private long synchronizer(int period){
        long minute, second, millisecond, deltaMin, deltaSec, delayMil, delta;
        GregorianCalendar calendar = new GregorianCalendar();

        minute = calendar.get(GregorianCalendar.MINUTE);
        second = calendar.get(GregorianCalendar.SECOND);
        millisecond = calendar.get(GregorianCalendar.MILLISECOND);

        deltaMin = minute / period * period + period - minute - 1;
        deltaSec = 59 - second;
        delayMil = 1000 - millisecond;

        delta = (deltaMin * 60*1000) + (deltaSec * 1000) + delayMil;

        System.out.println(String.format("Launch collector in %2d:%2d.%2d or %d ms", deltaMin, deltaSec, delayMil, delta));

        return delta;
    }
}


