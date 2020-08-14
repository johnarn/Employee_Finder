package com.example.employeefinder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DbControllerTests {

    // Context of the app under test.
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private DbController dbController;

    @Before
    public void setUp() {
        dbController = new DbController(appContext);
        dbController.dropTables();
        dbController.createTables();
    }


    @Test
    public void checkDbControllerInitialization() {
        assertNotNull(dbController);
    }

    @Test
    public void testCreateTablesShouldReturnTrue() {
        ArrayList<String> table_names = new ArrayList<>();
        dbController.createTables();
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                table_names.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        assertEquals(table_names.get(0), "Attributes");
        assertEquals(table_names.get(1), "Employees");
        assertEquals(table_names.get(2), "EmployeeAttribute");
    }

    @Test
    public void testDropTablesShouldReturnTrue() {
        ArrayList<String> table_names = new ArrayList<>();
        dbController.dropTables();
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                table_names.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        assertEquals(1, table_names.size());
    }

    @Test
    public void testDropTablesShouldReturnFalse() {
        ArrayList<String> table_names = new ArrayList<>();
        dbController.dropTables();
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                table_names.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        assertFalse(table_names.size() > 1);
    }

    @Test
    public void testInsertToAttributesTableShouldReturnTrue() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToAttributesTable("organized");
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM Attributes WHERE name='organized'", null);
        if (c.moveToFirst()) {
            assertEquals(c.getString(0), "organized");
        }
    }

    @Test
    public void testInsertToAttributesTableShouldReturnFalse() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToAttributesTable("fast");
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM Attributes WHERE name='organized'", null);
        if (c.moveToFirst()) {
            assertNotEquals(c.getString(0), "organized");
        }
    }

    @Test
    public void testInsertToEmployeesTableShouldReturnTrue() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM Employees WHERE name='John'", null);
        if (c.moveToFirst()) {
            assertEquals(c.getString(0), "John");
        }
    }

    @Test
    public void testInsertToEmployeesTableShouldReturnFalse() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        Cursor c = sqLiteDatabase.rawQuery("SELECT name FROM Employees WHERE name='Jake'", null);
        if (c.moveToFirst()) {
            assertNotEquals(c.getString(0), "John");
        }
    }

    @Test
    public void testInsertToEmployeeAttributeTableShouldReturnTrue() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        Cursor c = sqLiteDatabase.rawQuery("SELECT attribute_id FROM EmployeeAttribute WHERE attribute_id='1'", null);
        if (c.moveToFirst()) {
            assertEquals(c.getString(0), "1");
        }
    }

    @Test
    public void testInsertToEmployeeAttributeTableShouldReturnFalse() {
        SQLiteDatabase sqLiteDatabase = appContext.openOrCreateDatabase("CITE.db", MODE_PRIVATE, null);
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        Cursor c = sqLiteDatabase.rawQuery("SELECT attribute_id FROM EmployeeAttribute WHERE attribute_id='1'", null);
        if (c.moveToFirst()) {
            assertNotEquals(c.getString(0), "2");
        }
    }

    @Test
    public void testGetAttributesNamesOfSpecificEmployeeShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "John");

        ArrayList<String> attributesOfEmployee = new ArrayList<>();
        attributesOfEmployee.add("organized");
        attributesOfEmployee.add("fast");

        assertEquals(attributesOfEmployee, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }

    @Test
    public void testGetAttributesNamesOfSpecificEmployeeShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "Jake");

        ArrayList<String> attributesOfEmployee = new ArrayList<>();
        attributesOfEmployee.add("organized");
        attributesOfEmployee.add("fast");

        assertNotEquals(attributesOfEmployee, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }

    @Test
    public void testGetEmployeesNamesWithSpecificAttributeShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "John");
        dbController.insertToEmployeeAttributeTable("organized", "Jake");

        ArrayList<String> testEmployeesWithSpecificAttribute = new ArrayList<>();
        testEmployeesWithSpecificAttribute.add("John");
        testEmployeesWithSpecificAttribute.add("Jake");

        assertEquals(testEmployeesWithSpecificAttribute, dbController.getEmployeesNamesWithSpecificAttribute("organized"));
    }

    @Test
    public void testGetEmployeesNamesWithSpecificAttributeShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "John");
        dbController.insertToEmployeeAttributeTable("organized", "Jake");

        ArrayList<String> testEmployeesWithSpecificAttribute = new ArrayList<>();
        testEmployeesWithSpecificAttribute.add("John");
        testEmployeesWithSpecificAttribute.add("Jake");

        assertNotEquals(testEmployeesWithSpecificAttribute, dbController.getEmployeesNamesWithSpecificAttribute("fast"));
    }

    @Test
    public void testGetAttributesIdsShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        ArrayList<Integer> testAttributesIds = new ArrayList<>();
        testAttributesIds.add(1);
        testAttributesIds.add(2);

        assertEquals(testAttributesIds, dbController.getAttributes_ids());
    }

    @Test
    public void testGetAttributesIdsShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        ArrayList<Integer> testAttributesIds = new ArrayList<>();
        testAttributesIds.add(3);
        testAttributesIds.add(4);

        assertNotEquals(testAttributesIds, dbController.getAttributes_ids());
    }

    @Test
    public void testGetEmployeesIdsShouldReturnTrue() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<Integer> testEmployeesIds = new ArrayList<>();
        testEmployeesIds.add(1);
        testEmployeesIds.add(2);

        assertEquals(testEmployeesIds, dbController.getEmployees_ids());
    }

    @Test
    public void testGetEmployeesIdsShouldReturnFalse() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<Integer> testEmployeesIds = new ArrayList<>();
        testEmployeesIds.add(3);
        testEmployeesIds.add(4);

        assertNotEquals(testEmployeesIds, dbController.getEmployees_ids());
    }

    @Test
    public void testGetEmployeesNamesShouldReturnTrue() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<String> testEmployeesNames = new ArrayList<>();
        testEmployeesNames.add("John");
        testEmployeesNames.add("Jake");

        assertEquals(testEmployeesNames, dbController.getEmployees_names());
    }

    @Test
    public void testGetEmployeesNamesShouldReturnFalse() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<String> testEmployeesNames = new ArrayList<>();
        testEmployeesNames.add("John");
        testEmployeesNames.add("Jonathan");


        assertNotEquals(testEmployeesNames, dbController.getEmployees_ids());
    }

    @Test
    public void testGetEmployeeHomeAddressShouldReturnTrue() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Kallirois 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");


        assertEquals("Kallirois 8, Athens", dbController.getHomeAddress("John"));
    }

    @Test
    public void testGetEmployeeHomeAddressShouldReturnFalse() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Kallirois 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");


        assertNotEquals("Sofokleous 8, Athens", dbController.getHomeAddress("John"));
    }

    @Test
    public void testReplaceToEmployeeAttributeTableShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");

        dbController.replaceToEmployeeAttributeTable("fast", "John", true);

        ArrayList<String> attribute = new ArrayList<>();
        attribute.add("fast");

        assertEquals(attribute, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }

    @Test
    public void testReplaceToEmployeeAttributeTableShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");

        dbController.replaceToEmployeeAttributeTable("fast", "John", true);

        ArrayList<String> attribute = new ArrayList<>();
        attribute.add("organized");

        assertNotEquals(attribute, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }

    @Test
    public void testRemoveFromEmployeeAttributeTableShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "John");

        dbController.removeFromEmployeeAttributeTable("John");

        ArrayList<String> attribute = new ArrayList<>();

        assertEquals(attribute, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }

    @Test
    public void testRemoveFromEmployeeAttributeTableShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.insertToEmployeeAttributeTable("organized", "John");
        dbController.insertToEmployeeAttributeTable("fast", "John");

        dbController.removeFromEmployeeAttributeTable("John");

        ArrayList<String> attribute = new ArrayList<>();
        attribute.add("organized");
        attribute.add("fast");

        assertNotEquals(attribute, dbController.getAttributesNamesOfSpecificEmployee("John"));
    }


    @Test
    public void testContainIntoAttributeTableShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        assertTrue(dbController.containIntoAttributeTable("organized"));
    }

    @Test
    public void testContainIntoAttributeTableShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        assertFalse(dbController.containIntoAttributeTable("slow"));
    }

    @Test
    public void testReplaceAttributeShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.replaceAttribute("fast", "slow");

        ArrayList<String> attributes_names = new ArrayList<>();
        attributes_names.add("organized");
        attributes_names.add("slow");

        assertEquals(attributes_names, dbController.getAttributes_names());
    }

    @Test
    public void testReplaceAttributeShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.replaceAttribute("fast", "slow");

        ArrayList<String> attributes_names = new ArrayList<>();
        attributes_names.add("organized");
        attributes_names.add("fast");

        assertNotEquals(attributes_names, dbController.getAttributes_names());
    }

    @Test
    public void testReplaceEmployeeShouldReturnTrue() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.replaceEmployee("Jake", "Jacob", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<String> employee_names = new ArrayList<>();
        employee_names.add("John");
        employee_names.add("Jacob");

        assertEquals(employee_names, dbController.getEmployees_names());
    }

    @Test
    public void testReplaceEmployeeShouldReturnFalse() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.replaceEmployee("Jake", "Jacob", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        ArrayList<String> employee_names = new ArrayList<>();
        employee_names.add("John");
        employee_names.add("Jake");

        assertNotEquals(employee_names, dbController.getEmployees_names());
    }

    @Test
    public void testRemoveAttributeShouldReturnTrue() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.removeAttribute("fast");

        ArrayList<String> attributes_names = new ArrayList<>();
        attributes_names.add("organized");

        assertEquals(attributes_names, dbController.getAttributes_names());
    }

    @Test
    public void testRemoveAttributeShouldReturnFalse() {
        dbController.insertToAttributesTable("organized");
        dbController.insertToAttributesTable("fast");

        dbController.removeAttribute("fast");

        ArrayList<String> attributes_names = new ArrayList<>();
        attributes_names.add("organized");
        attributes_names.add("fast");

        assertNotEquals(attributes_names, dbController.getAttributes_names());
    }

    @Test
    public void testRemoveEmployeeShouldReturnTrue() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.removeEmployee("Jake");

        ArrayList<String> employee_names = new ArrayList<>();
        employee_names.add("John");

        assertEquals(employee_names, dbController.getEmployees_names());
    }

    @Test
    public void testRemoveEmployeeShouldReturnFalse() {
        dbController.insertToEmployeesTable("John", "Yes", "10/02/2014", "Sofokleous 8, Athens");
        dbController.insertToEmployeesTable("Jake", "Yes", "10/02/2014", "Sofokleous 8, Athens");

        dbController.removeEmployee("Jake");

        ArrayList<String> employee_names = new ArrayList<>();
        employee_names.add("John");
        employee_names.add("Jake");

        assertNotEquals(employee_names, dbController.getEmployees_names());
    }


}