package ncsu.course.android.broadcastchat1.activities;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import ncsu.course.android.broadcastchat1.R;
import ncsu.course.android.broadcastchat1.model.UserMessage;
import ncsu.sourse.android.broadcastchat1.dao.BroadcastDbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BroadcastCreateRoom extends Activity {
	
	private Button button;
	
	private BroadcastDbAdapter mDbAdapter;

	private EditText textfield;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbAdapter = new BroadcastDbAdapter(this);
		mDbAdapter.open();
		
		setContentView(R.layout.createroom);
		
		textfield = (EditText) findViewById(R.id.create_room_editText);
		
		button = (Button) findViewById(R.id.createRoomBtn);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				UserMessage msg = new UserMessage("annon", textfield.getText().toString(), "", new Timestamp(GregorianCalendar.MILLISECOND));
				mDbAdapter.insertMessage(msg);
				
				Intent i = new Intent(BroadcastCreateRoom.this, BroadcastRooms.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mDbAdapter.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mDbAdapter.open();
	}
}
