package com.example.omarhossam.omar_al_odah;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Downloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OpenMovies extends AppCompatActivity {

    ArrayList<list_item_comments> listcomments = new ArrayList<list_item_comments>();
    VideoView video;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    TextView title , like , comment ;
    String movie_id;
    String username;
    String link;
    ImageButton add_like , add_comment;
    EditText Ecomment;
    Globalv globalv;
    ListView comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_movies);

        video = (VideoView) findViewById(R.id.video);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        title = (TextView) findViewById(R.id.title);
        like = (TextView) findViewById(R.id.total_likes);
        comment = (TextView) findViewById(R.id.total_comments);
        add_like = (ImageButton) findViewById(R.id.like);
        add_comment = (ImageButton) findViewById(R.id.comment);
        Ecomment = (EditText) findViewById(R.id.editText);
        comments = (ListView) findViewById(R.id.comments);
        globalv = (Globalv) getApplicationContext();
        username = globalv.getUsername();
        assert progressBar != null;
        progressBar.bringToFront();
        ///////////////////////////////////////////////////
        Intent data = getIntent();
        link = data.getExtras().getString("link");
        Uri uri = Uri.parse(link);
        movie_id = data.getExtras().getString("movie_id");
        title.setText(data.getExtras().getString("title").trim());
        /////////////////////////////////////////////////////
        MediaController mediaController = new MediaController(this);
        video.setVideoURI(uri);
        video.setMediaController(new MediaController(this));
        mediaController.setAnchorView(video);
        video.requestFocus();
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        getcomments();
        getlikes();
        listData();


    }



    @Override
    protected void onStop()
    {
        super.onStop();

        Intent intent = new Intent(OpenMovies.this , MyService.class);
        startService(intent);

        finish();
    }



public void getcomments (){
        String url = "http://192.168.1.4:8080/android/show_comments.php?movie_id=" + movie_id;

        requestQueue = Volley.newRequestQueue(this);
        listcomments.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("comments");
                    comment.setText(String.valueOf(jsonArray.length()));
                    for (int i = 0 ; i<jsonArray.length() ; i++ ){
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("movie_id");
                        String name = object.getString("name");
                        String text = object.getString("text");
                        String data_time = object.getString("date_time");

                        listcomments.add(new list_item_comments(id,name,text,data_time));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                listData();
            }
        } , new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error){
                Log.e("VOLLEY" , "ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

public void getlikes(){
    String url = "http://192.168.1.4:8080/android/show_likes.php?movie_id=" + movie_id;

    requestQueue = Volley.newRequestQueue(this);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            StringBuilder text = new StringBuilder();
            try {
                JSONArray jsonArray = response.getJSONArray("likes");
                like.setText(String.valueOf(jsonArray.length()));
                for (int i = 0 ; i<jsonArray.length() ; i++ ){
                    JSONObject respnos = jsonArray.getJSONObject(i);
                    text.append(respnos.getString("name"));


                }

            }catch (JSONException e){
                e.printStackTrace();
            }
            if (text.toString().contains(username)){
                add_like.setEnabled(false);
            }
        }
    } , new Response.ErrorListener(){
        @Override
        public void onErrorResponse (VolleyError error){
            Log.e("VOLLEY" , "ERROR");
        }
    });
    requestQueue.add(jsonObjectRequest);

}


public void listData() {
    ListAdapter adapter = new ListAdapter(listcomments);
    comments.setAdapter(adapter);

}

public void add_like(View view) {


    RequestQueue queue = Volley.newRequestQueue(OpenMovies.this);
        String url = "http://192.168.1.4:8080/android/add_like.php?movie_id=" + movie_id + "&ename=" + username;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(OpenMovies.this , "Like" , Toast.LENGTH_LONG).show();
                        add_like.setEnabled(false);
                        getlikes();
                    }else{
                        Toast.makeText(OpenMovies.this , "Failed" , Toast.LENGTH_LONG).show();
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }
                listData();
            }
        } , new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error){
                Log.e("VOLLEY" , "ERROR");
            }
        });


queue.add(request);

        }


public void btn_add_comment(View view) {

    String comment_text = Ecomment.getText().toString().trim();

    if (comment_text.equals("")){
        Toast.makeText(OpenMovies.this , "You Must Write The Comment" , Toast.LENGTH_LONG).show();
    }else {
        Response.Listener<String> responselistner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(OpenMovies.this , "Comment Sent" , Toast.LENGTH_LONG).show();
                        Ecomment.setText("");
                        getcomments();
                    }else {
                        Toast.makeText(OpenMovies.this , "Failed To Send Comment" , Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){e.printStackTrace();}
            }
        };
    send_data_comment send_data_comment = new send_data_comment(movie_id , username , comment_text , responselistner);
        RequestQueue queue = Volley.newRequestQueue(OpenMovies.this);
        queue.add(send_data_comment);

    }


    }



  /*  class ListAdapter extends BaseAdapter{

        ArrayList<list_item_comments> item = new ArrayList<list_item_comments>();
        ListAdapter (ArrayList<list_item_comments>item){
            this.item = item ;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position).name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.info_list , null);
            TextView datec = (TextView) view.findViewById(R.id.date_comment);
            TextView user_namec = (TextView) view.findViewById(R.id.username);
            TextView comment_txtc = (TextView) view.findViewById(R.id.comment);

            datec.setText(item.get(position).data_time);
            user_namec.setText(item.get(position).name);
            comment_txtc.setText(item.get(position).text);

            return view;
        }
    }

*/






    class ListAdapter extends BaseAdapter{
        ArrayList<list_item_comments> item = new ArrayList<list_item_comments>();
        public ListAdapter (ArrayList<list_item_comments>item){
            this.item = item ;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position).name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.info_list , null);
            TextView name = (TextView) view.findViewById(R.id.username);
            TextView desc = (TextView) view.findViewById(R.id.date_comment);
            TextView imageView = (TextView) view.findViewById(R.id.comment);

            name.setText(item.get(position).name);
            desc.setText(item.get(position).data_time);
            imageView.setText(item.get(position).text);
            return view;
        }
    }






}


