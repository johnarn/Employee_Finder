package com.example.employeefinder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * DbController contains all the necessary methods to make the communication between the application and the database
 */
public class DbController {

    /**
     *  Initialization of variables
     */
    private SQLiteDatabase sql;
    private ArrayList<Integer> attributes_ids = new ArrayList<>();
    private ArrayList<String> attributes_names = new ArrayList<>();
    private ArrayList<Integer> employees_ids = new ArrayList<>();
    private ArrayList<String> employees_names = new ArrayList<>();
    private ArrayList<String> licenses = new ArrayList<>();
    private ArrayList<String> birthdays = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<Integer> attributesIdsOfSpecificEmployee = new ArrayList<>();
    private ArrayList<Integer> employeesIdsWithSpecificAttribute = new ArrayList<>();


    /**
     * Constructor of DbController that opens the CITE database or creates it if it doesn't exists
     */
    public DbController(Context context) {
        sql = context.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        createTables();
    }

    /**
     * Find and return all the attributes of a specific Employee
     */
    public ArrayList<String> getAttributesNamesOfSpecificEmployee(String employee_name) {
        readFromEmployeeAttributeTableToFindAttributes("*", employee_name);
        ArrayList<Integer> attr_ids = getAttributes_ids();
        ArrayList<String> attr_names = getAttributes_names();
        ArrayList<String> attributesNamesOfSpecificEmployee = new ArrayList<>();
        for (int i = 0; i < attributesIdsOfSpecificEmployee.size(); i++) {
            int position = attr_ids.indexOf(attributesIdsOfSpecificEmployee.get(i));
            attributesNamesOfSpecificEmployee.add(attr_names.get(position));
        }
        return attributesNamesOfSpecificEmployee;
    }

    /**
     * Find and return the employees with a certain Attribute
     */
    public ArrayList<String> getEmployeesNamesWithSpecificAttribute(String attribute_name) {
        readFromEmployeeAttributeTableToFindEmployees("*", attribute_name);
        ArrayList<Integer> emp_ids = getEmployees_ids();
        ArrayList<String> emp_names = getEmployees_names();
        ArrayList<String> employeesNamesWithSpecificAttribute = new ArrayList<>();
        for (int i = 0; i < employeesIdsWithSpecificAttribute.size(); i++) {
            int position = emp_ids.indexOf(employeesIdsWithSpecificAttribute.get(i));
            if (position > -1 && position < emp_names.size()) {
                employeesNamesWithSpecificAttribute.add(emp_names.get(position));
            }
        }
        return employeesNamesWithSpecificAttribute;
    }

    /**
     * Get a list with the attribute's ids
     */
    public ArrayList<Integer> getAttributes_ids() {
        readFromAttributesTable("*");
        return attributes_ids;
    }

    /**
     * Get a list with the attribute's names
     */
    public ArrayList<String> getAttributes_names() {
        readFromAttributesTable("*");
        return attributes_names;
    }

    /**
     * Get a list with the employee's ids
     */
    public ArrayList<Integer> getEmployees_ids() {
        readFromEmployeesTable("*");
        return employees_ids;
    }

    /**
     * Get a list with the employee's names
     */
    public ArrayList<String> getEmployees_names() {
        readFromEmployeesTable("*");
        return employees_names;
    }

    /**
     * Get the home address of a certain employee
     */
    public String getHomeAddress(String emp_name) {
        readFromEmployeesTable("*");
        int position = employees_names.indexOf(emp_name);
        return addresses.get(position);
    }

    /**
     * Insert data to Employees Table
     */
    public void insertToEmployeesTable(String employee_name, String employee_license, String employee_birthday, String employee_address) {
        String query = "INSERT INTO Employees(name, license, birthday, address) VALUES('" + employee_name + "','" + employee_license + "','" + employee_birthday + "','" + employee_address + "');";
        sql.execSQL(query);
    }

    /**
     * Insert data to Attributes Table
     */
    public void insertToAttributesTable(String attribute_name) {
        String query = "INSERT INTO Attributes(name) VALUES('" + attribute_name + "');";
        sql.execSQL(query);
    }

    /**
     * Insert data to EmployeeAttribute Table
     */
    public void insertToEmployeeAttributeTable(String attribute_name, String employee_name) {
        readFromAttributesTable("*");
        readFromEmployeesTable("*");
        int attr_position = attributes_names.indexOf(attribute_name);
        int empl_position = employees_names.indexOf(employee_name);
        String query = "INSERT INTO EmployeeAttribute(attribute_id, employee_id) VALUES('" + attributes_ids.get(attr_position) + "', '" + employees_ids.get(empl_position) + "');";
        sql.execSQL(query);
    }

    /**
     * Replace an entry with a new one at EmployeeAttribute table
     * It recreates a new connection between an employee and an attribute
     */
    public void replaceToEmployeeAttributeTable(String attribute_name, String employee_name, boolean employee_already_exist) {
        //If the attribute-employee combination is an existing entry then delete it and create a new one
        if (employee_already_exist) {
            removeFromEmployeeAttributeTable(employee_name);
        }
        readFromAttributesTable("*");
        readFromEmployeesTable("*");
        insertToEmployeeAttributeTable(attribute_name, employee_name);
    }

    /**
     * Read the EmployeeAttribute table and finds all the attributes that a certain employee has
     */
    public void readFromEmployeeAttributeTableToFindAttributes(String column_name, String employee_name) {
        readFromEmployeesTable("*");
        int emp_id = employees_ids.get(employees_names.indexOf(employee_name));
        String query = "SELECT " + column_name + " FROM  EmployeeAttribute WHERE employee_id LIKE '" + emp_id + "';";
        Cursor c = sql.rawQuery(query, null);
        int attr_id = -1;
        if (c.moveToFirst()) {
            attributesIdsOfSpecificEmployee.clear();
            do {
                attr_id = c.getInt(0);
                attributesIdsOfSpecificEmployee.add(attr_id);

            } while (c.moveToNext());
        }
        c.close();
    }

    /**
     * Read the EmployeeAttribute table and finds all the employees that have a certain attribute
     */
    public void readFromEmployeeAttributeTableToFindEmployees(String column_name, String attribute_name) {
        readFromAttributesTable("*");
        int attr_id = attributes_ids.get(attributes_names.indexOf(attribute_name));
        String query = "SELECT " + column_name + " FROM  EmployeeAttribute WHERE attribute_id LIKE '" + attr_id + "';";
        Cursor c = sql.rawQuery(query, null);
        int emp_id = -1;
        if (c.moveToFirst()) {
            employeesIdsWithSpecificAttribute.clear();
            do {
                emp_id = c.getInt(1);
                employeesIdsWithSpecificAttribute.add(emp_id);

            } while (c.moveToNext());
        }
        c.close();
    }

    /**
     * Removes a specific Employee and all of his Attributes from the EmployeeAttribute table
     */
    public void removeFromEmployeeAttributeTable(String employee_name) {
        //Update the lists
        readFromEmployeesTable("*");
        readFromAttributesTable("*");
        int empl_position = employees_names.indexOf(employee_name);
        String query = "DELETE FROM EmployeeAttribute WHERE employee_id LIKE '" + employees_ids.get(empl_position) + "' ;";
        sql.execSQL(query);
    }

    /**
     * Drops all the tables of the CITE database
     */
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

    /**
     * Creates all the necessary tables of the CITE database
     */
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

    /**
     * Read the Attributes table and update the attribute_ids, attributes_names lists
     */
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

    /**
     * Read the Attributes table and update the employees_ids, employees_names, licenses, birthdays, addresses lists
     */
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

    /**
     * Check if the Attribute table contains a certain attribute
     */
    public boolean containIntoAttributeTable(String attribute_name) {
        readFromAttributesTable("*");
        return attributes_names.contains(attribute_name);
    }

    /**
     * Finds and replaces an attribute in Attributes table
     */
    public void replaceAttribute(String oldName, String newName) {
        removeAttribute(oldName);
        insertToAttributesTable(newName);
        readFromAttributesTable("*");
    }

    /**
     * Finds and replaces an employee in Employees table
     */
    public void replaceEmployee(String old_employee_name,
                                String new_employee_name, String new_employee_license, String new_employee_birthday, String new_employee_address) {

        removeEmployee(old_employee_name);
        insertToEmployeesTable(new_employee_name, new_employee_license, new_employee_birthday, new_employee_address);
        readFromEmployeesTable("*");
    }

    /**
     * Remove a certain attribute from Attributes table
     */
    public void removeAttribute(String attribute_name) {
        String query = "DELETE FROM Attributes WHERE name LIKE '" + attribute_name + "';";
        sql.execSQL(query);
        readFromAttributesTable("*");
    }

    /**
     * Remove a certain employee from Employees table
     */
    public void removeEmployee(String employee_name) {
        String query = "DELETE FROM Employees WHERE name LIKE '" + employee_name + "';";
        sql.execSQL(query);
        readFromEmployeesTable("*");
    }

}
