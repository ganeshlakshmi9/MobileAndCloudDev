package ncsu.course.android.broadcastchat1.activities;

import ncsu.course.android.broadcastchat1.R;
import ncsu.course.android.broadcastchat1.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BroadcastProfile extends Activity{
	
	private static final String TAG = "Bprofile";
	private static final boolean D = true;
	
	private Button mButton;
	private SharedPreferences mPreferences;
	private EditText mTextfield;
	private String mProfileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
		
		setContentView(R.layout.profile_edit);
		setTitle(R.string.app_name);
		
		getProfile();
		
		if(D) Log.e(TAG, "+++ ON PROFILE SETUP +++");
		mTextfield = (EditText) findViewById(R.id.editTextProfile);
		mTextfield.setText(mProfileName);
		
		mButton = (Button) findViewById(R.id.edit_profile_button);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				SharedPreferences.Editor editor = mPreferences.edit();
				editor.putString(Constants.PROFILE_NAME, mTextfield.getText().toString()); // value to store
				editor.commit();
				
				Intent intent = new Intent();
	            setResult(RESULT_OK, intent);
	            finish();
			}
		});
	}

	@Override
	protected void onPause() {
		if(D) Log.e(TAG, "+++ ON PROFILE PAUSE +++");
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume() {
		if(D) Log.e(TAG, "+++ ON PROFILE RESUME +++");
		super.onResume();
		populateFields();
	}
	
	private void saveState(){
		String profname = mTextfield.getText().toString();
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(Constants.PROFILE_NAME, profname); // value to store
		editor.commit();
	}
	
	private void populateFields() {
        if (mProfileName != null) {
        	getProfile();
    		mTextfield.setText(mProfileName);
        }
    }
	
	private void getProfile(){
		mProfileName = mPreferences.getString(Constants.PROFILE_NAME, "Annon");
	}
	
}
