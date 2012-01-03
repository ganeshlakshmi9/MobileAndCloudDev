package com.androidmandetory.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.androidmandetory.R;

public class ShoppingListCursorAdapter extends CursorAdapter {
	
	private String label;

	public ShoppingListCursorAdapter(Context context, Cursor c, String label) {
		super(context, c);
		this.label = label;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtName = (TextView) view
				.findViewById(R.id.list_item_textView_name);
		
		TextView txtPrice = (TextView) view
				.findViewById(R.id.list_item_textView_price);
		
		TextView txtQuantity = (TextView) view
				.findViewById(R.id.list_item_textView_quantity);
		
		TextView txtTotal = (TextView) view
				.findViewById(R.id.list_item_textView_total_price);
		
		CheckBox checkbox = (CheckBox) view
				.findViewById(R.id.list_item_checkbox);
		txtName.setText(cursor.getString(cursor.getColumnIndex("name")));
		txtQuantity.setText("Qty: " + cursor.getInt(cursor.getColumnIndex("quantity")));
		txtPrice.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex("price"))) + label);
		txtTotal.setText("Total: "+ String.format("%.2f", cursor.getDouble(cursor.getColumnIndex("total"))) + label);
		
		short checked = cursor.getShort(cursor.getColumnIndex("checked"));
		checkbox.setChecked(checked == 1);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_row_layout,
				parent, false);
		bindView(view, context, cursor);
		return view;
	}

}
