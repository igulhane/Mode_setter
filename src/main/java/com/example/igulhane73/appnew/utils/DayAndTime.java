package com.example.igulhane73.appnew.utils;

import com.example.igulhane73.appnew.info.ConfigTableData;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by HimeshK on 7/28/2015.
 * Defining class for day and time operations to get number vs day mapping
 *
 */
public class DayAndTime {
    static HashMap<Integer , String> daysToName = new HashMap<Integer , String>();
    private static boolean hasBeenLoaded;
    private static int numOfDaysInAWeek;
    public static void addDaysToHashMap (){
        numOfDaysInAWeek = 7;
        daysToName.put(Calendar.SUNDAY , ConfigTableData.TimeConfigTableInfo.SunDay);
        daysToName.put(Calendar.MONDAY, ConfigTableData.TimeConfigTableInfo.MonDay);
        daysToName.put(Calendar.TUESDAY , ConfigTableData.TimeConfigTableInfo.TuesDay);
        daysToName.put(Calendar.WEDNESDAY , ConfigTableData.TimeConfigTableInfo.WednesDay);
        daysToName.put(Calendar.THURSDAY , ConfigTableData.TimeConfigTableInfo.ThursDay);
        daysToName.put(Calendar.FRIDAY , ConfigTableData.TimeConfigTableInfo.FriDay);
        daysToName.put(Calendar.SATURDAY , ConfigTableData.TimeConfigTableInfo.SaturDay);
    }
    public static String getCurrentDayName(int currentDay , int offset ){
        if (hasBeenLoaded == false){
            addDaysToHashMap();
        }
        //checking current day
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DAY_OF_WEEK , offset);
        return daysToName.get(cl.get(Calendar.DAY_OF_WEEK));
    }
    public static int getNumOfDaysInAWeek(){
        return numOfDaysInAWeek;
    }
//converts time to milliseconds
    public static long getTimeInMilliSeconds(String time , int dayOfTheWeek){
        Calendar cl = Calendar.getInstance();
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1].split(" ")[0]);
        String AMPM = (time.split(":")[1].split(" ")[1]).trim();
        cl.set(Calendar.HOUR  , hour);
        cl.set(Calendar.MINUTE  , minute);
        cl.set(Calendar.SECOND , 50);
        cl.set(Calendar.AM_PM, AMPM.equals("AM") ? Calendar.AM : Calendar.PM);
        cl.set(Calendar.DAY_OF_WEEK , dayOfTheWeek);
        return cl.getTimeInMillis();
    }
    public static boolean checkIfDaySelected(String entry){
        return entry.equals("1");
    }
}
