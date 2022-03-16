package com.example.nutritionfactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.widget.Toast;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    // Database Information
    private static final String DATABASE_NAME = "ProductDB.db";
    private static final int DATABASE_VERSION = 1;

    // Product Table Information
    private static final String TABLE_NAME = "products";

    private static final String COLUMN_CODE = "barcode";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GRADE = "grade";
    private static final String COLUMN_NOVAGROUP = "nova_group";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_NUTRIENTS = "nutrients";
    private static final String COLUMN_IMAGE ="images";

    // List table Information
    private static final String TABLE_NAME2 = "lists";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LIST_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";

    //Product to list table Information
    private static final String TABLE_NAME3 = "products_to_lists";

    private static final String COLUMN_PRODUCT_CODE ="barcode";
    private static final String COLUMN_LIST_ID = "id";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);// create DB
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating table of products
        String productTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_CODE + " TEXT PRIMARY KEY, " +
                        COLUMN_NAME + " TEXT, " + COLUMN_GRADE + " TEXT, " + COLUMN_NOVAGROUP + " TEXT,"  +
                        COLUMN_INGREDIENTS + " TEXT, " + COLUMN_NUTRIENTS + " TEXT, " + COLUMN_IMAGE + " TEXT );";

        // Creating table of lists
        String listTable = "CREATE TABLE " + TABLE_NAME2 + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LIST_NAME +  " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT );";

        //Creating table of product to list
        String productListTable = "CREATE TABLE " + TABLE_NAME3 + " (" + COLUMN_PRODUCT_CODE + " TEXT , " + COLUMN_LIST_ID + " INTEGER, " +
                                    "PRIMARY KEY ( " +  COLUMN_PRODUCT_CODE + ", " + COLUMN_LIST_ID + ") " + ");";

        db.execSQL(productTable);
        db.execSQL(listTable);
        db.execSQL(productListTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);

        onCreate(db);
    }

    // Inserting item data to the products table
    void addItem( String name, String grade,String nova,String barcode, String ingredients, String nutrients,String imageURL){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues DataItems = new ContentValues();
        // inputting data to each field
        DataItems.put(COLUMN_NAME,name);
        DataItems.put(COLUMN_GRADE,grade);
        DataItems.put(COLUMN_NOVAGROUP,nova);
        DataItems.put(COLUMN_CODE,barcode);
        DataItems.put(COLUMN_INGREDIENTS,ingredients);
        DataItems.put(COLUMN_NUTRIENTS,nutrients);
        DataItems.put(COLUMN_IMAGE , imageURL);


        db.insert(TABLE_NAME, null, DataItems);

    }

    // Inserting list data to the lists table
    void addList( String listName, String description){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues DataLists = new ContentValues();
        // inputting the lists
        DataLists.put(COLUMN_LIST_NAME,listName);
        DataLists.put(COLUMN_DESCRIPTION,description);

        // if data fail to enter to the table output message
        long pass2 = db.insert(TABLE_NAME2, null,DataLists);
        if ( pass2 == -1){
            Toast.makeText(context,"Failed Entry", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Added", Toast.LENGTH_SHORT).show();
        }
    }

    //Inserting into the product to list table
    void add_products_list(String listId, String productId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues DataListProducts =new ContentValues();
        // inputting the field data
        DataListProducts.put(COLUMN_LIST_ID,listId);
        DataListProducts.put(COLUMN_PRODUCT_CODE, productId);

        db.insert(TABLE_NAME3,null,DataListProducts);

    }

    //Delete Method

    void deleteProduct(String row_name){
        SQLiteDatabase db = this.getReadableDatabase();
        long result = db.delete(TABLE_NAME, "name=?", new String[]{row_name});
        if ( result == -1){
            Toast.makeText(context,"Failed to Delete", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete List

    void deleteList(String row_id){
        SQLiteDatabase db = this.getReadableDatabase();
        long result = db.delete(TABLE_NAME2, "id=?", new String[]{row_id});
        if ( result == -1){
            Toast.makeText(context,"Failed to Delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteProductToList(String list_id){
        SQLiteDatabase db = this.getReadableDatabase();
        long result = db.delete(TABLE_NAME3, "id=?", new String[]{list_id});
        if ( result == -1){
            Toast.makeText(context,"Failed to Delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    // return cursor object by reading all data from db
    Cursor readAllProductData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    Cursor readAllListData(){
        String query2 = "SELECT * FROM " + TABLE_NAME2; // select all data from the table
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor2 = null;
        if(db != null){ // have data check
            cursor2 = db.rawQuery(query2, null); //returns the data in the table
        }
        return cursor2;

    }

    // Getting the list id by list name
    public Cursor getListIDFromListName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+ TABLE_NAME2 +" where name = ?", new String[]{name});
        return cursor;
    }

    // Getting the barcodes in the specific list relating to the list id
    public Cursor getBarcodeFromListID(String listID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("Select * from "+ TABLE_NAME3 +" where id = ?", new String[]{listID});
        return cursor1;
    }

    // Getting the product details relating to the specific barcode
    public Cursor getProductFromBarcode(String barcode)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+ TABLE_NAME +" where barcode = ?", new String[]{barcode});
        return cursor;
    }

}
