package com.example.madad_pro;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dialog_handler extends AppCompatDialogFragment {

    private ToggleButton emergency_toggle;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box, null);
        builder.setView(view);
        Spinner crime_category = view.findViewById(R.id.spinner);         //codingflow
        Switch auth_toggle = (Switch) view.findViewById(R.id.switch1);
        //Toast.makeText(this, "Switch: " + auth_toggle, Toast.LENGTH_SHORT).show();
         Boolean toggleButtonState=auth_toggle.isChecked();
         String crime = crime_category.getSelectedItem().toString();
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confrim = view.findViewById(R.id.confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return builder.create();

    }
}
