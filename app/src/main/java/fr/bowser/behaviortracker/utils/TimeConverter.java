package fr.bowser.behaviortracker.utils;

public class TimeConverter {

    public static String convertSecondsToHumanTime(long time){

        String string = "";

        long hours = time / 3600;
        if(hours < 10){
            string = "0";
        }
        string = string + hours + ":";

        time %= 3600;

        long minutes = time / 60;
        if(minutes < 10){
            string += "0";
        }
        string = string + minutes + ":";

        time %= 60;

        long seconds = time;
        if(seconds < 10){
            string += "0";
        }
        string += seconds;

        return string;
    }
}
