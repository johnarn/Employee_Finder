package com.example.employeefinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {

    /**
     * Initialize variables
     */
    private int positionOfItem;
    private ArrayList<String> employee_names = new ArrayList<>();
    private HashMap<String, Boolean> employees_names;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // Initialize the controller of the database
        DbController dbController = new DbController(this);

        // HashMap that contains all the employees names that have the selected attribute
        employees_names = new HashMap<>();

        // Get the selected attribute name from previous activity (SearchActivity)
        String attr_name = getIntent().getStringExtra("attribute_name");

        // Initialize the views of the SearchResultActivity
        ListView listViewOfEmployees = findViewById(R.id.listViewOfEmployees);
        Button btnContinue = findViewById(R.id.btnContinue);

        // Populate the HashMap
        employee_names = dbController.getEmployeesNamesWithSpecificAttribute(attr_name);

        //Populate the listView with the employees names that have the selected attribute
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employee_names);
        listViewOfEmployees.setAdapter(adapter);
        listViewOfEmployees.setOnItemClickListener((parent, view, position, arg3) -> {
            view.setSelected(true);
            positionOfItem = position;
        });

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(SearchResultActivity.this, MapsActivity.class);
            // Set as true the selected employee
            for (String name : employee_names) {
                if (employee_names.get(positionOfItem).equals(name)) {
                    employees_names.put(name, true);
                } else {
                    employees_names.put(name, false);
                }
            }
            // Send the hashMap to MapsActivity
            intent.putExtra("HashMapOfEmployeesNames", employees_names);
            startActivity(intent);
        });


    }
}
