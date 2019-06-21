package com.example.omarhossam.omar_al_odah;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Downloader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Omar Hossam on 25/01/2019.
 */

public class send_data_comment extends StringRequest {
    private static final String send_data_url = "http://192.168.1.4:8080/android/add_comment.php";
    private Map<String , String> MapData;

    public send_data_comment(String movie_id, String name, String comment_text , Response.Listener <String> listener ) {
        super(Method.POST, send_data_url, listener , null);
        MapData = new HashMap<>();
        MapData.put("movie_id" , movie_id);
        MapData.put("ename" , name);
        MapData.put("comment_text" , comment_text);
    }
    @Override
    public Map <String , String> getParams(){
        return MapData;
    }
}
