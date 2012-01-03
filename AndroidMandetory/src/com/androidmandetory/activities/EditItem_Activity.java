package com.androidmandetory.activities;

import com.androidmandetory.R;
import com.androidmandetory.db.ItemDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItem_Activity extends Activity{
	
	private EditText mItemName;
	private EditText mItemQuantity;
	private EditText mItemPrice;
	
	private Button mAddButton;
	
	private Long mRowId;
	private ItemDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbHelper = new ItemDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.edit_item_layout);
		
		mItemName = (EditText) findViewById(R.id.editText_name);
		mItemQuantity = (EditText) findViewById(R.id.editText_quantity);
		mItemPrice = (EditText) findViewById(R.id.editText_price);
		
		mAddButton = (Button) findViewById(R.id.add_button);
		
		mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(ItemDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(ItemDbAdapter.KEY_ROWID)
                                    : null;
        }
		
		populateFields();

		mAddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	setResult(RESULT_OK);
            	finish();
            }
        });
	}
	
    private void populateFields() {
        if (mRowId != null) {
            Cursor item = mDbHelper.fetchItem(mRowId);
            startManagingCursor(item);
            mItemName.setText(item.getString(
                    item.getColumnIndexOrThrow(ItemDbAdapter.KEY_ITEM_NAME)));
            
            mItemQuantity.setText(item.getString(
                    item.getColumnIndexOrThrow(ItemDbAdapter.KEY_ITEM_QUANTITY)));
            
            mItemPrice.setText(item.getString(
                    item.getColumnIndexOrThrow(ItemDbAdapter.KEY_ITEM_PRICE_PR_ITEM)));
        }
        else{
            mItemQuantity.setText("0");
            mItemPrice.setText("0.00");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ItemDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
    	String name 	= mItemName.getText().toString();
        int quantity 	= Integer.parseInt(mItemQuantity.getText().toString());
        double price	= Double.parseDouble(mItemPrice.getText().toString());

        if (mRowId == null) {
        	if(!name.equals("")){
        		long id = mDbHelper.createItem(name, price, quantity);
                if (id > 0) {
                    mRowId = id;
                }
        	}
        } else {
            mDbHelper.updateItem(mRowId, name, quantity, price);
        }
    }
}
