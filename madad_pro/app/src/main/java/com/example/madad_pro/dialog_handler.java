package com.example.madad_pro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import dmax.dialog.SpotsDialog;


public class dialog_handler extends AppCompatDialogFragment{


       private String url = "https://helpnet-web.herokuapp.com/request";
//    private String url = "http://192.168.0.5:8000/request";


    private Boolean auth_involved= false,nearby_people=false,emergency_contacts=false;
    private String crime;

    private String user_id;
    private Double pinnedLat =0.0;
    private Double pinnedLng =0.0;
    SharedPreferences.Editor editor;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy", Locale.getDefault());

    private String currentDateandTime;

    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box, null);
        builder.setView(view);

        Spinner crime_category = view.findViewById(R.id.spinner);
        Switch auth_toggle = (Switch) view.findViewById(R.id.switch1);
        Switch nearby_ppl_toggle = (Switch) view.findViewById(R.id.switch2);
        Switch emergency_cont_toggle = (Switch) view.findViewById(R.id.switch3);


        editor = this.getActivity().getSharedPreferences("token_sp", Context.MODE_PRIVATE).edit();

        user_id = ((MyApplication) getActivity().getApplication()).getUser_id();
        pinnedLat = ((MyApplication) getActivity().getApplication()).getPinnedlat();
        pinnedLng = ((MyApplication) getActivity().getApplication()).getPinnedlng();


        crime_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                 crime = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        auth_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                auth_involved=isChecked;
            }
        });

        nearby_ppl_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                nearby_people=isChecked;
            }
        });

        emergency_cont_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                emergency_contacts=isChecked;
            }
        });


        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndRequestResponse();

            }
        });

        return builder.create();

    }
    private void sendAndRequestResponse(){

        final SpotsDialog spotsDialog = new SpotsDialog(getContext());
        spotsDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spotsDialog.dismiss();
                        Intent intent = new Intent(getContext(),HelpInfo.class);
                        intent.putExtra("req_id",response);
                        intent.putExtra("loc",""+pinnedLat+":"+pinnedLng);

                        editor.putString("loc", ""+pinnedLat+":"+pinnedLng);
                        editor.putString("req_id", response);
                        editor.apply();

                        startActivity(intent);
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("auth_resp","" +auth_involved.toString());
                params.put("nearby_people","" +nearby_people.toString());
                params.put("emergency_contacts","" +emergency_contacts.toString());
       //         params.put("description","" +description.toString());
                params.put("req_type",""+crime);
                params.put("status","active");
                params.put("username","");
                params.put("user_id",""+user_id);
                currentDateandTime = sdf.format(new Date());
                params.put("req_time",""+currentDateandTime);
                params.put("nprespond","0");
                params.put("location",""+pinnedLat+":"+pinnedLng);
                params.put("presponded_ids","");
                params.put("passigned_ids","");


                return params;
            }

        };

        queue.add(postRequest);

    }
}

