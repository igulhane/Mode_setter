package com.example.igulhane73.appnew.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import com.example.igulhane73.appnew.R;
import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;
import com.example.igulhane73.appnew.info.ConfigTableData;
import com.example.igulhane73.appnew.utils.AddingPDS;
import com.example.igulhane73.appnew.utils.*;

import java.util.Calendar;

/**
 * Created by HimeshK on 8/19/2015.
 */
public class ChangeModeService extends Service {
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager audioManager = (AudioManager)getSystemService(getApplication().AUDIO_SERVICE);
        String mode = intent.getStringExtra("mode");
        boolean ifThisIsStopAlarm = false;
        if (intent.getAction().split(getApplicationContext().getString(R.string.startMode)).length > 1) {
            ifThisIsStopAlarm = true;
            //this has to be in startAlarm intent only because while stopping it doesn't matter where the user is
            String id = intent.getAction();
            //getting the latitude and logitude in the intent
            Double dblLongitude = intent.getDoubleExtra("longitude", -1.000);
            Double dblLatitude = intent.getDoubleExtra("latitude", -1.0000);
            //if they are greater than zero means the user has set some value
            if (dblLatitude > 0 && dblLongitude > 0) {
                //getting the location service class initialized
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //makes sense only if gps provider is enabled.
                // other wise just use the time
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                    //getting the handle to the GPS
                    Location loc = new Location(LocationManager.GPS_PROVIDER);
                    double dblLongitudeCurr = loc.getLatitude();
                    double dblLatitudeCurr = loc.getLongitude();
                    //result array for storing results
                    float results[] = new float[30];
                    loc.distanceBetween(dblLatitude, dblLongitude, dblLatitudeCurr, dblLongitudeCurr, results);
                    if (results[0] > 3000) {
                        float prevDist = results[0];
                        try {
                            //wait for 2 min and see if distance is reducing do that 3 times then change
                            for (int loop1 = 0; loop1 <= 3; loop1++) {
                                wait(60 * 1000);
                                loc.distanceBetween(dblLatitude, dblLongitude, loc.getLatitude(), loc.getLongitude(), results);
                                //if there has been a reduction in distance from the target location means we are getting there
                                if (prevDist <= results[0]) {
                                    prevDist = results[0];
                                    continue;
                                }
                                //it has been same over the past 3 min no point
                                else {
                                    if (loop1 == 3 && results[0] > 2800) {
                                        stopSelf();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.d("Wait exception", e.getMessage());
                        }
                    }
                }
            }
        }
        // this is where we create an alarm for the closing
        if ( ifThisIsStopAlarm == true){
            ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(getApplicationContext());
            //since to make alarm unique we were appending string to id extracting it
            String id = intent.getStringExtra(ConfigTableData.TimeConfigTableInfo.id);
            id = id.split(getString(R.string.startMode))[1];
            Cursor cr = cdp.retrieveNewTimeConfig(cdp.getReadableDatabase(), "( " + ConfigTableData.TimeConfigTableInfo.id + " = " + id  , null);
            if (cr.moveToFirst()) {
                Calendar cl = Calendar.getInstance();
                String time = cr.getString(cr.getColumnIndex(ConfigTableData.TimeConfigTableInfo.Etime));
                //getting the current ringer mode from the audio manager
                mode = "" + audioManager.getRingerMode();
                //get time which needs to be set in the manager
                long timeInMilli = DayAndTime.getTimeInMilliSeconds(time, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                // finally with all this information we are setting the alarmmanager to revert back to the orignal mode
                AddingPDS.addPI(getApplicationContext(), 4, getString(R.string.stopMode), intent.getStringExtra(ConfigTableData.TimeConfigTableInfo.name),Integer.parseInt(id) ,getString(R.string.StopAlarmSymbol) , mode, time, 1,Calendar.DAY_OF_WEEK);
            }
        }
        mode = intent.getStringExtra("mode");
        changemodecurrent(mode , audioManager);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public static void changemodecurrent(String mode , AudioManager audioManager){
        Log.d(" In change mode " , mode);
        if (mode.equals("All")){
            Log.d(" mode " ,   mode);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        else if (mode.equals("Priority")){
            Log.d(" mode ", mode);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
        else if (mode.equals("None")){
            Log.d(" mode ", mode);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        else {
            Log.d(" in ringer ", "" + audioManager.getRingerMode());
            audioManager.setRingerMode(Integer.parseInt(mode));
        }
    }
}
