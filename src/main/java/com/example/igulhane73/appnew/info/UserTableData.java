package com.example.igulhane73.appnew.info;

import android.provider.BaseColumns;

/**
 * Created by raghavbabu on 7/18/15.
 */
public class UserTableData {

    public UserTableData(){

    }

    public static abstract  class TableInfo implements BaseColumns{

        public static final String USER_NAME = "user_name";
        public static final String USER_PASS = "user_pass";
        public static final String USER_MAILID = "user_mailid";
        public static final String USER_INFODB = "user_infodb";
        public static final String USER_TABLE = "user_infotable";

    }
}
