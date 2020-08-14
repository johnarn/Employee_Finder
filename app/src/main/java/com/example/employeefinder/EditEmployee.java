package com.example.employeefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EditEmployee extends Activity {

    /**
     * Initialize variables
     */
    private ArrayAdapter<String> adapterAttributesOfEmployee, adapterAvailableAttributesOfEmployee;
    private EditText editTextEmployeeName, editTextEmployeeDayOfBirth, editTextEmployeeHomeAddress;
    private RadioGroup radioGroupDriversLicense;
    private DbController dbController;
    private String old_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_employee);

        // Initialize the database controller
        dbController = new DbController(this);

        // The name of the selected employee
        old_name = getIntent().getStringExtra("old_name");

        // Contains all the attributes of a certain employee
        ArrayList<String> listAttributeOfEmployee;

        // Contains all the available attributes for an employee

        // Initialize all the views from the dialog_create_employee
        editTextEmployeeName = findViewById(R.id.editTextEmployeeName);
        editTextEmployeeDayOfBirth = findViewById(R.id.editTextEmployeeDayOfBirth);
        editTextEmployeeHomeAddress = findViewById(R.id.editTextEmployeeHomeAddress);
        radioGroupDriversLicense = findViewById(R.id.radioGroupDriversLicense);
        Button btnEditOk = findViewById(R.id.btnEditOk);
        Button btnEditDelete = findViewById(R.id.btnEditDelete);
        ListView listViewAvailableAttributesOfEmployee = findViewById(R.id.listViewAvailableAttributesOfEmployee);
        ListView listViewAttributeOfEmployee = findViewById(R.id.listViewAttributeOfEmployee);

        // Populate the list with all the available attributes from database
        ArrayList<String> listAvailableAttributes = dbController.getAttributes_names();

        // Populate the list with the attributes of the specific employee from the database
        listAttributeOfEmployee = dbController.getAttributesNamesOfSpecificEmployee(old_name);

        // Removes the attributes which the employee already has from the list of available attributes
        for (String attr : listAttributeOfEmployee) {
            listAvailableAttributes.remove(attr);
        }

        // Show all the attributes that a certain employee has
        adapterAttributesOfEmployee = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listAttributeOfEmployee);
        listViewAttributeOfEmployee.setAdapter(adapterAttributesOfEmployee);
        listViewAttributeOfEmployee.setOnItemClickListener((parent, view, position, id) -> {
            adapterAvailableAttributesOfEmployee.add(adapterAttributesOfEmployee.getItem(position));
            adapterAttributesOfEmployee.remove(adapterAttributesOfEmployee.getItem(position));
        });

        // Show all the available attributes that a certain employee can have
        adapterAvailableAttributesOfEmployee = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listAvailableAttributes);
        listViewAvailableAttributesOfEmployee.setAdapter(adapterAvailableAttributesOfEmployee);
        listViewAvailableAttributesOfEmployee.setOnItemClickListener((parent, view, position, id) -> {
            adapterAttributesOfEmployee.add(adapterAvailableAttributesOfEmployee.getItem(position));
            adapterAvailableAttributesOfEmployee.remove(adapterAvailableAttributesOfEmployee.getItem(position));
        });
        adapterAttributesOfEmployee.notifyDataSetChanged();
        adapterAvailableAttributesOfEmployee.notifyDataSetChanged();

        btnEditDelete.setOnClickListener(v -> {
            dbController.removeEmployee(old_name);

            // Sent back to EmployeesActivity the name of the employee that will be deleted
            Intent intent = new Intent();
            intent.putExtra("NAME", old_name);
            setResult(4, intent);

            // finish the activity
            finish();
        });

        btnEditOk.setOnClickListener(v -> {

            // Security checks
            if (editTextEmployeeName.getText().toString().matches("")) {
                Toast.makeText(EditEmployee.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextEmployeeDayOfBirth.getText().toString().matches("")) {
                Toast.makeText(EditEmployee.this, "You did not enter a birthday", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextEmployeeHomeAddress.getText().toString().matches("")) {
                Toast.makeText(EditEmployee.this, "You did not enter a home address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (radioGroupDriversLicense.getCheckedRadioButtonId() == -1) {
                Toast.makeText(EditEmployee.this, "You did not enter driver's license", Toast.LENGTH_SHORT).show();
                return;
            }

            // Find which radio button is checked
            int radioButtonID = radioGroupDriversLicense.getCheckedRadioButtonId();
            final RadioButton radioButton = radioGroupDriversLicense.findViewById(radioButtonID);

            // Replace the entry in the Employees table
            dbController.replaceEmployee(old_name, editTextEmployeeName.getText().toString(),
                    radioButton.getText().toString(), editTextEmployeeDayOfBirth.getText().toString(),
                    editTextEmployeeHomeAddress.getText().toString());

            // Connect the employee with all of his attributes
            for (int i = 0; i < adapterAttributesOfEmployee.getCount(); i++) {
                if (i == 0) {
                    dbController.replaceToEmployeeAttributeTable(adapterAttributesOfEmployee.getItem(i), editTextEmployeeName.getText().toString(), true);
                } else {
                    dbController.replaceToEmployeeAttributeTable(adapterAttributesOfEmployee.getItem(i), editTextEmployeeName.getText().toString(), false);
                }
            }

            // Sent back to EmployeesActivity the name of the new employee
            Intent intent = new Intent();
            intent.putExtra("NAME", editTextEmployeeName.getText().toString());
            setResult(2, intent);

            // finish the activity
            finish();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(1);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
