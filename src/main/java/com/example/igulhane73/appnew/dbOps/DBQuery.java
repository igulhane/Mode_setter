package com.example.igulhane73.appnew.dbOps;

import android.content.Context;
import android.database.Cursor;

import com.example.igulhane73.appnew.info.ConfigTableData;

/**
 * Created by HimeshK on 8/16/2015.
 */
public class DBQuery {
    public static Cursor getActiveAlaramsOnDay(Context context ,String daysToCheck[] , ConfigDatabaseOperations cdp){
        String str= " ";
        for (int loop1 = 0  ; loop1 < daysToCheck.length ; loop1++){
            str = str + daysToCheck[loop1] + " = '" + context.getResources().getIdentifier("DayActiveInDB" , "string" , context.getPackageName())    +  "'";
            if (loop1 == daysToCheck.length - 1){
                continue;
            }
            str = str + " OR ";
        }
       Cursor cr =  cdp.retrieveNewTimeConfig(cdp.getReadableDatabase(), "( " + str + " ) " +
                " AND " + ConfigTableData.TimeConfigTableInfo.active, null);
        return cr;
    }
    public static Cursor getCursorById(Context context , String daysToCheck[] , int id , ConfigDatabaseOperations cdp){
        String str= " ";
        if (daysToCheck != null) {
            for (int loop1 = 0; loop1 < daysToCheck.length; loop1++) {
                str = str + daysToCheck[loop1] + " = '" + context.getResources().getIdentifier("DayActiveInDB" , "string" , context.getPackageName())    +  "'";
                if (loop1 == daysToCheck.length - 1) {
                    continue;
                }
                str = str + " OR ";
            }
        }
        Cursor cr =  cdp.retrieveNewTimeConfig(cdp.getReadableDatabase(), "( " + str + " ) " +
                " AND " +  ConfigTableData.TimeConfigTableInfo.id + " = " + id, null);
        return cr;

    }
}
