package com.androidmandetory;

import android.app.ListActivity;
import android.os.Bundle;

public class ItemList_Activity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);
    }
}