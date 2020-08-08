package com.example.employeefinder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Attributes extends AppCompatActivity {

    private Context context = this;
    private EditText attributeName;
    private Dialog dialog;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributes);

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_create_attribute);
        dialog.setCancelable(false);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                if (dialog.isShowing()) {
                    btnOk = dialog.findViewById(R.id.btnCreateOk);
                    System.out.println(btnOk != null);
                    attributeName = dialog.findViewById(R.id.editTextAttributeName);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            TODO
                                -Set new attribute to database
                                -Check if the attribute exists
                                -Show the new attribute to ListView
                             */
                            System.out.println(attributeName.getText());
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }
}