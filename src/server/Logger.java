package server;

import java.util.Date;

public class Logger {

    static void log(String string, String className){

        System.out.println((new Date().getTime()) + Utilites.getStartText(className) + string);

    }


    public static void logError(String string, String className) {

        System.out.println("[ОШИБКА] - " + (new Date().getTime()) + Utilites.getStartText(className) + string);

    }
}
