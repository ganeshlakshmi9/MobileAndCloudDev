package com.androidmandetory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditItemActivity extends Activity{
	
	private EditText mItemName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mItemName = (EditText) findViewById(R.id.editText2);
		mItemName.isClickable();
	}
}
