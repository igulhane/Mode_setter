package com.example.igulhane73.appnew.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by raghavbabu on 7/24/15.
 */
public class DBHelper {

    private static DBHelper dbHelperInstance = null;
    private DBHelper(){

    }

    public static DBHelper getDbHelperInstance(){

        if (dbHelperInstance == null) {

            synchronized (DBHelper.class) {
                if (dbHelperInstance == null) {
                    dbHelperInstance = new DBHelper();
                }
            }
        }
        return dbHelperInstance;

    }

    public void addRow(SQLiteDatabase sqlitedb,String tableName, ContentValues cv) {

        long k = sqlitedb.insert(tableName,null,cv);
        Log.d("DBHelper :", "One User data row inserted in "+tableName);
    }


    public Cursor getRowValues(SQLiteDatabase sqlitedb, String tableName, String[] projections , String whereClause,String[] remainder ){

        return sqlitedb.query(tableName,projections,whereClause,null,null,null,null);
    }

    public void deleteUserData(SQLiteDatabase sqlitedb , String table, String where , String[] whereArgs){
        sqlitedb.delete(table, where, whereArgs);
    }
  //udating user data to read more : http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#update(java.lang.String, android.content.ContentValues, java.lang.String, java.lang.String[])
    // sqlite databse object , table name cv - basically like a hashmap which store key value pair in this case
    // key  is the column name and value the value we want to add , where is where clause , whereArgs for now null
    public void updateUserData(SQLiteDatabase sqlitedb , String table , ContentValues cv , String where , String[] whereArgs){
        long k = sqlitedb.update(table, cv , where , whereArgs);
        Log.d("update code " , " " + k);
    }

    public void dropTable(SQLiteDatabase sqlitedb,String tableName){

        sqlitedb.execSQL("DROP TABLE IF EXISTS " +tableName);
        Log.d("DBHelper : ","Table "+ tableName +" Dropped");
    }

}
