package com.example.employeefinder;

import android.app.Activity;
import android.content.Context;
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

public class EditEmployee extends Activity {

    private Button btnEditOk;
    private EditText editTextEmployeeName, editTextEmployeeDayOfBirth, editTextEmployeeHomeAddress;
    private RadioGroup radioGroupDriversLicense;
    private DbController dbController;
    private String old_name;
    private ListView listViewAttributeOfEmployee, listViewAvailableAttributesOfEmployee;
    private ArrayList<String> listAttributeOfEmployee, listAvailableAttributes;
    ArrayAdapter<String> adapterAttributesOfEmployee, adapterAvailableAttributesOfEmployee;
    Context context;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_employee);
        dbController = new DbController(this);
        old_name = getIntent().getStringExtra("old_name");
        context = this;


        listAttributeOfEmployee = new ArrayList<>();
        listAvailableAttributes = new ArrayList<>();


        editTextEmployeeName = findViewById(R.id.editTextEmployeeName);
        editTextEmployeeDayOfBirth = findViewById(R.id.editTextEmployeeDayOfBirth);
        editTextEmployeeHomeAddress = findViewById(R.id.editTextEmployeeHomeAddress);
        radioGroupDriversLicense = findViewById(R.id.radioGroupDriversLicense);
        btnEditOk = findViewById(R.id.btnEditOk);
        listViewAvailableAttributesOfEmployee = findViewById(R.id.listViewAvailableAttributesOfEmployee);
        listViewAttributeOfEmployee = findViewById(R.id.listViewAttributeOfEmployee);




        btnEditOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                int radioButtonID = radioGroupDriversLicense.getCheckedRadioButtonId();
                final RadioButton radioButton = radioGroupDriversLicense.findViewById(radioButtonID);

                dbController.replaceEmployee(old_name, editTextEmployeeName.getText().toString(),
                        radioButton.getText().toString(), editTextEmployeeDayOfBirth.getText().toString(),
                        editTextEmployeeHomeAddress.getText().toString());
                Intent intent=new Intent();
                intent.putExtra("NAME",editTextEmployeeName.getText().toString());
                setResult(2,intent);
                System.out.println(adapterAttributesOfEmployee.getCount());
                for(int i=0; i< adapterAttributesOfEmployee.getCount(); i++){
                    if(i == 0){
                        dbController.replaceToEmployeeAttributeTable(adapterAttributesOfEmployee.getItem(i), editTextEmployeeName.getText().toString(), true);
                    }else{
                        dbController.replaceToEmployeeAttributeTable(adapterAttributesOfEmployee.getItem(i), editTextEmployeeName.getText().toString(), false);
                    }
                }
                finish();//finishing activity
            }
        });


        listAvailableAttributes = dbController.getAttributes_names();
        listAttributeOfEmployee = dbController.getEmployee_attribute_names(old_name);

        for(String attr : listAttributeOfEmployee){
            listAvailableAttributes.remove(attr);
        }


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
