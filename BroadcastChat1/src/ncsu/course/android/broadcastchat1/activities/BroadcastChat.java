package ncsu.course.android.broadcastchat1.activities;

import ncsu.course.android.broadcastchat1.R;
import ncsu.course.android.broadcastchat1.model.UserMessage;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
/**
 * This is the main Activity that displays the current chat session.
 */
public class BroadcastChat extends Activity {
    // Debugging
    private static final String TAG = "BcastChat";
    private static final boolean D = true;
    
    // Key names received from the BroadcastChatService Handler
    public static final String TOAST = "toast";

    // Layout Views
    private ListView 	mConversationView;
    private EditText 	mOutEditText;
    private Button 		mSendButton;

    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    
    // Member object for the chat services
    private BroadcastChatService mChatService = null;
    
    // User information
    private String mUser = "Kristian";
    private String mRoom = "Global";
    
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
		                
		                mConversationArrayAdapter.add("Me:  " + userMsg);
	                }
	                else{
	                	mConversationArrayAdapter.add("Me:  " + writeBuf);
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
		                
		                mConversationArrayAdapter.add(user+": "+userMsg);
	                }
	                else{
	                	mConversationArrayAdapter.add("Unknown:  " + readBuf);
	                }
	                
	                break;               
	            case Constants.MESSAGE_TOAST:
	                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
	                               Toast.LENGTH_SHORT).show();
	                break;
            }
        }
    }; 
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName cn) {
			mChatService = null;
		}
		
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			mChatService = ((ServiceBinder)binder).getService();
			mChatService.registerHandler(mHandler);
		}
	};
	
	private Intent mServiceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        // Set up the window layout
        setContentView(R.layout.main);
        
        mServiceIntent = new Intent(this, BroadcastChatService.class);
        bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
        
    }

   @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        
        setupChat();
        mChatService.start();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        
        mChatService.start();
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if(D) Log.e(TAG, "[sendButton clicked]");
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(new UserMessage(mUser, mRoom, message));
            }
        });
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    
    
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }


    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
        
    }


    public void onDestroy() {
        super.onDestroy();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
        mChatService.stop();
    }
    
    public ArrayAdapter<String> getmConversationArrayAdapter() {
		return mConversationArrayAdapter;
	}

	/**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(UserMessage message) {
    	if(D) Log.e(TAG, "[sendMessage]");
        // Check that there's actually something to send
    	String temp = message.toString();
    	
        if (temp.length() > 0 ) {
            // Get the message bytes and tell the BluetoothChatService to write
            mChatService.write(temp);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }


    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(new UserMessage(mUser, mRoom, message));
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };
}