package com.example.omarhossam.omar_al_odah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Omar Hossam on 07/09/2018.
 */

public class regist extends StringRequest {
    static final String send_data_url = "http://192.168.1.4:8080/android/omarodah_reg.php" ;
    private Map<String, String > MapData;
    public regist (String username , String password , Response.Listener<String> listener){
        super(Method.POST , send_data_url , listener , null);
        MapData = new HashMap<>();
        MapData.put("eusername" , username);
        MapData.put("epassword" , password);

    }
    @Override
    public Map  <String , String> getParams(){
        return MapData;
    }

}


