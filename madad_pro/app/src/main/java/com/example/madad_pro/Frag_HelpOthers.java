package com.example.madad_pro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.madad_pro.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Frag_HelpOthers extends Fragment {

    int user_id;

    Double Lat, Lng;
    String rtypes;
    MyRecyclerViewAdapter adapter;
    public Context mContext;


    public String url = "https://helpnet-web.herokuapp.com/loc";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final SpotsDialog spotsDialog = new SpotsDialog(Frag_HelpOthers.this.getActivity());
        spotsDialog.show();

        Lat = ((MyApplication) Frag_HelpOthers.this.getActivity().getApplication()).getLat();
        Lng = ((MyApplication) Frag_HelpOthers.this.getActivity().getApplication()).getLng();

        RequestQueue queue = Volley.newRequestQueue(Frag_HelpOthers.this.getActivity());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jObject = new JSONObject();

                        try {
                            jObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        spotsDialog.dismiss();

                        try {
                            rtypes = jObject.getString("rtypes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        rtypes = rtypes.substring(1, rtypes.length() - 1);

                        ArrayList reqList = new ArrayList<String>(Arrays.asList(rtypes.split(",")));

                        for (int i = 0; i < reqList.size(); i++) {
                            reqList.set(i, reqList.get(i).toString().substring(1, reqList.get(i).toString().length() - 1));
                        }

                        // set up the RecyclerView
                        RecyclerView recyclerView =getView().findViewById(R.id.rv_Animals);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        adapter = new MyRecyclerViewAdapter(getActivity(), reqList);
                        //adapter.setClickListener(getActivity().this);
                        recyclerView.setAdapter(adapter);
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

                params.put("user_id", "" + user_id);
                params.put("last_loc", "" + Lat + ":" + Lng);

                return params;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        queue.add(postRequest);


        return inflater.inflate(R.layout.fraghelpothers, container, false);
    }
}


