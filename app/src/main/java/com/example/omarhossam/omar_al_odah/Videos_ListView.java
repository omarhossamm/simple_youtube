package com.example.omarhossam.omar_al_odah;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.DataSetObserver;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Videos_ListView extends AppCompatActivity {
    RequestQueue requestQueue;
    String url = "http://192.168.1.4:8080/android/omarodah_videoslist.php";
    ArrayList<Vidoeslist_info> listmovies = new ArrayList<Vidoeslist_info>();
    ListView listView;
    TextView total , username;
    Globalv globalv;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos__list_view);
        listView = (ListView) findViewById(R.id.vidoes_list);
        total = (TextView) findViewById(R.id.videos_count);
        username = (TextView) findViewById(R.id.username);
        globalv = (Globalv) getApplicationContext();
        assert username != null;
        username.setText("Name : " + globalv.getUsername());
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("allvideos");
                    total.setText("Videos Count : " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        title = object.getString("title");
                        String info = object.getString("info");
                        String link = object.getString("link");
                        String photo = object.getString("photo");
                        listmovies.add(new Vidoeslist_info(id, title, link, info, photo));


                    }
                    JSONObject object = jsonArray.getJSONObject(0);
                    String id = object.getString("id");

                    globalv.setTotal_threades(Integer.parseInt(String.valueOf(id)));
              //      Toast.makeText(Videos_ListView.this , Integer.parseInt(String.valueOf(id)) + "" , Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            listData();
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY" , "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    public void listData(){
        ListAdapter adapter = new ListAdapter(listmovies);
        listView.setAdapter(adapter);


    }
    @Override
    protected void onStop()
    {
        super.onStop();
        Intent intent = new Intent(Videos_ListView.this , MyService.class);
        startService(intent);
        finish();
    }

    class ListAdapter extends BaseAdapter{
        ArrayList<Vidoeslist_info> listtime = new ArrayList<Vidoeslist_info>();
        public ListAdapter(ArrayList<Vidoeslist_info> listtime) {
            this.listtime = listtime;
        }

        @Override
        public int getCount() {
            return listtime.size();
        }

        @Override
        public Object getItem(int position) {
            return listtime.get(position).title;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_item , null);
            TextView title = (TextView) view.findViewById(R.id.title);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            TextView info = (TextView) view.findViewById(R.id.info);

            title.setText(listtime.get(position).title);
            info.setText(listtime.get(position).info);
            Picasso.with(getApplicationContext()).load("http://192.168.1.4:8080/android/image/" + listtime.get(position).photo).into(img);


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openmovies = new Intent(getApplicationContext() , OpenMovies.class);
                    openmovies.putExtra("title" , listtime.get(position).title);
                    openmovies.putExtra("movie_id" , listtime.get(position).id);
                    openmovies.putExtra("link" , listtime.get(position).link);
                    startActivity(openmovies);

                }
            });

           return view;
        }
    }
}
