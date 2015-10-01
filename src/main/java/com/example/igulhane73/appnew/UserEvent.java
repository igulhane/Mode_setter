package com.example.igulhane73.appnew;

import java.io.Serializable;

/**
 * Created by igulhane73 on 8/6/15.
 */
public class UserEvent implements Serializable {
    int id;
    String title;
    String start_time;
    String end_time;
    String mode;
    String endTime;
    boolean active;
    String location;
    int weekday[];
    int sun;
    int mon;
    int tue;
    int wed;
    int thur;
    int fri;
    int sat;

    UserEvent(){
        weekday=new int[7];
        title="";
        mode="";
        active=false;
        start_time="";
        end_time="";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getTue() {
        return tue;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public int getWed() {
        return wed;
    }

    public void setWed(int wed) {
        this.wed = wed;
    }

    public int getThur() {
        return thur;
    }

    public void setThur(int thur) {
        this.thur = thur;
    }

    public int getFri() {
        return fri;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int[] getWeekday() {
        return weekday;
    }

    public void setWeekday(int[] week) {
        for(int i=0;i<7;i++){
            weekday[i]=week[i];
        }
    }
}
