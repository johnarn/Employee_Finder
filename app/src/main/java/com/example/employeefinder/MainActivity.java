package com.example.employeefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAttributes = findViewById(R.id.btnAttributes);
        Button btnEmployees = findViewById(R.id.btnEmployees);
        Button btnMap = findViewById(R.id.btnMap);



        btnAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Attributes.class);
                startActivity(intent);
            }
        });
    }
}