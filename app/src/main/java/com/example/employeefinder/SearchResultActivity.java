package com.example.employeefinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {
    private DbController dbController;
    private ListView listViewOfEmployees;
    private Button btnContinue;
    private int positionOfItem;
    ArrayAdapter<String> adapter;
    private ArrayList<String> employee_names = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        dbController = new DbController(this);
        final HashMap<String, Boolean> employees_names = new HashMap<>();

        String attr_name = getIntent().getStringExtra("attribute_name");

        listViewOfEmployees = findViewById(R.id.listViewOfEmployees);
        btnContinue = findViewById(R.id.btnContinue);



        employee_names = dbController.getAttribute_employee_names(attr_name);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, employee_names);
        listViewOfEmployees.setAdapter(adapter);


        listViewOfEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                positionOfItem = position;

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultActivity.this, MapsActivity.class);

                for(String name : employee_names){
                    if(employee_names.get(positionOfItem).equals(name)){
                        employees_names.put(name, true);
                    }else{
                        employees_names.put(name, false);
                    }
                }

                intent.putExtra("HashMapOfEmployeesNames", employees_names);
                startActivity(intent);
            }
        });



    }
}
