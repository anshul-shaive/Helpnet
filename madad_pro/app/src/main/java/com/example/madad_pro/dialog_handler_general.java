package com.example.madad_pro;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class dialog_handler_general extends AppCompatDialogFragment {

    private String url = "http://172.16.16.243:8000/request";


    private Boolean auth_involved= false;
    private String crime;
    private String description;
    private String cash;
    private int i_person,i_cash;
    private String persons;
    EditText edit_desc;
    EditText edit_cash,edit_persons;

    private String user_id;
    private Double Lat =0.0;
    private Double Lng =0.0;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy", Locale.getDefault());

    private String currentDateandTime = sdf.format(new Date());

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box_general, null);
        builder.setView(view);


        user_id = ((MyApplication) getActivity().getApplication()).getUser_id();
        Lat = ((MyApplication) getActivity().getApplication()).getLat();
        Lng = ((MyApplication) getActivity().getApplication()).getLng();

        edit_desc = view.findViewById(R.id.editText);
         edit_cash = (EditText)view.findViewById(R.id.cash_editText);
         edit_persons = (EditText)view.findViewById(R.id.person_editText);
         edit_desc.requestFocus();

          description = edit_desc.getText().toString();
          cash = edit_cash.getText().toString();
          persons = edit_cash.getText().toString();

          i_person = Integer.parseInt(persons);
          i_cash = Integer.parseInt(cash);

        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   dismiss();
//                Intent intent = new Intent(getContext(),help2.class);
//                startActivity(intent);
//                getActivity().finish();
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
                        intent.putExtra("loc",""+Lat+":"+Lng);

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

                params.put("status","active");
                params.put("username","");
                params.put("user_id",""+user_id);
                params.put("req_time",""+currentDateandTime);
                params.put("nprespond","0");
                params.put("location",""+Lat+":"+Lng);
                params.put("presponded_ids","");
                params.put("passigned_ids","");
                params.put("description",""+description);
                params.put("cash",""+cash);
                params.put("persons_required",""+persons);

                return params;
            }

        };

        queue.add(postRequest);

    }

}
