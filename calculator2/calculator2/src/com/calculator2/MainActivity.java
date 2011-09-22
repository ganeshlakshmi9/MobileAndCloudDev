package com.calculator2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	EditText operand1;
	EditText operand2;
	Button btnAdd;
	TextView txtViewResult;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        operand1 = (EditText)findViewById(R.id.operand1);
        operand2 = (EditText)findViewById(R.id.operand2);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        txtViewResult = (TextView)findViewById(R.id.txtViewResult);
        OnClickListener ocl = new OnClickListener(){

			@Override
			public void onClick(View v) {
			   MainActivity.this.onClick(v);
				
			}};
		btnAdd.setOnClickListener(ocl);


        
        //Toast.makeText(this, "Put your message here", Toast.LENGTH_SHORT).show();
    
    }

	protected void onClick(View v) {
		
		Toast.makeText(this, "The add button is clicked", Toast.LENGTH_SHORT).show();
	    
		int number1 = Integer.parseInt(operand1.getText().toString());
		int number2 = Integer.parseInt(operand2.getText().toString());
		int result = number1 + number2;
		txtViewResult.setText("" + result);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mainmenu, menu);
            return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
            case R.id.menuNew:
                   Toast.makeText(this, "New selected", Toast.LENGTH_SHORT).show();                        
                    break;
            case R.id.menuAbout:
                  // put your code here
                    break;
            }
            return true;
    }
	
	boolean isLandscape()
	{
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();
		return orientation == 0;
		
	}
	
	
}