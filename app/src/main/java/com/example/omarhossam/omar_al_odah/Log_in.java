package com.example.omarhossam.omar_al_odah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Omar Hossam on 07/09/2018.
 */

public class Log_in extends StringRequest {
    private static final String url = "http://192.168.1.4:8080/android/omarodah_login.php";
    private Map<String , String> mapdata;

    public Log_in(String username , String password , Response.Listener<String> listener ) {
        super(Method.POST , url , listener , null);
        mapdata = new HashMap<>();
        mapdata.put("login_name" , username);
        mapdata.put("login_password" , password);

    }
    @Override
    public Map <String , String> getParams(){
        return mapdata;
    }
}

