package ru.kuryakin.meteoCollector;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.kuryakin.meteoCollector.collector.TimerRunner;
import ru.kuryakin.meteoCollector.controller.WeatherController;

import java.util.Scanner;

/***
 * Запуск приложения.
 */
public class ApplicationMeteoCollector {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(WeatherController.class);

        TimerRunner timerRunner = context.getBean(TimerRunner.class);
        timerRunner.run();
        while (true){
            Scanner in = new Scanner(System.in);
            String str = in.next();
            if (str.equals("stop")){
                return;
            }
        }
    }
}
