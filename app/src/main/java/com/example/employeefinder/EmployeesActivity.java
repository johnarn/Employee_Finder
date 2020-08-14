package com.example.employeefinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeesActivity extends Activity {

    /**
     * Initialize variables
     */
    private ArrayList<String> listItems = new ArrayList<>();
    private ListView listViewOfEmployees;
    private ArrayAdapter<String> adapter;
    private int previous_position;

    private Dialog create_employee_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        // Initialize the database controller
        DbController dbController = new DbController(this);

        // Initialize the views
        listViewOfEmployees = findViewById(R.id.listViewEmployees);
        FloatingActionButton btnAddEmployee = findViewById(R.id.btnAddEmployee);

        // Get all the employees names from the database
        listItems = dbController.getEmployees_names();

        // Populate the listView with the employees names
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listViewOfEmployees.setAdapter(adapter);



        listViewOfEmployees.setOnItemClickListener((parent, view, position, id) -> {

            // Find the position of the selected employee
            previous_position = position;

            // Go to EditEmployee and send the name of the employee
            Intent intent = new Intent(EmployeesActivity.this, EditEmployee.class);
            intent.putExtra("old_name", listItems.get(position));
            startActivityForResult(intent, 3);
        });


        btnAddEmployee.setOnClickListener(v -> {

            // Go to CreateEmployee
            Intent intent = new Intent(EmployeesActivity.this, CreateEmployee.class);
            startActivityForResult(intent, 2);
        });
    }

    /**
     * Get Results from intents and update the view
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Results returned when back button is pressed
        if(resultCode == 1){

            // Update the listView of the Employees
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }

        // Results returned from CreateEmployee View
        if (resultCode == 2) {

            // Get the returned result
            String returnedResult = data.getStringExtra("NAME");

            // Update the listView of the Employees
            listItems.add(returnedResult);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }

        // Results returned from EditEmployee View (Create Employee)
        if (resultCode == 3) {

            // Get the returned result
            String returnedResult = data.getStringExtra("NAME");

            // Update the listView of the Employees
            listItems.set(previous_position, returnedResult);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }

        // Results returned from EditEmployee View (Delete Employee)
        if (resultCode == 4) {

            // Get the returned result
            String returnedResult = data.getStringExtra("NAME");

            // Update the listView of the Employees
            listItems.remove(returnedResult);
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            listViewOfEmployees.setAdapter(adapter);
        }
    }
}
