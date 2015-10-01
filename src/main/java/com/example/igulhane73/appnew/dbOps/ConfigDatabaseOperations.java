package com.example.igulhane73.appnew.dbOps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.igulhane73.appnew.info.*;
import com.example.igulhane73.appnew.utils.*;

/**
 * Created by raghavbabu on 7/24/15.
 */
public class ConfigDatabaseOperations extends SQLiteOpenHelper {


    public static int db_version = 2;
    public String createConfigTableQuery = "CREATE TABLE IF NOT EXISTS "+ ConfigTableData.TimeConfigTableInfo.Config_Table+" ("
            + ConfigTableData.TimeConfigTableInfo.id +" INTEGER," +   ConfigTableData.TimeConfigTableInfo.time+" TEXT,"
            + ConfigTableData.TimeConfigTableInfo.Etime + " TEXT ,"
            + ConfigTableData.TimeConfigTableInfo.mode+" TEXT, "  +   ConfigTableData.TimeConfigTableInfo.SunDay+" TEXT,"
            + ConfigTableData.TimeConfigTableInfo.MonDay+" TEXT," +   ConfigTableData.TimeConfigTableInfo.TuesDay+" TEXT,"
            + ConfigTableData.TimeConfigTableInfo.WednesDay+" TEXT,"+ ConfigTableData.TimeConfigTableInfo.ThursDay+" TEXT,"
            + ConfigTableData.TimeConfigTableInfo.FriDay+" TEXT,"   + ConfigTableData.TimeConfigTableInfo.SaturDay+" TEXT,"
            + ConfigTableData.TimeConfigTableInfo.name+" TEXT," + ConfigTableData.TimeConfigTableInfo.active+" BOOLEAN  );" ;

    private static DBHelper dbHelper = DBHelper.getDbHelperInstance();


    public ConfigDatabaseOperations(Context context){
        super(context, ConfigTableData.TimeConfigTableInfo.USER_INFODB, null, db_version);
        Log.d("TimeConfigDatabaseOp ", "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabasedb) {

    }
    public void dropDB(ConfigDatabaseOperations cdo){
        cdo.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + ConfigTableData.TimeConfigTableInfo.USER_INFODB);
    }
    public void createTables(ConfigDatabaseOperations cdo){
        try {
            cdo.getWritableDatabase().execSQL(createConfigTableQuery);
            Log.d(" String " , createConfigTableQuery);
        }
        catch (Exception e) {
            Log.d(" Error " , e.getMessage());
        }
        finally {
            cdo.close();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabasedb, int oldVersion, int newVersion) {

        //sqLiteDatabasedb.execSQL("DROP TABLE IF EXISTS " + ConfigTableData.TimeConfigTableInfo.Config_Table);
        sqLiteDatabasedb.execSQL(createConfigTableQuery);
        Log.d("TimeConfigDatabaseOp ","Table Created");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewTimeConfig(ConfigDatabaseOperations tcdop,int id , String time ,String eTime ,String mode ,
                                 String sunday ,String monday , String tuesday , String wednesday ,
                                 String thursday , String friday , String saturday , String name, boolean active){
        try {
        SQLiteDatabase sqlitedb = tcdop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ConfigTableData.TimeConfigTableInfo.Etime , eTime);
        cv.put(ConfigTableData.TimeConfigTableInfo.id, id);
        cv.put(ConfigTableData.TimeConfigTableInfo.time,time);
        cv.put(ConfigTableData.TimeConfigTableInfo.mode, mode);
        cv.put(ConfigTableData.TimeConfigTableInfo.SunDay , sunday);
        cv.put(ConfigTableData.TimeConfigTableInfo.MonDay, monday);
        cv.put(ConfigTableData.TimeConfigTableInfo.TuesDay, tuesday);
        cv.put(ConfigTableData.TimeConfigTableInfo.WednesDay , wednesday);
        cv.put(ConfigTableData.TimeConfigTableInfo.ThursDay, thursday);
        cv.put(ConfigTableData.TimeConfigTableInfo.FriDay , friday);
        cv.put(ConfigTableData.TimeConfigTableInfo.SaturDay , saturday);
        cv.put(ConfigTableData.TimeConfigTableInfo.name , name  );
        cv.put(ConfigTableData.TimeConfigTableInfo.active , active) ;
        dbHelper.addRow(sqlitedb, ConfigTableData.TimeConfigTableInfo.Config_Table, cv);
        }
        catch(Exception e){}
        finally {
            tcdop.close();
        }

    }

    public Cursor retrieveNewTimeConfig(SQLiteDatabase sqlitedb , String where , String [] remainder){

        String[] projections = {
        ConfigTableData.TimeConfigTableInfo.id ,
        ConfigTableData.TimeConfigTableInfo.time,
        ConfigTableData.TimeConfigTableInfo.Etime,
        ConfigTableData.TimeConfigTableInfo.mode  ,
        ConfigTableData.TimeConfigTableInfo.SunDay ,
        ConfigTableData.TimeConfigTableInfo.MonDay,
        ConfigTableData.TimeConfigTableInfo.TuesDay,
        ConfigTableData.TimeConfigTableInfo.WednesDay,
        ConfigTableData.TimeConfigTableInfo.ThursDay,
        ConfigTableData.TimeConfigTableInfo.FriDay ,
        ConfigTableData.TimeConfigTableInfo.SaturDay,
        ConfigTableData.TimeConfigTableInfo.name,
        ConfigTableData.TimeConfigTableInfo.active
        };
        Cursor cursor = dbHelper.getRowValues(sqlitedb, ConfigTableData.TimeConfigTableInfo.Config_Table, projections, where, remainder);
        return cursor;
    }
    public int getHighestId(SQLiteDatabase sqlitedb ) {
        Integer id = null;
        try {
            //final String MY_QUERY = "SELECT MAX(id) As id FROM " + ConfigTableData.TimeConfigTableInfo.Config_Table;
            final String MY_QUERY = " SELECT * FROM " + ConfigTableData.TimeConfigTableInfo.Config_Table + " ORDER BY id DESC LIMIT 1";
            Cursor mCursor = sqlitedb.rawQuery(MY_QUERY, null);
            if (mCursor.moveToFirst()) {
                id = mCursor.getInt(0);
                System.out.println(mCursor.getInt(0));
                System.out.println(mCursor.getInt(1));
                return (id);
            }
            else
                return (0);
        }
        catch (Exception e){
            Log.d(" Error " , e.getMessage());
        }
        finally {
            sqlitedb.close();
        }
        if (id == null)
            return 0;
        else
            return id;
    }
    public void deleteUserData(SQLiteDatabase sqlitedb  , String where , String whereArgs[]){
        System.out.println(" " + where);
        dbHelper.deleteUserData(sqlitedb, ConfigTableData.TimeConfigTableInfo.Config_Table, where, whereArgs);

    }

    public void updateUserData(ContentValues cv , String where , String[] whereArgs, String[] misc){
        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        dbHelper.updateUserData( sqlitedb ,  ConfigTableData.TimeConfigTableInfo.Config_Table,  cv , where ,  whereArgs);

        sqlitedb.close();
    }

}
