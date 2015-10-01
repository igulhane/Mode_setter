package com.example.igulhane73.appnew.info;

import android.provider.BaseColumns;

/**
 * Created by raghavbabu on 7/24/15.
 */
public class ConfigTableData {


    public ConfigTableData(){

    }

    public static abstract  class TimeConfigTableInfo implements BaseColumns {

        public static final String id = "id";
        public static final String time = "time";
        public static final String Etime = "Etime";
        public static final String mode = "mode";
        public static final String USER_INFODB = "user_infodb";
        public static final String Config_Table = "mode_configTable";
        public static final String SunDay = "Sunday";
        public static final String MonDay = "Monday";
        public static final String TuesDay = "Tuesday";
        public static final String WednesDay = "Wednesday";
        public static final String ThursDay = "Thursday";
        public static final String FriDay = "Friday";
        public static final String SaturDay = "Saturday";
        public static final String name = "Name";
        public static final String active = "active";
    }
}
