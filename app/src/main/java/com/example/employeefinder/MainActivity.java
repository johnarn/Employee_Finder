package com.example.employeefinder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Context context = this;

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDB();

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

    private void initializeDB() {
        SQLiteDatabase sql = openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);

        createTables(sql);

        insertToAttributesTable(sql, "Jack");
        insertToEmployeesTable(sql, "Name", "yes", "4/1/95", "home");


        readFromAttributesTable("*", sql);
        readFromEmployeesTable("*", sql);
    }

    private void insertToEmployeesTable(SQLiteDatabase sql, String employee_name, String employee_license, String employee_birthday, String employee_address) {
        String query = "INSERT INTO Employees(name, license, birthday, address) VALUES('" + employee_name + "','" + employee_license + "','" + employee_birthday + "','" + employee_address + "');";
        sql.execSQL(query);
    }

    private void insertToAttributesTable(SQLiteDatabase sql, String attribute_name) {
        String query = "INSERT INTO Attributes(name) VALUES('" + attribute_name + "');";
        sql.execSQL(query);
    }

    public void dropTables(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

        // call DROP TABLE on every table name
        for (String table : tables) {
            System.out.println("TABLES: " + table);

            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
        c.close();

    }

    public void createTables(SQLiteDatabase sql) {
        String query = "CREATE TABLE IF NOT EXISTS Attributes( _id INTEGER PRIMARY KEY, name TEXT);";
        sql.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS Employees( _id INTEGER PRIMARY KEY, name TEXT, license TEXT, birthday TEXT, address TEXT);";
        sql.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS EmployeeAttribute(" +
                "attribute_id INTEGER, " +
                "employee_id INTEGER, " +
                "FOREIGN KEY(attribute_id) REFERENCES Attributes(_id),  " +
                "FOREIGN KEY(employee_id) REFERENCES Employees(_id));";

        sql.execSQL(query);


    }

    public void readFromAttributesTable(String column_name, SQLiteDatabase sql) {
        String query = "SELECT " + column_name + " FROM  Attributes;";
        Cursor c = sql.rawQuery(query, null);
        c.moveToFirst();
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> attributes_names = new ArrayList<>();
        int id = -1;
        String attribute_name = "";
        do {

            id = c.getInt(0);
            attribute_name = c.getString(1);


            ids.add(id);
            attributes_names.add(attribute_name);

        } while (c.moveToNext());
        System.out.println("FIRST ATTRIBUTE: " + ids.get(0) + " " + attributes_names.get(0));
        c.close();
    }

    public void readFromEmployeesTable(String column_name, SQLiteDatabase sql) {
        String query = "SELECT " + column_name + " FROM  Employees;";
        Cursor c = sql.rawQuery(query, null);
        c.moveToFirst();
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> employees_names = new ArrayList<>();
        ArrayList<String> licenses = new ArrayList<>();
        ArrayList<String> birthdays = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();

        int id = -1;
        String employee_name = "", license = "", birthday = "", address = "";
        do {

            id = c.getInt(0);
            employee_name = c.getString(1);
            license = c.getString(2);
            birthday = c.getString(3);
            address = c.getString(4);


            ids.add(id);
            employees_names.add(employee_name);
            licenses.add(license);
            birthdays.add(birthday);
            addresses.add(address);

        } while (c.moveToNext());
        System.out.println("FIRST EMPLOYEE: " + ids.get(0) + " " + employees_names.get(0) + " " + licenses.get(0) + " " + birthdays.get(0) + " " + addresses.get(0) + " ");
        c.close();
    }
}