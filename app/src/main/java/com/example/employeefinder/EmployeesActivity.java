package com.example.employeefinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeesActivity extends Activity {


    ArrayList<String> listItems = new ArrayList<String>();


    private DbController dbController;

    private FloatingActionButton btnAddEmployee;
    private Context context;
    private ListView listViewOfEmployees;
    private ArrayAdapter<String> adapter;
    private int previous_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        dbController = new DbController(this);

        listViewOfEmployees = findViewById(R.id.listViewEmployees);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);

        listItems = dbController.getEmployees_names();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listViewOfEmployees.setAdapter(adapter);


        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeesActivity.this, CreateEmployee.class);
                startActivityForResult(intent, 2);

            }
        });


        listViewOfEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                previous_position = position;
                Intent intent = new Intent(EmployeesActivity.this, EditEmployee.class);
                intent.putExtra("old_name", listItems.get(position));
                startActivityForResult(intent, 3);
            }
        });

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listViewOfEmployees.setAdapter(adapter);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            String returnedResult = data.getStringExtra("NAME");
            listItems.add(returnedResult);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }
        if (requestCode == 3){
            String returnedResult = data.getStringExtra("NAME");
            listItems.set(previous_position, returnedResult);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }


    }


}
