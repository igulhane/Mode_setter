package com.example.igulhane73.appnew.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.util.Log;

import com.example.igulhane73.appnew.R;
import com.example.igulhane73.appnew.Services.ChangeModeService;
import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;
import com.example.igulhane73.appnew.dbOps.DBQuery;
import com.example.igulhane73.appnew.info.ConfigTableData;

import java.util.Calendar;

/**
 * Created by HimeshK on 8/21/2015.
 * has all the code relating to the the pending intent and other
 * thing that require setting schedular
 */
public class AddingPDS {
    public static void addPI(Context context ,int requestId , String type_of_alarm , String name , int id , String startSymbol , String mode , String time , int day , int day_of_the_week){
        long timeMilli = DayAndTime.getTimeInMilliSeconds(time, day_of_the_week);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pd = get_PendingIntent(context, name, requestId, id, type_of_alarm, startSymbol, mode, day, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(am.RTC_WAKEUP, timeMilli, pd);
    }

    public static PendingIntent get_PendingIntent(Context context , String name , int requestCode , int id , String startSymbol , String type_of_alarm , String mode , int day , int type_of_alarm1){
        //getting alarm manager for the pending intent
        Intent temp = new Intent(context, ChangeModeService.class);
        temp.putExtra(ConfigTableData.TimeConfigTableInfo.name, name);
        String setActionStart = startSymbol +  id;
        temp.setType(type_of_alarm + id);
        temp.putExtra(ConfigTableData.TimeConfigTableInfo.mode, mode);
        temp.setAction(day + "" + setActionStart);
        //Log.d("Start Time Day 1", "" + cl.getTimeInMillis());
        return (PendingIntent.getService(context , requestCode ,temp , type_of_alarm1));
    }
    //update alarm by using the id and the context in whihc it was created
    public static void updateAlarmById(int id , Context context) {
        // using data base to get details of the particular alarm which was modified to get
        ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(context);
        Calendar cl = Calendar.getInstance();
        int currentDay = cl.get(Calendar.DAY_OF_WEEK);
        Log.d("SrtComand EyDayService", "EveryDayService");
        String daysToCheck[] = new String[2];
        int dayOfTheWeek[] = new int[2];
        //checking current day and accordingly setting it
        daysToCheck[0] = DayAndTime.getCurrentDayName(currentDay, 0);
        daysToCheck[1] = DayAndTime.getCurrentDayName(currentDay, 1);
        dayOfTheWeek[0] =  cl.get(Calendar.DAY_OF_WEEK);
        //creasting temprory calendar since we are modifying values and then deleting it
        Calendar clTemp = Calendar.getInstance();
        //modification of the clTemp
        clTemp.add(Calendar.DAY_OF_WEEK , 1);
        dayOfTheWeek[1] = clTemp.get(Calendar.DAY_OF_WEEK);
        clTemp = null;
        Cursor cr = DBQuery.getCursorById(context, daysToCheck, id, cdp);
        addAlarmInSchedular(cr ,context , daysToCheck  , dayOfTheWeek , cdp , context.getString(R.string.StartAlarmSymbol) , context.getString(R.string.startMode));
    }
    public static void addAlarmInSchedular(Cursor cr , Context context , String [] daysToCheck , int[] dayOfTheWeek , ConfigDatabaseOperations cdp , String alarmSymbol , String startMode){

        try {
            if (cr.moveToFirst()) {
                do {
                    // setting alarm for all alarms for the day and tomo ;
                    AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
                    //getting the time mode and name
                    String time = cr.getString(cr.getColumnIndex(ConfigTableData.TimeConfigTableInfo.time)) ;
                    String mode = cr.getString(cr.getColumnIndex(ConfigTableData.TimeConfigTableInfo.mode));
                    String name = cr.getString(cr.getColumnIndex(ConfigTableData.TimeConfigTableInfo.name));
                    int id = cr.getColumnIndex(ConfigTableData.TimeConfigTableInfo.id);
                    long timeInMilli = DayAndTime.getTimeInMilliSeconds(time , dayOfTheWeek[0]);
                    //Log.d(" Lets see" , " " + (Calendar.getInstance().getTimeInMillis() - cl.getTimeInMillis()));
                    if (cr.getString(cr.getColumnIndex(daysToCheck[0])).equals("1") && ((Calendar.getInstance()).getTimeInMillis() <= timeInMilli)) {
                        AddingPDS.addPI(context, 2, startMode , name, id,alarmSymbol , mode, time, 1 ,dayOfTheWeek[0] );
                    }
                    if (cr.getString(cr.getColumnIndex(daysToCheck[1])).equals("1")) {
                        AddingPDS.addPI(context, 3, startMode, name, id,  alarmSymbol, mode, time, 2 , dayOfTheWeek[1]);
                    }
                }while(cr.moveToNext());
            }
        }
        catch(Exception e){
            Log.d(" Adding pending intent", e.getMessage());
        }
        finally {

            cdp.close();
        }
    }
}
