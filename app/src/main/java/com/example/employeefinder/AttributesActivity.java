package com.example.employeefinder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AttributesActivity extends AppCompatActivity {

    private Context context = this;
    private EditText attributeName, attributeEditName;
    private Dialog createDialog, editDialog;
    private Button btnOk, btnEditOk, btnEditDelete;
    private DbController dbController;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributes);

        dbController = new DbController(context);


        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        final ListView listView = findViewById(R.id.listView);

        editDialog = new Dialog(context);
        editDialog.setContentView(R.layout.dialog_edit_attribute);
        editDialog.setCancelable(false);


        createDialog = new Dialog(context);
        createDialog.setContentView(R.layout.dialog_create_attribute);
        createDialog.setCancelable(false);

        listItems = dbController.getAttributes_names();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);


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

                            if(attributeEditName.getText().toString().matches("")){
                                Toast.makeText(AttributesActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (!dbController.containIntoAttributeTable(attributeName.getText().toString())) {
                                dbController.insertToAttributesTable(attributeName.getText().toString());
                                listItems.add(attributeName.getText().toString());
                            }
                            adapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1,
                                    listItems);
                            listView.setAdapter(adapter);
                            createDialog.dismiss();
                        }
                    });
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                editDialog.show();
                if (editDialog.isShowing()) {
                    btnEditOk = editDialog.findViewById(R.id.btnEditOK);
                    btnEditDelete = editDialog.findViewById(R.id.btnEditDelete);
                    attributeEditName = editDialog.findViewById(R.id.editTextEditAttributeName);
                    btnEditOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(attributeEditName.getText().toString().matches("")){
                                Toast.makeText(AttributesActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dbController.replaceAttribute(listView.getItemAtPosition(position).toString(), attributeEditName.getText().toString());
                            System.out.println("POSITION: " + position);
                            listItems = dbController.getAttributes_names();

                            adapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1,
                                    listItems);
                            listView.setAdapter(adapter);
                            attributeEditName.setText("");
                            editDialog.dismiss();
                        }
                    });

                    btnEditDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listItems.remove(listItems.get(position));
                            adapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_list_item_1,
                                    listItems);
                            listView.setAdapter(adapter);
                            attributeEditName.setText("");
                            editDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}