package com.example.igulhane73.appnew.info;

/**
 * Created by raghavbabu on 7/18/15.
 */
public class UserDataProvider {


    private String name;
    private String password;
    private String mailid;

    public UserDataProvider(String name,String password,String mailid){
        this.name = name;
        this.password = password;
        this.mailid = mailid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }



}
