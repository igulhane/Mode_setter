package com.example.igulhane73.appnew.Services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.example.igulhane73.appnew.R;
import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;
import com.example.igulhane73.appnew.dbOps.DBQuery;
import com.example.igulhane73.appnew.utils.AddingPDS;
import com.example.igulhane73.appnew.utils.DayAndTime;

import java.util.Calendar;

/**
 * Created by HimeshK on 8/19/2015.
 */
public class EveryDayService extends Service {
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        // creating configDatabase
        ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(getApplicationContext());
        Calendar cl = Calendar.getInstance();
        int currentDay = cl.get(Calendar.DAY_OF_WEEK);
        Log.d("srtComand EyDayService" ,"EveryDayService" );
        String daysToCheck[] = new String[2];
        int dayOfTheWeek[] =  new int[2];
        //checking current day and next day and setting those alarms
        daysToCheck[0] = DayAndTime.getCurrentDayName(currentDay, 0);
        daysToCheck[1] = DayAndTime.getCurrentDayName(currentDay, 1);
        dayOfTheWeek[0] =  cl.get(Calendar.DAY_OF_WEEK);
        //creasting temprory calendar since we are modifying values and then deleting it
        Calendar clTemp = Calendar.getInstance();
        //modification of the clTemp
        clTemp.add(Calendar.DAY_OF_WEEK , 1);
        dayOfTheWeek[1] = clTemp.get(Calendar.DAY_OF_WEEK);
        clTemp = null;
        //getting alarms from a particular day
        Cursor cr = DBQuery.getActiveAlaramsOnDay(getApplicationContext() ,daysToCheck , cdp);
        AddingPDS.addAlarmInSchedular(cr , getApplicationContext() , daysToCheck  , dayOfTheWeek , cdp , getString(R.string.StartAlarmSymbol), getString(R.string.startMode));
        //while (true){}
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
