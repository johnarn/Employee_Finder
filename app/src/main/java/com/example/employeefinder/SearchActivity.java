package com.example.employeefinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {

    /**
     * Initialize variables
     */
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize the controller of the database
        DbController dbController = new DbController(this);


        // Initialize the views of the SearchActivity
        SearchView searchView = findViewById(R.id.searchView);
        ListView listView = findViewById(R.id.listView);

        // Populate the listView with the attributes names
        list = dbController.getAttributes_names();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // Filter the search query
                if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(SearchActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {

            //Go to SearchResultActivity and send the attribute name that has been selected
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("attribute_name", list.get(position));
            startActivity(intent);
        });


    }


}

