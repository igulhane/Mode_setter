package com.example.igulhane73.appnew.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by HimeshK on 8/22/2015.
 */
public class startServiceOnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // assumes WordService is a registered service
        intent = new Intent(context, EveryDayService.class);
        context.startService(intent);
    }
}
