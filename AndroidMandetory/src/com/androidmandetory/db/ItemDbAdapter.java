package com.androidmandetory.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDbAdapter {
	
	public static final String KEY_ROWID = "_id";
	
	public static final String KEY_ITEM_NAME 			= "name";
    public static final String KEY_ITEM_PRICE_PR_ITEM 	= "price";
    public static final String KEY_ITEM_QUANTITY 		= "quantity";
    public static final String KEY_ITEM_CHECKED 		= "checked";
    
    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table notes (_id integer primary key autoincrement, "
        + "name text not null, price text not null, quantity text not null, checked integer not null);";

    private static final String DATABASE_NAME = "shopping_list_data";
    private static final String DATABASE_TABLE_ITEM = "items";
    private static final int DATABASE_VERSION = 2;
    
    private final Context mCtx;
    
    public ItemDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public ItemDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Inserts a new item into the database. The checked bool is automatically set to 
     * false since it's a new registration.
     * 
     * @param name
     * @param price
     * @param quantity
     * @return id
     */
    public long createItem(String name, String price, String quantity) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ITEM_NAME, name);
        initialValues.put(KEY_ITEM_PRICE_PR_ITEM, price);
        initialValues.put(KEY_ITEM_QUANTITY, quantity);
        initialValues.put(KEY_ITEM_CHECKED, false);

        return mDb.insert(DATABASE_TABLE_ITEM, null, initialValues);
    }
    
    /**
     * Deletes an item based on given id.
     * 
     * @param rowId
     * @return if successful or not
     */
    public boolean deleteItem(int rowId){
    	return mDb.delete(DATABASE_TABLE_ITEM, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Updates an item based on id. 
     * 
     * @param rowId
     * @param name
     * @param quantity
     * @param price
     * @param checked
     * @return if successful or not
     */
    public boolean updateItem(long rowId, String name, String quantity, String price, int checked) {
        ContentValues args = new ContentValues();
        args.put(KEY_ITEM_NAME, name);
        args.put(KEY_ITEM_PRICE_PR_ITEM, price);
        args.put(KEY_ITEM_QUANTITY, quantity);
        args.put(KEY_ITEM_CHECKED, checked);

        return mDb.update(DATABASE_TABLE_ITEM, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

}
