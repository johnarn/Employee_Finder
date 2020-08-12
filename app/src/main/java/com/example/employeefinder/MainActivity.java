package com.example.employeefinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity class
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all the views from the main_activity
        Button btnAttributes = findViewById(R.id.btnAttributes);
        Button btnEmployees = findViewById(R.id.btnEmployees);
        Button btnMap = findViewById(R.id.btnMap);

        // Go to attributes activity
        btnAttributes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AttributesActivity.class);
            startActivity(intent);
        });

        // Go to employees activity
        btnEmployees.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EmployeesActivity.class);
            startActivity(intent);
        });

        // Go to map activity
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}