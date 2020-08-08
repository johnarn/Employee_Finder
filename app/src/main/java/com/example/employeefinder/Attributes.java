package com.example.employeefinder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Attributes extends AppCompatActivity {

    private Context context = this;
    private EditText attributeName, attributeEditName;
    private Dialog createDialog, editDialog;
    private Button btnOk, btnEditOk, btnEditDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributes);

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        ListView listView = findViewById(R.id.listView);

        editDialog = new Dialog(context);
        editDialog.setContentView(R.layout.dialog_edit_attribute);
        editDialog.setCancelable(false);


        createDialog = new Dialog(context);
        createDialog.setContentView(R.layout.dialog_create_attribute);
        createDialog.setCancelable(false);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.show();
                if (createDialog.isShowing()) {
                    btnOk = createDialog.findViewById(R.id.btnCreateOk);
                    attributeName = createDialog.findViewById(R.id.editTextAttributeName);

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
                            createDialog.dismiss();
                        }
                    });
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editDialog.show();
                if (editDialog.isShowing()) {
                    btnEditOk = editDialog.findViewById(R.id.btnEditOK);
                    btnEditDelete = editDialog.findViewById(R.id.btnEditDelete);
                    attributeEditName = editDialog.findViewById(R.id.editTextEditAttributeName);
                    btnEditOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            TODO
                                -Replace Attribute name at listView and database
                             */
                            editDialog.dismiss();
                        }
                    });

                    btnEditDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            TODO
                                -Delete Attribute at listView and database
                                -Remove this attribute from all the employees who has it
                             */
                            System.out.println(attributeEditName.getText());
                            editDialog.dismiss();
                        }
                    });
                }


            }
        });


    }
}