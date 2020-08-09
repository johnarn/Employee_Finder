package com.example.employeefinder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DbController {
    private SQLiteDatabase sql;
    private ArrayList<Integer> attributes_ids = new ArrayList<>();
    private ArrayList<String> attributes_names = new ArrayList<>();
    private ArrayList<Integer> employees_ids = new ArrayList<>();
    private ArrayList<String> employees_names = new ArrayList<>();
    private ArrayList<String> licenses = new ArrayList<>();
    private ArrayList<String> birthdays = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();

    public DbController(Context context) {
        sql = context.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        createTables();
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public void insertToEmployeesTable(String employee_name, String employee_license, String employee_birthday, String employee_address) {
        String query = "INSERT INTO Employees(name, license, birthday, address) VALUES('" + employee_name + "','" + employee_license + "','" + employee_birthday + "','" + employee_address + "');";
        sql.execSQL(query);
    }

    public void insertToAttributesTable(String attribute_name) {
        String query = "INSERT INTO Attributes(name) VALUES('" + attribute_name + "');";
        sql.execSQL(query);
    }

    public void dropTables() {
        Cursor c = sql.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

        // call DROP TABLE on every table name
        for (String table : tables) {
            System.out.println("TABLES: " + table);

            String dropQuery = "DROP TABLE IF EXISTS " + table;
            sql.execSQL(dropQuery);
        }
        c.close();

    }

    public void createTables() {
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

    public void readFromAttributesTable(String column_name) {
        String query = "SELECT " + column_name + " FROM  Attributes;";
        Cursor c = sql.rawQuery(query, null);
        int id = -1;
        String attribute_name = "";
        if (c.moveToFirst()) {
            attributes_ids.clear();
            attributes_names.clear();
            do {

                id = c.getInt(0);
                attribute_name = c.getString(1);

                attributes_ids.add(id);
                attributes_names.add(attribute_name);

            } while (c.moveToNext());
        }

        c.close();
    }

    public void readFromEmployeesTable(String column_name) {
        String query = "SELECT " + column_name + " FROM  Employees;";
        Cursor c = sql.rawQuery(query, null);
        if (c.moveToFirst()) {
            int id = -1;
            String employee_name = "", license = "", birthday = "", address = "";
            employees_ids.clear();
            employees_names.clear();
            licenses.clear();
            birthdays.clear();
            addresses.clear();
            do {

                id = c.getInt(0);
                employee_name = c.getString(1);
                license = c.getString(2);
                birthday = c.getString(3);
                address = c.getString(4);


                employees_ids.add(id);
                employees_names.add(employee_name);
                licenses.add(license);
                birthdays.add(birthday);
                addresses.add(address);

            } while (c.moveToNext());
        }
        c.close();
    }

    public boolean containIntoAttributeTable(String name) {
        readFromAttributesTable("*");
        return attributes_names.contains(name);
    }

    public boolean replaceAttribute(String name) {
        String query = "INSERT INTO Attributes(name) VALUES('" + name + "');";
        sql.execSQL(query);
        readFromAttributesTable("*");
        return attributes_names.contains(name);
    }

    public boolean removeAttribute(String name) {

        String query = "DELETE FROM Attributes WHERE name LIKE '" + name + "';";
        sql.execSQL(query);
        readFromAttributesTable("*");
        return !attributes_names.contains(name);

    }
}
