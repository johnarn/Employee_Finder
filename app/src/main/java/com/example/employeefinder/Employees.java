package com.example.employeefinder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Employees extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listAvailableItems = new ArrayList<String>();
    ArrayList<String> listItems = new ArrayList<String>();


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapterAttributesOfEmployee, adapterAvailableAttributesOfEmployee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_employee);
        getSupportActionBar().hide();


        adapterAttributesOfEmployee = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);


        adapterAvailableAttributesOfEmployee = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listAvailableItems);



        /*
         TODO
            -Get attributes from database
         */
        adapterAttributesOfEmployee.add("Hey");

        adapterAvailableAttributesOfEmployee.add("Hello");


        ListView list = findViewById(R.id.listViewAttributeOfEmployee);
        list.setAdapter(adapterAttributesOfEmployee);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterAvailableAttributesOfEmployee.add(adapterAttributesOfEmployee.getItem(position));
                adapterAttributesOfEmployee.remove(adapterAttributesOfEmployee.getItem(position));
            }
        });


        ListView listAvailable = findViewById(R.id.listViewAvailableAttributesOfEmployee);
        listAvailable.setAdapter(adapterAvailableAttributesOfEmployee);
        listAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
