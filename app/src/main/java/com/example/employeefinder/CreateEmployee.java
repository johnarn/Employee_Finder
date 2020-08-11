package com.example.employeefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CreateEmployee extends Activity {

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapterAttributesOfEmployee, adapterAvailableAttributesOfEmployee;
    private Button btnCreateOk;
    private EditText editTextEmployeeName, editTextEmployeeDayOfBirth, editTextEmployeeHomeAddress;
    private RadioGroup radioGroupDriversLicense;
    private DbController dbController;
    private ArrayList<String> employees_list;
    private ListView listViewAvailableAttributesOfEmployee, listViewAttributeOfEmployee;
    private ArrayList<String> listAvailableAttributes, listAttributeOfEmployee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_employee);
        dbController = new DbController(this);

        listAvailableAttributes = new ArrayList<>();
        listAttributeOfEmployee = new ArrayList<>();


        editTextEmployeeName = findViewById(R.id.editTextEmployeeName);
        editTextEmployeeDayOfBirth = findViewById(R.id.editTextEmployeeDayOfBirth);
        editTextEmployeeHomeAddress = findViewById(R.id.editTextEmployeeHomeAddress);
        radioGroupDriversLicense = findViewById(R.id.radioGroupDriversLicense);
        btnCreateOk = findViewById(R.id.btnCreateOk);
        listViewAvailableAttributesOfEmployee = findViewById(R.id.listViewAvailableAttributesOfEmployee);
        listViewAttributeOfEmployee = findViewById(R.id.listViewAttributeOfEmployee);


        btnCreateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmployeeName.getText().toString().matches("")) {
                    Toast.makeText(CreateEmployee.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextEmployeeDayOfBirth.getText().toString().matches("")) {
                    Toast.makeText(CreateEmployee.this, "You did not enter a birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextEmployeeHomeAddress.getText().toString().matches("")) {
                    Toast.makeText(CreateEmployee.this, "You did not enter a home address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radioGroupDriversLicense.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CreateEmployee.this, "You did not enter driver's license", Toast.LENGTH_SHORT).show();
                    return;
                }


                int radioButtonID = radioGroupDriversLicense.getCheckedRadioButtonId();
                final RadioButton radioButton = radioGroupDriversLicense.findViewById(radioButtonID);
                dbController.insertToEmployeesTable(editTextEmployeeName.getText().toString(),
                        radioButton.getText().toString(), editTextEmployeeDayOfBirth.getText().toString(),
                        editTextEmployeeHomeAddress.getText().toString());

                for (int i = 0; i < adapterAttributesOfEmployee.getCount(); i++) {
                    dbController.insertToEmployeeAttributeTable(adapterAttributesOfEmployee.getItem(i), editTextEmployeeName.getText().toString());
                }

                Intent intent = new Intent();
                intent.putExtra("NAME", editTextEmployeeName.getText().toString());
                setResult(2, intent);
                finish();//finishing activity
            }

        });

        listAvailableAttributes = dbController.getAttributes_names();

        adapterAttributesOfEmployee = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listAttributeOfEmployee);

        adapterAvailableAttributesOfEmployee = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listAvailableAttributes);


        listViewAttributeOfEmployee = findViewById(R.id.listViewAttributeOfEmployee);
        listViewAttributeOfEmployee.setAdapter(adapterAttributesOfEmployee);
        listViewAttributeOfEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterAvailableAttributesOfEmployee.add(adapterAttributesOfEmployee.getItem(position));
                adapterAttributesOfEmployee.remove(adapterAttributesOfEmployee.getItem(position));

            }
        });

        listViewAvailableAttributesOfEmployee = findViewById(R.id.listViewAvailableAttributesOfEmployee);
        listViewAvailableAttributesOfEmployee.setAdapter(adapterAvailableAttributesOfEmployee);
        listViewAvailableAttributesOfEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterAttributesOfEmployee.add(adapterAvailableAttributesOfEmployee.getItem(position));
                adapterAvailableAttributesOfEmployee.remove(adapterAvailableAttributesOfEmployee.getItem(position));


            }
        });
        adapterAttributesOfEmployee.notifyDataSetChanged();
        adapterAvailableAttributesOfEmployee.notifyDataSetChanged();


    }
}
