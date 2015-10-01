package com.example.igulhane73.appnew.info;

/**
 * Created by raghavbabu on 7/24/15.
 */
public class ConfigDataProvider {

    private String time;
    private String mode;

    public ConfigDataProvider(String time, String mode){
        this.time = time;
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
