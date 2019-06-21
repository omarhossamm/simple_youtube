package com.example.omarhossam.omar_al_odah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {
    EditText username , password , con_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        con_pass = (EditText) findViewById(R.id.confirm_pass);
    }

    public void regstration(View view) {
       // Failed Registration If Password Not Matched
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String con = con_pass.getText().toString();
        if (user.length() == 0 || pass.length() == 0){
            Toast.makeText(getApplicationContext() , "Enter Username Or Password" , Toast.LENGTH_LONG).show();
        }
        else if (pass.length() < 5){
            Toast.makeText(getApplicationContext() , "Password Is Very Weak" , Toast.LENGTH_LONG).show();
        }
        else if (!pass.equals(con)){
            Toast.makeText(getApplicationContext() , "Password Not Matched" , Toast.LENGTH_LONG).show();
        }

        // Registration new account in database in mysql
        else {
            Response.Listener <String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(Registration.this , "Registration Successfull" , Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext() , Log_reg.class));
                        }
                        else{
                            Toast.makeText(Registration.this , "Username Already Exist" , Toast.LENGTH_LONG).show();
                        }

                    }catch (JSONException e){e.printStackTrace();
                    }
                }
            };
            regist send_data = new regist(user , pass , responseListner);
            RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
            requestQueue.add(send_data);


        }

    }

}
