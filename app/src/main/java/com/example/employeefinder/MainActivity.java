package com.example.employeefinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         TODO
            -Exchange the dbController through out the whole project
         */
        final DbController dbController = new DbController(context);

        Button btnAttributes = findViewById(R.id.btnAttributes);
        Button btnEmployees = findViewById(R.id.btnEmployees);
        Button btnMap = findViewById(R.id.btnMap);


        btnAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AttributesActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmployeesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}