package com.example.employeefinder;

import android.app.Dialog;
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

    /**
     * Initialize variables
     */
    private EditText attributeName, attributeEditName;
    private Dialog createDialog, editDialog;
    private Button btnOk, btnEditOk, btnEditDelete;
    private DbController dbController;
    private ArrayList<String> listOfAttributes = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FloatingActionButton btnAdd;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributes);

        // Initialize the database controller
        dbController = new DbController(AttributesActivity.this);

        // Initialize the views
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        editDialog = new Dialog(AttributesActivity.this);
        editDialog.setContentView(R.layout.dialog_edit_attribute);
        editDialog.setCancelable(false);

        createDialog = new Dialog(AttributesActivity.this);
        createDialog.setContentView(R.layout.dialog_create_attribute);
        createDialog.setCancelable(false);

        // Get all the attributes names from the database
        listOfAttributes = dbController.getAttributes_names();

        // Populate the listView with the attributes names
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listOfAttributes);
        listView.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.show();
                if (createDialog.isShowing()) {

                    // Initialize the views in the createDialog View
                    btnOk = createDialog.findViewById(R.id.btnCreateOk);
                    attributeName = createDialog.findViewById(R.id.editTextAttributeName);


                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Security check
                            if (attributeName.getText().toString().matches("")) {
                                Toast.makeText(AttributesActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Create the attribute to the database
                            if (!dbController.containIntoAttributeTable(attributeName.getText().toString())) {
                                dbController.insertToAttributesTable(attributeName.getText().toString());
                                listOfAttributes.add(attributeName.getText().toString());
                            }

                            // Update the listView with the new attribute
                            adapter = new ArrayAdapter<String>(AttributesActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    listOfAttributes);
                            listView.setAdapter(adapter);

                            // Set attribute text to "" for the next time that it will be in use
                            attributeName.setText("");

                            // finish
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

                    // Initialize all the Views in the EditDialog view
                    btnEditOk = editDialog.findViewById(R.id.btnEditOK);
                    btnEditDelete = editDialog.findViewById(R.id.btnEditDelete);
                    attributeEditName = editDialog.findViewById(R.id.editTextEditAttributeName);

                    btnEditOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Security checks
                            if (attributeEditName.getText().toString().matches("")) {
                                Toast.makeText(AttributesActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Replace the existing attribute with the new one at the database
                            dbController.replaceAttribute(listView.getItemAtPosition(position).toString(), attributeEditName.getText().toString());

                            // Get all the attributes names from the database
                            listOfAttributes = dbController.getAttributes_names();

                            // Update the listView with the new attribute
                            adapter = new ArrayAdapter<String>(AttributesActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    listOfAttributes);
                            listView.setAdapter(adapter);

                            // Set attribute text to "" for the next time that it will be in use
                            attributeEditName.setText("");

                            // finish
                            editDialog.dismiss();
                        }
                    });

                    btnEditDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Remove from the list the attribute
                            listOfAttributes.remove(listOfAttributes.get(position));

                            // Remove the attribute from the database
                            dbController.removeAttribute(listOfAttributes.get(position));

                            // Update the listView
                            adapter = new ArrayAdapter<String>(AttributesActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    listOfAttributes);
                            listView.setAdapter(adapter);

                            // Set attribute text to "" for the next time that it will be in use
                            attributeEditName.setText("");

                            // finish
                            editDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}