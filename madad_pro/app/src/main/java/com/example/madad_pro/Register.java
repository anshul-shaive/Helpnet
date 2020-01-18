package com.example.madad_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

//import com.google.android.gms.common.api.Response;

public class Register extends AppCompatActivity {
    private EditText editfullname;
    private EditText editaadhar;
    private EditText editmob_no;
    private EditText editpassword;
    private EditText editconf_password;

    public String fullname;
    public String aadhar;
    public String mob_no;
    public String password;
    public String conf_password;

    RequestQueue queue;

//public String url = "https://helpnet-web.herokuapp.com";
public String url = "http://192.168.0.5:8000/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editfullname=(EditText)findViewById(R.id.fullnameTv);
        editaadhar=(EditText)findViewById(R.id.aadharTv);
        editmob_no=(EditText)findViewById(R.id.mobileTv);
        editpassword=(EditText)findViewById(R.id.edit_passwordTv);
        editconf_password=(EditText)findViewById(R.id.confirmTv);


    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent;
        Toast.makeText(this,"Registration left incomplete",Toast.LENGTH_SHORT).show();
        intent = new Intent(Register.this, Option.class);
        startActivity(intent);
        Register.this.finish();
    }
    public void cancel(View view)
    {
        Intent intent =new Intent(Register.this, Option.class);
        startActivity(intent);
        Register.this.finish();
    }
    public void confirm(View view)
    {
        fullname = editfullname.getText().toString();
        aadhar = editaadhar.getText().toString();
        mob_no = editmob_no.getText().toString();
        password = editpassword.getText().toString();
        conf_password = editconf_password.getText().toString();
        sendAndRequestResponse();

    }

    private void sendAndRequestResponse() {

        final SpotsDialog spotsDialog = new SpotsDialog(Register.this);
        spotsDialog.show();
        queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
//                        Log.d("Response", response);
//
                        spotsDialog.dismiss();

                        if(response.equals("Registered")) {
                            Intent intent = new Intent(Register.this, Otp.class);
                            startActivity(intent);
                            Register.this.finish();

                        }
                        Toast.makeText(Register.this, ""+response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname","" +fullname);
                params.put("aadhar",""+aadhar);
                params.put("phone",""+mob_no);
                params.put("username","");
                params.put("phelped","");
                params.put("last_loc","");
                params.put("avg_rating","");
                params.put("verified","");
                params.put("password",""+password);

                return params;
            }
        };

        if(conf_password.equals(password)) {
            queue.add(postRequest);
        }
    }

}
