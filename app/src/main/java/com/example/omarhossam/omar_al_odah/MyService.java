package com.example.omarhossam.omar_al_odah;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyService extends Service {

    public MyService() {

    }

@Override
public void onCreate(){
    super.onCreate();

}
    @Override
    public int onStartCommand(Intent intent , int flags , int startid ){

     //   Toast.makeText(MyService.this , "StartServices" , Toast.LENGTH_LONG).show();
        run_loop();
        return START_STICKY;
    }
   public void run_loop() {



       final Globalv globalv = (Globalv) getApplicationContext();
       RequestQueue requestQueue;
       String url = "http://192.168.1.4:8080/android/omarodah_videoslist.php";
       requestQueue = Volley.newRequestQueue(this);
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {

               try {

                   JSONArray jsonArray = response.getJSONArray("allvideos");
                   JSONObject object = jsonArray.getJSONObject(0);
                   String id = object.getString("id");
                   String title = object.getString("title");
                   int lastthread = Integer.parseInt(String.valueOf(id));
           //        Toast.makeText(MyService.this, lastthread + ":" + globalv.getTotal_threades(), Toast.LENGTH_LONG).show();
Intent intent = new Intent(MyService.this , Videos_ListView.class);

                   if (globalv.getTotal_threades() < lastthread) {
                       globalv.setTotal_threades(lastthread);

                       NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                       PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0 , intent,
                               PendingIntent.FLAG_ONE_SHOT);
                       builder.setContentTitle(title);
                       builder.setContentText("فيديو جديد");
                       builder.setSmallIcon(R.drawable.halal);
                       builder.setAutoCancel(true);
                       builder.setNumber(1);
                       builder.setContentIntent(pendingIntent);
                       Notification notification = builder.build();
                       NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                       manager.cancel(1);
                       manager.notify(1, notification);
                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }

       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("VOLLEY", "ERROR");
           }
       });
       requestQueue.add(jsonObjectRequest);
       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               run_loop();
           }
       }, 29000);


   }


   @Override
   public void onDestroy(){
    //   Toast.makeText(MyService.this , "Stop Service" , Toast.LENGTH_LONG).show();
       super.onDestroy();

   }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;

    }

}
