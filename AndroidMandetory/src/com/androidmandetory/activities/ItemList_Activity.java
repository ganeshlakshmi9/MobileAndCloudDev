package com.androidmandetory.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmandetory.R;
import com.androidmandetory.db.ItemDbAdapter;
import com.androidmandetory.util.ShoppingListCursorAdapter;

public class ItemList_Activity extends ListActivity {

	private ItemDbAdapter mDbHelper;

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_list_layout);
		mDbHelper = new ItemDbAdapter(this);
		mDbHelper.open();
		fillData();
		setupActionBar();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_layout, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(R.string.context_menu_title);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_layout, menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_insert_item:
			createItem();
			return true;
		case R.id.menu_currency:
			createCurrencyAlertDialog();
			return true;
		case R.id.menu_clear_checked:
			mDbHelper.deleteCheckedItems();
			fillData();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.context_menu_delete:
			mDbHelper.deleteItem(info.id);
			fillData();
			return true;
		case R.id.context_menu_edit:
			Intent i = new Intent(this, EditItem_Activity.class);
			i.putExtra(ItemDbAdapter.KEY_ROWID, info.id);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		CheckBox checkbox = (CheckBox) v.findViewById(R.id.list_item_checkbox);
		checkbox.setChecked(!checkbox.isChecked());
		mDbHelper.updateItemChecked(id, checkbox.isChecked());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			Toast.makeText(getApplicationContext(), "Item Saved",
					Toast.LENGTH_SHORT).show();
		}
		fillData();
	}

	/**
	 * Creates and fires a new activity, with the purpose of creating a new list
	 * item.
	 */
	private void createItem() {
		Intent i = new Intent(this, EditItem_Activity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	/**
	 * Sets up the action bar image buttons.
	 */
	private void setupActionBar() {
		ImageButton mNewButton = (ImageButton) findViewById(R.id.actionbar_new);
		ImageButton mCurrencyButton = (ImageButton) findViewById(R.id.actionbar_currency);

		mNewButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				createItem();
			}
		});

		mCurrencyButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				createCurrencyAlertDialog();
			}
		});
	}

	/**
	 * Creates an alert dialog, which is used to change the currency. 
	 */
	private void createCurrencyAlertDialog() {
		String[] currencies = { "Danish Crowns (kr)", "US Dollars ($)",
				"Euro (€)" };

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ItemList_Activity.this);
		dialog.setTitle("Select currency:");
		dialog.setItems(currencies, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, final int which) {
				new Thread() {
					public void run() {
						switch (which) {
						case 0: // DKK
							mDbHelper.setCurrency(
									getString(R.string.currency_dkk),
									getString(R.string.currency_label_dkk),
									ItemList_Activity.this);
							break;
						case 1: // USD
							mDbHelper.setCurrency(
									getString(R.string.currency_usd),
									getString(R.string.currency_label_usd),
									ItemList_Activity.this);
							break;
						case 2: // EUR
							mDbHelper.setCurrency(
									getString(R.string.currency_eur),
									getString(R.string.currency_label_eur),
									ItemList_Activity.this);
							break;
						}
						runOnUiThread(new Runnable() {
							public void run() {
								fillData();
							}
						});
					};
				}.start();
			}
		});
		dialog.show();
	}

	/**
	 * Fills the listview with data.
	 */
	private void fillData() {
		Cursor itemsCursor = mDbHelper.fetchAllItems();
		startManagingCursor(itemsCursor);
		String currencyLabel = mDbHelper
				.getCurrencyLabel(ItemList_Activity.this);
		ShoppingListCursorAdapter items = new ShoppingListCursorAdapter(this,
				itemsCursor, currencyLabel);
		TextView totalText = (TextView) findViewById(R.id.footer_cost_text);
		totalText.setText(getString(R.string.footer_total_cost) + " "
				+ String.format("%.2f", mDbHelper.getTotalPrice())
				+ currencyLabel);
		setListAdapter(items);
	}
}