package com.example.igulhane73.appnew.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.IBinder;

public class UserSpeedService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager lm =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            try {
                Location loc = new Location(LocationManager.GPS_PROVIDER);

            double dblLatitude = loc.getLatitude();
            double dblLongitude = loc.getLongitude();
            //result array for storing results
            float results[] = new float[30];
            double prevDistance = 0;
            int learningRateAlpha = 10;
            int counter = 0;
            while(true){
                double dblLatitudeCurr = loc.getLatitude();
                double dblLongitudeCurr = loc.getLongitude();
                loc.distanceBetween(dblLatitude, dblLongitude, dblLatitudeCurr, dblLongitudeCurr, results);
                double distance = results[0];
                if (distance - prevDistance < 1500){
                    try {
                        learningRateAlpha++;
                        wait(1000*(learningRateAlpha * counter ));
                    } catch(Exception e){}
                    counter++;
                }
                else {
                    learningRateAlpha--;
                    counter=0;
                }
                if ((distance - prevDistance) >  (35*1200/3600)*learningRateAlpha*counter) {
                    ChangeModeService.changemodecurrent("All", (AudioManager) getSystemService(getApplication().AUDIO_SERVICE));
                }
                prevDistance = distance;
                dblLatitude = dblLatitudeCurr;
                dblLongitude = dblLongitudeCurr;
            }
            }
            catch(Exception e){ e.printStackTrace();}
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
