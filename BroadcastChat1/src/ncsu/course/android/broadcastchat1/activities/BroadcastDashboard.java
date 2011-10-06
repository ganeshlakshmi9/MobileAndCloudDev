package ncsu.course.android.broadcastchat1.activities;


import ncsu.course.android.broadcastchat1.R;
import ncsu.course.android.broadcastchat1.services.BroadcastChatService;
import ncsu.course.android.broadcastchat1.services.ServiceBinder;
import ncsu.course.android.broadcastchat1.util.Constants;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author kristiansejersen
 *
 * Main menu dashboard.
 */
public class BroadcastDashboard extends Activity{
	
	private static final String TAG = "Bdashboard";
	
	private static final int BUTTON_PROFILE=0;
	
    private static final boolean D = true;
    
	private Button mGlobalButton;
	private Button mProfileButton;
	private Button mCreateRoomButton;
	private Button mRoomsButton;
	
	private BroadcastChatService mChatService;
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName cn) {
			mChatService = null;
		}
		
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			mChatService = ((ServiceBinder)binder).getService();
			mChatService.registerHandler(mHandler);
		}
	};
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
        	if(D) Log.e(TAG, "[handleMessage !!!!!!!!!!!!]");
        	
            switch (msg.what) {
            
	            case Constants.MESSAGE_WRITE:
	            	String writeBuf = (String) msg.obj;
	                String[] resultsWrite = writeBuf.split(";");
	                
	                if(resultsWrite.length == 3){
		                String room 	= resultsWrite[1];
		                String userMsg  = resultsWrite[2];
		                
//		                mConversationArrayAdapter.add("Me:  " + userMsg);
	                }
	                else{
//	                	mConversationArrayAdapter.add("Me:  " + writeBuf);
	                }
	                
	                break;
	            case Constants.MESSAGE_READ:
	                String readBuf = (String) msg.obj;
	                String[] resultsRead = readBuf.split(";");
	                
	                
	                //TODO ROOM FILTERING
	                if(resultsRead.length == 3){
	                	String user 	= resultsRead[0];
		                String room 	= resultsRead[1];
		                String userMsg  = resultsRead[2];
		                
//		                mConversationArrayAdapter.add(user+": "+userMsg);
	                }
	                else{
//	                	mConversationArrayAdapter.add("Unknown:  " + readBuf);
	                }
	                
	                break;               
	            case Constants.MESSAGE_TOAST:
//	                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
//	                               Toast.LENGTH_SHORT).show();
	                break;
            }
        }
    }; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		if(D) Log.e(TAG, "+++ ON DASH CREATE +++");
		setupDashboard();
		
		Intent intent=new Intent(this, BroadcastChatService.class); 
//	    startService(intent);
	    
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(D) Log.e(TAG, "+++ ON DASH STOP +++");
		stopService(new Intent(this, BroadcastChatService.class));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(D) Log.e(TAG, "+++ ON DASH DESTROY +++");
	}
	
	private void setupDashboard(){
		
		if(D) Log.e(TAG, "+++ ON DASH SETUP +++");
      mGlobalButton = (Button) findViewById(R.id.imageButtonGlobal);
      mGlobalButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				if(D) Log.e(TAG, "+++ ON GLOBAL PRES +++");
				Intent i = new Intent(BroadcastDashboard.this, BroadcastChat.class);
		        startActivity(i);
			}
		});
      
      mProfileButton = (Button) findViewById(R.id.imageButtonProfile);
      mProfileButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				if(D) Log.e(TAG, "+++ ON PROFILE PRES +++");
				Intent i = new Intent(BroadcastDashboard.this, BroadcastProfile.class);
		        startActivityForResult(i, BUTTON_PROFILE);
			}
		});
      
      mCreateRoomButton = (Button) findViewById(R.id.imageButtonCreateRoom);
      mCreateRoomButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				if(D) Log.e(TAG, "+++ ON CREATE ROOM PRES +++");
				Intent i = new Intent(BroadcastDashboard.this, BroadcastCreateRoom.class);
		        startActivity(i);
			}
		});
      
      mRoomsButton = (Button) findViewById(R.id.imageButtonRooms);
      mRoomsButton.setOnClickListener(new OnClickListener() {
		
		public void onClick(View arg0) {
			if(D) Log.e(TAG, "+++ ON ROOMS PRES +++");
			Intent i = new Intent(BroadcastDashboard.this , BroadcastRooms.class);
	        startActivity(i);
		}
	});
	}
}
