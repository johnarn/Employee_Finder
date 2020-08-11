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
    private ArrayList<Integer> employee_attribute_ids = new ArrayList<>();
    private ArrayList<Integer> attribute_employee_ids = new ArrayList<>();


    public DbController(Context context) {
        sql = context.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        createTables();
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public ArrayList<String> getEmployee_attribute_names(String employee_name) {
        readFromEmployeeAttributeTableToFindAttributes("*", employee_name);
        ArrayList<Integer> attr_ids = getAttributes_ids();
        ArrayList<String> attr_names = getAttributes_names();
        ArrayList<String> employee_attribute_names = new ArrayList<>();
        for (int i = 0; i < employee_attribute_ids.size(); i++) {

            int position = attr_ids.indexOf(employee_attribute_ids.get(i));
            employee_attribute_names.add(attr_names.get(position));
        }
        return employee_attribute_names;
    }

    public ArrayList<String> getAttribute_employee_names(String attribute_name){
        readFromEmployeeAttributeTableToFindEmployees("*", attribute_name);
        ArrayList<Integer> emp_ids = getEmployees_ids();
        ArrayList<String> emp_names = getEmployees_names();
        ArrayList<String> attribute_employee_names = new ArrayList<>();
        for (int i = 0; i < attribute_employee_ids.size(); i++) {
            int position = emp_ids.indexOf(attribute_employee_ids.get(i));
            if(position>-1 && position <emp_names.size()){
                attribute_employee_names.add(emp_names.get(position));
            }
        }
        return attribute_employee_names;
    }

    public SQLiteDatabase getSql() {
        return sql;
    }

    public void setSql(SQLiteDatabase sql) {
        this.sql = sql;
    }

    public ArrayList<Integer> getAttributes_ids() {
        readFromAttributesTable("*");
        return attributes_ids;
    }

    public void setAttributes_ids(ArrayList<Integer> attributes_ids) {
        this.attributes_ids = attributes_ids;
    }

    public ArrayList<String> getAttributes_names() {
        readFromAttributesTable("*");
        return attributes_names;
    }

    public void setAttributes_names(ArrayList<String> attributes_names) {
        this.attributes_names = attributes_names;
    }

    public ArrayList<Integer> getEmployees_ids() {
        readFromEmployeesTable("*");
        return employees_ids;
    }

    public void setEmployees_ids(ArrayList<Integer> employees_ids) {
        this.employees_ids = employees_ids;
    }

    public ArrayList<String> getEmployees_names() {
        readFromEmployeesTable("*");
        return employees_names;
    }

    public void setEmployees_names(ArrayList<String> employees_names) {
        this.employees_names = employees_names;
    }

    public ArrayList<String> getLicenses() {
        readFromEmployeesTable("*");
        return licenses;
    }

    public void setLicenses(ArrayList<String> licenses) {
        this.licenses = licenses;
    }

    public ArrayList<String> getBirthdays() {
        readFromEmployeesTable("*");
        return birthdays;
    }

    public void setBirthdays(ArrayList<String> birthdays) {
        this.birthdays = birthdays;
    }

    public ArrayList<String> getAddresses() {
        readFromEmployeesTable("*");
        return addresses;
    }

    public void setAddresses(ArrayList<String> addresses) {
        this.addresses = addresses;
    }

    public void insertToEmployeesTable(String employee_name, String employee_license, String employee_birthday, String employee_address) {
        String query = "INSERT INTO Employees(name, license, birthday, address) VALUES('" + employee_name + "','" + employee_license + "','" + employee_birthday + "','" + employee_address + "');";
        sql.execSQL(query);
    }

    public void insertToAttributesTable(String attribute_name) {
        String query = "INSERT INTO Attributes(name) VALUES('" + attribute_name + "');";
        sql.execSQL(query);
    }

    public void insertToEmployeeAttributeTable(String attribute_name, String employee_name) {
        readFromAttributesTable("*");
        readFromEmployeesTable("*");
        int attr_position = attributes_names.indexOf(attribute_name);
        int empl_position = employees_names.indexOf(employee_name);
        String query = "INSERT INTO EmployeeAttribute(attribute_id, employee_id) VALUES('" + attributes_ids.get(attr_position) + "', '" + employees_ids.get(empl_position) + "');";
        sql.execSQL(query);
    }

    public void replaceToEmployeeAttributeTable(String attribute_name, String employee_name, boolean flag) {

        if (flag) {
            removeFromEmployeeAttributeTable(employee_name);
        }
        readFromAttributesTable("*");
        readFromEmployeesTable("*");
        insertToEmployeeAttributeTable(attribute_name, employee_name);
    }

    public void readFromEmployeeAttributeTableToFindAttributes(String column_name, String employee_name) {
        readFromEmployeesTable("*");
        int emp_id = employees_ids.get(employees_names.indexOf(employee_name));
        String query = "SELECT " + column_name + " FROM  EmployeeAttribute WHERE employee_id LIKE '" + emp_id + "';";
        Cursor c = sql.rawQuery(query, null);
        int attr_id = -1;
        if (c.moveToFirst()) {
            employee_attribute_ids.clear();
            do {
                attr_id = c.getInt(0);
                employee_attribute_ids.add(attr_id);

            } while (c.moveToNext());
        }
        c.close();
    }
    public void readFromEmployeeAttributeTableToFindEmployees(String column_name, String attribute_name) {
        readFromAttributesTable("*");
        int attr_id = attributes_ids.get(attributes_names.indexOf(attribute_name));
        String query = "SELECT " + column_name + " FROM  EmployeeAttribute WHERE attribute_id LIKE '" + attr_id + "';";
        Cursor c = sql.rawQuery(query, null);
        int emp_id = -1;
        if (c.moveToFirst()) {
            attribute_employee_ids.clear();
            do {
                emp_id = c.getInt(1);
                attribute_employee_ids.add(emp_id);

            } while (c.moveToNext());
        }
        c.close();
    }

    public void removeFromEmployeeAttributeTable(String employee_name) {
        readFromEmployeesTable("*");
        readFromAttributesTable("*");
        int empl_position = employees_names.indexOf(employee_name);
        String query = "DELETE FROM EmployeeAttribute WHERE employee_id LIKE '" + employees_ids.get(empl_position) + "' ;";
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

    public boolean replaceAttribute(String oldName, String newName) {
        removeAttribute(oldName);
        insertToAttributesTable(newName);
        readFromAttributesTable("*");
        return attributes_names.contains(newName);
    }

    public boolean replaceEmployee(String old_employee_name,
                                   String new_employee_name, String new_employee_license, String new_employee_birthday, String new_employee_address) {

        removeEmployee(old_employee_name);
        insertToEmployeesTable(new_employee_name, new_employee_license, new_employee_birthday, new_employee_address);
        readFromEmployeesTable("*");
        return employees_names.contains(new_employee_name);
    }

    public boolean removeAttribute(String name) {

        String query = "DELETE FROM Attributes WHERE name LIKE '" + name + "';";
        sql.execSQL(query);
        readFromAttributesTable("*");
        return !attributes_names.contains(name);

    }

    public boolean removeEmployee(String name) {

        String query = "DELETE FROM Employees WHERE name LIKE '" + name + "';";
        sql.execSQL(query);
        readFromEmployeesTable("*");
        return !employees_names.contains(name);

    }

    public ArrayList<Integer> getAttributesOfEmployee(int empl_id) {
        String query = "SELECT * FROM  EmployeeAttribute WHERE employee_id LIKE '" + empl_id + "';";
        Cursor c = sql.rawQuery(query, null);
        ArrayList<Integer> attributesOfEmployee = new ArrayList<>();
        if (c.moveToFirst()) {
            int Aid = -1;
            do {

                attributesOfEmployee.add(c.getInt(0));


            } while (c.moveToNext());
        }
        c.close();
        return attributesOfEmployee;
    }




}
