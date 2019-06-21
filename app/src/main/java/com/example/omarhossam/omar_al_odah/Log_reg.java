package com.example.omarhossam.omar_al_odah;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Log_reg extends AppCompatActivity {
    EditText username , password;
RelativeLayout relativeLayout;
    Globalv globalv;
    CheckBox checkBox;
    DB_sqlite db;
    String check , save_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);
        db = new DB_sqlite(this);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        globalv = (Globalv) getApplicationContext();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
relativeLayout = (RelativeLayout) findViewById(R.id.rel);




        // save data login
        if (db.get_check() != null){
            check = db.get_check();
            if (check.equals("1")){
                username.setText(db.get_username());
                password.setText(db.get_password());
                checkBox.setChecked(true);
                save_data = "1";

            }
            else {
                checkBox.setChecked(false);
                save_data="0";
            }

        }



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   save_data = "1";
               }else {
                   save_data = "0";
               }
            }
        });

        Intent intent = new Intent(Log_reg.this , MyService.class);
        stopService(intent);

    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(Log_reg.this , "back" , Toast.LENGTH_LONG).show();
         }
        else if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            Toast.makeText(Log_reg.this , "home" , Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        Intent intent = new Intent(Log_reg.this , MyService.class);
        startService(intent);
        finish();
    }




     @Override
    public void onBackPressed(){
        Intent intent = new Intent(Log_reg.this , MyService.class);
        startService(intent);
        finish();
        super.onBackPressed();
    }






*/




 /*

    @Override
    public void onUserLeaveHint(){


        Intent intent = new Intent(Log_reg.this , MyService.class);
        startService(intent);
        finish();
        super.onBackPressed();

    }
*/

    public void regstration(View view) {
        // move to registration activity
            startActivity(new Intent(getApplicationContext() , Registration.class));
        username.setText("");
        password.setText("");
        relativeLayout.setVisibility(View.INVISIBLE);
    }

    public void log_in(View view) {
        final String user = username.getText().toString();
        String pass = password.getText().toString();



        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (username.length() == 0 || password.length() == 0){
                        Toast.makeText(getApplicationContext() , "Enter Username Or Password" , Toast.LENGTH_LONG).show();
                    }


                    else if (success){
                        Intent intentt = new Intent(Log_reg.this , MyService.class);
                        startService(intentt);
                        Toast.makeText(getApplicationContext() , "Login Successfull" , Toast.LENGTH_LONG).show();
                        globalv.setUsername(user);
                        db.updatedate(username.getText().toString() , password.getText().toString() , save_data);
                        Intent intent = new Intent(getApplicationContext() , Videos_ListView.class);
                        startActivity(intent);
                    }
                    else {Toast.makeText(getApplicationContext() , "Login Failed" , Toast.LENGTH_LONG).show();
                    }


                }catch (JSONException e){e.printStackTrace();
                }
            }
        };
        Log_in log_in = new Log_in(user , pass , listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(log_in);
    }



    public void visibile(View view) {
        relativeLayout.setVisibility(View.VISIBLE);

    }
}
