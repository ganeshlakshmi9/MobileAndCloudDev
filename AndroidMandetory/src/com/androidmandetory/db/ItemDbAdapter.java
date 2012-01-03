package com.androidmandetory.db;

import com.androidmandetory.util.MySoapHandler;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ItemDbAdapter {

	public static final String KEY_ROWID = "_id";

	public static final String KEY_ITEM_NAME = "name";
	public static final String KEY_ITEM_PRICE_PR_ITEM = "price";
	public static final String KEY_ITEM_QUANTITY = "quantity";
	public static final String KEY_ITEM_CHECKED = "checked";
	public static final String KEY_ITEM_TOTAL = "total";

	private static final String DATABASE_NAME = "shoppingList_data";
	private static final String DATABASE_TABLE_ITEM = "items";
	private static final int DATABASE_VERSION = 2;

	private static final String SHARED_PREF = "sharedPrefSS";

	private static final String TAG = "ItemDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table items (_id integer primary key autoincrement, "
			+ "name text NOT NULL, price DECIMAL NOT NULL, quantity INTEGER NOT NULL, checked BIT NOT NULL, total DECIMAL NOT NULL);";

	private final Context mCtx;

	private String currency;
	private String currencyLabel;

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
	 * Inserts a new item into the database. The checked bool is automatically
	 * set to false since it's a new registration.
	 * 
	 * @param name
	 * @param price
	 * @param quantity
	 * @return id
	 */
	public long createItem(String name, double price, int quantity) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ITEM_NAME, name);
		initialValues.put(KEY_ITEM_PRICE_PR_ITEM, price);
		initialValues.put(KEY_ITEM_QUANTITY, quantity);
		initialValues.put(KEY_ITEM_CHECKED, "0");
		Double res = price * quantity;
		initialValues.put(KEY_ITEM_TOTAL, res);

		return mDb.insert(DATABASE_TABLE_ITEM, null, initialValues);
	}

	/**
	 * Deletes an item based on given id.
	 * 
	 * @param id
	 * @return if successful or not
	 */
	public boolean deleteItem(long id) {
		return mDb.delete(DATABASE_TABLE_ITEM, KEY_ROWID + "=" + id, null) > 0;
	}

	/**
	 * Deletes all checked items.
	 * 
	 * @return if successful or not
	 */
	public boolean deleteCheckedItems() {
		return mDb
				.delete(DATABASE_TABLE_ITEM, KEY_ITEM_CHECKED + "=" + 1, null) > 0;
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
	public boolean updateItem(long rowId, String name, int quantity,
			double price) {
		ContentValues values = new ContentValues();
		values.put(KEY_ITEM_NAME, name);
		values.put(KEY_ITEM_PRICE_PR_ITEM, price);
		values.put(KEY_ITEM_QUANTITY, quantity);
		values.put(KEY_ITEM_TOTAL, price * quantity);

		return mDb.update(DATABASE_TABLE_ITEM, values, KEY_ROWID + "=" + rowId,
				null) > 0;
	}

	/**
	 * Update method specially designed for the update of checked items.
	 * 
	 * @param rowId
	 * @param checked
	 * @return
	 */
	public boolean updateItemChecked(long rowId, boolean checked) {
		ContentValues args = new ContentValues();
		args.put(KEY_ITEM_CHECKED, checked);

		return mDb.update(DATABASE_TABLE_ITEM, args, KEY_ROWID + "=" + rowId,
				null) > 0;
	}

	public void totalSum() {
		mDb.execSQL("select sum");
	}

	/**
	 * Return a Cursor over the list of all items in the database
	 * 
	 * @return Cursor over all items
	 */
	public Cursor fetchAllItems() {
		return mDb.query(DATABASE_TABLE_ITEM, new String[] { KEY_ROWID,
				KEY_ITEM_NAME, KEY_ITEM_PRICE_PR_ITEM, KEY_ITEM_QUANTITY,
				KEY_ITEM_TOTAL, KEY_ITEM_CHECKED }, null, null, null, null,
				null);
	}

	/**
	 * Return a Cursor with a specific item based on the rowId param
	 * 
	 * @param rowId
	 * @return
	 */
	public Cursor fetchItem(long rowId) {
		Cursor cursor = mDb.query(true, DATABASE_TABLE_ITEM, new String[] {
				KEY_ROWID, KEY_ITEM_NAME, KEY_ITEM_PRICE_PR_ITEM,
				KEY_ITEM_QUANTITY, KEY_ITEM_CHECKED }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	/**
	 * Retrieves the total amount for all items. 
	 * 
	 * @return total price
	 */
	public Double getTotalPrice() {
		Cursor cursor = mDb.rawQuery("select SUM(total) from "
				+ DATABASE_TABLE_ITEM, null);
		cursor.moveToFirst();
		double price = cursor.getDouble(0);
		cursor.close();
		return price;
	}

	/**
	 * Gets the current currency. In case of a null value, the method
	 * will try to retrieve from the shared preferences. 
	 * 
	 * @param context
	 * @return currency
	 */
	public String getCurrency(Context context) {
		if (currency == null) {
			SharedPreferences mgr = context.getSharedPreferences(ItemDbAdapter.SHARED_PREF, 0);
			currency = mgr.getString("currency", "DKK");
		}
		return currency;
	}

	/**
	 * Gets the current currency label. In case of a null value, the method
	 * will try to retrieve from the shared preferences. 
	 * 
	 * @param context
	 * @return currency label
	 */
	public String getCurrencyLabel(Context context) {
		if (currencyLabel == null) {
			SharedPreferences mgr = context.getSharedPreferences(ItemDbAdapter.SHARED_PREF, 0);
			currencyLabel = mgr.getString("currencylabel", "kr");
		}
		return currencyLabel;
	}

	/**
	 * Sets the currency and currency label information in both the shared preferences
	 * and the local db values. 
	 * 
	 * @param currency
	 * @param currencyLabel
	 * @param context
	 */
	public void setCurrency(String currency,
			String currencyLabel, Context context) {
		if (getCurrency(context).equals(currency))
			return;
		double conversionrate = MySoapHandler.convertCurrency(getCurrency(context),
				currency);

		if (Double.compare(conversionrate, -1) == 0)
			throw new IllegalStateException("SOAP failed.");

		updateCurrency(conversionrate);
		SharedPreferences prefs = context.getSharedPreferences(ItemDbAdapter.SHARED_PREF, 0);

		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("currency", currency);
		edit.putString("currencylabel", currencyLabel);
		edit.commit();
		
		this.currency = currency;
		this.currencyLabel = currencyLabel;
	}

	/**
	 * Updates all prices and totals in the db to a new currency.
	 * 
	 * @param conversionrate
	 */
	private void updateCurrency(double conversionrate) {
		SQLiteDatabase handle = mDbHelper.getWritableDatabase();
		
		SQLiteStatement convertStm = handle.compileStatement("UPDATE "
				+ DATABASE_TABLE_ITEM + " SET price = price * ?;");
		convertStm.bindDouble(1, conversionrate);
		convertStm.execute();
		
		SQLiteStatement recalTotalStm = handle.compileStatement("UPDATE "
				+ DATABASE_TABLE_ITEM + " SET total = price * quantity;");
		recalTotalStm.execute();
		
		convertStm.close();
		recalTotalStm.close();
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
