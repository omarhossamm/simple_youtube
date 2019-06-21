package com.example.omarhossam.omar_al_odah;

import android.app.Application;

/**
 * Created by Omar Hossam on 10/09/2018.
 */

public class Globalv extends Application {
    private String username;
    private int total_threades;


    public int getTotal_threades() {
        return total_threades;
    }

    public void setTotal_threades(int total_threades) {
        this.total_threades = total_threades;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername (String username){
        this.username = username;
    }
}
