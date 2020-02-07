package com.example.madad_pro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class login extends AppCompatActivity {

 RequestQueue queue;

public String url = "https://helpnet-web.herokuapp.com/login";
//public String url = "http://192.168.0.5:8000/login";


    EditText editusername;
    EditText editpassword;
    CoordinatorLayout coordinatorLayout;

    public String username;
    public String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editusername = (EditText) findViewById(R.id.loginusernameEt);
        editpassword = (EditText) findViewById(R.id.loginpassEt);
    }

    public void signin(View view)
    {
        username = editusername.getText().toString();
        password = editpassword.getText().toString();
        sendAndRequestResponse();

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(login.this, Option.class);
        startActivity(intent);
        login.this.finish();
    }

    private void sendAndRequestResponse() {
        queue = Volley.newRequestQueue(this);

        final SpotsDialog spotsDialog = new SpotsDialog(login.this);
        spotsDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObject;
                        String user_id = "0";
                        String status = "";

                        Log.d("", response);
                        System.out.println(response);

                        if (response.equals("Wrong Password")) {
                            spotsDialog.dismiss();
                            Toast.makeText(login.this, "Wrong Password", Toast.LENGTH_LONG).show();

                        }
                        else {

                            try {
                                jObject = new JSONObject(response);
                                status = jObject.getString("status");
                                user_id = jObject.getString("user_id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(login.this, "" + status, Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = getSharedPreferences("token_sp", MODE_PRIVATE).edit();
                            editor.putString("token", status);
                            editor.putInt("user_id", Integer.parseInt(user_id));
                            editor.apply();

                            ((MyApplication) login.this.getApplication()).setUser_id(user_id);
                            ((MyApplication) login.this.getApplication()).setStatus(status);

                            Intent intent;

                            if (status.equals("verified")) {
                                spotsDialog.dismiss();
                                intent = new Intent(login.this, MainActivity.class);
                            } else {
                                spotsDialog.dismiss();
                                intent = new Intent(login.this, Option.class);
                                Toast.makeText(login.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                            }

                            startActivity(intent);
                            login.this.finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                        spotsDialog.dismiss();
                        Intent intent;
                        intent = new Intent(login.this, Option.class);
                        Toast.makeText(login.this, "Please try again if registered or Sign Up", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        login.this.finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username","" +username);
                params.put("password",""+password);

                return params;
            }
        };
            queue.add(postRequest);
    }
}
